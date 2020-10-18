package com.app.session.baseFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.activity.SplashActivity;
import com.app.session.base.BaseActivity;
import com.app.session.customview.MyDialog;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Methods;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class BaseFragment extends Fragment {

    public String loginType = "", accessToken = "", userId = "",lang="en",country_cd="",is_consultant ="",is_company="",user_name="";;
    public BaseActivity baseActivity;
    public String profileUrl="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginType = DataPrefrence.getPref(getContext(), Constant.LOGIN_TYPE, "");
        accessToken = "Bearer "+DataPrefrence.getPref(getContext(), Constant.ACCESS_TOKEN, "");
        userId = DataPrefrence.getPref(getContext(), Constant.USER_ID, "");
        country_cd= DataPrefrence.getPref(getContext(), Constant.COUNTRY_ID,"");
        baseActivity = (BaseActivity) this.getActivity();
        is_consultant = DataPrefrence.getPref(getContext(), Constant.IS_CONSULTANT, "");
        is_company = DataPrefrence.getPref(getContext(), Constant.IS_COMPANY, "");
        profileUrl = DataPrefrence.getPref(getContext(), Constant.PROFILE_IMAGE, "");
        user_name = DataPrefrence.getPref(getContext(), Constant.USER_NAME, "");
    }

    public void showInternetPop(Context context) {
        MyDialog.iPhone(context.getResources().getString(R.string.connection), context);
    }

    public void showToast(String x) {
        Toast.makeText(getContext(), x, Toast.LENGTH_SHORT).show();
    }

    public void showInternetConnectionToast() {
        Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
    }
    public Boolean isInternetConnected() {
        return Methods.isInternetConnected(getContext());
    }

    public void unAuthorized(Context context, Activity activity) {
        clearDataBase(context);
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        activity.finish();
    }

    public void clearDataBase(Context context) {
        DataPrefrence.setPref(context, Constant.EMAILID, "");
        DataPrefrence.setPref(context, Constant.PASSWORD, "");
        DataPrefrence.setPref(context, Constant.CONTACT_NO, "");
        DataPrefrence.setPref(context, Constant.PROFILE_IMAGE, "");
        DataPrefrence.setPref(context, Constant.USER_ID, "");
        DataPrefrence.setPref(context, Constant.ACCESS_TOKEN, "");
        DataPrefrence.setPref(context, Constant.VERIFICATION_CODE, "");
        DataPrefrence.setPref(context, Constant.LOGIN_FLAG, false);
        DataPrefrence.setPref(context, Constant.VERIFICATION_FLAG, false);

    }


    public android.app.Dialog dd;
    ProgressView progressDialog;

    public void showLoading() {

        System.out.println("baseFrg  start loading");

        if (dd != null) {
            dd.dismiss();
        }
        dd = new android.app.Dialog(baseActivity);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.custom_loading);
            progressDialog = (ProgressView) dd.findViewById(R.id.rey_loading);
            progressDialog.start();
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismiss_loading() {
        try {
            if (dd.isShowing() || dd != null) {
                if (progressDialog != null)
                    progressDialog.stop();
            }
            dd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void errorBody(Response<ResponseBody> response, boolean flag)
    {
        try {
            ResponseBody responseBody=null;
            if(flag) {
                responseBody = response.body();
            }else {
                responseBody = response.errorBody();
            }
            String data =responseBody.string();

            System.out.println("error1"+data);
            JSONObject jsonObject=new JSONObject(data);
            System.out.println("error2"+jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
