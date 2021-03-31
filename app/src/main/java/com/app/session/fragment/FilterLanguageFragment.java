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
import com.app.session.adapter.PreferenceLanguageAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Language;
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

public class FilterLanguageFragment extends BaseFragment implements View.OnClickListener {


    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Context context;
    CustomTextView btnDone;
    private PreferenceLanguageAdapter preflanguageAdapter;

    private ArrayList<Language> languageArrayList = new ArrayList<>();
    public static ArrayList<Language> selectlanguageList = new ArrayList<>();
    private ArrayList<Language> searchlanguageArrayList = new ArrayList<>();
    ImageView imgBack;
    String lang_id = "", prefered_language = "";
    String[] id;
    public CustomEditText edtSearch;
    String typeActivity = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        Bundle bundle = this.getArguments();


    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_registert_language);
//        context = this;
//        initHtml();
//        if (isConnectingToInternet(context)) {
//            callGetLangauge(getParam(), Urls.GET_REGISTER_LANGUAGE);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_language, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (baseActivity.isConnectingToInternet(context)) {
            callGetLangauge(getParam(), Urls.GET_REGISTER_LANGUAGE);
        } else {
            showInternetPop(context);
        } }


    private void initView(View view) {

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        preflanguageAdapter = new PreferenceLanguageAdapter(context, languageArrayList, new ApiCallback() {
            @Override
            public void result(String x) {

            }
        });
        recyclerView.setAdapter(preflanguageAdapter);
        edtSearch = (CustomEditText)view.findViewById(R.id.edtSearch);
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
                    searchPrefLanguage("" + key);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchPrefLanguage(String key) {

        searchlanguageArrayList = new ArrayList<>();
        if (languageArrayList.size() > 0) {

            for (int i = 0; i < languageArrayList.size(); i++) {
                Language language = languageArrayList.get(i);
                String languageName = language.getEnglish_name().toLowerCase();
                if (key.length() < languageName.length()) {
                    String ss = languageName.substring(0, key.length());
                    if (key.toLowerCase().startsWith(ss)) {
                        searchlanguageArrayList.add(language);

                    }
                }
            }


            if (searchlanguageArrayList.size() > 0) {
                preflanguageAdapter = new PreferenceLanguageAdapter(context, searchlanguageArrayList, new ApiCallback() {
                    @Override
                    public void result(String x) {
                        int position = Integer.parseInt(x);
                        Language language = languageArrayList.get(position);
                        Intent intent = new Intent();
                        intent.putExtra("ID", language.getLanguage_cd());
                        intent.putExtra("NAME", language.getNative_name());
                        intent.putExtra("TYPE", Constant.PICK_PREF_LANGUAGE);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();

                    }
                });

                recyclerView.setAdapter(preflanguageAdapter);
            }
        }


    }

    private void refresh() {
        preflanguageAdapter = new PreferenceLanguageAdapter(context, languageArrayList, new ApiCallback() {
            @Override
            public void result(String x) {

            }
        });
        recyclerView.setAdapter(preflanguageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



        }

    }


    private void callGetLangauge(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(baseActivity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    dismiss_loading();

                    try {
                        languageArrayList=new ArrayList<>();
                        JSONArray jsonArray = js.getJSONArray("languages_dt");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Language language = new Language();
                            language.setEnglish_name(jsonObject.getString("language_name"));
                            language.setLanguage_cd(jsonObject.getString("language_cd"));
                            language.setNative_name(jsonObject.getString("language_name").replace(",", " "));
                            languageArrayList.add(language);
                        }
                        preflanguageAdapter = new PreferenceLanguageAdapter(context, languageArrayList, new ApiCallback() {
                            @Override
                            public void result(String x) {
                                int position = Integer.parseInt(x);
                                Language language = languageArrayList.get(position);
                                Intent intent = new Intent();
                                intent.putExtra("ID", language.getLanguage_cd());
                                intent.putExtra("NAME", language.getNative_name());
                                intent.putExtra("TYPE", Constant.PICK_PREF_LANGUAGE);
                                getActivity().setResult(Activity.RESULT_OK, intent);
                                getActivity().finish();

                            }
                        });
//                        recyclerView.setAdapter(preflanguageAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    MyDialog.iPhone(failed, context);
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

    private Map<String, Object> getParam() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        return params;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            if (baseActivity.isConnectingToInternet(context)) {
//                callGetLangauge(getParam(), Urls.GET_REGISTER_LANGUAGE);
            } else {
                showInternetPop(context);
            }
        }
    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        selectlanguageList = new ArrayList<>();
//        finish();
//    }
}
