package com.app.session.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.session.R;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Constant {

    public static final String CHAT_SERVER_URL = "https://www.sessionway.com/";
    public static final String NEW_MESSAGE = "new_message";
    public static final String EVENT_JOIN = "join";
    public static final String FILES_EVENT = "files";
    public static final String IS_ON = "isOn";//reciever
    public static final String IS_ONLINE = "isOnline";//emit
    public static final String JOINMOBILEUSERS = "joinMobileUsers";//emit
    public static final String BACKGROUNDSTATE = "backgroundstate";//emit

    public static  String IS_TYPING = "isTyping";
    public static final String TYPING = "typing";

    public static final String CALL_NOTIFICATION = "callNotifcation";//emit
    public static final String CALL_DISCONNECT = "callDisconnect";//emit
    public static final String CALL_DISCONNECT_NOTIFY = "callDisconnectNotify";//emit
    public static final String CALL_DISCONNECT_CALLER_END = "callDisconnectCallerEnd";//emit
    public static final String LEAVE_ROOM = "leaveRoom";//emit
    public static final String CONNECTED_BOTH = "connected_both";//event
    public static final String CALL_NOTIFY = "callNotify";//event
    public static final String CALL_DISCONNECT_CALLED_END_NOTIFY = "callDisconnectCalledEndNotify";//event
    public static final String IS_CONTENTREAD = "isContentRead";//event
    public static final String CONNECT_CALL = "connect_call";
    public static final String DISCONNECT_CALL = "disconnect_call";
    public static final String DISCONNECT_MISSED_CALL = "disconnect_missed_call";
    public static final String CALL_BACK = "call_back";





    public static final int REQUEST_CODE_CAMERA = 10;
    public static final int REQUEST_CODE_ALBUM = 2;
    public static final int PICKFILE_RESULT_CODE = 29;
    public static final int PICK_AUDIO_CODE = 30;
    public static final int REQUEST_CODE_ALBUM_Gallery = 15;
    public static final int REQUEST_RESULT = 220;
    public static final int REQUEST_CURRENCY = 221;
    public static final int REQUEST_AUDIO_RECORD = 16;

    public static final int PICK_COUNTRY = 3;
    public static final int PICK_CATEGORY = 4;
    public static final int PICK_SUB_CATEGORY = 5;
    public static final int PICK_LANGUAGE = 6;
    public static final int PICK_PREF_LANGUAGE = 12;
    public static final int PAGE_REFRESH = 13;
    public static final int PROFILE_REFRESH = 24;
    public static final int LANG_REFRESH = 25;
    public static final int CATG_REFRESH = 26;
    public static final int PICK_CONSULTANT_NAME = 14;
    public static final int PICK_VIDEO_CAPTURE = 17;
    public static final int PICK_VIDEO_THUMB = 18;
    public static final int PICK_VIDEO_URI = 21;
    public static final int REQUEST_BRIEF = 22;
    public static final int REQUEST_NEW_STORY = 23;
    public static final int REQUEST_CODE_MY_PICK = 27;
    public static final String LANGUAGE_SPEAK = "language_speak";
    public static  final int LANGUAGE_CODE = 7;
    public static final int CATEGORY_CODE = 8;
    public static int SUB_CATEGORY_CODE = 9;

    public static final String VIDEO = "Video";
    public static final String AUDIO = "Audio";
    public static final String DISCONNECT_AUDIO = "Disconnect Audio";
    public static final String DISCONNECT_VIDEO = "Disconnect Video";
    public static final String CALLING_TYPE = "calling_type";

    public static final String CUSTOME_BROADCAST = "com.app.session";
    public static final String CUSTOME_BROADCAST_SMS = "android.provider.Telephony.SMS_RECEIVED";
    public static final String PREFERED_LANGUAGE = "prefered_language";
    public static final String PREFERED_LANGUAGE_NAME = "prefered_language_name";

    public static final String CATEGORY_CD = "category_cd";
    public static final String BRIEF_CV = "brief_cv";
    public static final String BRIEF_CV_DB = "brief_cv_db";
    public static final String LANG_DB = "lang_db";

    public static final String SUB_CATEGORY_CD = "sub_category_cd";
    public static final String LANGUAGE_CD = "language_cd";
    public static final String USERLANGAUGES = "userLangauges";
    public static final String LANGUAGE_NAME = "language_name";


    public static final String LANGUAGE = "language";
    public static final String TYPE_DATA = "type_data";
    public static final String CATEGORY = "category";
    public static final String SUB_CATEGORY = "sub_category";
    public static final String LANGUAGE_SELECTED = "language_selected";
    public static final String CATEGORY_SELECTED = "category_selected";
    public static final String IS_COMPANY = "is_company";
    public static final String IS_CONSULTANT = "is_consultant";
    public static final String IS_USER = "is_user";
    public static final String USER_TYPE = "user_type";
    public static final String CONSULTANT = "consultant";
    public static final String CLIENT = "client";
    public static final String FROM_USER_CD = "from_user_cd";
    public static final String FROM_USER_NAME = "from_user_name";
    public static final String VIDEO_SESSIONID = "video_sessionId";
    public static final String VIDEO_TOKENID = "video_tokenId";


    public static String CALL = "call";

    public static String LOGIN_TYPE = "login_type";
    public static String LOGIN_FLAG = "login_flag";



    public static  String EMAILID = "email_id";
    public static  String USER_NAME = "user_name";
    public static  String PASSWORD = "password";
    public static  String CONFIRM_PASSWORD = "confirm_password";
    public static  String FULLNAME = "fullname";
    public static  String MOBILE_NO = "mobile_no";
    public static  String COUNTRY_ID = "country_id";
    public static  String COUNTRY_CODE = "country_code";
    public static  String CONTACT_NO = "contact_no";
    public static  String DIAL_Code = "dial_code";
    public static  String GENDER = "gender";


    public static String VERIFICATION_FLAG = "verification_flag";
    public static String ACCESS_TOKEN = "access_token";
    public static String USER_ID = "user_id";
    public static String LOGIN_USER_ID = "login_user_id";
    public static String DEVICE_ID = "device_id";
    public static String ID = "id";
    public static String PROFILE_IMAGE = "profile_image";
    public static String VERIFICATION_CODE = "verification_code";
    public final static String MEDIA_PATH = "media_path";

    /*Day*/
    public static String SUNDAY = "sunday";
    public static String MONDAY = "monday";
    public static String TUESDAY = "tuesday";
    public static String WEDNESDAY = "wednesday";
    public static String THURSDAY = "thursday";
    public static String FRIDAY = "friday";
    public static String SATURDAY = "saturday";

    public static Integer LOCATION = 0x1;


    public static int CAM = 0;
    public static Bitmap bitmap = null;

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        String manufactures = android.os.Build.MANUFACTURER;

        if (Constant.CAM == 0) {
            matrix.preScale(-1.0f, 1.0f);
        }
        else
        {
            System.out.println("CAM1 MATRIX");
            matrix.preScale(-1.0f, 1.0f);
        }

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



//    package com.journaldev.bundlednotificationsexample;
//
//        import android.app.NotificationChannel;
//        import android.app.NotificationManager;
//        import android.app.PendingIntent;
//        import android.content.Context;
//        import android.content.Intent;
//        import android.support.v4.app.NotificationCompat;
//        import android.support.v7.app.AppCompatActivity;
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.Toast;
//
//    public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//        Button btnBundleNotification, btnSingleNotification;
//        NotificationManager notificationManager;
//        int bundleNotificationId = 100;
//        int singleNotificationId = 100;
//        NotificationCompat.Builder summaryNotificationBuilder;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//
//            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            btnBundleNotification = findViewById(R.id.btnBundleNotification);
//            btnSingleNotification = findViewById(R.id.btnSingleNotification);
//            btnBundleNotification.setOnClickListener(this);
//            btnSingleNotification.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btnBundleNotification:
//
//
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        NotificationChannel groupChannel = new NotificationChannel("bundle_channel_id", "bundle_channel_name", NotificationManager.IMPORTANCE_LOW);
//                        notificationManager.createNotificationChannel(groupChannel);
//                    }
//                    bundleNotificationId += 100;
//                    singleNotificationId = bundleNotificationId;
//                    String bundle_notification_id = "bundle_notification_" + bundleNotificationId;
//                    Intent resultIntent = new Intent(this, MainActivity.class);
//                    resultIntent.putExtra("notification", "Summary Notification Clicked");
//                    resultIntent.putExtra("notification_id", bundleNotificationId);
//                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//                    summaryNotificationBuilder = new NotificationCompat.Builder(this, "bundle_channel_id")
//                            .setGroup(bundle_notification_id)
//                            .setGroupSummary(true)
//                            .setContentTitle("Bundled Notification. " + bundleNotificationId)
//                            .setContentText("Content Text for bundle notification")
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentIntent(resultPendingIntent);
//
//                    notificationManager.notify(bundleNotificationId, summaryNotificationBuilder.build());
//
//                    break;
//
//                case R.id.btnSingleNotification:
//
//
//                    bundle_notification_id = "bundle_notification_" + bundleNotificationId;
//
//                    resultIntent = new Intent(this, MainActivity.class);
//                    resultIntent.putExtra("notification", "Summary Notification Clicked");
//                    resultIntent.putExtra("notification_id", bundleNotificationId);
//                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    //We need to update the bundle notification every time a new notification comes up.
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        if (notificationManager.getNotificationChannels().size() < 2) {
//                            NotificationChannel groupChannel = new NotificationChannel("bundle_channel_id", "bundle_channel_name", NotificationManager.IMPORTANCE_LOW);
//                            notificationManager.createNotificationChannel(groupChannel);
//                            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
//                            notificationManager.createNotificationChannel(channel);
//                        }
//                    }
//                    summaryNotificationBuilder = new NotificationCompat.Builder(this, "bundle_channel_id")
//                            .setGroup(bundle_notification_id)
//                            .setGroupSummary(true)
//                            .setContentTitle("Bundled Notification " + bundleNotificationId)
//                            .setContentText("Content Text for group summary")
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentIntent(resultPendingIntent);
//
//
//                    if (singleNotificationId == bundleNotificationId)
//                        singleNotificationId = bundleNotificationId + 1;
//                    else
//                        singleNotificationId++;
//
//                    resultIntent = new Intent(this, MainActivity.class);
//                    resultIntent.putExtra("notification", "Single notification clicked");
//                    resultIntent.putExtra("notification_id", singleNotificationId);
//                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//                    NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "channel_id")
//                            .setGroup(bundle_notification_id)
//                            .setContentTitle("New Notification " + singleNotificationId)
//                            .setContentText("Content for the notification")
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setGroupSummary(false)
//                            .setContentIntent(resultPendingIntent);
//
//                    notificationManager.notify(singleNotificationId, notification.build());
//                    notificationManager.notify(bundleNotificationId, summaryNotificationBuilder.build());
//                    break;
//
//            }
//        }
//
//        @Override
//        protected void onNewIntent(Intent intent) {
//            super.onNewIntent(intent);
//
//            Bundle extras = intent.getExtras();
//            if (extras != null) {
//                int notification_id = extras.getInt("notification_id");
//                Toast.makeText(getApplicationContext(), "Notification with ID " + notification_id + " is cancelled", Toast.LENGTH_LONG).show();
//                notificationManager.cancel(notification_id);
//            }
//        }
//    }


}

