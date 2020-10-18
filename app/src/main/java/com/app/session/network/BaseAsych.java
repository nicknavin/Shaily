package com.app.session.network;

import android.os.AsyncTask;

import com.app.session.interfaces.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by navin nimade on 26-07-2019.
 */

public class BaseAsych extends AsyncTask<String, Void, String> {

    private String objectstring;
    private String result = "";
    private String url = "";

    String type = "0";
    RequestCallback requestCallback;
    ApiCalls apiCalls;
    Map<String, Object> hashMap;
    File file;
    String company_cd = "", user_i_cd = "", user_type_cd = "",name="";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        apiCalls = new ApiCalls();
    }

    public BaseAsych(String url, String objectstring, RequestCallback requestCallback) {
        this.requestCallback = requestCallback;
        this.objectstring = objectstring;
        this.url = url;
        type = "0";
        System.out.println("URL:" + url);
        System.out.println("request:" + objectstring);

    }

    public BaseAsych(String url, Map<String, Object> hashMap, RequestCallback requestCallback) {
        this.requestCallback = requestCallback;
        this.hashMap = hashMap;
        this.url = url;
        type = "1";
        System.out.println("URL:" + url);
        System.out.println("request:" + objectstring);

    }

    public BaseAsych(String url, File file, String id1, String id2, String id3, RequestCallback requestCallback) {
        this.requestCallback = requestCallback;
        user_i_cd=id1;
        user_type_cd=id2;
        company_cd=id3;
        this.url = url;
        type = "2";
        this.file = file;
        System.out.println("URL:" + url);
        System.out.println("request:" + objectstring);

    }
    public BaseAsych(String url, File file, String name, RequestCallback requestCallback) {
        this.requestCallback = requestCallback;

        this.name=name;
        this.url = url;
        type = "3";
        this.file = file;
        System.out.println("URL:" + url);
        System.out.println("request:" + objectstring);

    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            if (type.equals("0")) {
                result = apiCalls.callAPIPost(url, objectstring);
            } else if (type.equals("1")) {
                result = apiCalls.callAPIPostHashmap(url, hashMap);
            } else if (type.equals("2")) {
                result = apiCalls.uploadMedia(url, file, user_i_cd, user_type_cd, company_cd);
            }
            else if (type.equals("3")) {
                result = apiCalls.uploadMedia(url, file, name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            System.out.println("result " + s);

            if(type.equals("3"))
            {
                if(s.equals("success"))
                {
                    requestCallback.onSuccess(null, "success");
                }
                else
                {
                    requestCallback.onFailed(null, "try again", 1);
                }

            }
            else {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.has("result")) {

                    JSONObject result = jsonObject.getJSONObject("result");
                    String msg = result.getString("rdescription");
                    if (result.getString("rstatus").equals("-1") || result.getString("rstatus").equals("0")) {
                        requestCallback.onFailed(jsonObject, msg, 1);
                    } else {
                        requestCallback.onSuccess(jsonObject, msg);
                    }
                } else {

                    System.out.println("result2 " + s);
                    requestCallback.onSuccess(jsonObject, "");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            requestCallback.onException(null, "");
        }

    }
}
