package com.app.session.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.adapter.SpinnerCustomeAdapter;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.Country;
import com.app.session.data.model.ReqSendStory;
import com.app.session.data.model.SendStoryBody;
import com.app.session.data.model.SendStoryResponseRoot;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.service.FileUploadService;
import com.app.session.thumby.MainActivitythumby;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;
import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;

public class AddSubscriptionStoryActivity extends BaseActivity implements View.OnClickListener, ServiceResultReceiver.Receiver {
    private LinearLayout attachmentLayout;
    CardView cardView;
    private boolean isHidden = true;

    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri, mDocumentUri;
    byte[] ByteArray;
    ImageView imgStory;
    CustomEditText edtStoryTitle, edtStoryNote;

    ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    Spinner spinner;
    File audioFile = null, documentFile = null;
    CustomTextView txt_progress;
    SeekBar audio_seekBar;
    MediaPlayer mPlayer=null;

    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    CheckBox audio_play;
    RelativeLayout layAudio;
    LinearLayout layAudioPlay;
    private String outputFile = null, audioFileName = "";
    private int cnt;
    private boolean isRecordring;
    CountDownTimer countDownTimer;
    private MediaRecorder audioRecorder;
    CustomTextView txtAudioTimer;

    ImageView imgMick, imgSend, imgAttach, imgRecordStop, imgRecordCancel;
    String mFileName = "";
    String story_type = "", story_title = "", story_text = "", thumbnail_text = "", language_cd = "5f108ad28e2da1c0cbcdfcf8";
    int position = 0;
    String video_url = "", subscription_id = "",storyProvider="";
    //    YouTubeThumbnailView youTubeThumbnailView;
    ImageView imageVideo;
    LinearLayout layProgress;
    ProgressBar progressBar;
    CustomTextView txtUploading;
    ;
    String selectedVideoPath = "", videoThumbPath = "", selectedDocumentPath = "";

    CustomTextView txtDocName;
    ImageView imgDoc;
    LinearLayout layDocument, layLoading;
    RelativeLayout layImage;
    ProgressView rey_loading;

    /*...................video area..............................*/
    Bitmap videoThumbBitmap;
    String docName = "";
    private ResultReceiver mResultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_story);
        initGetYouTube();
        if(getIntent().getParcelableExtra(RECEIVER)!=null)
        {
            mResultReceiver = getIntent().getParcelableExtra(RECEIVER);

        }

        if (getIntent().getStringExtra("ID") != null) {
            subscription_id = getIntent().getStringExtra("ID");
            storyProvider="2";
            System.out.println("subscription_id " + subscription_id);
        }
        else
        {
            storyProvider="1";
        }

        position = getIntent().getIntExtra("POSITION", 0);

        initView();
        initWhatsPop();
    }


    private void initGetYouTube() {
        story_type = "video_url";
        Intent intent = getIntent();

        String action = intent.getAction();

        if (intent.getType() != null) {
            String type = intent.getType();
            String packageName = intent.getComponent().getPackageName();
            //if(!type.equals("image/*")&&!packageName.equals("com.app.sessionway"))
            {
                if ("android.intent.action.SEND".equals(action) && type != null && "text/plain".equals(type)) {
                    video_url = intent.getStringExtra("android.intent.extra.TEXT");
                    Log.println(Log.ASSERT, "shareablTextExtra", intent.getStringExtra("android.intent.extra.TEXT"));
                }

                if ("android.intent.action.SEND".equals(action) && type != null && "video".equals(type)) {
                    video_url = intent.getStringExtra("android.intent.extra.TEXT");
                    Log.println(Log.ASSERT, "shareablTextExtra", intent.getStringExtra("android.intent.extra.TEXT"));
                }
                initalizeYoutubePlayerThumb();
            }
//            else
//            {
//                finish();
//            }


            new GetYoutubeTask().execute(video_url);


//https://youtu.be/pEnM4ArZf7U
//            String youtube="https://youtu.be/6ym8dyK94i0";
//            String df= "https://youtu.be/6ym8dyK94i0"+getYoutubeVideoIdFromUrl(youtube) + "/0.jpg";
//
//            if (type.startsWith("text/")) {
//
//                String receivedText = intent
//                        .getStringExtra(Intent.EXTRA_TEXT);
//                if (receivedText != null) {
//                    //do your stuff
//                }
//            }
        }


    }

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
        return "http://img.youtube.com/vi/" + getYoutubeVideoIdFromUrl(videoUrl) + "/0.jpg";
    }

    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        inUrl = inUrl.replace("&feature=youtu.be", "");
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public void initView() {


        brief_cvList = DataPrefrence.getBriefLanguage(context, Constant.BRIEF_CV_DB);
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


//        Intent intent = getIntent();
//        bundle = intent.getBundleExtra("BUNDLE");
//        if (bundle != null) {
//            brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");
//        }
        rey_loading = (ProgressView) findViewById(R.id.rey_loading);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layAudioPlay = (LinearLayout) findViewById(R.id.layAudioPlay);
        layDocument = (LinearLayout) findViewById(R.id.layDocument);
        layLoading = (LinearLayout) findViewById(R.id.layLoading);
        layImage = (RelativeLayout) findViewById(R.id.layImage);
        imgDoc = (ImageView) findViewById(R.id.imgDoc);
        txtDocName = (CustomTextView) findViewById(R.id.txtDocName);
        layAudio = (RelativeLayout) findViewById(R.id.layAudio);
        imgRecordStop = (ImageView) findViewById(R.id.imgRecordStop);
        imgRecordStop.setOnClickListener(this);
        imgRecordCancel = (ImageView) findViewById(R.id.imgRecordCancel);
        imgRecordCancel.setOnClickListener(this);
        txtUploading = (CustomTextView) findViewById(R.id.txtUploading);
        txtAudioTimer = (CustomTextView) findViewById(R.id.txtAudioTimer);
        spinner = (Spinner) findViewById(R.id.spinner);

        SpinnerCustomeAdapter spinnerCustomeAdapter = new SpinnerCustomeAdapter(this, R.layout.row_spinner_layout, brief_cvList);
        spinner.setAdapter(spinnerCustomeAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (brief_cvList.size() > 0) {
//                    Toast.makeText(context, categoryArrayList.get(i).getCategory_name(), Toast.LENGTH_SHORT).show();
                    Brief_CV brief_cv = brief_cvList.get(i);
                    //language_cd = brief_cv.getLanguage_cd();
                    language_cd = "5f108ad28e2da1c0cbcdfcf8";
//
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txt_progress = (CustomTextView) findViewById(R.id.txt_progress);
        audio_seekBar = (SeekBar) findViewById(R.id.audio_seekBar);
        audio_play = (CheckBox) findViewById(R.id.img_audio_play);
        audio_play.setOnClickListener(this);
        edtStoryTitle = (CustomEditText) findViewById(R.id.edtStoryTitle);
        edtStoryNote = (CustomEditText) findViewById(R.id.edtStoryNote);
        ((ImageView) findViewById(R.id.imgAttach)).setOnClickListener(this);

        imgMick = (ImageView) findViewById(R.id.imgMick);
        imgMick.setOnClickListener(this);
        imgAttach = (ImageView) findViewById(R.id.imgAttach);
        imgAttach.setOnClickListener(this);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        imgSend.setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgVideo)).setOnClickListener(this);
        imgStory = (ImageView) findViewById(R.id.imgStory);
        spinner.setSelection(position);


    }

    public void initWhatsPop() {
        attachmentLayout = (LinearLayout) findViewById(R.id.menu_attachments);
        cardView = (CardView) findViewById(R.id.cardView);
        ImageButton btnDocument = (ImageButton) findViewById(R.id.menu_attachment_document);
        ImageButton btnCamera = (ImageButton) findViewById(R.id.menu_attachment_camera);
        ImageButton btnGallery = (ImageButton) findViewById(R.id.menu_attachment_gallery);
        ImageButton btnAudio = (ImageButton) findViewById(R.id.menu_attachment_audio);
        RelativeLayout layPickVideo = (RelativeLayout) findViewById(R.id.layPickVideo);
        ImageButton btnYoutubeVideo = (ImageButton) findViewById(R.id.menu_attachment_youtube);


        btnDocument.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        layPickVideo.setOnClickListener(this);
        btnYoutubeVideo.setOnClickListener(this);

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
            File filesDir = context.getFilesDir();
            File f = new File(filesDir + "/session.png");
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

    public void takeDocument() {
        Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("text/plain");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, Constant.PICKFILE_RESULT_CODE);
    }

    private void openFile1(Uri pickerInitialUri) {
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Provide read access to files and sub-directories in the user-selected
        // directory.
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION |
                Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        startActivityForResult(intent, Constant.PICKFILE_RESULT_CODE);
    }

    private void openFile(Uri pickerInitialUri) {
        String[] mimeTypesS =
                {"application/zip", "application/pdf", "application/msword", "application/vnd.ms-powerpoint", "application/vnd.ms-excel", "text/plain"};
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), Constant.PICKFILE_RESULT_CODE);
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

    int sec = 0;
    long audioTime = 0;

    private void initAudio() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName = "AUD_" + timeStamp + "_" + ".mp3";
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        outputFile = context.getFilesDir().getAbsolutePath();
        outputFile += "/AudioRecording.mp3";
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                cnt++;
                sec++;
                long millis = cnt;
                audioTime = millis;
                //  sec=(int)millis;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                if (sec == 60) {
                    sec = 0;
                }

                txtAudioTimer.setText(String.format("%02d:%02d", seconds, sec));
//                txtAudioTimer.setText(String.format("%02d:%02d", minutes, seconds));

            }

            @Override
            public void onFinish() {

            }
        };

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

    public void pickAudio() {

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.menu_attachment_document:
                story_type = "anydoc";
                openFile(mDocumentUri);
                showMenu();
                break;
            case R.id.menu_attachment_camera:
                setCameraPermission();
                showMenu();

                isForCamera = true;
                break;
            case R.id.menu_attachment_gallery:

                showMenu();
                setGalleryPermission();
                isForCamera = false;
                break;
            case R.id.menu_attachment_audio:

                showMenu();
                story_type = "audio";
                setAudioPermission();
                break;
            case R.id.layPickVideo:
                callVideoFrameThumb();
                showMenu();



                break;
            case R.id.menu_attachment_youtube:
                pickYoutubeVideo();
                showMenu();

                break;


            case R.id.img_audio_play:

                if (audio_play.isChecked()) {
                    initAudioPlayStart();
                } else {
                    initAudioPlayPause();
                }
                break;


            case R.id.imgSend:
                if (validation()) {
                    imgSend.setClickable(false);
                    if (story_type.equals("audio")) {

                        if (mPlayer != null)
                        {
                            if (mPlayer.isPlaying()) {
                                stopRecording();

                            }
                            layAudio.setVisibility(View.GONE);
                            stopRecording();
                            selectedDocumentPath = outputFile;
                            callService();
//                            File file = new File(outputFile);
//                            sendSubscriptionStory(file);


                        } else {
                            layAudio.setVisibility(View.GONE);
                            stopRecording();
//                            File file = new File(outputFile);
//                            sendSubscriptionStory(file);
                            selectedDocumentPath = outputFile;
                            callService();
                        }

                    }
                    if (story_type.equals("image")) {
                        callService();

                    }
                    else if (story_type.equals("video")) {

                        //callSendVideoStory();
                        callService();

                    }
                    else if (story_type.equals("video_url"))
                    {
                        //  callService("");
                        callSendVideoStory();
                    } else if (story_type.equals("anydoc")) {

                        callService();
                    }


                }


                break;
            case R.id.imgRecordStop:
                layAudio.setVisibility(View.GONE);
                stopRecording();
                break;
            case R.id.imgRecordCancel:
                callRecordingCancel();
                break;
            case R.id.imgMick:
                story_type = "audio";
                setAudioPermission();
                break;

            case R.id.imgAttach:
                imgMick.setEnabled(true);

                showMenu();
                // showMenu();
                break;


        }
    }


    public void pickYoutubeVideo() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
        try {
            startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
        }
    }


    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);

        return imgString;
    }


    public MediaRecorder prepareMediaRecorder() {

        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        audioRecorder.setAudioEncodingBitRate(256);
        audioRecorder.setAudioChannels(1);
        audioRecorder.setAudioSamplingRate(44100);
        audioRecorder.setOutputFile(outputFile);
        return audioRecorder;

    }


    public void setAudioPermission() {

        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage")) {
                return;
            }
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.RECORD_AUDIO, "Record Audio")) {
                return;
            }
        }
        imgMick.setEnabled(false);
        imgStory.setVisibility(View.GONE);
        layAudio.setVisibility(View.VISIBLE);
        initAudio();
        callRecord();

    }


    public void callRecord() {
        try {
            showToast(context.getResources().getString(R.string.msg_audio_start));
            if (isRecordring) {
                this.stopRecording();
                countDownTimer.cancel();

            } else {

                if (audioRecorder == null) {
                    prepareMediaRecorder();
                }
//                audioRecordingText.setText(getResources().getString(com.applozic.mobicomkit.uiwidgets.R.string.stop));
                audioRecorder.prepare();
                audioRecorder.start();
                isRecordring = true;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_mic_inverted);
//                t.cancel();
                countDownTimer.start();
//                cnt = 0;
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callRecordingCancel() {
        imgMick.setEnabled(true);
        layAudioPlay.setVisibility(View.GONE);
        layAudio.setVisibility(View.GONE);
        if (isRecordring) {
            this.cancelRecording();
        }

        if (!outputFile.isEmpty()) {
            File file = new File(outputFile);
            if (file != null) {
                // Utils.printLog(context, "AudioFRG:", "File deleted...");
                file.delete();
            }
        }

    }

    public void cancelRecording() {

        if (audioRecorder != null) {
            try {
                showToast(context.getResources().getString(R.string.msg_audio_stop));
                audioRecorder.stop();
            } catch (RuntimeException stopException) {
//                Utils.printLog(context, "AudioMsgFrag:", "Runtime exception.This is thrown intentionally if stop is called just after start");
            } finally {
                audioRecorder.release();
                audioRecorder = null;
                isRecordring = false;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_normal);
//                audioRecordingText.setText(getResources().getText(com.applozic.mobicomkit.uiwidgets.R.string.start_text));
                countDownTimer.cancel();
            }

        }
        cnt = 0;
        sec = 0;
        audioTime = 0;
    }


    public void stopRecording() {

        imgMick.setEnabled(true);

        if (audioRecorder != null) {
            try {
                showToast(context.getResources().getString(R.string.msg_audio_stop));
                audioRecorder.stop();
            } catch (RuntimeException stopException) {
//                Utils.printLog(context, "AudioMsgFrag:", "Runtime exception.This is thrown intentionally if stop is called just after start");
            } finally {
                audioRecorder.release();
                audioRecorder = null;
                isRecordring = false;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_normal);
//                audioRecordingText.setText(getResources().getText(com.applozic.mobicomkit.uiwidgets.R.string.start_text));
                countDownTimer.cancel();
            }

        }
        layAudioPlay.setVisibility(View.VISIBLE);
        initAudioPlayer();
    }


    private static int oTime = 0, sTime = 0, eTime = 0;

    private Handler hdlr = new Handler();

    //    private TextView  startTime, songTime;
    private void initAudioPlayer() {
        try {
            String path = outputFile;

            mFileName = Utility.getTimeOnly() + "story.mp3";
            if (mPlayer != null) {
                mPlayer = null;
            }
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    initAudioPlayer();
                    audio_play.setChecked(false);

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initAudioPlayStart() {

        mPlayer.start();
        eTime = mPlayer.getDuration();
        sTime = mPlayer.getCurrentPosition();
        if (oTime == 0) {
            audio_seekBar.setMax(eTime);
            oTime = 1;
        }

        txt_progress.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
//        startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
//                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
//        txt_progress.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
//                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
        audio_seekBar.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);


    }

    private void initAudioPlayPause() {
        mPlayer.pause();

        Toast.makeText(getApplicationContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
    }


    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
//            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
//                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            audio_seekBar.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };


    private void callService() {
//        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
//        mServiceResultReceiver.setReceiver(this);
        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", selectedVideoPath);
        mIntent.putExtra("FileName", "");
        mIntent.putExtra("VIDEO_THUMB", videoThumbBitmap);//this for image
        mIntent.putExtra("TYPE", "story");
        mIntent.putExtra("USER_ID", userId);
        mIntent.putExtra("TOKEN", accessToken);
        mIntent.putExtra("LANGUAGE_ID", "cd");

        ReqSendStory reqSendStory = new ReqSendStory();
        reqSendStory.setStoryText(story_text);
        reqSendStory.setStoryTitle(story_title);
        reqSendStory.setStory_language_id(language_cd);
        reqSendStory.setStoryProvider(storyProvider);
        reqSendStory.setStoryType(story_type);
        reqSendStory.setVideoUrl(video_url);
        reqSendStory.setSubscription_id(subscription_id);
        reqSendStory.setDocFileName(docName);
        reqSendStory.setDocFilePath(selectedDocumentPath);


        mIntent.putExtra("DATA", reqSendStory);
//        mIntent.putExtra(RECEIVER, mServiceResultReceiver);
        mIntent.putExtra(RECEIVER,mResultReceiver);
        mIntent.setAction(ACTION_DOWNLOAD);
        FileUploadService.enqueueWork(context, mIntent);
        finish();
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

                rey_loading.stop();
//                layProgress.setVisibility(View.GONE);
                layLoading.setVisibility(View.GONE);
                showToast("File is uploaded ");
//                layProgress.setVisibility(View.GONE);
                // callUploadVieoUpdate();
                showToast("navin nimade");
                if (resultData != null) {
                    String data = resultData.getString("DATA");
                    System.out.println("data " + data);
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONObject body = object.getJSONObject("body");
                        SendStoryBody sendStoryBody = new SendStoryBody();
                        sendStoryBody.setStory_text(body.getString("story_text"));
                        sendStoryBody.setStory_title(body.getString("story_title"));
                        sendStoryBody.setStory_type(body.getString("story_type"));
                        sendStoryBody.setStory_url(body.getString("story_url"));
                        sendStoryBody.setThumbnail_url(body.getString("thumbnail_url"));
                        sendStoryBody.setViews(body.getString("views"));
                        sendStoryBody.set_id(body.getString("_id"));
                        sendStoryBody.setUser_id(body.getString("user_id"));
                        sendStoryBody.setSubscription_id(body.getString("subscription_id"));
                        sendStoryBody.setDisplay_doc_name(body.getString("display_doc_name"));
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("REFRESH", true);
                        returnIntent.putExtra("DATA", sendStoryBody);
                        setResult(Constant.REQUEST_NEW_STORY, returnIntent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                break;

            case FAIL:
                imgSend.setClickable(true);
                layLoading.setVisibility(View.GONE);
                showToast("uploading is fail, please try again");
//                layProgress.setVisibility(View.GONE);
                break;

        }
    }


    File audioRecordFile = null;

    public void send() {
        if (isRecordring) {
            stopRecording();
        }
        //FILE CHECK ....
        if (!(new File(outputFile).exists())) {
//            Toast.makeText(context, context.getResources().getString(R.string.audio_recording_send_text), Toast.LENGTH_SHORT).show();
            return;
        }
//                    ConversationUIService conversationUIService = new ConversationUIService(this);
//                    conversationUIService.sendAudioMessage(outputFile);

        audioRecordFile = new File(outputFile);


    }

    public void sendSubscriptionStory(File file) {
        if (isInternetConnected()) {

            showLoading();

            RequestBody userCd = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody storyText = RequestBody.create(MediaType.parse("text/plain"), story_text);
            RequestBody storyType = RequestBody.create(MediaType.parse("text/plain"), story_type);
            RequestBody storyTitle = RequestBody.create(MediaType.parse("text/plain"), story_title);
            RequestBody videoUrl = RequestBody.create(MediaType.parse("text/plain"), video_url);
            RequestBody subscriptionId = RequestBody.create(MediaType.parse("text/plain"), subscription_id);
            RequestBody languageId = RequestBody.create(MediaType.parse("text/plain"), language_cd);
            System.out.println("subscription_id 2: " + subscription_id);
            RequestBody requestfile = null;
            MultipartBody.Part audioFile = null;

            if (file != null) {
                requestfile = RequestBody.create(MediaType.parse("audio/*"), file);
                audioFile = MultipartBody.Part.createFormData("file", file.getName(), requestfile);
            }

            ApiInterface apiService =
                    ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SendStoryResponseRoot> call;
            if (subscription_id.isEmpty()) {
                System.out.println("subscription_id 3:" + subscription_id);
                call = apiService.callUserSendStory1(accessToken, userCd, storyType, storyTitle, storyText, videoUrl, languageId,audioFile);
            } else {
                System.out.println("subscription_id 4:" + subscription_id);
                call = apiService.callSendStory1(accessToken, userCd, subscriptionId, storyType, storyTitle, storyText, videoUrl, languageId,audioFile);
            }
            call.enqueue(new Callback<SendStoryResponseRoot>() {
                @Override
                public void onResponse(Call<SendStoryResponseRoot> call, Response<SendStoryResponseRoot> response) {
                    dismiss_loading();
                    SendStoryResponseRoot responseBody = response.body();
                    if (responseBody != null) {
                        SendStoryBody sendStoryBody = response.body().getSendStoryBody();

                        if (responseBody.getStatus() == 200) {
//                                if(db.getNotesCount()>0)
//                                {
//                                    db.deleteNote();
//                                }
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("REFRESH", true);
                            returnIntent.putExtra("DATA", sendStoryBody);
                            setResult(Constant.REQUEST_NEW_STORY, returnIntent);
                            finish();
                        }


                    } else {
                        //      errorBody(response,false);
                    }
                }

                @Override
                public void onFailure(Call<SendStoryResponseRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });


        } else {
            showInternetConnectionToast();
        }
    }



    /*......................sendFile.........................*/

    public void sendDocFileStory() {
        if (isInternetConnected()) {

            showLoading();
            String filePath = getRealPathFromUri(mDocumentUri);


            RequestBody userCd = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody storyText = RequestBody.create(MediaType.parse("text/plain"), story_text);
            RequestBody storyType = RequestBody.create(MediaType.parse("text/plain"), story_type);
            RequestBody storyTitle = RequestBody.create(MediaType.parse("text/plain"), story_title);
            RequestBody subscriptionId = RequestBody.create(MediaType.parse("text/plain"), subscription_id);
            RequestBody docName = RequestBody.create(MediaType.parse("text/plain"), txtDocName.getText().toString());
            System.out.println("subscription_id 2: " + subscription_id);
            RequestBody requestfile = null;
            MultipartBody.Part audioFile = null;

            if (filePath != null && !filePath.isEmpty()) {
                File file = new File(filePath);
                requestfile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                audioFile = MultipartBody.Part.createFormData("file", file.getName(), requestfile);
            }

            ApiInterface apiService =
                    ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SendStoryResponseRoot> call;
            if (subscription_id.isEmpty()) {
                System.out.println("subscription_id 3:" + subscription_id);
                call = apiService.callUserSendStoryDocFile(accessToken, userCd, storyType, storyTitle, storyText, docName, audioFile);
            } else {
                System.out.println("subscription_id 4:" + subscription_id);
                call = apiService.callSendStoryDocFile(accessToken, userCd, subscriptionId, storyType, storyTitle, storyText, docName, audioFile);
            }
            call.enqueue(new Callback<SendStoryResponseRoot>() {
                @Override
                public void onResponse(Call<SendStoryResponseRoot> call, Response<SendStoryResponseRoot> response) {
                    dismiss_loading();
                    SendStoryResponseRoot responseBody = response.body();
                    if (responseBody != null) {
                        SendStoryBody sendStoryBody = response.body().getSendStoryBody();
                        sendStoryBody.setDisplay_doc_name(txtDocName.getText().toString());
                        if (responseBody.getStatus() == 200) {
//                                if(db.getNotesCount()>0)
//                                {
//                                    db.deleteNote();
//                                }
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("REFRESH", true);
                            returnIntent.putExtra("DATA", sendStoryBody);
                            setResult(Constant.REQUEST_NEW_STORY, returnIntent);
                            finish();
                        }


                    } else {
                        //      errorBody(response,false);
                    }
                }

                @Override
                public void onFailure(Call<SendStoryResponseRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });


        } else {
            showInternetConnectionToast();
        }
    }


    private boolean validation() {
        story_title = edtStoryTitle.getText().toString();
        story_text = edtStoryNote.getText().toString();
//        if(story_title.isEmpty())
//        {
//            showToast("Please give title ");
//            return false;
//        }
//        if(story_text.isEmpty())
//        {
//            showToast("Please enter description");
//            return false;
//        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.pause();
            mPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    File imageFile;

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

    private void callSendStory() {
        if (isConnectingToInternet(context)) {
            showLoading();

            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);

            RequestBody userCd = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody storyText = RequestBody.create(MediaType.parse("text/plain"), story_text);
            RequestBody storyType = RequestBody.create(MediaType.parse("text/plain"), story_type);
            RequestBody storyTitle = RequestBody.create(MediaType.parse("text/plain"), story_title);
            RequestBody videoUrl = RequestBody.create(MediaType.parse("text/plain"), video_url);
            RequestBody subscriptionId = RequestBody.create(MediaType.parse("text/plain"), subscription_id);
            RequestBody languageId = RequestBody.create(MediaType.parse("text/plain"), language_cd);
            RequestBody requestfile = null;
            MultipartBody.Part productimg = null;

            if (imageFile != null) {
                //   Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                //  imgProfile.setImageBitmap(myBitmap);
                requestfile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                productimg = MultipartBody.Part.createFormData("file", "image.jpg", requestfile);
            }
            Call<SendStoryResponseRoot> call = null;
            if (subscription_id.isEmpty()) {

              //  call = apiInterface.reqUserSendsStory(accessToken, userCd, storyType, storyTitle, storyText, videoUrl,languageId ,productimg);

            } else {

                call = apiInterface.reqSendsStory(accessToken, userCd, subscriptionId, storyType, storyTitle, storyText, videoUrl,languageId, productimg);
            }
            call.enqueue(new Callback<SendStoryResponseRoot>() {
                @Override
                public void onResponse(Call<SendStoryResponseRoot> call, Response<SendStoryResponseRoot> response) {
                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {

                            SendStoryBody sendStoryBody = response.body().getSendStoryBody();
                            System.out.println("subscription_id result :" + sendStoryBody.getSubscription_id());
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("REFRESH", true);
                            returnIntent.putExtra("DATA", sendStoryBody);
                            setResult(Constant.REQUEST_NEW_STORY, returnIntent);

                            finish();
                        }
                    }

//                    if(response.body()!=null)
//                    {
//                        errorBody(response,true);
//                    }
//                    else
//                    {
//                        errorBody(response,false);
//                    }
                }

                @Override
                public void onFailure(Call<SendStoryResponseRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });

        } else {
            showInternetConnectionToast();
        }
    }


    private void callSendVideoStory() {
        if (isConnectingToInternet(context)) {
            showLoading();

            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);

            RequestBody userCd = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody storyText = RequestBody.create(MediaType.parse("text/plain"), story_text);
            RequestBody storyType = RequestBody.create(MediaType.parse("text/plain"), story_type);
            RequestBody storyTitle = RequestBody.create(MediaType.parse("text/plain"), story_title);
            RequestBody stryProvider = RequestBody.create(MediaType.parse("text/plain"), storyProvider);
            RequestBody videoUrl = RequestBody.create(MediaType.parse("text/plain"), video_url);
            RequestBody languageId = RequestBody.create(MediaType.parse("text/plain"), language_cd);

            RequestBody requestfile = null;
            MultipartBody.Part productimg = null;

            Call<SendStoryResponseRoot> call = null;
            call = apiInterface.reqUserSendsStory(accessToken, userCd, storyType, storyTitle, storyText, videoUrl,stryProvider,languageId, productimg);

            call.enqueue(new Callback<SendStoryResponseRoot>() {
                @Override
                public void onResponse(Call<SendStoryResponseRoot> call, Response<SendStoryResponseRoot> response) {
                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {

                            SendStoryBody sendStoryBody = response.body().getSendStoryBody();
                            System.out.println("subscription_id result :" + sendStoryBody.getSubscription_id());

                            Intent returnIntent = new Intent(context, ConsultantUserActivity.class);
                            returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            returnIntent.putExtra("REFRESH", true);
                            returnIntent.putExtra("DATA", sendStoryBody);
                            startActivity(returnIntent);
                            finish();
                        }
                    }

//                    if(response.body()!=null)
//                    {
//                        errorBody(response,true);
//                    }
//                    else
//                    {
//                        errorBody(response,false);
//                    }
                }

                @Override
                public void onFailure(Call<SendStoryResponseRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });

        } else {
            showInternetConnectionToast();
        }
    }

    private void initalizeYoutubePlayerThumb() {
        imageVideo = (ImageView) findViewById(R.id.imgStory);
        String url = Utility.getYoutubeThumbnailUrlFromVideoUrl(video_url);
        Picasso.with(context).load(url).into(imageVideo);
        imageVideo.setVisibility(View.VISIBLE);


//        youTubeThumbnailView.initialize(Config.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
////                String youtube="https://youtu.be/GtJizVWPYBA";
//                String id =extractYTId(video_url);
//                youTubeThumbnailLoader.setVideo(id);
//                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
//
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                //write something for failure
//            }
//        });
    }

    public String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

    YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
        @Override
        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//            youTubePlayerView.setVisibility(View.VISIBLE);
//            initializeYoutubePlayer();
//            youTubeThumbnailView.setVisibility(View.VISIBLE);
//            holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
        }
    };


    public String getTitleQuietly(String youtubeUrl) {
        try {
            if (youtubeUrl != null) {
                String id = extractYTId(youtubeUrl);

                URL embededURL = new URL("https://www.youtube.com/oembed?url=http://www.youtube.com/watch?v=" +
                        id + "&format=json");


                System.out.println("embededURL" + embededURL);
                String weather = "";
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) embededURL.openConnection();

                    InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();

                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }

                    JSONObject topLevel = new JSONObject(builder.toString());
                    weather = topLevel.toString();


                    urlConnection.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return weather;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    String imgHtml = "";

    private class GetYoutubeTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];

            String data = getTitleQuietly(url);
            return data;
        }

        @Override
        protected void onPostExecute(String temp) {
            try {
                JSONObject object = new JSONObject(temp);
                edtStoryTitle.setText(object.getString("title"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(temp);
        }

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void showMenu() {
        int cx = (cardView.getLeft() + cardView.getRight());
        int cy = cardView.getTop();
        int radius = Math.max(cardView.getWidth(), cardView.getHeight());

        if (isHidden) {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0, radius);
            cardView.setVisibility(View.VISIBLE);
            anim.setDuration(400);
            anim.start();
            isHidden = false;
        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(cardView, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    cardView.setVisibility(View.INVISIBLE);
                    isHidden = true;
                }
            });
            anim.setDuration(400);
            anim.start();
        }
    }

    private void hideMenu() {
        attachmentLayout.setVisibility(View.GONE);
        isHidden = true;
    }

    public void initProgress(int value) {
        System.out.println("loading is start");
        rey_loading.start();
        layLoading.setVisibility(View.VISIBLE);
        txtUploading.setText("" + value + "%" + "uploaded");
//        layProgress.setVisibility(View.VISIBLE);

//        progressBar.setProgress(value);


    }

    public void callVideoFrameThumb() {
        Intent intent = new Intent(context, MainActivitythumby.class);
        startActivityForResult(intent, Constant.PICK_VIDEO_THUMB);
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

//                        CropImage.activity(mImageCaptureUri)
//                                .setAspectRatio(1, 1)
//                                .setGuidelines(CropImageView.Guidelines.ON)
//                                .setFixAspectRatio(false)
//                                .start(this);
                        int w = Utility.getScreenWidth(this);
                        CropImage.activity(mImageCaptureUri)
//                                .setCropShape(CropImageView.CropShape.RECTANGLE)
//                                .setAspectRatio(2, 1)
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
                            if (mPlayer != null) {
                                if (mPlayer.isPlaying())
                                    callRecordingCancel();
                            }
                            imgMick.setEnabled(true);

                            story_type = "image";
                            ByteArray = null;
                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            videoThumbBitmap = bm;
                            imgStory.setImageBitmap(bm);
                            imgStory.setVisibility(View.VISIBLE);


//                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
//                            bm.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
//                            ByteArray = datasecond.toByteArray();
//                            story_imgBase64 = base64String(ByteArray);

                            persistImage(bm, "story_image");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;
                case Constant.REQUEST_AUDIO_RECORD:
                    Uri audioFileUri = data.getData();
                    audioFile = new File(audioFileUri.getPath());

                    break;

                default:
                    break;


            }

            if (requestCode == Constant.PICKFILE_RESULT_CODE) {
                if (data != null) {
                    try {
                        mDocumentUri = data.getData();
                        layDocument.setVisibility(View.VISIBLE);
                        selectedDocumentPath = getRealPathFromUri(mDocumentUri);
                        docName = getFileName(mDocumentUri);
                        if (docName.contains(".doc")) {
                            imgDoc.setImageResource(R.mipmap.docs_story);
                        }
                        if (docName.contains(".pdf")) {
                            imgDoc.setImageResource(R.mipmap.pdf_story);
                        }
                        if (docName.contains(".zip")) {
                            imgDoc.setImageResource(R.mipmap.zip_story);
                        }
                        if (docName.contains(".xls")) {
                            imgDoc.setImageResource(R.mipmap.xls_story);
                        }

                        txtDocName.setText(docName);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }


            if (requestCode == Constant.PICK_VIDEO_THUMB) {

                if (data != null) {

                    long location = data.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0);
                    Uri uri = data.getParcelableExtra(EXTRA_URI);
                    videoThumbBitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);

                    imgStory.setImageBitmap(videoThumbBitmap);

                    ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                    videoThumbBitmap.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
//                    try {
//                        imageFile =Utility.getFileByBitmap(context,bitmap,"thumb");
//                        videoThumbPath=imageFile.getAbsolutePath();
//
//                        System.out.println("videoThumbPath: "+videoThumbPath);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }


                    selectedVideoPath = data.getStringExtra(VIDEO_PATH);

                    //ima.setVisibility(View.GONE);

                    //callUploadImage(imgbase, language_cd + "_" + userId + "_thumbnail");
                    story_type = "video";


                }

            }


        }


    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }


    public String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mPlayer!=null)
        {
            if(mPlayer.isPlaying())
            {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer=null;
        }
        rey_loading.stop();
        finish();
    }
}

