package com.app.session.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created on : Feb 25, 2019
 * Author     : AndroidWave
 */
public class RetrofitInstance {

    private static Retrofit retrofit = null;

    public static RestApiService getApiService()
    {


        if (retrofit == null) {

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl("http://sessionway.com/users/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

        return retrofit.create(RestApiService.class);

    }
    public static RestApiService getApiStoryService()
    {


        if (retrofit == null) {

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl("http://sessionway.com/userProfile/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

        return retrofit.create(RestApiService.class);

    }

}
