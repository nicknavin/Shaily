package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotActivity extends BaseActivity implements View.OnClickListener {

    CustomEditText edt_email, edit_mobile;
    CustomTextView btn_forgot, txtBack;
    Context context;
    RadioButton radio_mobile, radio_email;
    String type = "", email = "", mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_layout);
        context = this;
        initView();
    }

    private void initView() {
        edt_email = (CustomEditText) findViewById(R.id.edit_email);
        edit_mobile = (CustomEditText) findViewById(R.id.edit_mobile);
        btn_forgot = (CustomTextView) findViewById(R.id.btn_forgot);
        btn_forgot.setOnClickListener(this);
        txtBack = (CustomTextView) findViewById(R.id.txtBack);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        radio_email = (RadioButton) findViewById(R.id.radio_email);
        radio_email.setOnClickListener(this);
        radio_mobile = (RadioButton) findViewById(R.id.radio_mobile);
        radio_mobile.setOnClickListener(this);

        if(radio_email.isChecked())
        {
            setVisible(edit_mobile,false);
            setVisible(edt_email,true);
        }
        else {
            setVisible(edit_mobile,true);
            setVisible(edt_email,false);
        }
    }


    public void setVisible(CustomEditText id, boolean flag)
    {
        id.setFocusableInTouchMode(flag);
        id.setFocusable(flag);
        id.setClickable(flag);
        id.setCursorVisible(flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_email:
                if (radio_email.isChecked()) {
                    radio_mobile.setChecked(false);
                    type = "email";
                    edit_mobile.setText(null);
                    setVisible(edit_mobile,false);
                    setVisible(edt_email,true);
                }

                break;
            case R.id.radio_mobile:
                if (radio_mobile.isChecked()) {
                    radio_email.setChecked(false);
                    type = "mobile";
                    edt_email.setText(null);
                    setVisible(edit_mobile,true);
                    setVisible(edt_email,false);
                }
                break;
            case R.id.btn_forgot:
                if (isConnectingToInternet(context)) {
                    if (validation()) {
                        if (type.equals("mobile")) {
                            callForgotPwd(getParam(mobile, type), Urls.FORGOT_PASSWORD);
                        } else {
                            callForgotPwd(getParam(email, type), Urls.FORGOT_PASSWORD);
                        }
                    }


                }

                break;
            case R.id.txtBack:
                onBackPressed();
                break;

        }
    }

    private boolean validation() {
        email = edt_email.getText().toString();
        mobile = edit_mobile.getText().toString();
        if (email.isEmpty() && mobile.isEmpty()) {
            showToast(context.getResources().getString(R.string.error_forgot));
            return false;
        }

        return true;
    }
//{"Status":true,"Message":"OTP Sent on your register mobile no.","user_dt":[{"mobile_no":"7415912174","dial_cd":"+91","email_address":"nimade.navin@gmail.com","user_name":"navin nimade"}]}
       private void callForgotPwd(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    try {
                        dismiss_loading();
                        JSONArray array=js.getJSONArray("user_dt");
                        JSONObject object=array.getJSONObject(0);
                        String mobile = object.getString("mobile_no");
                        String dial_cd=object.getString("dial_cd");
                        String email_address=object.getString("email_address");
                        Intent intent= new Intent(context,OTPActivity.class);
                        if(type.equals("mobile")) {
                            intent.putExtra("mobile", dial_cd+mobile);

                        }
                        else
                        {
                            intent.putExtra("email",email_address);
                        }

                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    private Map<String, Object> getParam(String id, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", id);
        params.put("type", type);
        return params;
    }
}
