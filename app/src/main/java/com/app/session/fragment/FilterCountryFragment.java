package com.app.session.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.CountryAdapters;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Country;
import com.app.session.utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterCountryFragment extends BaseFragment implements View.OnClickListener {

    ImageView imgBack;
    CustomTextView btnDone;
    public CustomEditText edtSearch;
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;
    Context context;
    private CountryAdapters countryAdapter;
    private ArrayList<Country> countryList = new ArrayList<>();
    private ArrayList<Country> searchCountryList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            language_cd = getArguments().getString("ID");
//
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_country, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

        edtSearch = (CustomEditText) view.findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String key = s.toString().trim();


                if (key.length() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    refresh();
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    searchCountry("" + key);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnDone = (CustomTextView) view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (baseActivity.isConnectingToInternet(context)) {

            callGetCountry(getParam(), Urls.GET_CONSULTANT_COUNTRY);

        } else {
            showInternetPop(context);
        }


    }

    private Map<String, Object> getParam() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.imgBack:
//                onBackPressed();
//                break;
        }

    }

    private void callGetCountry(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(baseActivity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {
                        countryList = new ArrayList<>();
                        JSONArray jsonArray = js.getJSONArray("country_list_dt");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Country country = new Country();
                            country.setCountry_cd(jsonObject.getString("country_cd"));
                            country.setCountryName(jsonObject.getString("country_name"));
                            country.setCountry_icon("");
                            country.setDial_cd("");
                            countryList.add(country);
                        }
                        countryAdapter = new CountryAdapters(context, countryList, new ApiCallback() {
                            @Override
                            public void result(String x) {
                                int position = Integer.parseInt(x);
                                Country country = countryList.get(position);
                                Intent intent = new Intent();
                                intent.putExtra("NAME", country.getCountryName());
                                if(country.getCountry_cd().equals("0"))
                                {
                                    intent.putExtra("ID", "");
                                }else {
                                    intent.putExtra("ID", country.getCountry_cd());
                                }
                                intent.putExtra("TYPE", Constant.PICK_COUNTRY);
                                getActivity().setResult(Activity.RESULT_OK, intent);
                                getActivity().finish();
                            }
                        });
//                        recyclerView.setAdapter(countryAdapter);

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


    private void callBack(String x) {
        int position = Integer.parseInt(x);
        Country country = countryList.get(position);
        Intent intent = new Intent();
        intent.putExtra("NAME", country.getCountryName());
        intent.putExtra("ID", country.getCountry_cd());
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
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
                        Country country = countryList.get(position);
                        Intent intent = new Intent();
                        intent.putExtra("NAME", country.getCountryName());
                        if(country.getCountry_cd().equals("0"))
                        {
                            intent.putExtra("ID", "");
                        }else {
                            intent.putExtra("ID", country.getCountry_cd());
                        }
                        intent.putExtra("TYPE", Constant.PICK_COUNTRY);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
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
//                    int position = Integer.parseInt(x);
//                    Country country = searchCountryList.get(position);
//                    Intent intent = getIntent();
//                    intent.putExtra("NAME", country.getCountryName());
//                    intent.putExtra("ID", country.getCountry_cd());
//                    intent.putExtra("DIAL_CD", country.getDial_cd());
//                    intent.putExtra("URL", country.getCountry_icon());
//                    setResult(RESULT_OK, intent);
//                    finish();
                }
            });
            recyclerView.setAdapter(countryAdapter);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            if (baseActivity.isConnectingToInternet(context)) {
//                callGetCountry(getParam(), Urls.GET_CONSULTANT_COUNTRY);
            } else {
                showInternetPop(context);
            }
        }
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = getIntent();
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}
