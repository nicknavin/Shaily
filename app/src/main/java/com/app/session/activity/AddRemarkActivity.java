package com.app.session.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddRemarkActivity extends BaseActivity {

    Context context;
    CustomTextView txtSubmit,txtSave,txtdelete;
    CustomEditText edt_remark;
    String client_cd = "",data ="",remarks_id="0";
    String the_date = "";
//    ImageView imgRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remark);
        context = this;
        if (getIntent().getStringExtra("ID") != null) {
            client_cd = getIntent().getStringExtra("ID");
        }

        if (getIntent().getStringExtra("NAME") != null)
        {
            user_name = getIntent().getStringExtra("NAME");
        }
        if(getIntent().getStringExtra("DATA")!=null)
        {
            data=getIntent().getStringExtra("DATA");
        }
        if (getIntent().getStringExtra("REMARKS_ID") != null) {
            remarks_id = getIntent().getStringExtra("REMARKS_ID");
        }
        initView();
        getTime();
    }

    public String getTime() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("timeStamp " + timeStamp);
        return timeStamp;
    }

    private void initView() {
        txtSave=(CustomTextView)findViewById(R.id.txtSave);
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet(context)) {
                    if(!edt_remark.getText().toString().isEmpty()) {
                        callAddRemark(getParamAddRemark(), Urls.SET_REMARK);
                    }
                    else
                    {
                        showToast(context.getResources().getString(R.string.error_email));
                    }
                } else {
                    showInternetPop(context);
                }
            }
        });
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.remark));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtdelete=(CustomTextView) findViewById(R.id.txtdelete);
        txtdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRemoveLang(getParamRemoveLang(), Urls.DELETE_REMARK);
            }
        });
        edt_remark = (CustomEditText) findViewById(R.id.edt_remark);
        txtSubmit = (CustomTextView) findViewById(R.id.txtSubmit);
        txtSubmit.setVisibility(View.GONE);
        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet(context)) {
                    callAddRemark(getParamAddRemark(), Urls.SET_REMARK);
                } else {
                    showInternetPop(context);
                }
            }
        });

        edt_remark.setText(data);
    }


    private void callAddRemark(Map<String, Object> param, String url) {

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


    private Map<String, Object> getParamAddRemark() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("client_cd", client_cd);
        params.put("remarks_id", remarks_id);
        params.put("the_date", getTime());
        params.put("remark_type", "text");
        params.put("remark_text", edt_remark.getText().toString());


        return params;
    }

    private void callRemoveLang(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                  finish();

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
        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }


    private Map<String, Object> getParamRemoveLang() {
        Map<String, Object> params = new HashMap<>();

        String accessToken = DataPrefrence.getPref(context, Constant.ACCESS_TOKEN, "");
        String userId = DataPrefrence.getPref(context, Constant.USER_ID, "");
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("remarks_id", remarks_id);


        return params;
    }

}
