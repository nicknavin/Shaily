package com.app.session.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialogCallback;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends BaseActivity {
    Context context;
    CustomEditText edt_CurrentPwd, edt_NewPwd, edt_ConfrmPwd;
    CustomTextView txtchangPwd;
    CheckBox eye_old_pwd,eye_confirm_pwd,eye_new_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context = this;
        initView();
    }

    private void initView() {
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.change_pwd));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edt_CurrentPwd = (CustomEditText) findViewById(R.id.edt_CurrentPwd);
        edt_NewPwd = (CustomEditText) findViewById(R.id.edt_NewPwd);
        edt_ConfrmPwd = (CustomEditText) findViewById(R.id.edt_ConfrmPwd);
        txtchangPwd = (CustomTextView) findViewById(R.id.txtchangPwd);
        txtchangPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet(context)) {
                    callUpdatePwd(getParamUpdatePwd(), Urls.CHANG_PASSWORD);
                } else {
                    showInternetPop(context);
                }
            }
        });

        eye_old_pwd = (CheckBox) findViewById(R.id.eye_old_pwd);
        eye_old_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eye_old_pwd.isChecked()) {
                    edt_CurrentPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edt_CurrentPwd.setSelection(edt_CurrentPwd.length());
                } else {
                    edt_CurrentPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_CurrentPwd.setSelection(edt_CurrentPwd.length());   }
            }
        });

        eye_new_pwd = (CheckBox) findViewById(R.id.eye_new_pwd);
        eye_new_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eye_new_pwd.isChecked()) {
                    edt_NewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edt_NewPwd.setSelection(edt_NewPwd.length());
                } else {
                    edt_NewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_NewPwd.setSelection(edt_NewPwd.length());   }
            }
        });


        eye_confirm_pwd = (CheckBox) findViewById(R.id.eye_confirm_pwd);
        eye_confirm_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eye_confirm_pwd.isChecked()) {
                    edt_ConfrmPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edt_ConfrmPwd.setSelection(edt_ConfrmPwd.length());
                } else {
                    edt_ConfrmPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_ConfrmPwd.setSelection(edt_ConfrmPwd.length());   }
            }
        });

    }

private boolean validation()
{
    String pwd_new =edt_NewPwd.getText().toString();
    String pwd_confrm =edt_ConfrmPwd.getText().toString();
    if(edt_CurrentPwd.getText().toString().isEmpty())
    {
        edt_CurrentPwd.setError(context.getResources().getString(R.string.valid_pwd_old));
        return false;
    }
    if(pwd_new.isEmpty())
    {
        edt_NewPwd.setError(context.getResources().getString(R.string.valid_pwd_new));
        return false;
    }
    if(pwd_confrm.isEmpty())
    {
        edt_ConfrmPwd.setError(context.getResources().getString(R.string.valid_pwd_confirm));
        return false;
    }

    if(!pwd_new.equals(pwd_confrm))
    {
        showToast(context.getResources().getString(R.string.valid_pwd_match));
        return false;
    }
    return true;
}
    private void callUpdatePwd(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    MyDialogCallback.iPhone(context.getResources().getString(R.string.change_pwd_success), context, new ApiCallback() {
                        @Override
                        public void result(String x) {
                            finish();
                        }
                    });

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {
                        MyDialogCallback.iPhone(failed, context, new ApiCallback() {
                            @Override
                            public void result(String x) {

                            }
                        });
                    }
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

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

    private Map<String, Object> getParamUpdatePwd() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("current_password", edt_CurrentPwd.getText().toString());
        params.put("new_password", edt_NewPwd.getText().toString());
        return params;
    }


}
