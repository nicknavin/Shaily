package com.app.session.activity;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.AudioVideoData;
import com.app.session.service.MyForgroundService;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class CallIncomingActivity extends BaseActivity implements View.OnClickListener {

    private MediaPlayer mMediaPlayer;
    private ImageView img_connect_call, img_end_call;
    private Context context;
    private String sessionId = "", token = "", caseId = "", patientId = "";
    CustomTextView txtCallingType,txtUserName;
String type="";
String data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_call_incoming_layout);

        if(getIntent().getStringExtra("DATA")!=null)
        {
            data=getIntent().getStringExtra("DATA");
            try {
                JSONObject jsonObject=new JSONObject(data);
                type=jsonObject.getString("callType");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        context = this;
        makeScreenOn();
        initView();
    }

    private void initView() {
        txtCallingType=(CustomTextView) findViewById(R.id.txtCallingType);
        txtUserName=(CustomTextView) findViewById(R.id.txtUserName);

        img_connect_call = (ImageView) findViewById(R.id.img_connect_call);
        img_connect_call.setOnClickListener(this);

        img_end_call = (ImageView) findViewById(R.id.img_end_call);
        img_end_call.setOnClickListener(this);



        String type = DataPrefrence.getPref(context, Constant.CALLING_TYPE, "").toLowerCase();
        String name = DataPrefrence.getPref(context, Constant.FROM_USER_NAME, "").toLowerCase();

        txtCallingType.setText(type.toUpperCase()+" CALLING");
        txtUserName.setText(name);
        Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, ring);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();





     /*   sessionId = getIntent().getStringExtra(Constant.SESSION_ID);
        token = getIntent().getStringExtra(Constant.TOKEN);
        caseId = getIntent().getStringExtra(Constant.CASE_ID);
        patientId = getIntent().getStringExtra(Constant.PATIENT_ID);*/

    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(Constant.CUSTOME_BROADCAST));
        super.onResume();
      /*  if(wl.isHeld()){
            wl.release();
        }
        if(wl_cpu.isHeld()){
            wl_cpu.release();
        }*/
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
//            String message = intent.getStringExtra("message");
//            tvListnerCount.setText(context.getResources().getString(R.string.people_listening)+" " + message);
//            Log.d("receiver", "Got message: " + message);

           callDisconnect();
        }
    };

    private void callDisconnect()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        }
        else {
            super.finish();
        }

    }


    public void logoutDialog() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_iphone);
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





    PowerManager.WakeLock wl, wl_cpu;

    private void makeScreenOn() {
        //unlock Phone
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        //Wake screen
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isInteractive();
        }

        Log.d("ReminderActivity", "Alarm Service: ScreenOn: " + isScreenOn);

//        if (!isScreenOn) {
//            wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
//            wl.acquire(10000);
//            wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCPULock");
//            wl_cpu.acquire(10000);
//        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        if (wl != null) {
            wl.acquire();
            wl_cpu.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_connect_call:
                pickCall();
                break;

            case R.id.img_end_call:
                endCall();
                break;

        }
    }

    private void endCall()
    {
            Intent service = new Intent(this, MyForgroundService.class);
            service.putExtra(MyForgroundService.EXTRA_EVENT_TYPE, MyForgroundService.EVENT_CALL_DISCONNET);
            service.putExtra("DATA",data);
            startService(service);

        finish();


//        finish();
    }
    private void endCallold()
    {
        finish();
        String type = DataPrefrence.getPref(context, Constant.CALLING_TYPE, "").toLowerCase();
        if (type.equals("video"))
        {
            dissconnectAudioVideoCalling(getParam(Constant.DISCONNECT_VIDEO), Urls.VIDEO_AUDIO_CALLING);
        } else {
            dissconnectAudioVideoCalling(getParam(Constant.DISCONNECT_AUDIO), Urls.VIDEO_AUDIO_CALLING);
        }

//        finish();
    }

    private void pickCall() {
        Intent intent=null;
      //  String type = DataPrefrence.getPref(context, Constant.CALLING_TYPE, "").toLowerCase();
        if (type.equals("video"))
        {

            intent = new Intent(context, VideoChatViewActivity.class);
        } else {
            intent = new Intent(context, VoiceChatViewActivity.class);
        }
        intent.putExtra("DATA",data);

        /*intent.putExtra(Constant.SESSION_ID, sessionId);
        intent.putExtra(Constant.TOKEN, token);
        intent.putExtra(Constant.CASE_ID, caseId);
        intent.putExtra(Constant.PATIENT_ID, patientId);*/
        startActivity(intent);
        finish();
    }


    private void dissconnectAudioVideoCalling(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    DataPrefrence.setPref(context, Constant.VIDEO_SESSIONID, "");
                    DataPrefrence.setPref(context, Constant.VIDEO_TOKENID, "");
callDisconnect();

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
//                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
//                    {
//                        unAuthorized();
//                        showToast(failed);
//                    } else {
//                        MyDialog.iPhone(failed, context);
//                    }
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

    private Map<String, Object> getParam(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("from_user_cd", userId);
        params.put("Token_id", accessToken);
        params.put("type",type );
        params.put("user_name",user_name);
        params.put("to_user_cd", DataPrefrence.getPref(context,Constant.FROM_USER_CD,""));
        return params;
    }


}
