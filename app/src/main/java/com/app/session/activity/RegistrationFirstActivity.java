package com.app.session.activity;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.data.model.UserFcm;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.Language;
import com.app.session.data.model.Root;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.ConvetBitmap;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class RegistrationFirstActivity extends BaseActivity implements View.OnClickListener {

    TextInputEditText  edt_userid, edit_password, edit_cnfmpassword, edt_selectLang;
    TextInputEditText      edt_email;
    CustomTextView txtNext, txtBack;
    private String email = "", password = "", username = "", confirmPassword = "";
    Context context;
    private CircleImageView imgProfile;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    public String PicturePath = "";

    String patientProfile = "", gender = "M";

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_first);
        context = this;
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        initView();
    }

    private void initView() {


        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);

        edt_email = (TextInputEditText) findViewById(R.id.edit_email);
        edt_userid = (TextInputEditText) findViewById(R.id.edit_userName);
        edit_password = (TextInputEditText) findViewById(R.id.edit_password);
        edit_cnfmpassword = (TextInputEditText) findViewById(R.id.edit_cnfmpassword);
        txtNext = (CustomTextView) findViewById(R.id.txtNext);
        txtNext.setOnClickListener(this);
        txtBack = (CustomTextView) findViewById(R.id.txtBack);
        txtBack.setOnClickListener(this);

    }

    private boolean isValid() {

        email = edt_email.getText().toString().trim();
        password = edit_password.getText().toString();
        confirmPassword = edit_cnfmpassword.getText().toString();
        username = edt_userid.getText().toString().trim();
        String namePattrn = "[a-zA-z]+([ '-][a-zA-Z]+)*";
        if (isEmpty(email)) {
            edt_email.setError(context.getResources().getString(R.string.error_email));
            return false;
        }
        if (!Utility.emailValidator(email)) {
            edt_email.setError(context.getResources().getString(R.string.error_email_invalid));
            return false;
        }
        if (username.isEmpty()) {
            edt_userid.setError(context.getResources().getString(R.string.error_name));
            return false;
        }
//        if (!Pattern.matches(namePattrn, username) || username.length() < 3) {
//
//            edt_userid.setError(context.getResources().getString(R.string.error_name_invalid));
//
//            return false;
//        }
        if (isEmpty(password)) {
            edit_password.setError(context.getResources().getString(R.string.error_pwd));
            return false;
        }
        if (password.length() < 4) {
            edit_password.setError(context.getResources().getString(R.string.error_pwd_invalid));

            return false;
        }
        if (confirmPassword.isEmpty()) {
            edit_cnfmpassword.setError(context.getResources().getString(R.string.error_confirm_pwd));

            return false;
        }
        if (!Pattern.matches(password, confirmPassword)) {
            edit_cnfmpassword.setError(context.getResources().getString(R.string.error_confirm_pwd_invalid));

            return false;
        }
//        if(language_id.isEmpty())
//        {
//            edt_selectLang.setError(context.getResources().getString(R.string.select_lang));
//        return false;
//        }

//        if(patientProfile.isEmpty())
//        {
//            showToast(context.getResources().getString(R.string.img_valid));
//            return false;
//        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.txtNext:
                if (isConnectingToInternet(context)) {

                    if (isValid()) {
                        callUserVerify();
                    }
                } else {
                    showInternetPop(context);
                }
                break;
            case R.id.edt_selectLang:
                edt_selectLang.setText(context.getResources().getString(R.string.select_lang));
                Intent intent = new Intent(context, SelectLangaugeActivity.class);
                startActivityForResult(intent, Constant.PICK_LANGUAGE);
                break;
            case R.id.txtBack:
                onBackPressed();
                break;
            case R.id.imgProfile:
                dialog();
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
        }

    }


    private void callUserExist(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {


                    dismiss_loading();
                    DataPrefrence.setPref(context, Constant.EMAILID, email);
                    DataPrefrence.setPref(context, Constant.USER_NAME, username);
                    DataPrefrence.setPref(context, Constant.PASSWORD, password);
                    DataPrefrence.setPref(context, Constant.CONFIRM_PASSWORD, confirmPassword);
                    DataPrefrence.setPref(context, Constant.PROFILE_IMAGE, patientProfile);
                    DataPrefrence.setPref(context, Constant.GENDER,gender);

                    startActivity(new Intent(context, LanguageRegActivity.class));


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

    private Map<String, Object> getParam() {
        Map<String, Object> params = new HashMap<>();
        params.put("email_id", email);
        params.put("user_id", username);


        return params;
    }


    private void callUserExist() {
        if (isConnectingToInternet(context))
        {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callUserExist(username,email);
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

                                dismiss_loading();
                                DataPrefrence.setPref(context, Constant.EMAILID, email);
                                DataPrefrence.setPref(context, Constant.USER_NAME, username);
                                DataPrefrence.setPref(context, Constant.PASSWORD, password);
                                DataPrefrence.setPref(context, Constant.CONFIRM_PASSWORD, confirmPassword);
                                if(DataPrefrence.getPref(context,Constant.IS_CONSULTANT,"").equals("1"))
                                {
                                    startActivity(new Intent(context, SelectLanguageCategoryActivity.class));
                                }
                                else
                                {
                                    startActivity(new Intent(context, VerificationCodeActivity.class));
                                }

                            }else {
                                //{"Status":false,"Message":"Emailid is Already exist"}
                                showToast(js.getString("Message"));
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

    String lang_id = "", name = "", id = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
//                        CropImage.activity(mImageCaptureUri)
//                                .start(this);


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


                            patientProfile = "data:image/jpeg;base64," + base64String(datasecond.toByteArray());
                            imgProfile.setImageBitmap(bm);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;

                case Constant.PICK_LANGUAGE:
                    if (data != null) {
                        if (data.getStringExtra("NAME") != null) {
                            name = data.getStringExtra("NAME");
                        }
                        if (data.getStringExtra("ID") != null) {
                            lang_id = data.getStringExtra("ID");
                        }

                        edt_selectLang.setText(name);
                    }
                    break;

                default:
                    break;


            }
        }
        if (requestCode == Constant.PICK_LANGUAGE) {
            String name = "", id = "";
            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    lang_id = data.getStringExtra("ID");
                }

                edt_selectLang.setText(name);
            }
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

    private Dialog dialogSelect;

    CustomTextView myView;

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
        myView = (CustomTextView) findViewById(R.id.txtNext);
        int i = myView.getWidth();
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
        myView = (CustomTextView) findViewById(R.id.txtNext);
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
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public boolean isForCamera = false;


    private void callSignUp()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callUserRegister(
                    DataPrefrence.getPref(context, Constant.EMAILID, ""),
                    DataPrefrence.getPref(context, Constant.USER_NAME, ""),
                    DataPrefrence.getPref(context, Constant.MOBILE_NO, ""),
                    DataPrefrence.getPref(context, Constant.PASSWORD, ""),
                    DataPrefrence.getPref(context, Constant.FULLNAME, ""),
                    DataPrefrence.getPref(context, Constant.COUNTRY_ID, ""),
                    DataPrefrence.getPref(context, Constant.IS_COMPANY, ""),
                    DataPrefrence.getPref(context, Constant.IS_CONSULTANT, ""),
                    DataPrefrence.getPref(context, Constant.LANGUAGE_CD, ""),
                    DataPrefrence.getPref(context, Constant.CATEGORY_CD,""),
                    DataPrefrence.getPref(context, Constant.GENDER,""),
                    DataPrefrence.getPref(context, Constant.BRIEF_CV,""),
                    DataPrefrence.getPref(context, Constant.PROFILE_IMAGE, "")
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        //{"Status":true,"Message":"User Successfully Register","user_cd":"168"}
                        String data = responseBody.string();
                        try {
                            JSONObject object=new JSONObject(data);
                            if (object.getBoolean("Status"))
                            {

                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                            showToast(object.getString("Message"));
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





    private void callUserVerify()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<Root> call= apiInterface.userVerify(getParameter());
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        Root root= response.body();
                        if(root.getStatus()==200)
                        {
                            dismiss_loading();
                            DataPrefrence.setPref(context, Constant.EMAILID, email);
                            DataPrefrence.setPref(context, Constant.USER_NAME, username);
                            DataPrefrence.setPref(context, Constant.PASSWORD, password);
                            DataPrefrence.setPref(context, Constant.CONFIRM_PASSWORD, confirmPassword);
//                            if(DataPrefrence.getPref(context,Constant.IS_CONSULTANT,"").equals("1"))
//                            {
//                                startActivity(new Intent(context, SelectLanguageCategoryActivity.class));
//                            }
//                            else
//                            {
//                                startActivity(new Intent(context, VerificationCodeActivity.class));
//                            }
                          //  startActivity(new Intent(context, VerificationCodeActivity.class));
                            callRegistration();

                        }
                        else
                        {
                            showToast(root.getMessage());
                        }


                    }
                    else
                    {
                       ResponseBody responseBody= response.errorBody();
                        try {
                            String data =responseBody.string();
                            System.out.println("data error body "+data);
                            try {
                                JSONObject jsonObject=new JSONObject(data);
                                showToast(jsonObject.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
    dismiss_loading();
                }
            });
        }
        else
        {
            showInternetConnectionToast();
        }
    }
    private JsonObject getParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("login_user_id",username);
            jsonObject.put("email",email);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }


    private void callRegistration()
    {
        initBrief();
        ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
        Call<Root> call= apiInterface.userRegistration(getRegParameter());
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response)
            {
                if(response!=null)
                {
                    Root responseBody= response.body();
                    if(responseBody.getStatus()==200) {

                        register(DataPrefrence.getPref(context, Constant.EMAILID, ""),DataPrefrence.getPref(context, Constant.PASSWORD, ""),DataPrefrence.getPref(context, Constant.FULLNAME, ""));
                        showToast(responseBody.getMessage());
                        Intent intent;
                        intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }


                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });
    }

    private JsonObject getRegParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("email",DataPrefrence.getPref(context, Constant.EMAILID, ""));
            jsonObject.put("login_user_id", DataPrefrence.getPref(context, Constant.USER_NAME, ""));
            jsonObject.put("user_password",DataPrefrence.getPref(context, Constant.PASSWORD, ""));
            jsonObject.put("mobile_no",DataPrefrence.getPref(context, Constant.MOBILE_NO, ""));
            jsonObject.put("user_name",DataPrefrence.getPref(context, Constant.FULLNAME, ""));
            jsonObject.put("country_id",DataPrefrence.getPref(context, Constant.COUNTRY_ID, ""));
            jsonObject.put("gender",DataPrefrence.getPref(context, Constant.GENDER,""));
            jsonObject.put("is_consultant",DataPrefrence.getPref(context, Constant.IS_CONSULTANT, ""));
            jsonObject.put("status_id","5f22e1a2dcbf027af6a1f7c5");
            jsonObject.put("mail_verfy","1");

            JSONArray jsonArrayLang=new JSONArray();

            for(int k=0;k<selectlanguageList.size();k++)
            {
                JSONObject obj=new JSONObject();
                String id =selectlanguageList.get(k).get_id();
                jsonArrayLang.put(id);
            }

            jsonObject.put("userLangauges",jsonArrayLang);
            JSONArray arrayBrief =new JSONArray();
            for (int i = 0; i < brief_cvList.size(); i++) {

                Brief_CV cv = brief_cvList.get(i);
                JSONObject object = new JSONObject();
                object.put("language_id", cv.getLanguage_cd());
                if(cv.getBrief_cv()==null)
                {
                    object.put("brief_cv", " ");
                }
                else if(cv.getBrief_cv().isEmpty())
                {
                    object.put("brief_cv", " ");
                }
                else
                {
                    object.put("brief_cv", cv.getBrief_cv());
                }

                arrayBrief.put(object);

            }
            jsonObject.put("briefCV",arrayBrief);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }



    private void register(String email,String password,String name)
    {


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                    String uid = firebaseAuth.getCurrentUser().getUid();
                    DataPrefrence.setPref(context, Constant.U_ID, uid);

                    DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
                    UserFcm users = new UserFcm();
                    users.setUid(uid);
                    users.setName(name);

                    documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //showToast("onSuccess: user profile is created for " + uid);
                            log("onSuccess: user profile is created for " + uid);
                        }
                    });



                    //Toast.makeText(context, "User created ", Toast.LENGTH_SHORT).show();
                    log("User created ");


                } else {
                   // Toast.makeText(context, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public ArrayList<Language> selectlanguageList = new ArrayList<>();
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();

    private void initBrief()
    {

        selectlanguageList= DataPrefrence.getLanguage(context, Constant.LANGUAGE_SPEAK);

        for(int i =0;i<selectlanguageList.size();i++)
        {
            Language language=selectlanguageList.get(i);
            Brief_CV brief_cv = new Brief_CV();
            brief_cv.setBrief_cv("");
            brief_cv.setNative_name(language.getName());
            brief_cv.setLanguage_cd(language.get_id());
            brief_cvList.add(brief_cv);
        }
    }

}
