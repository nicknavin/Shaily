package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app.session.adapter.BriefCvEditAdapter;
import com.app.session.adapter.CategoryEditAdapter;
import com.app.session.adapter.LanguageEditAdapter;
import com.app.session.adapter.OfficeEditAdapters;
import com.app.session.adapter.ProfileCategoryAdapter;
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
import com.app.session.model.Category;
import com.app.session.model.SubCategoryModel;
import com.app.session.model.User;
import com.app.session.model.UserBank;
import com.app.session.model.UserId;
import com.app.session.model.UserLangauges;
import com.app.session.model.UserRoot;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.ConvetBitmap;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditConsultantActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout layOtp, layMobile;
    Context context;

    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;



    CircleImageView imgProfile;
    ImageView imgEdit, imgAdd_Address;
    CheckBox checkEdit_Profile, checkEdit_bank, checkEdit_lang, checkEdit_profession;

    CustomTextView edt_selectSubCat, edt_selectCountry;
    CustomTextView txtbank, txtUpdate_profile, txt_Update_briefcv, txtUpdate_lang, txtUpdate_subCatg;
    CustomTextView myView, edt_phone_number, edt_email, txt_add_specialization, txt_img_updated;
    CustomEditText edt_fullname, edt_Bankname, edt_BranchName, edt_AccountNo, edt_BankIbn, edt_swifcode, edt_branch_address, edt_pref_lang;
    CardView card_address;
    CustomTextView txtUpdate_bank;
    RecyclerView recyclerView, recyclerView_address, recyclerView_lang, recyclerView_category;
    LinearLayoutManager linearLayoutManager, linearLayoutManager_address, linearLayoutManager_lang, linearLayoutManager_catg;

    BriefCvEditAdapter briefCvAdapter;
    OfficeEditAdapters officeAdapters;
    LanguageEditAdapter languageEditAdapter;


    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    private ArrayList<Brief_CV> brief_cvList_add = new ArrayList<>();

    String categoryId = "", subCategoryNames = "", subCategoryId = "", language_name = "", country_cd = "", language_cd = "";
    String patientProfile = "", imageUrl = "", PicturePath = "", fullname = "";
    String prefered_language_cd = "", prefered_language_name = "";

    ArrayList<AddressLocation> addressLocationsList = new ArrayList<>();
    ArrayList<Brief_CV> languageArrayList = new ArrayList<>();
    AddressLocation addressLocation = new AddressLocation();
    private Dialog dialogSelect;
    public boolean isForCamera = false;
    LinearLayout layProfileUpdate, layBankdetail, layAdd_lang, layProfession;
    HashMap<String, Category> catgStoreHashMap = new HashMap<>();
    LinearLayout layAddLang;
    ImageView imgEditLanguage, imgEditCategory;
    Bitmap bmProfileImg=null;

    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_profile_edit);
        context = this;
        initView();
    }

    private void initView() {

        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.edit_profile));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        layAddLang = (LinearLayout) findViewById(R.id.layAddLang);
        layAddLang.setOnClickListener(this);
        imgEditLanguage = (ImageView) findViewById(R.id.imgEditLanguage);
        imgEditLanguage.setOnClickListener(this);
        imgEditCategory = (ImageView) findViewById(R.id.imgEditCategory);
        imgEditCategory.setOnClickListener(this);
        edt_pref_lang = (CustomEditText) findViewById(R.id.edt_pref_lang);
        edt_fullname = (CustomEditText) findViewById(R.id.edt_fullname);

        edt_selectCountry = (CustomTextView) findViewById(R.id.edt_selectCountry);
        edt_selectCountry.setOnClickListener(this);
        edt_email = (CustomTextView) findViewById(R.id.edt_email);
        edt_email.setOnClickListener(this);
        edt_phone_number = (CustomTextView) findViewById(R.id.edt_phone_number);
        edt_phone_number.setOnClickListener(this);

        txtUpdate_profile = (CustomTextView) findViewById(R.id.txtUpdate_profile);
        txtUpdate_profile.setOnClickListener(this);
        txt_img_updated = (CustomTextView) findViewById(R.id.txt_img_updated);
        txt_img_updated.setOnClickListener(this);


        txtUpdate_lang = (CustomTextView) findViewById(R.id.txtUpdate_lang);
        txtUpdate_lang.setOnClickListener(this);

        txt_add_specialization = (CustomTextView) findViewById(R.id.txt_add_specialization);
        txt_add_specialization.setOnClickListener(this);

        txtUpdate_subCatg = (CustomTextView) findViewById(R.id.txtUpdate_subCatg);
        txtUpdate_subCatg.setOnClickListener(this);


        edt_Bankname = (CustomEditText) findViewById(R.id.edt_Bankname);
        edt_BranchName = (CustomEditText) findViewById(R.id.edt_BranchName);
        edt_AccountNo = (CustomEditText) findViewById(R.id.edt_AccountNo);
        edt_BankIbn = (CustomEditText) findViewById(R.id.edt_BankIbn);
        edt_swifcode = (CustomEditText) findViewById(R.id.edt_swifcode);
        edt_branch_address = (CustomEditText) findViewById(R.id.edt_branch_address);


        txtUpdate_bank = (CustomTextView) findViewById(R.id.txt_bank_Update);
        txtUpdate_bank.setOnClickListener(this);

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);
        Picasso.with(context).load(profileUrl).placeholder(R.mipmap.profile_large).into(imgProfile);


        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(this);

        checkEdit_Profile = (CheckBox) findViewById(R.id.checkEdit_Profile);
        checkEdit_Profile.setOnClickListener(this);

        checkEdit_bank = (CheckBox) findViewById(R.id.checkEdit_bank);
        checkEdit_bank.setOnClickListener(this);


        imgAdd_Address = (ImageView) findViewById(R.id.imgAdd_Address);
        imgAdd_Address.setOnClickListener(this);

        card_address = (CardView) findViewById(R.id.card_address);


        layProfileUpdate = (LinearLayout) findViewById(R.id.layProfileUpdate);
        layBankdetail = (LinearLayout) findViewById(R.id.layBankdetail);
        layAdd_lang = (LinearLayout) findViewById(R.id.layAdd_lang);
        layProfession = (LinearLayout) findViewById(R.id.layProfession);

        if (is_consultant.equals("0") && is_company.equals("0")) {
            txtbank.setVisibility(View.GONE);
            card_address.setVisibility(View.GONE);
        } else if (is_consultant.equals("0") && is_company.equals("1")) {
            card_address.setVisibility(View.VISIBLE);
        } else if (is_consultant.equals("1") && is_company.equals("0")) {
            card_address.setVisibility(View.GONE);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        briefCvAdapter = new BriefCvEditAdapter(context, brief_cvList, new ApiCallback() {
            @Override
            public void result(String x) {

            }
        });
        recyclerView.setAdapter(briefCvAdapter);

        recyclerView_address = (RecyclerView) findViewById(R.id.recyclerView_address);
        recyclerView_address.setNestedScrollingEnabled(false);
        linearLayoutManager_address = new LinearLayoutManager(context);
        recyclerView_address.setLayoutManager(linearLayoutManager_address);
        officeAdapters = new OfficeEditAdapters(context, addressLocationsList, new ApiCallback() {
            @Override
            public void result(String x) {
//                int position = Integer.parseInt(x);
//                addressLocation = addressLocationsList.get(position);
//                Intent intent = new Intent(context, AddEditAddressActivity.class);
//                intent.putExtra("DATA", addressLocation);
//                context.startActivity(intent);
            }
        });

        recyclerView_address.setAdapter(officeAdapters);
        recyclerView_lang = (RecyclerView) findViewById(R.id.recyclerView_lang);
        linearLayoutManager_lang = new LinearLayoutManager(context);
        recyclerView_lang.setLayoutManager(linearLayoutManager_lang);
        languageEditAdapter = new LanguageEditAdapter(context, languageArrayList, new ApiCallback() {
            @Override
            public void result(String x) {

            }
        });
        recyclerView_lang.setAdapter(languageEditAdapter);

        recyclerView_category = (RecyclerView) findViewById(R.id.recyclerView_category);
        linearLayoutManager_catg = new LinearLayoutManager(context);
        recyclerView_category.setLayoutManager(linearLayoutManager_catg);
        getUserDetail();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imgEditLanguage:
                intent = new Intent(context, EditLanguageActivity.class);
                intent.putExtra("TYPE", "1");
                startActivityForResult(intent, Constant.LANG_REFRESH);
                break;
            case R.id.imgEditCategory:
                intent = new Intent(context, EditLanguageActivity.class);
                intent.putExtra("LANGUAGE_CD", language_cd);
                intent.putExtra("TYPE", "2");
                startActivityForResult(intent, Constant.LANG_REFRESH);
                break;

            case R.id.checkEdit_Profile:
//                updateProfile();
                intent = new Intent(context, ProfileDetailEditActivity.class);
                startActivityForResult(intent, Constant.PROFILE_REFRESH);

                break;


            case R.id.checkEdit_bank:

                intent = new Intent(context, AddBankDetail.class);
                intent.putExtra("DATA", userData.getUserBank());
                startActivityForResult(intent, Constant.PAGE_REFRESH);

                break;
            case R.id.checkEdit_lang:
                intent = new Intent(context, SearchLanguageCategoryActivity.class);
                intent.putExtra("TYPE", "1");
                startActivityForResult(intent, Constant.PICK_LANGUAGE);

                break;
            case R.id.imgEdit:
                dialog();
                break;
            case R.id.imgProfile:
                dialog();
                break;
            case R.id.txt_img_updated:
                dialog();
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

                    callRemoveImage();
                } else {
                    showInternetPop(context);
                }
                break;


            case R.id.imgAdd_Address:
//                openAddOffice(addressLocation);
                break;

            case R.id.txt_add_specialization:
                intent = new Intent(context, SearchLanguageCategoryActivity.class);
                intent.putExtra("TYPE", "2");
                intent.putExtra("LANG_CD", language_cd);
                startActivityForResult(intent, Constant.PICK_CATEGORY);
                break;

            case R.id.imgBack:
                onBackPressed();


                break;
            case R.id.txt_bank_Update:

                if (isConnectingToInternet(context)) {
                    intent = new Intent(context, AddBankDetail.class);
                    UserBank userBank = userData.getUserBank();
                    intent.putExtra("DATA", userBank);
                    startActivity(intent);
                }
                break;

            default:
                break;

        }
    }




    private void getUserDetail() {
        if (Utility.isConnectingToInternet(context)) {
            showLoading();
            UserId user = new UserId();
            user.setUser_id(userId);
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<UserRoot> call = apiInterface.getUserDetails(accessToken, user);
            call.enqueue(new retrofit2.Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                    dismiss_loading();

                    if (response.body() != null) {
                        UserRoot root = response.body();
                        if (root.getStatus() == 200) {
                            userData = root.getUserBody().getUser();
                            DataPrefrence.setPref(context, Constant.USER_NAME, userData.getUser_name());
                            edt_fullname.setText(userData.getUser_name());
                            //   edt_selectCountry.setText(user.getcjsonObject.getString("country_name"));
                            edt_phone_number.setText(userData.getMobile_no());
                            edt_email.setText(userData.getEmail());


                            if (userData.getImageUrl() != null && !userData.getImageUrl().isEmpty()) {
                                Picasso.with(context).load(Urls.BASE_IMAGES_URL + userData.getImageUrl()).placeholder(R.mipmap.profile_large).into(imgProfile);
                                DataPrefrence.setPref(context, Constant.PROFILE_IMAGE, Urls.BASE_IMAGES_URL + userData.getImageUrl());
                            }

                            country_cd = userData.getCountry_id().getCountry_cd();
                            edt_selectCountry.setText(userData.getCountry_id().getCountry_english());


                            UserBank bank = userData.getUserBank();

                            edt_Bankname.setText(bank.getBank_name());
                            edt_AccountNo.setText(bank.getAccount_number());
                            edt_BankIbn.setText(bank.getIban_code());
                            edt_swifcode.setText(bank.getSwift_code());
                            edt_BranchName.setText(bank.getBank_address());

                            int langsize = userData.getUserLangauges().size();
                            StringBuffer lang_name = new StringBuffer("");
                            StringBuffer lang_id = new StringBuffer("");

                            languageArrayList = new ArrayList<>();
                            brief_cvList_add = new ArrayList<>();
                            for (int i = 0; i < langsize; i++) {
                                UserLangauges userLangauges = userData.getUserLangauges().get(i);
                                Brief_CV brief_cv = new Brief_CV();
                                brief_cv.setChecked(false);
                                brief_cv.setLanguage_cd(userData.getUserLangauges().get(i).getCode());
                                brief_cv.setEnglish_name(userLangauges.getName());
                                brief_cv.setNative_name(userLangauges.getNativeName());
                                brief_cv.setBrief_cv("");
                                brief_cv.setSer_no("");
                                brief_cv.setLanguage_id(userLangauges);
                                lang_name.append(userLangauges.getNativeName() + ",");
                                lang_id.append(userLangauges.getCode() + ",");
                                languageArrayList.add(brief_cv);
                                brief_cvList_add.add(brief_cv);

                            }


                            languageEditAdapter = new LanguageEditAdapter(context, languageArrayList, new ApiCallback() {
                                @Override
                                public void result(String x) {
                                    if (isConnectingToInternet(context)) {
                                        getUserDetail();
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

                            setBriefCV(userData.getBriefCV());


                            for (int i = 0; i < userData.getUserCategory().size(); i++) {
                                Category category = new Category();
                                category.setCategoryName(userData.getUserCategory().get(i).getCategoryName());
                                category.setCategoryID(userData.getUserCategory().get(i).getId());
                                catgStoreHashMap.put(category.getCategoryID(), category);
                                callAddCategory();
                            }


                            DataPrefrence.setBriefLanguage(context, Constant.BRIEF_CV_DB, userData.getBriefCV());


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


    File imageFile;


    private void persistImage(Bitmap bitmap, String name) {
        imgProfile.setImageBitmap(bitmap);
        File filesDir = context.getFilesDir();
        imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }


    }

    private void callUpdateProfileImage() {
        if (isConnectingToInternet(context)) {
            showLoading();

            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            RequestBody usercd = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody langId = RequestBody.create(MediaType.parse("text/plain"), "en");
            RequestBody requestfile = null;
            MultipartBody.Part productimg = null;

            if (imageFile != null) {

                requestfile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                productimg = MultipartBody.Part.createFormData("image", imageFile.getName(), requestfile);
            }

            Call<ResponseBody> call = apiInterface.reqUpdateProfileImage(accessToken, usercd, productimg);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    if (response.body() != null) {

                        try {
                            imgProfile.setImageBitmap(bmProfileImg);
                            ResponseBody  responseBody = response.body();
                            String data =responseBody.string();
                            JSONObject jsonObject=new JSONObject(data);
                            if (jsonObject.getInt("status")==200)
                            {
                                JSONObject object=jsonObject.getJSONObject("body");
                                String url =Urls.BASE_IMAGES_URL+object.getString("imageUrl");
                                DataPrefrence.setPref(context,Constant.PROFILE_IMAGE,url);

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        imgProfile.setImageBitmap(myBitmap);
                        errorBody(response, true);
                    } else {
                        errorBody(response, false);
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

    private void callRemoveImage() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callRemoveImage(userId, accessToken);
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

                }
            });
        } else {
            showInternetConnectionToast();
        }
    }


    public void setBriefCV(final ArrayList<Brief_CV> list) {
        String lang_id = "", check_lang_id = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Brief_CV brief_cv = list.get(i);
                lang_id = brief_cv.getLanguage_id().getCode();

                for (int j = 0; j < brief_cvList_add.size(); j++) {
                    Brief_CV cv = brief_cvList_add.get(j);
                    check_lang_id = cv.getLanguage_id().getCode();
                    if (check_lang_id.equals(lang_id)) {
                        brief_cvList_add.remove(cv);
                    }
                }

            }
        }


        for (int j = 0; j < brief_cvList_add.size(); j++) {
            Brief_CV brief_cv = brief_cvList_add.get(j);
            list.add(brief_cv);
        }

        briefCvAdapter = new BriefCvEditAdapter(context, list, new ApiCallback() {
            @Override
            public void result(String x) {
                int position = Integer.parseInt(x);
                Brief_CV brief_cv = list.get(position);
                Intent intent = new Intent(context, AddBriefCVActivity.class);
                intent.putExtra("DATA", brief_cv);
                intent.putExtra("POSITION", position);
                startActivityForResult(intent, Constant.PAGE_REFRESH);


//                dialogUpdateBriefCv(brief_cv, position);


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
        if (isLocationEnabled(context)) {
//            AddressLocation addressLocation = data;
//            Intent intent = new Intent(context, AddEditAddressActivity.class);
//            intent.putExtra("DATA", addressLocation);
//            startActivity(intent);
//            startTransition();
        } else {
            showSettingsAlert(new ApiCallback() {
                @Override
                public void result(String x) {
                }
            });
        }
    }


    private void callAddCategory() {
        StringBuffer catgId = new StringBuffer("");
        categoryArrayList = new ArrayList<>();
        if (catgStoreHashMap.size() > 0) {
            for (Map.Entry<String, Category> eee : catgStoreHashMap.entrySet()) {
                String key = eee.getKey();
                Category category = eee.getValue();

                catgId.append(category.getCategoryID() + ",");

                categoryArrayList.add(category);
                System.out.println("language_cd" + key);
                // TODO: Do something.
            }
            String selected = "" + catgId;

            categoryId = selected.substring(0, selected.length() - 1);
            ProfileCategoryAdapter profileCategoryAdapter = new ProfileCategoryAdapter(context, categoryArrayList);
            recyclerView_category.setAdapter(profileCategoryAdapter);
        }
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
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
    }



    public void editable(CustomEditText edt, boolean flag) {
        edt.setCursorVisible(flag);
        edt.setClickable(flag);
        edt.setFocusable(flag);
        edt.setFocusableInTouchMode(flag);
    }


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
                            bmProfileImg=null;
                            bmProfileImg = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            imgProfile.setImageBitmap(bmProfileImg);
                            imageFile = null;
                            imageFile = Utility.getFileByBitmap(context, bmProfileImg, "profileImage");
                            callUpdateProfileImage();
//                            bm = ConvetBitmap.Mytransform(bm);
//                            bm = Utility.rotateImage(bm, new File(PicturePath));
//                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
//                            bm.compress(Bitmap.CompressFormat.JPEG, 50, datasecond);
//                            ByteArray = datasecond.toByteArray();
//                            patientProfile = base64String(datasecond.toByteArray());
//                            //imgProfile.setImageBitmap(bm);
//                            if (isConnectingToInternet(context))
//                            {
////                                callUpdateProfileImage(patientProfile);
//                            }

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
            getUserDetail();

        }
        if (requestCode == Constant.LANG_REFRESH) {
            getUserDetail();

        }
        if (requestCode == Constant.PROFILE_REFRESH) {
            edt_fullname.setText(DataPrefrence.getPref(context, Constant.USER_NAME, ""));

        }
        if (requestCode == Constant.PICK_LANGUAGE) {

            languageEditAdapter.notifyDataSetChanged();
            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    language_name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    language_cd = data.getStringExtra("ID");
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
                catgStoreHashMap.put(category.getCategoryID(), category);
                callAddCategory();
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


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
//        Intent intent=new Intent(context,ConsultantUserActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }
}
