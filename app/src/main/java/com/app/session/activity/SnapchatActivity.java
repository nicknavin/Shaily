package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.adapter.SampleAdapter;
import com.app.session.adapter.SnapStoryGroupAdapter;
import com.app.session.api.AqueryCall;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.AllStory;
import com.app.session.model.FollowerMyStory;
import com.app.session.model.StoryGroup;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.SampleData;
import com.app.session.utility.Utility;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnapchatActivity extends AppCompatActivity {
    private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    SnapStoryGroupAdapter storyGroupAdapter;
    private boolean mHasRequestedMore;
    private SampleAdapter mAdapter;

    private ArrayList<String> mData;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<StoryGroup> storyGroupArrayList = new ArrayList<>();
    ArrayList<AllStory> storyList = new ArrayList<>();
    ArrayList<FollowerMyStory> followerMyStories = new ArrayList<>();
    Context context;
    public String accessToken = "", userId = "";
    ImageView imgOption;
    LinearLayout lay_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapchat);
        context = this;
        accessToken = DataPrefrence.getPref(context, Constant.ACCESS_TOKEN, "");
        userId = DataPrefrence.getPref(context, Constant.USER_ID, "");
        if (savedInstanceState != null) {
            mData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
        }
        initView();

    }


    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //LayoutInflater layoutInflater = getLayoutInflater();

//        View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//        View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
//        TextView txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
//        txtHeaderTitle.setText("THE HEADER!");
//        txtFooterTitle.setText("THE FOOTER!");

//        mGridView.addHeaderView(header);
//        mGridView.addFooterView(footer);
        mAdapter = new SampleAdapter(context, R.id.txt_line1, storyList);

        // do we have saved data?


        if (isConnectingToInternet(context)) {

            //callGetGroup(getParamGroup(), Urls.GET_GROUP_LIST);
//            callGetMyStory(getParamMyStory(), Urls.GET_UNSEEN_USER);
//            callGetAllStory(getParamAllStory(), Urls.GET_DEFAULT_STORY);
            callGetMyStory();
            callGetAllStory();
        } else {
            showInternetPop(context);
        }
        ((ImageView) findViewById(R.id.imgOption)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsultantUserActivity.class);
                startActivity(intent);

            }
        });
        lay_header = (LinearLayout) findViewById(R.id.lay_header);
        lay_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsultantUserActivity.class);
                intent.putExtra("TAB", "2");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sgv_dynamic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, mData);
    }


    private void callGetAllStory(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    try {

                        storyList = new ArrayList<AllStory>();


                        JSONArray array = js.getJSONArray("all_story");

                        System.out.println("list lenght " + array.length());

                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                if (!jsonObject.getString("type").equals("null") && !jsonObject.getString("type").equals("1")) {
                                    AllStory allStory = new AllStory();
                                    allStory.setFollower_user_cd(jsonObject.getString("follower_user_cd"));
                                    allStory.setExpert_user_cd(jsonObject.getString("expert_user_cd"));
                                    allStory.setStory_id(jsonObject.getString("story_id"));
                                    allStory.setUser_name(jsonObject.getString("user_name"));
                                    allStory.setImageUrl(jsonObject.getString("imageUrl"));
                                    allStory.setImgUrl(jsonObject.getString("imgUrl"));
                                    allStory.setStory_time(jsonObject.getString("story_time"));
                                    allStory.setType(jsonObject.getString("type"));
                                    allStory.setStory_titel(jsonObject.getString("story_titel"));
                                    allStory.setStory_text(jsonObject.getString("story_text"));
                                    allStory.setStory_caption(jsonObject.getString("story_caption"));
                                    allStory.setThumbnail_text(jsonObject.getString("thumbnail_text"));
                                    storyList.add(allStory);
                                }

                            }
                            mAdapter = new SampleAdapter(context, R.id.txt_line1, storyList);
                            SampleData.SAMPLE_DATA_ITEM_COUNT = array.length();
                            if (mData == null) {
                                mData = SampleData.generateSampleData();
                            }

                            for (String data : mData) {
                                mAdapter.add(data);
                            }

                            // mGridView.setAdapter(mAdapter);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismiss_loading();
                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    Intent intent = new Intent(context, ConsultantUserActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            MyDialog.iPhone(getString(R.string.something_wrong), context);
        }
    }

    private Map<String, Object> getParamAllStory() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        return params;
    }


    private void callGetAllStory() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getDefaultStory(userId, accessToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getBoolean("Status"))
                            {
                                try {

                                    storyList = new ArrayList<AllStory>();


                                    JSONArray array = js.getJSONArray("all_story");

                                    System.out.println("list lenght " + array.length());

                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            if (!jsonObject.getString("type").equals("null") && !jsonObject.getString("type").equals("1")) {
                                                AllStory allStory = new AllStory();
                                                allStory.setFollower_user_cd(jsonObject.getString("follower_user_cd"));
                                                allStory.setExpert_user_cd(jsonObject.getString("expert_user_cd"));
                                                allStory.setStory_id(jsonObject.getString("story_id"));
                                                allStory.setUser_name(jsonObject.getString("user_name"));
                                                allStory.setImageUrl(jsonObject.getString("imageUrl"));
                                                allStory.setImgUrl(jsonObject.getString("imgUrl"));
                                                allStory.setStory_time(jsonObject.getString("story_time"));
                                                allStory.setType(jsonObject.getString("type"));
                                                allStory.setStory_titel(jsonObject.getString("story_titel"));
                                                allStory.setStory_text(jsonObject.getString("story_text"));
                                                allStory.setStory_caption(jsonObject.getString("story_caption"));
                                                allStory.setThumbnail_text(jsonObject.getString("thumbnail_text"));
                                                storyList.add(allStory);
                                            }

                                        }
                                        mAdapter = new SampleAdapter(context, R.id.txt_line1, storyList);
                                        SampleData.SAMPLE_DATA_ITEM_COUNT = array.length();
                                        if (mData == null) {
                                            mData = SampleData.generateSampleData();
                                        }


                                        for (int j = 0; j < mData.size(); j++) {
                                            mAdapter.add(data);
                                        }

//                                        for (String data : mData) {
//                                            mAdapter.add(data);
//                                        }

                                        // mGridView.setAdapter(mAdapter);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            else
                            {
                                Intent intent = new Intent(context, ConsultantUserActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetPop(context);
        }
    }


    private void callGetMyStory(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();

                    try {

                        followerMyStories = new ArrayList<>();


                        JSONArray array = js.getJSONArray("all_follow_user");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jsonObject = array.getJSONObject(i);
                            if (!jsonObject.getString("user_cd").equals(userId)) {
                                FollowerMyStory followerMyStory = new FollowerMyStory();

                                followerMyStory.setImageUrl(jsonObject.getString("imageUrl"));
                                followerMyStory.setImgUrl(jsonObject.getString("imgUrl"));
                                followerMyStory.setUser_cd(jsonObject.getString("user_cd"));
                                followerMyStory.setFollower_user_cd(jsonObject.getString("follower_user_cd"));
                                followerMyStory.setUser_name(jsonObject.getString("user_name"));
                                followerMyStory.setNo_of_unseen_stories(jsonObject.getInt("no_of_unseen_stories"));

                                followerMyStories.add(followerMyStory);
                            }

                        }


                        storyGroupAdapter = new SnapStoryGroupAdapter(context, followerMyStories);
                        recyclerView.setAdapter(storyGroupAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamMyStory() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        return params;
    }

    private void callGetMyStory() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getUnseenUser(userId, accessToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getBoolean("Status")) {
                                try {

                                    followerMyStories = new ArrayList<>();


                                    JSONArray array = js.getJSONArray("all_follow_user");

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject jsonObject = array.getJSONObject(i);
                                        if (!jsonObject.getString("user_cd").equals(userId)) {
                                            FollowerMyStory followerMyStory = new FollowerMyStory();

                                            followerMyStory.setImageUrl(jsonObject.getString("imageUrl"));
                                            followerMyStory.setImgUrl(jsonObject.getString("imgUrl"));
                                            followerMyStory.setUser_cd(jsonObject.getString("user_cd"));
                                            followerMyStory.setFollower_user_cd(jsonObject.getString("follower_user_cd"));
                                            followerMyStory.setUser_name(jsonObject.getString("user_name"));
                                            followerMyStory.setNo_of_unseen_stories(jsonObject.getInt("no_of_unseen_stories"));

                                            followerMyStories.add(followerMyStory);
                                        }

                                    }


                                    storyGroupAdapter = new SnapStoryGroupAdapter(context, followerMyStories);
                                    recyclerView.setAdapter(storyGroupAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
//                               {"Status":false,"Message":"Your Not Authorised User...","all_follow_user":[]}
                                if (js.getString("Message").equals("Your Not Authorised User...")||js.getString("Message").contains("Your Not Authorised User..."))
                                {

                                    Utility.unAuthorized(context,SnapchatActivity.this);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetPop(context);
        }
    }


    public void showInternetPop(Context context) {
        MyDialog.iPhone(context.getResources().getString(R.string.connection), context);
    }

    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static android.app.Dialog dd;
    static ProgressView progressDialog;

    public void showLoading() {

        System.out.println("baseFrg  start loading");

        if (dd != null) {
            dd.dismiss();
        }
        dd = new android.app.Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.custom_loading);
            progressDialog = (ProgressView) dd.findViewById(R.id.rey_loading);
            progressDialog.start();
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismiss_loading() {
        try {
            if (dd != null) {
                if (dd.isShowing() || dd != null) {
                    if (progressDialog != null)
                        progressDialog.stop();
                }
                dd.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
