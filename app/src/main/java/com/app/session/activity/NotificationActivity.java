package com.app.session.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.NotificationModule;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    CustomTextView btn_accept, btn_reject, txt_invitetion;
    boolean confirm;
    String company_cd = "", company_name = "";
    LinearLayout lay_invitation;
    ArrayList<NotificationModule> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnectingToInternet(context)) {
            callGetInvitation(getParamGetInvitation(), Urls.GET_INVITITION);
        } else {
            showInternetConnectionToast();
        }
    }

    private void initView() {
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.notifications));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        txt_invitetion = (CustomTextView) findViewById(R.id.txt_invitetion);
        lay_invitation = (LinearLayout) findViewById(R.id.lay_invitation);
        lay_invitation.setVisibility(View.GONE);
        btn_reject = (CustomTextView) findViewById(R.id.btn_reject);
        btn_reject.setOnClickListener(this);
        btn_accept = (CustomTextView) findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btn_accept:
                confirm = true;
                callApiAcceptReject();
                break;
            case R.id.btn_reject:
                confirm = false;
                callApiAcceptReject();
                break;
        }


    }

    public void callApiAcceptReject() {
        if (isConnectingToInternet(context)) {
            callInvitation(getParamInvitation(), Urls.INVITITION_CONFIRMATION);
        } else {
            showInternetConnectionToast();
        }
    }

    private void callInvitation(Map<String, Object> param, String url) {
        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

//                    try {
//                        JSONArray array=js.getJSONArray("company_invititon");
//                        JSONObject object=array.getJSONObject(0);
//                        company_cd  = object.getString("company_cd");
//                        company_name=object.getString("company_name");
//                        if(object.getBoolean("request"))
//                        {
//                            lay_invitation.setVisibility(View.GONE);
//                            txt_invitetion.setText(company_name+" "+context.getResources().getString(R.string.company_name));
//                        }
//                        else
//                        {
//                            lay_invitation.setVisibility(View.VISIBLE);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    finish();
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    lay_invitation.setVisibility(View.GONE);
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


        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamInvitation() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("confirm", confirm);
        params.put("company_cd", company_cd);


        return params;
    }

    private void callGetInvitation(Map<String, Object> param, String url) {
        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONArray array = js.getJSONArray("company_invititon");
                        if (array.length() > 0) {
                            JSONObject object = array.getJSONObject(0);
                            company_cd = object.getString("company_cd");
                            company_name = object.getString("company_name");
                            if (object.getString("request").equals("true")) {
                                lay_invitation.setVisibility(View.GONE);
                            } else {
                                lay_invitation.setVisibility(View.VISIBLE);
                                txt_invitetion.setText(company_name + " " + context.getResources().getString(R.string.company_name));
                            }
                        }
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


        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamGetInvitation() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);


        return params;
    }


    private void callGetAllNotification(Map<String, Object> param, String url) {
        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {
                        Type type = new TypeToken<ArrayList<NotificationModule>>() {
                        }.getType();
                        JSONArray jsonArray = new JSONArray(js.getString("remark_list"));

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


        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamAllNotification() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);


        return params;
    }

}
