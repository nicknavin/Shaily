package com.app.session.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.adapter.SampleDemoAdapter;
import com.app.session.adapter.SubscriptionStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.StoryData;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.User;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserRoot;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SubscriptionGroupProfileActivity extends BaseActivity implements View.OnClickListener{

    RecyclerView recyclerViewtop,recyclerView;
    ArrayList<SubscriptionGroup> subscriptionGroupsList = new ArrayList<>();
    ReadMoreTextView txtTitleBriefCV;
    ArrayList<StoryData> storyDataArrayList=new ArrayList<>();
    SubscriptionStoryAdapter subscriptionStoryAdapter;
    CustomTextView txtUserName,txtExplore;
    CircleImageView imgProfile;
    RecyclerView recyclerViewBrief;
    CheckBox imgDrop;
    String groupiconUrl="";
    CustomTextView txtBio,txtTitleName,txtSubscriber;
    int selection=0;
    ImageView imgStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_subscription_group_profile_page);
        initView();
        initVideoView();
        getUserDetail();
    }

    private void initView()
    {


        imgStory = (ImageView) findViewById(R.id.imgStory);
        txtBio = (CustomTextView) findViewById(R.id.txtBio);
        txtTitleName = (CustomTextView) findViewById(R.id.txtTitleName);
        txtSubscriber = (CustomTextView)findViewById(R.id.txtSubscriber);


        txtSubscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selection==0)
                {
                    selection=1;
                    txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber_select));
                }
                else
                {
                    selection=0;
                    txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber));
                }
            }
        });

        txtExplore=(CustomTextView)findViewById(R.id.txtExplore);
        txtUserName=(CustomTextView)findViewById(R.id.txtGroupName);
        imgProfile=(CircleImageView)findViewById(R.id.imgGroupCover);


        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerViewtop=(RecyclerView)findViewById(R.id.recyclerViewtop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewtop.setLayoutManager(layoutManager);
        SampleDemoAdapter sampleDemoAdapter=new SampleDemoAdapter(context, subscriptionGroupsList, new ApiItemCallback() {
            @Override
            public void result(int position) {

            }
        });
        recyclerViewtop.setAdapter(sampleDemoAdapter);

        recyclerViewBrief=(RecyclerView)findViewById(R.id.recyclerViewBrief);
        recyclerViewBrief.setLayoutManager(new LinearLayoutManager(context));

    }

    @Override
    protected void onResume() {
        super.onResume();


        callGetSubscriptionGroup();
    }

    ArrayList<Brief_CV> brief_cvList;
    private void callGetStory() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.GET_STORY_ALL, getStoryParameter(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {

                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("result"+jsonObject.toString());
                        if (jsonObject.getString("rstatus").equals("1"))
                        {
                            try {
                                JSONArray jsonArray = js.getJSONArray("story_data");
                                Type type = new TypeToken<ArrayList<StoryData>>() {
                                }.getType();
                                storyDataArrayList = new Gson().fromJson(jsonArray.toString(), type);
                                subscriptionStoryAdapter=new SubscriptionStoryAdapter(context, storyDataArrayList, txtUserName.getText().toString(), groupiconUrl, new ObjectCallback() {
                                    @Override
                                    public void getObject(Object object, int position,View view)
                                    {
                                        StoryData storyData= (StoryData) object;
//                                        showMenu(view,storyData,position);

                                    }
                                });
                                recyclerView.setAdapter(subscriptionStoryAdapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    showToast(success);

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    showToast(failed);


                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            }).execute();

        } else {
            showInternetConnectionToast();
        }
    }
    private String getStoryParameter() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_cd",userId);


            return jsonObject.toString();



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  System.out.println("input : " + gsonObject.toString());
        return null ;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

        }
    }

    private void callGetSubscriptionGroup() {
        if (Utility.isConnectingToInternet(context)) {
            showLoading();
            new BaseAsych(Urls.GET_SUBSCRIPTION_GROUP, getSubcriptionGroup(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        System.out.println("subscription_list" + js.toString());
                        subscriptionGroupsList = new ArrayList<>();
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1"))
                        {
                            if (js.getJSONArray("subscription_group_list") != null)
                            {
                                JSONArray jsonArray = js.getJSONArray("subscription_group_list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    SubscriptionGroup group = new SubscriptionGroup();
                                    group.setCategory_cd(object.getString("category_cd"));
                                    group.setCurrency_cd(object.getString("currency_cd"));
                                    group.setGroup_desc(object.getString("group_desc"));
                                    group.setGroup_introduction_video_url(object.getString("group_introduction_video_url"));
                                    group.setGroup_name(object.getString("group_name"));
                                    group.setGroup_url_image_address(object.getString("group_url_image_address"));
                                    group.setLanguage_cd(object.getString("language_cd"));
                                    group.setSubscription_group_cd(object.getString("subscription_group_cd"));
                                    group.setSubscription_price(object.getDouble("subscription_price"));
                                    group.setUser_cd(object.getString("user_cd"));
                                    subscriptionGroupsList.add(group);
                                }


                            }
                            SampleDemoAdapter sampleDemoAdapter=new SampleDemoAdapter(context, subscriptionGroupsList, new ApiItemCallback() {
                                @Override
                                public void result(int position) {

                                }
                            });
                            recyclerViewtop.setAdapter(sampleDemoAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    showToast(success);

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    showToast(failed);


                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            }).execute();

        } else {
            showInternetConnectionToast();
        }
    }

    private String getSubcriptionGroup() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_cd", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();


    }


    SimpleExoPlayer player;

    PlayerView playerView;
    ImageView fullscreenButton;
    boolean fullscreen = false;
    String url = "";
    int w = 0, h = 0;
    private void initVideoView() {
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());

        playerView = findViewById(R.id.player);


        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setVisibility(View.VISIBLE);





//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));
//
//        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(url));
//
//        player.prepare(videoSource);
//        player.setPlayWhenReady(true);


    }

    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("W", w);
        outState.putInt("H",h);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        w=savedInstanceState.getInt("W");
        h=savedInstanceState.getInt("H");


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            //  Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();



            if(w>h)
            {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = h;
                playerView.setLayoutParams(params);
            }
            else
            {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                playerView.setMinimumWidth(w);
                playerView.setMinimumHeight(h);
            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
        if(w>h)
        {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setLayoutParams(params);
         }
         else
         {
             playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                playerView.setMinimumWidth(w);
                playerView.setMinimumHeight(h);
            }

//            setVideoLayout(h,w);
            // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    public void setVideoLayout(int height,int width)
    {
        ViewGroup.LayoutParams p = playerView.getLayoutParams();
        int currWidth = playerView.getWidth();

        // Set new width/height of view
        // height or width must be cast to float as int/int will give 0
        // and distort view, e.g. 9/16 = 0 but 9.0/16 = 0.5625.
        // p.height is int hence the final cast to int.
        p.width = currWidth;
        p.height = (int) ((float) height / width * currWidth);

        // Redraw myView
        playerView.requestLayout();
    }

    private void getUserDetailold() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getUserDetailRequest(userId, accessToken);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                    JSONArray jsonArray = js.getJSONArray("User_Detail");


                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    txtUserName.setText(jsonObject.getString("user_name"));
                                    txtExplore.setText(jsonObject.getString("user_name"));
                                    if (jsonObject.getString("imageUrl") != null && !jsonObject.getString("imageUrl").isEmpty()) {
                                        groupiconUrl=jsonObject.getString("imageUrl");
                                        Picasso.with(context).load(jsonObject.getString("imageUrl")).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(imgProfile);


                                    }

                                    JSONArray arrayBrief = js.getJSONArray("user_brief");
                                    if(brief_cvList!=null){
                                        brief_cvList.clear();}
                                    brief_cvList = new ArrayList<>();

                                    for (int i = 0; i < arrayBrief.length(); i++) {
                                        JSONObject object = arrayBrief.getJSONObject(i);
                                        Brief_CV brief_cv = new Brief_CV();
                                        brief_cv.setBrief_cv(object.getString("breaf_cv"));
                                        brief_cv.setLanguage_cd(object.getString("language_cd"));
                                        brief_cv.setNative_name(object.getString("native_name"));
                                        brief_cv.setEnglish_name(object.getString("english_name"));
                                        brief_cv.setSer_no(object.getString("ser_no"));
                                        brief_cv.setVideo_thumbnail(object.getString("video_thumbnail"));
                                        brief_cv.setVideo_duration(object.getString("video_duration"));
                                        brief_cv.setVideo_url(object.getString("video_url"));
//                                        brief_cv.setTitle_cd(object.getString("titele_cd"));
                                        brief_cv.setTitle_name(object.getString("title_name"));
                                        brief_cvList.add(brief_cv);
                                    }

                                    Brief_CV briefCv= brief_cvList.get(0);

                                    txtTitleBriefCV=(ReadMoreTextView)findViewById(R.id.txtTitleBriefCV);
                                    txtBio.setText("Price");
                                    if(!briefCv.getTitle_name().equals("0")) {
                                        txtTitleName.setText(briefCv.getTitle_name());
                                    }

                                    Typeface face = Typeface.createFromAsset(context.getAssets(),
                                            "Segoe_UI.ttf");
                                    txtTitleBriefCV.setTypeface(face);
                                    txtTitleBriefCV.setTrimCollapsedText(" more");
                                    txtTitleBriefCV.setTrimExpandedText(" less");
                                    txtTitleBriefCV.setText(briefCv.getBrief_cv());
                                    String videoUrl = Urls.BASE_VIDEO_URL + briefCv.getVideo_url();
                                    System.out.println("videourl "+videoUrl);

                                    if (briefCv.getVideo_thumbnail() != null && !briefCv.getVideo_thumbnail().isEmpty() && !briefCv.getVideo_thumbnail().equals("null")) {
                                        String url = Urls.BASE_IMAGES_URL + briefCv.getVideo_thumbnail();
                                        Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgStory);
                                        fullscreenButton.setVisibility(View.VISIBLE);
                                        fullscreenButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {



//                                                  //  fullscreenButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_close));
//
//                                                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//                                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//                                                    if (getSupportActionBar() != null) {
//                                                        getSupportActionBar().hide();
//                                                    }
//
//                                                    if (w > h) {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                                                    } else {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                                    }
                                                Intent intent =new Intent(context,PlayerActivity.class);
                                                intent.putExtra("URL",videoUrl);
                                                intent.putExtra("W",w);
                                                intent.putExtra("H",h);
                                                startActivity(intent);

//                                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
//
//                                                    params.width = params.MATCH_PARENT;
//                                                    params.height = params.MATCH_PARENT;
//                                                    playerView.setLayoutParams(params);


                                            }
                                        });

                                        playerView.setPlayer(player);
                                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                                        player.addVideoListener(new VideoListener() {
                                            @Override
                                            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                                                w = width;
                                                h = height;
                                                imgStory.setVisibility(View.GONE);
                                                setVideoLayout(h,w);
                                            }

                                            @Override
                                            public void onSurfaceSizeChanged(int width, int height) {

                                            }

                                            @Override
                                            public void onRenderedFirstFrame() {

                                            }
                                        });



                                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

                                        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                                                .createMediaSource(Uri.parse(videoUrl));

                                        player.prepare(videoSource);
                                        player.setPlayWhenReady(true);



                                    }
                                    else
                                    {
                                        imgStory.setVisibility(View.GONE);
                                    }






                                } catch (JSONException e) {
                                    e.printStackTrace();
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
            showInternetConnectionToast();
        }
    }
    private void getUserDetail() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            UserId user=new UserId();
            user.setUser_id(userId);
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<UserRoot> call = apiInterface.getUserDetails(accessToken, user);
            call.enqueue(new retrofit2.Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                    dismiss_loading();

                    if(response.body()!=null)
                    {
                        UserRoot root = response.body();
                        if(root.getStatus()==200)
                        {
                            User user=root.getUserBody().getUser();

                            txtUserName.setText(user.getUser_name());
                            txtExplore.setText(user.getUser_name());
                            if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                groupiconUrl=Urls.BASE_IMAGES_URL+user.getImageUrl();
                                Picasso.with(context).load(groupiconUrl).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(imgProfile);
                            }
                            Brief_CV briefCv= user.getBriefCV().get(0);

                            txtTitleBriefCV=(ReadMoreTextView)findViewById(R.id.txtTitleBriefCV);
                            txtBio.setText("Price");
                            if(!briefCv.getTitle_name().equals("0")) {
                                txtTitleName.setText(briefCv.getTitle_name());
                            }



                            Typeface face = Typeface.createFromAsset(context.getAssets(),
                                    "Segoe_UI.ttf");
                            txtTitleBriefCV.setTypeface(face);
                            txtTitleBriefCV.setTrimCollapsedText(" more");
                            txtTitleBriefCV.setTrimExpandedText(" less");
                            txtTitleBriefCV.setText(briefCv.getBrief_cv());

                            String videoUrl = Urls.BASE_VIDEO_URL + briefCv.getVideo_url();
                            System.out.println("videoUrl "+videoUrl);

                            if (briefCv.getVideo_thumbnail() != null && !briefCv.getVideo_thumbnail().isEmpty() && !briefCv.getVideo_thumbnail().equals("null")) {
                                String url = Urls.BASE_IMAGES_URL + briefCv.getVideo_thumbnail();
                                Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgStory);
                                fullscreenButton.setVisibility(View.VISIBLE);
                                fullscreenButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {



//                                                  //  fullscreenButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_close));
//
//                                                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//                                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//                                                    if (getSupportActionBar() != null) {
//                                                        getSupportActionBar().hide();
//                                                    }
//
//                                                    if (w > h) {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                                                    } else {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                                    }
                                        Intent intent =new Intent(context,PlayerActivity.class);
                                        intent.putExtra("URL",videoUrl);
                                        intent.putExtra("W",w);
                                        intent.putExtra("H",h);
                                        startActivity(intent);

//                                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
//
//                                                    params.width = params.MATCH_PARENT;
//                                                    params.height = params.MATCH_PARENT;
//                                                    playerView.setLayoutParams(params);


                                    }
                                });

                                playerView.setPlayer(player);
                                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                                player.addVideoListener(new VideoListener() {
                                    @Override
                                    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                                        w = width;
                                        h = height;
                                        imgStory.setVisibility(View.GONE);
                                        setVideoLayout(h,w);
                                    }

                                    @Override
                                    public void onSurfaceSizeChanged(int width, int height) {

                                    }

                                    @Override
                                    public void onRenderedFirstFrame() {

                                    }
                                });



                                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

                                MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                                        .createMediaSource(Uri.parse(videoUrl));

                                player.prepare(videoSource);
                                player.setPlayWhenReady(true);



                            }
                            else
                            {
                                imgStory.setVisibility(View.GONE);
                            }
                        }
                    }





                }

                @Override
                public void onFailure(Call<UserRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }
}
