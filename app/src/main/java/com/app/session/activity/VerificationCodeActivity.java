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
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Brief_CV;
import com.app.session.model.Language;
import com.app.session.model.Root;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends BaseActivity implements View.OnClickListener {
    private CustomTextView txtDone, txt_Resend, text_patientContact;
    String country_code = "", mobile_no = "", contact_no = "", otp = "";
    static TextInputEditText edt_otp;
    Context context;
    ImageView imgBack;
    static boolean  flag_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        context = this;
        country_code = DataPrefrence.getPref(context, Constant.COUNTRY_CODE, "");
        mobile_no = DataPrefrence.getPref(context, Constant.MOBILE_NO, "");
        contact_no = country_code + mobile_no;
//        getSMSLoading();
        initView();
        initBrief();
    }

    public void initView() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.verification));
        text_patientContact = (CustomTextView) findViewById(R.id.text_patientContact);
        text_patientContact.setText( country_code + "-" + mobile_no);
        txt_Resend = (CustomTextView) findViewById(R.id.txt_Resend);
        txt_Resend.setOnClickListener(this);
        edt_otp = (TextInputEditText) findViewById(R.id.edt_otp);

        edt_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(flag_otp)
                {
                    if (isConnectingToInternet(context)) {
                        if (isValid()) {
                          //  callSendOTP(getParam(), Urls.SEND_OTP);
                            callSendOTP();
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
                    //if (isValid())
                    {
                        //callCheckOTP(getParam(), Urls.CHECK_OTP);
//                        callChekcOTP();
                        callRegistration();
                    }
                } else {
                    showInternetPop(context);
                }
//                startActivity(new Intent(context, RegistrationThirdActivity.class));
                break;
            case R.id.txt_Resend:
                if (isConnectingToInternet(context)) {

                       // callSendOTP(getParamSendOTP(), Urls.SEND_OTP);
                        callSendOTP();

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
                //callSignUp(getParamSignUp(), Urls.USER_REGISTER);




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
        params.put("dial_cd", country_code);


        return params;
    }

    private void callSendOTP() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callSendOTP(mobile_no, country_code);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();


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
    private void callChekcOTP() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callCheckOTP(contact_no,otp);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        //[text={"Status":true,"Message":"OTP Validation Sucessfully"}]
                        try {
                            String data = responseBody.string();
                            JSONObject jsonObject=new JSONObject(data);
                            if(jsonObject.getBoolean("Status"))
                            {
                                showToast(jsonObject.getString("Message"));
                                callRegistration();
                            }
                            else
                            {
                                showToast(jsonObject.getString("Message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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



    static MyDialog loadings;

    public void getSMSLoading() {

        loadingSMS(activity, "Waiting to automatically detect an SMS sent at "+ contact_no+ ".");

    }

    public  void updateMessageBox(String msg) {

        dismiss_loading();

        String str = msg.replace("Your One Time Password (OTP) is ", "");
        String otp = str.replace(".","");
//        String sms = msg.substring(0,6);
//        edt_varification.setText(sms);
        edt_otp.setText(otp);

        if (isConnectingToInternet(context))
        {
            if (isValid()) {
               // callCheckOTP(getParam(), Urls.CHECK_OTP);
                callChekcOTP();
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
                String otp = str.replace(".","");
    //        String sms = msg.substring(0,6);
    //        edt_varification.setText(sms);
                edt_otp.setText(otp);

                if (isConnectingToInternet(context))
                {
                    if (isValid()) {
//                        callCheckOTP(getParam(), Urls.CHECK_OTP);
                        callChekcOTP();
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



    private void callSignUp(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success)
                {
                    dismiss_loading();

//
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
                    {
                        unAuthorized();

                    } else {
                        MyDialog.iPhone(failed, context);
                    }      }

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
        params.put("category_cd", DataPrefrence.getPref(context, Constant.CATEGORY_CD,""));
        //params.put("prefered_language",DataPrefrence.getPref(context,Constant.PREFERED_LANGUAGE,""));
        params.put("gender", DataPrefrence.getPref(context, Constant.GENDER,""));
        params.put("brief_cv", DataPrefrence.getPref(context, Constant.BRIEF_CV,""));
        params.put("myBase64String", DataPrefrence.getPref(context, Constant.PROFILE_IMAGE, ""));

        return params;
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
                    DataPrefrence.getPref(context, Constant.BRIEF_CV,getBriefCV()),
                    DataPrefrence.getPref(context, Constant.PROFILE_IMAGE, "")
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {
                            JSONObject jsonObject=new JSONObject(data);
                            if(jsonObject.getBoolean("Status"))
                            {
                             Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {

                            }
                            showToast(jsonObject.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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


    public ArrayList<Language> selectlanguageList = new ArrayList<>();
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    private void initBrief()
    {

        selectlanguageList= DataPrefrence.getLanguage(context, Constant.LANGUAGE_SPEAK);

        for(int i =0;i<selectlanguageList.size();i++)
        {
            Language language=selectlanguageList.get(i);
            Brief_CV brief_cv = new Brief_CV();
            brief_cv.setBrief_cv("");
            brief_cv.setNative_name(language.getName());
            brief_cv.setLanguage_cd(language.get_id());
            brief_cvList.add(brief_cv);
        }
    }

    private String  getBriefCV() {


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



    private void callRegistration()
    {
        ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
        Call<Root> call= apiInterface.userRegistration(getRegParameter());
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response)
            {
                if(response!=null)
                {
                    Root responseBody= response.body();
                    if(responseBody.getStatus()==200) {

                        showToast(responseBody.getMessage());
                        Intent intent;
                        intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }


                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });
    }


    private JsonObject getRegParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("email",DataPrefrence.getPref(context, Constant.EMAILID, ""));
            jsonObject.put("login_user_id", DataPrefrence.getPref(context, Constant.USER_NAME, ""));
            jsonObject.put("user_password",DataPrefrence.getPref(context, Constant.PASSWORD, ""));
            jsonObject.put("mobile_no",DataPrefrence.getPref(context, Constant.MOBILE_NO, ""));
            jsonObject.put("user_name",DataPrefrence.getPref(context, Constant.FULLNAME, ""));
            jsonObject.put("country_id",DataPrefrence.getPref(context, Constant.COUNTRY_ID, ""));
            jsonObject.put("gender",DataPrefrence.getPref(context, Constant.GENDER,""));
            jsonObject.put("is_consultant",DataPrefrence.getPref(context, Constant.IS_CONSULTANT, ""));
            jsonObject.put("status_id","5f22e1a2dcbf027af6a1f7c5");
            jsonObject.put("mail_verfy","1");



            JSONArray jsonArrayLang=new JSONArray();





            for(int k=0;k<selectlanguageList.size();k++)
            {
                JSONObject obj=new JSONObject();
                String id =selectlanguageList.get(k).get_id();
                jsonArrayLang.put(id);
            }

            jsonObject.put("userLangauges",jsonArrayLang);
            JSONArray arrayBrief =new JSONArray();
            for (int i = 0; i < brief_cvList.size(); i++) {

                Brief_CV cv = brief_cvList.get(i);
                JSONObject object = new JSONObject();
                object.put("language_id", cv.getLanguage_cd());
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

                arrayBrief.put(object);

            }
            jsonObject.put("briefCV",arrayBrief);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }



}
