package com.app.session.data.repository;

import android.app.Application;
import android.content.Context;

import com.app.session.data.api.ApiClients;
import com.app.session.data.api.ApiService;
import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.ReqSubscriptionStories;
import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.StoriesFollowingUsers;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserRoot;

import io.reactivex.Single;
import retrofit2.http.Body;


public class MainRepository {

    private ApiService apiService;
    private static MainRepository mainRepository;

    public static MainRepository getInstance(Context cxt)
    {
        if (mainRepository == null)
        {
            mainRepository = new MainRepository(cxt);

        }
        return mainRepository;
    }

    public MainRepository(Context context) {
        apiService = ApiClients.getClient(context).create(ApiService.class);
    }



    public Single<SubscribedAllStroiesRoot> getStoriesOFFollowingUser(StoriesFollowingUsers storiesFollowingUsers)
    {
        return apiService.getStoriesFollowingUsers(storiesFollowingUsers);

    }
    public Single<SubscribedAllStroiesRoot> getStoriesOFuNFollowingUser(StoriesFollowingUsers storiesFollowingUsers)
    {
        return apiService.getStoriesOfNonFollowingUsers(storiesFollowingUsers);

    }


    public Single<SubscriptionGroupRoot> getSubscribedSubscriptionGroups(UserId userId)
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

    public  Single<Root> deleteStory(ReqDeleteStory deleteStory)
    {
        return apiService.reqDeleteStory(deleteStory);
    }

}
