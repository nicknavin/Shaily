package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.adapter.TitleAddAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.TitleRef;
import com.app.session.data.model.UpdateBriefReq;
import com.app.session.data.model.User;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserLangauges;
import com.app.session.data.model.UserRoot;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.service.CountingRequestBody;
import com.app.session.service.FileUploadService;
import com.app.session.thumby.MainActivitythumby;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.app.session.utils.MIMEType;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.FlowableEmitter;
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
import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;
import static com.app.session.utility.Utility.isConnectingToInternet;

public class AddBriefCVActivity extends BaseActivity implements View.OnClickListener, ServiceResultReceiver.Receiver {
    CustomEditText edt_brief;
    CustomTextView txtUsercv, txtUserName_dilog, txtCvName, btn_update, txtSelectImagCover, txtSelectVideo, txtUploading;
    CircleImageView imgProfile_di;
    Context context;
    String imageUrl = "", language_cd = "",language_id = "", language_name = "";
    int position = 0;
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    private ArrayList<Brief_CV> langList = new ArrayList<>();
    ArrayList<Brief_CV> languageArrayList = new ArrayList<>();
    ImageView img_flag;
    Brief_CV brief_cv_param;
    LinearLayout layUploadVideo;

    LinearLayout layVideothumb;
    private ServiceResultReceiver mServiceResultReceiver;
    String mFileName = "";
    LinearLayout layPlayVideo, layImgVideoSelection;
    ImageView imgVideoThumb;
    Bitmap videoThumb, imgThumb;
    ImageView imgVideoCover;
    String videoUrl = "", PicturePath = "";
    CheckBox checkDropSelection;
    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    CustomEditText edit_specify_title;
    ArrayList<TitleRef> titleRefsList = new ArrayList<>();
    String title_cd = "0", titleName = "";
    LinearLayout layProgress;
    ProgressBar progressBar;
    Brief_CV brief_cv;
    /* A/c 100048577176
     IFSC Code
      INDB0000878
     Branch Name :- VIJAY NAGAR INDORE
     Bank name : Indusind bank*/

    UpdateBriefReq updateBriefReq;
    String videoPath="";
    File imageFile =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brief_cv);
        context = this;


        position = getIntent().getIntExtra("POSITION", 0);
        if (getIntent().getParcelableExtra("DATA") != null) {
            brief_cv = getIntent().getParcelableExtra("DATA");
        }

        initView();
        updateView(brief_cv);

    }

    public void initView() {
        txtUploading = (CustomTextView) findViewById(R.id.txtUploading);
        edit_specify_title = (CustomEditText) findViewById(R.id.edit_specify_title);
        checkDropSelection = (CheckBox) findViewById(R.id.checkDropSelection);
        checkDropSelection.setOnClickListener(this);
        imgVideoCover = (ImageView) findViewById(R.id.imgVideoCover);
        imgVideoCover.setOnClickListener(this);
        layPlayVideo = (LinearLayout) findViewById(R.id.layPlayVideo);
        layImgVideoSelection = (LinearLayout) findViewById(R.id.layImgVideoSelection);
        layVideothumb = (LinearLayout) findViewById(R.id.layVideothumb);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        img_flag = (ImageView) findViewById(R.id.img_flag);
        imgVideoThumb = (ImageView) findViewById(R.id.imgVideoThumb);
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.btn_update)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.btn_cancel)).setOnClickListener(this);
        layUploadVideo = (LinearLayout) findViewById(R.id.layUploadVideo);
        layUploadVideo.setOnClickListener(this);

        edt_brief = (CustomEditText) findViewById(R.id.edt_cv);
        txtUserName_dilog = (CustomTextView) findViewById(R.id.txtUserName);
        txtUsercv = (CustomTextView) findViewById(R.id.txtUsercv);
        txtCvName = (CustomTextView) findViewById(R.id.txtCvName);
        txtSelectVideo = (CustomTextView) findViewById(R.id.txtSelectVideo);
        txtSelectVideo.setOnClickListener(this);
        txtSelectImagCover = (CustomTextView) findViewById(R.id.txtSelectImagCover);
        txtSelectImagCover.setOnClickListener(this);
        imgProfile_di = (CircleImageView) findViewById(R.id.imgProfile);
        edt_brief.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtUsercv.setText("" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        if (isConnectingToInternet(context)) {
//            getUserDetail();
//        }
    }

    public void updateView(Brief_CV brief_cv)
    {
        if (brief_cv != null)
        {
            language_id=brief_cv.getLanguage_id().get_id();

            language_cd = brief_cv.getLanguage_id().getCode();
            mFileName = language_cd + "introduction.mp4";

            edt_brief.setText(brief_cv.getBrief_cv());
            edt_brief.setHint(context.getResources().getString(R.string.brief_cv_txt) +brief_cv.getLanguage_id().getName());
            edt_brief.setSelection(brief_cv.getBrief_cv().length());
            txtUsercv.setText(brief_cv.getBrief_cv());
            txtCvName.setText("Bio " + brief_cv.getLanguage_id().getName());
            edit_specify_title.setText(brief_cv.getTitle_name());
            if (brief_cv.getVideo_thumbnail() != null && !brief_cv.getVideo_thumbnail().isEmpty()) {
                String url = Urls.BASE_IMAGES_URL + brief_cv.getVideo_thumbnail();
                Picasso.with(context).load(url).placeholder(R.mipmap.profile_large).into(imgVideoCover);
            }
            if (brief_cv.getVideo_url() != null && !brief_cv.getVideo_url().isEmpty())
            {

                videoUrl = Urls.BASE_IMAGES_URL + brief_cv.getVideo_url();
                layVideothumb.setVisibility(View.VISIBLE);
                layUploadVideo.setVisibility(View.GONE);

            } else {
                layVideothumb.setVisibility(View.GONE);
                layUploadVideo.setVisibility(View.VISIBLE);
            }

            //getTitleRef();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private int progressStatus = 0;

    public void initProgress(int value) {
        txtUploading.setText("" + value + "%" + "uploaded...Please wait");
        layProgress.setVisibility(View.VISIBLE);
        progressBar.setProgress(value);


    }









    public void setBriefCV(final ArrayList<Brief_CV> list) {
        String lang_id = "", check_lang_id = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Brief_CV brief_cv = list.get(i);
                lang_id = brief_cv.getLanguage_id().getCode();
                for (int j = 0; j < langList.size(); j++) {
                    Brief_CV cv = langList.get(j);
                    check_lang_id = cv.getLanguage_cd();
                    if (check_lang_id.equals(lang_id)) {
                        langList.remove(cv);
                    }
                }

            }
        }


        for (int j = 0; j < langList.size(); j++) {
            Brief_CV brief_cv = langList.get(j);
            list.add(brief_cv);
        }
        brief_cv_param = list.get(position);


        if (brief_cv_param.getBrief_cv() != null) {


            language_cd = brief_cv_param.getLanguage_cd();
            mFileName = language_cd + "introduction.mp4";

            edt_brief.setText(brief_cv_param.getBrief_cv());
            edt_brief.setHint(context.getResources().getString(R.string.brief_cv_txt) + brief_cv_param.getNative_name());
            edt_brief.setSelection(brief_cv_param.getBrief_cv().length());
            txtUsercv.setText(brief_cv_param.getBrief_cv());
            txtCvName.setText("Bio " + brief_cv_param.getNative_name());
            edit_specify_title.setText(brief_cv_param.getTitle_name());
            if (brief_cv_param.getVideo_thumbnail() != null && !brief_cv_param.getVideo_thumbnail().isEmpty()) {
                String url = Urls.BASE_IMAGES_URL + brief_cv_param.getVideo_thumbnail();
                Picasso.with(context).load(url).placeholder(R.mipmap.profile_large).into(imgVideoCover);
            }
            if (brief_cv_param.getVideo_url() != null && !brief_cv_param.getVideo_url().isEmpty()) {

                videoUrl = Urls.BASE_IMAGES_URL + brief_cv_param.getVideo_url();
                layVideothumb.setVisibility(View.VISIBLE);
                layUploadVideo.setVisibility(View.GONE);

            } else {
                layVideothumb.setVisibility(View.GONE);
                layUploadVideo.setVisibility(View.VISIBLE);
            }

            //getTitleRef();
        }

    }





    private void callUpdateBriefCv()
    {
        if (isConnectingToInternet(context))
        {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.reqUpdateBriefCv(accessToken,updateBriefReq);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    if(response.body()!=null)
                    {

                        try {
                            ResponseBody root = response.body();
                            String data =root.string();
                            JSONObject object=new JSONObject(data);
                            if(object.getInt("status")==200)
                            {

                                    Intent intent = new Intent();
                                    setResult(Constant.PAGE_REFRESH, intent);
                                    finish();


                            }

                            System.out.println("ds");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else
                    {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismiss_loading();
                }
            });
        }
        else
        {
            showInternetConnectionToast();
        }
    }



    private void callUpdateBriefCvImage()
    {
        if (isConnectingToInternet(context))
        {
showLoading();
            RequestBody usercd=RequestBody.create( MediaType.parse("text/plain"),userId);
            RequestBody languageId=RequestBody.create( MediaType.parse("text/plain"),language_id);
            MultipartBody.Part productimg= null;
            RequestBody requestfile=null;
            if (imageFile !=null) {
                requestfile=RequestBody.create(MediaType.parse("image/jpeg"),imageFile);
                productimg = MultipartBody.Part.createFormData("image",imageFile.getName(),requestfile);
            }

            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.reqUpdateBriefThumbnail(accessToken,usercd,languageId,productimg);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        errorBody(response,true);
                        try {
                            ResponseBody responseBody=null;

                            responseBody = response.errorBody();

                            if(responseBody!=null) {
                                String data = responseBody.string();

                                System.out.println("error1" + data);
                                JSONObject jsonObject = new JSONObject(data);

                                if (jsonObject.getInt("status") == 200) {
                                    showToast(jsonObject.getString("message"));
                                }
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

                }
            });

        }
        else
        {
            showInternetConnectionToast();
        }
    }

    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);

        return MultipartBody.Part.createFormData("image", mFileName, createCountingRequestBody(file, MIMEType.IMAGE.value, emitter));
    }
    private RequestBody createCountingRequestBody(File file, String mimeType, final FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                double progress = (1.0 * bytesWritten) / contentLength;
                emitter.onNext(progress);
            }
        });
    }

    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {


            case R.id.edit_specify_title:
                intent = new Intent(context, SearchAddTitleActivity.class);
                intent.putExtra("language_cd", language_cd);
                startActivityForResult(intent, Constant.REQUEST_RESULT);
                break;

            case R.id.checkDropSelection:
                if (checkDropSelection.isChecked()) {
                    layImgVideoSelection.setVisibility(View.VISIBLE);
                } else {
                    layImgVideoSelection.setVisibility(View.GONE);
                }
                break;


            case R.id.txtSelectImagCover:
                layImgVideoSelection.setVisibility(View.GONE);
                checkDropSelection.setChecked(false);
                dialog();
                break;
            case R.id.imgTake_photo:
                layImgVideoSelection.setVisibility(View.GONE);
                checkDropSelection.setChecked(false);
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
            case R.id.txtSelectVideo:
                callVideoFrameThumb();
                break;


            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;
            case R.id.btn_update:

                if (isConnectingToInternet(context)) {

//                    brief_cv_param.setBrief_cv(edt_brief.getText().toString());
//                    brief_cvList.set(position, brief_cv_param);
                    updateBriefReq=new UpdateBriefReq();
                    updateBriefReq.setLanguage_id(language_id);
                    updateBriefReq.setUserId(userId);
                    updateBriefReq.setTitleName(edit_specify_title.getText().toString());
                    updateBriefReq.setBriefCv(edt_brief.getText().toString());
                    callUpdateBriefCv();
                } else {
                    showInternetPop(context);
                }
                break;

            case R.id.layUploadVideo:

                callVideoFrameThumb();
                break;

            case R.id.imgVideoCover:

                //navin nimade comment
                if (!videoUrl.isEmpty() && videoUrl != null) {
                    mlog("videoUrl "+videoUrl);
                    intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("URL", videoUrl);
                    startActivity(intent);
                }
                break;


        }
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

                callUpdateBriefCvImage();
                break;
            case FAIL:
                showToast("uploading is fail, please try again");
                layProgress.setVisibility(View.GONE);
                break;
        }
    }









    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";

    private void callService(String path) {

        if (isConnectingToInternet(context)) {
            mServiceResultReceiver = new ServiceResultReceiver(new Handler());
            mServiceResultReceiver.setReceiver(this);
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", path);
            mIntent.putExtra("FileName", mFileName);
            mIntent.putExtra("LANGUAGE_ID", language_id);
            mIntent.putExtra("USER_ID", userId);
            mIntent.putExtra("TOKEN", accessToken);
            mIntent.putExtra(RECEIVER, mServiceResultReceiver);
            mIntent.setAction(ACTION_DOWNLOAD);
            FileUploadService.enqueueWork(context, mIntent);
        }
        else
        {
            showInternetConnectionToast();
        }

    }

    public void callVideoFrameThumb() {
        Intent intent = new Intent(context, MainActivitythumby.class);
        startActivityForResult(intent, Constant.PICK_VIDEO_THUMB);

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


    private void getTitleRef() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.GET_TITLE_REF, getParamTitleReg(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1")) {
                            titleRefsList = new ArrayList<>();
                            if (!js.isNull("title_reflist")) {
                                JSONArray jsonArray = js.getJSONArray("title_reflist");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    TitleRef titleRef = new TitleRef();
                                    titleRef.setLanguage_cd(object.getString("language_cd"));
                                    titleRef.setTitle_cd(object.getString("title_cd"));
                                    titleRef.setTitle_name(object.getString("title_name"));
                                    titleRefsList.add(titleRef);
                                    System.out.println("dsfsd" + titleRefsList.size());
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

    private String getParamTitleReg() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("language_cd", language_cd);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void callAddTitleDialog() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_add_title);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RecyclerView recyclerView = (RecyclerView) dd.findViewById(R.id.recyclerView);
            TitleAddAdapter titleAddAdapter = new TitleAddAdapter(context, titleRefsList, new ApiItemCallback() {
                @Override
                public void result(int x) {
                    TitleRef titleRef = titleRefsList.get(x);
                    title_cd = titleRef.getTitle_cd();
                    edit_specify_title.setText(titleRef.getTitle_name());
                    callAddTitleUpdate(titleRef.getTitle_name());
                    dd.dismiss();

                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(titleAddAdapter);

            if (titleRefsList.size() > 4) {
                ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                params.height = 150;
                recyclerView.setLayoutParams(params);
            }


            ((CustomTextView) dd.findViewById(R.id.txtAddNewTitle)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callAddNewTitleDialog();
                    dd.dismiss();
                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }


    public void callAddNewTitleDialog() {
        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_add_new_title);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            CustomEditText editTitle = (CustomEditText) dd.findViewById(R.id.editTitle);

            ((CustomTextView) dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titleNew = editTitle.getText().toString();
                    if (!titleNew.isEmpty()) {
                        callAddTitleUpdate(titleNew);
                        //callAddNewCategory(languageCd,  categoryNew);
                    } else {
                        editTitle.setError("please insert category");
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


    private void callAddTitleUpdate(String titleName) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.ADD_TITLE_REF, getParamAddTitle(titleName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
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

    private String getParamAddTitle(String titleName) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("language_cd", language_cd);
            jsonObject.put("title_name", titleName);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:

                    CropImage.activity(mCameraImageUri)

                            .start(this);

                    break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {
                        mImageCaptureUri = data.getData();
                        CropImage.activity(mImageCaptureUri)

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
                            imgVideoCover.setImageBitmap(bm);
                            try {
                                imageFile =Utility.getFileByBitmap(context,bm,"thumb");
                                callUpdateBriefCvImage();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
//                            bm.compress(Bitmap.CompressFormat.JPEG, 50, datasecond);
//                            ByteArray = datasecond.toByteArray();
//                            String coverImg = base64String(datasecond.toByteArray());
//
//                            callUploadImage(coverImg, language_cd + "_" + userId + "_thumbnail");

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
                imgVideoCover.setImageBitmap(bitmap);
                try {
                    imageFile =Utility.getFileByBitmap(context,bitmap,"thumb");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                byte[] ByteArray = datasecond.toByteArray();
                String imgbase = base64String(ByteArray);
                String selectedVideoPath = data.getStringExtra(VIDEO_PATH);
                videoPath=selectedVideoPath;
                imgVideoThumb.setVisibility(View.GONE);

                //callUploadImage(imgbase, language_cd + "_" + userId + "_thumbnail");
                callService(selectedVideoPath);

            }

        }
        if (requestCode == Constant.REQUEST_RESULT) {

            if (data != null) {

                String result = data.getStringExtra("result");
                title_cd = data.getStringExtra("id");
                edit_specify_title.setText(result);

            }

        }

    }



    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);
        System.out.println("imgString " + imgString);
        return imgString;
    }


}


/*
* [21:23, 11/05/2020] Ratufa Pankaj: Sir actually my expectations for salary was more, but ashok sir always convey to me that he will increase the salary but he always increase the date that we will discuss it later and never increased my salary.
[21:35, 11/05/2020] Ratufa Pankaj: I joined this company because of my friend tarun, he told me that we need you and we will give u a good salary and also time to time promotion but when i left old company and joined this one ashok sir told me that he will give me 38k and i got struck  because of relationship and at that time i have no more option, but only because of good relationship with you i was working here.
[21:38, 11/05/2020] Ratufa Pankaj: And at the time of joining i asked for 45k but he says that we will increment that but give some time, but that time never came sir after 1 year in incremented 6k.*/