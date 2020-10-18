package com.app.session.api;

public interface Urls {

    // API KEY
    String API_KEY = "123456";
    String DEVICE_ID = "123456";

    /*Base URL*/
//    String BASE = "http://expertlot.com/consultant/ConsultationServices.asmx/"; //production

    String BASE = "http://www.consultlot.com/consultant/ConsultationServices.asmx/"; //production

    public String BASE_URL = "http://consultlot.com/consultantService.svc/";


//    http://www.consultlot.com/consultantService.svc/get_currency_ref

    public String BASE_VIDEO_URL = "http://consultlot.com/video/users/";
    public String BASE_IMAGES_URL = "http://sessionway.com/";
    public String PRIVATEMESSAGE_URL = "http://sessionway.com/chat/";


    /*Common URLS*/
    String ADD_CATEGORY = BASE_URL+"add_category";
    String GETCATEGORYBYLANGUAGE = BASE+"get_category";
    String GETCATEGORY = BASE+"GetCategory";
    String GET_PROFILE = BASE+"get_profile";
    String GET_COUNTRY = BASE+"get_country";
    String GET_CATEGORY = BASE+"get_category";
    String GET_SUBCATEGORY = BASE+"get_subcategory";
    String REGISTRATIONUSER = BASE+"registrationUser";
    String USER_EXIST = BASE+"user_exist";
    String SEND_OTP = BASE+"send_otp";
    String CHECK_OTP = BASE+"check_otp";
    String USER_LOGIN = BASE+"user_login";
    String USER_LOGOUT = BASE+"user_logout";
    String USER_REGISTER = BASE+"user_register";
    String GET_LANGUAGES = BASE+"get_languages";
    String SET_USERS_LANGUAGES = BASE+"set_users_languages";
    String SET_CONSULTANT_SPECIALTIES = BASE+"set_consultant_specialties";
    String SET_CONSULTANT_CVS = BASE+"set_consultant_cvs";
    String SEARCH_CONSULTANT = BASE+"search_consultant";
    String GET_CONULTANT = BASE+"get_conultant";
    String VIDEO_AUDIO_CALLING = BASE+"video_audio_calling";
    String GET_CALLING_STATUS = BASE+"get_calling_status";
    String SET_CALLING_STATUS = BASE+"set_calling_status";
    String GET_USER_DETAIL = BASE+"get_user_detail";
    String UPDATE_CONSULTANT = BASE+"update_consultant";
    String UPDATE_IMAGE = BASE+"update_image";
    String UPDATE_BRIEF = BASE+"update_brief";
    String SET_USER_LOCATIONS = BASE+"set_user_locations";
    String GET_USER_LOCATIONS = BASE+"get_user_locations";
    String SET_USER_LANGUAGE = BASE+"set_user_language";
    String SEND_INVITITION = BASE+"send_invitition";
    String CANCEL_INVITITION = BASE+"cancel_invitition";
    String GET_INVITITION  = BASE+"get_invitition";
    String SEARCH_TEAM  = BASE+"search_team";
    String GET_TEAM  = BASE+"get_team";
    String INVITITION_CONFIRMATION  = BASE+"invitition_confirmation";
    String UPDATE_BANK_DETAIL  = BASE+"update_bank_detail";
    String REMOVE_TEAM_MEMBER  = BASE+"remove_team_member";
    String SET_REMARK  = BASE+"set_remark";
    String GET_REMARK  = BASE+"get_remark";
    String GET_TO_DO  = BASE+"get_to_do";
    String SET_TO_DO  = BASE+"set_to_do";
    String REQUEST_VIDEO_CALLING  = BASE+"request_video_calling";
    String REQUEST_AUDIO_CALLING  = BASE+"request_audio_calling";
    String GET_ALL_NOTIFICATION  = BASE+"get_all_notification";
    String SET_VIDEO_STATUS  = BASE+"set_video_status";
    String SET_AUDIO_STATUS  = BASE+"set_audio_status";
    String CHANG_PASSWORD  = BASE+"chang_password";
    String REMOVE_IMAGE=BASE+"remove_image";
    String CHANG_PREFRED_LANGUAGE=BASE+"chang_prefred_language";


    String SENT_OTP_CHANGE_EMAILID=BASE+"sent_otp_change_emailid";
    String SENT_MAIL_CHANGE_NO=BASE+"sent_mail_change_no";


    String SET_NEW_MOBILENO=BASE+"set_new_mobileno";
    String SET_NEW_EMAILID=BASE+"set_new_emailid";
    String UPDATE_USER_NAME=BASE+"update_user_name";
    String REMOVE_LANGUAGE=BASE+"remove_language";
    String REMOVE_CATEGORY=BASE+"remove_category";
    String REMOVE_BRIEF_CV=BASE+"remove_brief_cv";
    String GET_OTHER_USER_DETAIL=BASE+"get_other_user_detail";
    String USER_FOLLOW=BASE+"user_follow";
    String USER_UNFOLLOW=BASE+"user_unfollow";

    String GET_REGISTER_LANGUAGE=BASE+"get_register_language";
    String GET_LANGUAGE_SUBCATEGORY=BASE+"get_language_subcategory";
    String GET_FILTER_CONSULTANTS=BASE+"get_consultants";
    String GET_CONSULTANT_COUNTRY=BASE+"get_consultant_country";
    String SET_REMARK_AUDIO=BASE+"set_remark_audio";
    String DELETE_REMARK=BASE+"delete_remark";
    String FORGOT_PASSWORD=BASE+"forgot_password";
    String SET_NEW_PASSWORD=BASE+"set_new_password";
    String GET_MYSTORY=BASE+"get_mystory";
    String SEND_STORY=BASE+"send_story";
    String GET_UNSEEN_USER=BASE+"get_unseen_user";
    String GET_UNSEEN_STORY=BASE+"get_unseen_story";
    String CREATE_GROUP=BASE+"create_group";
    String GET_GROUP_LIST=BASE+"get_group_list";
    String DELETE_GROUP=BASE+"delete_group";
    String GET_GROUP_STORY=BASE+"get_group_story";
    String SEND_GROUP_STORY=BASE+"send_group_story";
    String DELETE_STORY=BASE+"delete_story";
    String GET_DEFAULT_STORY=BASE+"get_default_story";
    String UPLOAD_VIDEO=BASE_URL+"upload_video";
    String UPLOADIMAGE=BASE_URL+"uploadImage";
    String UPLOAD_IMAGE=BASE_URL+"upload_image";
    String GET_TITLE_REF=BASE_URL+"get_title_ref";
    String ADD_TITLE_REF=BASE_URL+"add_title_ref";
    String ADD_SUBSCRIPTION_GROUP=BASE_URL+"add_subscription_group";
    String GET_SUBSCRIPTION_GROUP=BASE_URL+"get_subscription_group";
    String UPLOAD_GROUP_VIDEO =BASE_URL+"upload_group_video";
    String UPLOAD_GROUP_IMAGE =BASE_URL+"upload_group_image";
    String UPDATE_GROUP_IMAGE =BASE_URL+"update_group_image";
    String UPADTE_SESSION_GROUP =BASE_URL+"upadte_session_group";
    String UPDATE_GROUP_THUMBNAIL =BASE_URL+"update_group_thumbnail";
    String SUBSCRIPTION_SEND_STORY =BASE_URL+"send_story";
    String SUBSCRIPTION_GET_STORY =BASE_URL+"get_story";
    String SUBSCRIPTION_DELETE_STORY =BASE_URL+"delete_story";
    String DELETE_MY_ACCOUNT =BASE_URL+"delete_my_account";
    String GET_CONSULTANT_CAT =BASE_URL+"get_consultant_cat";
    String GET_CONSULTANT =BASE_URL+"get_consultant";
    String GET_LANG_CATEGORY =BASE_URL+"get_lang_category";
    String GET_STORY_ALL  =BASE_URL+"get_story_all";











}
