package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.model.LoginBody;
import com.app.session.model.LoginRoot;
import com.app.session.model.LoginUser;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;



import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    CustomTextView txt_Forgot, txt_Signup,btn_skip;
    TextInputEditText edt_email, edt_pwd;
    CustomTextView btn_login;
    Context context;
    String pwd = "", email = "";
    String fcmId = "";

   RelativeLayout lay_bottem;
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        context = this;

        initView();
    }

    private void initView() {
        lay_bottem = (RelativeLayout) findViewById(R.id.lay_bottem);
        txt_Forgot = (CustomTextView) findViewById(R.id.txt_forgot);
        txt_Forgot.setOnClickListener(this);
        txt_Signup = (CustomTextView) findViewById(R.id.txt_singup);
        txt_Signup.setOnClickListener(this);
        btn_login = (CustomTextView) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_skip = (CustomTextView) findViewById(R.id.btn_skip);
        btn_skip.setOnClickListener(this);
        edt_email = (TextInputEditText) findViewById(R.id.edit_email);
        edt_pwd = (TextInputEditText) findViewById(R.id.edit_password);

    }

    private boolean isValid() {
        email = edt_email.getText().toString();
        pwd = edt_pwd.getText().toString();

        if (email.isEmpty()) {
            edt_email.setError(context.getResources().getString(R.string.error_email));
            return false;
        }

        if (pwd.isEmpty()) {
            edt_pwd.setError(context.getResources().getString(R.string.error_pwd));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_skip:
                startActivity(new Intent(getApplicationContext(), HomeSessionActivity.class));
                break;

            case R.id.txt_singup:
                if(isConnectingToInternet(context))
                {
                    startActivity(new Intent(context, SelectLanguageYouSpeakActivity.class));
                }
                else
                {
                    showInternetPop(context);
                }
                break;
            case R.id.txt_forgot:
                startActivity(new Intent(context, ForgotActivity.class));
                break;
            case R.id.btn_login:
                if (isConnectingToInternet(context)) {
                    if (isValid())
                    {
                      //  new RegisterGCMId().execute();
//                        callUserLogin(getParam(), Urls.USER_LOGIN);
//                        callLoging();
                        callUserLogin();
                    }
                } else {
                    showInternetPop(context);
                }
//                startActivity(new Intent(context, HomeActivityold.class));
                break;
            default:
                break;

        }
    }


    private void callLoging()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.callLoginRequest(email,pwd,fcmId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    dismiss_loading();
                    ResponseBody responseBody=response.body();
                    try {
                        String data=  responseBody.string();
                        try {
                            JSONObject js=new JSONObject(data);

                            showToast(js.getString("Message"));
                            if (js.getBoolean("Status")) {
                                JSONArray array = js.getJSONArray("User_Detail");
                                JSONObject jsonObject = array.getJSONObject(0);
                                DataPrefrence.setPref(context, Constant.LOGIN_FLAG, true);
                                DataPrefrence.setPref(context, Constant.USER_ID, jsonObject.getString("user_cd"));
                                DataPrefrence.setPref(context, Constant.EMAILID, jsonObject.getString("email_address"));
                                DataPrefrence.setPref(context, Constant.USER_NAME, jsonObject.getString("login_user_id"));
                                DataPrefrence.setPref(context, Constant.DIAL_Code, jsonObject.getString("dial_cd") );
                                DataPrefrence.setPref(context, Constant.MOBILE_NO, jsonObject.getString("dial_cd") + jsonObject.getString("mobile_no"));
                                DataPrefrence.setPref(context, Constant.FULLNAME, jsonObject.getString("user_name"));
                                DataPrefrence.setPref(context, Constant.COUNTRY_ID, jsonObject.getString("country_cd"));
                                DataPrefrence.setPref(context, Constant.ACCESS_TOKEN, jsonObject.getString("token_id"));
                                DataPrefrence.setPref(context, Constant.PROFILE_IMAGE, jsonObject.getString("imageUrl"));
                                System.out.println(jsonObject.getBoolean("is_language"));
                                DataPrefrence.setPref(context, Constant.LANGUAGE_SELECTED, jsonObject.getBoolean("is_language"));
                                DataPrefrence.setPref(context, Constant.CATEGORY_SELECTED, jsonObject.getBoolean("is_catecory"));
                                if (jsonObject.getString("is_company").equals("0") && jsonObject.getString("is_consultant").equals("0"))
                                {
                                    DataPrefrence.setPref(context, Constant.LOGIN_TYPE, Constant.CLIENT);
                                } else
                                {
                                    DataPrefrence.setPref(context, Constant.LOGIN_TYPE, Constant.CONSULTANT);
                                }
                                DataPrefrence.setPref(context, Constant.IS_COMPANY, jsonObject.getString("is_company"));
                                DataPrefrence.setPref(context, Constant.IS_CONSULTANT, jsonObject.getString("is_consultant"));


                                Intent intent;
//                                if(DataPrefrence.getPref(context,Constant.IS_COMPANY,"").equals("1"))
//                                {
//                                    //intent = new Intent(context, CompanyHomeActivity.class);
//                                }else {
//                                    intent = new Intent(context, SnapchatActivity.class);
//                                }
                                intent = new Intent(context, ConsultantStoryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();


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

    private void callUserLogin()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<LoginRoot> call= apiInterface.mylogin(getParameter());
            call.enqueue(new Callback<LoginRoot>() {
                @Override
                public void onResponse(Call<LoginRoot> call, Response<LoginRoot> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        LoginRoot responseBody= response.body();
                        if(responseBody.getStatus()==200)
                        {
                            LoginBody loginBody = responseBody.getLoginBody();
                            LoginUser loginUser= loginBody.getLoginUser();
                            DataPrefrence.setPref(context, Constant.LOGIN_FLAG, true);
                            DataPrefrence.setPref(context, Constant.USER_ID, loginUser.get_id());
                            DataPrefrence.setPref(context,Constant.LOGIN_USER_ID,loginUser.getLogin_user_id());
                            DataPrefrence.setPref(context, Constant.EMAILID, loginUser.getEmail());
                            DataPrefrence.setPref(context, Constant.USER_NAME, loginUser.getUser_name());
                            DataPrefrence.setPref(context, Constant.MOBILE_NO, loginUser.getMobile_no());
                            DataPrefrence.setPref(context, Constant.COUNTRY_ID, loginUser.getCountry_id());
                            DataPrefrence.setPref(context, Constant.ACCESS_TOKEN,loginBody.getToken() );
                            DataPrefrence.setPref(context, Constant.PROFILE_IMAGE, loginUser.getImageUrl());
                            if(loginUser.getUserLangauges().size()>0)
                            {
                                DataPrefrence.setPref(context, Constant.LANGUAGE_SELECTED, true);
                            }
                            else
                            {
                                DataPrefrence.setPref(context, Constant.LANGUAGE_SELECTED, false);
                            }

//                            if(loginUser.getUserCategory().size()>0)
//                            {
//                                DataPrefrence.setPref(context, Constant.CATEGORY_SELECTED, true);
//                            }
//                            else
//                            {
//                                DataPrefrence.setPref(context, Constant.CATEGORY_SELECTED, false);
//                            }

                            if(loginUser.getIs_consultant().equals("1"))
                            {
                                DataPrefrence.setPref(context, Constant.LOGIN_TYPE, Constant.CONSULTANT);
                                DataPrefrence.setPref(context,Constant.IS_CONSULTANT,"1");
                            }
                            else
                            {
                                DataPrefrence.setPref(context, Constant.LOGIN_TYPE, Constant.CLIENT);
                            }
                            connectWithSocket();
                            Intent intent;
                            intent = new Intent(context, HomeUserChatProfileActivity.class);
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
                        ResponseBody responseBody=response.errorBody();
                        try {
                            String data =responseBody.string();
                            JSONObject jsonObject=new JSONObject(data);
                            showToast(jsonObject.getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginRoot> call, Throwable t) {

                }
            });
        }
        else
        {
            showInternetConnectionToast();
        }
    }


    private JsonObject getParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("login_user_id",email);
            jsonObject.put("password",pwd);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }

    private Boolean isConnected = true;
    public void connectWithSocket()
    {
        try {
            mSocket = IO.socket(Constant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.connect();
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mSocket.emit("join", accessToken);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(!isConnected) {
//                        if(null!=accessToken)
//                            mSocket.emit("join", accessToken);
////                        Toast.makeText(getActivity().getApplicationContext(),
////                                R.string.connect, Toast.LENGTH_LONG).show();
//                        showToast("connect");
//                        isConnected = true;
//                    }
//                }
//            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
//            runOnUiThread(new Runnable()
//            {
//                @Override
//                public void run() {
//                    Log.i(TAG, "diconnected");
//                    System.out.println("DATA"+args[0]);
//                    isConnected = false;
//                    showToast("diconnected");
////                    Toast.makeText(getActivity().getApplicationContext(),
////                            R.string.disconnect, Toast.LENGTH_LONG).show();
//                }
//            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e(TAG, "Error connecting");
//                    showToast("Error connecting");
////                    Toast.makeText(getActivity().getApplicationContext(),
////                            R.string.error_connect, Toast.LENGTH_LONG).show();
//                }
//            });
        }
    };





}
