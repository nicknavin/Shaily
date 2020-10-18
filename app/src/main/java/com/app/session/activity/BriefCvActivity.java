package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.BriefCvRegAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Brief_CV;
import com.app.session.model.Language;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BriefCvActivity extends BaseActivity implements View.OnClickListener {
    Context context;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CustomTextView txtBack, txtSkipp, txtNext;
    BriefCvRegAdapter briefCvAdapter;
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    String[] id;
    public ArrayList<Language> selectlanguageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_cv);
        context = this;
        initView();
    }

    private void initView() {


       selectlanguageList= DataPrefrence.getLanguage(context, Constant.LANGUAGE_SPEAK);

       for(int i =0;i<selectlanguageList.size();i++)
       {
           Language language=selectlanguageList.get(i);
           Brief_CV brief_cv = new Brief_CV();
           brief_cv.setBrief_cv("");
           brief_cv.setNative_name(language.getNative_name());
           brief_cv.setLanguage_cd(language.getLanguage_cd());
           brief_cvList.add(brief_cv);
       }



//        String lang_id = DataPrefrence.getPref(context, Constant.LANGUAGE_CD, "");
//        String lang_name = DataPrefrence.getPref(context, Constant.LANGUAGE_NAME, "");
//        String[] id = lang_id.split(",");
//        String[] name = lang_name.split(",");
//
//        for (int i = 0; i <id.length; i++) {
//            Brief_CV brief_cv = new Brief_CV();
//            brief_cv.setBrief_cv("");
//            brief_cv.setNative_name(name[i]);
//            brief_cv.setLanguage_cd(id[i]);
//            brief_cvList.add(brief_cv);
//        }

        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.brief_cv));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        txtSkipp = (CustomTextView) findViewById(R.id.txtSkipp);
        txtSkipp.setOnClickListener(this);
        txtNext = (CustomTextView) findViewById(R.id.txtNext);
        txtNext.setOnClickListener(this);
        txtBack = (CustomTextView) findViewById(R.id.txtBack);
        txtBack.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        briefCvAdapter = new BriefCvRegAdapter(context, brief_cvList, new ApiCallback() {
            @Override
            public void result(String x) {

            }
        });
        recyclerView.setAdapter(briefCvAdapter);


    }


    private String getBriefCV() {


        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < brief_cvList.size(); i++) {

                Brief_CV cv = brief_cvList.get(i);
                JSONObject object = new JSONObject();
                object.put("language_cd", cv.getLanguage_cd());
                if(cv.getBrief_cv()==null)
                {
                    object.put("brief_cv", " ");
                }
                else if(cv.getBrief_cv().isEmpty())
                {
                    object.put("brief_cv", " ");
                }
                else
                {
                    object.put("brief_cv", cv.getBrief_cv());
                }

                jsonArray.put(object);

            }

            jsonObject.put("brief", jsonArray);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.txtNext:
                if (isConnectingToInternet(context))
                {
                    DataPrefrence.setPref(context, Constant.BRIEF_CV,getBriefCV());
                    Intent intent = new Intent(context, RegistrationFirstActivity.class);
                    startActivity(intent);
                 //   callSignUp(getParamSignUp(), Urls.USER_REGISTER);

                   }
                else {
                    showInternetPop(context);
                }

                break;
            case R.id.txtSkipp:
                if (isConnectingToInternet(context)) {
                    brief_cvList = new ArrayList<>();
                    callSignUp();
                } else {
                    showInternetPop(context);
                }

                break;
            case R.id.txtBack:
                onBackPressed();
                break; case R.id.imgBack:
                onBackPressed();
                break;
        }

    }



    private void callSignUp()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callUserRegister(
                    DataPrefrence.getPref(context, Constant.EMAILID, ""),
                    DataPrefrence.getPref(context, Constant.USER_NAME, ""),
                    DataPrefrence.getPref(context, Constant.MOBILE_NO, ""),
                    DataPrefrence.getPref(context, Constant.PASSWORD, ""),
                    DataPrefrence.getPref(context, Constant.FULLNAME, ""),
                    DataPrefrence.getPref(context, Constant.COUNTRY_ID, ""),
                    DataPrefrence.getPref(context, Constant.IS_COMPANY, ""),
                    DataPrefrence.getPref(context, Constant.IS_CONSULTANT, ""),
                    DataPrefrence.getPref(context, Constant.LANGUAGE_CD, ""),
                    DataPrefrence.getPref(context, Constant.CATEGORY_CD,""),
                    DataPrefrence.getPref(context, Constant.GENDER,""),
                    DataPrefrence.getPref(context, Constant.BRIEF_CV,""),
                    DataPrefrence.getPref(context, Constant.PROFILE_IMAGE, "")
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }




    private void callSignUp(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();


                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                    MyDialog.iPhone(getString(R.string.something_wrong), context); }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamSignUp() {
        Map<String, Object> params = new HashMap<>();


        params.put("email_id", DataPrefrence.getPref(context, Constant.EMAILID, ""));
        params.put("user_id", DataPrefrence.getPref(context, Constant.USER_NAME, ""));
        params.put("mobile_no", DataPrefrence.getPref(context, Constant.MOBILE_NO, ""));
        params.put("user_password", DataPrefrence.getPref(context, Constant.PASSWORD, ""));
        params.put("user_name", DataPrefrence.getPref(context, Constant.FULLNAME, ""));
        params.put("country_cd", DataPrefrence.getPref(context, Constant.COUNTRY_ID, ""));
        params.put("is_company", DataPrefrence.getPref(context, Constant.IS_COMPANY, ""));
        params.put("consultant", DataPrefrence.getPref(context, Constant.IS_CONSULTANT, ""));
        params.put("language_cd", DataPrefrence.getPref(context, Constant.LANGUAGE_CD, ""));
        params.put("sub_category_cd", DataPrefrence.getPref(context, Constant.SUB_CATEGORY_CD, ""));
        params.put("brief_cv", getBriefCV());
        params.put("prefered_language", DataPrefrence.getPref(context, Constant.PREFERED_LANGUAGE,""));
        params.put("gender", DataPrefrence.getPref(context, Constant.GENDER,""));
        params.put("myBase64String", DataPrefrence.getPref(context, Constant.PROFILE_IMAGE, ""));

        return params;
    }


}
