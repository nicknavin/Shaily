package com.app.session.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.utility.Constant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class OTPActivity extends BaseActivity implements View.OnClickListener {
    private CustomTextView txtDone, txt_Resend, text_patientContact;
    String dial_cd = "", mobile_no = "", contact_no = "", otp = "", email = "", type = "";
    static CustomEditText edt_otp;
    Context context;
    ImageView imgBack;
    static boolean flag_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);
        context = this;
        if (getIntent().getStringExtra("email") != null) {
            contact_no = getIntent().getStringExtra("email");
        }
        if (getIntent().getStringExtra("mobile") != null) {
            contact_no = getIntent().getStringExtra("mobile");

        }

        initView();
    }

    public void initView() {

        text_patientContact = (CustomTextView) findViewById(R.id.text_patientContact);
        text_patientContact.setText(dial_cd + "-" + mobile_no);
        txt_Resend = (CustomTextView) findViewById(R.id.txt_Resend);
        txt_Resend.setOnClickListener(this);
        edt_otp = (CustomEditText) findViewById(R.id.edt_otp);
        edt_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (flag_otp) {
                    if (isConnectingToInternet(context)) {
                        if (isValid()) {
                            callSendOTP(getParam(), Urls.SEND_OTP);
                        }
                    } else {
                        showInternetPop(context);
                    }
                }

            }
        });
        txtDone = (CustomTextView) findViewById(R.id.txtDone);
        txtDone.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.txtDone:
                if (isConnectingToInternet(context)) {
                    if (isValid()) {

                            callCheckOTP(getParam(), Urls.CHECK_OTP);

                    }
                } else {
                    showInternetPop(context);
                }
//                startActivity(new Intent(context, RegistrationThirdActivity.class));
                break;
            case R.id.txt_Resend:
                if (isConnectingToInternet(context)) {

                    callSendOTP(getParamSendOTP(), Urls.SEND_OTP);

                } else {
                    showInternetPop(context);
                }
                break;

            default:
                break;
        }

    }

    private boolean isValid() {
        otp = edt_otp.getText().toString();
        if (otp.isEmpty()) {
            edt_otp.setError(context.getResources().getString(R.string.error_otp));
            return false;
        }
        return true;
    }

    private void callCheckOTP(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {


                    dismiss_loading();
                   Intent intent = new Intent(context, ResetPasswordActivity.class);
                   intent.putExtra("id",contact_no);
                    startActivity(intent);
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
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });

//            request.commonAPI(url, param, new RequestCallback() {
//
//                @Override
//                public void onSuccess(JSONObject js, String success) {
//                    dismiss_loading();
//                    Log.d(TAG, "JSON: " + js.toString());
//
//                }
//
//                @Override
//                public void onFailed(JSONObject js, String failed, int status) {
//                    dismiss_loading();
//                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
//                    {
//                        unAuthorized();
//                        showToast(failed);
//                    } else {
//                        MyDialog.iPhone(failed, context);
//                    }
//                }
//
//                @Override
//                public void onNull(JSONObject js, String nullp) {
//                    MyDialog.iPhone(nullp, context);
//                    dismiss_loading();
//                }
//
//                @Override
//                public void onException(JSONObject js, String exception) {
//                    MyDialog.iPhone(exception, context);
//                    dismiss_loading();
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParam() {
        Map<String, Object> params = new HashMap<>();

        params.put("mobile_no", contact_no);
        params.put("otp", otp);


        return params;
    }


    private void
    callSendOTP(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                    MyDialog.iPhone(getString(R.string.something_wrong), context);
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            MyDialog.iPhone(getString(R.string.something_wrong), context);
        }
    }

    private Map<String, Object> getParamSendOTP() {
        Map<String, Object> params = new HashMap<>();


        params.put("mobile_no", mobile_no);
        params.put("dial_cd", dial_cd);


        return params;
    }

    static MyDialog loadings;

    public void getSMSLoading() {

        loadingSMS(activity, "Waiting to automatically detect an SMS sent at " + contact_no + ".");

    }

    public void updateMessageBox(String msg) {

        dismiss_loading();

        String str = msg.replace("Your One Time Password (OTP) is ", "");
        String otp = str.replace(".", "");
//        String sms = msg.substring(0,6);
//        edt_varification.setText(sms);
        edt_otp.setText(otp);

        if (isConnectingToInternet(context)) {
            if (isValid()) {
                callCheckOTP(getParam(), Urls.CHECK_OTP);
            }
        } else {
            showInternetPop(context);
        }

    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(Constant.CUSTOME_BROADCAST));
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String msg = intent.getStringExtra("MSG");
            String str = msg.replace(" is your one time password(OTP) for phone verification.", "");
            String otp = str.replace(".", "");
//        String sms = msg.substring(0,6);
//        edt_varification.setText(sms);
            edt_otp.setText(otp);

            if (isConnectingToInternet(context)) {
                if (isValid()) {
                    callCheckOTP(getParam(), Urls.CHECK_OTP);
                }
            } else {
                showInternetPop(context);
            }
        }
    };


    static android.app.Dialog dd;


    public void loadingSMS(Activity act, String msg) {


        dd = new android.app.Dialog(act);
        try {

            //dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.custom_loading);
            dd.getWindow().setLayout(-1, -2);
            dd.setCancelable(false);
//            ((CustomTextView)dd.findViewById(R.id.tv_loading)).setVisibility(View.VISIBLE);
//            ((CustomTextView)dd.findViewById(R.id.tv_loading)).setText(msg);
            //((ProgressView)dd.findViewById(R.id.rey_loading)).setst

//            ((MyTextView)dd.findViewById(R.id.title)).setText(title);
//            ((MyTextView)dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dd.dismiss();
//                }
//            });
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static void dismiss_loading() {
//        if (dd != null && dd.isShowing()) {
//            dd.dismiss();
//        }
//    }
}
