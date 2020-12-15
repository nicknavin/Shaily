package com.app.session.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitInstance {

    private static Retrofit retrofit = null;

    public static RestApiService getApiService()
    {

        if (retrofit == null) {

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl("http://sessionway.com/api/users/")
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
                    .baseUrl("http://sessionway.com/api/userProfile/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

        return retrofit.create(RestApiService.class);

    }
    public static RestApiService getApiChat()
    {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }).build();

        if (retrofit == null) {

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl("http://sessionway.com/api/chat/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

        return retrofit.create(RestApiService.class);

    }



}
