package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.adapter.LanguageAdapter;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.Language;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LanguageRegActivity extends BaseActivity implements View.OnClickListener {

    Context context;
    CustomEditText edt_selectLang;
    CustomTextView txtNext, txtBack, txt_add_lang, txtSkip, txt_pref_lang;
    String lang_id = "", lang_name = "";
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    LanguageAdapter languageAdapter;
    ArrayList<Language> languageArrayList= new ArrayList<>();
    String pref_id="", pref_name ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_reg);
        context = this;
        initView();
    }


    private void initView() {
        edt_selectLang = (CustomEditText) findViewById(R.id.edt_selectLang);
        edt_selectLang.setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.lang_selection));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);

        Language language= new Language();
        language.setLanguage_cd(DataPrefrence.getPref(context, Constant.PREFERED_LANGUAGE,""));
        language.setNative_name(DataPrefrence.getPref(context, Constant.PREFERED_LANGUAGE_NAME,""));
        language.setPreferred_lang(true);
        languageArrayList.add(language);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        languageAdapter= new LanguageAdapter(context,languageArrayList);
        recyclerView.setAdapter(languageAdapter);
        pref_id=language.getLanguage_cd();
        pref_name =language.getNative_name();
        txt_pref_lang = (CustomTextView) findViewById(R.id.txt_pref_lang);
//        txt_pref_lang.setText("1." + DataPrefrence.getPref(context, Constant.PREFERED_LANGUAGE_NAME, ""));
        txt_pref_lang.setVisibility(View.GONE);


        txtNext = (CustomTextView) findViewById(R.id.txtNext);
        txtNext.setOnClickListener(this);
        txtBack = (CustomTextView) findViewById(R.id.txtBack);
        txtBack.setOnClickListener(this);

        txt_add_lang = (CustomTextView) findViewById(R.id.txt_add_lang);
        txt_add_lang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.edt_selectLang:
//
//                intent = new Intent(context, SelectLangaugeActivity.class);
//
//                intent.putExtra("ID", lang_id);
//
//                startActivityForResult(intent, Constant.PICK_LANGUAGE);
                break;
            case R.id.txt_add_lang:

                intent = new Intent(context, SelectLangaugeActivity.class);

                intent.putExtra("ID", lang_id);

                startActivityForResult(intent, Constant.PICK_LANGUAGE);
                break;
            case R.id.txtBack:
                onBackPressed();
                break;

            case R.id.imgBack:
                onBackPressed();

                break;
            case R.id.txtNext:
                if (lang_id.isEmpty()) {
                    lang_id=pref_id;
                    lang_name=pref_name;
                    DataPrefrence.setPref(context, Constant.LANGUAGE_CD, lang_id);
                    DataPrefrence.setPref(context, Constant.LANGUAGE_NAME, lang_name);
                    startActivity(new Intent(context, UploadProfileImageActivity.class));
                }
                else {
                    DataPrefrence.setPref(context, Constant.LANGUAGE_CD, lang_id);
                    DataPrefrence.setPref(context, Constant.LANGUAGE_NAME, lang_name);
                    startActivity(new Intent(context, UploadProfileImageActivity.class));
                }
//                else {
//                    showToast(context.getResources().getString(R.string.select_lang));
//                }
                break;
            case R.id.txtSkipp:


                lang_id = "";
                lang_name = "";

                DataPrefrence.setPref(context, Constant.LANGUAGE_CD, lang_id);
                DataPrefrence.setPref(context, Constant.LANGUAGE_NAME, lang_name);
                startActivity(new Intent(context, UploadProfileImageActivity.class));

                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.PICK_LANGUAGE) {
            String name = "", id = "";
            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {

                    id = data.getStringExtra("ID");
                }
                lang_id=pref_id+","+id;
                lang_name= pref_name+","+name;
                makeLangList(lang_id,lang_name);


            }
        }


    }


    public void makeLangList(String ids, String names)
    {
        String[] lang_id,lang_name;

        languageArrayList= new ArrayList<>();
      if(ids.contains(","))
      {
         lang_id= ids.split(",");
         lang_name=names.split(",");


         for(int i=0;i<lang_id.length;i++)
         {
             Language language= new Language();
             language.setNative_name(lang_name[i]);
             language.setLanguage_cd(lang_id[i]);
             languageArrayList.add(language);
         }
      }
      else
      {
          Language language= new Language();
          language.setNative_name(names);
          language.setLanguage_cd(ids);
          languageArrayList.add(language);
      }




        ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
      if(languageArrayList.size()<=4) {
          params.height = 110 * languageArrayList.size();
      }
      else
      {
          params.height = 110 *4;
      }
      recyclerView.setLayoutParams(params);
        languageAdapter= new LanguageAdapter(context,languageArrayList);
        recyclerView.setAdapter(languageAdapter);
    }



}





