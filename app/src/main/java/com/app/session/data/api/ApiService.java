package com.app.session.data.api;

import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.ReqSubscriptionStories;
import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.StoriesFollowingUsers;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscribedAllStroiesBody;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserRoot;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService
{

    @POST("userProfile/getStoriesOfFollowingUsers")
    public Single<SubscribedAllStroiesRoot> getStoriesFollowingUsers(@Body StoriesFollowingUsers storiesFollowingUsers);

    @POST("userProfile/getStoriesOfNonFollowingUsers")
    public Single<SubscribedAllStroiesRoot> getStoriesOfNonFollowingUsers(@Body StoriesFollowingUsers storiesFollowingUsers);


    @POST("userProfile/getSubscribedSubscriptionGroups")
    public Single<SubscriptionGroupRoot> reqGetSubscribedSubscriptionGroups(@Body UserId userId);

    @POST("users/getUserDetails")
    public Single<UserRoot> getUserDetails(@Body UserId userId);

    @POST("userProfile/getUserStories")
    public Single<StoryRoot> getUserStories(@Body ReqUserStory reqUserStory);

    @POST("userProfile/getUserSubscriptionGroups")
    public Single<SubscriptionGroupRoot> reqGetUserSubscriptionGroups(@Body UserId userId);


    @POST("userProfile/subscriptionStories")
    public Single<SubscriptionStoriesRoot> reqGetSubscriptionStories(@Body ReqSubscriptionStories reqSubscriptionStories);

    @POST("userProfile/deleteStory")
    public Single<Root> reqDeleteStory(@Body ReqDeleteStory reqDeleteStory);






}

