package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.adapter.RemarkAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.RemarkModel;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RemarkActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView img_add, imgRemark, imgAudio;
    RemarkAdapter remarkAdapter;
    LinearLayout layChat, layRemark, layTodo;
    View viewRemark, viewtodo, viewChat;
    String user_cd = "", is_consultant = "", user_name = "", userId = "", accessToken = "";
    String calling_status = "", user_is_consultant = "", email = "";
    CustomEditText edt_remark;
    ImageView imgSend;
    LinearLayout lay;
    boolean flag_permision;
    RelativeLayout parent_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_remark);
        context = this;
        if (getIntent().getStringExtra("ID") != null) {
            user_cd = getIntent().getStringExtra("ID");
        }
        if (getIntent().getStringExtra("NAME") != null) {
            user_name = getIntent().getStringExtra("NAME");
        }
        if (getIntent().getStringExtra("EMAIL") != null) {
            email = getIntent().getStringExtra("EMAIL");
        }

        if (getIntent().getStringExtra("IS_CONSULTANT") != null) {
            is_consultant = getIntent().getStringExtra("IS_CONSULTANT");
            System.out.println("is_consultant    " + is_consultant);
        }
        if (getIntent().getStringExtra("CALLING_STATUS") != null) {
            calling_status = getIntent().getStringExtra("CALLING_STATUS");
        }
        if (getIntent().getStringExtra("USER_IS_CONSULTANT") != null) {
            user_is_consultant = getIntent().getStringExtra("USER_IS_CONSULTANT");
        }

        initView();

        if (Utility.isConnectingToInternet(context)) {
            callGetRemark(getParamRemark(), Urls.GET_REMARK);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

//        if (Utility.isConnectingToInternet(context)) {
//            callGetRemark(getParamRemark(), Urls.GET_REMARK);
//        }
    }


    int i;

    private void initView() {


        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.remark));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        imgSend.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        edt_remark = (CustomEditText) findViewById(R.id.edt_remark);
        imgRemark = (ImageView) findViewById(R.id.imgRemark);
        imgRemark.setOnClickListener(this);
        imgAudio = (ImageView) findViewById(R.id.imgAudio);
        imgAudio.setOnClickListener(this);
        lay = (LinearLayout) findViewById(R.id.lay);
        parent_layout = (RelativeLayout) findViewById(R.id.parent_layout);


        img_add = (ImageView) findViewById(R.id.img_add);
        img_add.setOnClickListener(this);
        remarkAdapter = new RemarkAdapter(context, remarkModelArrayList);
        recyclerView.setAdapter(remarkAdapter);


        RecordView recordView = (RecordView) findViewById(R.id.record_view);
        final RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
//        Button btnChangeOnclick = (Button) findViewById(R.id.btn_change_onclick);

        //IMPORTANT
        recordButton.setRecordView(recordView);

        // if you want to click the button (in case if you want to make the record button a Send Button for example..)
//        recordButton.setListenForRecord(false);

//        btnChangeOnclick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (recordButton.isListenForRecord()) {
//                    recordButton.setListenForRecord(false);
//                    Toast.makeText(RemarkActivity.this, "onClickEnabled", Toast.LENGTH_SHORT).show();
//                } else {
//                    recordButton.setListenForRecord(true);
//                    Toast.makeText(RemarkActivity.this, "onClickDisabled", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        //ListenForRecord must be false ,otherwise onClick will not be called
        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RemarkActivity.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                Log.d("RecordButton", "RECORD BUTTON CLICKED");
            }
        });


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 25
        recordView.setCancelBounds(30);


        recordView.setSmallMicColor(Color.parseColor("#c2185b"));

        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);


        recordView.setSlideToCancelText("Slide To Cancel");


        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);


        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                Log.d("RecordView", "onStart");
                initAudio();
                callRecord();
                Toast.makeText(RemarkActivity.this, "OnStartRecord", Toast.LENGTH_SHORT).show();
                lay.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
                Toast.makeText(RemarkActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
                lay.setVisibility(View.VISIBLE);
                Log.d("RecordView", "onCancel");
                callCancel();
            }

            @Override
            public void onFinish(long recordTime) {

                String time = getHumanTimeText(recordTime);
                Toast.makeText(RemarkActivity.this, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish");

                Log.d("RecordTime", time);
                lay.setVisibility(View.VISIBLE);
                send();
            }

            @Override
            public void onLessThanSecond() {
                Toast.makeText(RemarkActivity.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onLessThanSecond");
            }
        });


        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {

                Log.d("RecordView", "Basket Animation Finished");
            }
        });
    }


    public void send() {
        if (isRecordring) {
            stopRecording();
        }
        //FILE CHECK ....
        if (!(new File(outputFile).exists())) {
//            Toast.makeText(context, com.applozic.mobicomkit.uiwidgets.R.string.audio_recording_send_text, Toast.LENGTH_SHORT).show();
            return;
        }
//                    ConversationUIService conversationUIService = new ConversationUIService(this);
//                    conversationUIService.sendAudioMessage(outputFile);

        File file = new File(outputFile);


        callAddRemark(getParamAddAudio(file), Urls.SET_REMARK_AUDIO);
    }


    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }


    private String outputFile = null;
    private int cnt;
    private boolean isRecordring;
    CountDownTimer t;
    private MediaRecorder audioRecorder;

    private void initAudio() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = "AUD_" + timeStamp + "_" + ".mp3";
//        outputFile = getFilePath(audioFileName, this.getApplicationContext(), "audio/mp3").getAbsolutePath();
        prepareMediaRecorder();
    }

    public void setAudioPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.RECORD_AUDIO, "Record Audio")) {
                return;
            }
        }


        parent_layout.setVisibility(View.VISIBLE);

//        dialogAudioMessage();

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


    public void callRecord() {
        try {

            if (isRecordring) {
                this.stopRecording();
//                cancel.setVisibility(View.VISIBLE);
//                send.setVisibility(View.VISIBLE);
            } else {
//                cancel.setVisibility(View.GONE);
//                send.setVisibility(View.GONE);
                if (audioRecorder == null) {
                    prepareMediaRecorder();
                }
//                audioRecordingText.setText(getResources().getString(com.applozic.mobicomkit.uiwidgets.R.string.stop));
                audioRecorder.prepare();
                audioRecorder.start();
                isRecordring = true;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_mic_inverted);
//                t.cancel();
//                t.start();
//                cnt = 0;
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callCancel(Dialog dd) {

        if (isRecordring) {
            this.stopRecording();
        }

        File file = new File(outputFile);
        if (file != null) {
           // Utils.printLog(context, "AudioFRG:", "File deleted...");
            file.delete();
        }
        dd.dismiss();
    }

    public void callCancel() {

        if (isRecordring) {
            this.stopRecording();
        }

        File file = new File(outputFile);
        if (file != null) {
            //Utils.printLog(context, "AudioFRG:", "File deleted...");
            file.delete();
        }

    }


    public void stopRecording() {

        if (audioRecorder != null) {
            try {
                audioRecorder.stop();
            } catch (RuntimeException stopException) {
//                Utils.printLog(context, "AudioMsgFrag:", "Runtime exception.This is thrown intentionally if stop is called just after start");
            } finally {
                audioRecorder.release();
                audioRecorder = null;
                isRecordring = false;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_normal);
//                audioRecordingText.setText(getResources().getText(com.applozic.mobicomkit.uiwidgets.R.string.start_text));
//                t.cancel();
            }

        }

    }


    ArrayList<RemarkModel> remarkModelArrayList = new ArrayList<>();

    private void callGetRemark(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {

                        Type type = new TypeToken<ArrayList<RemarkModel>>() {
                        }.getType();
                        JSONArray jsonArray = new JSONArray(js.getString("remark_list"));
                        remarkModelArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            RemarkModel model = new RemarkModel();
                            model.setClient_cd(object.getString("client_cd"));
                            model.setClient_name(object.getString("client_name"));
                            model.setConsultant_cd(object.getString("consultant_cd"));
                            model.setConsultant_name(object.getString("consultant_name"));
                            model.setRemark_text(object.getString("remark_text"));
                            model.setRemark_type(object.getString("remark_type"));
                            model.setRemarks_id(object.getString("remarks_id"));
                            model.setThe_date(object.getString("the_date"));
                            model.setSelected(false);
                            model.setDownload_status("0");
                            remarkModelArrayList.add(model);
                        }
//                        remarkModelArrayList = new Gson().fromJson(jsonArray.toString(), type);


                        remarkAdapter = new RemarkAdapter(context, remarkModelArrayList);
                        recyclerView.setAdapter(remarkAdapter);
                        int i = recyclerView.getAdapter().getItemCount() - 1;
                        recyclerView.smoothScrollToPosition(i);
                        remarkAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
//                        Utility.unAuthorized(context);
//                        Utility.un

                    } else {

                    }
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


    private Map<String, Object> getParamRemark() {
        accessToken = DataPrefrence.getPref(context, Constant.ACCESS_TOKEN, "");
        userId = DataPrefrence.getPref(context, Constant.USER_ID, "");
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("client_cd", user_cd);
        params.put("consultant_cd", userId);
        return params;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;

            case R.id.img_add:
                intent = new Intent(context, AddRemarkActivity.class);
                intent.putExtra("ID", user_cd);
                intent.putExtra("NAME", user_name);
                startActivity(intent);
                break;
            case R.id.imgRemark:
                intent = new Intent(context, AddRemarkActivity.class);
                intent.putExtra("ID", user_cd);
                intent.putExtra("NAME", user_name);
                startActivity(intent);
                break;
            case R.id.imgSend:
                if (Utility.isConnectingToInternet(context)) {
                    if (!edt_remark.getText().toString().isEmpty()) {
                        callAddRemark(getParamAddRemark(), Urls.SET_REMARK);
                    } else {
//                        Utility.showToast(context.getResources().getString(R.string.error_email));
                    }
                } else {
                    Utility.showInternetPop(context);
                }
                break;

            case R.id.imgAudio:


//                setAudioPermission();
                break;


            default:
                break;

        }
    }


    private void setUI() {
        viewChat.setVisibility(View.GONE);
        viewRemark.setVisibility(View.GONE);
        viewtodo.setVisibility(View.GONE);
    }


    private void callAddRemark(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    edt_remark.setText(null);
//                    finish();
                    if (Utility.isConnectingToInternet(context)) {
                        callGetRemark(getParamRemark(), Urls.GET_REMARK);
                    }
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
//                        unAuthorized();

                    } else {

                    }
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
//
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


    private Map<String, Object> getParamAddRemark() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("client_cd", user_cd);
        params.put("remark_type", "text");
        params.put("remarks_id", "0");

        params.put("the_date", getTime());
        params.put("remark_text", edt_remark.getText().toString());


        return params;
    }


    private Map<String, Object> getParamAddAudio(File file) {
        Map<String, Object> params = new HashMap<>();
        params.put("", file);
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("client_cd", user_cd);
        params.put("remark_type", "audio");
        params.put("remarks_id", "0");
        params.put("the_date", getTime());
        params.put("remark_text", edt_remark.getText().toString());


        return params;
    }


    public String getTime() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("timeStamp " + timeStamp);
        return timeStamp;
    }


    Button cancel, send;
    TextView audioRecordingText, txtcount;
    ImageButton record;

    public void dialogAudioMessage() {


        final Dialog dd = new Dialog(context);
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String audioFileName = "AUD_" + timeStamp + "_" + ".mp3";
           // outputFile = FileClientService.getFilePath(audioFileName, this.getApplicationContext(), "audio/mp3").getAbsolutePath();
            prepareMediaRecorder();
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.audio_message_layout);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);
            record = (ImageButton) dd.findViewById(R.id.audio_mic_imageview);
            txtcount = (TextView) dd.findViewById(R.id.txtcount);
            audioRecordingText = (TextView) dd.findViewById(R.id.audio_recording_text);
            cancel = (Button) dd.findViewById(R.id.audio_cancel);
            send = (Button) dd.findViewById(R.id.audio_send);
            t = new CountDownTimer(Long.MAX_VALUE, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    cnt++;
                    long millis = cnt;
                    int seconds = (int) (millis / 60);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    txtcount.setText(String.format("%d:%02d:%02d", minutes, seconds, millis));

                }

                @Override
                public void onFinish() {

                }
            };


            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callRecord();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callCancel(dd);
                }
            });


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {

                    //IF recording is running stoped it ...
                    if (isRecordring) {
                        stopRecording();
                    }
                    //FILE CHECK ....
                    if (!(new File(outputFile).exists())) {
//                        Toast.makeText(context, com.applozic.mobicomkit.uiwidgets.R.string.audio_recording_send_text, Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    ConversationUIService conversationUIService = new ConversationUIService(this);
//                    conversationUIService.sendAudioMessage(outputFile);

                    File file = new File(outputFile);


                    callAddRemark(getParamAddAudio(file), Urls.SET_REMARK_AUDIO);

                    dd.dismiss();

                }
            });


            dd.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        remarkAdapter.stop();
//        remarkAdapter.shutdown = true;
        finish();
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                    Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));

                    if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
                        setAudioPermission();
                    } else {
                        checkIfPermissionsGranted();
                    }
                }
                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
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

}
