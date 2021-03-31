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
import com.app.session.adapter.SearchTitleAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.TitleCallback;
import com.app.session.data.model.TitleRef;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAddTitleActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    String key;
    CustomEditText edtSearch;
    SearchTitleAdapter searchTitleAdapter;
    ImageView imgsearch, imgCross, imgBack;
    String languageCd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_language_category);

        if (getIntent().getStringExtra("language_cd") != null) {
            languageCd = getIntent().getStringExtra("language_cd");
        }

        initView();

        getTitleRef();


    }


    private void initView() {


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        imgsearch = (ImageView) findViewById(R.id.imgsearch);
        imgCross = (ImageView) findViewById(R.id.imgCross);
        imgCross.setOnClickListener(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        edtSearch = (CustomEditText) findViewById(R.id.edtSearch);
        edtSearch.setHint(context.getResources().getString(R.string.search_addtitle));
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                key = s.toString().trim();


                searchTitleAdapter.getFilter().filter(key);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    searchTitleAdapter.getFilter().filter(key);


                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCross:
                edtSearch.setText(null);
//                setLayoutMargin(0);
                break;

        }
    }


    private void getTitleRef() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.GET_TITLE_REF, getParamTitleRef(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        ArrayList<TitleRef> titleRefsList = new ArrayList<>();
                        if (jsonObject.getString("rstatus").equals("1")) {

                            if (!js.isNull("title_reflist")) {

                                JSONArray jsonArray = js.getJSONArray("title_reflist");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    TitleRef titleRef = new TitleRef();
                                    titleRef.setLanguage_cd(object.getString("language_cd"));
                                    titleRef.setTitle_cd(object.getString("title_cd"));
                                    titleRef.setTitle_name(object.getString("title_name"));
                                    titleRefsList.add(titleRef);
                                    System.out.println("dsfsd" + titleRefsList.size());
                                }
                                TitleRef titleRef = new TitleRef();
                                titleRef.setTitle_name("Add New Title");
                                titleRef.setTitle_cd("0");
                                titleRef.setLanguage_cd(languageCd);
                                titleRefsList.add(titleRef);
                                searchTitleAdapter=new SearchTitleAdapter(context, titleRefsList, new TitleCallback() {
                                    @Override
                                    public void getTitle(TitleRef titleRef)
                                    {
                                        if(titleRef.getTitle_name().equals("Add New Title"))
                                        {
                                            callAddNewTitleDialog();
                                        }
                                        else
                                        {
                                            Intent returnIntent = new Intent();
                                            returnIntent.putExtra("result",titleRef.getTitle_name());
                                            returnIntent.putExtra("id",titleRef.getTitle_cd());
                                            setResult(Constant.REQUEST_RESULT, returnIntent);
                                            finish();
                                        }
                                    }
                                });


                                recyclerView.setAdapter(searchTitleAdapter);
                            }
                            else
                            {
                                TitleRef titleRef = new TitleRef();
                                titleRef.setTitle_name("Add New Title");
                                titleRef.setTitle_cd("0");
                                titleRef.setLanguage_cd(languageCd);
                                titleRefsList.add(titleRef);
                                searchTitleAdapter=new SearchTitleAdapter(context, titleRefsList, new TitleCallback() {
                                    @Override
                                    public void getTitle(TitleRef titleRef)
                                    {
                                        if(titleRef.getTitle_name().equals("Add New Title"))
                                        {
                                            callAddNewTitleDialog();
                                        }
                                        else
                                        {
                                            Intent returnIntent = new Intent();
                                            returnIntent.putExtra("result",titleRef.getTitle_name());
                                            returnIntent.putExtra("id",titleRef.getTitle_cd());
                                            setResult(Constant.REQUEST_RESULT, returnIntent);
                                            finish();
                                        }
                                    }
                                });


                                recyclerView.setAdapter(searchTitleAdapter);
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    showToast(success);

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    showToast(failed);


                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            }).execute();

        } else {
            showInternetConnectionToast();
        }
    }

    private String getParamTitleRef() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("language_cd", languageCd);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void callAddNewTitleDialog() {
        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_add_new_title);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            CustomEditText editTitle= (CustomEditText) dd.findViewById(R.id.editTitle);

            ((CustomTextView) dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titleNew = editTitle.getText().toString();
                    if (!titleNew.isEmpty())
                    {
                        callAddTitleUpdate(titleNew,"0");
                        //callAddNewCategory(languageCd,  categoryNew);
                    } else {
                        editTitle.setError("please insert category");
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


    private void callAddTitleUpdate(String titleName, String id)
    {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.ADD_TITLE_REF, getParamAddTitle(titleName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1"))
                        {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result",titleName);
                            returnIntent.putExtra("id",id);
                            setResult(Constant.REQUEST_RESULT, returnIntent);
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    showToast(success);

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    showToast(failed);


                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            }).execute();

        } else {
            showInternetConnectionToast();
        }
    }

    private String getParamAddTitle(String titleName) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("language_cd", languageCd);
            jsonObject.put("title_name", titleName);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
