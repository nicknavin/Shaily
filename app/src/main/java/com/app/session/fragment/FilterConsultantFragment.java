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

import com.app.session.adapter.SelectConsultantAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CustomEditText;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.FilterConsultant;
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

public class FilterConsultantFragment extends BaseFragment {
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<FilterConsultant> consultantArrayList = new ArrayList<>();
    private ArrayList<FilterConsultant> searchlanguageArrayList = new ArrayList<>();
    String language_cd = "en";
    ImageView imgBack;
    public CustomEditText edtSearch;
    SelectConsultantAdapter selectConsultantAdapter;
    View view;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_consultant);
//        context = this;
//        if (getIntent().getStringExtra("ID") != null) {
//            language_cd = getIntent().getStringExtra("ID");
//
//        }
//        initHtml();
//        if (isConnectingToInternet(context)) {
//            callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);
//
//        } else {
//            showInternetPop(context);
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            language_cd = getArguments().getString("ID");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_consultant, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        edtSearch = (CustomEditText) view.findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String key = s.toString().trim();

                if (key.length() == 0) {
                    refresh();
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    searchPrefLanguage("" + key);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (baseActivity.isConnectingToInternet(context)) {
            callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);
        } else {
            showInternetPop(context);
        }
    }


    private void searchPrefLanguage(String key) {

        searchlanguageArrayList = new ArrayList<>();
        if (consultantArrayList.size() > 0) {

            for (int i = 0; i < consultantArrayList.size(); i++) {
                FilterConsultant language = consultantArrayList.get(i);
                String languageName = language.getUser_name().toLowerCase();
                if (key.length() < languageName.length()) {
                    String ss = languageName.substring(0, key.length());
                    if (key.toLowerCase().startsWith(ss)) {
                        searchlanguageArrayList.add(language);

                    }
                }
            }

            if (searchlanguageArrayList.size() > 0) {
                selectConsultantAdapter = new SelectConsultantAdapter(context, searchlanguageArrayList, new ApiCallback() {
                    @Override
                    public void result(String x) {
                        Intent intent = new Intent();
                        intent.putExtra("NAME", x);
                        intent.putExtra("TYPE", Constant.PICK_CONSULTANT_NAME);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                });
                recyclerView.setAdapter(selectConsultantAdapter);

            }
        }


    }

    private void refresh() {
        selectConsultantAdapter = new SelectConsultantAdapter(context, consultantArrayList, new ApiCallback() {
            @Override
            public void result(String x) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("NAME", x);
                getActivity().setResult(getActivity().RESULT_OK, intent);
                getActivity().finish();
            }
        });
        recyclerView.setAdapter(selectConsultantAdapter);
    }


    private void callSearchConsultant(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();


                    try {
                        consultantArrayList = new ArrayList<>();
                        JSONArray array = js.getJSONArray("consultans_list_dt");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            FilterConsultant consultant = new FilterConsultant();
                            consultant.setUser_cd(object.getString("user_cd"));
                            consultant.setUser_name(object.getString("user_name"));
                            consultant.setCountry_cd(object.getString("country_cd"));

                            if (object.getString("country_icon") != null) {
                                consultant.setCountry_icon(object.getString("country_icon"));
                            }
                            if (object.getString("imageUrl") != null) {
                                consultant.setImageUrl(object.getString("imageUrl"));
                            }
                            consultantArrayList.add(consultant);

                        }
                        selectConsultantAdapter = new SelectConsultantAdapter(context, consultantArrayList, new ApiCallback() {
                            @Override
                            public void result(String x) {


//                                Intent intent = new Intent();
//                                intent.putExtra("NAME", x);
//
//                                intent.putExtra("TYPE", Constant.PICK_CONSULTANT_NAME);
//                                getActivity().setResult(Activity.RESULT_OK, intent);
//                                getActivity().finish();
//                                int position = Integer.parseInt(x);
//                                FilterConsultant consultant = consultantArrayList.get(position);
//                                Intent intent = new Intent(context,ConsultantFollowActivity.class);
//                                intent.putExtra("ID",consultant.getUser_cd());
//                                getActivity().startActivity(intent);


                            }
                        });
//                        recyclerView.setAdapter(selectConsultantAdapter);
                        recyclerView.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
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

    private Map<String, Object> getParamSearchConsultant() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("consultant_name", "");
        params.put("profession_code", "");
        params.put("language_cd", language_cd);
        params.put("country_cd", "");

        return params;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            if (baseActivity.isConnectingToInternet(context)) {
//                callSearchConsultant(getParamSearchConsultant(), Urls.GET_FILTER_CONSULTANTS);
            } else {
                showInternetPop(context);
            }
        }
    }

}
