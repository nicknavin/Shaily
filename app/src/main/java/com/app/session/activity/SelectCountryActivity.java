package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.CategoryAdapter;
import com.app.session.adapter.CountryAdapters;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Category;
import com.app.session.data.model.Country;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCountryActivity extends BaseActivity implements View.OnClickListener {

    ImageView imgBack;
    CustomTextView btnDone;
    CustomEditText edtSearch;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Context context;
    private CountryAdapters countryAdapter;
    private ArrayList<Country> countryList = new ArrayList<>();
    private ArrayList<Country> searchCountryList = new ArrayList<>();
    String type = "";
    private ArrayList<Category> categoryList = new ArrayList<>();
    private ArrayList<Category> searchCategoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        if (getIntent().getStringExtra("TYPE") != null) {
            type = getIntent().getStringExtra("TYPE");
        }
        context = this;
        initView();

    }


    private void initView() {

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        edtSearch = (CustomEditText) findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String key = s.toString().trim();

                if (type.equals("Country")) {
                    if (key.length() == 0) {
                        refresh();
                    } else {
                        searchCountry("" + key);
                    }
                }
                else
                {
                    if (key.length() == 0) {
                        refreshCategory();
                    } else {
                        searchCategory("" + key);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (type.equals("Country")) {
            ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.select_country));

        } else {
            ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.select_category));
            edtSearch.setHint(context.getResources().getString(R.string.search_catg));
        }


        btnDone = (CustomTextView) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (type.equals("Country")) {
            if (isConnectingToInternet(context)) {

                //callGetCountry(getParam("en"), Urls.GET_COUNTRY);
                callGetCountry("en");

            } else {
                showInternetPop(context);
            }
        } else {
            if (isConnectingToInternet(context)) {
              //  callGetCategory(getParam(lang), Urls.GET_CATEGORY);
                callGetCategory();
            } else {
                showInternetPop(context);
            }
        }


    }

    private Map<String, Object> getParam(String lang) {
        Map<String, Object> params = new HashMap<>();
        params.put("Language", lang);
        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }

    }

    private void callGetCountry(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {
                        JSONArray jsonArray = js.getJSONArray("Country");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Country country = new Country();
                            country.setCountry_cd(jsonObject.getString("country_cd"));
                            country.setCountryName(jsonObject.getString("CountryName"));
                            country.setCountry_icon(jsonObject.getString("country_icon"));
                            country.setDial_cd(jsonObject.getString("dial_cd"));
                            countryList.add(country);
                        }
                        countryAdapter = new CountryAdapters(context, countryList, new ApiCallback() {
                            @Override
                            public void result(String x) {
                                int position = Integer.parseInt(x);
                                Country country = countryList.get(position);
                                Intent intent = getIntent();
                                intent.putExtra("NAME", country.getCountryName());
                                intent.putExtra("ID", country.getCountry_cd());
                                intent.putExtra("DIAL_CD", country.getDial_cd());
                                intent.putExtra("URL",country.getCountry_icon());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                        recyclerView.setAdapter(countryAdapter);

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



    private void callGetCountry(String lang) {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getCountry(lang);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getBoolean("Status")) {
                                try {
                                    JSONArray jsonArray = js.getJSONArray("Country");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Country country = new Country();
                                        country.setCountry_cd(jsonObject.getString("country_cd"));
                                        country.setCountryName(jsonObject.getString("CountryName"));
                                        country.setCountry_icon(jsonObject.getString("country_icon"));
                                        country.setDial_cd(jsonObject.getString("dial_cd"));
                                        countryList.add(country);
                                    }
                                    countryAdapter = new CountryAdapters(context, countryList, new ApiCallback() {
                                        @Override
                                        public void result(String x) {
                                            int position = Integer.parseInt(x);
                                            Country country = countryList.get(position);
                                            Intent intent = getIntent();
                                            intent.putExtra("NAME", country.getCountryName());
                                            intent.putExtra("ID", country.getCountry_cd());
                                            intent.putExtra("DIAL_CD", country.getDial_cd());
                                            intent.putExtra("URL",country.getCountry_icon());
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });
                                    recyclerView.setAdapter(countryAdapter);

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
        } else {
            showInternetConnectionToast();
        }
    }


    private void searchCountry(String key) {

        searchCountryList = new ArrayList<>();
        if (countryList.size() > 0) {

            for (Country country : countryList) {
                String countryName = country.getCountryName().toLowerCase();
                if (key.length() < countryName.length()) {
                    String ss = countryName.substring(0, key.length());
                    if (key.toLowerCase().startsWith(ss)) {
                        searchCountryList.add(country);

                    }
                }
            }


            if (searchCountryList.size() > 0) {
                countryAdapter = new CountryAdapters(context, searchCountryList, new ApiCallback() {
                    @Override
                    public void result(String x) {
                        int position = Integer.parseInt(x);
                        Country country = searchCountryList.get(position);
                        Intent intent = getIntent();
                        intent.putExtra("NAME", country.getCountryName());
                        intent.putExtra("ID", country.getCountry_cd());
                        intent.putExtra("DIAL_CD", country.getDial_cd());
                        intent.putExtra("URL",country.getCountry_icon());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                recyclerView.setAdapter(countryAdapter);
                countryAdapter.notifyDataSetChanged();
            }
        }


    }

    private void refresh() {
        if (countryList.size() > 0) {
            countryAdapter = new CountryAdapters(context, countryList, new ApiCallback() {
                @Override
                public void result(String x) {
                    int position = Integer.parseInt(x);
                    Country country = searchCountryList.get(position);
                    Intent intent = getIntent();
                    intent.putExtra("NAME", country.getCountryName());
                    intent.putExtra("ID", country.getCountry_cd());
                    intent.putExtra("DIAL_CD", country.getDial_cd());
                    intent.putExtra("URL",country.getCountry_icon());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            recyclerView.setAdapter(countryAdapter);
        }
    }

    private void refreshCategory() {
        if (categoryList.size() > 0) {
            categoryAdapter = new CategoryAdapter(context, categoryList, new ApiCallback() {
                @Override
                public void result(String x) {
                    int position = Integer.parseInt(x);
                    Category category = categoryList.get(position);
                    Intent intent = getIntent();
                    intent.putExtra("NAME", category.getCategoryName());
                    intent.putExtra("ID", category.getCategoryID());

                    setResult(RESULT_OK, intent);
                    finish();

                }
            });
            recyclerView.setAdapter(categoryAdapter);
        }
    }
    private void searchCategory(String key) {

        searchCategoryList = new ArrayList<>();
        if (categoryList.size() > 0) {

            for (Category category : categoryList) {
                String categoryName = category.getCategoryName().toLowerCase();
                if (key.length() < categoryName.length()) {
                    String ss = categoryName.substring(0, key.length());
                    if (key.toLowerCase().startsWith(ss)) {
                        searchCategoryList.add(category);

                    }
                }
            }


            if (searchCategoryList.size() > 0) {
                categoryAdapter = new CategoryAdapter(context, searchCategoryList, new ApiCallback() {
                    @Override
                    public void result(String x) {
                        int position = Integer.parseInt(x);
                        Category category = searchCategoryList.get(position);
                        Intent intent = getIntent();
                        intent.putExtra("NAME", category.getCategoryName());
                        intent.putExtra("ID", category.getCategoryID());
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                });
                recyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
            }
        }


    }






    private void callGetCategory(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    try {
                        dismiss_loading();
                        JSONArray jsonArray = js.getJSONArray("Category");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Category category = new Category();
                            category.setCategoryName(jsonObject.getString("CategoryName"));
                            category.setCategoryID(jsonObject.getString("category_cd"));
                            categoryList.add(category);
                        }

                        categoryAdapter = new CategoryAdapter(context, categoryList, new ApiCallback() {
                            @Override
                            public void result(String x) {
                                int position = Integer.parseInt(x);
                                Category category = categoryList.get(position);
                               Intent intent = getIntent();
                                intent.putExtra("NAME", category.getCategoryName());
                                intent.putExtra("ID", category.getCategoryID());
                                setResult(RESULT_OK, intent);
                                finish();

                            }
                        });
                        recyclerView.setAdapter(categoryAdapter);


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
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }


    private void callGetCategory() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getCategory(lang);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getBoolean("Status")) {

                                try {
                                    dismiss_loading();
                                    JSONArray jsonArray = js.getJSONArray("Category");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Category category = new Category();
                                        category.setCategoryName(jsonObject.getString("CategoryName"));
                                        category.setCategoryID(jsonObject.getString("category_cd"));
                                        categoryList.add(category);
                                    }

                                    categoryAdapter = new CategoryAdapter(context, categoryList, new ApiCallback() {
                                        @Override
                                        public void result(String x) {
                                            int position = Integer.parseInt(x);
                                            Category category = categoryList.get(position);
                                            Intent intent = getIntent();
                                            intent.putExtra("NAME", category.getCategoryName());
                                            intent.putExtra("ID", category.getCategoryID());
                                            setResult(RESULT_OK, intent);
                                            finish();

                                        }
                                    });
                                    recyclerView.setAdapter(categoryAdapter);


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
        } else {
            showInternetConnectionToast();
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
