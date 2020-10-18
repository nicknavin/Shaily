package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.adapter.SelectedLanguageAdapter;
import com.app.session.adapter.SpeakLanguageSelectionAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Language;
import com.app.session.model.LanguagesRoot;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLanguageYouSpeakActivity extends BaseActivity implements View.OnClickListener {


    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView, recyclerViewSelected;
    Context context;
    CustomTextView btnNext, txtBack,txtCancel;

    private ArrayList<Language> languageArrayList = new ArrayList<>();
    public static ArrayList<Language> selectlanguageList = new ArrayList<>();

    ImageView imgBack;
    String languageCd = "";

    String typeActivity = "";

    HashMap<String, Language> langHashMap = new HashMap<>();
    CardView card_view;
    LinearLayout laySelectedLang;
   RelativeLayout layBackNext;
    CardView card_viewSelected;
    ImageView imgAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language_you_speak);
        context = this;

        if (getIntent().getStringExtra("TYPE") != null) {
            typeActivity = getIntent().getStringExtra("TYPE");
        }
        initView();
        if (isConnectingToInternet(context)) {
//            callGetLangauge(getParam(), Urls.GET_LANGUAGES);
            callGetLangauge();

        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initView() {


        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.languages_select_speak));


        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnNext = (CustomTextView) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        txtBack = (CustomTextView) findViewById(R.id.txtBack);
        txtBack.setOnClickListener(this);
        txtCancel = (CustomTextView) findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(this);
        imgAddItem = (ImageView) findViewById(R.id.imgAddItem);
        imgAddItem.setOnClickListener(this);

        laySelectedLang=(LinearLayout)findViewById(R.id.laySelectedLang);
        card_viewSelected=(CardView) findViewById(R.id.card_viewSelected);
        layBackNext=(RelativeLayout) findViewById(R.id.layBackNext);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerViewSelected = (RecyclerView) findViewById(R.id.recyclerViewSelectedLang);
        recyclerViewSelected.setLayoutManager(new LinearLayoutManager(context));


    }


    private void initSet() {


        selectlanguageList = new ArrayList<>();
        for (Map.Entry<String, Language> ee : langHashMap.entrySet()) {
            String key = ee.getKey();
            Language language = ee.getValue();
            selectlanguageList.add(language);
            System.out.println("language_cd" + key);
            // TODO: Do something.
        }

        SelectedLanguageAdapter selectedLanguageAdapter = new SelectedLanguageAdapter(context, selectlanguageList);
        recyclerViewSelected.setAdapter(selectedLanguageAdapter);

        if(selectlanguageList.size()>10)
        {
            ViewGroup.LayoutParams params=recyclerViewSelected.getLayoutParams();
            params.height=1000;
            recyclerViewSelected.setLayoutParams(params);
        }



        if(selectlanguageList.size()==0)
        {
            laySelectedLang.setVisibility(View.GONE);
            card_viewSelected.setVisibility(View.GONE);
        }
        else
        {
            laySelectedLang.setVisibility(View.VISIBLE);
            card_viewSelected.setVisibility(View.VISIBLE);
        }


    }

    public void getLangIDs()
    {
        StringBuffer lang_id = new StringBuffer("");

        for(int i=0;i<selectlanguageList.size();i++)
        {
            lang_id.append(selectlanguageList.get(i).getCode()+",");
        }

        if (!lang_id.toString().equals(""))
        {
            String selected = "" + lang_id;
            languageCd = selected.substring(0, selected.length() - 1);
        }


        DataPrefrence.setPref(context, Constant.LANGUAGE_CD,languageCd);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:

                if (selectlanguageList.size() > 0)
                {
                    getLangIDs();
                    DataPrefrence.setLanguage(context, Constant.LANGUAGE_SPEAK, selectlanguageList);
                    startActivity(new Intent(context, RegistrationSecondActivity.class));

                } else {
                    showToast("Please select at least one language.");
                }

                break;


            case R.id.imgBack:
                onBackPressed();
                break;

            case R.id.txtBack:
                finish();
                break;
                case R.id.imgAddItem:
                    txtCancel.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                laySelectedLang.setVisibility(View.GONE);
                    layBackNext.setVisibility(View.GONE);
                break;
            case R.id.txtCancel:
                txtCancel.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                laySelectedLang.setVisibility(View.VISIBLE);
                layBackNext.setVisibility(View.VISIBLE);


                if(selectlanguageList.size()==0)
                {

                    card_viewSelected.setVisibility(View.GONE);
                }
                else
                {

                    card_viewSelected.setVisibility(View.VISIBLE);
                }

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

//                        Language language0 = new Language();
//                        language0.setNative_name("Select Language");
//                        languageArrayList.add(language0);


                        JSONArray jsonArray = js.getJSONArray("languages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Language language = new Language();
                            language.setEnglish_name(jsonObject.getString("english_name"));
                            language.setLanguage_cd(jsonObject.getString("language_cd"));
                            language.setNative_name(jsonObject.getString("native_name").replace(",", " "));
                            language.setChecked(false);
                            languageArrayList.add(language);
                        }
                        SpeakLanguageSelectionAdapter speakLanguageSelectAdapter = new SpeakLanguageSelectionAdapter(context, languageArrayList, new ApiCallback() {
                            @Override
                            public void result(String x)
                            {
                                int position = Integer.parseInt(x);
                                Language language = languageArrayList.get(position);
                                langHashMap.put(language.getLanguage_cd(), language);
                                initSet();
                                recyclerView.setVisibility(View.GONE);
                                laySelectedLang.setVisibility(View.VISIBLE);
                                layBackNext.setVisibility(View.VISIBLE);
                                txtCancel.setVisibility(View.GONE);

                            }
                        });
                        recyclerView.setAdapter(speakLanguageSelectAdapter);


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



    private void callGetLangauge() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<LanguagesRoot> call = apiInterface.getLanguages();
            call.enqueue(new Callback<LanguagesRoot>() {
                @Override
                public void onResponse(Call<LanguagesRoot> call, Response<LanguagesRoot> response) {
                    dismiss_loading();

                    if(response.body()!=null)
                    {
                        languageArrayList=new ArrayList<>();
                        LanguagesRoot root=response.body();

                        if(root.getStatus()==200)
                        {
                            languageArrayList=root.getUserLangauges();
                            SpeakLanguageSelectionAdapter speakLanguageSelectAdapter = new SpeakLanguageSelectionAdapter(context, languageArrayList, new ApiCallback() {
                                @Override
                                public void result(String x)
                                {
                                    int position = Integer.parseInt(x);
                                    Language language = languageArrayList.get(position);
                                    langHashMap.put(language.get_id(), language);
                                    initSet();
                                    recyclerView.setVisibility(View.GONE);
                                    laySelectedLang.setVisibility(View.VISIBLE);
                                    layBackNext.setVisibility(View.VISIBLE);
                                    txtCancel.setVisibility(View.GONE);

                                }
                            });
                            recyclerView.setAdapter(speakLanguageSelectAdapter);
                        }
                    }





                }

                @Override
                public void onFailure(Call<LanguagesRoot> call, Throwable t) {
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
        selectlanguageList = new ArrayList<>();
        finish();
    }

//    private void callGetLanuguages()
//    {
//        if (isConnectingToInternet(context))
//        {
//            showLoading();
//            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
//            Call<LanguagesRoot> call = apiInterface.getLanguages();
//            call.enqueue(new Callback<LanguagesRoot>() {
//                @Override
//                public void onResponse(Call<LanguagesRoot> call, Response<LanguagesRoot> response)
//                {
//                    dismiss_loading();
//
//                    if(response.body()!=null)
//                    {
//                        LanguagesRoot root=response.body();
//                        if(root.getStatus()==200)
//                        {
//                            ArrayList<UserLangauges> userLangauges =root.getUserLangauges();
//
//                            SpeakLanguageSelectionAdapter speakLanguageSelectAdapter = new SpeakLanguageSelectionAdapter(context, languageArrayList, new ApiCallback() {
//                                @Override
//                                public void result(String x)
//                                {
//                                    int position = Integer.parseInt(x);
//                                    Language language = languageArrayList.get(position);
//                                    langHashMap.put(language.getLanguage_cd(), language);
//                                    initSet();
//                                    recyclerView.setVisibility(View.GONE);
//                                    laySelectedLang.setVisibility(View.VISIBLE);
//                                    layBackNext.setVisibility(View.VISIBLE);
//                                    txtCancel.setVisibility(View.GONE);
//
//                                }
//                            });
//                            recyclerView.setAdapter(speakLanguageSelectAdapter);
//
//                        }
//                        else
//                        {
//
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<LanguagesRoot> call, Throwable t) {
//                    dismiss_loading();
//                }
//            });
//        }
//        else
//        {
//            showInternetConnectionToast();
//        }
//
//    }

}