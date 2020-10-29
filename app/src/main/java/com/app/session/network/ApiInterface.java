package com.app.session.network;


import com.app.session.model.AddBriefCv;
import com.app.session.model.AddCategoryRoot;
import com.app.session.model.AllConsultUser;
import com.app.session.model.AllUserRoot;
import com.app.session.model.CategoryBody;
import com.app.session.model.CategoryRoot;
import com.app.session.model.ChatUserId;
import com.app.session.model.ChatedPersonsResponse;
import com.app.session.model.ClearChat;
import com.app.session.model.ConsultUser;
import com.app.session.model.ConsultUserRoot;
import com.app.session.model.ConsultantData;
import com.app.session.model.CurrencyRefResponse;
import com.app.session.model.CurrencyRoot;
import com.app.session.model.DeleteLangReq;
import com.app.session.model.LanguageCd;
import com.app.session.model.LanguagesRoot;
import com.app.session.model.LoadMessage;
import com.app.session.model.LoginRoot;
import com.app.session.model.OtherUserId;
import com.app.session.model.PersonalUserStory;
import com.app.session.model.ProfileUpdateReq;
import com.app.session.model.ReqAllUser;
import com.app.session.model.ReqCategory;
import com.app.session.model.ReqDeleteCategory;
import com.app.session.model.ReqDeleteStory;
import com.app.session.model.ReqFollowUser;
import com.app.session.model.ReqGetUser;
import com.app.session.model.ReqMessageRead;
import com.app.session.model.ReqStory;
import com.app.session.model.ReqSubscribeGroupStories;
import com.app.session.model.ReqSubscriptionStories;
import com.app.session.model.ReqUpdateBank;
import com.app.session.model.ReqUserProfile;
import com.app.session.model.RequestUpdateCategory;
import com.app.session.model.Root;
import com.app.session.model.RootChatMessage;
import com.app.session.model.RootFollowers;
import com.app.session.model.SearchUser;
import com.app.session.model.SendStoryResponseRoot;
import com.app.session.model.StoryId;
import com.app.session.model.StoryRoot;
import com.app.session.model.SubscribedAllStroiesRoot;
import com.app.session.model.SubscriptionGroupDetailRoot;
import com.app.session.model.SubscriptionGroupRoot;
import com.app.session.model.SubscriptionStoriesRoot;
import com.app.session.model.UpdateBriefReq;
import com.app.session.model.UserId;
import com.app.session.model.UserProfileRoot;
import com.app.session.model.UserRoot;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("user_login")
    public Call<ResponseBody> callLoginRequest(@Field("user_id") String user_id, @Field("password") String password, @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("user_logout")
    public Call<ResponseBody> callLogoutRequest(@Field("user_id") String user_id, @Field("token_id") String token_id);

    @FormUrlEncoded
    @POST("user_exist")
    public Call<ResponseBody> callUserExist(@Field("user_id") String user_id, @Field("email_id") String email_id);

    @FormUrlEncoded
    @POST("get_user_detail")
    public Call<ResponseBody> getUserDetailRequest(@Field("user_cd") String user_cd, @Field("token_id") String token_id);


    @POST("get_languages")
    public Call<ResponseBody> getLanguage();

    @FormUrlEncoded
    @POST("set_user_language")
    public Call<ResponseBody> callUpdateLanguage(@Field("user_cd") String user_cd, @Field("token_id") String token_id, @Field("language_cd") String language_cd);

    @FormUrlEncoded
    @POST("get_country")
    public Call<ResponseBody> getCountry(@Field("Language") String Language);

    @FormUrlEncoded
    @POST("check_otp")
    public Call<ResponseBody> callCheckOTP(@Field("mobile_no") String mobile_no, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("send_otp")
    public Call<ResponseBody> callSendOTP(@Field("mobile_no") String mobile_no, @Field("dial_cd") String dial_cd);

    @FormUrlEncoded
    @POST("user_register")
    public Call<ResponseBody> callUserRegister(@Field("email_id") String email_id,
                                               @Field("user_id") String user_id,
                                               @Field("mobile_no") String mobile_no,
                                               @Field("user_password") String user_password,
                                               @Field("user_name") String user_name,
                                               @Field("country_cd") String country_cd,
                                               @Field("is_company") String is_company,
                                               @Field("consultant") String consultant,
                                               @Field("language_cd") String language_cd,
                                               @Field("category_cd") String category_cd,
                                               @Field("gender") String gender,
                                               @Field("brief_cv") String brief_cv,
                                               @Field("myBase64String") String myBase64String
    );

    @FormUrlEncoded
    @POST("get_category")
    public Call<ResponseBody> getCategory(@Field("Language_cd") String Language_cd);

    @FormUrlEncoded
    @POST("set_consultant_specialties")
    public Call<ResponseBody> callUpdateCategory(@Field("user_cd") String user_cd, @Field("token_id") String token_id, @Field("category_cd") String category_cd, @Field("country_cd") String country_cd);

    @FormUrlEncoded
    @POST("remove_category")
    public Call<ResponseBody> callRemoveCategory(@Field("user_cd") String user_cd, @Field("token_id") String token_id, @Field("category_cd") String category_cd);


    @FormUrlEncoded
    @POST("update_brief")
    public Call<ResponseBody> callUpdateBrief(@Field("user_cd") String user_cd, @Field("token_id") String token_id, @Field("brief_cv") String brief_cv, @Field("title_name") String title_name, @Field("language_cd") String language_cd);

    @FormUrlEncoded
    @POST("update_image")
    public Call<ResponseBody> callUpdateImage(@Field("user_cd") String user_cd, @Field("token_id") String token_id, @Field("myBase64String") String myBase64String);

    @FormUrlEncoded
    @POST("remove_image")
    public Call<ResponseBody> callRemoveImage(@Field("user_cd") String user_cd, @Field("token_id") String token_id);

    @FormUrlEncoded
    @POST("update_user_name")
    public Call<ResponseBody> callUpdateUserName(@Field("user_cd") String user_cd, @Field("token_id") String token_id, @Field("user_name") String user_name, @Field("gender") String gender);

    @POST("get_currency_ref")
    public Call<CurrencyRefResponse> GetCurrencyRef();

    @POST("get_subscription_group")
    public Call<ResponseBody> GetSubscriptionGroup();


    @FormUrlEncoded
    @POST("get_unseen_user")
    public Call<ResponseBody> getUnseenUser(@Field("user_cd") String user_cd, @Field("token_id") String token_id);

    @FormUrlEncoded
    @POST("get_default_story")
    public Call<ResponseBody> getDefaultStory(@Field("user_cd") String user_cd, @Field("token_id") String token_id);


    @FormUrlEncoded
    @POST("get_mystory")
    public Call<ResponseBody> getMyStory(@Field("user_cd") String user_cd, @Field("token_id") String token_id);

    @Multipart
    @POST("imageuploadHandler.ashx")
    public Call<ResponseBody> sendMyStoryImg(@Query("name") String name, @Part MultipartBody.Part file);


    @Multipart
    @POST("set_remark_audio")
    public Call<ResponseBody> callSendStory(@Part("user_cd") RequestBody user_cd,
                                            @Part("token_id") RequestBody token_id,
                                            @Part("story_type") RequestBody story_type,
                                            @Part("story_title") RequestBody story_title,
                                            @Part("story_time") RequestBody story_time,
                                            @Part("story_text") RequestBody story_text,
                                            @Part("thumbnail_text") RequestBody thumbnail_text,
                                            @Part("subscription_group_cd") RequestBody subscription_group_cd,
                                            @Part("language_cd") RequestBody language_cd,
                                            @Part MultipartBody.Part storyImage
    );




    /*---------------------------------------------New API--------------------------------------------------------------------*/


    @Headers("Content-Type: application/json")
    @POST("userLogin")
    public Call<LoginRoot> mylogin(@Body JsonObject jsonObject);


    @Headers("Content-Type: application/json")
    @POST("userRegister")
    public Call<Root> userRegistration(@Body JsonObject jsonObject);


    @Headers("Content-Type: application/json")
    @POST("userVerify")
    public Call<Root> userVerify(@Body JsonObject jsonObject);


    @GET("getLanguages")
    public Call<LanguagesRoot> getLanguages();


    @GET("getCountries")
    public Call<ResponseBody> getCountries();

    @Headers("Content-Type: application/json")
    @POST("userMobileVerify")
    public Call<Root> userMobileVerify(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("getCategories")
    public Call<ResponseBody> getCategories(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("getCategories")
    public Call<ResponseBody> reqGetCategories(@Body ReqCategory jsonObject);

    @Headers("Content-Type: application/json")
    @POST("addCategories")
    public Call<AddCategoryRoot> reqAddCategories(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("getUserDetails")
    public Call<UserRoot> getUserDetails(@Header("Authorization") String token, @Body UserId userId);


    @Headers("Content-Type: application/json")
    @POST("getUserProfile")
    public Call<UserProfileRoot> reqGetUserProfile(@Body ReqUserProfile reqUserProfile);


    @Headers("Content-Type: application/json")
    @POST("getUserSubscriptionGroups")
    public Call<SubscriptionGroupRoot> reqGetUserSubscriptionGroups(@Header("Authorization") String token, @Body UserId jsonObject);

    @Headers("Content-Type: application/json")
    @POST("upateUserLanguage")
    public Call<ResponseBody> reqUpateUserLanguage(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("upateUserProfile")
    public Call<Root> reqUpateUserProfile(@Header("Authorization") String token, @Body ProfileUpdateReq jsonObject);


    @Headers("Content-Type: application/json")
    @POST("upateUserBankDetails")
    public Call<Root> reqUpateUserBankDetails(@Header("Authorization") String token, @Body ReqUpdateBank jsonObject);

    @Multipart
    @POST("updateProfileImage")
    public Call<ResponseBody> reqUpdateProfileImage(@Header("Authorization") String token, @Part("user_id") RequestBody user_cd,
                                                    @Part MultipartBody.Part storyImage
    );

    @GET("getCurrencies")
    public Call<CurrencyRoot> reqGetCurrencies();


    @Multipart
    @POST("addSubscription")
    public Call<ResponseBody> reqAddSubscription(@Header("Authorization") String token,
                                                 @Part("user_id") RequestBody user_cd,
                                                 @Part("language_id") RequestBody language_id,
                                                 @Part("category_id") RequestBody category_id,
                                                 @Part("currency_id") RequestBody currency_id,
                                                 @Part("subscription_price") RequestBody subscription_price,
                                                 @Part("group_description") RequestBody group_description,
                                                 @Part("group_name") RequestBody group_name,
                                                 @Part MultipartBody.Part storyImage
    );


    @Headers("Content-Type: application/json")
    @POST("getSubscriptionGroup")
    public Call<SubscriptionGroupRoot> reqGetSubscriptionGroup(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("updateBriefCv")
    public Call<ResponseBody> reqUpdateBriefCv(@Header("Authorization") String token, @Body UpdateBriefReq jsonObject);


    @Headers("Content-Type: application/json")
    @POST("deleteUserLanguage")
    public Call<ResponseBody> reqDeleteUserLanguage(@Header("Authorization") String token, @Body DeleteLangReq jsonObject);

    @Headers("Content-Type: application/json")
    @POST("getAllStroies")
    public Call<StoryRoot> reqGetAllStroies(@Header("Authorization") String token, @Body ReqStory userId);


    @Headers("Content-Type: application/json")
    @POST("categories")
    public Call<CategoryRoot> reqGetCategories(@Header("Authorization") String token, @Body LanguageCd languageCd);


    @Headers("Content-Type: application/json")
    @POST("upateUserCategory")
    public Call<Root> reqUpateUserCategory(@Header("Authorization") String token, @Body RequestUpdateCategory requestUpdateCategory);

    @Headers("Content-Type: application/json")
    @POST("getUsers")
    public Call<ConsultUserRoot> reqGetUsers(@Header("Authorization") String token, @Body ReqGetUser reqGetUser);


    @Headers("Content-Type: application/json")
    @POST("getAllUsers")
    public Call<ConsultUserRoot> reqGetAllUsers(@Header("Authorization") String token, @Body ReqAllUser reqAllUser);

    @Headers("Content-Type: application/json")
    @POST("deleteStory")
    public Call<Root> reqDeleteStory(@Header("Authorization") String token, @Body ReqDeleteStory reqDeleteStory);

    @Multipart
    @POST("updateBriefThumbnail")
    public Call<ResponseBody> reqUpdateBriefThumbnail(@Header("Authorization") String token,
                                                      @Part("user_id") RequestBody user_cd,
                                                      @Part("language_id") RequestBody language_id,
                                                      @Part MultipartBody.Part storyImage
    );


    @Multipart
    @POST("sendStory")
    public Call<SendStoryResponseRoot> reqSendsStory(@Header("Authorization") String token,
                                                     @Part("user_id") RequestBody user_cd,
                                                     @Part("subscription_id") RequestBody subscription_id,
                                                     @Part("story_type") RequestBody story_type,
                                                     @Part("story_title") RequestBody story_title,
                                                     @Part("story_text") RequestBody story_text,
                                                     @Part("video_url") RequestBody video_url,
                                                     @Part MultipartBody.Part storyImage
    );

    @Multipart
    @POST("sendStory")
    public Call<SendStoryResponseRoot> reqUserSendsStory(@Header("Authorization") String token,
                                                         @Part("user_id") RequestBody user_cd,
                                                         @Part("story_type") RequestBody story_type,
                                                         @Part("story_title") RequestBody story_title,
                                                         @Part("story_text") RequestBody story_text,
                                                         @Part("video_url") RequestBody video_url,
                                                         @Part MultipartBody.Part storyImage
    );

    @Multipart
    @POST("sendStory")
    public Call<SendStoryResponseRoot> callSendStory1(@Header("Authorization") String token,
                                                      @Part("user_id") RequestBody user_cd,
                                                      @Part("subscription_id") RequestBody subscription_id,
                                                      @Part("story_type") RequestBody story_type,
                                                      @Part("story_title") RequestBody story_title,
                                                      @Part("story_text") RequestBody story_text,
                                                      @Part("video_url") RequestBody video_url,
                                                      @Part MultipartBody.Part storyImage
    );

    @Multipart
    @POST("sendStory")
    public Call<SendStoryResponseRoot> callUserSendStory1(@Header("Authorization") String token,
                                                          @Part("user_id") RequestBody user_cd,

                                                          @Part("story_type") RequestBody story_type,
                                                          @Part("story_title") RequestBody story_title,
                                                          @Part("story_text") RequestBody story_text,
                                                          @Part("video_url") RequestBody video_url,
                                                          @Part MultipartBody.Part storyImage
    );



    /*..................................Send File..............................*/


    @Multipart
    @POST("sendAnyDoc")
    public Call<SendStoryResponseRoot> callSendStoryDocFile(@Header("Authorization") String token,
                                                            @Part("user_id") RequestBody user_cd,
                                                            @Part("subscription_id") RequestBody subscription_id,
                                                            @Part("story_type") RequestBody story_type,
                                                            @Part("story_title") RequestBody story_title,
                                                            @Part("story_text") RequestBody story_text,
                                                            @Part("doc_name") RequestBody doc_name,
                                                            @Part MultipartBody.Part docFile
    );

    @Multipart
    @POST("sendAnyDoc")
    public Call<SendStoryResponseRoot> callUserSendStoryDocFile(@Header("Authorization") String token,
                                                                @Part("user_id") RequestBody user_cd,
                                                                @Part("story_type") RequestBody story_type,
                                                                @Part("story_title") RequestBody story_title,
                                                                @Part("story_text") RequestBody story_text,
                                                                @Part("doc_name") RequestBody doc_name,
                                                                @Part MultipartBody.Part docFile
    );


    @Headers("Content-Type: application/json")
    @POST("userFollow")
    public Call<Root> reqsendUserFollow(@Header("Authorization") String token, @Body ReqFollowUser reqFollowUser);

    @Headers("Content-Type: application/json")
    @POST("UnFollow")
    public Call<Root> reqsendUserUnFollow(@Header("Authorization") String token, @Body ReqFollowUser reqFollowUser);

    @Headers("Content-Type: application/json")
    @POST("followers")
    public Call<RootFollowers> reqFollowers(@Header("Authorization") String token, @Body UserId userId);

    @Headers("Content-Type: application/json")
    @POST("getSubscribedSubscriptionGroups")
    public Call<SubscriptionGroupRoot> reqGetSubscribedSubscriptionGroups(@Header("Authorization") String token, @Body UserId reqFollowUser);


    //sessionway.com/userProfile/getuserSubscribedAllStroies
    @Headers("Content-Type: application/json")
    @POST("getuserSubscribedAllStroies")
    public Call<SubscribedAllStroiesRoot> reqGetuserSubscribedAllStroies(@Header("Authorization") String token, @Body ReqSubscribeGroupStories reqFollowUser);


    @Headers("Content-Type: application/json")
    @POST("subscriptionStories")
    public Call<SubscriptionStoriesRoot> reqGetSubscriptionStories(@Header("Authorization") String token, @Body ReqSubscriptionStories reqSubscriptionStories);

    @Headers("Content-Type: application/json")
    @POST("getAllUserByMachineLang")
    public Call<ConsultUserRoot> reqGetAllUserByMachineLang(@Header("Authorization") String token, @Body LanguageCd languageCd);

    @Headers("Content-Type: application/json")
    @POST("searchUser")
    public Call<ConsultUserRoot> reqSearchUser(@Body SearchUser searchUser);

    @Headers("Content-Type: application/json")
    @POST("getPersonProfile")
    public Call<UserProfileRoot> reqGetPersonProfile(@Body OtherUserId otherUserId);


    @Headers("Content-Type: application/json")
    @POST("viewsCount")
    public Call<Root> reqSendViewsCount(@Body StoryId storyId);

    @Headers("Content-Type: application/json")
    @POST("addBriefCv")
    public Call<Root> reqAddBriefCv(@Header("Authorization") String token, @Body AddBriefCv addBriefCv);

    @Headers("Content-Type: application/json")
    @POST("deleteUserCategory")
    public Call<Root> reqDeleteUserCategory(@Header("Authorization") String token, @Body ReqDeleteCategory reqDeleteCategory);

    @Headers("Content-Type: application/json")
    @POST("getPersonAllStrories")
    public Call<StoryRoot> reqGetPersonAllStrories(@Body PersonalUserStory personalUserStory);

    //sessionway.com/chat/privateMessage
    @Headers("Content-Type: application/json")
    @POST("privateMessage")
    public Call<RootChatMessage> reqLoadPreviousMsg(@Header("Authorization") String token, @Body LoadMessage loadMessage);

    @Headers("Content-Type: application/json")
    @POST("chatedPersons")
    public Call<ChatedPersonsResponse> reqChatedPersons(@Header("Authorization") String token, @Body ChatUserId userId);

    @Headers("Content-Type: application/json")
    @POST("clearChat")
    public Call<Root> reqClearChat(@Header("Authorization") String token, @Body ClearChat clearChat);

    @Headers("Content-Type: application/json")
    @POST("messageRead")
    public Call<Root> reqReadMsg(@Header("Authorization") String token, @Body ReqMessageRead reqMessageRead);




    @GET("format=json")
    public Call<ResponseBody> getYoutube();


}



