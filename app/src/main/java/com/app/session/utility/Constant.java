package com.app.session.utility;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;

public class Constant {

    public static final String CHAT_SERVER_URL = "https://www.sessionway.com/";
    public static final String NEW_MESSAGE = "new_message";
    public static  String IS_TYPING = "isTyping";
    public static final String TYPING = "typing";





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

    public static final String CUSTOME_BROADCAST = "com.app.sessionway";
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

}

