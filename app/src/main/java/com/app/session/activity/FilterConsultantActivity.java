package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.FilterConsultantAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.FilterConsultant;
import com.app.session.utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterConsultantActivity extends BaseActivity implements View.OnClickListener {
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Context context;
    String user_language = "", consultant_name = "", profession_code = "", language_cd = "", country_cd = "", user_type = "", profession_name = "", language_name = "";
    ArrayList<FilterConsultant> consultantArrayList = new ArrayList<>();
    FilterConsultantAdapter filterAdapter;
    CustomTextView txt_search_lang, txt_search_Country, txt_search_ConsultName;
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        context = this;

        if (getIntent().getStringExtra("ID") != null) {
            profession_code = getIntent().getStringExtra("ID");
        }
        if (getIntent().getStringExtra("LANG_ID") != null) {
            language_cd = getIntent().getStringExtra("LANG_ID");
        }

        if (getIntent().getStringExtra("NAME") != null) {
            profession_name = getIntent().getStringExtra("NAME");
        }

        if (is_company.equals("0") && is_consultant.equals("1")) {
            user_type = "consultant";
        } else if (is_company.equals("0") && is_consultant.equals("0")) {
            user_type = "user";
        } else {
            user_type = "company";
        }
        initView();
        if (isConnectingToInternet(context)) {
            callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);

        } else {
            showInternetPop(context);
        }
    }

    private void initView()
    {
        imgsearch = (ImageView) findViewById(R.id.imgsearch);
        imgsearch.setOnClickListener(this);
        txt_search_ConsultName = (CustomTextView) findViewById(R.id.txt_search_ConsultName);
        txt_search_ConsultName.setOnClickListener(this);
        txt_search_lang = (CustomTextView) findViewById(R.id.txt_search_lang);
        txt_search_lang.setOnClickListener(this);
        txt_search_Country = (CustomTextView) findViewById(R.id.txt_search_Country);
        txt_search_Country.setOnClickListener(this);

        ((CustomTextView) findViewById(R.id.header)).setText(profession_name);
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        filterAdapter = new FilterConsultantAdapter(context, consultantArrayList, user_type);
        recyclerView.setAdapter(filterAdapter);
    }

    private void callSearchConsultant(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();


                    try {
                        consultantArrayList = new ArrayList<>();
                        JSONArray array = js.getJSONArray("consultans_list_dt");

                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                FilterConsultant consultant = new FilterConsultant();
                                consultant.setUser_cd(object.getString("user_cd"));
                                //                            consultant.setEmail_address(object.getString("email_address"));
                                //                          consultant.setMobile_no(object.getString("dial_cd") + "-" + object.getString("mobile_no"));
                                //                            consultant.setMobile_no(object.getString("mobile_no"));
                                consultant.setUser_name(object.getString("user_name"));
                                consultant.setCv(object.getString("cv"));
                                consultant.setCountry_cd(object.getString("country_cd"));
                                //                            consultant.setCategory(object.getString("category"));
                                //                            consultant.setSub_category(object.getString("sub_category"));

                                //                            consultant.setLanguage_name(object.getString("language"));

                                if (object.getString("country_icon") != null) {
                                    consultant.setCountry_icon(object.getString("country_icon"));
                                }
                                if (object.getString("imageUrl") != null) {
                                    consultant.setImageUrl(object.getString("imageUrl"));
                                }
                                consultantArrayList.add(consultant);

                            }
                        } else {
                            MyDialog.iPhone(getString(R.string.data), context);
                        }
                        filterAdapter = new FilterConsultantAdapter(context, consultantArrayList, user_type);
                        recyclerView.setAdapter(filterAdapter);


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

    private Map<String, Object> getParamSearchConsultant() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("consultant_name", consultant_name);
        params.put("profession_code", profession_code);
        params.put("language_cd", language_cd);
        params.put("country_cd", country_cd);

        return params;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.txt_search_ConsultName:
//                intent = new Intent(context, SelectConsultantActivity.class);
//                intent.putExtra("ID", language_cd);
//                startActivityForResult(intent, Constant.PICK_CONSULTANT_NAME);
                break;
            case R.id.txt_search_lang:
                intent = new Intent(context, SelectRegistertLanguageActivity.class);
                startActivityForResult(intent, Constant.PICK_PREF_LANGUAGE);
                break;
            case R.id.txt_search_Country:
//                intent = new Intent(context, SelectConsultantCountryActivity.class);
//                startActivityForResult(intent, Constant.PICK_COUNTRY);
                break;
            case R.id.imgsearch:
//                intent=new Intent(context, FilterTabActivity.class);
////                intent = new Intent(context, FilterParameterActivity.class);
//                intent.putExtra("ID", profession_code);
//                intent.putExtra("LANG_ID", language_cd);
//                intent.putExtra("NAME", profession_name);
////                startActivity(intent);
//                startActivityForResult(intent, Constant.PAGE_REFRESH);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PAGE_REFRESH) {

            if(data!=null){
            int type = data.getIntExtra("TYPE", 0);
            if (type == Constant.PICK_COUNTRY) {
                String name = "", id = "";
                if (data.getStringExtra("NAME") != null) {
                    name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    id = data.getStringExtra("ID");
                }

                country_cd = id;
                if (isConnectingToInternet(context)) {
                    callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);

                } else {
                    showInternetPop(context);
                }
            } else if (type == Constant.PICK_PREF_LANGUAGE) {
                if (data.getStringExtra("NAME") != null) {
                    language_name="";
                    language_name = data.getStringExtra("NAME");

                }
                if (data.getStringExtra("ID") != null) {
                    language_cd="";
                    language_cd = data.getStringExtra("ID");
                }
                if (isConnectingToInternet(context)) {
                    callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);

                } else {
                    showInternetPop(context);
                }
            } else if (type == Constant.PICK_CONSULTANT_NAME) {
                if (data.getStringExtra("NAME") != null) {
                    consultant_name = data.getStringExtra("NAME");
                    showToast(consultant_name);
                }
                if (isConnectingToInternet(context)) {
                    callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);

                } else {
                    showInternetPop(context);
                }
            }}

        }
//        if (requestCode == Constant.PICK_COUNTRY) {
//            String name = "", id = "";
//            if (data != null) {
//                if (data.getStringExtra("NAME") != null) {
//                    name = data.getStringExtra("NAME");
//                }
//                if (data.getStringExtra("ID") != null) {
//                    id = data.getStringExtra("ID");
//                }
//
//                country_cd = id;
//                if (isConnectingToInternet(context)) {
//                    callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);
//
//                } else {
//                    showInternetPop(context);
//                }
//            }
//        }
//
//        if (requestCode == Constant.PICK_PREF_LANGUAGE) {
//
//            if (data != null) {
//                if (data.getStringExtra("NAME") != null) {
//                    language_name = data.getStringExtra("NAME");
//
//                }
//                if (data.getStringExtra("ID") != null) {
//                    language_cd = data.getStringExtra("ID");
//                }
//                if (isConnectingToInternet(context)) {
//                    callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);
//
//                } else {
//                    showInternetPop(context);
//                }
//            }
//
//
//        }
//        if (requestCode == Constant.PICK_CONSULTANT_NAME) {
//
//            if (data != null) {
//                if (data.getStringExtra("NAME") != null) {
//                    consultant_name = data.getStringExtra("NAME");
//                    showToast(consultant_name);
//                }
//                if (isConnectingToInternet(context)) {
//                    callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);
//
//                } else {
//                    showInternetPop(context);
//                }
//            }
//
//
//        }

    }


}
