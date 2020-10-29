package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.app.session.adapter.SearchCategoryAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customspinner.NiceSpinner;
import com.app.session.customspinner.OnSpinnerItemSelectedListener;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.DefaultCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.model.Brief_CV;
import com.app.session.model.Category;
import com.app.session.model.CurrencyRef;
import com.app.session.model.CurrencyRefResponse;
import com.app.session.model.Language;
import com.app.session.model.ReqCategory;
import com.app.session.model.RequestUpdateCategory;
import com.app.session.model.Root;
import com.app.session.model.SubscriptionGroup;
import com.app.session.model.UserLangauges;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiClientProfile;
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

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;
import static com.app.session.utility.Utility.getTime;

public class AddSubscriptionGroupActivity extends BaseActivity implements View.OnClickListener, ServiceResultReceiver.Receiver {
    SubscriptionGroup group=null;
    NiceSpinner spinnerLanguage, spinnerCategory;
    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    private ServiceResultReceiver mServiceResultReceiver;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    String mFileName = "", imgCoverName="",videoUrl = "", coverImg = "", language_cd = "", currency_cd = "", category_cd = "",subscription_group_cd="";
    String language_id,category_id="",currency_id;
    LinearLayout layProgress;
    CircleImageView imgGroupCover;
    CustomTextView txtUploading, spinnerCurrency;
    ProgressBar progressBar;
    ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    ArrayList<Brief_CV> userLanguageArrayList = new ArrayList<>();
    ArrayList<CurrencyRef> currencyRefsList = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    CustomEditText edtGroupName, edtSubscriptionprice, edt_cv;
    Bitmap bmGroupCover = null,bmVideoCover=null;
    String videoCoverImg = "", videoCoverImgName="";
    ImageView imgVideoCover;
    ArrayList<UserLangauges> userLangaugesArrayList;
    File imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcription_group);
        userLangaugesArrayList= DataPrefrence.getLanguageDb(context, Constant.LANG_DB);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        if (args != null) {
            userLanguageArrayList = (ArrayList<Brief_CV>) args.getSerializable("List");
            language_cd = userLanguageArrayList.get(0).getLanguage_cd();
        }
        language_cd=userLangaugesArrayList.get(0).getCode();
        language_id=userLangaugesArrayList.get(0).get_id();
        initView();
        callGetCategory();
    }


    private void initView() {
        imgVideoCover=(ImageView)findViewById(R.id.imgVideoCover);
        edtGroupName = (CustomEditText) findViewById(R.id.edtGroupName);
        edt_cv = (CustomEditText) findViewById(R.id.edt_cv);
        edtSubscriptionprice = (CustomEditText) findViewById(R.id.edtSubscriptionprice);
        spinnerCategory = (NiceSpinner) findViewById(R.id.spinnerCategory);
        spinnerCurrency = (CustomTextView) findViewById(R.id.spinnerCurrency);
        spinnerCurrency.setOnClickListener(this);
        spinnerLanguage = (NiceSpinner) findViewById(R.id.spinnerLanguage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtUploading = (CustomTextView) findViewById(R.id.txtUploading);
        imgGroupCover = (CircleImageView) findViewById(R.id.imgGroupCover);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        ((CustomTextView) findViewById(R.id.txtSave)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.laySelectVideo)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.laySelectImgCover)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgCross)).setOnClickListener(this);
        spinnerLanguage.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id)
            {
                UserLangauges userLanguage = (UserLangauges) spinnerLanguage.getSelectedItem();
                language_cd = userLanguage.getCode();
                language_id=userLanguage.get_id();
                callGetCategory();

            }
        });
        spinnerLanguage.attachDataSource(userLangaugesArrayList, "1");

        if (getIntent().getParcelableExtra("DATA") != null)
        {

             group=getIntent().getParcelableExtra("DATA");
            subscription_group_cd=group.getSubscription_group_cd();
             edt_cv.setText(group.getGroup_desc());
             edtGroupName.setText(group.getGroup_name());
             edtSubscriptionprice.setText(""+group.getSubscription_price());
            if (group.getGroup_url_image_address() != null && !group.getGroup_url_image_address().isEmpty()) {

                //Picasso.with(context).load(Urls.BASE_IMAGES_URL +group.getGroup_url_image_address()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(holder.imgGroup);
                //Picasso.with(context).load(Urls.BASE_IMAGES_URL +group.getGroup_url_image_address()).placeholder(R.mipmap.profile_large).into(imgGroupCover);
                Picasso.with(context).load(Urls.BASE_IMAGES_URL +group.getGroup_url_image_address()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(imgGroupCover);
            }

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
                    sendSubscriptionStory(null);
                }


                break;
            case R.id.spinnerCurrency:
                Intent intent = new Intent(context, SearchCurrencyActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CURRENCY);
                break;
            case R.id.laySelectVideo:
                if(!subscription_group_cd.isEmpty())
                {
                    callVideoFrameThumb();
                }
                else {
                    showToast("First you have to fill all detail.");
                }
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



    private void callService(String path) {
        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setReceiver(this);
        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", path);
        mIntent.putExtra("FileName", mFileName);
        mIntent.putExtra(RECEIVER, mServiceResultReceiver);
        mIntent.setAction(ACTION_DOWNLOAD);
        FileUploadService.enqueueWork(this, mIntent);

    }

    private void callUploadVideoUpdate() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPLOAD_GROUP_VIDEO , getParamUpdateVideo(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {

                        System.out.println("video upload result");
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1")) {
                            callUploadVideoCoverImage(videoCoverImg,videoCoverImgName);
                            if (jsonObject.getString("video_url") != null && !jsonObject.getString("video_url").isEmpty())
                            {
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
            System.out.println("Video parameter "+jsonObject.toString());
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

//        if (value == 100) {
//            progressBar.setVisibility(View.GONE);
//
//        }

    }


    private void callGetCurrencyRef() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CurrencyRefResponse> call = apiInterface.GetCurrencyRef();

            call.enqueue(new Callback<CurrencyRefResponse>() {
                @Override
                public void onResponse(Call<CurrencyRefResponse> call, Response<CurrencyRefResponse> response) {
                    dismiss_loading();
                    CurrencyRefResponse responseBody = response.body();
                    currencyRefsList = responseBody.getCurrencyRefsList();
//                    spinnerCurrency.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
//                            CurrencyRef currencyRef = (CurrencyRef) spinnerCurrency.getSelectedItem();
//
//                        }
//                    });
//                    spinnerCurrency.attachDataSource(currencyRefsList, "1");

                }

                @Override
                public void onFailure(Call<CurrencyRefResponse> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }


    private void callGetCategory0() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getCategory(language_cd);
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

                                try {
                                    categoryArrayList = new ArrayList<>();
                                    JSONArray jsonArray = js.getJSONArray("Category");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Category category = null;
                                        category = new Category();
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        category.setCategoryID(jsonObject.getString("category_cd"));
                                        category.setCategoryName(jsonObject.getString("categoryName"));
                                        if (i == 0) {
                                            category_cd = category.getCategoryID();
                                        }
                                        categoryArrayList.add(category);
                                    }


//                                    Category category1 = new Category();
//                                    category1.setCategoryID("");
//                                    category1.setCategoryName("Add New Category");
//                                    categoryArrayList.add(category1);


                                    spinnerCategory.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
//                                            Category category = (Category) spinnerCategory.getSelectedItem();
//                                            category_cd = category.getCategoryID();

                                        }
                                    });
                                    spinnerCategory.attachDataSource(categoryArrayList, "1");


//
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


    private void callGetCategory() {
        if (isConnectingToInternet(context)) {
//            showLoading();
            ReqCategory reqCategory=new ReqCategory();
            ArrayList<String> list=new ArrayList<>();
            for(int i =0;i<userLangaugesArrayList.size();i++)
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

                            if (js.getInt("status")==200)
                            {
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
                                        public void onItemSelected(NiceSpinner parent, View view, int position, long id)
                                        {

                                            Category category = (Category) spinnerCategory.getSelectedItem();

                                            if(category.getCategoryName().equals("Add New Category"))
                                            {
                                                Intent intent=new Intent(context, SearchLanguageCategoryActivity.class);
                                                intent.putExtra("TYPE","2");
                                                intent.putExtra("LANG_CD",language_cd);
                                                intent.putExtra("LANG",userLangaugesArrayList);
                                                startActivityForResult(intent, Constant.PICK_CATEGORY);
                                            }
                                            else
                                            {
                                                category_cd = category.getCategoryID();

                                            }




                                        }
                                    });
                                    spinnerCategory.attachDataSource(categoryArrayList, "1");



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
    private JsonObject getCatgParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            jsonArray.put("en");
            jsonObject.put("language_cd",jsonArray);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }











    String groupName = "", subscriptionPrice = "", description = "";

    private boolean validation() {
        groupName = edtGroupName.getText().toString();
        subscriptionPrice = edtSubscriptionprice.getText().toString();
        description = edt_cv.getText().toString();

        if (groupName.isEmpty()) {
            edtGroupName.setError(context.getResources().getString(R.string.error));
            return false;
        }
        if (subscriptionPrice.isEmpty()) {
            edtSubscriptionprice.setError(context.getResources().getString(R.string.error));
            return false;
        }
        if (description.isEmpty()) {
            edt_cv.setError(context.getResources().getString(R.string.error));
            return false;
        }
        return true;
    }



    public void sendSubscriptionStory(File file)
    {
        if (isInternetConnected()) {

            showLoading();
            RequestBody usercd=RequestBody.create( MediaType.parse("text/plain"),userId);
            RequestBody languageId=RequestBody.create( MediaType.parse("text/plain"),language_id);
            RequestBody categoryId=RequestBody.create( MediaType.parse("text/plain"),category_id);
            RequestBody currencyId=RequestBody.create( MediaType.parse("text/plain"),currency_id);
            RequestBody subscription_price=RequestBody.create( MediaType.parse("text/plain"),subscriptionPrice);
            RequestBody group_name=RequestBody.create( MediaType.parse("text/plain"),groupName);
            RequestBody group_description=RequestBody.create( MediaType.parse("text/plain"),description);
            RequestBody requestfile=null;
            MultipartBody.Part productimg= null;

            if (imageFile !=null) {
                requestfile=RequestBody.create(MediaType.parse("image/jpeg"),imageFile);
                productimg = MultipartBody.Part.createFormData("image",imageFile.getName(),requestfile);
            }

            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            //,description,coverImg,videoUrl
            Call<ResponseBody> call = apiInterface.reqAddSubscription(accessToken,usercd,languageId,categoryId,currencyId,subscription_price,group_description,group_name,productimg);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        try {
                            ResponseBody body=response.body();
                            String data =body.string();
                            JSONObject object=new JSONObject(data);
                            if(object.getInt("status")==200)
                            {
                                finish();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {

                        errorBody(response,false);

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
                            imageFile=null;
                            imageFile=Utility.getFileByBitmap(context,bm,"subscription");

                            bmGroupCover = bm;

                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                            ByteArray = datasecond.toByteArray();
                            String coverImg = base64String(ByteArray);
                            imgGroupCover.setImageBitmap(bmGroupCover);


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

                long location = data.getLongExtra(ThumbyActivity.EXTRA_THUMBNAIL_POSITION, 0);
                Uri uri = data.getParcelableExtra(ThumbyActivity.EXTRA_URI);
                Bitmap bitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);
                bmVideoCover=bitmap;
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                byte[] ByteArray = datasecond.toByteArray();
                videoCoverImg = base64String(ByteArray);
                String selectedVideoPath = data.getStringExtra(ThumbyActivity.VIDEO_PATH);


                mFileName = subscription_group_cd + "_group_video_url"+".mp4";
                videoCoverImgName= subscription_group_cd +"_groupname_thumbnail.png";
                   callService(selectedVideoPath);
            }

        }
        if (requestCode == Constant.REQUEST_CURRENCY) {
            if (data != null) {

                String currencyName = data.getStringExtra("result");
                currency_id = data.getStringExtra("id");
                spinnerCurrency.setText(currencyName);

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
                        System.out.println("image requ: "+js.toString());
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

            System.out.println("parameterImage"+jsonObject.toString());
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
                        System.out.println("image requ final: "+js.toString());
                        if (jsonObject.getString("rstatus").equals("1"))
                        {
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
            System.out.println("getParamUplodVideoCoverImageFinal : "+jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void persistImage(Bitmap bitmap, String name) {
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


    private void callUpdateCategory(String categoryId)
    {
        if (isConnectingToInternet(context)) {
            showLoading();


            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            RequestUpdateCategory requestUpdateCategory=new RequestUpdateCategory();
            requestUpdateCategory.setUserId(userId);
            requestUpdateCategory.setUserCategory(categoryId);
            Call<Root> call=apiInterface.reqUpateUserCategory(accessToken,requestUpdateCategory);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {


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




}
