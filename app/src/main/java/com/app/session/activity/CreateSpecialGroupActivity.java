package com.app.session.activity;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.StoryGroup;
import com.app.session.utility.Constant;
import com.app.session.utility.ConvetBitmap;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import static com.app.session.utility.PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS;

public class CreateSpecialGroupActivity extends BaseActivity implements View.OnClickListener {

    Context context;
    String group_name = "", group_price = "", group_purpose = "", PicturePath = "", patientProfile = "",update_group_cd="0";
    private Dialog dialogSelect;
    public int isForCamera = 0;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    CircleImageView imgProfile;
    CustomTextView txt_create_group;
    CustomEditText edt_group_price, edt_group_purpose, edt_group_name;
    StoryGroup storyGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_special_group);
        context = this;
        if (getIntent().getParcelableExtra("DATA") != null) {
            storyGroup = getIntent().getParcelableExtra("DATA");
        }
        initView();
    }

    private void initView() {
        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(this);
        txt_create_group = (CustomTextView) findViewById(R.id.txt_create_group);
        edt_group_name = (CustomEditText) findViewById(R.id.edt_group_name);
        edt_group_price = (CustomEditText) findViewById(R.id.edt_group_price);
        edt_group_purpose = (CustomEditText) findViewById(R.id.edt_group_purpose);
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.create_spcl_group));
        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);
        txt_create_group.setOnClickListener(this);

        if (storyGroup != null) {
            update_group_cd=storyGroup.getGroup_user_cd();
            edt_group_price.setText(storyGroup.getFee());
            edt_group_name.setText(storyGroup.getGroup_name());
            edt_group_purpose.setText(storyGroup.getGroup_desc());
            if (storyGroup.getGroup_url_image_address() != null) {
                if (!storyGroup.getGroup_url_image_address().isEmpty()) {
                    AQuery aQuery = null;
                    aQuery = new AQuery(context);
                    aQuery.id(imgProfile).image(storyGroup.getGroup_url_image_address(), false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
                } else {
                    imgProfile.setImageResource(R.mipmap.profile_pic);
                }
            } else {
                imgProfile.setImageResource(R.mipmap.profile_pic);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgProfile:
                dialog();
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
            case R.id.txt_create_group:
                if (isInternetConnected()) {
                    if (validation()) {
                        callCreateGroup(getParamMyStory(), Urls.CREATE_GROUP);
                    }
                } else {
                    showInternetPop(context);
                }
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            default:
                break;
        }
    }


    private void callCreateGroup(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                finish();}

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

    private Map<String, Object> getParamMyStory() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("group_name", group_name);
        params.put("group_desc", group_purpose);
        params.put("fee", group_price);
        params.put("update_group_cd", update_group_cd);
        params.put("file", imageFile);
        return params;
    }

    File imageFile = null;

    private void persistImage(Bitmap bitmap) {
        File filesDir = context.getFilesDir();
        imageFile = new File(filesDir, "consult.jpg");

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

    private boolean validation() {
        group_name = edt_group_name.getText().toString();
        group_purpose = edt_group_purpose.getText().toString();
        group_price = edt_group_price.getText().toString();

        if (group_name.isEmpty()) {
            edt_group_name.setError(context.getResources().getString(R.string.hint_group_name));
            return false;
        }
        if (group_purpose.isEmpty()) {
            edt_group_purpose.setError(context.getResources().getString(R.string.hint_group_name));
            return false;
        }
        if (group_price.isEmpty()) {
            edt_group_price.setError(context.getResources().getString(R.string.hint_group_name));
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:
//                    if (is_company.equals("1") && is_consultant.equals("0")) {
//                        CropImage.activity(mCameraImageUri)
//                                .setAspectRatio(274, 130)
//                                .setFixAspectRatio(true)
//                                .start(this);
//                    } else
                {

                    CropImage.activity(mCameraImageUri)
                            .setAspectRatio(1, 1)
                            .setFixAspectRatio(true)
                            .start(this);
                }
                break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {
                        mImageCaptureUri = data.getData();
                        {
                            CropImage.activity(mImageCaptureUri)
                                    .setAspectRatio(1, 1)
                                    .setFixAspectRatio(true)
                                    .start(this);
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
//                            ByteArray = datasecond.toByteArray();
//                            patientProfile = base64String(datasecond.toByteArray());
                            imgProfile.setImageBitmap(bm);
                            persistImage(bm);

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

    }

    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);
        System.out.println("imgString " + imgString);
        return imgString;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (this != null && ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
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

    public void setGalleryPermission() {
        isForCamera = 2;
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
        }


        Gallery();
    }

    public void TakePic() {

        try {
            isForCamera = 1;
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory() + "/sessionway.png");
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


    public void dialog() {
        dialogSelect = new Dialog(context, R.style.MaterialDialogSheet);
        dialogSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelect.setContentView(R.layout.dialog_default);
        dialogSelect.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogSelect.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogSelect.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        int i = Utility.getScreenWidth(this);
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

}
