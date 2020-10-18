package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.SelectLanguageAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Language;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectLangaugeActivity extends BaseActivity implements View.OnClickListener {
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Context context;
    CustomTextView btnDone;
    CustomEditText edtSearch;
    private SelectLanguageAdapter selectLanguageAdapter;
    private ArrayList<Language> languageArrayList = new ArrayList<>();
    public static ArrayList<Language> selectlanguageList = new ArrayList<>();
    private ArrayList<Language> searchlanguageArrayList = new ArrayList<>();
    ImageView imgBack;
    String lang_id = "";
    String[] id;
    String preferred_lang_cd = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_langauge);
        context = this;

        if (getIntent().getStringExtra("ID") != null) {
            lang_id = getIntent().getStringExtra("ID");

        }
        preferred_lang_cd = DataPrefrence.getPref(context, Constant.PREFERED_LANGUAGE, "");
        initView();
        if (isConnectingToInternet(context)) {
            callGetLangauge(getParam(), Urls.GET_LANGUAGES);
        }
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
        selectLanguageAdapter = new SelectLanguageAdapter(context, languageArrayList);
        recyclerView.setAdapter(selectLanguageAdapter);
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
                    searchLanguage("" + key);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void refresh()
    {
        selectLanguageAdapter = new SelectLanguageAdapter(context, languageArrayList);
        recyclerView.setAdapter(selectLanguageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                selectLanguage();
//                if (isConnectingToInternet(context)) {
//                    selectedLang();
//                    if(DataPrefrence.getPref(context,Constant.LOGIN_TYPE,"").equals(Constant.CLIENT))
//                    {
//                        callConsultant_Specialties(getJsonUserObject(), Urls.SET_USERS_LANGUAGES);
//                    }
//                    else {
//                        callConsultant_Specialties(getJsonUserObject(), Urls.SET_USERS_LANGUAGES);
//                        callConsultant_Specialties(getJsonObject(), Urls.SET_CONSULTANT_CVS);
//                    }
//                } else {
//                    showInternetPop(context);
//                }
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

//                            if (!preferred_lang_cd.equals(jsonObject.getString("language_cd")))

                            Language language = new Language();
                            language.setEnglish_name(jsonObject.getString("english_name"));
                            language.setLanguage_cd(jsonObject.getString("language_cd"));
                            language.setNative_name(jsonObject.getString("native_name").replace(","," "));

                            if (!lang_id.isEmpty())
                            {
                                if (lang_id.contains(",")) {

                                    if ((lang_id + ",").contains(language.getLanguage_cd() + ",")) {
                                        language.setChecked(true);
                                    }

                                } else if ((lang_id + ",").equals(language.getLanguage_cd() + ",")) {
                                    language.setChecked(true);
                                }
                                selectlanguageList.add(language);
                            } else {
                                language.setChecked(false);

                            }

                            if(!language.getLanguage_cd().equals(preferred_lang_cd)) {
                                languageArrayList.add(language);
                            }


                        }

                        selectLanguageAdapter = new SelectLanguageAdapter(context, languageArrayList);
                        recyclerView.setAdapter(selectLanguageAdapter);


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
    private void searchLanguage(String key) {

        searchlanguageArrayList = new ArrayList<>();
        if (languageArrayList.size() > 0) {

            for (int i = 0; i < languageArrayList.size(); i++) {
                Language language = languageArrayList.get(i);
                String languageName = language.getEnglish_name().toLowerCase();
                if (key.length() < languageName.length()) {
                    String ss = languageName.substring(0, key.length());
                    if (key.toLowerCase().startsWith(ss))
                    {
                        if (!lang_id.isEmpty()) {
                            if (lang_id.contains(",")) {

                                if ((lang_id + ",").contains(language.getLanguage_cd() + ",")) {
                                    language.setChecked(true);
                                }

                            } else if ((lang_id + ",").contains(language.getLanguage_cd() + ",")) {
                                language.setChecked(true);
                            }
                            selectlanguageList.add(language);
                        } else {
                            language.setChecked(false);
//                            searchlanguageArrayList.add(language);
                        }


                        if(!language.getLanguage_cd().equals(preferred_lang_cd)) {
                            searchlanguageArrayList.add(language);
                        }
                    }
                }
            }


            if (searchlanguageArrayList.size() > 0) {
                selectLanguageAdapter = new SelectLanguageAdapter(context, searchlanguageArrayList);
                recyclerView.setAdapter(selectLanguageAdapter);
            }
        }


    }


    private void selectLanguage() {

        StringBuffer lang_id = new StringBuffer("");
        StringBuffer lang_name = new StringBuffer("");
        String lang_ids = "", lang_names = "";


//        if (searchlanguageArrayList.size() > 0) {
//            for (int i = 0; i < searchlanguageArrayList.size(); i++) {
//                Language language = searchlanguageArrayList.get(i);
//                if (language.isChecked()) {
//                    language_id.append(language.getLanguage_cd() + ",");
//                    lang_name.append(language.getEnglish_name() + ",");
//                }
//
//            }
//        } else {
//            for (int i = 0; i < languageArrayList.size(); i++) {
//                Language language = languageArrayList.get(i);
//                if (language.isChecked()) {
//                    language_id.append(language.getLanguage_cd() + ",");
//                    lang_name.append(language.getEnglish_name() + ",");
//                }
//
//            }
//        }


//        for (int i = 0; i < selectlanguageList.size(); i++) {
//            Language language = selectlanguageList.get(i);
//            if (language.isChecked()) {
//                language_id.append(language.getLanguage_cd() + ",");
//                lang_name.append(language.getNative_name() + ",");
//            }
//
//        }

        HashMap<String, String> hm = new HashMap<String, String>();
        for (int i = 0; i < selectlanguageList.size(); i++) {
            Language language = selectlanguageList.get(i);
            if (language.isChecked()) {
                hm.put(language.getLanguage_cd(), language.getNative_name());
            }
        }


        for (Map.Entry m : hm.entrySet()) {
            lang_id.append("" + m.getKey() + ",");
            lang_name.append("" + m.getValue() + ",");

        }


        if (!lang_id.toString().equals("")) {
            String selected = "" + lang_id;
            String selected_name = "" + lang_name;
            lang_ids = selected.substring(0, selected.length() - 1);
            lang_names = selected_name.substring(0, selected_name.length() - 1);
        }


        selectlanguageList = new ArrayList<>();
        Intent intent = new Intent();
        intent.putExtra("ID", lang_ids);
        intent.putExtra("NAME", lang_names);
        setResult(Constant.PICK_SUB_CATEGORY, intent);
        finish();//finishing activity
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectlanguageList = new ArrayList<>();
        finish();
    }
}
