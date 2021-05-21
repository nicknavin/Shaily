package com.app.session.data.api;

import android.content.Context;
import android.text.TextUtils;

import com.androidquery.util.Constants;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;


import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Headers;


public class ApiClients
{
    public static final String BASE_URL = "http://sessionway.com/api/";


    private static Retrofit retrofit = null;
    private static int REQUEST_TIMEOUT = 60;
    private static OkHttpClient okHttpClient=null;
private static String token=null;

    public static Retrofit getClient(String accessToken)
    {

        token=accessToken;
        if (okHttpClient == null) {
            initOkHttp();
        }


//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
//
//
//            httpClient.addInterceptor(new Interceptor()
//            {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request().newBuilder()
//                            .addHeader("Content-Type", "application/json")
//                            .addHeader("Authorization", token).build();
//                    return chain.proceed(request);
//                }
//            });


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient  initOkHttp() {

        OkHttpClient.Builder httpClient=null;
         httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", token);


                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

       return okHttpClient = httpClient.build();

    }
    public static Retrofit getClient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
