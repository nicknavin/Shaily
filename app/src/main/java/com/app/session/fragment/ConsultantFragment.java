package com.app.session.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.activity.SelectRegistertLanguageActivity;
import com.app.session.adapter.AutoCompleteLangAdapter;
import com.app.session.adapter.MyExpandableAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.AnimatedExpandableListView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Profession;
import com.app.session.data.model.RegisterLanguage;
import com.app.session.data.model.Specialist;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;


public class ConsultantFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ConsultantFragment";
    Context context;
    String language_name = "", language_cd = "en";
    AutoCompleteTextView edtSearch;
    private ArrayList<RegisterLanguage> languageArrayList = new ArrayList<>();
    AutoCompleteLangAdapter autoCompleteLangAdapter;
    ArrayList<Specialist> specialist_item = new ArrayList<>();
    ArrayList<Profession> professions_List = new ArrayList<>();
    MyExpandableAdapter adapter;
    AnimatedExpandableListView list_menu;

Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity=getActivity();
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultant, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

    }

    public void initView(View view) {


        list_menu = (AnimatedExpandableListView) view.findViewById(R.id.list_menu);

        edtSearch = (AutoCompleteTextView) view.findViewById(R.id.edtSearch);

        list_menu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group_selected
                // expansion/collapse.
                if (list_menu.isGroupExpanded(groupPosition)) {
                    list_menu.collapseGroupWithAnimation(groupPosition);
                    ((ImageView) v.findViewById(R.id.indicator)).setImageResource(R.mipmap.dropdown_menu);
                    //.setImageResource(isExpanded ? R.drawable.dropdownicon : R.drawable.dropdownicon);
                } else {
                    ((ImageView) v.findViewById(R.id.indicator)).setImageResource(R.mipmap.dropdown_menu01);

                    list_menu.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
        list_menu.setGroupIndicator(null);
        if (Utility.isConnectingToInternet(context)) {

            callGetSubCategory(getParamSubCateg("en"), Urls.GET_LANGUAGE_SUBCATEGORY);

        }

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            if (baseActivity.isConnectingToInternet(context)) {
//                callGetSubCategory(getParamSubCateg("en"), Urls.GET_LANGUAGE_SUBCATEGORY);
            } else {
                showInternetPop(context);
            }
        }
    }

    private void callGetSubCategory(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(baseActivity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {
                        JSONArray jsonArray = js.getJSONArray("categort_list");
                        professions_List = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            Profession profession = new Profession();
                            profession.setCategory(jobj.getString("category"));
                            JSONArray jsonArr = jobj.getJSONArray("data");
                            specialist_item = new ArrayList<>();
                            for (int j = 0; j < jsonArr.length(); j++) {
                                JSONObject obj = jsonArr.getJSONObject(j);
                                Specialist specialist = new Specialist();

                                specialist.setProfession(obj.getString("profession"));
                                specialist.setIcon_address(obj.getString("icon_address"));
                                specialist.setSubcategory_cd(obj.getString("subcategory_cd"));
                                specialist.setCategory_cd(obj.getString("category_cd"));
                                specialist_item.add(specialist);
                                profession.setSpecial_List(specialist_item);
                            }
                            professions_List.add(profession);
//                            list_menu.expandGroupWithAnimation(i);
                        }
                        if (professions_List.size() > 0) {
                            adapter = new MyExpandableAdapter(context, professions_List, language_cd);
                            list_menu.setAdapter(adapter);


                            for (int i = 0; i < professions_List.size(); i++) {
                                list_menu.expandGroupWithAnimation(i);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized(context, getActivity());
                        showToast(failed);
                    } else {
                        MyDialog.iPhone(failed, context);
                    }
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
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamSubCateg(String lang_cd) {

        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("user_language", lang_cd);
        Log.e(TAG, "getParams: getCountry lang : " + lang);
        return params;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Constant.PICK_PREF_LANGUAGE) {

            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    language_name = data.getStringExtra("NAME");

                }
                if (data.getStringExtra("ID") != null) {
                    language_cd = data.getStringExtra("ID");
                }

            }
            callGetSubCategory(getParamSubCateg(language_cd), Urls.GET_LANGUAGE_SUBCATEGORY);
        }


    }

    private MenuItem menuItem_cart, menuItem_search, menuItem_notification, menuItem_touch, menuItem_setting;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_main, menu);
//        menuItem_setting = menu.findItem(R.id.action_setting);
//        menuItem_notification = menu.findItem(R.id.action_notification);
        menuItem_search = menu.findItem(R.id.action_search);
        menuItem_search.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
//            case android.R.id.home:
//                openLeft();
//                break;
            case R.id.action_search:
                intent = new Intent(context, SelectRegistertLanguageActivity.class);
                startActivityForResult(intent, Constant.PICK_PREF_LANGUAGE);
//                startActivity(new Intent(context, FilterTabActivity.class));
                break;

            default:
                break;
        }

        return true;
    }

    private void callGetLangauge(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    dismiss_loading();

                    try {
                        JSONArray jsonArray = js.getJSONArray("languages_dt");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            RegisterLanguage language = new RegisterLanguage();
                            language.setLanguage_cd(jsonObject.getString("language_cd"));
                            language.setLanguage_name(jsonObject.getString("language_name").replace(",", " "));
                            languageArrayList.add(language);

                        }
                        edtSearch.setThreshold(1);
                        autoCompleteLangAdapter = new AutoCompleteLangAdapter(context, languageArrayList);
                        edtSearch.setAdapter(autoCompleteLangAdapter);


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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        MenuItem item=menu.findItem(R.id.action_search);
//        item.setVisible(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

//            case R.id.imgsearch:
//
//                  intent = new Intent(context, SelectRegistertLanguageActivity.class);
//                startActivityForResult(intent, Constant.PICK_PREF_LANGUAGE);
//
//                break;


        }
    }


}
