package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.SubCategoryAdapters;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.SubCategoryModel;
import com.app.session.utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectSubCategory extends BaseActivity implements View.OnClickListener {
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Context context;
    CustomTextView btnDone;
    CustomEditText edtSearch;

    private SubCategoryAdapters subCategoryAdapters;


    private ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>();
    private ArrayList<SubCategoryModel> searchSubCategoryList = new ArrayList<>();

    ImageView imgBack;
    String lang = "en";
    String categoryId = "", categoryName = "", sub_categoryId = "", sub_categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_category);
        context = this;
        if (getIntent().getStringExtra("ID") != null) {
            categoryId = getIntent().getStringExtra("ID");
        }
        initView();

    }


    private void initView() {

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnDone = (CustomTextView) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        subCategoryAdapters = new SubCategoryAdapters(context, subCategoryList);
        recyclerView.setAdapter(subCategoryAdapters);


        if (isConnectingToInternet(context)) {

            callGetSubCategory(getParamSubCateg(lang, categoryId), Urls.GET_SUBCATEGORY);

        }
        edtSearch = (CustomEditText) findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String key = s.toString().trim();

                if (key.length() == 0) {
                    refresh();
                } else {
                    searchSubCountry("" + key);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void selectSubCategory() {

        StringBuffer sub_cat_id = new StringBuffer("");
        StringBuffer sub_cat_name = new StringBuffer("");
        String sub_cat_ids = "", sub_cat_names = "";


        if(searchSubCategoryList.size()>0)
        {
            for (int i = 0; i < searchSubCategoryList.size(); i++) {
                SubCategoryModel subCategoryModel = searchSubCategoryList.get(i);
                if (subCategoryModel.isChecked()) {
                    sub_cat_id.append(subCategoryModel.getSubcategory_cd() + ",");
                    sub_cat_name.append(subCategoryModel.getSubCategoryName() + ",");
                }

            }
        }
        else
        {
            for (int i = 0; i < subCategoryList.size(); i++) {
                SubCategoryModel subCategoryModel = subCategoryList.get(i);
                if (subCategoryModel.isChecked()) {
                    sub_cat_id.append(subCategoryModel.getSubcategory_cd() + ",");
                    sub_cat_name.append(subCategoryModel.getSubCategoryName() + ",");
                }

            }
        }



        if (!sub_cat_id.toString().equals("")) {
            String selected = "" + sub_cat_id;
            String selected_name = "" + sub_cat_name;
            sub_cat_ids = selected.substring(0, selected.length() - 1);
            sub_cat_names = selected_name.substring(0, selected_name.length() - 1);
        }


        Intent intent = new Intent();
        intent.putExtra("ID", sub_cat_ids);
        intent.putExtra("NAME", sub_cat_names);
        setResult(Constant.PICK_SUB_CATEGORY, intent);
        finish();//finishing activity
    }

    private void callGetSubCategory(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONArray jsonArray = js.getJSONArray("subcategory");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            SubCategoryModel subCategory = new SubCategoryModel();
                            subCategory.setSubcategory_cd(jsonObject.getString("subcategory_cd"));
                            subCategory.setSubCategoryName(jsonObject.getString("subCategoryName"));
                            subCategoryList.add(subCategory);
                        }
                        subCategoryAdapters = new SubCategoryAdapters(context, subCategoryList);
                        recyclerView.setAdapter(subCategoryAdapters);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
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

    private Map<String, Object> getParamSubCateg(String lang, String category) {
        Map<String, Object> params = new HashMap<>();
        params.put("Language", lang);
        params.put("category_cd", category);

        return params;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnDone:

                selectSubCategory();


                break;
        }
    }


    private void searchSubCountry(String key) {

        searchSubCategoryList = new ArrayList<>();
        if (subCategoryList.size() > 0) {

            for (SubCategoryModel subCategoryModel : subCategoryList) {
                String countryName = subCategoryModel.getSubCategoryName().toLowerCase();
                if (key.length() < countryName.length()) {
                    String ss = countryName.substring(0, key.length());
                    if (key.toLowerCase().startsWith(ss)) {
                        searchSubCategoryList.add(subCategoryModel);

                    }
                }
            }


            if (searchSubCategoryList.size() > 0) {
                subCategoryAdapters = new SubCategoryAdapters(context, searchSubCategoryList);
                recyclerView.setAdapter(subCategoryAdapters);
            }
        }


    }

    private void refresh() {
        searchSubCategoryList = new ArrayList<>();
        subCategoryAdapters = new SubCategoryAdapters(context, subCategoryList);
        recyclerView.setAdapter(subCategoryAdapters);
    }


}
