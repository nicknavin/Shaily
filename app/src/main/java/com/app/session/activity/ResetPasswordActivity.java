package com.app.session.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends BaseActivity {

    Context context;
    CustomEditText edt_new_pwd, edt_cnfm_pwd;
    CustomTextView btn_done;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        context=this;
        if(getIntent().getStringExtra("id")!=null)
        {
            id=getIntent().getStringExtra("id");
        }
        initView();

    }

    private void initView() {
        edt_new_pwd = (CustomEditText) findViewById(R.id.edt_new_pwd);
        edt_cnfm_pwd = (CustomEditText) findViewById(R.id.edt_cnfm_pwd);
        btn_done=(CustomTextView)findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnectingToInternet(context))
                {
                    if(validation())
                    {
                        callNewPwd(getParam(), Urls.SET_NEW_PASSWORD);
                    }
                }
                else
                {
                    showInternetPop(context);
                }
            }
        });
    }
String new_pwd="",cnfm_pwd="";
    private boolean validation()
    {
        new_pwd=edt_new_pwd.getText().toString();
        cnfm_pwd=edt_cnfm_pwd.getText().toString();
        if(new_pwd.isEmpty())
        {
            edt_new_pwd.setError(context.getResources().getString(R.string.error_pwd_new));
            return false;
        }
        if(cnfm_pwd.isEmpty())
        {
            edt_cnfm_pwd.setError(context.getResources().getString(R.string.error_pwd_cnfm));
            return false;
        }

        if(!new_pwd.equals(cnfm_pwd))
        {
            edt_cnfm_pwd.setError(context.getResources().getString(R.string.error_confirm_pwd_invalid));
            return false;
        }

        return true;
    }
    private void callNewPwd(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success)
                {

                    dismiss_loading();
                    finish();

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    MyDialog.iPhone(failed, context);
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                    MyDialog.iPhone(context.getResources().getString(R.string.something_wrong), context);
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            dismiss_loading();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParam() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", id);
        params.put("new_password", new_pwd);
        return params;
    }
}
