package com.app.session.fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {


    Context context;
    ImageView imgEdit;
    CircleImageView imgProfile;
    CustomTextView tvUserCategory, tvUserName, txt_email, txt_mobile, txt_fees, txt_catg, txt_sub_catg, txt_country, txt_languages;
    String imageUrl = "";
    LinearLayout layCatg,laySub;
    View viewcat,viewsubcat;
    String language_name="",subCategoryNames="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUserName = (CustomTextView) view.findViewById(R.id.tvUserName);
        txt_email = (CustomTextView) view.findViewById(R.id.txt_email);
        txt_mobile = (CustomTextView) view.findViewById(R.id.txt_mobile);
        txt_fees = (CustomTextView) view.findViewById(R.id.txt_fees);
        txt_catg = (CustomTextView) view.findViewById(R.id.txt_catg);
        txt_sub_catg = (CustomTextView) view.findViewById(R.id.txt_sub_catg);
        txt_country = (CustomTextView) view.findViewById(R.id.txt_country);
        txt_languages = (CustomTextView) view.findViewById(R.id.txt_languages);
        tvUserCategory = (CustomTextView) view.findViewById(R.id.tvUserCategory);
        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        laySub=(LinearLayout)view.findViewById(R.id.laySub);
        layCatg=(LinearLayout)view.findViewById(R.id.layCatg);
        viewcat=(View)view.findViewById(R.id.viewcat);
        viewsubcat=(View)view.findViewById(R.id.viewsubcat);
        imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(this);


        if (baseActivity.is_consultant.equals("0")&&baseActivity.is_company.equals("0"))
        {
          laySub.setVisibility(View.GONE);
            layCatg.setVisibility(View.GONE);
            viewsubcat.setVisibility(View.GONE);
            viewcat.setVisibility(View.GONE);
            tvUserCategory.setVisibility(View.GONE);
            txt_fees.setVisibility(View.GONE);
        }
        if (baseActivity.isConnectingToInternet(context)) {
            callGetProfile(getParamSubCateg(), Urls.GET_PROFILE);
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            if (baseActivity.isConnectingToInternet(context)) {
                callGetProfile(getParamSubCateg(), Urls.GET_PROFILE);
            }

        }
    }

    private void callGetProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(baseActivity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

//{"Status":true,"Message":"USuccesside..","User_Detail":[{"user_cd":24,"email_address":"thor@gmail.com","login_user_id":"Thor","mobile_no":"7415912174","user_name":"thor","is_company":"0","device_id":"eHO293dBneM:APA91bGWrww6MaicPkY0uNPCytE-0T2cbe6azXRUX6XzrVzExYGVgvJUKh2xBTykiaTNBmMSQf4GjQgGUBql_UKw15T_zo8W5VCUPisflCRqauDR6gvBu2OZ3H5xKIxHG3LcgQxSyXt9","token_id":"XLlZzF4THh8vB0PpdroW","is_consultant":"1","dial_cd":"+91","country_cd":"IN","is_language":"False","is_catecory":"False","imageUrl":null}],"user_language":[{"language_cd":60,"english_name":"Hindi"}],"user_category":[{"subcategory_cd":8,"category_cd":1,"subcategory_name_english":"Gynaecologists","category_name_english":"Doctor"},{"subcategory_cd":9,"category_cd":1,"subcategory_name_english":"Physian","category_name_english":"Doctor"}]}
                    try {
                        JSONArray jsonArray = js.getJSONArray("User_Detail");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        tvUserName.setText(jsonObject.getString("user_name"));

                        txt_email.setText(jsonObject.getString("email_address"));
                        txt_country.setText(jsonObject.getString("country_name"));
                        txt_mobile.setText(jsonObject.getString("dial_cd") + jsonObject.getString("mobile_no"));

                        JSONArray arrayLang = js.getJSONArray("user_language");
                        StringBuffer lang_name = new StringBuffer("");
                        for (int i = 0; i < arrayLang.length(); i++) {
                            JSONObject object = arrayLang.getJSONObject(i);
                            lang_name.append(object.getString("native_name") + ",");
                        }

                        if (!lang_name.toString().equals("")) {

                            String selected_name = "" + lang_name;

                            language_name = selected_name.substring(0, selected_name.length() - 1);
                        }



                        txt_languages.setText("" + language_name);

                        JSONArray jsonArraySubCatg = js.getJSONArray("user_category");
                        StringBuffer sub_cat_name = new StringBuffer("");
                        for (int i = 0; i < jsonArraySubCatg.length(); i++) {
                            JSONObject object = jsonArraySubCatg.getJSONObject(i);
                            sub_cat_name.append(object.getString("subcategory_name_english") + ",");
                            if(object.getString("category_name_english")!=null&&!object.getString("category_name_english").isEmpty())
                            {
                                txt_catg.setText(object.getString("category_name_english"));
                                tvUserCategory.setText(object.getString("category_name_english"));
                                tvUserCategory.setPadding(10, 10, 10, 10);
                            }
                            else
                            {
                                tvUserCategory.setVisibility(View.GONE);
                                txt_catg.setText("");
                            }

                        }

                        if (!sub_cat_name.toString().equals("")) {

                            String selected_name = "" + sub_cat_name;

                            subCategoryNames = selected_name.substring(0, selected_name.length() - 1);
                            txt_sub_catg.setText(subCategoryNames);
                        }
                        else
                        {
                            txt_sub_catg.setText("");
                        }




                        if (jsonObject.getString("imageUrl") != null) {
                            imageUrl = jsonObject.getString("imageUrl");

                            if (!imageUrl.equals("")) {
                                AQuery aQuery = new AQuery(context);
                                aQuery.id(imgProfile).image(imageUrl, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
                            } else {
                                imgProfile.setImageResource(R.mipmap.profile_large);
                            }
                        }



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

//            request.commonAPI(url, param, new RequestCallback() {
//
//                @Override
//                public void onSuccess(JSONObject js, String success) {
//                    dismiss_loading();
//                    Log.d(TAG, "JSON: " + js.toString());
//
//                }
//
//                @Override
//                public void onFailed(JSONObject js, String failed, int status) {
//                    dismiss_loading();
//                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
//                    {
//                        unAuthorized();
//                        showToast(failed);
//                    } else {
//                        MyDialog.iPhone(failed, context);
//                    }
//                }
//
//                @Override
//                public void onNull(JSONObject js, String nullp) {
//                    MyDialog.iPhone(nullp, context);
//                    dismiss_loading();
//                }
//
//                @Override
//                public void onException(JSONObject js, String exception) {
//                    MyDialog.iPhone(exception, context);
//                    dismiss_loading();
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamSubCateg() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);

        return params;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.imgEdit:
//                if (is_consultant.equals("0") && is_company.equals("1")) {
//                    startActivity(new Intent(context, ProfileEditTabActivity.class));
//                }
//                else  if (is_consultant.equals("1") && is_company.equals("0"))
//                {
//                    startActivity(new Intent(context, ConsultantProfileEditActivity.class));
//                }
//                else  if (is_consultant.equals("0") && is_company.equals("0"))
//                {
//                    startActivity(new Intent(context, UserProfileEditActivity.class));
//                }
//
//                break;
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (baseActivity.isConnectingToInternet(context)) {
            callGetProfile(getParamSubCateg(), Urls.GET_PROFILE);
        }
    }
}
