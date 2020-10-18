package com.app.session.network;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Navin Nimade on 14/11/16.
 */
public class ApiCalls {

    OkHttpClient client = getClient();

    private OkHttpClient getClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        return client;
    }

    public static final MediaType JSON
            = MediaType.parse("text/plain; charset=utf-8");

    //http://sessionway.com/ServiceIIP.svc/get_masterdata
    //Response{protocol=http/1.1, code=404, message=Not Found, url=http://sessionway.com/ServiceIIP.svc/get_masterdata}

    //json data send
    public String callAPIPost(String url, String objectstring) throws IOException
    {
        RequestBody body = RequestBody.create(JSON, objectstring);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String callAPIPostHashmap(String url, Map<String, Object> hashMap)
    {
        try {

            OkHttpClient client = new OkHttpClient();
            client.connectTimeoutMillis(); // connect timeout
            client.readTimeoutMillis();
            //client.readTimeout(30, TimeUnit.SECONDS);

            Request.Builder builder=new Request.Builder().url(url);
            if(hashMap!=null) {
				/*FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
				Set<String> keyset=hashMap.keySet();
				for (String key:keyset) {
					formEncodingBuilder.add(key, hashMap.get(key));
				}
				RequestBody formBody = formEncodingBuilder.build();*/
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                // Add Params to Builder
                for ( Map.Entry<String, Object> entry : hashMap.entrySet() ) {
                    formBodyBuilder.add( entry.getKey(), entry.getValue().toString() );
                }
                RequestBody formBody = formBodyBuilder.build();
                builder.post(formBody);
            }
            Request request = builder.build();
            Response response = client.newCall(request).execute();
             String result=response.body().string();
            return result;
        }
        catch (Exception ex)
        {
            return ex.toString();
        }
    }

    public String uploadMedia(String url, File file, String user_i_cd, String user_type_cd, String company_cd) throws IOException
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .addFormDataPart("user_i_cd", user_i_cd)
                .addFormDataPart("user_type_cd", user_type_cd)
                .addFormDataPart("company_cd", company_cd)
                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String uploadMedia(String url, File file, String name) throws IOException
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "",
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .addFormDataPart("name",name)

                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
