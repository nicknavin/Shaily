package com.app.session.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.adapter.SelectCategoryAdapter;
import com.app.session.adapter.SingleLanguageSelectionAdapter;
import com.app.session.adapter.SpeakLanguageSelectionAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Category;
import com.app.session.data.model.Language;
import com.app.session.data.model.LanguageCategory;
import com.app.session.data.model.LanguageCategoryData;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectProfileCategoryActivity extends BaseActivity implements View.OnClickListener {


    CustomTextView txtNext, txtBack,txtCancel;
    RecyclerView recyclerView, recyclerViewSelectedLang, recyclerViewlang, recyclerViewCategory;
    String languageCd = "", languageName = "";
    private ArrayList<Language> languageArrayList = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private ArrayList<Category> selectCategoryArrayList = new ArrayList<>();
    HashMap<String, LanguageCategory> arrayListHashMap = new HashMap<String, LanguageCategory>();
    HashMap<String, ArrayList<Category>> categoryHashMap = new HashMap<>();
    HashMap<String, HashMap<String, Category>> categoryLangHashMap = new HashMap<>();
    CardView card_view;

    CardView card_viewSelectedLang, card_viewlang, card_viewCategory;
    ImageView imgAddItem, imgAddCategroy;
    CustomEditText editCategory;
    ArrayList<LanguageCategoryData> languageCategoryDataArrayList;
    HashMap<String, Language> langHashMap = new HashMap<>();
    HashMap<String, Category> catgStoreHashMap = new HashMap<>();
    RelativeLayout layCategory;
    LinearLayout layCatg,layLangimagAdd,layCatgimagAdd;
    ImageView imgAdd;

    String categoryStr="",languageStr="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language_category);

        ((CustomTextView)findViewById(R.id.header)).setText("Select categories of your expertise");

        initLangList();


        initView();

    }


    private void initLangList()
    {
        languageCd= DataPrefrence.getPref(context, Constant.LANGUAGE_CD,"");
        languageArrayList = DataPrefrence.getLanguage(context, Constant.LANGUAGE_SPEAK);

    }


    private void initView() {
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        card_viewSelectedLang = (CardView) findViewById(R.id.card_viewSelectedLang);
        card_viewlang = (CardView) findViewById(R.id.card_viewlang);
        card_viewCategory = (CardView) findViewById(R.id.card_viewCategory);

        layCatg=(LinearLayout)findViewById(R.id.layCatg);
        layLangimagAdd=(LinearLayout)findViewById(R.id.layLangimagAdd);
        layCatgimagAdd=(LinearLayout)findViewById(R.id.layCatgimagAdd);

        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(this);

        imgAddItem = (ImageView) findViewById(R.id.imgAddItem);
        imgAddItem.setOnClickListener(this);

        imgAddCategroy = (ImageView) findViewById(R.id.imgAddCategroy);
        imgAddCategroy.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        card_view = (CardView) findViewById(R.id.card_view);


        txtNext = (CustomTextView) findViewById(R.id.txtNext);
        txtNext.setOnClickListener(this);
        txtBack = (CustomTextView) findViewById(R.id.txtBack);
        txtBack.setOnClickListener(this);
        txtCancel = (CustomTextView) findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(this);


        //callGetCategory(getParam(), Urls.GETCATEGORYBYLANGUAGE);
        callGetCategory();

        recyclerViewCategory = (RecyclerView) findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(context));

        recyclerViewlang = (RecyclerView) findViewById(R.id.recyclerViewlang);
        recyclerViewlang.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSelectedLang = (RecyclerView) findViewById(R.id.recyclerViewSelectedLang);
        recyclerViewSelectedLang.setLayoutManager(new LinearLayoutManager(context));
        SpeakLanguageSelectionAdapter speakLanguageSelectAdapter = new SpeakLanguageSelectionAdapter(context, languageArrayList, new ApiCallback() {
            @Override
            public void result(String x) {
                if (languageArrayList.size() > 1) {
                    int position = Integer.parseInt(x);
                    Language language = languageArrayList.get(position);
                    languageCd=language.getLanguage_cd();
                    card_viewSelectedLang.setVisibility(View.GONE);

                    layLangimagAdd.setVisibility(View.GONE);

                    card_viewlang.setVisibility(View.GONE);
                    langHashMap.put(language.getLanguage_cd(), language);
                    initSelectedLang();
                    callGetCategory();
                    //callGetCategory(getParam(), Urls.GETCATEGORYBYLANGUAGE);

                }


            }
        });
        recyclerViewSelectedLang.setAdapter(speakLanguageSelectAdapter);
        if(languageArrayList.size()>8)
        {
            ViewGroup.LayoutParams params=recyclerViewSelectedLang.getLayoutParams();
            params.height=1000;
            recyclerViewSelectedLang.setLayoutParams(params);
        }
//        else  if(languageArrayList.size()<8)
//        {
//            ViewGroup.LayoutParams params=recyclerViewSelectedLang.getLayoutParams();
//            params.height=500;
//            recyclerViewSelectedLang.setLayoutParams(params);
//        }


    }

    public static ArrayList<Language> selectlanguageList = new ArrayList<>();

    private void initSelectedLang() {


        selectlanguageList = new ArrayList<>();
        for (Map.Entry<String, Language> ee : langHashMap.entrySet()) {
            String key = ee.getKey();
            Language language = ee.getValue();
            selectlanguageList.add(language);
            System.out.println("language_cd" + key);
            // TODO: Do something.
        }

        SingleLanguageSelectionAdapter singleLanguageSelectionAdapter = new SingleLanguageSelectionAdapter(context, selectlanguageList, new ApiCallback() {
            @Override
            public void result(String x) {
                imgAdd.setVisibility(View.VISIBLE);
                int position = Integer.parseInt(x);
                selectCategoryArrayList = new ArrayList<>();
                Language language = selectlanguageList.get(position);
                languageName = language.getNative_name();
                languageCd = language.getLanguage_cd();
                if (categoryHashMap.size() > 0) {
                    if (categoryHashMap.get(languageCd) != null) {
                        selectCategoryArrayList = categoryHashMap.get(languageCd);
                    }
                }
                callGetCategory();
                //callGetCategory(getParam(), Urls.GETCATEGORYBYLANGUAGE);
            }
        });
        recyclerViewlang.setAdapter(singleLanguageSelectionAdapter);


    }

    private void initSetCategory() {
card_view.setVisibility(View.VISIBLE);
        selectCategoryArrayList=new ArrayList<>();
        if (categoryLangHashMap.size() > 0)
        {

            if(categoryLangHashMap.get(languageCd)!=null)
            {

                for (Map.Entry<String, HashMap<String, Category>> ee : categoryLangHashMap.entrySet())
                {
                    catgStoreHashMap =ee.getValue();
                    if (catgStoreHashMap.size()>0)
                    {
                        for (Map.Entry<String, Category> eee : catgStoreHashMap.entrySet())
                        {
                            String key = eee.getKey();
                            Category category = eee.getValue();
                            selectCategoryArrayList.add(category);
                            System.out.println("language_cd" + key);
                            // TODO: Do something.
                        }
                        SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(context, selectCategoryArrayList);
                        recyclerView.setAdapter(selectCategoryAdapter);

                        if(selectCategoryArrayList.size()>10)
                        {
                            if(categoryArrayList.size()>10)
                            {
                                ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
                                params.height=1000;
                                recyclerView.setLayoutParams(params);

                            }
                        }


                    }
                    else
                    {
                        card_view.setVisibility(View.GONE);
                    }


                }




            }
            else {
                card_view.setVisibility(View.GONE);
            }
        }
//
//        languageCategoryDataArrayList = new ArrayList<>();
//        if (arrayListHashMap.size() > 0) {
//
//            if (arrayListHashMap.get(languageCd) != null) {
//                LanguageCategory languageCategoryData = arrayListHashMap.get(languageCd);
//                selectCategoryArrayList = languageCategoryData.getCategoryList();
//                if (selectCategoryArrayList.size() > 0)
//                {
//                    SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(context, selectCategoryArrayList);
//                    recyclerView.setAdapter(selectCategoryAdapter);
//                } else {
//                    card_view.setVisibility(View.GONE);
//                }
//            } else {
//                card_view.setVisibility(View.GONE);
//            }
//        } else {
//            card_view.setVisibility(View.GONE);
//        }


    }


    private void callGetCategory(Map<String, Object> param, String url) {

        try {
            catgStoreHashMap=new HashMap<>();
            categoryArrayList=new ArrayList<>();
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    dismiss_loading();
                    //card_viewCategory.setVisibility(View.VISIBLE);

                    layCatgimagAdd.setVisibility(View.GONE);
                    card_view.setVisibility(View.VISIBLE);
                    categoryStr=js.toString();
                    Intent intent=new Intent(context, SearchLanguageCategoryActivity.class);
                    intent.putExtra("DATA",categoryStr);
                    intent.putExtra("TYPE","2");
                    intent.putExtra("LANG_CD",languageCd);
                    intent.putExtra("LANG_NAME",languageName);
                    startActivityForResult(intent, Constant.PICK_CATEGORY);
//                        JSONArray jsonArray = js.getJSONArray("Category");
//                        categoryArrayList = new ArrayList<>();
//                        Category category0 = new Category();
//                        category0.setCategoryID("");
//                        category0.setCategoryName("Select Category");
//                        categoryArrayList.add(category0);
//                        Category category1 = new Category();
//                        category1.setCategoryID("");
//                        category1.setCategoryName("Add New Category");
//
//                        categoryArrayList.add(category1);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            Category category = null;
//
//                            category = new Category();
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            category.setCategoryID(jsonObject.getString("category_cd"));
//                            category.setCategoryName(jsonObject.getString("categoryName"));
//
//                            categoryArrayList.add(category);
//                        }
//
//                        if(categoryArrayList.size()>10)
//                        {
//                            ViewGroup.LayoutParams params=recyclerViewCategory.getLayoutParams();
//                            params.height=1000;
//                            recyclerViewCategory.setLayoutParams(params);
//
//                        }
//                        else
//                        {
//                            ViewGroup.LayoutParams params=recyclerViewCategory.getLayoutParams();
//                            params.height=700;
//                            recyclerViewCategory.setLayoutParams(params);
//                        }
//
//
//                        if (categoryArrayList.size() > 0) {
//
//                            CategorySelectionAdapter categorySelectionAdapter = new CategorySelectionAdapter(context, categoryArrayList, new ApiCallback() {
//                                @Override
//                                public void result(String x) {
//                                    int position = Integer.parseInt(x);
//
//                                    Category category0 = categoryArrayList.get(position);
//
//                                    card_viewCategory.setVisibility(View.GONE);
//                                    card_view.setVisibility(View.VISIBLE);
//                                    layLangimagAdd.setVisibility(View.GONE);
//                                    layCatgimagAdd.setVisibility(View.GONE);
//                                    imgAdd.setVisibility(View.VISIBLE);
//                                    String item = category0.getCategoryName();
//                                    if (!item.equals("Select Category"))
//                                    {
//                                        if (item.equals("Add New Category"))
//                                        {
//                                            callAddCAtegoryDialog(languageCd, languageName);
//
//                                        } else {
//                                            if (categoryArrayList.size() > 0)
//                                            {
//                                                Category category = categoryArrayList.get(position);
//                                                catgStoreHashMap.put(category.getCategoryID(), category);
//                                                categoryLangHashMap.put(languageCd, catgStoreHashMap);
//                                                initSetCategory();
//                                            }
//                                        }
//                                    }
//
//
//                                }
//                            });
//                            recyclerViewCategory.setAdapter(categorySelectionAdapter);
//
//
//                           // initSetCategory();
//                        } else {
//                            card_view.setVisibility(View.GONE);
//                            showToast("there is no category for this language");
//                        }


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

    private Map<String, Object> getParam()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("Language_cd", languageCd);
        return params;
    }


    private void callGetCategory() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getCategories(getCatgParameter());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getInt("status")==200)
                            {

                                layCatgimagAdd.setVisibility(View.GONE);
                                card_view.setVisibility(View.VISIBLE);
                                categoryStr=js.toString();
                                Intent intent=new Intent(context, SearchLanguageCategoryActivity.class);
                                intent.putExtra("DATA",categoryStr);
                                intent.putExtra("TYPE","2");
                                intent.putExtra("LANG_CD",languageCd);
                                intent.putExtra("LANG_NAME",languageName);
                                startActivityForResult(intent, Constant.PICK_CATEGORY);
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
    private JsonObject getCatgParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            jsonArray.put(languageCd);
            jsonObject.put("language_cd",jsonArray);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }




    public void callAddCAtegoryDialog(String languageCd, String languageName) {
        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_add_category);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            editCategory = (CustomEditText) dd.findViewById(R.id.editCategory);


            ((CustomTextView) dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String categoryNew = editCategory.getText().toString();
                    if (!categoryNew.isEmpty()) {
//                        callAddNewCategory(languageCd, languageName, categoryNew);
                    } else {
                        editCategory.setError("please insert category");
                    }

                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }

    public void getLangIDs()
    {
        StringBuffer lang_id = new StringBuffer("");

        for(int i=0;i<languageArrayList.size();i++)
        {
            lang_id.append(languageArrayList.get(i).getLanguage_cd()+",");
        }

        if (!lang_id.toString().equals(""))
        {
            String selected = "" + lang_id;
            languageCd = selected.substring(0, selected.length() - 1);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtBack:
                finish();
                break;
            case R.id.txtNext:
                //startActivity(new Intent(context, CategoryActivity.class));
                if(selectCategoryArrayList.size()>0)
                {
                    StringBuffer lang_id = new StringBuffer("");

                    for (int i = 0; i < languageArrayList.size(); i++)
                    {
                        Language language = languageArrayList.get(i);
                        String cd = language.get_id();
                        JSONArray jsonArray=new JSONArray();
                        jsonArray.put(cd);
                        lang_id.append("" + cd + ",");

                    }
                    String selected = "" + lang_id;
                    selected = selected.substring(0, selected.length() - 1);
                    DataPrefrence.setPref(context, Constant.LANGUAGE_CD, selected);



                    StringBuffer catg_id = new StringBuffer("");
                    for (int i = 0; i < selectCategoryArrayList.size(); i++) {
                        Category category = selectCategoryArrayList.get(i);
                        String cd = category.getCategoryID() ;
                        catg_id.append("" + cd + ",");
                    }
                    String catg_selected = "" + catg_id;
                    catg_selected = catg_selected.substring(0, catg_selected.length() - 1);
                    DataPrefrence.setPref(context, Constant.CATEGORY_CD,catg_selected);

                    startActivity(new Intent(context, VerificationCodeActivity.class));
                }
                else
                {
                    showToast("please select at least on category");
                }


                break;

            case R.id.imgAdd:


                Intent intent=new Intent(context, SearchLanguageCategoryActivity.class);
                intent.putExtra("DATA",categoryStr);
                intent.putExtra("TYPE","2");
                intent.putExtra("LANG_CD",languageCd);
                intent.putExtra("LANG_NAME",languageName);
                startActivityForResult(intent, Constant.PICK_CATEGORY);
//                layLangimagAdd.setVisibility(View.VISIBLE);
//                layCatgimagAdd.setVisibility(View.VISIBLE);

                break;
            case R.id.imgAddItem:

                layLangimagAdd.setVisibility(View.GONE);
                layCatgimagAdd.setVisibility(View.GONE);
                card_viewSelectedLang.setVisibility(View.GONE);
                card_viewCategory.setVisibility(View.GONE);
                card_viewlang.setVisibility(View.GONE);
                card_view.setVisibility(View.GONE);
                break;

            case R.id.imgAddCategroy:
                card_view.setVisibility(View.GONE);
                card_viewCategory.setVisibility(View.VISIBLE);

                layCatgimagAdd.setVisibility(View.GONE);
                break;
            case R.id.txtCancel:
                card_viewCategory.setVisibility(View.GONE);
                card_view.setVisibility(View.VISIBLE);
                layLangimagAdd.setVisibility(View.GONE);
                layCatgimagAdd.setVisibility(View.GONE);
                imgAdd.setVisibility(View.VISIBLE);
                initSetCategory();
                break;



        }
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.PICK_LANGUAGE) {
            if(data!=null){
                if (data.getStringExtra("ID") != null)
                {
                    languageCd = data.getStringExtra("ID");

                }
            }
        }
        if(resultCode== Constant.PICK_CATEGORY)
        {
            if(data!=null)
            {

                String catgoryName="",categoryId="";
                if (data.getStringExtra("ID") != null) {
                   categoryId  = data.getStringExtra("ID");
                }
                if (data.getStringExtra("NAME") != null) {
                    catgoryName = data.getStringExtra("NAME");
                }

                Category category=new Category();
                category.setCategoryID(categoryId);
                category.setCategoryName(catgoryName);
                catgStoreHashMap.put(category.getCategoryID(), category);
                categoryLangHashMap.put(languageCd, catgStoreHashMap);
                initSetCategory();

            }
        }

    }
}
