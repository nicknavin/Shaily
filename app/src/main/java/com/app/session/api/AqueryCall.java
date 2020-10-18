package com.app.session.api;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.app.session.R;
import com.app.session.interfaces.RequestCallback;

import org.json.JSONObject;

import java.util.Map;


public class AqueryCall {

    private Activity context;
    AQuery aq;

    public AqueryCall(Activity context) {
        this.context = context;
    }


    public void contactVerification(String url, JSONObject jsonInput, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());

        try {
            Log.e("url.....", "" + url + " " + jsonInput.toString());
            aq.post(url, jsonInput, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Log.e("result.....", "" + json);
                    if (json != null) {
                        try {
                            String Message="";
                            if(json.has("message")) {
                                 Message = json.getString("message");
                            }
                            boolean jsonStatus = json.getBoolean("Status");
                            if (jsonStatus) {
                                request.onSuccess(json, Message);
                            } else {
                                request.onFailed(json, Message, 1);
                            }
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }


    public void commonApiJson(String url, JSONObject jsonInput, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());

        try {
            Log.e("url.....", "" + url + " " + jsonInput.toString());
            aq.post(url, jsonInput, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Log.e("result.....", "" + json);
                    if (json != null) {
                        try {
                            String Message = json.getString("Message");
                            boolean jsonStatus = json.getBoolean("Status");
                            if (jsonStatus) {
                                request.onSuccess(json, Message);
                            } else {
                                request.onFailed(json, Message, 1);
                            }
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    private void nullCase(AjaxStatus status, int networkError, RequestCallback request) {
        if (status.getCode() == networkError) {
            request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.connection));
        } else {

            request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }
//    public void commonAPI(String url, Map<String, Object> params, final RequestCallback request) {
//        aq = new AQuery(context.getApplicationContext());
//
//        try {
//
//            Log.e("url.....", "" + url + " " + params.toString());
//            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//
//                @Override
//                public void callback(String url, JSONObject json, AjaxStatus status) {
//                    Log.e("result.....", "" + json);
//                    if (json != null) {
//                        try {
//                            String Message="";
//                            if(json.has("message")) {
//                                Message = json.getString("message");
//                            }
//                            boolean jsonStatus = json.getBoolean("status");
//                            if (jsonStatus) {
//                                request.onSuccess(json, Message);
//                            } else {
//                                request.onFailed(json, Message, 1);
//                            }
//                        } catch (Exception e1) {
//
//                            e1.printStackTrace();
//                        }
//                    } else {
//                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
//                    }
//                }
//            }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
//        }
//    }

//{token_id=8Nnb2p6VJjXxDRFft05m, user_cd=3, follow_user_cd=6}
    public void commonAPI(String url, Map<String, Object> params, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            Log.e("URL: ", "" + url + " " + params);
            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {

                        Log.e("result.....", "" + json);

                    if(json!=null)
                    {
                        try {
                            String Message="";
                            if(json.has("Message")) {
                                Message = json.getString("Message");
                            }
//                           String jsonStatus = json.getString("Status");
//
//                            if (jsonStatus.equals("Success")) {
//                                request.onSuccess(json, Message);
//                            } else {
//                                request.onFailed(json, Message, 1);
//                            }
                            boolean jsonStatus=json.getBoolean("Status");
                            {
                                if(jsonStatus)
                                { request.onSuccess(json, Message);

                                }else
                                {

                                    request.onFailed(json, Message, 1);
                                }
                            }
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    }
                    else
                    {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }


                }
            }.method(AQuery.METHOD_POST));
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }


    public void commonAPI2(String url, Map<String, Object> params, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            Log.e("URL: ", "" + url + " " + params);
            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {

                    Log.e("result.....", "" + json);

                    if(json!=null)
                    {
                        try {
                            String Message="";
                            if(json.has("Message")) {
                                Message = json.getString("Message");
                            }
//                           String jsonStatus = json.getString("Status");
//
//                            if (jsonStatus.equals("Success")) {
//                                request.onSuccess(json, Message);
//                            } else {
//                                request.onFailed(json, Message, 1);
//                            }
                            request.onSuccess(json, Message);
//                            boolean jsonStatus=json.getBoolean("Status");
//                            {
//                                if(jsonStatus)
//                                { request.onSuccess(json, Message);
//
//                                }else
//                                {
//                                    request.onFailed(json, Message, 1);
//                                }
//                            }
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    }
                    else
                    {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }


                }
            }.method(AQuery.METHOD_POST));
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }


//    public void getProfile(String url, String token, final RequestCallback request) {
//        aq = new AQuery(context.getApplicationContext());
//        try {
//            Log.e("url.....", url + " token " + token);
//            aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//
//                @Override
//                public void callback(String url, JSONObject json, AjaxStatus status) {
//                    Log.e("result.....", "" + json);
//                    if (json != null) {
//                        try {
//                            String Message = json.getString("message");
//                            String jsonStatus = json.getString("status");
//                            if (jsonStatus.equalsIgnoreCase("success")) {
//                                if (Message.equalsIgnoreCase("Successfull")) {
//                                    request.onSuccess(json, Message);
//                                } else {
//                                    request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
//                                }
//                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
//                                if (Message.equalsIgnoreCase("Your Great Lakes account has been temporarily suspended as a security precaution.")) {
//                                    request.onFailed(json, Message, 3);
//                                } else {
//                                    request.onFailed(json, Message, 1);
//                                }
//                            } else if (jsonStatus.equalsIgnoreCase("false")) {
//                                request.onFailed(json, Message, 2);
//                            } else {
//                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
//                            }
//                        } catch (Exception e1) {
//                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
//                            e1.printStackTrace();
//                        }
//                    } else {
//                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
//                    }
//                }
//            }.method(AQuery.METHOD_GET).header(Urls.API_KEY, token));
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
//        }
//    }
//
//
//    public void getAnswer(String url, String token, final RequestCallback request) {
//        aq = new AQuery(context.getApplicationContext());
//        try {
//            Log.e("url.....", url + " token " + token);
//            aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//
//                @Override
//                public void callback(String url, JSONObject json, AjaxStatus status) {
//                    Log.e("result.....", "" + json);
//                    if (json != null) {
//                        try {
//                            String Message = json.getString("message");
//                            String jsonStatus = json.getString("status");
//                            if (jsonStatus.equalsIgnoreCase("success")) {
//                                if (Message.equalsIgnoreCase("Successfull")) {
//                                    request.onSuccess(json, Message);
//                                } else {
//                                    request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
//                                }
//                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
//                                if (Message.equalsIgnoreCase("Your Great Lakes account has been temporarily suspended as a security precaution.")) {
//                                    request.onFailed(json, Message, 3);
//                                } else {
//                                    request.onFailed(json, Message, 1);
//                                }
//                            } else if (jsonStatus.equalsIgnoreCase("false")) {
//                                request.onFailed(json, Message, 2);
//                            } else {
//                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
//                            }
//                        } catch (Exception e1) {
//                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
//                            e1.printStackTrace();
//                        }
//                    } else {
//                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
//        }
//    }
}




