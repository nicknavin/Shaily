package com.app.session.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.network.BaseAsych;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateSubscriptionGroupDiscription extends BaseActivity {

    CustomEditText edt_cv;
    SubscriptionGroup group=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_subscription_group_discription);
        initView();
    }

    private void initView()
    {
        edt_cv=(CustomEditText)findViewById(R.id.edt_cv);
        if (getIntent().getParcelableExtra("DATA") != null)
        {

            group=getIntent().getParcelableExtra("DATA");
               edt_cv.setText(group.getGroup_desc());


        }
        ((ImageView)findViewById(R.id.imgCross)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((CustomTextView) findViewById(R.id.txtSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!edt_cv.getText().toString().isEmpty())
             {
                 callUpdateSubscriptionGroup();
             }
             else
             {
                 showToast(context.getResources().getString(R.string.error_desc));
             }
            }
        });


    }

    //{"rdescription":"update success","rstatus":"1","url":null}
    private void callUpdateSubscriptionGroup() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPADTE_SESSION_GROUP, getAddSubcriptionObj(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {

                        if (!js.getString("rstatus").equals("0"))
                        {
                        finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    showToast(failed);


                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            }).execute();

        } else {
            showInternetConnectionToast();
        }
    }


    private String getAddSubcriptionObj() {
        JsonObject gsonObject = null;
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_cd", userId);
            jsonObject.put("subscription_group_cd", group.getSubscription_group_cd());
            jsonObject.put("group_name", group.getGroup_name());
            jsonObject.put("language_cd", group.getLanguage_cd());
            jsonObject.put("category_cd", group.getCategory_cd());
            jsonObject.put("currency_cd", group.getCurrency_cd());
            jsonObject.put("subscription_price", group.getSubscription_price());
            jsonObject.put("group_desc", edt_cv.getText().toString());

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
