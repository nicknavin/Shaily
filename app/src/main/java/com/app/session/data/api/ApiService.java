package com.app.session.data.api;

import com.app.session.data.model.AddSubscription;
import com.app.session.data.model.PurchaseSubscribeGroupRoot;
import com.app.session.data.model.ReqDeleteGroupStory;
import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.ReqPurchaseSubscriptionGroups;
import com.app.session.data.model.ReqSubscriptionStories;
import com.app.session.data.model.ReqUnSubscribeAfter;
import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.RequestReadCount;
import com.app.session.data.model.RequestReadCountNormalStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.StoriesFollowingUsers;
import com.app.session.data.model.StoryId;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscribedAllStroiesBody;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.UnDoUnSubscribe;
import com.app.session.data.model.UndoSubscriptionRoot;
import com.app.session.data.model.UnsubscribedRoot;
import com.app.session.data.model.UpdateSubscriptionGroupDetail;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserRoot;
import com.app.session.data.model.UserSubscriptionGroupsRoot;
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
    public Single<PurchaseSubscribeGroupRoot> reqGetSubscribedSubscriptionGroups(@Body UserId userId);

    @POST("users/getUserDetails")
    public Single<UserRoot> getUserDetails(@Body UserId userId);

    @POST("userProfile/getUserStories")
    public Single<StoryRoot> getUserStories(@Body ReqUserStory reqUserStory);

    @POST("userProfile/getUserSubscriptionGroups")
    public Single<SubscriptionGroupRoot> reqGetUserSubscriptionGroups(@Body UserId userId);


    @POST("userProfile/subscriptionStories")
    public Single<SubscriptionStoriesRoot> reqGetSubscriptionStories(@Body ReqSubscriptionStories reqSubscriptionStories);

   @POST("explore/readsCount")
    public Single<ResponseBody> reqReadCount(@Body RequestReadCount requestReadCount);

    @POST("explore/readsCount")
    public Single<ResponseBody> reqReadCountNormalStory(@Body RequestReadCountNormalStory requestReadCountNormalStory);

    @POST("userProfile/deleteStory")
    public Single<Root> reqDeleteStory(@Body ReqDeleteGroupStory reqDeleteStory);


    @POST("userProfile/getUserSubscriptionGroups")
    public Single<UserSubscriptionGroupsRoot>getUserSubscriptionGroups(@Body UserId  userId);

    @POST("explore/userSubscriptionGroups")
    public Single<UserSubscriptionGroupsRoot>userSubscriptionGroupsForUnRegister(@Body UserId  userId);

    @POST("userProfile/updateSubscription")
    public Single<Root>updateSubscriptionGroupDetail(@Body UpdateSubscriptionGroupDetail groupDetail);

    @POST("userProfile/purchaseSubscriptionGroups")
    public Single<Root>purchaseSubscribeGroup(@Body ReqPurchaseSubscriptionGroups reqPurchaseSubscriptionGroups);

    @POST("explore/unSubscribe")
    public Single<UnsubscribedRoot>unSubscribeAfter24(@Body ReqUnSubscribeAfter reqUnSubscribeAfter);

    @POST("explore/unDoUnSubscribe24")
    public Single<UndoSubscriptionRoot>unDoUnSubscribe24(@Body UnDoUnSubscribe unDoUnSubscribe);


    @POST("explore/viewsCount")
    public Single<Root>viewCount(@Body StoryId storyId);






}

