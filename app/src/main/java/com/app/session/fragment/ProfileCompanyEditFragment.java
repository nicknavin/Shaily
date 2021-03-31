package com.app.session.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.app.session.R;

import com.app.session.activity.SelectCountryActivity;
import com.app.session.activity.SelectLangaugeActivity;
import com.app.session.activity.SelectSubCategory;
import com.app.session.adapter.BriefCvEditAdapter;
import com.app.session.adapter.LanguageEditAdapter;
import com.app.session.adapter.OfficeEditAdapters;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.AddressLocation;
import com.app.session.data.model.Brief_CV;
import com.app.session.utility.Constant;
import com.app.session.utility.ConvetBitmap;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.session.utility.PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS;

public class ProfileCompanyEditFragment extends BaseFragment implements View.OnClickListener {

    Context context;

    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;

    private AlertDialog alertDialog;
    View view;
    CircleImageView imgProfile;
    ImageView imgProfile_company;
    ImageView imgEdit, imgAdd_Address;
    CustomEditText edt_fullname, edt_Bankname, edt_BranchName, edt_AccountNo, edt_BankIbn, edt_swifcode, edt_branch_address;
    CustomTextView edt_selectCat, edt_selectSubCat, edt_selectCountry, edt_selectLang;
    CustomTextView txtbank, txtUpdate, txt_Update_briefcv, txtUpdate_lang, txtUpdate_subCatg, txt_bank_Update;
    CustomTextView myView, edt_phone_number, edt_email, txt_add_specialization;
    CardView card_address;
    RecyclerView recyclerView, recyclerView_address, recyclerView_lang;
    LinearLayoutManager linearLayoutManager, linearLayoutManager_address, linearLayoutManager_lang;

    BriefCvEditAdapter briefCvAdapter;
    OfficeEditAdapters officeAdapters;
    LanguageEditAdapter languageEditAdapter;
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    private ArrayList<Brief_CV> brief_cvList_add = new ArrayList<>();

    String categoryId = "", subCategoryNames = "", subCategoryId = "", language_name = "", country_cd = "", language_cd = "";
    String patientProfile = "", imageUrl = "", PicturePath = "", fullname = "";


    ArrayList<AddressLocation> addressLocationsList = new ArrayList<>();
    ArrayList<Brief_CV> languageArrayList = new ArrayList<>();
    AddressLocation addressLocation = new AddressLocation();
    private Dialog dialogSelect;

    public int isForCamera = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_profile_company_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initView(view);
    }

    private void initView(View view) {

        edt_fullname = (CustomEditText) view.findViewById(R.id.edt_fullname);
        edt_selectLang = (CustomTextView) view.findViewById(R.id.edt_selectLang);
        edt_selectLang.setOnClickListener(this);
        edt_selectCat = (CustomTextView) view.findViewById(R.id.edt_selectCat);
        edt_selectCat.setOnClickListener(this);
        edt_selectCountry = (CustomTextView) view.findViewById(R.id.edt_selectCountry);
        edt_selectCountry.setOnClickListener(this);
        edt_selectSubCat = (CustomTextView) view.findViewById(R.id.edt_selectSubCat);
        edt_selectSubCat.setOnClickListener(this);
        edt_email = (CustomTextView) view.findViewById(R.id.edt_email);
        edt_phone_number = (CustomTextView) view.findViewById(R.id.edt_phone_number);
        edt_Bankname = (CustomEditText) view.findViewById(R.id.edt_Bankname);
        edt_BranchName = (CustomEditText) view.findViewById(R.id.edt_BranchName);
        edt_AccountNo = (CustomEditText) view.findViewById(R.id.edt_AccountNo);
        edt_BankIbn = (CustomEditText) view.findViewById(R.id.edt_BankIbn);
        edt_swifcode = (CustomEditText) view.findViewById(R.id.edt_swifcode);
        edt_branch_address = (CustomEditText) view.findViewById(R.id.edt_branch_address);
        txt_bank_Update = (CustomTextView) view.findViewById(R.id.txt_bank_Update);
        txt_bank_Update.setOnClickListener(this);
//        txtUpdate = (CustomTextView) view.findViewById(R.id.txtUpdate);
//        txtUpdate.setOnClickListener(this);

        txt_Update_briefcv = (CustomTextView) view.findViewById(R.id.txt_Update_briefcv);
        txt_Update_briefcv.setOnClickListener(this);

        txtUpdate_lang = (CustomTextView) view.findViewById(R.id.txtUpdate_lang);
        txtUpdate_lang.setOnClickListener(this);

        txt_add_specialization = (CustomTextView) view.findViewById(R.id.txt_add_specialization);
        txt_add_specialization.setOnClickListener(this);

        txtUpdate_subCatg = (CustomTextView) view.findViewById(R.id.txtUpdate_subCatg);
        txtUpdate_subCatg.setOnClickListener(this);


        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);
        imgProfile_company=(ImageView)view.findViewById(R.id.imgProfile_company);
        imgProfile_company.setOnClickListener(this);
        imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(this);
        imgAdd_Address = (ImageView) view.findViewById(R.id.imgAdd_Address);
        imgAdd_Address.setOnClickListener(this);

        card_address = (CardView) view.findViewById(R.id.card_address);

        if (is_consultant.equals("0") && is_company.equals("0")) {

            txtbank.setVisibility(View.GONE);
            card_address.setVisibility(View.GONE);
        } else if (is_consultant.equals("0") && is_company.equals("1")) {

            card_address.setVisibility(View.VISIBLE);

        } else if (is_consultant.equals("1") && is_company.equals("0")) {

            card_address.setVisibility(View.GONE);
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        briefCvAdapter = new BriefCvEditAdapter(context, brief_cvList, new ApiCallback() {
            @Override
            public void result(String x) {

            }
        });
        recyclerView.setAdapter(briefCvAdapter);

        recyclerView_address = (RecyclerView) view.findViewById(R.id.recyclerView_address);
        recyclerView_address.setNestedScrollingEnabled(false);
        linearLayoutManager_address = new LinearLayoutManager(context);
        recyclerView_address.setLayoutManager(linearLayoutManager_address);
        recyclerView_address.setNestedScrollingEnabled(false);
        officeAdapters = new OfficeEditAdapters(context, addressLocationsList, new ApiCallback() {
            @Override
            public void result(String x) {
                int position = Integer.parseInt(x);
//                addressLocation = addressLocationsList.get(position);
//                Intent intent = new Intent(context, AddEditAddressActivity.class);
//                intent.putExtra("DATA", addressLocation);
//                context.startActivity(intent);
            }
        });
        recyclerView_address.setAdapter(officeAdapters);

        recyclerView_lang = (RecyclerView) view.findViewById(R.id.recyclerView_lang);
        recyclerView_lang.setNestedScrollingEnabled(false);
        linearLayoutManager_lang = new LinearLayoutManager(context);
        recyclerView_lang.setLayoutManager(linearLayoutManager_lang);
//        languageEditAdapter = new LanguageEditAdapter(context, languageArrayList);
        recyclerView_lang.setAdapter(languageEditAdapter);
        if (baseActivity.isConnectingToInternet(context)) {

            callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
            if (is_consultant.equals("0") && is_company.equals("1")) {
                callGetAddressProfile(getParamAddress(), Urls.GET_USER_LOCATIONS);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.imgEdit:
                dialog();
                break;
            case R.id.imgProfile:
                dialog();
                break;
            case R.id.imgProfile_company:
                dialog();
                break;
            case R.id.edt_selectLang:

                Intent intent = new Intent(context, SelectLangaugeActivity.class);
                intent.putExtra("ID", language_cd);
                startActivityForResult(intent, Constant.PICK_LANGUAGE);

                break;

            case R.id.edt_selectCat:
                edt_selectSubCat.setText(context.getResources().getString(R.string.select_sub_category));
                subCategoryId = "";
                Intent intent2 = new Intent(context, SelectCountryActivity.class);
                intent2.putExtra("TYPE", "Category");
                startActivityForResult(intent2, Constant.PICK_CATEGORY);
                break;
            case R.id.edt_selectSubCat:
                if (categoryId.length() != 0) {

                    Intent intent1 = new Intent(context, SelectSubCategory.class);
                    intent1.putExtra("ID", categoryId);
                    startActivityForResult(intent1, Constant.PICK_SUB_CATEGORY);

                } else {
                    showToast("Please select first Category");
                }
                break;
            case R.id.imgTake_photo:
                setCameraPermission();
                isForCamera = 1;
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.imgTake_gallery:
                setGalleryPermission();
                isForCamera = 2;
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.imgCancel:
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

//            case R.id.txtUpdate:
//
//                if (validation()) {
//
//                    if (baseActivity.isConnectingToInternet(context)) {
//                        if (is_consultant.equals("0") && is_company.equals("0")) {
//
//                        } else {
//                            callUpdateProfile(getParamUpdateProfile(), Urls.UPDATE_CONSULTANT);
//                        }
//                    }
//                }
//                break;
            case R.id.txt_bank_Update:

                if (baseActivity.isConnectingToInternet(context)) {
                    if (validBank()) {
                        if (is_consultant.equals("0") && is_company.equals("0")) {

                        } else {
                            callUpdateBankDetail(getParamBankDetail(), Urls.UPDATE_BANK_DETAIL);
                        }
                    }
                }
                break;

            case R.id.txt_Update_briefcv:
                if (baseActivity.isConnectingToInternet(context)) {
                    callUpdateBriefCV(getParamUpdate(), Urls.UPDATE_BRIEF);
                } else {
                    showInternetPop(context);
                }

                break;


            case R.id.imgAdd_Address:
                openAddOffice(addressLocation);
                break;

            case R.id.txt_add_specialization:
                if (categoryId.length() != 0) {

                    Intent intent1 = new Intent(context, SelectSubCategory.class);
                    intent1.putExtra("ID", categoryId);
                    startActivityForResult(intent1, Constant.PICK_SUB_CATEGORY);

                } else {
                    showToast("Please select first Category");
                }
                break;


            case R.id.txtUpdate_lang:
                if (baseActivity.isConnectingToInternet(context)) {
                    callUpdateLanguage(getParamUpdateLanguage(), Urls.SET_USER_LANGUAGE);
                } else {
                    showInternetPop(context);
                }

                break;
            case R.id.txtUpdate_subCatg:
                if (baseActivity.isConnectingToInternet(context)) {
                    callConsultant_Specialties(getParamUpdateSpecialites(), Urls.SET_CONSULTANT_SPECIALTIES);
                } else {
                    showInternetPop(context);
                }

                break;


            default:
                break;

        }
    }


    private boolean validation() {
        fullname = edt_fullname.getText().toString().trim();
        String namePattrn = "[a-zA-z]+([ '-][a-zA-Z]+)*";

        if (fullname.isEmpty()) {
            edt_fullname.setError(context.getResources().getString(R.string.error_name));
            return false;
        }
        if (!Pattern.matches(namePattrn, fullname) || fullname.length() < 3) {

            edt_fullname.setError(context.getResources().getString(R.string.error_name_invalid));

            return false;
        }
        if (language_cd.isEmpty()) {
            showToast(context.getResources().getString(R.string.lang_selection_valid));
            return false;
        }

        return true;
    }

    /*.............................................callGetProfile....................................................*/
    private void callGetProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();


                    try {
                        JSONArray jsonArray = js.getJSONArray("User_Detail");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        edt_fullname.setText(jsonObject.getString("user_name"));
                        edt_selectCountry.setText(jsonObject.getString("country_name"));
                        edt_phone_number.setText(jsonObject.getString("mobile_no"));
                        edt_email.setText(jsonObject.getString("email_address"));
                        country_cd = jsonObject.getString("country_cd");
                        JSONArray arrayLang = js.getJSONArray("user_language");
                        StringBuffer lang_name = new StringBuffer("");
                        StringBuffer lang_id = new StringBuffer("");
                        languageArrayList = new ArrayList<>();
                        brief_cvList_add = new ArrayList<>();
                        for (int i = 0; i < arrayLang.length(); i++) {
                            JSONObject object = arrayLang.getJSONObject(i);
                            Brief_CV language = new Brief_CV();
                            language.setChecked(false);
                            language.setLanguage_cd(object.getString("language_cd"));
                            language.setEnglish_name(object.getString("english_name"));
                            language.setNative_name(object.getString("native_name"));
                            lang_name.append(object.getString("native_name") + ",");
                            lang_id.append(object.getString("language_cd") + ",");
                            languageArrayList.add(language);
                            brief_cvList_add.add(language);
                        }

//                        languageEditAdapter = new LanguageEditAdapter(context, languageArrayList);
                        recyclerView_lang.setAdapter(languageEditAdapter);


                        if (!lang_id.toString().equals("")) {
                            String selected = "" + lang_id;
                            String selected_name = "" + lang_name;
                            language_cd = selected.substring(0, selected.length() - 1);
                            language_name = selected_name.substring(0, selected_name.length() - 1);
                        }


                        JSONArray arrayBrief = js.getJSONArray("user_brief");
                        brief_cvList = new ArrayList<>();
                        for (int i = 0; i < arrayBrief.length(); i++) {
                            JSONObject object = arrayBrief.getJSONObject(i);
                            Brief_CV brief_cv = new Brief_CV();
                            brief_cv.setBrief_cv(object.getString("breaf_cv"));
                            brief_cv.setLanguage_cd(object.getString("language_cd"));
                            brief_cv.setNative_name(object.getString("native_name"));
                            brief_cvList.add(brief_cv);
                        }
                        setBriefCV(brief_cvList);


                        JSONArray jsonArraySubCatg = js.getJSONArray("user_category");
                        StringBuffer sub_cat_name = new StringBuffer("");
                        StringBuffer sub_cat_id = new StringBuffer("");
                        for (int i = 0; i < jsonArraySubCatg.length(); i++) {
                            JSONObject object = jsonArraySubCatg.getJSONObject(i);
                            sub_cat_name.append(object.getString("subcategory_name_english") + ",");
                            sub_cat_id.append(object.getString("subcategory_cd") + ",");
                            edt_selectCat.setText(object.getString("category_name_english"));
                            categoryId = object.getString("category_cd");
                        }


                        if (!sub_cat_id.toString().equals("")) {
                            String selected = "" + sub_cat_id;
                            String selected_name = "" + sub_cat_name;
                            subCategoryId = selected.substring(0, selected.length() - 1);
                            subCategoryNames = selected_name.substring(0, selected_name.length() - 1);
                        }
                        edt_selectSubCat.setText(subCategoryNames);

                        if (jsonObject.getString("imageUrl") != null) {
                            imageUrl = jsonObject.getString("imageUrl");
                            System.out.println("patientProfile " + imageUrl);
                            if (!imageUrl.equals("")) {
                                AQuery aQuery = null;
                                aQuery = new AQuery(context);
                                aQuery.id(imgProfile).image(imageUrl, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
                                aQuery.id(imgProfile_company).image(imageUrl, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
                            } else {
                                imgProfile.setImageResource(R.mipmap.profile_large);
                            }
                        }


                        JSONArray bank_detail = js.getJSONArray("bank_detail");
                        JSONObject objectbank = bank_detail.getJSONObject(0);
                        edt_Bankname.setText(objectbank.getString("bank_name"));
                        edt_AccountNo.setText(objectbank.getString("account_number"));
                        edt_BankIbn.setText(objectbank.getString("iban_code"));
                        edt_swifcode.setText(objectbank.getString("swift_code"));
                        edt_BranchName.setText(objectbank.getString("bank_address"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
                    {
                        unAuthorized(context,getActivity());

                    } else {
//                        MyDialog.iPhone(failed, context);
                    } }

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

    private Map<String, Object> getParamProfile() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);

        return params;
    }
/*.................................................................................................*/

/*........................................callGetAddressProfile.........................................................*/

    private void callGetAddressProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {
                        JSONArray array = js.getJSONArray("user_locations");
                        Type type = new TypeToken<ArrayList<AddressLocation>>() {
                        }.getType();
                        addressLocationsList = new Gson().fromJson(array.toString(), type);
                        officeAdapters = new OfficeEditAdapters(context, addressLocationsList, new ApiCallback() {
                            @Override
                            public void result(String x) {
                                int position = Integer.parseInt(x);
                                addressLocation = addressLocationsList.get(position);
                                openAddOffice(addressLocation);
                            }
                        });
                        recyclerView_address.setAdapter(officeAdapters);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
                    {
                        unAuthorized(context,getActivity());

                    } else {
//                        MyDialog.iPhone(failed, context);
                    }   }

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

    private Map<String, Object> getParamAddress() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);

        return params;
    }

/*.................................................................................................*/

    private void callUpdateLanguage(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    if (baseActivity.isConnectingToInternet(context)) {
                        callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
                    }
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
                    {
                        unAuthorized(context,getActivity());

                    } else {
                        MyDialog.iPhone(failed, context);
                    }   }

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

    private Map<String, Object> getParamUpdateLanguage() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("language_cd", language_cd);


        return params;
    }
/*.................................................................................................*/



/*.................................................................................................*/

    private void callUpdateProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    if (baseActivity.isConnectingToInternet(context)) {
                        callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
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

    private Map<String, Object> getParamUpdateProfile() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("user_name", edt_fullname.getText().toString());
        params.put("language_cd", language_cd);
        params.put("country_cd", country_cd);
        params.put("sub_category_cd", subCategoryId);
        params.put("myBase64String", patientProfile);

        return params;
    }
/*.................................................................................................*/

    private void callUpdateBankDetail(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    if (baseActivity.isConnectingToInternet(context)) {
                        callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
                    }
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
                    {
                        unAuthorized(context,getActivity());

                    } else {
                        MyDialog.iPhone(failed, context);
                    }      }

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

    private Map<String, Object> getParamBankDetail() {

        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("bank_name", edt_Bankname.getText().toString());
        params.put("account_number", edt_AccountNo.getText().toString());
        params.put("iban_code", edt_BankIbn.getText().toString());
        params.put("swift_code", edt_swifcode.getText().toString());
        params.put("bank_address", edt_branch_address.getText().toString());
        return params;
    }


    private boolean validBank() {
        if (edt_Bankname.getText().toString().isEmpty()) {
            edt_Bankname.setError(context.getResources().getString(R.string.valid_bank_name));
            return false;
        }
        if (edt_BranchName.getText().toString().isEmpty()) {
            edt_BranchName.setError(context.getResources().getString(R.string.valid_bank_branc));
            return false;
        }
//        if (edt_branch_address.getText().toString().isEmpty()) {
//            edt_branch_address.setError(context.getResources().getString(R.string.valid_bank_address));
//            return false;
//        }
        if (edt_AccountNo.getText().toString().isEmpty()) {
            edt_AccountNo.setError(context.getResources().getString(R.string.valid_bank_account));
            return false;
        }

        if (edt_BankIbn.getText().toString().isEmpty()) {
            edt_BankIbn.setError(context.getResources().getString(R.string.valid_bank_ibn));
            return false;
        }
        if (edt_swifcode.getText().toString().isEmpty()) {
            edt_swifcode.setError(context.getResources().getString(R.string.valid_bank_swif));
            return false;
        }


        return true;
    }


/*.................................................................................................*/

    private void callUpdateBriefCV(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    dismiss_loading();
                    if (baseActivity.isConnectingToInternet(context)) {
                        callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
                    {
                        unAuthorized(context,getActivity());

                    } else {
                        MyDialog.iPhone(failed, context);
                    }        }

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

    private Map<String, Object> getParamUpdate() {
        Map<String, Object> params = new HashMap<>();


        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("brief_cv", getBriefCV());


        return params;
    }

    private String getBriefCV() {
        if (brief_cvList.size() > 0) {
            for (int i = 0; i < brief_cvList.size(); i++) {
                Brief_CV brief_cv = brief_cvList.get(i);
            }

        }

        try {

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < brief_cvList.size(); i++) {
                Brief_CV cv = brief_cvList.get(i);
                JSONObject object = new JSONObject();
                object.put("language_cd", cv.getLanguage_cd());
                object.put("brief_cv", cv.getBrief_cv());
                jsonArray.put(object);
            }
            jsonObject.put("brief", jsonArray);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";


    }
/*.................................................................................................*/


    private void callConsultant_Specialties(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(getActivity());
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    dismiss_loading();
                    if (baseActivity.isConnectingToInternet(context)) {
                        callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
                    {
                        unAuthorized(context,getActivity());

                    } else {
                        MyDialog.iPhone(failed, context);
                    }          }

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


    private Map<String, Object> getParamUpdateSpecialites() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("sub_category_cd", subCategoryId);


        return params;
    }


    public void setBriefCV(final ArrayList<Brief_CV> brief_cvList) {
        String lang_id = "", check_lang_id = "";
        if (brief_cvList.size() > 0) {
            for (int i = 0; i < brief_cvList.size(); i++) {
                Brief_CV brief_cv = brief_cvList.get(i);
                lang_id = brief_cv.getLanguage_cd();
                for (int j = 0; j < brief_cvList_add.size(); j++) {
                    Brief_CV cv = brief_cvList_add.get(j);
                    check_lang_id = cv.getLanguage_cd();
                    if (check_lang_id.equals(lang_id)) {
                        brief_cvList_add.remove(cv);
                    }

                }

            }
        }


        for (int j = 0; j < brief_cvList_add.size(); j++) {
            Brief_CV brief_cv = brief_cvList_add.get(j);
            brief_cvList.add(brief_cv);
        }

        briefCvAdapter = new BriefCvEditAdapter(context, brief_cvList, new ApiCallback() {
            @Override
            public void result(String x) {
                int position = Integer.parseInt(x);
                Brief_CV brief_cv = brief_cvList.get(position);
                UpdateBriefCvDialog(brief_cv,position);
            }
        });
        recyclerView.setAdapter(briefCvAdapter);

    }


    private void openAddOffice(AddressLocation data) {

        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION, "Location")) {
                System.out.println("Location FALSE");
                return;
            }

            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION, "Location")) {
                System.out.println("Location FALSE");
                return;
            }
        }
        if (baseActivity.isLocationEnabled(context)) {
//            AddressLocation addressLocation = data;
//            Intent intent = new Intent(context, AddEditAddressActivity.class);
//            intent.putExtra("DATA", addressLocation);
//            startActivity(intent);
//            baseActivity.startTransition();
        } else {
            baseActivity.showSettingsAlert(new ApiCallback() {
                @Override
                public void result(String x) {
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:

                    if(is_company.equals("1")&&is_consultant.equals("0"))
                    {
                        CropImage.activity(mCameraImageUri)
                                .setAspectRatio(274,130)
                                .setFixAspectRatio(true)
                                .start(getActivity(),this);
                    }
                        else {
                        CropImage.activity(mCameraImageUri)
                                .setAspectRatio(1, 1)
                                .setFixAspectRatio(true)
                                .start(getActivity(), this);
                    }
                    break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {
                        mImageCaptureUri = data.getData();
                        if(is_company.equals("1")&&is_consultant.equals("0"))
                        {
                            CropImage.activity(mImageCaptureUri)
                                    .setAspectRatio(274,130)
                                    .setFixAspectRatio(true)
                                    .start(getActivity(),this);
                        }
                        else {
                            CropImage.activity(mImageCaptureUri)
                                    .setAspectRatio(1, 1)
                                    .setFixAspectRatio(true)
                                    .start(getActivity(), this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == -1) {
                        Uri resultUri = result.getUri();
                        try {
                            ByteArray = null;
//                            Bitmap bm = BitmapFactory.decodeFile(PicturePath);
                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            bm = ConvetBitmap.Mytransform(bm);
                            bm = Utility.rotateImage(bm, new File(PicturePath));
                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 50, datasecond);
                            ByteArray = datasecond.toByteArray();
                            patientProfile = base64String(datasecond.toByteArray());

                            if(is_company.equals("1")&&is_consultant.equals("0"))
                            {
                                imgProfile_company.setImageBitmap(bm);
                            }
                            else
                            { imgProfile.setImageBitmap(bm);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;


                default:
                    break;


            }
        }


        if (requestCode == Constant.PICK_LANGUAGE) {

            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    language_name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    language_cd = data.getStringExtra("ID");
                }


                brief_cvList_add = new ArrayList<>();
                languageArrayList = new ArrayList<>();

                if (!language_cd.isEmpty() && !language_name.isEmpty()) {

                    String[] id = language_cd.split(",");
                    String[] name = language_name.split(",");

                    for (int i = 0; i < id.length; i++) {
                        Brief_CV brief_cv = new Brief_CV();
                        brief_cv.setBrief_cv("");
                        brief_cv.setNative_name(name[i]);
                        brief_cv.setLanguage_cd(id[i]);
                        brief_cvList_add.add(brief_cv);
                        languageArrayList.add(brief_cv);
                    }

                    setBriefCV(brief_cvList);


//                    languageEditAdapter = new LanguageEditAdapter(context, languageArrayList);
                    recyclerView_lang.setAdapter(languageEditAdapter);

                }
            }
        }
        if (requestCode == Constant.PICK_SUB_CATEGORY) {
            String name = "", id = "";
            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    subCategoryId = data.getStringExtra("ID");
                }

                edt_selectSubCat.setText(name);
            }
        }

        if (requestCode == Constant.PICK_CATEGORY) {
            String name = "", id = "";
            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    categoryId = data.getStringExtra("ID");
                }


                edt_selectCat.setText(name);
            }
        }
        if (requestCode == Constant.LOCATION) {
            if (baseActivity.isLocationEnabled(context)) {
//                Toast.makeText(getContext(), "Location on", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, AddEditAddressActivity.class);
//                intent.putExtra("DATA", addressLocation);
//                startActivity(intent);
//                baseActivity.startTransition();
            }
        }

    }


    public void setCameraPermission() {
        isForCamera = 1;
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!isPermissionGranted(context, Manifest.permission.CAMERA, "Camera")) {
                return;
            }

            if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
                if (!isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                    return;
                }
            }
        }
        TakePic();
    }

    public void setGalleryPermission() {
        isForCamera = 2;
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
        }


        Gallery();
    }

    public void dialogold() {
        dialogSelect = new Dialog(context, R.style.MaterialDialogSheet);
        dialogSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelect.setContentView(R.layout.dialog_g);
        dialogSelect.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogSelect.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogSelect.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        int i = Utility.getScreenWidth(getActivity());
        ((CustomTextView) dialogSelect.findViewById(R.id.txtTake_photo)).setTranslationX(-i);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtTake_gallery)).setTranslationX(-i);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtCancel)).setTranslationX(-i);
        ((View) dialogSelect.findViewById(R.id.view1)).setTranslationX(-i);
        ((View) dialogSelect.findViewById(R.id.view2)).setTranslationX(-i);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtTake_photo)).animate().translationX(0).setDuration(500).setStartDelay(400);
        ((View) dialogSelect.findViewById(R.id.view1)).animate().translationX(0).setDuration(500).setStartDelay(500);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtTake_gallery)).animate().translationX(0).setDuration(500).setStartDelay(600);
        ((View) dialogSelect.findViewById(R.id.view2)).animate().translationX(0).setDuration(500).setStartDelay(700);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtCancel)).animate().translationX(0).setDuration(500).setStartDelay(800);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtTake_photo)).setOnClickListener(this);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtTake_gallery)).setOnClickListener(this);
        ((CustomTextView) dialogSelect.findViewById(R.id.txtCancel)).setOnClickListener(this);
        dialogSelect.show();
    }
    public void dialog() {
        dialogSelect = new Dialog(context, R.style.MaterialDialogSheet);
        dialogSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelect.setContentView(R.layout.dialog_image);
        dialogSelect.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogSelect.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogSelect.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        myView = (CustomTextView)view.findViewById(R.id.txtNext);
        int i = myView.getWidth();
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgCancel)).setTranslationX(-i);
//        ((View) dialogSelect.findViewById(R.id.view1)).setTranslationX(-i);
//        ((View) dialogSelect.findViewById(R.id.view2)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).animate().translationX(0).setDuration(500).setStartDelay(400);
//        ((View) dialogSelect.findViewById(R.id.view1)).animate().translationX(0).setDuration(500).setStartDelay(500);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).animate().translationX(0).setDuration(500).setStartDelay(600);
//        ((View) dialogSelect.findViewById(R.id.view2)).animate().translationX(0).setDuration(500).setStartDelay(700);
        ((ImageView) dialogSelect.findViewById(R.id.imgCancel)).animate().translationX(0).setDuration(500).setStartDelay(800);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgCancel)).setOnClickListener(this);
        dialogSelect.show();
    }
    public void TakePic() {

        try {
            isForCamera = 1;
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory() + "/consultlot.png");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mCameraImageUri = Uri.fromFile(f);
            } else {
                mCameraImageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", f);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, Constant.REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //REQUEST_CODE_ALBUM
    public void Gallery() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.REQUEST_CODE_ALBUM);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constant.REQUEST_CODE_ALBUM);
        }
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + baseActivity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    public void checkIfPermissionsGranted() {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(getString(R.string.permission));
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                goToSettings();
            }
        });

        alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
                    if (isForCamera == 1)
                        TakePic();
                    else if (isForCamera == 2)
                        Gallery();
                    else if (isForCamera == 3) {

                    }
                } else {
                    checkIfPermissionsGranted();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);
        System.out.println("imgString " + imgString);
        return imgString;
    }

    public boolean isPermissionGranted(final Context context, String permission, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, permission)) {
                permissionsNeeded.add(text);
            }

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++) {
                        message = message + ", " + permissionsNeeded.get(i);
                    }
                    showMessageOKCancel(context, message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return false;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return false;
            }

        }
        return true;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (getActivity() != null && ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!(shouldShowRequestPermissionRationale(permission)))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    CustomEditText edt_brief;
    public void UpdateBriefCvDialog(final Brief_CV brief_cv, final int position) {
        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.language_update);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            edt_brief =(CustomEditText) dd.findViewById(R.id.edt_cv);
            ((CustomTextView)dd.findViewById(R.id.txtlang)).setText(context.getResources().getString(R.string.brief_cv_txt)+brief_cv.getNative_name());
            edt_brief.setText(brief_cv.getBrief_cv());
            ((CustomTextView) dd.findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("000000000");
                    brief_cv.setBrief_cv(edt_brief.getText().toString());
                    brief_cvList.set(position,brief_cv);
                    if (baseActivity.isConnectingToInternet(context)) {
                        callUpdateBriefCV(getParamUpdate(), Urls.UPDATE_BRIEF);
                    } else {
                        showInternetPop(context);
                    }
                    dd.dismiss();
                }
            });

            ((CustomTextView) dd.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("0001111");

                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }
}
