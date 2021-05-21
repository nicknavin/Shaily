package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.app.session.R;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.activity.viewmodel.EditSubscriptionGroupViewModel;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customspinner.NiceSpinner;
import com.app.session.customspinner.OnSpinnerItemSelectedListener;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.AddSubscription;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.Category;
import com.app.session.data.model.CategoryId;
import com.app.session.data.model.CurrencyBody;
import com.app.session.data.model.CurrencyRef;
import com.app.session.data.model.ReqCategory;
import com.app.session.data.model.RequestUpdateCategory;
import com.app.session.data.model.Root;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.UpdateSubscriptionGroupDetail;
import com.app.session.data.model.UserLangauges;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.databinding.ActivitySubcriptionEditGroupBinding;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.service.FileUploadService;
import com.app.session.thumby.MainActivitythumby;
import com.app.session.thumby.ThumbyActivity;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
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

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;
import static com.app.session.utility.Utility.isConnectingToInternet;

public class EditSubscriptionGroupDetailActivity extends BaseActivity implements View.OnClickListener, ServiceResultReceiver.Receiver {

    NiceSpinner spinnerLanguage, spinnerCategory;
    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    private ServiceResultReceiver mServiceResultReceiver;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    String mFileName = "", subscriptionPrice = "", description = "", groupName = "", videoUrl = "", coverImg = "", language_cd = "", currency_cd = "", category_cd = "", subscription_group_cd = "";
    String language_id, category_id = "", currency_id;
    LinearLayout layProgress;
    CircleImageView imgGroupCover;
    CustomTextView txtUploading;
    ProgressBar progressBar;
    ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    ArrayList<Brief_CV> userLanguageArrayList = new ArrayList<>();
    ArrayList<CurrencyRef> currencyRefsList = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();

    Bitmap bmGroupCover = null, bmVideoCover = null;
    String videoCoverImg = "", videoCoverImgName = "";
    ImageView imgVideoCover;
    ArrayList<UserLangauges> userLangaugesArrayList;
    File imageFile;
    CustomTextView txtSave;
    UserSubscriptionGroupsBody subscriptionGroupsBody;

    EditSubscriptionGroupViewModel viewModel;
    ActivitySubcriptionEditGroupBinding binding;
    String fileType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subcription_edit_group);
        setupViewModel();
        userLangaugesArrayList = DataPrefrence.getLanguageDb(context, Constant.LANG_DB);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        if (args != null) {
            userLanguageArrayList = (ArrayList<Brief_CV>) args.getSerializable("List");
        }

        initView();
        getGroupData();
        callGetCategory();

        setupObserver();

    }

    private void initView() {
        imgVideoCover = (ImageView) findViewById(R.id.imgVideoCover);
        spinnerCategory = (NiceSpinner) findViewById(R.id.spinnerCategory);
        binding.spinnerCurrency.setOnClickListener(this);
        spinnerLanguage = (NiceSpinner) findViewById(R.id.spinnerLanguage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtUploading = (CustomTextView) findViewById(R.id.txtUploading);
        imgGroupCover = (CircleImageView) findViewById(R.id.imgGroupCover);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        txtSave = (CustomTextView) findViewById(R.id.txtSave);
        txtSave.setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.laySelectVideo)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.laySelectImgCover)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgCross)).setOnClickListener(this);
        spinnerLanguage.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                UserLangauges userLanguage = (UserLangauges) spinnerLanguage.getSelectedItem();
                subscriptionGroupsBody.setLanguage_id(userLanguage);
                viewModel.getUserSubscriptionGroups().setValue(subscriptionGroupsBody);
                callGetCategory();

            }
        });
        spinnerLanguage.attachDataSource(userLangaugesArrayList, "1");


    }


    private void setupViewModel() {
        viewModel = new ViewModelProvider(this, new ViewModelFactory(userId,accessToken)).get(EditSubscriptionGroupViewModel.class);
        binding.setGroup(viewModel);
        binding.setLifecycleOwner(this);


    }

    private void getGroupData() {

        if (getIntent().getParcelableExtra(Constant.SUBS_GROUP) != null) {
            subscriptionGroupsBody = getIntent().getParcelableExtra(Constant.SUBS_GROUP);
            subscription_group_cd = subscriptionGroupsBody.get_id();
            if (subscriptionGroupsBody.getGroup_image_url() != null && !subscriptionGroupsBody.getGroup_image_url().isEmpty()) {
                Picasso.with(context).load(Urls.BASE_IMAGES_URL + subscriptionGroupsBody.getGroup_image_url()).placeholder(R.mipmap.profile_large).into(binding.imgGroupCover);
            }
            if (subscriptionGroupsBody.getGroup_video_thumbnail_url() != null && !subscriptionGroupsBody.getGroup_video_thumbnail_url().isEmpty())
            {
                Picasso.with(context).load(Urls.BASE_IMAGES_URL + subscriptionGroupsBody.getGroup_video_thumbnail_url()).placeholder(R.mipmap.profile_large).into(binding.imgVideoCover);
            }

            binding.laySelectVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(subscriptionGroupsBody.getGroup_introduction_video_url()!=null&&!subscriptionGroupsBody.getGroup_introduction_video_url().isEmpty())
                    {
                        String videoUrl = Urls.BASE_IMAGES_URL +subscriptionGroupsBody.getGroup_introduction_video_url();
                        Intent intent = new Intent(context, PlayerActivity.class);
                        intent.putExtra("URL", videoUrl);
                        startActivity(intent);
                    }
                }
            });


            UserLangauges userLangauges = subscriptionGroupsBody.getLanguage_id();
            int index = userLangaugesArrayList.indexOf(userLangauges);
            log("index " + index);
            binding.spinnerLanguage.setSelectedIndex(index);
            viewModel.getUserSubscriptionGroups().setValue(subscriptionGroupsBody);

        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgCross:
                finish();
                break;
            case R.id.txtSave:
                if (validation()) {
                    sendSubscriptionStory();
                }


                break;
            case R.id.spinnerCurrency:
                Intent intent = new Intent(context, SearchCurrencyActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CURRENCY);
                break;
            case R.id.laySelectVideo:

                callVideoFrameThumb();

                break;
            case R.id.laySelectImgCover:
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
        }
    }

    public void callVideoFrameThumb() {
        Intent intent = new Intent(context, MainActivitythumby.class);
        startActivityForResult(intent, Constant.PICK_VIDEO_THUMB);

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


    private void callService() {

        if (isConnectingToInternet(context)) {
            mServiceResultReceiver = new ServiceResultReceiver(new Handler());
            mServiceResultReceiver.setReceiver(this);
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", selectedVideoPath);
            mIntent.putExtra("FileName", "");
            mIntent.putExtra("VIDEO_THUMB", videoThumbBitmap);//this for image
            mIntent.putExtra("TYPE", Constant.UPDATE_GROUP_IMAGE);
            mIntent.putExtra("USER_ID", userId);
            mIntent.putExtra("TOKEN", accessToken);
            mIntent.putExtra(Constant.SUBSCN_GROUP_CD, subscription_group_cd);
            mIntent.putExtra("LANGUAGE_ID", language_cd);
            mIntent.putExtra(RECEIVER, mServiceResultReceiver);
            mIntent.setAction(ACTION_DOWNLOAD);
            FileUploadService.enqueueWork(context, mIntent);
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

                        System.out.println("video upload result");
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1")) {
                            callUploadVideoCoverImage(videoCoverImg, videoCoverImgName);
                            if (jsonObject.getString("video_url") != null && !jsonObject.getString("video_url").isEmpty()) {
                                videoUrl = jsonObject.getString("video_url");


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
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(143);
                finish();
                //callUploadVideoUpdate();
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

//        if (value == 100) {
//            progressBar.setVisibility(View.GONE);
//
//        }

    }


    private void callGetCategory() {
        if (isConnectingToInternet(context)) {
//            showLoading();
            ReqCategory reqCategory = new ReqCategory();
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < userLangaugesArrayList.size(); i++)
            {
                list.add(userLangaugesArrayList.get(i).getCode());
            }
            reqCategory.setLanguageCd(list);
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.reqGetCategories(reqCategory);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getInt("status") == 200) {
                                try {
                                    categoryArrayList = new ArrayList<>();
                                    JSONArray jsonArray = js.getJSONArray("body");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Category category = null;
                                        category = new Category();

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        category.setCategoryID(jsonObject.getString("_id"));
                                        category.setCategoryName(jsonObject.getString("category_name"));
                                        category.setLanguageCd(jsonObject.getString("language_cd"));
                                        categoryArrayList.add(category);

                                    }

                                    Category category1 = new Category();
                                    category1.setCategoryID("");
                                    category1.setCategoryName("Add New Category");
                                    categoryArrayList.add(category1);





                                    spinnerCategory.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(NiceSpinner parent, View view, int position, long id) {

                                            Category category = (Category) spinnerCategory.getSelectedItem();

                                            if (category.getCategoryName().equals("Add New Category")) {
                                                Intent intent = new Intent(context, SearchLanguageCategoryActivity.class);
                                                intent.putExtra("TYPE", "2");
                                                intent.putExtra("LANG_CD", language_cd);
                                                intent.putExtra("LANG", userLangaugesArrayList);
                                                startActivityForResult(intent, Constant.PICK_CATEGORY);
                                            } else {
                                                CategoryId categoryId = new CategoryId();
                                                categoryId.set_id(category.getCategoryID());
                                                categoryId.setLanguage_cd(category.getLanguageCd());
                                                categoryId.setCategory_name(category.getCategoryName());
                                                subscriptionGroupsBody.setCategory_id(categoryId);
                                                viewModel.getUserSubscriptionGroups().setValue(subscriptionGroupsBody);

                                            }


                                        }
                                    });
                                    spinnerCategory.attachDataSource(categoryArrayList, "1");

                                    if(categoryArrayList.size()>0)
                                    {
                                        CategoryId categoryId=subscriptionGroupsBody.getCategory_id();
                                        Category category=new Category();
                                        category.setCategoryID(categoryId.get_id());
                                        category.setCategoryName(categoryId.getCategory_name());
                                        category.setLanguageCd(categoryId.getLanguage_cd());
                                        int indexCat= categoryArrayList.indexOf(category);
                                        binding.spinnerCategory.setSelectedIndex(indexCat);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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


    private boolean validation() {

        UserSubscriptionGroupsBody groupsBody = viewModel.getUserSubscriptionGroups().getValue();


        if (groupsBody.getGroup_name().isEmpty()) {
            binding.edtGroupName.setError(context.getResources().getString(R.string.error));
            return false;
        }
        if (groupsBody.getSubscription_price().isEmpty()) {
            binding.edtSubscriptionprice.setError(context.getResources().getString(R.string.error));
            return false;
        }
        if (groupsBody.getGroup_description().isEmpty()) {
            binding.edtCv.setError(context.getResources().getString(R.string.error));
            return false;
        }
//        if (videoThumbBitmap == null) {
//            showToast("Please select cover image");
//            return false;
//        }
        return true;
    }


    public void sendSubscriptionStory() {

        UserSubscriptionGroupsBody body = viewModel.getUserSubscriptionGroups().getValue();
        UpdateSubscriptionGroupDetail groupDetail = new UpdateSubscriptionGroupDetail();
        groupDetail.setSubscription_id(subscription_group_cd);
        groupDetail.setCategory_id(body.getCategory_id().get_id());
        groupDetail.setCurrency_id(body.getCurrency_id().get_id());
        groupDetail.setLanguage_id(body.getLanguage_id().get_id());
        groupDetail.setGroup_name(body.getGroup_name());
        groupDetail.setGroup_description(body.getGroup_description());
        groupDetail.setSubscription_price(body.getSubscription_price());
        viewModel.updateSubscriptionGroupDetail(groupDetail);
        // txtSave.setClickable(false);

    }


    Bitmap videoThumbBitmap = null;
    String selectedVideoPath = "";

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
//                        String PicturePath="";
                        mImageCaptureUri = data.getData();
//                        Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageCaptureUri);
//                        bm = ConvetBitmap.Mytransform(bm);
//                        bm = Utility.rotateImage(bm, new File(mImageCaptureUri.getPath()));
//                        imgBriefCV.setImageBitmap(bm);
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
                            videoThumbBitmap = bm;
                            imageFile = null;
                            imageFile = Utility.getFileByBitmap(context, bm, "subscription");

                            bmGroupCover = bm;

                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                            ByteArray = datasecond.toByteArray();


                            imgGroupCover.setImageBitmap(bmGroupCover);
                            fileType=Constant.GROUP_IMAGE;
                            callService();

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
             //   callService(selectedVideoPath);
                Log.d("path", selectedVideoPath);

            }
        }
        if (requestCode == Constant.PICK_VIDEO_THUMB) {

            if (data != null) {

                long location = data.getLongExtra(ThumbyActivity.EXTRA_THUMBNAIL_POSITION, 0);
                Uri uri = data.getParcelableExtra(ThumbyActivity.EXTRA_URI);
                Bitmap bitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);
                bmVideoCover = bitmap;
                videoThumbBitmap = bitmap;
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                byte[] ByteArray = datasecond.toByteArray();
                videoCoverImg = base64String(ByteArray);
                selectedVideoPath = data.getStringExtra(ThumbyActivity.VIDEO_PATH);
                imgVideoCover.setImageBitmap(bmVideoCover);
                fileType=Constant.GROUP_VIDEO;
                callService();
            }

        }
        if (requestCode == Constant.REQUEST_CURRENCY) {
            if (data != null) {

                String name = data.getStringExtra("result");
                String id = data.getStringExtra("id");
                String cd = data.getStringExtra("cd");
                CurrencyBody currency = new CurrencyBody();
                currency.setCurrency_cd(cd);
                currency.setCurrency_name(name);
                currency.set_id(id);

                subscriptionGroupsBody.setCurrency_id(currency);
                viewModel.getUserSubscriptionGroups().setValue(subscriptionGroupsBody);


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


                if (isConnectingToInternet(context)) {
                    //callConsultant_Specialties(getParamUpdateSpecialites(categoryId), Urls.SET_CONSULTANT_SPECIALTIES);
                    callUpdateCategory(categoryId);
                } else {
                    showInternetPop(context);
                }


            }
        }


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
                        imgVideoCover.setImageBitmap(bmVideoCover);
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("image requ final: " + js.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {
                            finish();

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




    private void callUpdateCategory(String categoryId) {
        if (isConnectingToInternet(context)) {
            showLoading();


            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            RequestUpdateCategory requestUpdateCategory = new RequestUpdateCategory();
            requestUpdateCategory.setUserId(userId);
            requestUpdateCategory.setUserCategory(categoryId);
            Call<Root> call = apiInterface.reqUpateUserCategory(accessToken, requestUpdateCategory);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    dismiss_loading();
                    if (response.body() != null) {


                        callGetCategory();
                    }

                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    dismiss_loading();
                }
            });
//            Call<ResponseBody> call = apiInterface.callUpdateCategory(userId, accessToken,categoryId,country_cd);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    dismiss_loading();
//                    ResponseBody responseBody = response.body();
//                    try {
//                        String data = responseBody.string();
//                        try {
//
//                            JSONObject js = new JSONObject(data);
//
//                            if (js.getBoolean("Status")) {
//
//                                getUserDetail();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dismiss_loading();
//                }
//            });
        } else {
            showInternetConnectionToast();
        }
    }


    private void setupObserver() {
        viewModel.getUserSubscriptionGroups().observe(this, new Observer<UserSubscriptionGroupsBody>() {
            @Override
            public void onChanged(UserSubscriptionGroupsBody userSubscriptionGroupsBody) {

            }
        });
        viewModel.getRootMutableLiveData().observe(this, new Observer<Root>() {
            @Override
            public void onChanged(Root root) {
                UserSubscriptionGroupsBody groupsBody= viewModel.getUserSubscriptionGroups().getValue();
                Intent intent=new Intent();
                intent.putExtra(Constant.SUBS_GROUP,groupsBody);
                setResult(Constant.GROUP_UPDATED,intent);
                finish();
            }
        });

        viewModel.getUserSubscriptionGroups().observe(this, new Observer<UserSubscriptionGroupsBody>() {
            @Override
            public void onChanged(UserSubscriptionGroupsBody group) {
                mlog(group.toString());

            }
        });


    }

}
