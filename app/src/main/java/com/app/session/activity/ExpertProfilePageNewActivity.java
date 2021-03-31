package com.app.session.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.adapter.SampleDemoAdapter;
import com.app.session.adapter.UserSubscriptionStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.database.DatabaseHelper;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.BriefCV;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.OtherUserProfile;
import com.app.session.data.model.ReqFollowUser;
import com.app.session.data.model.ReqSubscribeGroupStories;
import com.app.session.data.model.ReqUserProfile;
import com.app.session.data.model.Root;
import com.app.session.data.model.SubscribedAllStroiesBody;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.SubscriptionGroupId;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserProfileBody;
import com.app.session.data.model.UserProfileRoot;
import com.app.session.data.model.UserStory;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpertProfilePageNewActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

String load="1";
    RecyclerView recyclerViewtop,recyclerView;
    ArrayList<SubscriptionGroup> subscriptionGroupsList = new ArrayList<>();
    ReadMoreTextView txtTitleBriefCV;
   CustomTextView txtSubscriber1;
    ArrayList<UserStory> storyDataArrayList=new ArrayList<>();
    UserSubscriptionStoryAdapter userSubscriptionStoryAdapter;
    CustomTextView txtUserName,txtExplore;
    CircleImageView imgProfile;
    RecyclerView recyclerViewBrief;
    CheckBox imgDrop;
    String groupiconUrl="";
    CustomTextView txtBio,txtTitleName,txtSubscriber;

    ImageView imgStory;
    ShimmerFrameLayout mShimmerViewContainer;
    ImageView imgVideoThumb;
    ArrayList<SubscriptionGroupId> listSubsIds=new ArrayList<>();
String id;
boolean followStatus;

    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_expert_profile_page);
        if(getIntent().getStringExtra("ID")!=null)
        {
            id=getIntent().getStringExtra("ID");
        }
        db = new DatabaseHelper(this);
        initView();
        initVideoView();
        getOtherUserDetail();

    }

    private void initView()
    {
        //SubscriptionGroupProfileActivity

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        imgStory = (ImageView) findViewById(R.id.imgStory);
        imgVideoThumb = (ImageView) findViewById(R.id.imgVideoThumb);
        txtBio = (CustomTextView) findViewById(R.id.txtBio);
        txtTitleName = (CustomTextView) findViewById(R.id.txtTitleName);
        txtSubscriber1 = (CustomTextView)findViewById(R.id.txtSubscriber1);
        txtSubscriber = (CustomTextView)findViewById(R.id.txtSubscriber);
        txtSubscriber.setOnClickListener(this);




        txtExplore=(CustomTextView)findViewById(R.id.txtExplore);
        txtUserName=(CustomTextView)findViewById(R.id.txtGroupName);
        imgProfile=(CircleImageView)findViewById(R.id.imgGroupCover);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(context,SubscriptionGroupProfileActivity.class));
            }
        });
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
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewBrief=(RecyclerView)findViewById(R.id.recyclerViewBrief);
        recyclerViewBrief.setLayoutManager(new LinearLayoutManager(context));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
//        getStory();
//        getSubscriptionGroup();
    }





    private void statusSubscribe()
    {
        if(followStatus)
        {
            txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber_select));
        }
        else
        {
            txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber));
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtSubscriber:
                if(followStatus)//true means login user follow this user
                {

                    callUnFolloowUser();//this for unfollow
                }
                else
                {

                    callFollowUser();//this for follow

                }
                break;

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
                                public void result(int position)
                                {
//                                    SubscriptionGroup group=subscriptionGroupsList.get(position);
//                                    Intent intent=new Intent(context,StoryDetailActivity.class);
//                                    intent.putExtra("ID",group.getSubscription_group_cd());
//                                    intent.putExtra("NAME",group.getGroup_name());
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("List", (Serializable) brief_cvList);
//                                    intent.putExtra("BUNDLE", bundle);
//                                    startActivity(intent);
                                        startActivity(new Intent(context,SubscriptionGroupProfileActivity.class));
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

    private void getSubscriptionGroup() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            UserId user=new UserId();
            user.setUser_id(userId);
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SubscriptionGroupRoot> call = apiInterface.reqGetUserSubscriptionGroups(accessToken, user);
            call.enqueue(new Callback<SubscriptionGroupRoot>() {
                @Override
                public void onResponse(Call<SubscriptionGroupRoot> call, Response<SubscriptionGroupRoot> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        SubscriptionGroupRoot root=response.body();
                        subscriptionGroupsList=root.getSubscriptionGroupBodies();
                        SampleDemoAdapter sampleDemoAdapter=new SampleDemoAdapter(context, subscriptionGroupsList, new ApiItemCallback() {
                            @Override
                            public void result(int position)
                            {
//                                    SubscriptionGroup group=subscriptionGroupsList.get(position);
//                                    Intent intent=new Intent(context,StoryDetailActivity.class);
//                                    intent.putExtra("ID",group.getSubscription_group_cd());
//                                    intent.putExtra("NAME",group.getGroup_name());
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("List", (Serializable) brief_cvList);
//                                    intent.putExtra("BUNDLE", bundle);
//                                    startActivity(intent);
                                startActivity(new Intent(context,SubscriptionGroupProfileActivity.class));
                            }
                        });
                        recyclerViewtop.setAdapter(sampleDemoAdapter);
                        //    System.out.println("data list"+subscriptionGroups.get(0).getUser_id());
                    }
                    else
                    {
                        try {
                            ResponseBody responseBody=response.errorBody();
                            String data= responseBody.string();

                            JSONObject jsonObject=new JSONObject(data);
                            System.out.println("error body "+jsonObject.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }

                @Override
                public void onFailure(Call<SubscriptionGroupRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });

        } else {
            showInternetConnectionToast();
        }
    }


    private void getStoryNew() {
        if (Utility.isConnectingToInternet(context))
        {
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);


            ReqSubscribeGroupStories reqFollowUser =new ReqSubscribeGroupStories();
            reqFollowUser.setmLoad(load);
            ArrayList<String> list=new ArrayList<>();
            reqFollowUser.setUser_id(userId);
            for(int i=0;i<listSubsIds.size();i++)
            {
                list.add(listSubsIds.get(i).get_id());
            }
            reqFollowUser.setmSubscriptionId(list);


            showLoading();
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SubscribedAllStroiesRoot> call  =apiInterface.reqGetuserSubscribedAllStroies(accessToken,reqFollowUser);
            call.enqueue(new Callback<SubscribedAllStroiesRoot>() {
                @Override
                public void onResponse(Call<SubscribedAllStroiesRoot> call, Response<SubscribedAllStroiesRoot> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        if(response.body().getmStatus()==200) {
                            SubscribedAllStroiesBody subscribedAllStroiesBody = response.body().getmBody();

                            storyDataArrayList= subscribedAllStroiesBody.getSubscriptionStories();


                            if (storyDataArrayList.size()>0) {
//                                if(db.getNotesCount()>0)
//                                {
//                                    db.deleteNote();
//                                }
//                                db.insertNote(Utility.getJsonToStringFromList(storyDataArrayList));

//                                userSubscriptionStoryAdapter=new UserSubscriptionStoryAdapter(context, storyDataArrayList, txtUserName.getText().toString(), groupiconUrl, new ObjectCallback() {
//                                    @Override
//                                    public void getObject(Object object, int position,View view)
//                                    {
//                                        if(position==-1) {
//                                            startActivity(new Intent(context, SubscriptionGroupProfileActivity.class));
//                                        }
//                                        else {
//                                            showMenu(view);
//                                        }
//
//                                    }
//                                });
//                                recyclerView.setAdapter(userSubscriptionStoryAdapter);
                            }


                        }
                    }
                }

                @Override
                public void onFailure(Call<SubscribedAllStroiesRoot> call, Throwable t) {

                }
            });



            //Call<ResponseBody> call = apiInterface.reqGetuserSubscribedAllStroies()


//            call.enqueue(new Callback<StoryRoot>() {
//                @Override
//                public void onResponse(Call<StoryRoot> call, Response<StoryRoot> response)
//                {
//                    dismiss_loading();
//                    if(response.body()!=null)
//                    {
//                        if(response.body().getStatus()==200)
//                        {
//                            StoryBody body=response.body().getStoryBody();
//                            storyDataArrayList =body.getUserStories();
////                            Gson gson = new Gson();
////                            String json = gson.toJson(storyDataArrayList);
////                            System.out.println("json :"+json);
////
////
////                         ArrayList<UserStory> data =   gson.fromJson(json,
////                                    new TypeToken<ArrayList<UserStory>>() {
////                                    }.getType());
//
//
//                            if (storyDataArrayList.size()>0) {
//                                if(db.getNotesCount()>0)
//                                {
//                                    db.deleteNote();
//                                }
//                                db.insertNote(Utility.getJsonToStringFromList(storyDataArrayList));
//
//                                showStoryData(storyDataArrayList);
//                            }
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<StoryRoot> call, Throwable t) {
//
//                }
//            });



        } else {
            showInternetConnectionToast();
        }
    }


//    private void showStoryData(ArrayList<UserStory> list)
//    {
//
//        if (list.size()>0) {
//            subscriptionStoryAdapter=new UserSubscriptionStoryAdapter(context,list,userName,userUrl, new ObjectCallback()
//            {
//                @Override
//                public void getObject(Object object, int position,View view)
//                {
//                    if(view.getId()==R.id.layTops||view.getId()==R.id.txtStoryTitle||view.getId()==R.id.txtStoryDiscription)
//                    {
//                        UserStory storyData = storyDataArrayList.get(position);
//                        Intent intent = new Intent(context, StoryPageDetailActivity.class);
//                        intent.putExtra("DATA", storyData);
//                        startActivity(intent);
//                    }
//                    else
//                    {
//                        showMenu(view);
//                    }
//                }
//            });
//            recyclerViewbottom.setAdapter(subscriptionStoryAdapter);
//        }
//        else
//        {
////            getStory();
//        }
//    }
//



    SimpleExoPlayer player;
    PlayerView playerView;
    ImageView fullscreenButton;
    boolean fullscreen = false;
    String url = "";
     int  w = 0, h = 0;
    private void initVideoView() {
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());

        playerView = findViewById(R.id.player);


        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);

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
            if(w>h) {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setLayoutParams(params);
            }
            else
            { playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                playerView.setMinimumWidth(w);
                playerView.setMinimumHeight(h);
            }
//            setVideoLayout(h,w);
           // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
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




    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_view_story, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_report:

                return true;


        }

        return false;
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


    ArrayList<Brief_CV> brief_cvList;
    private void getUserDetail0() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getUserDetailRequest(userId, accessToken);
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
                                    txtBio.setText(briefCv.getEnglish_name());
                                    if(!briefCv.getTitle_name().equals("0")) {
                                        txtTitleName.setText(briefCv.getTitle_name());
                                    }

                                    Typeface face = Typeface.createFromAsset(context.getAssets(),
                                            "Segoe_UI.ttf");
                                    txtTitleBriefCV.setTypeface(face);

                                    String videoUrl = Urls.BASE_VIDEO_URL + briefCv.getVideo_url();
                                    System.out.println("videoUrl "+videoUrl);




                                    txtTitleBriefCV.setTrimCollapsedText(" more");
                                    txtTitleBriefCV.setTrimExpandedText(" less");
                                    txtTitleBriefCV.setText(briefCv.getBrief_cv());


//                                    webview.setWebViewClient(new WebViewClient());
//                                    webview.getSettings().setJavaScriptEnabled(true);
//                                    webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                                    webview.getSettings().setPluginState(WebSettings.PluginState.ON);
//                                    webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
//                                    webview.setWebChromeClient(new WebChromeClient());
//                                    webview.loadUrl(videoUrl);

                                    if (briefCv.getVideo_thumbnail() != null && !briefCv.getVideo_thumbnail().isEmpty() && !briefCv.getVideo_thumbnail().equals("null")) {

                                        String url = Urls.BASE_IMAGES_URL + briefCv.getVideo_thumbnail();
//                                        Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgVideoThumb, new Callback() {
//                                            @Override
//                                            public void onSuccess() {
//
//                                            }
//
//                                            @Override
//                                            public void onError() {
//
//                                            }
//                                        });

                                        Glide.with(context)
                                                .asBitmap()
                                                .load(url)
                                                .into(new BitmapImageViewTarget(imgVideoThumb) {
                                                    @Override
                                                    protected void setResource(Bitmap resource) {
                                                        // Do bitmap magic here

                                                        super.setResource(resource);


                                                    }
                                                });

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
//                                                    if (w > h)
//                                                    {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                                                    }
//                                                    else
//                                                    {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                                    }

                                                Intent intent =new Intent(context,PlayerActivity.class);
                                                intent.putExtra("URL",videoUrl);
                                                intent.putExtra("W",w);
                                                intent.putExtra("H",h);
                                                startActivity(intent);
//                                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
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
                                                imgVideoThumb.setVisibility(View.GONE);
                                                if(w>h) {
                                                    setVideoLayout(h, w);
                                                }
                                                else
                                                {
                                                    playerView.setMinimumWidth(w);
                                                    playerView.setMinimumHeight(h);
                                                }

                                            }

                                            @Override
                                            public void onSurfaceSizeChanged(int width, int height) {
                                                //  showToast("surface changed");
                                                //   setVideoLayout(height,width);
                                                // setVideoLayout(height,width);
                                            }

                                            @Override
                                            public void onRenderedFirstFrame() {

                                            }
                                        });







                                        //playerView.setMinimumHeight(hh);
                                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(context, getApplicationContext().getString(R.string.app_name)));
                                        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                                                .createMediaSource(Uri.parse(videoUrl));

                                        player.prepare(videoSource);
                                        player.setPlayWhenReady(true);



                                        //      player.setRepeatMode(Player.REPEAT_MODE_ALL);
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

    String videoUrl="";
    private void getOtherUserDetail() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            ReqUserProfile user=new ReqUserProfile();
            user.setUserId(userId);
            user.setPersonUserId(id);
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<UserProfileRoot> call = apiInterface.reqGetUserProfile(user);
            call.enqueue(new Callback<UserProfileRoot>() {
                @Override
                public void onResponse(Call<UserProfileRoot> call, Response<UserProfileRoot> response) {
                    dismiss_loading();

                    if(response.body()!=null)
                    {
                        UserProfileRoot root = response.body();
                        if(root.getStatus()==200)
                        {
                            UserProfileBody userBody=root.getUserBody();
                            OtherUserProfile user=userBody.getUser();
                            txtSubscriber1.setText(""+userBody.getFollowers());
                            followStatus =     root.getUserBody().isFollow();
                            statusSubscribe();
                            txtUserName.setText(user.getUser_name());
                            txtExplore.setText(user.getUser_name());
                            if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                groupiconUrl=Urls.BASE_IMAGES_URL+user.getImageUrl();
                                Picasso.with(context).load(groupiconUrl).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(imgProfile);
                            }

                            listSubsIds=user.getSubscription_group_id();
                            BriefCV briefCv= user.getBriefCV().get(0);

                            txtTitleBriefCV=(ReadMoreTextView)findViewById(R.id.txtTitleBriefCV);
                            txtBio.setText(user.getUserLangauges().get(0).getName());
                            if(!briefCv.getTitleName().equals("0")) {
                                txtTitleName.setText(briefCv.getTitleName());
                            }

                            Typeface face = Typeface.createFromAsset(context.getAssets(),
                                    "Segoe_UI.ttf");
                            txtTitleBriefCV.setTypeface(face);


                            if(briefCv.getVideoUrl()!=null&&!briefCv.getVideoUrl().isEmpty()) {
                                 videoUrl = Urls.BASE_VIDEO_URL + briefCv.getVideoUrl();
                                playerView.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                playerView.setVisibility(View.GONE);
                            }
                            System.out.println("videoUrl "+videoUrl);
                            txtTitleBriefCV.setTrimCollapsedText(" more");
                            txtTitleBriefCV.setTrimExpandedText(" less");
                            txtTitleBriefCV.setText(briefCv.getBriefCv());


                            if (briefCv.getVideoThumbnail() != null && !briefCv.getVideoThumbnail().isEmpty() && !briefCv.getVideoThumbnail().equals("null")) {

                                String url = Urls.BASE_IMAGES_URL + briefCv.getVideoThumbnail();
//                                        Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgVideoThumb, new Callback() {
//                                            @Override
//                                            public void onSuccess() {
//
//                                            }
//
//                                            @Override
//                                            public void onError() {
//
//                                            }
//                                        });

                                Glide.with(context)
                                        .asBitmap()
                                        .load(url)
                                        .into(new BitmapImageViewTarget(imgVideoThumb) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                // Do bitmap magic here

                                                super.setResource(resource);


                                            }
                                        });

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
//                                                    if (w > h)
//                                                    {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                                                    }
//                                                    else
//                                                    {
//                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                                    }

                                        Intent intent =new Intent(context,PlayerActivity.class);
                                        intent.putExtra("URL",videoUrl);
                                        intent.putExtra("W",w);
                                        intent.putExtra("H",h);
                                        startActivity(intent);
//                                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
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
                                        imgVideoThumb.setVisibility(View.GONE);
                                        if(w>h) {
                                            setVideoLayout(h, w);
                                        }
                                        else
                                        {
                                            playerView.setMinimumWidth(w);
                                            playerView.setMinimumHeight(h);
                                        }

                                    }

                                    @Override
                                    public void onSurfaceSizeChanged(int width, int height) {
                                        //  showToast("surface changed");
                                        //   setVideoLayout(height,width);
                                        // setVideoLayout(height,width);
                                    }

                                    @Override
                                    public void onRenderedFirstFrame() {

                                    }
                                });







                                //playerView.setMinimumHeight(hh);
                                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(context, getApplicationContext().getString(R.string.app_name)));
                                MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                                        .createMediaSource(Uri.parse(videoUrl));

                                player.prepare(videoSource);
                                player.setPlayWhenReady(true);



                                //      player.setRepeatMode(Player.REPEAT_MODE_ALL);
                            }
                            else
                            {
                                imgStory.setVisibility(View.GONE);
                            }

                            getStoryNew();

                        }
                    }





                }

                @Override
                public void onFailure(Call<UserProfileRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }


    private void callFollowUser()
    {
        showLoading();
        ReqFollowUser reqFollowUser=new ReqFollowUser() ;
        reqFollowUser.setUserId(userId);
        reqFollowUser.setFollowerUserId(id);
        ApiInterface apiInterface=ApiClientExplore.getClient().create(ApiInterface.class);
        Call<Root> call;

            call = apiInterface.reqsendUserFollow(accessToken, reqFollowUser);



        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response)
            {
                dismiss_loading();
                if(response.body()!=null)
                {
                    if (response.body().getStatus() == 200)
                    {
                        followStatus=true;
                        statusSubscribe();
                    }

                }
                else
                {

                }

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });


    }

    private void callUnFolloowUser()
    {
        showLoading();
        ReqFollowUser reqFollowUser=new ReqFollowUser() ;
        reqFollowUser.setUserId(userId);
        reqFollowUser.setFollowerUserId(id);
        ApiInterface apiInterface=ApiClientExplore.getClient().create(ApiInterface.class);
        Call<Root> call = apiInterface.reqsendUserUnFollow(accessToken, reqFollowUser);



        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response)
            {
                dismiss_loading();
                if(response.body()!=null)
                {
                    if (response.body().getStatus() == 200)
                    {
                        followStatus=false;
                        statusSubscribe();
                    }

                }
                else
                {

                }

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });


    }












}
