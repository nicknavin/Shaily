package com.app.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.adapter.LanguageEditAdapter;
import com.app.session.adapter.ProfileCategoryAdapter;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.data.model.AddBriefCv;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.Category;
import com.app.session.data.model.CategoryBody;
import com.app.session.data.model.CategoryRoot;
import com.app.session.data.model.CountryId;
import com.app.session.data.model.DeleteLangReq;
import com.app.session.data.model.LanguageCd;
import com.app.session.data.model.ReqDeleteCategory;
import com.app.session.data.model.RequestUpdateCategory;
import com.app.session.data.model.Root;
import com.app.session.data.model.User;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserLangauges;
import com.app.session.data.model.UserRoot;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLanguageActivity extends BaseActivity {
RecyclerView recyclerView;
    LanguageEditAdapter languageEditAdapter;
    ProfileCategoryAdapter profileCategoryAdapter;
    LinearLayout layAddLang;
    String language_name="",language_cd="",country_cd="",categoryId="";
    String profileData="";
    ArrayList<Brief_CV> languageArrayList = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    ArrayList<UserLangauges> userLangauges=new ArrayList<>();

    CustomTextView edt_selectLang,txttitle;
    String type="";
    DeleteLangReq deleteLangReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_language);
        type=getIntent().getStringExtra("TYPE");
        if(getIntent().getStringExtra("LANGUAGE_CD")!=null)
        {
            language_cd=getIntent().getStringExtra("LANGUAGE_CD");
        }
        initView();
    }


    private void initView()
    {


        if(type.equals("1")) {
            ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.lang_speak));
            ((CustomTextView) findViewById(R.id.edt_selectLang)).setText(context.getResources().getString(R.string.add_lang));
            ((CustomTextView) findViewById(R.id.txttitle)).setText(context.getResources().getString(R.string.lang_speak));

        }
        else
        {
            ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.category));
            ((CustomTextView) findViewById(R.id.edt_selectLang)).setText(context.getResources().getString(R.string.add_category));
            ((CustomTextView) findViewById(R.id.txttitle)).setText(context.getResources().getString(R.string.category));
        }

        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layAddLang=(LinearLayout)findViewById(R.id.layAddLang);
        layAddLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Intent intent=new Intent(context, SearchLanguageCategoryActivity.class);
                intent.putExtra("TYPE",type);
                if(type.equals("1")) {
                    startActivityForResult(intent, Constant.PICK_LANGUAGE);
                }
                if(type.equals("2")) {
                    intent.putExtra("LANG_CD",language_cd);
                    intent.putExtra("LANG",userLangauges);
                    startActivityForResult(intent, Constant.PICK_CATEGORY);
                }

            }
        });



        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_lang);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        getUserDetail();


    }






    private void getUserDetail() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            UserId user=new UserId();
            user.setUser_id(userId);
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<UserRoot> call = apiInterface.getUserDetails(accessToken, user);
            call.enqueue(new retrofit2.Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        UserRoot root = response.body();
                        if(root.getStatus()==200) {

                            User user = root.getUserBody().getUser();
                           CountryId country_id = user.getCountry_id();
                            country_cd=country_id.get_id();




                            int langsize = user.getUserLangauges().size();
                            userLangauges=user.getUserLangauges();
                            StringBuffer lang_name = new StringBuffer("");
                            StringBuffer lang_id = new StringBuffer("");

                            if (type.equals("1")) {
                                languageArrayList = new ArrayList<>();
                                for (int i = 0; i < langsize; i++) {
                                    UserLangauges userLangauges = user.getUserLangauges().get(i);
                                    Brief_CV brief_cv = new Brief_CV();
                                    brief_cv.setChecked(false);
                                    brief_cv.setLanguage_cd(user.getUserLangauges().get(i).get_id());
                                    brief_cv.setEnglish_name(userLangauges.getName());
                                    brief_cv.setNative_name(userLangauges.getNativeName());
                                    brief_cv.setBrief_cv("");
                                    brief_cv.setSer_no("");
                                    brief_cv.setLanguage_id(userLangauges);
                                    lang_name.append(userLangauges.getNativeName() + ",");
                                    lang_id.append(userLangauges.get_id() + ",");
                                    languageArrayList.add(brief_cv);
                                }

                                languageEditAdapter = new LanguageEditAdapter(context, languageArrayList,true, new ObjectCallback() {
                                    @Override
                                    public void getObject(Object object, int position, View view)
                                    {
                                        Brief_CV brief_cv=(Brief_CV)object;
                                        deleteLangReq=new DeleteLangReq();
                                        deleteLangReq.setUser_id(userId);
                                        deleteLangReq.setUserLangauges(brief_cv.getLanguage_id().get_id());
                                        callDeleteLanguage(brief_cv,position);
                                    }


                                });
                                recyclerView.setAdapter(languageEditAdapter);
                            }


                            categoryArrayList=new ArrayList<>();
                            if(type.equals("2")) {


                                for (int i=0;i<user.getUserCategory().size();i++)
                                {
                                    Category category=new Category();
                                    category.setCategoryName(user.getUserCategory().get(i).getCategoryName());
                                    category.setCategoryID(user.getUserCategory().get(i).getId());
                                    categoryArrayList.add(category);

                                }
                                profileCategoryAdapter = new ProfileCategoryAdapter(context, categoryArrayList, true, new ApiItemCallback() {
                                    @Override
                                    public void result(int position)
                                    {
                                        Category category=categoryArrayList.get(position);
//                                                callRemoveCategory(getParamRemoveCatg(category.getCategoryID()), Urls.REMOVE_CATEGORY,category,position);
                                        callRemoveCategory(category.getCategoryID(),category,position);

                                    }
                                });
                                recyclerView.setAdapter(profileCategoryAdapter);


                            }






                        }
                    }





                }

                @Override
                public void onFailure(Call<UserRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }




String langId="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PICK_LANGUAGE) {


            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    language_name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    language_cd = data.getStringExtra("ID");
                }
                if (data.getStringExtra("IDs") != null) {
                    langId = data.getStringExtra("IDs");
                }

                if (isConnectingToInternet(context)) {
//                    callUpdateLanguage(getParamUpdateLanguage(), Urls.SET_USER_LANGUAGE);
                    callUpdateLanguage();
                } else {
                    showInternetPop(context);
                }

            }

        }
        if (resultCode == Constant.PICK_CATEGORY) {
            if (data != null) {

                String catgoryName = "", categoryId = "";
                if (data.getStringExtra("ID") != null) {
                    categoryId = data.getStringExtra("ID");
                }
                if (data.getStringExtra("NAME") != null) {
                    catgoryName = data.getStringExtra("NAME");
                }

                Category category = new Category();
                category.setCategoryID(categoryId);

                category.setCategoryName(catgoryName);


                if (isConnectingToInternet(context)) {
                    //callConsultant_Specialties(getParamUpdateSpecialites(categoryId), Urls.SET_CONSULTANT_SPECIALTIES);
                    callUpdateCategory(categoryId);
                } else {
                    showInternetPop(context);
                }


            }
        }

    }





    private void callUpdateLanguage()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.reqUpateUserLanguage( accessToken,getUpdateLangParameter());
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
                                callAddBriefCv(language_cd);

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
    private JsonObject getUpdateLangParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("user_id",userId);

            jsonObject.put("userLangauges",language_cd);
            JsonParser jsonParser = new JsonParser();

            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }


    private void callAddBriefCv(String id)
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            AddBriefCv addBriefCv =new AddBriefCv();
addBriefCv.setLanguageId(id);
addBriefCv.setUserId(userId);
addBriefCv.setBriefCv(" ");

            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<Root> call = apiInterface.reqAddBriefCv(accessToken,addBriefCv);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {

                    getUserDetail();
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {

                }
            });

        } else {
            showInternetConnectionToast();
        }
    }







    private void callUpdateCategory(String categoryId)
    {
        if (isConnectingToInternet(context)) {
            showLoading();


            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            RequestUpdateCategory requestUpdateCategory=new RequestUpdateCategory();
            requestUpdateCategory.setUserId(userId);
            requestUpdateCategory.setUserCategory(categoryId);
            Call<Root> call=apiInterface.reqUpateUserCategory(accessToken,requestUpdateCategory);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    if(response.body()!=null)
                    {
                        getUserDetail();
                    }

                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {

                }
            });
//            Call<ResponseBody> call = apiInterface.callUpdateCategory(userId, accessToken,categoryId,country_cd);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    dismiss_loading();
//                    ResponseBody responseBody = response.body();
//                    try {
//                        String data = responseBody.string();
//                        try {
//
//                            JSONObject js = new JSONObject(data);
//
//                            if (js.getBoolean("Status")) {
//
//                                getUserDetail();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dismiss_loading();
//                }
//            });
        } else {
            showInternetConnectionToast();
        }
    }



    private void callRemoveCategory(String categoryId, final Category category, final int position)
    {
        if (isConnectingToInternet(context)) {
            showLoading();

            ReqDeleteCategory reqDeleteCategory=new ReqDeleteCategory();
            reqDeleteCategory.setUserId(userId);
            reqDeleteCategory.setUserCategory(categoryId);

            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<Root> call=apiInterface.reqDeleteUserCategory(accessToken,reqDeleteCategory);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        Root root=response.body();
                        if(root.getStatus()==200)
                        {
                            categoryArrayList.remove(category);
                            profileCategoryAdapter.notifyItemRemoved(position);
                            profileCategoryAdapter.notifyItemRangeChanged(position, categoryArrayList.size());
                        }
                        else
                        {

                        }
                    }

                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {

                }
            });




//            Call<ResponseBody> call = apiInterface.callRemoveCategory(userId, accessToken,categoryId);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    dismiss_loading();
//                    ResponseBody responseBody = response.body();
//                    try {
//                        String data = responseBody.string();
//                        try {
//
//                            JSONObject js = new JSONObject(data);
//
//                            if (js.getBoolean("Status")) {
//
//                                categoryArrayList.remove(category);
//                                profileCategoryAdapter.notifyItemRemoved(position);
//                                profileCategoryAdapter.notifyItemRangeChanged(position, categoryArrayList.size());
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dismiss_loading();
//                }
//            });
        } else {
            showInternetConnectionToast();
        }
    }



    private void callDeleteLanguage(Brief_CV brief_cv,int position)
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.reqDeleteUserLanguage(accessToken, deleteLangReq);

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
                                languageEditAdapter.refreshList(brief_cv,position);
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


    ArrayList<CategoryBody> categoryBodies=new ArrayList<>();
    private void getCategories()
    {
        if (Utility.isConnectingToInternet(context))
        {
            LanguageCd languageCd=new LanguageCd();
            ArrayList<String> langCdList=new ArrayList<>();
            langCdList.add("en");
            langCdList.add("ar");
            langCdList.add("aa");
            languageCd.setLanguageCd(langCdList);
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<CategoryRoot> call = apiInterface.reqGetCategories(accessToken, languageCd);
            call.enqueue(new Callback<CategoryRoot>() {
                @Override
                public void onResponse(Call<CategoryRoot> call, Response<CategoryRoot> response)
                {
                    if(response.body()!=null)
                    {
                        if (response.body().getStatus()==200)
                        {

                            categoryBodies=response.body().getBody();

                        }
                    }
                }

                @Override
                public void onFailure(Call<CategoryRoot> call, Throwable t) {

                }
            });



        } else {
            showInternetConnectionToast();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(Constant.LANG_REFRESH, intent);
        finish();
    }
}

/*  if(!profileData.isEmpty())
        {
            try {
                JSONObject jsonObject=new JSONObject(profileData);
                JSONArray arrayLang = jsonObject.getJSONArray("user_language");
                for(int i=0;i<arrayLang.length();i++)
                {
                    JSONObject object = arrayLang.getJSONObject(i);
                    Brief_CV language = new Brief_CV();
                    language.setChecked(false);
                    language.setLanguage_cd(object.getString("language_cd"));
                    language.setEnglish_name(object.getString("english_name"));
                    language.setNative_name(object.getString("native_name"));
                    if (object.getString("breaf_cv") == null || object.getString("breaf_cv").isEmpty() || object.getString("breaf_cv").equals("null")) {
                        language.setBrief_cv("");
                    } else {
                        language.setBrief_cv(object.getString("breaf_cv"));
                    }
                    language.setSer_no(object.getString("ser_no"));
                    languageArrayList.add(language);

                }

                languageEditAdapter =new LanguageEditAdapter(context, languageArrayList, new ApiCallback() {
                    @Override
                    public void result(String x)
                    {

                    }
                });
                recyclerView.setAdapter(languageEditAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/