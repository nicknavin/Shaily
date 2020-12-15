package com.app.session.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClientNew {
    //http://sessionway.com/api/url
    public static final String BASE_URL = "http://sessionway.com/api/users/";
    public static final String BASE_PROFILE_URL = "http://sessionway.com/api/userProfile/";
    public static final String BASE_URL_File = "http://www.consultlot.com/";
    private static Retrofit retrofit = null;

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
    public static Retrofit getClientProfile() {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_PROFILE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



//    public static Retrofit getClientFile() {
//        if (retrofit==null)
//        {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL_File)
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
}
