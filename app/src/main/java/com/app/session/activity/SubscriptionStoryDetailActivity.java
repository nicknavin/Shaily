package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.adapter.SpinnerCustomeAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.data.model.Brief_CV;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.service.FileUploadService;
import com.app.session.utility.Constant;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.core.content.FileProvider;
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

public class SubscriptionStoryDetailActivity extends BaseActivity implements View.OnClickListener , ServiceResultReceiver.Receiver{


    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    ImageView imgStory;
    CustomEditText edtStoryTitle, edtStoryNote;
    String subscription_group_cd = "", subscription_group_name = "", story_imgBase64 = "";
    Bundle bundle;
    ArrayList<Brief_CV> brief_cvList;
    Spinner spinner;
    File audioFile = null;
    CustomTextView txt_progress;
    SeekBar audio_seekBar;
    MediaPlayer mPlayer;
    private ServiceResultReceiver mServiceResultReceiver;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    CheckBox audio_play;
    RelativeLayout layAudio;
    LinearLayout layAudioPlay;
    private String outputFile = null,audioFileName="";
    private int cnt;
    private boolean isRecordring;
    CountDownTimer countDownTimer;
    private MediaRecorder audioRecorder;
    CustomTextView txtAudioTimer;

    ImageView imgMick,imgSend,imgAttach,imgRecordStop,imgRecordCancel;
    String mFileName="";
    String story_type ="",story_title="",story_text="",thumbnail_text="",language_cd="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_story);
        if (getIntent().getStringExtra("ID") != null)
        {
            subscription_group_cd=getIntent().getStringExtra("ID");
        }
        initView();
    }

    public void initView() {

        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        bundle = intent.getBundleExtra("BUNDLE");
        if (bundle != null) {
            brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");

        }

        layAudioPlay = (LinearLayout) findViewById(R.id.layAudioPlay);
        layAudio = (RelativeLayout) findViewById(R.id.layAudio);
        imgRecordStop = (ImageView) findViewById(R.id.imgRecordStop);
        imgRecordStop.setOnClickListener(this);
        imgRecordCancel = (ImageView) findViewById(R.id.imgRecordCancel);
        imgRecordCancel.setOnClickListener(this);
        txtAudioTimer = (CustomTextView) findViewById(R.id.txtAudioTimer);
        spinner = (Spinner) findViewById(R.id.spinner);

        SpinnerCustomeAdapter spinnerCustomeAdapter = new SpinnerCustomeAdapter(this, R.layout.row_spinner_layout, brief_cvList);
        spinner.setAdapter(spinnerCustomeAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (brief_cvList.size() > 0)
                {
//                    Toast.makeText(context, categoryArrayList.get(i).getCategory_name(), Toast.LENGTH_SHORT).show();
                    Brief_CV brief_cv = brief_cvList.get(i);
                    language_cd=brief_cv.getLanguage_cd();
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

        imgMick=(ImageView) findViewById(R.id.imgMick);
        imgMick.setOnClickListener(this);
        imgAttach=(ImageView) findViewById(R.id.imgAttach);
        imgAttach.setOnClickListener(this);
        imgSend=(ImageView) findViewById(R.id.imgSend);
        imgSend.setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgVideo)).setOnClickListener(this);
        imgStory = (ImageView) findViewById(R.id.imgStory);
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

    int sec = 0;
    long audioTime = 0;

    private void initAudio() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName = "AUD_" + timeStamp + "_" + ".mp3";
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
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

            case R.id.img_audio_play:

                if(audio_play.isChecked())
                {
                    initAudioPlayStart();
                }
                else
                {
                    initAudioPlayPause();
                }
                break;

            case R.id.imgVideo:
                pickYoutubeVideo();
                break;
            case R.id.imgSend:

                if(story_type.equals("4"))
                {
                    if(validation())
                    {
                        if(mPlayer!=null)
                        {
                            if(mPlayer.isPlaying())
                            {
                                stopRecording();

                            }
                            layAudio.setVisibility(View.GONE);
                            stopRecording();
                            File file = new File(outputFile);
                            sendSubscriptionStory(file);


                        }
                        else
                        {
                            layAudio.setVisibility(View.GONE);
                            stopRecording();
                            File file = new File(outputFile);
                            sendSubscriptionStory(file);
                        }

                    }

                }
                if(story_type.equals("1"))
                {

                    if(validation()) {
                        callUploadImageForStory(story_imgBase64, "story" + Utility.getTimeOnly());
                    }
                }
                else
                {
                    callSendStory("");
                }

                // callSendStory(baseStory);
                break;
            case R.id.imgRecordStop:
                layAudio.setVisibility(View.GONE);
                stopRecording();
                break;
            case R.id.imgRecordCancel:
                callRecordingCancel();
                break;
            case R.id.imgMick:
                story_type ="4";
                setAudioPermission();
                break;

            case R.id.imgAttach:
                imgMick.setEnabled(true);
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


    public void pickYoutubeVideo() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
        try {
            startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
        }
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
                            if(mPlayer!=null) {
                                if (mPlayer.isPlaying())
                                    callRecordingCancel();
                            }
                            imgMick.setEnabled(true);

                            story_type="1";
                            ByteArray = null;
                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            imgStory.setImageBitmap(bm);
                            imgStory.setVisibility(View.VISIBLE);
                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                            ByteArray = datasecond.toByteArray();
                            story_imgBase64 = base64String(ByteArray);

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
        }


    }

    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);

        return imgString;
    }



    public void logoutDialog() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_audio);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setText("Logout");
            ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dd.dismiss();
                }
            });

            ((CustomTextView) dd.findViewById(R.id.tvConfirmCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
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

    Button cancel, send;
    TextView audioRecordingText, txtcount;
    ImageButton record;


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

        if(!outputFile.isEmpty()) {
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
        cnt=0;
        sec=0;
        audioTime=0;
    }



    public void stopRecording()
    {

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

            mFileName = Utility.getTimeOnly()+"story.mp3";
            if(mPlayer!=null)
            {
                mPlayer=null;
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



    private void initAudioPlayStart()
    {

        mPlayer.start();
        eTime = mPlayer.getDuration();
        sTime = mPlayer.getCurrentPosition();
        if(oTime == 0){
            audio_seekBar.setMax(eTime);
            oTime =1;
        }

        txt_progress.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
//        startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
//                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
//        txt_progress.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
//                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
        audio_seekBar.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);



    }

    private void initAudioPlayPause()
    {
        mPlayer.pause();

        Toast.makeText(getApplicationContext(),"Pausing Audio", Toast.LENGTH_SHORT).show();
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




    private void callService(String path) {
        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setReceiver(this);
        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", path);
            mIntent.putExtra("FileName", "");
        mIntent.putExtra(RECEIVER, mServiceResultReceiver);
        mIntent.setAction(ACTION_DOWNLOAD);
        FileUploadService.enqueueWork(this, mIntent);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SHOW_RESULT:
                if (resultData != null) {
                   // initProgress(resultData.getInt("progress"));

                }
                break;

            case STATUS:

                showToast("File is uploaded ");
//                layProgress.setVisibility(View.GONE);
               // callUploadVieoUpdate();

                if (resultData != null) {
                   String data= resultData.getString("DATA");

                    System.out.println("data "+data );
                    callSendStory(data);

                }


                break;

            case FAIL:

                showToast("uploading is fail, please try again");
//                layProgress.setVisibility(View.GONE);
                break;

        }
    }


    private void callUploadVieoUpdate() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPLOAD_VIDEO, getParamUpdateVideo(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();



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

            jsonObject.put("video_url", mFileName);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }



//image sending
    private void callUploadImageForStory(String baseImg, String imgName)
    {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.UPLOADIMAGE, getParamUplodImage(baseImg, imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("story image requ: "+js.toString());
                        if (jsonObject.getString("rstatus").equals("1"))
                        {

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
    private String  getParamUplodImage(String baseImg, String imgName) {
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
//






    private void callSendStory(String filename) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.SUBSCRIPTION_SEND_STORY, sendStoryParameter(filename), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {

                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("result story: "+jsonObject.toString());
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

    private String sendStoryParameter(String fname) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subscription_group_cd", subscription_group_cd);
            jsonObject.put("user_cd", userId);
            jsonObject.put("story_title", edtStoryTitle.getText().toString());
            jsonObject.put("story_type", story_type);
            jsonObject.put("story_text", edtStoryNote.getText().toString());
            jsonObject.put("story_caption", "");
            if(story_type.equals("1")) {
                if (fname.isEmpty()) {
                    jsonObject.put("thumbnail_text", "");
                } else {
                    jsonObject.put("thumbnail_text", fname + ".png");
                }
            }
            else if(story_type.equals("4"))
            {
                if (fname.isEmpty()) {
                    jsonObject.put("thumbnail_text", "");
                } else {
                    jsonObject.put("thumbnail_text", fname );
                }
            }
            else
            {
                jsonObject.put("thumbnail_text", "");
            }
            System.out.println("input : " + jsonObject.toString());
            return jsonObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  System.out.println("input : " + gsonObject.toString());
        return null;
    }


File audioRecordFile=null;
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








    public void sendSubscriptionStory(File file)
    {
        if (isInternetConnected()) {

            showLoading();
            RequestBody usercd=RequestBody.create( MediaType.parse("text/plain"),userId);
            RequestBody tokenid=RequestBody.create( MediaType.parse("text/plain"),accessToken);
            RequestBody storytype=RequestBody.create( MediaType.parse("text/plain"),story_type);
            RequestBody storytitle=RequestBody.create( MediaType.parse("text/plain"),story_title);
            RequestBody time=RequestBody.create( MediaType.parse("text/plain"),getTime());
            RequestBody storytext=RequestBody.create( MediaType.parse("text/plain"),story_text);
            RequestBody thumbnailtext=RequestBody.create( MediaType.parse("text/plain"),thumbnail_text);
            RequestBody subscriptiongroupcd=RequestBody.create( MediaType.parse("text/plain"),subscription_group_cd);
            RequestBody languagecd =RequestBody.create( MediaType.parse("text/plain"),language_cd);
            RequestBody requestfile=null;
            MultipartBody.Part productimg= null;

            if (file!=null) {
                requestfile=RequestBody.create(MediaType.parse("audio/*"),file);
                productimg = MultipartBody.Part.createFormData("AudioFile",file.getName(),requestfile);
            }

            ApiInterface apiService =
                    ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiService.callSendStory(usercd,tokenid,storytype,storytitle,time,storytext,thumbnailtext,subscriptiongroupcd,languagecd,productimg);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody  responseBody=response.body();
                    if(responseBody!=null)
                    {
                        String data = null;
                        try {
                            data = responseBody.string();
                            JSONObject jsonObject=new JSONObject(data);
                            System.out.println("result : "+jsonObject.toString());
                            if(jsonObject.getBoolean("Status"))
                            {
                                finish();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


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


    private boolean validation()
    {
        story_title=edtStoryTitle.getText().toString();
        story_text=edtStoryNote.getText().toString();
        if(story_title.isEmpty())
        {
            showToast("Please give title ");
            return false;
        }
        if(story_text.isEmpty())
        {
            showToast("Please enter description");
            return false;
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mPlayer!=null)
        {
            mPlayer.pause();
            mPlayer=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

