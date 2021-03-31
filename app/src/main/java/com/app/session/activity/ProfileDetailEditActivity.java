package com.app.session.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.ProfileUpdateReq;
import com.app.session.data.model.Root;
import com.app.session.data.model.User;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserRoot;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailEditActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    CustomEditText edt_fullname;
    CustomTextView myView, edt_phone_number, edt_email,edt_selectCountry,txtUpdate_profile;
    String fullname = "";
    RadioGroup rg_group_gender;
    String gender;
    RadioButton rb_male,rb_female;
    ProfileUpdateReq profileUpdateReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail_edit);
        context= this;
        initView();
    }

    public void initView()
    {
        ((CustomTextView)findViewById(R.id.header)).setText(context.getResources().getString(R.string.profile_edit));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        edt_fullname = (CustomEditText) findViewById(R.id.edt_fullname);
        edt_email = (CustomTextView) findViewById(R.id.edt_email);
        edt_email.setOnClickListener(this);
        edt_phone_number = (CustomTextView) findViewById(R.id.edt_phone_number);
        edt_phone_number.setOnClickListener(this);
        edt_selectCountry = (CustomTextView) findViewById(R.id.edt_selectCountry);
        edt_selectCountry.setOnClickListener(this);
        txtUpdate_profile=(CustomTextView)findViewById(R.id.txtUpdate_profile);
        txtUpdate_profile.setOnClickListener(this);

        if (isConnectingToInternet(context)) {

//            getUserDetail();

            getUserDetailo();

        }

        rb_female=(RadioButton)findViewById(R.id.rb_female);
        rb_male=(RadioButton)findViewById(R.id.rb_male);
        rg_group_gender=(RadioGroup)findViewById(R.id.rg_group_gender);

        rg_group_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
                // This puts the value (true/false) into the variable

                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    gender=""+checkedRadioButton.getText().toString().charAt(0);


                }
            }
        });


//       gender= DataPrefrence.getPref(context, Constant.GENDER,"");
//       if(gender.equals("M"))
//       {
//           rb_male.setChecked(true);
//       }
//       else
//       {
//           rb_female.setChecked(true);
//       }


    }
    public void editable(CustomEditText edt, boolean flag) {
        edt.setCursorVisible(flag);
        edt.setClickable(flag);
        edt.setFocusable(flag);
        edt.setFocusableInTouchMode(flag);
    }
    private void callGetProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();


                    try {
                        JSONArray jsonArray = js.getJSONArray("User_Detail");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        edt_fullname.setText(jsonObject.getString("user_name"));
                        if(jsonObject.getString("user_name").length()>0)
                        {
                            edt_fullname.setSelection(jsonObject.getString("user_name").length());
                        }
                        edt_selectCountry.setText(jsonObject.getString("country_name"));
                        edt_phone_number.setText(jsonObject.getString("dial_cd") +"-"+ jsonObject.getString("mobile_no"));
                        edt_email.setText(jsonObject.getString("email_address"));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {

                    }
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                    MyDialog.iPhone(getString(R.string.something_wrong), context);         }

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

    private Map<String, Object> getParamProfile() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);

        return params;
    }


    private void getUserDetail()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.getUserDetailRequest(userId,accessToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {dismiss_loading();
                    ResponseBody responseBody=response.body();
                    try {
                        String data=  responseBody.string();
                        try {

                            JSONObject js =new JSONObject(data);

                            if(js.getBoolean("Status")) {
                                try {
                                    JSONArray jsonArray = js.getJSONArray("User_Detail");

                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    edt_fullname.setText(jsonObject.getString("user_name"));
                                    if(jsonObject.getString("user_name").length()>0)
                                    {
                                        edt_fullname.setSelection(jsonObject.getString("user_name").length());
                                    }
                                    edt_selectCountry.setText(jsonObject.getString("country_name"));
                                    edt_phone_number.setText(jsonObject.getString("dial_cd") +"-"+ jsonObject.getString("mobile_no"));
                                    edt_email.setText(jsonObject.getString("email_address"));



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
        }
        else
        {
            showInternetConnectionToast();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.edt_phone_number:
                dialogUpdateMobile();
                break;
            case R.id.edt_email:
                dialogUpdateEmail();
                break;
            case R.id.txtUpdate_profile:

                if (isConnectingToInternet(context)) {
                    if (validation())
                    {

                        callUserUpdateProfile();
                    }
                } else
                    {
                    showInternetConnectionToast();
                }

                break;
            case R.id.imgBack:
                onBackPressed();
                break;

        }
    }

    CustomEditText edt_cntryCode_new, edt_MobileNo_new, edt_otp;
    CustomTextView btn_cancelotp, btn_Send;
    LinearLayout layOtp, layMobile;
    CustomEditText edt_email_new;
    String otp = "", newMobile = "", newEmail = "";

    public void dialogUpdateMobile() {


        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.activity_chang_mobile_number);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);

            edt_cntryCode_new = (CustomEditText) dd.findViewById(R.id.edt_cntryCode_new);
            edt_MobileNo_new = (CustomEditText) dd.findViewById(R.id.edt_MobileNo_new);
            edt_otp = (CustomEditText) dd.findViewById(R.id.edt_otp);
            btn_cancelotp = (CustomTextView) dd.findViewById(R.id.btn_cancelotp);
            btn_Send = (CustomTextView) dd.findViewById(R.id.btn_Send);
            layMobile = (LinearLayout) dd.findViewById(R.id.layMobile);
            layOtp = (LinearLayout) dd.findViewById(R.id.layOtp);
            String dial_cd = DataPrefrence.getPref(context, Constant.DIAL_Code, "");
            edt_cntryCode_new.setText(dial_cd);


            btn_Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    otp = edt_otp.getText().toString();
                    if (isConnectingToInternet(context)) {
                        if (!otp.isEmpty()) {
                            callSendOTP(getParamSendOTP(otp, newMobile), Urls.SET_NEW_MOBILENO, dd);
                        } else {
                            edt_otp.setError(context.getResources().getString(R.string.error_otp));
                        }
                    } else {
                        showInternetPop(context);
                    }
                }
            });
            btn_cancelotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });

            ((CustomTextView) dd.findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    newMobile = edt_MobileNo_new.getText().toString();
                    if (newMobile.isEmpty()) {
                        edt_MobileNo_new.setError(context.getResources().getString(R.string.error_mobile));
                        return;
                    }
                    if (isConnectingToInternet(context)) {
                        callUpdatedMobileEmail(getParamUpdateMobile(newMobile), Urls.SENT_MAIL_CHANGE_NO);
                    } else {
                        showInternetPop(context);
                    }

                }
            });

            ((CustomTextView) dd.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
//                    brief_cv.setBrief_cv(edt_brief.getText().toString());
//                    brief_cvList.add(position,brief_cv);
//                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }

    public void dialogUpdateEmail() {


        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.activity_chang_email);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);

            edt_email_new = (CustomEditText) dd.findViewById(R.id.edt_email_new);
            edt_otp = (CustomEditText) dd.findViewById(R.id.edt_otp);
            btn_cancelotp = (CustomTextView) dd.findViewById(R.id.btn_cancelotp);
            btn_Send = (CustomTextView) dd.findViewById(R.id.btn_Send);
            layMobile = (LinearLayout) dd.findViewById(R.id.layMobile);
            layOtp = (LinearLayout) dd.findViewById(R.id.layOtp);


            ((CustomTextView) dd.findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    newEmail = edt_email_new.getText().toString();
                    if (newEmail.isEmpty()) {
                        edt_email_new.setError(context.getResources().getString(R.string.error_email));
                        return;
                    }
                    if (!Utility.emailValidator(newEmail)) {
                        edt_email_new.setError(context.getResources().getString(R.string.error_email_invalid));
                        return;
                    }
                    if (isConnectingToInternet(context)) {
                        callUpdatedMobileEmail(getParamUpdateEmail(newEmail), Urls.SENT_OTP_CHANGE_EMAILID);
                    } else {
                        showInternetPop(context);
                    }


                }
            });

            ((CustomTextView) dd.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();

                }
            });

            btn_Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    otp = edt_otp.getText().toString();
                    if (isConnectingToInternet(context)) {
                        if (!otp.isEmpty()) {
                            callSendOTP(getParamEmailSendOTP(otp, newEmail), Urls.SET_NEW_EMAILID, dd);
                        } else {
                            edt_otp.setError(context.getResources().getString(R.string.error_otp));
                        }
                    } else {
                        showInternetPop(context);
                    }
                }
            });

            btn_cancelotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    /*..............................................................................*/
    private void callSendOTP(Map<String, Object> param, String url, final Dialog dd) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    dd.dismiss();
                    callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {

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
//
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

    private Map<String, Object> getParamSendOTP(String otp, String mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_mobileno", mobile);
        params.put("otp", otp);

        return params;
    }

    private Map<String, Object> getParamEmailSendOTP(String otp, String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_emailid", email);
        params.put("otp", otp);

        return params;
    }
    /*..............................................................................*/


    /*..............................................................................*/
    private void callUpdatedMobileEmail(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    layOtp.setVisibility(View.VISIBLE);
                    layMobile.setVisibility(View.GONE);


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {
                        MyDialog.iPhone(failed, context);
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
//
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

    private Map<String, Object> getParamUpdateMobile(String mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_mobileno", mobile);

        return params;
    }

    private Map<String, Object> getParamUpdateEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_emailid", email);

        return params;
    }
    /*..............................................................................*/

    private boolean validation() {


        fullname = edt_fullname.getText().toString().trim();

        profileUpdateReq.setUser_name(fullname);
        profileUpdateReq.setUser_gender(gender);

        String namePattrn = "[a-zA-z]+([ '-][a-zA-Z]+)*";

        if (fullname.isEmpty()) {
            edt_fullname.setError(context.getResources().getString(R.string.error_name));

            return false;
        }
        if (!Pattern.matches(namePattrn, fullname) || fullname.length() < 3) {

            edt_fullname.setError(context.getResources().getString(R.string.error_name_invalid));

            return false;
        }




//        if (language_cd.isEmpty()) {
//            showToast(context.getResources().getString(R.string.lang_selection_valid));
//            return false;
//        }

        return true;
    }




    private void getUserDetailo() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            UserId user=new UserId();
            user.setUser_id(userId);
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<UserRoot> call = apiInterface.getUserDetails(accessToken, user);
            call.enqueue(new retrofit2.Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                    dismiss_loading();

                    if(response.body()!=null)
                    {
                        UserRoot root = response.body();
                        if(root.getStatus()==200)
                        {
                            User user=root.getUserBody().getUser();
                            edt_fullname.setText(user.getUser_name());
                            //   edt_selectCountry.setText(user.getcjsonObject.getString("country_name"));
                            edt_phone_number.setText(user.getMobile_no());
                            edt_email.setText(user.getEmail());

                            edt_selectCountry.setText(user.getCountry_id().getCountry_english());

                           gender= user.getGender();
                            if(user.getGender().toLowerCase().startsWith("m"))
                            {
                                rb_male.setChecked(true);
                                rb_female.setChecked(false);
                            }
                            else
                            {
                                rb_male.setChecked(false);
                                rb_female.setChecked(true);
                            }

                            profileUpdateReq=new ProfileUpdateReq();
                            profileUpdateReq.setUser_name(user.getUser_name());
                            profileUpdateReq.setUser_email(user.getEmail());
                            profileUpdateReq.setUser_id(userId);
                            profileUpdateReq.setUser_gender(user.getGender());
                            profileUpdateReq.setUser_mobile_no(user.getMobile_no());




                        }
                    }





                }

                @Override
                public void onFailure(Call<UserRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }





  private void callUserUpdateProfile()
  {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<Root> call = apiInterface.reqUpateUserProfile(accessToken,profileUpdateReq);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {
                    dismiss_loading();

                    if(response.body()!=null)
                    {
                        Root root=response.body();
                        if(response.body().getStatus()==200)
                        {

                                DataPrefrence.setPref(context, Constant.GENDER,gender);
                                DataPrefrence.setPref(context, Constant.USER_NAME,fullname);
                                Intent intent = new Intent();
                                setResult(Constant.PROFILE_REFRESH, intent);
                                finish();

                        }
                        else
                        {

                        }
                         showToast(root.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
dismiss_loading();
                }
            });

        } else {
            showInternetConnectionToast();
        }
    }
    private JsonObject getUserUpdateProfileParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("user_id",userId);
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
