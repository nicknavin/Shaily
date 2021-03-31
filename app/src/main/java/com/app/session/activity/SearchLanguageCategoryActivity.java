package com.app.session.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.session.R;
import com.app.session.adapter.SearchCategoryAdapter;
import com.app.session.adapter.SearchLanguageAdapter;
import com.app.session.adapter.SearchSuggestionAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.DefaultCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.AddCategoryRoot;
import com.app.session.data.model.Category;
import com.app.session.data.model.CategoryId;
import com.app.session.data.model.Language;
import com.app.session.data.model.LanguagesRoot;
import com.app.session.data.model.ReqCategory;
import com.app.session.data.model.UserLangauges;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

public class SearchLanguageCategoryActivity extends BaseActivity implements View.OnClickListener
{
//https://uxplanet.org/top-15-search-github-ui-libraries-and-components-java-swift-8d7403e73aa8
    RecyclerView recyclerView,recyclerViewSuggestion;
    String categoryStr,type,  key;
    CustomEditText edtSearch;
    SearchCategoryAdapter searchCategoryAdapter;
    SearchLanguageAdapter searchLanguageAdapter;
    SearchSuggestionAdapter searchSuggestionAdapter;
    private ArrayList<Language> languageArrayList = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private ArrayList<String> suggenstionArrayList = new ArrayList<>();
    ImageView imgsearch,imgCross,imgBack;
    String languageCd="";
    ArrayList<UserLangauges> userLangauges=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_language_category);
        languageArrayList = DataPrefrence.getLanguage(context, Constant.LANGUAGE_SPEAK);
        if(getIntent().getStringExtra("TYPE")!=null)
        {
            type=getIntent().getStringExtra("TYPE");
            if(type.equals("1"))
            {

                callGetLangauge();
            }
            else if(type.equals("2"))
            {


                    languageCd=getIntent().getStringExtra("LANG_CD");
                  userLangauges=getIntent().getParcelableArrayListExtra("LANG");
                    callGetCategory();



            }
        }



        initView();
        //initSuggestionData();

    }


    private void setLayoutMargin(int visibilty)
    {
     if(visibilty==1)
     {
         imgsearch.setVisibility(View.GONE);
         imgCross.setVisibility(View.VISIBLE);

     }
     else
     {
         imgsearch.setVisibility(View.INVISIBLE);
         imgCross.setVisibility(View.GONE);

     }

    }


    private void initView ()
    {


        imgsearch=(ImageView) findViewById(R.id.imgsearch);
        imgCross=(ImageView) findViewById(R.id.imgCross);
        imgCross.setOnClickListener(this);
        imgBack=(ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        edtSearch = (CustomEditText) findViewById(R.id.edtSearch);

        if(type.equals("1"))
        {
            edtSearch.setHint(context.getResources().getString(R.string.select_search_lang));
        }
        else
        {
            edtSearch.setHint(context.getResources().getString(R.string.select_category));
        }


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                 key = s.toString().trim();

                 if(type.equals("1"))
                 {
                     searchLanguageAdapter.getFilter().filter(key);
                 }
                 else {
                     searchCategoryAdapter.getFilter().filter(key);
                 }


                if(key.length()>0)
                {
                    setLayoutMargin(1);
                }
                else
                {
                    setLayoutMargin(0);
                }

//                 if(key.length()>0) {
//                     recyclerViewSuggestion.setVisibility(View.GONE);
//
//
//                 }
//                 else
//                 {
//                     recyclerViewSuggestion.setVisibility(View.GONE);
//                 }
//
//                 if(suggenstionArrayList.size()>0)
//                 {
//                     searchSuggestionAdapter.getFilter().filter(key);
//                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
//                    if(key.length()>0)
//                    {
//
//                    }else
//                    {
//
//                    }
//                    recyclerView.setVisibility(View.VISIBLE);
//                    recyclerViewSuggestion.setVisibility(View.GONE);
//                    suggenstionArrayList.add(key);
//                    searchSuggestionAdapter.notifyDataSetChanged();

                    if(type.equals("1"))
                    {
                        searchLanguageAdapter.getFilter().filter(key);
                    }
                    else {
                        searchCategoryAdapter.getFilter().filter(key);
                    }

                    return true;
                }
                return false;
            }
        });

    }

    private void initLanguageData()
    {
        searchLanguageAdapter=new SearchLanguageAdapter(context, languageArrayList, new DefaultCallback() {
            @Override
            public void result(int x)
            {

            }

            @Override
            public void getCategory(Category category) {

            }

            @Override
            public void getLanguage(Language language) {
                Intent intent = getIntent();
                intent.putExtra("ID", language.getLanguage_cd());
                intent.putExtra("IDs", language.get_id());
                setResult(Constant.PICK_LANGUAGE, intent);
                finish();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(searchLanguageAdapter);
    }





    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCross:
                edtSearch.setText(null);
//                setLayoutMargin(0);
                break;

        }
    }






    public void callAddCAtegoryDialog(String languageCd) {
        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_add_category);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            CustomEditText editCat= (CustomEditText) dd.findViewById(R.id.editCategory);


            ((CustomTextView) dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String categoryNew = editCat.getText().toString();
                    if (!categoryNew.isEmpty())
                    {
                      //  callAddNewCategory(languageCd,  categoryNew);
                        callAddCategory(languageCd,  categoryNew);
                    } else {
                        editCat.setError("please insert category");
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

String selectLangCd="",selectLangName="";

    public void callLanguageListDialog(Dialog dd)
    {
        dd.dismiss();
        final Dialog ddSelector = new Dialog(context);
        try {
            ddSelector.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            ddSelector.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ddSelector.setContentView(R.layout.dialog_langaugelist);
            ddSelector.getWindow().setLayout(-1, -2);
            ddSelector.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RecyclerView recyclerView=(RecyclerView)ddSelector.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            searchLanguageAdapter=new SearchLanguageAdapter(context, languageArrayList, new DefaultCallback() {
                @Override
                public void result(int x)
                {

                }

                @Override
                public void getCategory(Category category) {

                }

                @Override
                public void getLanguage(Language language)
                {
                    ddSelector.dismiss();
                    selectLangCd=language.get_id();
                    selectLangName=language.getName();
                    callLangCategoryDialog();


                }
            });
            recyclerView.setAdapter(searchLanguageAdapter);


            ddSelector.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }


CustomTextView txtLanguageName;
    TextInputEditText txtSelectCategory;
    public void callLangCategoryDialog()
    {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_lang_category);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtLanguageName=(CustomTextView)dd.findViewById(R.id.txtLanguageName);

            if(!selectLangName.equals(""))
            {
                txtLanguageName.setText(selectLangName);
            }

            txtSelectCategory=(TextInputEditText)dd.findViewById(R.id.txtSelectCategory);


            ((CustomTextView)dd.findViewById(R.id.txtLanguageName)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callLanguageListDialog(dd);
                }
            });
            ((CustomTextView)dd.findViewById(R.id.txtAddCategory)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(!selectLangCd.equals("")) {

                        String categoryNew = txtSelectCategory.getText().toString();
                        if (!categoryNew.isEmpty())
                        {
                          //  callAddNewCategory(languageCd,  categoryNew);
                            callAddCategory(languageCd,  categoryNew);
                        } else {
                            txtSelectCategory.setError("please insert category");
                        }
                    }
                    else
                    {
                        showToast("First select langauge to add new category");
                    }
                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }




    private void callGetCategory(Map<String, Object> param, String url) {

        try {
            categoryArrayList=new ArrayList<>();
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        categoryArrayList = new ArrayList<>();
                        JSONArray jsonArray = js.getJSONArray("Category");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Category category = null;
                            category = new Category();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            category.setCategoryID(jsonObject.getString("category_cd"));
                            category.setCategoryName(jsonObject.getString("categoryName"));
                            categoryArrayList.add(category);
                        }

                        Category category1 = new Category();
                        category1.setCategoryID("");
                        category1.setCategoryName("Add New Category");
                        categoryArrayList.add(category1);

                        searchCategoryAdapter=new SearchCategoryAdapter(context, categoryArrayList, new DefaultCallback() {
                            @Override
                            public void result(int x)
                            {

                            }

                            @Override
                            public void getCategory(Category category)
                            {

                                String item = category.getCategoryName();
                                if (item.equals("Add New Category"))
                                {
                                    if(languageArrayList.size()>1)
                                    {
                                        selectLangName="";
                                        selectLangCd="";
                                        callLangCategoryDialog();

                                    }else {
                                        callAddCAtegoryDialog(languageCd);
                                    }

                                }
                                else {

                                    Intent intent = getIntent();
                                    intent.putExtra("ID", category.getCategoryID());
                                    intent.putExtra("NAME", category.getCategoryName());
                                    setResult(Constant.PICK_CATEGORY, intent);
                                    finish();
                                }
                            }

                            @Override
                            public void getLanguage(Language language) {

                            }


                        });
                        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(searchCategoryAdapter);
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

    private Map<String, Object> getParam()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("Language_cd", languageCd);
        return params;
    }
    private void callGetCategoryold() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getCategory(languageCd);
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
                                    categoryArrayList = new ArrayList<>();
                                    JSONArray jsonArray = js.getJSONArray("Category");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Category category = null;
                                        category = new Category();
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        category.setCategoryID(jsonObject.getString("category_cd"));
                                        category.setCategoryName(jsonObject.getString("categoryName"));
                                        categoryArrayList.add(category);
                                    }

                                    Category category1 = new Category();
                                    category1.setCategoryID("");
                                    category1.setCategoryName("Add New Category");
                                    categoryArrayList.add(category1);

                                    searchCategoryAdapter=new SearchCategoryAdapter(context, categoryArrayList, new DefaultCallback() {
                                        @Override
                                        public void result(int x)
                                        {

                                        }

                                        @Override
                                        public void getCategory(Category category)
                                        {

                                            String item = category.getCategoryName();
                                            if (item.equals("Add New Category"))
                                            {
                                                if(languageArrayList.size()>1)
                                                {
                                                    selectLangName="";
                                                    selectLangCd="";
                                                    callLangCategoryDialog();

                                                }else {
                                                    callAddCAtegoryDialog(languageCd);
                                                }

                                            }
                                            else {

                                                Intent intent = getIntent();
                                                intent.putExtra("ID", category.getCategoryID());
                                                intent.putExtra("NAME", category.getCategoryName());
                                                setResult(Constant.PICK_CATEGORY, intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void getLanguage(Language language) {

                                        }


                                    });
                                    recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerView.setAdapter(searchCategoryAdapter);
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



    private void callGetCategory() {
        if (isConnectingToInternet(context)) {
//            showLoading();

            ReqCategory reqCategory=new ReqCategory();
            ArrayList<String> list=new ArrayList<>();
            for(int i =0;i<userLangauges.size();i++)
            {
                list.add(userLangauges.get(i).getCode());
            }
            reqCategory.setLanguageCd(list);

            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.reqGetCategories(reqCategory);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                   // dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getInt("status")==200)
                            {
                                try {
                                    categoryArrayList = new ArrayList<>();
                                    JSONArray jsonArray = js.getJSONArray("body");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Category category = null;
                                        category = new Category();
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        category.setCategoryID(jsonObject.getString("_id"));
                                        category.setCategoryName(jsonObject.getString("category_name"));
                                        category.setLanguageCd(jsonObject.getString("language_cd"));
                                        categoryArrayList.add(category);
                                    }

                                    Category category1 = new Category();
                                    category1.setCategoryID("");
                                    category1.setCategoryName("Add New Category");
                                    categoryArrayList.add(category1);

                                    searchCategoryAdapter=new SearchCategoryAdapter(context, categoryArrayList, new DefaultCallback() {
                                        @Override
                                        public void result(int x)
                                        {

                                        }

                                        @Override
                                        public void getCategory(Category category)
                                        {

                                            String item = category.getCategoryName();
                                            if (item.equals("Add New Category"))
                                            {
                                                if(languageArrayList.size()>1)
                                                {
                                                    selectLangName="";
                                                    selectLangCd="";
                                                    callLangCategoryDialog();

                                                }else {
                                                    callAddCAtegoryDialog(languageCd);
                                                }

                                            }
                                            else {

                                                Intent intent = getIntent();
                                                intent.putExtra("ID", category.getCategoryID());
                                                intent.putExtra("NAME", category.getCategoryName());
                                                setResult(Constant.PICK_CATEGORY, intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void getLanguage(Language language) {

                                        }


                                    });
                                    recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerView.setAdapter(searchCategoryAdapter);
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
                            searchLanguageAdapter=new SearchLanguageAdapter(context, languageArrayList, new DefaultCallback() {
                                @Override
                                public void result(int x)
                                {

                                }

                                @Override
                                public void getCategory(Category category) {

                                }

                                @Override
                                public void getLanguage(Language language) {
                                    Intent intent = getIntent();
                                    intent.putExtra("ID", language.get_id());
                                    intent.putExtra("IDs", language.get_id());
                                    setResult(Constant.PICK_LANGUAGE, intent);
                                    finish();
                                }
                            });

                            recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(searchLanguageAdapter);
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




    private void callAddNewCategory(String language_cd, String categoryName) {
//        if (isInternetConnected()) {
//            showLoading();
//            new BaseAsych(Urls.ADD_CATEGORY, bodyAddCategory(language_cd, categoryName), new RequestCallback() {
//                @Override
//                public void onSuccess(JSONObject js, String success) {
//                    dismiss_loading();
//                    showToast(success);
//                    try {
//                        JSONObject object = js.getJSONObject("result");
//                        String rdescription = object.getString("rdescription");
//                        if (rdescription.equals("Success")) {
//                            JSONObject obj = js.getJSONObject("categiory_details");
//                            Category category = new Category();
//                            if (obj.getString("addedNewCatgory") != null)
//                            {
//                                category.setCategoryName(obj.getString("addedNewCatgory"));
//                            } else {
//                                category.setCategoryName("demo");
//                            }
//                            category.setCategoryID(obj.getString("category_cd"));
//                            category.setChecked(false);
//
//
//
//                            Intent intent = getIntent();
//                            intent.putExtra("ID", category.getCategoryID());
//                            intent.putExtra("NAME", category.getCategoryName());
//                            setResult(Constant.PICK_CATEGORY, intent);
//                            finish();
//
//
//
//
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailed(JSONObject js, String failed, int status) {
//                    dismiss_loading();
//                    showToast(failed);
//
//
//                }
//
//                @Override
//                public void onNull(JSONObject js, String nullp) {
//                    dismiss_loading();
//                }
//
//                @Override
//                public void onException(JSONObject js, String exception) {
//                    dismiss_loading();
//                }
//            }).execute();
//
//        } else {
//            showInternetConnectionToast();
//        }
    }


    private String bodyAddCategory(String languageCd, String categoryName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("langauge_cd", languageCd);
            jsonObject.put("addedNewCatgory", categoryName);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }








    private void callAddCategory(String langcd,String catg) {
        if (isConnectingToInternet(context)) {
          //  showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<AddCategoryRoot> call = apiInterface.reqAddCategories(addCatgParameter(langcd,catg));
            call.enqueue(new Callback<AddCategoryRoot>() {
                @Override
                public void onResponse(Call<AddCategoryRoot> call, Response<AddCategoryRoot> response) {
                    if(response.body()!=null)
                    {
                        AddCategoryRoot addCategoryRoot=response.body();

                        if(addCategoryRoot.getStatus()==200)
                        {
                            CategoryId categoryId=addCategoryRoot.getBody();
                            Category category = new Category();
                            category.setCategoryName(categoryId.getCategory_name());
                            category.setCategoryID(categoryId.get_id());
                            category.setChecked(false);
                            Intent intent = getIntent();
                            intent.putExtra("ID", categoryId.get_id());
                            intent.putExtra("NAME", categoryId.getCategory_name());
                            setResult(Constant.PICK_CATEGORY, intent);
                            finish();

                        }

                    }
                }

                @Override
                public void onFailure(Call<AddCategoryRoot> call, Throwable t) {

                }
            });

        } else {
            showInternetConnectionToast();
        }
    }
    private JsonObject addCatgParameter(String langcd,String catg)
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("language_cd",langcd);
            jsonObject.put("category_name",catg);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }





}
