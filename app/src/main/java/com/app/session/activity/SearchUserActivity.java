package com.app.session.activity;

import android.content.Context;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.adapter.ChattingTabAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Consultant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserActivity extends BaseActivity implements View.OnClickListener {

    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ChattingTabAdapter chatingTabAdapter;
    ArrayList<Consultant> consultantArrayList = new ArrayList<>();
    private ArrayList<Consultant> searchConsultantArrayList = new ArrayList<>();
    String user_type = "";
    CustomEditText edtSearch;
    ImageView imgSearch,imgBack,imgCross;
    LinearLayout lay_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        context = this;
        initView();
    }

    private void initView() {


        imgBack=(ImageView) findViewById(R.id.imgBack);
        imgCross=(ImageView) findViewById(R.id.imgCross);
        imgSearch =(ImageView) findViewById(R.id.imgsearch);
        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgCross.setOnClickListener(this);

        if (is_company.equals("0") && is_consultant.equals("1")) {
            user_type = "consultant";
        } else if (is_company.equals("0") && is_consultant.equals("0")) {
            user_type = "user";
        } else {
            user_type = "company";
        }
        lay_search=(LinearLayout)findViewById(R.id.lay_search);
        edtSearch = (CustomEditText) findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        chatingTabAdapter = new ChattingTabAdapter(context, consultantArrayList, user_type);
        recyclerView.setAdapter(chatingTabAdapter);
        if (isConnectingToInternet(context)) {
            callGetConsultant(getParamSubCateg(), Urls.GET_CONULTANT);
        } else {
            showInternetPop(context);
        }


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String key = s.toString().trim();


                if (key.length() == 0) {
                    refresh();
                } else {
                    searchCountry("" + key);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void callGetConsultant(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {
                        JSONArray jsonArray = js.getJSONArray("Consultant");
                        Type type = new TypeToken<ArrayList<Consultant>>() {
                        }.getType();
                        consultantArrayList = new Gson().fromJson(jsonArray.toString(), type);
                        chatingTabAdapter = new ChattingTabAdapter(context, consultantArrayList, user_type);
                        recyclerView.setAdapter(chatingTabAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();

                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();
                        showToast(failed);
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
            MyDialog.iPhone(getString(R.string.something_wrong), context);
        }
    }

    private Map<String, Object> getParamSubCateg() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);

        return params;
    }


    private void refresh() {
        if (consultantArrayList.size() > 0) {
            chatingTabAdapter = new ChattingTabAdapter(context, consultantArrayList, user_type);
            recyclerView.setAdapter(chatingTabAdapter);
        }
    }

    private void searchCountry(String key) {

        searchConsultantArrayList = new ArrayList<>();
        if (consultantArrayList.size() > 0) {

            for (Consultant consultant : consultantArrayList) {
                String countryName = consultant.getUser_name().toLowerCase();
                if (key.length() < countryName.length()) {
                    String ss = countryName.substring(0, key.length());
                    if (key.toLowerCase().startsWith(ss)) {
                        searchConsultantArrayList.add(consultant);

                    }
                }
            }


            if (searchConsultantArrayList.size() > 0) {
                chatingTabAdapter = new ChattingTabAdapter(context, searchConsultantArrayList, user_type);
                recyclerView.setAdapter(chatingTabAdapter);
                chatingTabAdapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgsearch:

                imgSearch.setVisibility(View.GONE);
                lay_search.setVisibility(View.VISIBLE);
                edtSearch.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));

                break;

            case R.id.imgCross:

                edtSearch.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
                lay_search.setVisibility(View.GONE);
                imgSearch.setVisibility(View.VISIBLE);

                break;
        }
    }
}
