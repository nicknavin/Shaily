package com.app.session.data.repository;

import android.app.Application;
import android.content.Context;

import com.app.session.data.api.ApiClients;
import com.app.session.data.api.ApiService;
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
import com.app.session.data.model.StoryRoot;
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

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;


public class MainRepository {

    private ApiService apiService;
    //private static MainRepository mainRepository;

//    public static MainRepository getInstance(Context cxt)
//    {
//        if (mainRepository == null)
//        {
//            mainRepository = new MainRepository(cxt);
//
//        }
//        return mainRepository;
//    }

    public MainRepository(String token)
    {

            apiService = ApiClients.getClient(token).create(ApiService.class);
//        }
//        else
//        {
//            apiService=ApiClients.getClient().create(ApiService.class);
//        }
    }



    public Single<SubscribedAllStroiesRoot> getStoriesOFFollowingUser(StoriesFollowingUsers storiesFollowingUsers)
    {
        return apiService.getStoriesFollowingUsers(storiesFollowingUsers);

    }
    public Single<SubscribedAllStroiesRoot> getStoriesOFuNFollowingUser(StoriesFollowingUsers storiesFollowingUsers)
    {
        return apiService.getStoriesOfNonFollowingUsers(storiesFollowingUsers);

    }


    public Single<PurchaseSubscribeGroupRoot> getSubscribedSubscriptionGroups(UserId userId)
    {
        return apiService.reqGetSubscribedSubscriptionGroups(userId);

    }

    public Single<UserRoot> getUserDetails(UserId userId) {
        return apiService.getUserDetails(userId);
    }

    public Single<StoryRoot> getUserStory(ReqUserStory reqUserStory)
    {
        return apiService.getUserStories(reqUserStory);
    }

    public Single<SubscriptionGroupRoot> getUserSubscriptionGroups(UserId userId)
    {
        return apiService.reqGetUserSubscriptionGroups(userId);
    }

   public Single<SubscriptionStoriesRoot> getSubscriptionStories(ReqSubscriptionStories reqSubscriptionStories)
    {
        return apiService.reqGetSubscriptionStories(reqSubscriptionStories);
    }
    public Single<ResponseBody> readCount(RequestReadCount requestReadCount)
    {
        return apiService.reqReadCount(requestReadCount);
    }
    public Single<ResponseBody> readCountNormalStory(RequestReadCountNormalStory story)
    {
        return apiService.reqReadCountNormalStory(story);
    }

    public  Single<Root> deleteStory(ReqDeleteGroupStory deleteStory)
    {
        return apiService.reqDeleteStory(deleteStory);
    }


    public Single<UserSubscriptionGroupsRoot> getOtherSubscriptionGroups(UserId userId)
    {
        return apiService.getUserSubscriptionGroups(userId);
    }
  public Single<UserSubscriptionGroupsRoot> userSubscriptionGroupsForUnRegister(UserId userId)
    {
        return apiService.userSubscriptionGroupsForUnRegister(userId);
    }


    public Single<Root> updateSubscriptionGroup(UpdateSubscriptionGroupDetail groupDetail)
    {
        return apiService.updateSubscriptionGroupDetail(groupDetail);
    }

    public Single<Root> purchaseSubscribeGroup(ReqPurchaseSubscriptionGroups reqPurchaseSubscriptionGroups)
    {
        return apiService.purchaseSubscribeGroup(reqPurchaseSubscriptionGroups);
    }


    public Single<UnsubscribedRoot> unSubscribeAfter24(ReqUnSubscribeAfter reqUnSubscribeAfter)
    {
        return apiService.unSubscribeAfter24(reqUnSubscribeAfter);
    }
public Single<UndoSubscriptionRoot> unDoUnSubscribe24(UnDoUnSubscribe unDoUnSubscribe)
    {
        return apiService.unDoUnSubscribe24(unDoUnSubscribe);
    }



}
