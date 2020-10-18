package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.adapter.LanguageEditAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.AddressLocation;
import com.app.session.model.Brief_CV;
import com.app.session.utility.Constant;
import com.app.session.utility.ConvetBitmap;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileEditUserActivity extends BaseActivity implements View.OnClickListener {

    Context context;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    CircleImageView imgProfile;
    ImageView imgEdit;
    CustomEditText edt_fullname, edt_pref_lang;
    CustomTextView edt_selectCountry, edt_selectLang, txt_img_updated;
    CustomTextView txtUpdate_profile, txtUpdate_lang;
    CustomTextView myView, edt_phone_number, edt_email;
    RecyclerView recyclerView, recyclerView_lang;
    LinearLayoutManager linearLayoutManager, linearLayoutManager_lang;
    LanguageEditAdapter languageEditAdapter;
    String language_name = "", country_cd = "", language_cd = "";
    String patientProfile = "", imageUrl = "", PicturePath = "", fullname = "";
    ArrayList<AddressLocation> addressLocationsList = new ArrayList<>();
    ArrayList<Brief_CV> languageArrayList = new ArrayList<>();
    AddressLocation addressLocation = new AddressLocation();
    private Dialog dialogSelect;

    public boolean isForCamera = false;
    LinearLayout layProfileUpdate, layBankdetail, layAdd_lang, layProfession;
    CheckBox checkEdit_Profile, checkEdit_lang, checkEdit_preflang;
    String prefered_language_cd = "", prefered_language_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
        context = this;
        initView();
    }

    private void initView()
    {

        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.edit_profile));
        ((CustomTextView) findViewById(R.id.header)).setTextColor(context.getResources().getColor(R.color.black));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        edt_pref_lang = (CustomEditText) findViewById(R.id.edt_pref_lang);
        edt_fullname = (CustomEditText) findViewById(R.id.edt_fullname);
        edt_selectLang = (CustomTextView) findViewById(R.id.edt_selectLang);
        edt_selectLang.setOnClickListener(this);
        txt_img_updated = (CustomTextView) findViewById(R.id.txt_img_updated);
        txt_img_updated.setOnClickListener(this);

        edt_selectCountry = (CustomTextView) findViewById(R.id.edt_selectCountry);
        edt_selectCountry.setOnClickListener(this);

        edt_email = (CustomTextView) findViewById(R.id.edt_email);
        edt_email.setOnClickListener(this);
        edt_phone_number = (CustomTextView) findViewById(R.id.edt_phone_number);
        edt_phone_number.setOnClickListener(this);

        txtUpdate_profile = (CustomTextView) findViewById(R.id.txtUpdate_profile);
        txtUpdate_profile.setOnClickListener(this);


        txtUpdate_lang = (CustomTextView) findViewById(R.id.txtUpdate_lang);
        txtUpdate_lang.setOnClickListener(this);

        layAdd_lang = (LinearLayout) findViewById(R.id.layAdd_lang);

        checkEdit_Profile = (CheckBox) findViewById(R.id.checkEdit_Profile);
        checkEdit_Profile.setOnClickListener(this);

        checkEdit_lang = (CheckBox) findViewById(R.id.checkEdit_lang);
        checkEdit_lang.setOnClickListener(this);

        checkEdit_preflang = (CheckBox) findViewById(R.id.checkEdit_preflang);
        checkEdit_preflang.setOnClickListener(this);

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(this);

        recyclerView_lang = (RecyclerView) findViewById(R.id.recyclerView_lang);
        linearLayoutManager_lang = new LinearLayoutManager(context);
        recyclerView_lang.setLayoutManager(linearLayoutManager_lang);
        languageEditAdapter = new LanguageEditAdapter(context, languageArrayList, new ApiCallback() {
            @Override
            public void result(String x) {
            }
        });
        recyclerView_lang.setAdapter(languageEditAdapter);
        if (isConnectingToInternet(context)) {

            callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.edt_phone_number:
//                dialogUpdateMobile();
                break;
            case R.id.edt_email:
//                dialogUpdateEmail();
                break;
            case R.id.checkEdit_Profile:
//                updateProfile();
                intent = new Intent(context, ProfileDetailEditActivity.class);
                startActivityForResult(intent, Constant.PAGE_REFRESH);
                break;
            case R.id.checkEdit_lang:
//                updatedLanguage();
                intent = new Intent(context, UpdatedLangaugeActivity.class);
                intent.putExtra("ID", language_cd);
                startActivityForResult(intent, Constant.PICK_LANGUAGE);
                break;
            case R.id.checkEdit_preflang:
//                updatePrefLang();
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgEdit:
                dialog();
                break;
            case R.id.txt_img_updated:
                dialog();
                break;
            case R.id.imgProfile:
                dialog();
                break;
            case R.id.edt_selectLang:

                 intent = new Intent(context, UpdatedLangaugeActivity.class);
                intent.putExtra("ID", language_cd);
                startActivityForResult(intent, Constant.PICK_LANGUAGE);

                break;

            case R.id.imgTake_photo:
                setCameraPermission();
                isForCamera = true;
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.imgTake_gallery:
                setGalleryPermission();
                isForCamera = false;
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.imgCancel:
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;
            case R.id.imgRemove:
                if (isConnectingToInternet(context)) {
                    callRemoveProfileImage(getRemoveUpdateProfileImage(), Urls.REMOVE_IMAGE);

                } else {
                    showInternetPop(context);
                }
                break;


            case R.id.txtUpdate_profile:
                if (isConnectingToInternet(context)) {
                    if (validation()) {
                        callUpdateProfile(getParamUpdateProfile(), Urls.UPDATE_CONSULTANT);
                    }
                }
                break;
            case R.id.txtUpdate_lang:
                if (isConnectingToInternet(context)) {
                    callUpdateLanguage(getParamUpdateLanguage(), Urls.SET_USER_LANGUAGE);
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

    private void callRemoveProfileImage(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    imgProfile.setImageResource(R.mipmap.profile_large);
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {

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
//
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

    private Map<String, Object> getRemoveUpdateProfileImage() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);


        return params;
    }

/*.................................................................................................*/

    private void callUpdateLanguage(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    if (isConnectingToInternet(context)) {

                        callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);

                    }
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

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

    private Map<String, Object> getParamUpdateLanguage() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("language_cd", language_cd);


        return params;
    }
/*.................................................................................................*/


    /*.............................................callGetProfile....................................................*/
    private void callGetProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
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
//                        edt_pref_lang.setText(jsonObject.getString("prefered_language_native_name"));
//                        prefered_language_cd = jsonObject.getString("prefered_language_cd");
                        country_cd = jsonObject.getString("country_cd");
                        JSONArray arrayLang = js.getJSONArray("user_language");
                        StringBuffer lang_name = new StringBuffer("");
                        StringBuffer lang_id = new StringBuffer("");
                        languageArrayList = new ArrayList<>();

                        for (int i = 0; i < arrayLang.length(); i++) {
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
                            lang_name.append(object.getString("native_name") + ",");
                            lang_id.append(object.getString("language_cd") + ",");
                            languageArrayList.add(language);

                        }

                        languageEditAdapter = new LanguageEditAdapter(context, languageArrayList, new ApiCallback()
                        {
                            @Override
                            public void result(String x) {
                                if (isConnectingToInternet(context))
                                {
                                    callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
                                }
                            }
                        });
                        recyclerView_lang.setAdapter(languageEditAdapter);


                        if (!lang_id.toString().equals("")) {
                            String selected = "" + lang_id;
                            String selected_name = "" + lang_name;
                            language_cd = selected.substring(0, selected.length() - 1);
                            language_name = selected_name.substring(0, selected_name.length() - 1);
                        }

                        if (jsonObject.getString("imageUrl") != null) {
                            imageUrl = jsonObject.getString("imageUrl");
                            System.out.println("patientProfile " + imageUrl);
                            if (!imageUrl.equals("")) {
                                AQuery aQuery = null;
                                aQuery = new AQuery(context);
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
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

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

    private Map<String, Object> getParamProfile() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);

        return params;
    }


    private void callUpdateProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    txtUpdate_profile.setVisibility(View.GONE);
                    checkEdit_Profile.setVisibility(View.VISIBLE);
                    checkEdit_Profile.setChecked(false);
//                    if (isConnectingToInternet(context)) {
//                        callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
//                    }
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

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

    private Map<String, Object> getParamUpdateProfile() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("user_name", edt_fullname.getText().toString());
        params.put("language_cd", language_cd);
        params.put("country_cd", country_cd);
        params.put("sub_category_cd", "");

        return params;
    }
/*.............................................callGetProfile....................................................*/


    /*................................................................*/
    private void callUpdatePrefLang(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {

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

    private Map<String, Object> getParamPrefLang() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("prefered_language_cd", prefered_language_cd);
        return params;
    }
    /*................................................................*/


    /*---------------------------------------------------------------------*/
    private void callUpdateProfileImage(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {

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
//
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


    private Map<String, Object> getParamUpdateProfileImage() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("myBase64String", patientProfile);

        return params;
    }

    /*---------------------------------------------------------------------*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:

                    CropImage.activity(mCameraImageUri)
                            .setAspectRatio(1, 1)
                            .setFixAspectRatio(true)
                            .start(this);

                    break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {
                        mImageCaptureUri = data.getData();
                        CropImage.activity(mImageCaptureUri)
                                .setAspectRatio(1, 1)
                                .setFixAspectRatio(true)
                                .start(this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
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
                            imgProfile.setImageBitmap(bm);
                            if (isConnectingToInternet(context)) {
                                callUpdateProfileImage(getParamUpdateProfileImage(), Urls.UPDATE_IMAGE);
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
        if (requestCode == Constant.PAGE_REFRESH) {
            if (isConnectingToInternet(context)) {

                callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);


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
                if (isConnectingToInternet(context)) {
                    callUpdateLanguage(getParamUpdateLanguage(), Urls.SET_USER_LANGUAGE);
                } else {
                    showInternetPop(context);
                }

//                languageArrayList = new ArrayList<>();
//
//                if (!language_cd.isEmpty() && !language_name.isEmpty()) {
//
//                    String[] id = language_cd.split(",");
//                    String[] name = language_name.split(",");
//
//                    for (int i = 0; i < id.length; i++) {
//                        Brief_CV brief_cv = new Brief_CV();
//                        brief_cv.setBrief_cv("");
//                        brief_cv.setNative_name(name[i]);
//                        brief_cv.setLanguage_cd(id[i]);
//
//                        languageArrayList.add(brief_cv);
//                    }
//
//
//                    languageEditAdapter = new LanguageEditAdapter(context, languageArrayList);
//                    recyclerView_lang.setAdapter(languageEditAdapter);
//
//                }
            }
        }


        if (requestCode == Constant.LOCATION) {
            if (isLocationEnabled(context)) {
                Toast.makeText(context, "Location on", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(context, AddEditAddressActivity.class);
//                intent.putExtra("DATA", addressLocation);
//                startActivity(intent);
//                startTransition();
            }
        }
        if (requestCode == Constant.PICK_PREF_LANGUAGE) {

            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    prefered_language_name = data.getStringExtra("NAME");
                    edt_pref_lang.setText(prefered_language_name);
                }
                if (data.getStringExtra("ID") != null) {
                    prefered_language_cd = data.getStringExtra("ID");
                }
                callUpdateProfileImage(getParamPrefLang(), Urls.CHANG_PREFRED_LANGUAGE);
            }
        }

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
                    if (isForCamera)
                        TakePic();
                    else
                        Gallery();
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


    public void setCameraPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.CAMERA, "Camera")) {
                return;
            }

            if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
                if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                    return;
                }
            }
        }
        TakePic();
    }

    public void setGalleryPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage")) {
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

        int i = Utility.getScreenWidth(this);
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
        myView = (CustomTextView) findViewById(R.id.txtUpdate_profile);//txtUpdate_profile
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
        ((ImageView) dialogSelect.findViewById(R.id.imgRemove)).setOnClickListener(this);
        dialogSelect.show();
    }

    public void TakePic() {

        try {
            isForCamera = true;
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
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    public void checkIfPermissionsGranted()     {
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

    private void updateProfile() {
        if (checkEdit_Profile.isChecked()) {

            checkEdit_Profile.setVisibility(View.INVISIBLE);
            txtUpdate_profile.setVisibility(View.VISIBLE);
            editable(edt_fullname, true);

        } else {
            checkEdit_Profile.setVisibility(View.VISIBLE);
            txtUpdate_profile.setVisibility(View.GONE);
            editable(edt_fullname, false);
        }

    }

    private void updatedLanguage() {
        if (checkEdit_lang.isChecked()) {
            layAdd_lang.setVisibility(View.VISIBLE);
            checkEdit_lang.setVisibility(View.INVISIBLE);
        } else {
            layAdd_lang.setVisibility(View.GONE);
            checkEdit_lang.setVisibility(View.VISIBLE);
        }
    }

    private void updatePrefLang() {
//        if (checkEdit_preflang.isChecked()) {
//
//            checkEdit_preflang.setVisibility(View.INVISIBLE);
//            Intent intent = new Intent(context, PreferenceLanguageActivity.class);
//            intent.putExtra("TYPE", "edit");
//            startActivityForResult(intent, Constant.PICK_PREF_LANGUAGE);
//            txtUpdate_pref_lang.setVisibility(View.VISIBLE);
//
//
//
//        } else {
//            checkEdit_preflang.setVisibility(View.VISIBLE);
//            txtUpdate_pref_lang.setVisibility(View.GONE);
//
//        }
        Intent intent = new Intent(context, PreferenceLanguageActivity.class);
        intent.putExtra("TYPE", "edit");
        startActivityForResult(intent, Constant.PICK_PREF_LANGUAGE);
    }

    public void editable(CustomEditText edt, boolean flag) {
        edt.setCursorVisible(flag);
        edt.setClickable(flag);
        edt.setFocusable(flag);
        edt.setFocusableInTouchMode(flag);
    }


    /*..............................................................................*/
    private void callUpdatedMobileEmail(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    layOtp.setVisibility(View.VISIBLE);
                    layMobile.setVisibility(View.GONE);


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

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
//
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

    private Map<String, Object> getParamUpdateMobile(String mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_mobileno", mobile);

        return params;
    }

    private Map<String, Object> getParamUpdateEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_emailid", email);

        return params;
    }
    /*..............................................................................*/

    /*..............................................................................*/
    private void callSendOTP(Map<String, Object> param, String url, final Dialog dd) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    dd.dismiss();
                    callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {

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
//
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

    private Map<String, Object> getParamSendOTP(String otp, String mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_mobileno", mobile);
        params.put("otp", otp);

        return params;
    }

    private Map<String, Object> getParamEmailSendOTP(String otp, String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("new_emailid", email);
        params.put("otp", otp);

        return params;
    }
    /*..............................................................................*/


    CustomEditText edt_cntryCode_new, edt_MobileNo_new, edt_otp;
    CustomTextView btn_cancelotp, btn_Send;
    LinearLayout layOtp, layMobile;
    CustomEditText edt_email_new, edt_email_confirm;
    String otp = "", newMobile = "", newEmail = "", confm_email = "";

    public void dialogUpdateMobile() {


        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.activity_chang_mobile_number);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);

            edt_cntryCode_new = (CustomEditText) dd.findViewById(R.id.edt_cntryCode_new);
            edt_MobileNo_new = (CustomEditText) dd.findViewById(R.id.edt_MobileNo_new);
            edt_otp = (CustomEditText) dd.findViewById(R.id.edt_otp);
            btn_cancelotp = (CustomTextView) dd.findViewById(R.id.btn_cancelotp);
            btn_Send = (CustomTextView) dd.findViewById(R.id.btn_Send);
            layMobile = (LinearLayout) dd.findViewById(R.id.layMobile);
            layOtp = (LinearLayout) dd.findViewById(R.id.layOtp);
            String dial_cd = DataPrefrence.getPref(context, Constant.DIAL_Code, "");
            edt_cntryCode_new.setText(dial_cd);


            btn_Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    otp = edt_otp.getText().toString();
                    if (isConnectingToInternet(context)) {
                        if (!otp.isEmpty()) {
                            callSendOTP(getParamSendOTP(otp, newMobile), Urls.SET_NEW_MOBILENO, dd);
                        } else {
                            edt_otp.setError(context.getResources().getString(R.string.error_otp));
                        }
                    } else {
                        showInternetPop(context);
                    }
                }
            });
            btn_cancelotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });

            ((CustomTextView) dd.findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    newMobile = edt_MobileNo_new.getText().toString();
                    if (newMobile.isEmpty()) {
                        edt_MobileNo_new.setError(context.getResources().getString(R.string.error_mobile));
                        return;
                    }
                    if (isConnectingToInternet(context)) {
                        callUpdatedMobileEmail(getParamUpdateMobile(newMobile), Urls.SENT_MAIL_CHANGE_NO);
                    } else {
                        showInternetPop(context);
                    }

                }
            });

            ((CustomTextView) dd.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
//                    brief_cv.setBrief_cv(edt_brief.getText().toString());
//                    brief_cvList.add(position,brief_cv);
//                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }

    public void dialogUpdateEmail() {


        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.activity_chang_email);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);

            edt_email_new = (CustomEditText) dd.findViewById(R.id.edt_email_new);
            edt_email_confirm = (CustomEditText) dd.findViewById(R.id.edt_email_confirm);
            edt_otp = (CustomEditText) dd.findViewById(R.id.edt_otp);
            btn_cancelotp = (CustomTextView) dd.findViewById(R.id.btn_cancelotp);
            btn_Send = (CustomTextView) dd.findViewById(R.id.btn_Send);
            layMobile = (LinearLayout) dd.findViewById(R.id.layMobile);
            layOtp = (LinearLayout) dd.findViewById(R.id.layOtp);


            ((CustomTextView) dd.findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    newEmail = edt_email_new.getText().toString();
                    confm_email = edt_email_confirm.getText().toString();

                    if (newEmail.isEmpty()) {
                        edt_email_new.setError(context.getResources().getString(R.string.error_email));
                        return;
                    }
                    if (!Utility.emailValidator(newEmail)) {
                        edt_email_new.setError(context.getResources().getString(R.string.error_email_invalid));
                        return;
                    }
//                    if (confm_email.equals(confm_email)) {
//                        edt_email_confirm.setError(context.getResources().getString(R.string.error_confm_email));
//                        return;
//                    }
                    if (isConnectingToInternet(context)) {
                        callUpdatedMobileEmail(getParamUpdateEmail(newEmail), Urls.SENT_OTP_CHANGE_EMAILID);
                    } else {
                        showInternetPop(context);
                    }


                }
            });

            ((CustomTextView) dd.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();

                }
            });

            btn_Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    otp = edt_otp.getText().toString();
                    if (isConnectingToInternet(context)) {
                        if (!otp.isEmpty()) {
                            callSendOTP(getParamEmailSendOTP(otp, newEmail), Urls.SET_NEW_EMAILID, dd);
                        } else {
                            edt_otp.setError(context.getResources().getString(R.string.error_otp));
                        }
                    } else {
                        showInternetPop(context);
                    }
                }
            });

            btn_cancelotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
