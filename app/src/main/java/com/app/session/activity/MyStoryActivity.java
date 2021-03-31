package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.adapter.MyStoryAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.MyStory;
import com.app.session.data.model.StoryGroup;
import com.app.session.utility.Constant;
import com.app.session.utility.ConvetBitmap;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyStoryActivity extends BaseActivity implements View.OnClickListener {

    private static final int PICK_CAPTURE_VIDEO = 120;
    private static final int PICK_CAPTURE_IMAGE = 121;
    Context context;
    CustomTextView txt_userName, txt_story_add;
    CircleImageView imgProfile;
    ArrayList<MyStory> storyList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView img_attachment, imgTake_cam;

    public boolean isForCamera = false, isCameraVideo = false, isGalleryImg = false, isGalleryVideo = false;

    String story_time = "", story_text = "", story_type = "", capture_video = "",
            story_caption = "", story_title = "", PicturePath = "", patientProfile = "";
    byte[] ByteArray;
    File file = null;
    CustomEditText edt_title, edt_story;
    private Dialog dialogSelect;
    private Uri mCameraImageUri = null, mImageCaptureUri = null;
    String follower_user_cd = "";
    LinearLayout lay_bottem;
    StoryGroup storyGroup = null;

    ImageView img_add, imgOption;
    public File imageFile = null;
    public static File originalFile = null;
    CoordinatorLayout layout;
    boolean flag_refresh;
    MyStoryAdapter storyAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);
        context = this;
        if (getIntent().getStringExtra("ID") != null) {
            follower_user_cd = getIntent().getStringExtra("ID");
        }
        if (getIntent().getParcelableExtra("DATA") != null) {
            storyGroup = getIntent().getParcelableExtra("DATA");
        }
        initView();
//        initViewRecycleview();


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!flag_refresh) {
            if (isConnectingToInternet(context)) {
                if (follower_user_cd.isEmpty()) {
                    callGetMyStory(getParamMyStory(), Urls.GET_MYSTORY);
                } else {
                    callGetMyStory(getParamFollowrStory(), Urls.GET_UNSEEN_STORY);
                }
            } else {
                showInternetPop(context);
            }
        }
    }


    private void initView() {

        if (!follower_user_cd.isEmpty()) {
            ((ImageView) findViewById(R.id.imgOption)).setVisibility(View.GONE);
        }

        layout = (CoordinatorLayout) findViewById(R.id.root);

        img_add = (ImageView) findViewById(R.id.img_add);
        img_add.setOnClickListener(this);
        imgOption = (ImageView) findViewById(R.id.imgOption);
        imgOption.setOnClickListener(this);
        lay_bottem = (LinearLayout) findViewById(R.id.lay_bottem);
        img_attachment = (ImageView) findViewById(R.id.img_attachment);
        img_attachment.setOnClickListener(this);
        imgTake_cam = (ImageView) findViewById(R.id.imgTake_cam);
        imgTake_cam.setOnClickListener(this);
//        txt_following = (CustomTextView) findViewById(R.id.txt_following);
//        txt_Follower = (CustomTextView) findViewById(R.id.txt_Follower);
        txt_story_add = (CustomTextView) findViewById(R.id.txt_story_add);
        txt_userName = (CustomTextView) findViewById(R.id.txt_userName);
//        txt_flwer_count = (CustomTextView) findViewById(R.id.txt_flwer_count);
//        txt_flwing_count = (CustomTextView) findViewById(R.id.txt_flwing_count);
        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (!follower_user_cd.isEmpty()) {
            lay_bottem.setVisibility(View.GONE);
            img_add.setVisibility(View.GONE);
        }

        if (storyGroup != null) {
//            txt_flwer_count.setVisibility(View.GONE);
//            txt_following.setVisibility(View.GONE);
//            txt_Follower.setVisibility(View.GONE);
//            txt_flwing_count.setVisibility(View.GONE);
            if (storyGroup.getGroup_url_image_address() != null) {
                String imageUrl = storyGroup.getGroup_url_image_address();
                System.out.println("patientProfile " + imageUrl);
                if (!imageUrl.equals("")) {
                    AQuery aQuery = null;
                    aQuery = new AQuery(context);
                    aQuery.id(imgProfile).image(imageUrl, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
                } else {
                    imgProfile.setImageResource(R.mipmap.profile_large);
                }
            }
            txt_userName.setText(storyGroup.getGroup_name());
        }


        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        txt_story_add.setOnClickListener(this);
    }

    private void callGetMyStory(Map<String, Object> param, String url) {
        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    try {
                        storyList = new ArrayList<MyStory>();
                        JSONObject object = js.getJSONObject("user_data");
                        txt_userName.setText(object.getString("user_name"));
                        object.getString("imgUrl");
//                        txt_flwer_count.setText(object.getString("follower_count"));
//                        txt_flwing_count.setText(object.getString("following_count"));
                        if (object.getString("imgUrl") != null) {
                            String imageUrl = object.getString("imgUrl");
                            System.out.println("patientProfile " + imageUrl);
                            if (!imageUrl.equals("")) {
                                AQuery aQuery = null;
                                aQuery = new AQuery(context);
                                aQuery.id(imgProfile).image(imageUrl, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
                            } else {
                                imgProfile.setImageResource(R.mipmap.profile_large);
                            }
                        }
                        JSONArray array;
                        if (follower_user_cd.isEmpty()) {
                            array = js.getJSONArray("all_user_story");
                        } else {
                            array = js.getJSONArray("user_story");
                        }

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jsonObject = array.getJSONObject(i);
                            MyStory myStory = new MyStory();
                            myStory.setImageUrl(jsonObject.getString("imageUrl"));
                            myStory.setUser_name(jsonObject.getString("user_name"));
                            myStory.setImgUrl(jsonObject.getString("imgUrl"));
                            myStory.setUser_cd(jsonObject.getString("user_cd"));
                            myStory.setStory_id(jsonObject.getString("story_id"));
                            myStory.setStory_text(jsonObject.getString("story_text"));
                            myStory.setStory_caption(jsonObject.getString("story_caption"));
                            myStory.setStory_title(jsonObject.getString("story_titel"));
                            myStory.setStory_time(jsonObject.getString("story_time"));
                            myStory.setType(jsonObject.getString("type"));
                            myStory.setThumbnail_text(jsonObject.getString("thumbnail_text"));
                            storyList.add(myStory);

                        }

                        storyAdapter = new MyStoryAdapter(context, storyList, follower_user_cd);
                        recyclerView.setAdapter(storyAdapter);
//                        mAdapter = new AutoPlayVideoAdapter(storyList, context, follower_user_cd);
//                        List<String> urls = new ArrayList<>();
//                        for (MyStory object1 : storyList) {
//                            if (object1.getStory_text() != null && object1.getStory_text().contains("http"))
//                                urls.add(object1.getStory_text());
//                        }
//
//                        recyclerView_video.preDownload(urls);
////                        recyclerView_video.playAvailableVideos(0);
//                        recyclerView_video.setAdapter(mAdapter);

                        dismiss_loading();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismiss_loading();
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
            MyDialog.iPhone(getString(R.string.something_wrong), context);
        }
    }

    private Map<String, Object> getParamMyStory() {
        Map<String, Object> params = new HashMap<>();
        if (storyGroup != null) {
            userId = storyGroup.getGroup_user_cd();
        }
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        return params;
    }

    private Map<String, Object> getParamFollowrStory() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("follower_user_cd", follower_user_cd);
        return params;
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

            edt_title = (CustomEditText) dd.findViewById(R.id.edt_title);
            edt_story = (CustomEditText) dd.findViewById(R.id.edt_story);

            ((CustomTextView) dd.findViewById(R.id.btn_Send)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validation()) {
                        if (isConnectingToInternet(context)) {
                            story_type = "1";
                            story_time = getTime();
                            callSendMyStory(getParamSendStory(null, null), Urls.SEND_STORY);
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


    public void initSelection() {
        isForCamera = false;
        isCameraVideo = false;
        isGalleryImg = false;
        isGalleryVideo = false;
    }


    @Override
    public void onClick(View v) {
        initSelection();
        switch (v.getId()) {
            case R.id.txt_story_add:
                callStoryDialog();
                break;
            case R.id.img_attachment:
                callDialogPickImgVideo();
                break;
            case R.id.imgTake_photo:
                isForCamera = true;
                setCameraPermission();

                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.imgTake_gallery:

                isGalleryImg = true;
                setGalleryPermission();
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.imgCancel:
                isGalleryVideo = true;


                setPickVideoPermission();

                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;
            case R.id.imgRemove:
                isCameraVideo = true;
                setPickCameraVideoPermission();

                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.img_add:
                lay_bottem.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.GONE);
                break;
            case R.id.imgOption:
                showMenu(imgOption);
                break;
            case R.id.imgTake_cam:
                callCameraPermission();
                break;

            default:
                break;


        }
    }

    private RunTimePermission runTimePermission;

    private void callCameraPermission() {
        runTimePermission = new RunTimePermission(this);
        runTimePermission.requestPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, new RunTimePermission.RunTimePermissionListener() {

            @Override
            public void permissionGranted() {
                // First we need to check availability of play services



            }

            @Override
            public void permissionDenied() {

                finish();
            }
        });
    }


    public void callDialogPickImgVideo() {

        dialogSelect = new Dialog(context, R.style.MaterialDialogSheet);
        dialogSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelect.setContentView(R.layout.dialog_image_video);
        dialogSelect.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogSelect.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogSelect.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
//        myView = (CustomTextView) findViewById(R.id.txtUpdate_profile);//txtUpdate_profile
        int i = Utility.getScreenWidth(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgCancel)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgRemove)).setTranslationX(-i);
//        ((View) dialogSelect.findViewById(R.id.view1)).setTranslationX(-i);
//        ((View) dialogSelect.findViewById(R.id.view2)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).animate().translationX(0).setDuration(500).setStartDelay(400);
//        ((View) dialogSelect.findViewById(R.id.view1)).animate().translationX(0).setDuration(500).setStartDelay(500);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).animate().translationX(0).setDuration(500).setStartDelay(600);
//        ((View) dialogSelect.findViewById(R.id.view2)).animate().translationX(0).setDuration(500).setStartDelay(700);
        ((ImageView) dialogSelect.findViewById(R.id.imgCancel)).animate().translationX(0).setDuration(500).setStartDelay(800);
        ((ImageView) dialogSelect.findViewById(R.id.imgRemove)).animate().translationX(0).setDuration(500).setStartDelay(1000);

        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgCancel)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgCancel)).setImageResource(R.mipmap.gallery_video_icon);
        ((ImageView) dialogSelect.findViewById(R.id.imgRemove)).setImageResource(R.mipmap.video_camera);
        ((ImageView) dialogSelect.findViewById(R.id.imgRemove)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setVisibility(View.GONE);
        ((ImageView) dialogSelect.findViewById(R.id.imgRemove)).setVisibility(View.GONE);
        dialogSelect.show();
    }


    public void TakePic() {

        try {
            isForCamera = true;
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory() + "/consult.jpg");
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

    public void GalleryVideo() {

//        if (Build.VERSION.SDK_INT < 19) {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("video/*");
//            startActivityForResult(intent, Constant.REQUEST_CODE_ALBUM_Gallery);
//        } else {
//            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, Constant.REQUEST_CODE_ALBUM_Gallery);
//        }


        Intent intent;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        }
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, Constant.REQUEST_CODE_ALBUM_Gallery);


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

    private void callSendMyStory(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    lay_bottem.setVisibility(View.GONE);
                    img_add.setVisibility(View.VISIBLE);

                    callGetMyStory(getParamMyStory(), Urls.GET_MYSTORY);
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

    public String getTime() {

        TimeZone tz = TimeZone.getDefault();
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("timeStamp " + timeStamp);
        return timeStamp;

    }

    private Map<String, Object> getParamSendStory(File file, File file2) {
        Map<String, Object> params = new HashMap<>();
        if (storyGroup != null) {
            userId = storyGroup.getGroup_user_cd();
        }
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("story_time", getTime());
        params.put("story_text", story_text);
        params.put("story_type", story_type);
        params.put("story_caption", story_caption);
        params.put("story_title", story_title);
        params.put("file", file);
        params.put("thumbnail", file2);
        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            flag_refresh = true;
            switch (requestCode) {

                case Constant.REQUEST_CODE_CAMERA:

                    CropImage.activity(mCameraImageUri)
                            .setAspectRatio(1, 1)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setFixAspectRatio(false)
                            .start(this);

                    break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {
                        mImageCaptureUri = data.getData();
                        CropImage.activity(mImageCaptureUri)
                                .setAspectRatio(1, 1)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setFixAspectRatio(false)
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

//                            makeFile(ByteArray);

                            callStoryPhotoDialog(bm, "2", null, "");


//                            file= new File(PicturePath);

//                            try {
//                                ByteArrayInputStream bis = new ByteArrayInputStream(ByteArray);
//                                ObjectInputStream ois = new ObjectInputStream(bis);
//                                file = (File) ois.readObject();

//                                patientProfile = base64String(datasecond.toByteArray());
//
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                            imgProfile.setImageBitmap(bm);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;

                case PICK_CAPTURE_VIDEO:

                    if (data != null) {

                        String path = data.getStringExtra("PATH");
                        File file = new File(
                                Environment.getExternalStorageDirectory()
                                        + File.separator
                                        ,
                                "VIDEO.mp4"
                                // "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4"
                        );

                        String selectedVideoPath = file.getPath();
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                        story_type = "3";
                        callStoryPhotoDialog(thumb, "3", file, "");


                        System.out.println("");
                        showToast(path);

                    }
                    break;

                case Constant.REQUEST_CODE_ALBUM_Gallery:


//                    try {
//                        String selectedVideoPath = getPath(data.getData());
//                        System.out.println("selectedVideoPath " + selectedVideoPath);
//                        File file = new File(selectedVideoPath);
//                        imageFile = file;
//                        if (file != null && file.length() > 0) {
//                            try {
//                                double bytes = imageFile.length();
//                                double kilobytes = (bytes / 1024);
//                                double megabytes = (kilobytes / 1024);
//                                if (megabytes <= 30) {
//                                    System.out.println("the after size " + megabytes);
//                                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
//                                    callStoryPhotoDialog(thumb, "3", imageFile);
//                                } else {
//                                    Utility.ShowSnackbarOther(context, layout, getString(R.string.video_size));
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
////                    Intent intent = new Intent(context, PostImageFileActivity.class);
////                    intent.putExtra("TYPE", "2");
////                    startActivity(intent);
//                        } else {
////                    Utility.ShowSnackbarOther(context, layout, getString(R.string.try_again));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    story_type = "3";
                    mImageCaptureUri = null;
                    mImageCaptureUri = data.getData();

                    if (mImageCaptureUri != null) {

                        Cursor cursor = context.getContentResolver().query(mImageCaptureUri, null, null, null, null, null);

                        try {
                            if (cursor != null && cursor.moveToFirst()) {

                                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                                String size = "";
                                if (!cursor.isNull(sizeIndex)) {
                                    size = cursor.getString(sizeIndex);
                                } else {
                                    size = "";
                                }

                                if (size.length() > 0 && Integer.parseInt(size) > 0) {
                                    long fileSizeInBytes = Long.parseLong(size);
                                    long fileSizeInKB = fileSizeInBytes / 1024;
                                    long fileSizeInMB = fileSizeInKB / 1024;

                                    if (fileSizeInMB <= 30) {
//                                        String mimeType = com.applozic.mobicommons.file.FileUtils.getMimeTypeByContentUriOrOther(context, mImageCaptureUri);
//                                        if (TextUtils.isEmpty(mimeType)) {
//                                            return;
//                                        }
//
//                                        String fileName = com.applozic.mobicommons.file.FileUtils.getFileName(context, mImageCaptureUri);
//                                        String fileFormat = com.applozic.mobicommons.file.FileUtils.getFileFormat(fileName);
//                                        if (TextUtils.isEmpty(fileFormat)) {
//                                            return;
//                                        } else {
//                                            String fileNameToWrite = "qalame_profile" + "." + fileFormat;
//                                            File mediaFile = FileClientService.getFilePath(fileNameToWrite, context, mimeType);
//                                            new FileTaskAsyncVideo(mediaFile, mImageCaptureUri, context).execute((Void) null);
//
////                                            String pathvidio = getPathVideo(mImageCaptureUri);
////                                            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(pathvidio, MediaStore.Images.Thumbnails.MINI_KIND);
////                                            File file = new File(pathvidio);
////                                    callStoryPhotoDialog(thumb, "3", file);
//
//                                        }
                                    } else
                                        Utility.ShowSnackbarOther(context, layout, getString(R.string.video_size));

                                } else
                                    Utility.ShowSnackbarOther(context, layout, getString(R.string.try_again));

                            }
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }
                    }

//                    if (data != null && data.getData() != null) {
//                        inputPath = data.getData().getPath();
//                        System.out.println("data path "+data);
//                        try {
//                            File file = new File(inputPath);
//                            float length = file.length() / 1024f;
//                            String value;
//                            String  mb="";
//                            if (length >= 1024) {
//                                mb = length / 1024f + " MB";
//                            } else {
//                                value = length + " KB";
//                            }
//
//
//                            if (Integer.parseInt(mb) <= 30) {
//
//
//                                if (file != null && file.length() > 0) {
//                                    try {
//                                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////use one of overloaded setDataSource() functions to set your data source
//                                        retriever.setDataSource(context, Uri.fromFile(file));
//                                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                                        long timeInMillisec = Long.parseLong(time);
//                                        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillisec);
//                                        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillisec);
////                        showToast("time of video " + minutes + ":" + seconds);
//                                        System.out.println("time of video " + minutes + ":" + seconds);
//                                        retriever.release();
//                                        if (minutes <= 5) {
//                                            double bytes = file.length();
//                                            double kilobytes = (bytes / 1024);
//                                            System.out.println("the 0000 size " + kilobytes);
//                                            String selectedVideoPath = file.getPath();
//                                            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoPath, MediaStore.Images.Thumbnails.MINI_KIND);
//                                            story_type = "3";
//                                            callStoryPhotoDialog(thumb, "3", file);
//                                        } else {
//                                            Utility.ShowSnackbarOther(context, layout, getString(R.string.video_time));
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                } else {
//                                    Utility.ShowSnackbarOther(context, layout, getString(R.string.try_again));
//                                }
//                            }
////                            System.out.println("0000file sizzzz" + value);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
                    break;

                case Constant.PICK_VIDEO_CAPTURE:

                    if (data != null) {

                        String path = data.getStringExtra("PATH");
                        String strTHUMB = data.getStringExtra("THUMB");
//                        File file = new File(
//                                path
//                                // "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4"
//                        );
//
//                        String selectedVideoPath = file.getPath();
//                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(strTHUMB, MediaStore.Images.Thumbnails.MINI_KIND);
                        story_type = "3";
//                        capture_video = "";
//                        callStoryPhotoDialog(thumb, "3", file, capture_video);

callStoryCaptureVideo(path,strTHUMB);
                        System.out.println("");
                        showToast(path);

                    }

                    break;


                default:
                    break;


            }

        }


    }



    public void printContent(File file) throws Exception {
        System.out.println("Print File Content");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
                    if (isForCamera)
                        TakePic();
                    else if (isGalleryImg)
                        Gallery();
                    else if (isCameraVideo) {
                        setPickCameraVideoPermission();
                    } else if (isGalleryVideo) {
                        GalleryVideo();
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

    public void setPickVideoPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
        }

        GalleryVideo();


    }


    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        story_type = "2";

        callSendMyStory(getParamSendStory(imageFile, null), Urls.SEND_STORY);
    }

    ImageView img_upload;

    public void callStoryPhotoDialog(final Bitmap bm, final String type, final File file, final String capture_video) {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_mystory);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            img_upload = (ImageView) dd.findViewById(R.id.img_upload);
            edt_title = (CustomEditText) dd.findViewById(R.id.edt_title);

            img_upload.setImageBitmap(bm);


            ((CustomTextView) dd.findViewById(R.id.btn_Send)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!edt_title.getText().toString().isEmpty()) {
                        if (isConnectingToInternet(context)) {
                            story_caption = edt_title.getText().toString();
                            if (type.equals("2")) {

                                persistImage(bm, "consult");
                            } else if (type.equals("3")) {
//
//      compressDone = false;
//                                originalFile = null;
//                                new VideoCompressor().execute();
//                                callAsynchronousTask();

                                if (capture_video.equals("1")) {
                                    callSendMyStory(getParamSendStory(file, callBitmaptoFile(bm, "vidfile")), Urls.SEND_STORY);
                                } else {
                                    callSendMyStory(getParamSendStory(file, callBitmaptoFile(bm, "vidfile")), Urls.SEND_STORY);
                                }


                            }


                        } else {
                            showInternetPop(context);
                        }
                        dd.dismiss();
                    } else {
                        showToast(context.getResources().getString(R.string.error_story_title));
                    }
                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }


    public void callStoryCaptureVideo(String video, String thumb) {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_mystory);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            img_upload = (ImageView) dd.findViewById(R.id.img_upload);
            edt_title = (CustomEditText) dd.findViewById(R.id.edt_title);
            img_upload.setImageURI(Uri.parse(thumb));
            final File fileVideo= new File(video);
            final File filethumb= new File(thumb);
            ((CustomTextView) dd.findViewById(R.id.btn_Send)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!edt_title.getText().toString().isEmpty()) {
                        if (isConnectingToInternet(context)) {
                            story_caption = edt_title.getText().toString();

                            callSendMyStory(getParamSendStory(fileVideo,filethumb), Urls.SEND_STORY);


                        } else {
                            showInternetPop(context);
                        }
                        dd.dismiss();
                    } else {
                        showToast(context.getResources().getString(R.string.error_story_title));
                    }
                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }
    public void callStoryCaptureImage(String video, String thumb) {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_mystory);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            img_upload = (ImageView) dd.findViewById(R.id.img_upload);
            edt_title = (CustomEditText) dd.findViewById(R.id.edt_title);
            img_upload.setImageURI(Uri.parse(thumb));
            final File fileVideo= new File(video);
            final File filethumb= new File(thumb);
            ((CustomTextView) dd.findViewById(R.id.btn_Send)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!edt_title.getText().toString().isEmpty()) {
                        if (isConnectingToInternet(context)) {
                            story_caption = edt_title.getText().toString();

                            callSendMyStory(getParamSendStory(fileVideo,filethumb), Urls.SEND_STORY);


                        } else {
                            showInternetPop(context);
                        }
                        dd.dismiss();
                    } else {
                        showToast(context.getResources().getString(R.string.error_story_title));
                    }
                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }


    public static boolean compressDone;



    public void callAsynchronousTask() {

        showLoading();
//        myLoading.show(getString(R.string.wait));
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (isInternetConnected()) {
                                if (compressDone) {

                                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(originalFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

                                    if (thumb == null)
                                        thumb = createVideoThumbnail(originalFile.getPath(), 1);

                                    if (thumb != null) {

                                        Bitmap bmp1 = thumb;
                                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                                        bmp1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                                        byte[] byteArray1 = null;
                                        byteArray1 = stream1.toByteArray();
                                        // write the bytes in file
                                        File f1 = new File(Environment.getExternalStorageDirectory() + File.separator + "qalamepriview.jpeg");

                                        if (f1.exists())
                                            f1.delete();

                                        f1.createNewFile();
                                        FileOutputStream fo1 = new FileOutputStream(f1);
                                        fo1.write(byteArray1);
                                        fo1.close();

                                        timer.cancel();
                                        dismiss_loading();

                                        if (originalFile != null) {
//                                            sendPostServer(f1,caption);
                                            showToast("video has send");
                                        } else {
                                            dismiss_loading();
                                            timer.cancel();
                                            Utility.ShowSnackbarOther(context, layout, getString(R.string.try_again));
                                        }

                                    } else {
                                        dismiss_loading();
                                        timer.cancel();
                                        Utility.ShowSnackbarOther(context, layout, getString(R.string.try_again));
                                    }

                                }
                            } else {
                                dismiss_loading();
                                timer.cancel();
                                Utility.ShowSnackbarOther(context, layout, getString(R.string.network_available));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            timer.cancel();
                            dismiss_loading();
                            Utility.ShowSnackbarOther(context, layout, getString(R.string.try_again));

                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 4000); //execute in every 50000 ms

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_create_group:
                        if (isConnectingToInternet(context)) {
                            startActivity(new Intent(context, CreateSpecialGroupActivity.class));
                        } else {
                            showInternetPop(context);
                        }
                        return true;

                    case R.id.menu_edit_profile:
                        if (isConnectingToInternet(context)) {
                            startActivity(new Intent(context, ProfileEditConsultantActivity.class));
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
        inflater.inflate(R.menu.menu_story, popup.getMenu());
        popup.show();
    }

    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) return null;

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        }
        return bitmap;
    }

    public String getPathVideo(Uri uri) {
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






    private void requestRecordingPermission() {
//        Log.i(TAG, "storage permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
//            Log.i(TAG,
//                    "Displaying camera permission rationale to provide additional context.");


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    Constant.REQUEST_AUDIO_RECORD);

        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    Constant.REQUEST_AUDIO_RECORD);
        }
        // END_INCLUDE(camera_permission_request)
    }


    public void setPickCameraVideoPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {

            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.CAMERA, "Camera")) {
                return;
            }
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.RECORD_AUDIO, "Record Audio")) {
                return;
            }

            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }

        }




//        startActivity(new Intent(this, Camera2VideoFragment.class));
//                    startActivity(new Intent(getActivity(), MyVideoJellyBeanCamera.class));
//        System.out.println("not samsung device");
//      finish();
//        File saveDir = null;
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            // Only use external storage directory if permission is granted, otherwise cache directory is used by default
//            saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
//            saveDir.mkdirs();
////                startActivity(new Intent(getActivity(), MyVideoJellyBeanCamera.class));
//        }
//        this.finish();


    }


    private File callBitmaptoFile(Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }


        return imageFile;
    }

//    String output = "/sdcard/test_compress_" + System.currentTimeMillis() + ".mp4";


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
