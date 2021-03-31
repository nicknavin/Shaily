package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.PreferenceLanguageAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Language;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PreferenceLanguageActivity extends BaseActivity implements View.OnClickListener {


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
    CustomEditText edtSearch;
    String typeActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_language);
        context = this;

        if (getIntent().getStringExtra("TYPE") != null) {
            typeActivity = getIntent().getStringExtra("TYPE");
        }
        initView();
        if (isConnectingToInternet(context)) {
            callGetLangauge(getParam(), Urls.GET_LANGUAGES);
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initView() {
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.languages));
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnDone = (CustomTextView) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        preflanguageAdapter = new PreferenceLanguageAdapter(context, languageArrayList, new ApiCallback() {
            @Override
            public void result(String x) {

            }
        });
        recyclerView.setAdapter(preflanguageAdapter);
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
                    searchPrefLanguage("" + key);
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
                        Language language = searchlanguageArrayList.get(position);
                        prefered_language = language.getLanguage_cd();
                        DataPrefrence.setPref(context, Constant.PREFERED_LANGUAGE, prefered_language);
                        if (typeActivity.equals("edit")) {
                            Intent intent= new Intent();
                            intent.putExtra("ID",language.getLanguage_cd());
                            intent.putExtra("NAME",language.getNative_name());
                            setResult(RESULT_OK, intent);
                            finish();


                        } else {

                            Utility.setLang(context,prefered_language);
                            startActivity(new Intent(context, RegistrationFirstActivity.class));
                        }
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
            case R.id.btnDone:

                startActivity(new Intent(context, RegistrationFirstActivity.class));

                break;


            case R.id.imgBack:
                onBackPressed();
                break;
        }

    }


    private void callGetLangauge(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    dismiss_loading();

                    try {
                        JSONArray jsonArray = js.getJSONArray("languages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Language language = new Language();
                            language.setEnglish_name(jsonObject.getString("english_name"));
                            language.setLanguage_cd(jsonObject.getString("language_cd"));
                            language.setNative_name(jsonObject.getString("native_name").replace(","," "));
                            languageArrayList.add(language);
                        }
                        preflanguageAdapter = new PreferenceLanguageAdapter(context, languageArrayList, new ApiCallback() {
                            @Override
                            public void result(String x) {
                                int position = Integer.parseInt(x);
                                Language language = languageArrayList.get(position);
                                prefered_language = language.getLanguage_cd();
                                DataPrefrence.setPref(context, Constant.PREFERED_LANGUAGE, prefered_language);
                                DataPrefrence.setPref(context, Constant.PREFERED_LANGUAGE_NAME, language.getNative_name());

                                if (typeActivity.equals("edit")) {
                                    Intent intent= new Intent();
                                    intent.putExtra("ID",language.getLanguage_cd());
                                    intent.putExtra("NAME",language.getNative_name());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    startActivity(new Intent(context, RegistrationFirstActivity.class));
                                }

                            }
                        });
                        recyclerView.setAdapter(preflanguageAdapter);

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

        return params;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectlanguageList = new ArrayList<>();
        finish();
    }
}