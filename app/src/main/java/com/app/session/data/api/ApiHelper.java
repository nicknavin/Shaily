package com.app.session.data.api;

import com.app.session.data.model.StoriesFollowingUsers;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.network.ApiClient;

import io.reactivex.Single;
import okhttp3.ResponseBody;

public class ApiHelper
{
    ApiService apiService;
    public ApiHelper(ApiService service)
    {
        this.apiService=service;
    }


}
