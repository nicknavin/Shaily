package com.app.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Brief_CV;
import com.app.session.model.Root;
import com.app.session.model.UpdateBriefReq;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBriefActivity extends BaseActivity {

    CustomEditText edtBriefCv;
    CustomEditText edtBriefTitle;
    Brief_CV briefCvData;
    String title_cd="0",title_name="",brief_cv_detail="";
    UpdateBriefReq updateBriefReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brief);
         updateBriefReq =new UpdateBriefReq();
        initView();
    }

    private void initView()
    {

        edtBriefCv=(CustomEditText)findViewById(R.id.edtBriefCv);
        edtBriefTitle=(CustomEditText) findViewById(R.id.edtBriefTitle);


        if(getIntent().getParcelableExtra("DATA")!=null)
        {
            briefCvData=getIntent().getParcelableExtra("DATA");
            title_cd=briefCvData.getTitle_cd();
            title_name=briefCvData.getTitle_name();


            edtBriefCv.setText(briefCvData.getBrief_cv());
            edtBriefTitle.setText(briefCvData.getTitle_name());
        }



        ((CustomTextView)findViewById(R.id.txtSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation())
                {
                    callUpdateBriefCv();
                }
            }
        });

        ((ImageView)findViewById(R.id.imgCross)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void callUpdateBriefCv0()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.callUpdateBrief(userId,accessToken,edtBriefCv.getText().toString(),edtBriefTitle.getText().toString(),briefCvData.getLanguage_cd());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    dismiss_loading();
                    ResponseBody responseBody=response.body();
                    try {
                        String data=  responseBody.string();
                        try {

                            JSONObject js =new JSONObject(data);

                            showToast(js.getString("Message"));
                            if(js.getBoolean("Status")) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("TITLE",edtBriefTitle.getText().toString());
                                returnIntent.putExtra("DATA",edtBriefCv.getText().toString());
                                setResult(Constant.REQUEST_BRIEF, returnIntent);
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

                }
            });
        }
        else
        {
            showInternetConnectionToast();
        }
    }


    private void callUpdateBriefCv()
    {
        if (isConnectingToInternet(context))
        {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.reqUpdateBriefCv(accessToken,updateBriefReq);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dismiss_loading();
                            if(response.body()!=null)
                            {

                                try {
                                    ResponseBody root = response.body();
                                    String data =root.string();
                                    JSONObject object=new JSONObject(data);
                                    if(object.getInt("status")==200)
                                    {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("TITLE",edtBriefTitle.getText().toString());
                                        returnIntent.putExtra("DATA",edtBriefCv.getText().toString());
                                        setResult(Constant.REQUEST_BRIEF, returnIntent);
                                        finish();
                                    }

                                    System.out.println("ds");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            else
                            {

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





    private boolean validation()
    {
        title_name=edtBriefTitle.getText().toString();
       brief_cv_detail =edtBriefCv.getText().toString();

        if(edtBriefTitle.getText().toString().isEmpty())
        {
            edtBriefTitle.setError(context.getResources().getString(R.string.error));
            return false;
        }
        if(edtBriefCv.getText().toString().isEmpty())
        {
            edtBriefCv.setError(context.getResources().getString(R.string.error));
            return false;
        }

        updateBriefReq.setTitleName(title_name);
        updateBriefReq.setBriefCv(brief_cv_detail);
        updateBriefReq.setUserId(userId);
        updateBriefReq.setLanguage_id(briefCvData.getLanguage_id().get_id());

        return true;
    }
    //{"Status":true,"Message":"Sucessfully"}
    private void callUpdateBriefCV(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        if(js.getBoolean("Status"))
                        {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("TITLE",title_name);
                            returnIntent.putExtra("DATA",edtBriefCv.getText().toString());
                            setResult(Constant.REQUEST_BRIEF, returnIntent);
                            finish();
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

    private Map<String, Object> getParamUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("brief_cv", edtBriefCv.getText().toString());
        params.put("title_name", title_name);
        params.put("language_cd", briefCvData.getLanguage_cd());
        return params;
    }


    private void getBriefTitle()
    {
      Intent intent=new Intent(context,SearchAddTitleActivity.class);
        intent.putExtra("language_cd",briefCvData.getLanguage_cd());
        startActivityForResult(intent, Constant.REQUEST_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Constant.REQUEST_RESULT)
        {

            if (data != null)
            {

                title_name=data.getStringExtra("result");
                title_cd=data.getStringExtra("id");
                edtBriefTitle.setText(title_name);

            }

        }
    }
}
