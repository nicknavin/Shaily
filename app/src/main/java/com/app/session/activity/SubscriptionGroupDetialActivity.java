package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.adapter.SubscriptionStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.StoryData;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.network.BaseAsych;
import com.app.session.service.FileUploadService;
import com.app.session.thumby.MainActivitythumby;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Methods;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;
import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;
import static com.app.session.utility.Utility.isConnectingToInternet;

public class SubscriptionGroupDetialActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {
    private static final String TAG = "SubscriptionGroupDetialActivity";
    ScrollView layMid;
    CheckBox imgDrop;
    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    private ServiceResultReceiver mServiceResultReceiver;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    String mFileName = "", imgCoverName = "", videoUrl = "", videoCoverImg = "", videoCoverImgName = "", language_cd = "", currency_cd = "", category_cd = "", subscription_group_cd = "", subscription_group_name = "";
    ImageView imgGroupCover, imgVideoCover, imgMenu, imgProfileCover;
    LinearLayout layProgress;
    Bitmap bmGroupCover = null, bmVideoCover = null;
    CustomTextView txtUploading;
    ProgressBar progressBar;
    SubscriptionGroup subscriptionGroup = null;
    CustomTextView txtGroupName, txtPrice, txtGroupName2, txtLanguageName, txtCategoryName, txtCategoryName1, txtCurrency, txtGroupName3;
    ReadMoreTextView txtDiscription;
    Bundle bundle;
    Context context;
    boolean refreshpage;
    String story_time = "", story_text = "", story_type = "", thumbnail_text = "", story_caption = "", story_title = "", story_imgBase64 = "";
    CustomEditText edt_title, edt_story;
    ImageView img_attachment, imgStory;
    CustomTextView txt_story_add;
    public String accessToken = "", userId = "", is_consultant = "", is_company = "", user_name = "";
    private boolean flagStory;
    ArrayList<StoryData> storyDataArrayList = new ArrayList<>();
    SubscriptionStoryAdapter subscriptionStoryAdapter;
    String groupName = "", groupiconUrl = "";
    RecyclerView recyclerView;
    ImageView imgAdd;
    FloatingActionButton fab;
    ArrayList<Brief_CV> brief_cvList;
    UserSubscriptionGroupsBody groupsBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_group_detial_new);
        context = this;
//        initGetYouTube();
        initData();
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("BUNDLE");
        if (bundle != null) {
            brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");
        }
        if (getIntent().getStringExtra("ID") != null) {
            subscription_group_cd = getIntent().getStringExtra("ID");
            ((CustomTextView) findViewById(R.id.header)).setText(getIntent().getStringExtra("NAME"));

        }
        initView();
        getGroupData();

    }


    private void initData() {
        accessToken = DataPrefrence.getPref(context, Constant.ACCESS_TOKEN, "");
        userId = DataPrefrence.getPref(context, Constant.USER_ID, "");
    }


    @Override
    protected void onResume() {
        super.onResume();
        callGetSubscriptionGroup();
        callGetStory();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txt_story_add = (CustomTextView) findViewById(R.id.txt_story_add);
        txt_story_add.setOnClickListener(this);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        txtGroupName3 = (CustomTextView) findViewById(R.id.txtGroupName3);
        txtCurrency = (CustomTextView) findViewById(R.id.txtCurrency);
        txtCategoryName = (CustomTextView) findViewById(R.id.txtCategoryName);
        txtCategoryName1 = (CustomTextView) findViewById(R.id.txtCategoryName1);
        txtLanguageName = (CustomTextView) findViewById(R.id.txtLanguageName);
        txtGroupName2 = (CustomTextView) findViewById(R.id.txtGroupName2);
        txtPrice = (CustomTextView) findViewById(R.id.txtPrice);
        txtGroupName = (CustomTextView) findViewById(R.id.txtGroupName);
        txtDiscription = (ReadMoreTextView) findViewById(R.id.txtDiscription);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtUploading = (CustomTextView) findViewById(R.id.txtUploading);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        imgGroupCover = (ImageView) findViewById(R.id.imgGroupCover);
        imgProfileCover = (ImageView) findViewById(R.id.imgProfileCover);
        imgVideoCover = (ImageView) findViewById(R.id.imgVideoCover);
        imgVideoCover.setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgMenu)).setOnClickListener(this);
        imgDrop = (CheckBox) findViewById(R.id.imgDrop);
        imgDrop.setOnClickListener(this);
        layMid = (ScrollView) findViewById(R.id.layMid);
        ((ImageView) findViewById(R.id.imgEditdisc)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgEditDetail)).setOnClickListener(this);


    }
    private void getGroupData()
    {
        if(getIntent().getParcelableExtra(Constant.SUBS_GROUP)!=null)
        {
            groupsBody =getIntent().getParcelableExtra(Constant.SUBS_GROUP);
            txtGroupName.setText(groupsBody.getGroup_name());
            txtGroupName2.setText(groupsBody.getGroup_name());
            txtCurrency.setText(groupsBody.getCurrency_id().getCurrency_name());
            txtDiscription.setText(groupsBody.getGroup_description());
            txtPrice.setText(groupsBody.getSubscription_price());
            txtLanguageName.setText(groupsBody.getLanguage_id().getNativeName());

        }
    }
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fab:
                intent = new Intent(context, AddSubscriptionStoryActivity.class);
                Bundle arg = new Bundle();
                arg.putSerializable("List", (Serializable) brief_cvList);
                intent.putExtra("BUNDLE", arg);
                intent.putExtra("ID", subscription_group_cd);
                startActivity(intent);
                break;
            case R.id.txt_story_add:
//
                callStoryDialog();
                break;
            case R.id.imgEditDetail:
                refreshpage = false;
                intent = new Intent(context, SubscriptionGroupUpdateActivity.class);
                intent.putExtra("BUNDLE", bundle);
                intent.putExtra("TYPE", "1");
                intent.putExtra("DATA", subscriptionGroup);
                startActivity(intent);
                break;
            case R.id.imgEditdisc:
                refreshpage = false;
                intent = new Intent(context, UpdateSubscriptionGroupDiscription.class);
                intent.putExtra("DATA", subscriptionGroup);
                startActivity(intent);
                break;


            case R.id.imgVideoCover:
                if (!videoUrl.isEmpty()) {
                    intent = new Intent(context, VideoPlayerActivity.class);
                    String url = Urls.BASE_VIDEO_URL + videoUrl;
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                break;
            case R.id.imgDrop:

                if (imgDrop.isChecked()) {
                    layMid.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    layMid.setVisibility(View.GONE);
                }

                break;

            case R.id.imgMenu:
                flagStory = false;
                showMenu(view);
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

        }
    }


    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.subscriptiongroup_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_select_cover:
                dialog();
                return true;
            case R.id.menu_replace_video:
                callVideoFrameThumb();
                return true;

        }

        return false;
    }


    public void callVideoFrameThumb() {
        Intent intent = new Intent(context, MainActivitythumby.class);
        startActivityForResult(intent, Constant.PICK_VIDEO_THUMB);

    }


    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public void dialog() {
        dialogSelect = new Dialog(context, R.style.MaterialDialogSheet);
        dialogSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelect.setContentView(R.layout.dialog_pick_image);
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

//        ((View) dialogSelect.findViewById(R.id.view1)).setTranslationX(-i);
//        ((View) dialogSelect.findViewById(R.id.view2)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).animate().translationX(0).setDuration(500).setStartDelay(400);
//        ((View) dialogSelect.findViewById(R.id.view1)).animate().translationX(0).setDuration(500).setStartDelay(500);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).animate().translationX(0).setDuration(500).setStartDelay(600);
//        ((View) dialogSelect.findViewById(R.id.view2)).animate().translationX(0).setDuration(500).setStartDelay(700);

        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).setOnClickListener(this);


        dialogSelect.show();
    }

    public void TakePic() {

        try {
            isForCamera = true;
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


    private void callUploadImage(String baseImg, String imgName) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPLOADIMAGE, getParamUplodImage(baseImg, imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("image requ: " + js.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {

                            callUploadImageFinal(imgName);
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

    private String getParamUplodImage(String baseImg, String imgName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fname", imgName);
            jsonObject.put("img", baseImg);

            System.out.println("parameterImage" + jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void callUploadImageFinal(String imgName) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPDATE_GROUP_IMAGE, getParamUplodImageFinal(imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        System.out.println("");
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("image requ final: " + js.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {
                            imgGroupCover.setImageBitmap(bmGroupCover);
                            imgProfileCover.setImageBitmap(bmGroupCover);
                            imgCoverName = jsonObject.getString("url");
                            //callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);
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

    private String getParamUplodImageFinal(String imgName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("subscription_group_cd", subscription_group_cd);
            jsonObject.put("user_cd", userId);
            jsonObject.put("url", imgName + ".png");
            System.out.println("parameterImageFinal" + jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void callService(String path) {

        if (isConnectingToInternet(context)) {
            mServiceResultReceiver = new ServiceResultReceiver(new Handler());
            mServiceResultReceiver.setReceiver(this);
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", path);
            mIntent.putExtra("FileName", mFileName);
            mIntent.putExtra(RECEIVER, mServiceResultReceiver);
            mIntent.setAction(ACTION_DOWNLOAD);
            FileUploadService.enqueueWork(this, mIntent);
        }
        else
        {
            showInternetConnectionToast();
        }

    }

    private void callUploadVideoUpdate() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPLOAD_GROUP_VIDEO, getParamUpdateVideo(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        //{"result":{"rdescription":"Success","rstatus":"1","url":"30_group_video_url"}}
                        imgVideoCover.setImageBitmap(bmVideoCover);
                        imgVideoCover.setBackgroundColor(Utility.getDominantColor(bmVideoCover));
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1")) {
                            callUploadVideoCoverImage(videoCoverImg, videoCoverImgName);
                            if (jsonObject.getString("url") != null && !jsonObject.getString("url").isEmpty()) {
                                videoUrl = jsonObject.getString("url");
                                System.out.println("videourl" + videoUrl);

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

    private String getParamUpdateVideo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_cd", userId);
            jsonObject.put("subscription_group_cd", subscription_group_cd);
            jsonObject.put("video_url", mFileName);
            System.out.println("Video parameter " + jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SHOW_RESULT:
                if (resultData != null) {
                    initProgress(resultData.getInt("progress"));

                }
                break;

            case STATUS:
                layProgress.setVisibility(View.GONE);
                callUploadVideoUpdate();
                break;

            case FAIL:
                showToast("uploading is fail, please try again");
                layProgress.setVisibility(View.GONE);
                break;

        }
    }


    public void CustomPopupSpinner(View view) {
        final View mView = LayoutInflater.from(context).inflate(R.layout.dialog_g, null, false);
        final PopupWindow popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        popUp.showAsDropDown(view);
    }


    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);

        return imgString;
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private int progressStatus = 0;

    public void initProgress(int value) {
        txtUploading.setText("" + value + "%" + "uploaded...Please wait");
        layProgress.setVisibility(View.VISIBLE);
        progressBar.setProgress(value);

        if (value == 100) {
            progressBar.setVisibility(View.GONE);
            layProgress.setVisibility(View.GONE);
        }

    }


    private void callGetSubscriptionGroup() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.GET_SUBSCRIPTION_GROUP, getSubcriptionGroup(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {

                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1")) {

                            if (js.getJSONArray("subscription_group_list") != null) {
                                JSONArray jsonArray = js.getJSONArray("subscription_group_list");
                                JSONObject object = jsonArray.getJSONObject(0);
                                subscriptionGroup = new SubscriptionGroup();
                                subscriptionGroup.setCategory_cd(object.getString("category_cd"));
                                subscriptionGroup.setCategory_name(object.getString("category_name"));
                                subscriptionGroup.setCurrency_cd(object.getString("currency_cd"));
                                subscriptionGroup.setCurrency_name_en(object.getString("currency_name_en"));
                                subscriptionGroup.setGroup_desc(object.getString("group_desc"));
                                subscriptionGroup.setGroup_introduction_video_url(object.getString("group_introduction_video_url"));
                                subscriptionGroup.setGroup_name(object.getString("group_name"));
                                subscriptionGroup.setGroup_url_image_address(object.getString("group_url_image_address"));
                                subscriptionGroup.setLanguage_cd(object.getString("language_cd"));
                                subscriptionGroup.setLanguage(object.getString("language"));
                                subscriptionGroup.setSubscription_group_cd(object.getString("subscription_group_cd"));
                                subscriptionGroup.setSubscription_price(object.getDouble("subscription_price"));
                                subscriptionGroup.setUser_cd(object.getString("user_cd"));
                                subscriptionGroup.setGroup_thumbnail_url(object.getString("group_thumbnail_url"));
                                subscription_group_name = subscriptionGroup.getGroup_name();
                                subscription_group_cd = subscriptionGroup.getSubscription_group_cd();
                                Typeface face = Typeface.createFromAsset(context.getAssets(),
                                        "Roboto-Regular.ttf");
                                txtDiscription.setTypeface(face);
                                txtDiscription.setTrimCollapsedText(" more");
                                txtDiscription.setTrimExpandedText(" less");
                                txtDiscription.setText(subscriptionGroup.getGroup_desc());

                                txtGroupName.setText(subscriptionGroup.getGroup_name());
                                txtGroupName2.setText(subscriptionGroup.getGroup_name());
                                txtGroupName3.setText(subscriptionGroup.getGroup_name());
                                txtPrice.setText("" + subscriptionGroup.getSubscription_price());
                                txtLanguageName.setText(subscriptionGroup.getLanguage());
                                txtCategoryName.setText(subscriptionGroup.getCategory_name());
                                txtCategoryName1.setText(subscriptionGroup.getCategory_name());
                                txtCurrency.setText(subscriptionGroup.getCurrency_name_en());
                                videoUrl = subscriptionGroup.getGroup_introduction_video_url();

                                if (subscriptionGroup.getGroup_thumbnail_url() != null && !subscriptionGroup.getGroup_thumbnail_url().isEmpty()) {
                                    String url = Urls.BASE_IMAGES_URL + subscriptionGroup.getGroup_thumbnail_url();
                                    Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgVideoCover);

                                }

                                if (subscriptionGroup.getGroup_url_image_address() != null && !subscriptionGroup.getGroup_url_image_address().isEmpty()) {

                                    groupiconUrl = Urls.BASE_IMAGES_URL + subscriptionGroup.getGroup_url_image_address();
                                    //Picasso.with(context).load(Urls.BASE_IMAGES_URL +group.getGroup_url_image_address()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(holder.imgGroup);
                                    //Picasso.with(context).load(Urls.BASE_IMAGES_URL +group.getGroup_url_image_address()).placeholder(R.mipmap.profile_large).into(imgGroupCover);
                                    System.out.println("img url:" + Urls.BASE_IMAGES_URL + subscriptionGroup.getGroup_url_image_address());
                                    Picasso.with(context).load(groupiconUrl).memoryPolicy(MemoryPolicy.NO_STORE).into(imgGroupCover);
//                                    Picasso.with(context).load(url).placeholder(R.mipmap.profile_large).into(imgGroupCover);
                                    Picasso.with(context).load(groupiconUrl).placeholder(R.mipmap.profile_large).into(imgProfileCover);
                                }


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

    private String getSubcriptionGroup() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("subscription_group_cd", subscription_group_cd);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }










    /*upload video cover image */


    private void callUploadVideoCoverImage(String baseImg, String imgName) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPLOADIMAGE, getParamUplodVideoCoverImage(baseImg, imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("image requ: " + js.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {

                            callUploadVideoCoverImageFinal(imgName);
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

    private String getParamUplodVideoCoverImage(String baseImg, String imgName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fname", imgName);
            jsonObject.put("img", baseImg);

            System.out.println("parameterImage" + jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void callUploadVideoCoverImageFinal(String imgName) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPDATE_GROUP_THUMBNAIL, getParamUplodVideoCoverImageFinal(imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        System.out.println("");
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("image requ final: " + js.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {


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

    private String getParamUplodVideoCoverImageFinal(String imgName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("subscription_group_cd", subscription_group_cd);
            jsonObject.put("user_cd", userId);
            jsonObject.put("url", imgName + ".png");
            System.out.println("getParamUplodVideoCoverImageFinal : " + jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean validation() {
        story_title = edt_title.getText().toString();
        story_text = edt_story.getText().toString();

        if (story_title.isEmpty()) {
            showToast(context.getResources().getString(R.string.error_story_title));
            return false;
        }
        if (story_text.isEmpty()) {
            showToast(context.getResources().getString(R.string.error_story));
            return false;
        }
        return true;
    }


    public void callStoryDialog() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.story_dailog_layout);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            imgStory = (ImageView) dd.findViewById(R.id.imgStory);

            img_attachment = (ImageView) dd.findViewById(R.id.img_attachment);
            img_attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flagStory = true;
                    dialog();
                }
            });
            edt_title = (CustomEditText) dd.findViewById(R.id.edt_title);
            edt_story = (CustomEditText) dd.findViewById(R.id.edt_story);

            ((CustomTextView) dd.findViewById(R.id.btn_Send)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validation()) {
                        if (isConnectingToInternet(context)) {
                            story_type = "1";
                            story_time = Utility.getTimeOnly();

                            callUploadImageForStory(story_imgBase64, "story" + Utility.getTimeOnly());

                        } else {
                            showInternetPop(context);
                        }
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


    public static android.app.Dialog dd;
    static ProgressView progressDialog;

    public void showLoading() {

        System.out.println("baseFrg  start loading");

        if (dd != null) {
            dd.dismiss();
        }
        dd = new android.app.Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.custom_loading);
            progressDialog = (ProgressView) dd.findViewById(R.id.rey_loading);
            progressDialog.start();
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismiss_loading() {
        try {
            if (dd != null) {
                if (dd.isShowing() || dd != null) {
                    if (progressDialog != null)
                        progressDialog.stop();
                }
                dd.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showInternetPop(Context context) {
        MyDialog.iPhone(context.getResources().getString(R.string.connection), context);
    }

    public Boolean isInternetConnected() {
        return Methods.isInternetConnected(context);
    }

    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showToast(String x) {
        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }

    public void showInternetConnectionToast() {
        Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
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

    private void callSendStory(String filename) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.SUBSCRIPTION_SEND_STORY, sendStoryParameter(filename), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        System.out.println("");
                        JSONObject jsonObject = js.getJSONObject("result");

                        if (jsonObject.getString("rstatus").equals("1")) {


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

    private String sendStoryParameter(String fname) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subscription_group_cd", subscription_group_cd);
            jsonObject.put("user_cd", userId);
            jsonObject.put("story_title", story_title);
            jsonObject.put("story_type", story_type);
            jsonObject.put("story_text", story_text);
            jsonObject.put("story_caption", story_caption);
            jsonObject.put("thumbnail_text", fname + ".png");

            return jsonObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  System.out.println("input : " + gsonObject.toString());
        return null;
    }

    private void callSendStoryImg(File file) {
        if (isInternetConnected()) {
            showLoading();
            String url = "http://www.consultlot.com/imageuploadHandler.ashx";

            new BaseAsych(url, file, "profile_image", new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    thumbnail_text = "profile_image";

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


    private void callGetStory() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.SUBSCRIPTION_GET_STORY, getStoryParameter(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {

                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("result" + jsonObject.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {
                            try {
                                JSONArray jsonArray = js.getJSONArray("story_data");
                                Type type = new TypeToken<ArrayList<StoryData>>() {
                                }.getType();
                                storyDataArrayList = new Gson().fromJson(jsonArray.toString(), type);
                                subscriptionStoryAdapter = new SubscriptionStoryAdapter(context, storyDataArrayList, subscription_group_name, groupiconUrl, new ObjectCallback() {
                                    @Override
                                    public void getObject(Object object, int position, View view) {
                                        if (position != -1) {
                                            StoryData storyData = (StoryData) object;
                                            showMenu(view, storyData, position);
                                        }

                                    }
                                });
                                recyclerView.setAdapter(subscriptionStoryAdapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
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

    private String getStoryParameter() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subscription_group_cd", subscription_group_cd);


            return jsonObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  System.out.println("input : " + gsonObject.toString());
        return null;
    }


    private void callDeleteStory(String story_id, int position) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.SUBSCRIPTION_DELETE_STORY, getDeleteParameter(story_id), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        System.out.println("");
                        JSONObject jsonObject = js.getJSONObject("result");

                        if (jsonObject.getString("rstatus").equals("1")) {
                            subscriptionStoryAdapter.updateData(position);


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

    private String getDeleteParameter(String story_id) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("story_id", story_id);


            return jsonObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  System.out.println("input : " + gsonObject.toString());
        return null;
    }


    public void showMenu(View v, final StoryData storyData, final int position) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_delete:
                        if (isConnectingToInternet(context)) {
                            callDeleteStory(storyData.getStory_id(), position);
                        } else {
                            showInternetPop(context);
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_subscription_story, popup.getMenu());


        popup.show();
    }


    private void callUploadImageForStory(String baseImg, String imgName) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPLOADIMAGE, getParamUplodImage(baseImg, imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("image requ: " + js.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {

                            callSendStory(imgName);
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

    private String getParamUplodImage0(String baseImg, String imgName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fname", imgName);
            jsonObject.put("img", baseImg);

            System.out.println("parameterImage" + jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshpage = true;
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

                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            if (!flagStory) {
                                bmGroupCover = bm;
                            } else {
                                imgStory.setVisibility(View.VISIBLE);
                                imgStory.setImageBitmap(bm);
                            }
                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                            ByteArray = datasecond.toByteArray();


                            if (!flagStory) {
                                String coverImg = base64String(ByteArray);

                                callUploadImage(coverImg, userId + "_" + subscription_group_name);
                            } else {
                                story_imgBase64 = base64String(ByteArray);

//                                callUploadImageForStory(coverImg, userId + "_" + subscription_group_name);

//                                File file = new File(Utility.getRealPathFromURI(context,resultUri));
//                                story_img_name=  "story_photo.png";
//                                callSendStoryImg(file);

//                                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), Utility.getRealPathFromURI(context,resultUri));
//                                callSendMyStoryImg(requestFile,file);


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
        if (requestCode == Constant.REQUEST_CODE_ALBUM_Gallery) {
            if (data != null) {

                Uri contentURI = data.getData();
                String selectedVideoPath = getPath(contentURI);
                callService(selectedVideoPath);
                Log.d("path", selectedVideoPath);

            }
        }
        if (requestCode == Constant.PICK_VIDEO_THUMB) {

            if (data != null) {

                long location = data.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0);
                Uri uri = data.getParcelableExtra(EXTRA_URI);
                Bitmap bitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);
                // imgBriefCV.setImageBitmap(bitmap);
                bmVideoCover = bitmap;
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                byte[] ByteArray = datasecond.toByteArray();
                videoCoverImg = base64String(ByteArray);
                String selectedVideoPath = data.getStringExtra(VIDEO_PATH);
//                imgBriefCV.setImageBitmap(bm);
                mFileName = subscription_group_cd + "_group_video_url" + ".mp4";
                videoCoverImgName = subscription_group_cd + "_groupname_thumbnail.png";
                callService(selectedVideoPath);
            }

        }


    }


    @Override
    public void finish() {
        super.finish();
        recyclerView.setAdapter(null);
    }


}
