package com.app.session.service;

import com.app.session.model.SendStoryResponseRoot;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created on : Feb 25, 2019
 * Author     : AndroidWave
 */
public interface RestApiService {


    @Multipart
    @POST("updateBriefVideo")
    Single<ResponseBody> onFileUpload(@Header("Authorization") String token, @Part("user_id") RequestBody user_cd,
                                      @Part("language_id") RequestBody language_id, @Part MultipartBody.Part file);


    @Multipart
    @POST("sendStory")
    public Single<ResponseBody>reqSendsStory(@Header("Authorization") String token,
                                                    @Part("user_id") RequestBody user_cd,
                                                    @Part("subscription_id") RequestBody subscription_id,
                                                    @Part("story_type") RequestBody story_type,
                                                    @Part("story_title") RequestBody story_title,
                                                    @Part("story_text") RequestBody story_text,
                                                    @Part MultipartBody.Part storyImage,
                                                    @Part MultipartBody.Part thumb
    );
    @Multipart
    @POST("sendStory")
    public Single<ResponseBody> reqUserSendsStory(@Header("Authorization") String token,
                                                         @Part("user_id") RequestBody user_cd,
                                                         @Part("story_type") RequestBody story_type,
                                                         @Part("story_title") RequestBody story_title,
                                                         @Part("story_text") RequestBody story_text,
                                                         @Part MultipartBody.Part storyImage,
                                                         @Part MultipartBody.Part thumb
    );




    @Multipart
    @POST("sendStory")
    public Single<ResponseBody> reqUserSendsStoryImage(@Header("Authorization") String token,
                                                         @Part("user_id") RequestBody user_cd,
                                                         @Part("story_type") RequestBody story_type,
                                                         @Part("story_title") RequestBody story_title,
                                                         @Part("story_text") RequestBody story_text,
                                                         @Part("video_url") RequestBody video_url,
                                                         @Part MultipartBody.Part storyImage
    );

    @Multipart
    @POST("sendStory")
    public Single<ResponseBody> reqUserSendsStoryImage1(@Header("Authorization") String token,
                                                      @Part("user_id") RequestBody user_cd,
                                                      @Part("subscription_id") RequestBody subscription_id,
                                                      @Part("story_type") RequestBody story_type,
                                                      @Part("story_title") RequestBody story_title,
                                                      @Part("story_text") RequestBody story_text,
                                                      @Part("video_url") RequestBody video_url,
                                                      @Part MultipartBody.Part storyImage
    );






    /*..................................Send File..............................*/


    @Multipart
    @POST("sendAnyDoc")
    public Single<ResponseBody> callUserSendStoryDocFile(@Header("Authorization") String token,
                                                         @Part("user_id") RequestBody user_cd,
                                                         @Part("story_type") RequestBody story_type,
                                                         @Part("story_title") RequestBody story_title,
                                                         @Part("story_text") RequestBody story_text,
                                                         @Part("doc_name") RequestBody doc_name,
                                                         @Part MultipartBody.Part docFile
    );



    @Multipart
    @POST("sendAnyDoc")
    public Single<ResponseBody> callSendStoryDocFile(@Header("Authorization") String token,
                                                            @Part("user_id") RequestBody user_cd,
                                                            @Part("subscription_id") RequestBody subscription_id,
                                                            @Part("story_type") RequestBody story_type,
                                                            @Part("story_title") RequestBody story_title,
                                                            @Part("story_text") RequestBody story_text,
                                                            @Part("doc_name") RequestBody doc_name,
                                                            @Part MultipartBody.Part docFile
    );


    /*...........................................Send Audio file..........................................................*/

    @Multipart
    @POST("sendStory")
    public Single<ResponseBody> callSendAudioStory1(@Header("Authorization") String token,
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
    public Single<ResponseBody> callUserSendAudioStory1(@Header("Authorization") String token,
                                                          @Part("user_id") RequestBody user_cd,
                                                          @Part("story_type") RequestBody story_type,
                                                          @Part("story_title") RequestBody story_title,
                                                          @Part("story_text") RequestBody story_text,
                                                          @Part("video_url") RequestBody video_url,
                                                          @Part MultipartBody.Part storyImage
    );




    @Multipart
    @POST("chatFileUpload")
    public Single<ResponseBody> reqSendMsg(@Header("Authorization") String token,
                                                  @Part("senderId") RequestBody senderId,
                                                  @Part("senderName") RequestBody senderName,
                                                  @Part("senderProfileUrl") RequestBody senderProfileUrl,
                                                  @Part("reciverId") RequestBody reciverId,
                                                  @Part("reciverName") RequestBody reciverName,
                                                  @Part("reciverProfileUrl") RequestBody reciverProfileUrl,
                                                  @Part("messageType") RequestBody messageType,
                                                  @Part("displayFileName") RequestBody displayFileName,
                                                  @Part("durationTime") RequestBody durationTime,
                                                  @Part MultipartBody.Part chatFile
    );






}
