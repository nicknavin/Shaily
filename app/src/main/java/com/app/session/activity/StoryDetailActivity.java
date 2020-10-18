package com.app.session.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.app.session.R;
import com.app.session.adapter.SubscriptionStoriesAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.database.DatabaseHelper;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Brief_CV;
import com.app.session.model.ReqDeleteStory;
import com.app.session.model.ReqSubscriptionStories;
import com.app.session.model.Root;
import com.app.session.model.SendStoryBody;
import com.app.session.model.SubscriptionGroup;
import com.app.session.model.SubscriptionStories;
import com.app.session.model.SubscriptionStoriesRoot;
import com.app.session.model.UserDetails;
import com.app.session.model.UserStory;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryDetailActivity extends BaseActivity implements View.OnClickListener {
    Bundle bundle;
    ArrayList<Brief_CV>brief_cvList;
    CustomTextView txtGroupName,txtPrice,txtGroupName2,txtLanguageName,txtCategoryName,txtCategoryName1,txtCurrency,txtGroupName3;
    ReadMoreTextView txtDiscription;

    SubscriptionStoriesAdapter subscriptionStoriesAdapter;
    ImageView imgGroupCover,imgVideoCover,imgProfileCover;
    CustomTextView txtUploading;
    ProgressBar progressBar;
    LinearLayout layProgress;
    ScrollView layMid;
    SubscriptionGroup subscriptionGroup =null;
    String groupiconUrl = "", imgCoverName="",videoUrl = "", videoCoverImg = "", videoCoverImgName="",language_cd = "", currency_cd = "", category_cd = "",subscription_group_cd="",subscription_group_name="";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ShimmerFrameLayout mShimmerViewContainer;
    LinkedList<SubscriptionStories> storyDataArrayList=new LinkedList<>();
    CircleImageView img_profilepic;
    private DatabaseHelper db;
    SwipeRefreshLayout swipeRefreshLayout;
    String subscription_id;
    int load=0;
    FloatingActionButton fab;

    String userType="";
    String userUrl="",groupUrl="",groupName="",loginuserId="";
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    int total_pages=0;
    boolean refreshFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("BUNDLE");
        if(bundle!=null) {
            brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");
        }
        if (getIntent().getStringExtra("ID") != null)
        {
            subscription_group_cd=getIntent().getStringExtra("ID");
            subscription_id=getIntent().getStringExtra("ID");
             ((CustomTextView)findViewById(R.id.header)).setText(getIntent().getStringExtra("USER_NAME"));

        }

        if(getIntent().getStringExtra("USER_URL")!=null)
        {
            userUrl=getIntent().getStringExtra("USER_URL");
        }
        if(getIntent().getStringExtra("GROUP_IMAGE")!=null)
        {
          groupiconUrl=  getIntent().getStringExtra("GROUP_IMAGE");
        }
        if(getIntent().getStringExtra("GROUP_NAME")!=null)
        {
          groupName=  getIntent().getStringExtra("GROUP_NAME");
        } if(getIntent().getStringExtra("USER_ID")!=null)
        {
            loginuserId=  getIntent().getStringExtra("USER_ID");
        }

        if(getIntent().getStringExtra("OTHER")!=null)
        {
            userType=getIntent().getStringExtra("OTHER");
        }
        initView();
        setupStoryRecylerview();
        setUpRecyclerListener();
        setSwipeLayout();
//        callGetSubscriptionGroup();
//        getSubscriptionGroup();

    }

    private void initView()
    {
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
        swipeRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        img_profilepic=(CircleImageView)findViewById(R.id.img_profilepic);
        if(profileUrl!=null&&!profileUrl.isEmpty())
        {
            Picasso.with(context).load(Urls.BASE_IMAGES_URL+userUrl).into(img_profilepic);
        }
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        txtGroupName3=(CustomTextView)findViewById(R.id.txtGroupName3);
        txtCurrency=(CustomTextView)findViewById(R.id.txtCurrency);
        txtCategoryName=(CustomTextView)findViewById(R.id.txtCategoryName);
        txtCategoryName1=(CustomTextView)findViewById(R.id.txtCategoryName1);
        txtLanguageName=(CustomTextView)findViewById(R.id.txtLanguageName);
        txtGroupName2=(CustomTextView)findViewById(R.id.txtGroupName2);
        txtPrice=(CustomTextView)findViewById(R.id.txtPrice);
        txtGroupName=(CustomTextView)findViewById(R.id.txtGroupName);
        txtDiscription=(ReadMoreTextView) findViewById(R.id.txtDiscription);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        txtDiscription .setTypeface(face);


        txtDiscription .setTrimCollapsedText(" more");
        txtDiscription .setTrimExpandedText(" less");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtUploading = (CustomTextView) findViewById(R.id.txtUploading);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        imgGroupCover=(ImageView)findViewById(R.id.imgGroupCover);
        imgProfileCover=(ImageView)findViewById(R.id.imgProfileCover);
        imgVideoCover=(ImageView)findViewById(R.id.imgVideoCover);
        imgVideoCover.setOnClickListener(this);
        ((ImageView)findViewById(R.id.imgMenu)).setOnClickListener(this);

        layMid=(ScrollView)findViewById(R.id.layMid);
        ((ImageView)findViewById(R.id.imgEditdisc)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.imgEditDetail)).setOnClickListener(this);

        if(userType.equals("0"))
        {
            fab.hide();
        }
        else
        {
            fab.show();
        }
        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        if(db.getNotesCount()>0)
//        {
//            List<StoryDb> sd=   db.getAllNotes();
//            String data= sd.get(0).getNote();
//
//            try {
//                JSONObject jsonObject=new JSONObject(data);
//                JSONArray jsonArray = jsonObject.getJSONArray("story_data");
//                showStoryData(jsonArray);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }




    }





    private void setupStoryRecylerview()
    {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        subscriptionStoriesAdapter=new SubscriptionStoriesAdapter(context, storyDataArrayList, groupName, groupiconUrl, new ObjectCallback() {
            @Override
            public void getObject(Object object, int position,View view)
            {
                SubscriptionStories story=(SubscriptionStories)object;
                if(position==-1)
                {
                    startActivity(new Intent(context, SubscriptionGroupProfileActivity.class));
                }
                else {

                    if(view.getId()==R.id.imgRemove)
                    {
                        if(story.getUserId().getId().equals(userId))
                        {
                            showMenu(view,story,position,1);
                        }
                        else
                        {
                            showMenu(view,story,position,0);
                        }
                    }
                    else
                    {

                        UserStory userStory=new UserStory();
                        userStory.setId(story.get_id());
                        userStory.setCreatedAt(story.getCreatedAt());
                        userStory.setStoryText(story.getStoryText());
                        userStory.setStoryTitle(story.getStoryTitle());
                        userStory.setStoryType(story.getStoryType());
                        userStory.setViews(story.getViews());
                        userStory.setStoryUrl(story.getStoryUrl());
                        userStory.setSubscriptionId(story.getSubscriptionId());
                        userStory.setThumbnail_url(story.getThumbnail_url());
                        userStory.setDisplay_doc_name(story.getDisplay_doc_name());
                        UserDetails details=new UserDetails();
                        details.setId(story.getUserId().getId());
                        userStory.setUserDetails(details);
                        Intent intent=new Intent(context,ShowStoryActivity.class);
                        intent.putExtra("DATA",userStory);
                        startActivityForResult(intent,Constant.PAGE_REFRESH);
                    }



                }

            }
        });
        recyclerView.setAdapter(subscriptionStoriesAdapter);
    }



    public void setSwipeLayout() {

        swipeRefreshLayout.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                load=1;
                swipeRefreshLayout.setRefreshing(true);
                storyDataArrayList=new LinkedList<>();
                getStory();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        if(!refreshFlag)
        {
            load = 1;
            getStory();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.imgVideoCover:
                if(!videoUrl.isEmpty()) {
                 Intent   intent = new Intent(context, VideoPlayerActivity.class);
                    String url = Urls.BASE_VIDEO_URL + videoUrl;
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                break;


            case R.id.fab:
                addNewStory();
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_NEW_STORY)
        {

            if (data != null) {
                {

                    if(data.getParcelableExtra("DATA")!=null)
                    {
                        refreshFlag=true;
                        SendStoryBody story=data.getParcelableExtra("DATA");
                        SubscriptionStories storyModel=new SubscriptionStories();
                        storyModel.set_id(story.get_id());
                        storyModel.setCreatedAt(story.getCreatedAt());
                        storyModel.setDaysAgo("0");
                        storyModel.setViews("0");
                        storyModel.setStoryText(story.getStory_text());
                        storyModel.setStoryTitle(story.getStory_title());
                        storyModel.setStoryType(story.getStory_type());
                        storyModel.setStoryUrl(story.getStory_url());
                        UserDetails details=new UserDetails();
                        details.setId(story.get_id());
                        details.setUserName(user_name);
                        details.setImageUrl(profileUrl);
                        storyModel.setUserId(details);
                        storyDataArrayList.addFirst(storyModel);

                        subscriptionStoriesAdapter.notifyDataSetChanged();
                    }
//                    storyModelsList


//                    ((MyProfileActivityNew)getActivity()).getUserStory();


                }
            }
        }
        if(requestCode==Constant.PAGE_REFRESH)
        {
            refreshFlag=true;
        }
    }

    private String getparamSubcriptionGroup()
    {
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("subscription_group_cd",subscription_group_cd);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();


    }




    private JsonObject getSubsGroupParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("subscription_id",subscription_id);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }




    private void callGetStory()
    {
        if (isInternetConnected())
        {

            showLoading();
            new BaseAsych(Urls.GET_STORY_ALL, getStoryParameter(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        swipeRefreshLayout.setRefreshing(false);
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("result"+jsonObject.toString());
                        if (jsonObject.getString("rstatus").equals("1"))
                        {
                            try {
                                if(db.getNotesCount()>0)
                                {
                                    db.deleteNote();
                                }
                                db.insertNote(js.toString());
                                JSONArray jsonArray = js.getJSONArray("story_data");

                                //showStoryData(jsonArray);


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






    private void getStory() {
        if (Utility.isConnectingToInternet(context))
        {

            ReqSubscriptionStories reqSubscriptionStories=new ReqSubscriptionStories();
            reqSubscriptionStories.setSubscriptionId(subscription_id);
            reqSubscriptionStories.setUser_id(userId);
            reqSubscriptionStories.setLoad(""+load);

            showLoading();
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SubscriptionStoriesRoot> call = apiInterface.reqGetSubscriptionStories(accessToken, reqSubscriptionStories);
            call.enqueue(new Callback<SubscriptionStoriesRoot>() {
                @Override
                public void onResponse(Call<SubscriptionStoriesRoot> call, Response<SubscriptionStoriesRoot> response)
                {
                    dismiss_loading();
                    swipeRefreshLayout.setRefreshing(false);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    if(response.body()!=null)
                    {
                        if(response.body().getStatus()==200)
                        {
                            total_pages=response.body().getSubscriptionStoriesRootBody().getTotal_Page();


                            LinkedList<SubscriptionStories> list=new LinkedList<>();
                            list =response.body().getSubscriptionStoriesRootBody().getSubscriptionStories();

                            if(list.size()>0)
                            {
                                if(load <=total_pages) {
                                    loading=true;
                                    load++;
                                }

                                for(SubscriptionStories subscriptionStories:list)
                                {
                                    storyDataArrayList.addLast(subscriptionStories);
                                }

                                subscriptionStoriesAdapter.notifyDataSetChanged();


                            }
//                            else
//                            {
//                                subscriptionStoriesAdapter=new SubscriptionStoriesAdapter(context, storyDataArrayList, groupName, groupiconUrl, new ObjectCallback() {
//                                    @Override
//                                    public void getObject(Object object, int position,View view)
//                                    {
//                                        SubscriptionStories story=(SubscriptionStories)object;
//                                        if(position==-1)
//                                        {
//                                            startActivity(new Intent(context, SubscriptionGroupProfileActivity.class));
//                                        }
//                                        else {
//
//                                            if(view.getId()==R.id.imgRemove)
//                                            {
//                                                if(story.getUserId().getId().equals(userId))
//                                                {
//                                                    showMenu(view,story,position,1);
//                                                }
//                                                else
//                                                {
//                                                    showMenu(view,story,position,0);
//                                                }
//                                            }
//                                            else
//                                            {
//
//                                                UserStory userStory=new UserStory();
//                                                userStory.setId(story.get_id());
//                                                userStory.setCreatedAt(story.getCreatedAt());
//                                                userStory.setStoryText(story.getStoryText());
//                                                userStory.setStoryTitle(story.getStoryTitle());
//                                                userStory.setStoryType(story.getStoryType());
//                                                userStory.setViews(story.getViews());
//                                                userStory.setStoryUrl(story.getStoryUrl());
//                                                userStory.setSubscriptionId(story.getSubscriptionId());
//                                                UserDetails details=new UserDetails();
//                                                details.setId(story.getUserId().getId());
//                                                userStory.setUserDetails(details);
//                                                Intent intent=new Intent(context,ShowStoryActivity.class);
//                                                intent.putExtra("DATA",userStory);
//                                                startActivity(intent);
//                                            }
//
//
//
//                                        }
//
//                                    }
//                                });
//                                recyclerView.setAdapter(subscriptionStoriesAdapter);
//                            }

                        }
                    }

                }

                @Override
                public void onFailure(Call<SubscriptionStoriesRoot> call, Throwable t) {
                   dismiss_loading();
                }
            });



        } else {
            showInternetConnectionToast();
        }
    }
















    public void showMenu(View v, final SubscriptionStories storyData, final int position,int userType ) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_delete:

                        callDeleteStory(storyData.get_id(), position);

                        return true;

                    case R.id.menu_edit:
//                        Intent intent = new Intent(context, UpdateSubscriptionStoryActivity.class);
//                        intent.putExtra("DATA", storyData);
//                        Bundle arg = new Bundle();
//                        arg.putSerializable("List", (Serializable) brief_cvList);
//                        intent.putExtra("BUNDLE", arg);
//                        intent.putExtra("ID", storyData.getId());
//                        startActivity(intent);
                        return true;


                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();

        if(userType==1)
        {
            inflater.inflate(R.menu.menu_subscription_story, popup.getMenu());
        }
        else
        {
            inflater.inflate(R.menu.menu_view_story, popup.getMenu());
        }



        popup.show();
    }




    private void callDeleteStory(String story_id, int position) {
        if (isInternetConnected()) {
            showLoading();

            ReqDeleteStory deleteStory=new ReqDeleteStory();
            deleteStory.setStoryId(story_id);
            ApiInterface apiInterface=ApiClientProfile.getClient().create(ApiInterface.class);
            Call<Root> call= apiInterface.reqDeleteStory(accessToken,deleteStory);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        if(response.body().getStatus()==200)
                        {
                            subscriptionStoriesAdapter.updateData(position);

                        }


                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {

                }
            });



        } else {
            showInternetConnectionToast();
        }
    }

//    private void showStoryData(JSONArray jsonArray) {
//        Type type = new TypeToken<ArrayList<StoryData>>() {
//        }.getType();
//
//        storyDataArrayList = new Gson().fromJson(jsonArray.toString(), type);
//        subscriptionStoryAdapter=new SubscriptionStoryAdapter(context, storyDataArrayList, user_name, groupiconUrl, new ObjectCallback() {
//            @Override
//            public void getObject(Object object, int position,View view)
//            {
//                if(position==-1) {
//                    startActivity(new Intent(context, SubscriptionGroupProfileActivity.class));
//                }
//                else {
//                    showMenu(view);
//                }
//
//            }
//        });
//        recyclerView.setAdapter(subscriptionStoryAdapter);
//    }

    private void addNewStory() {
        Intent intent = new Intent(context, AddSubscriptionStoryActivity.class);
        Bundle arg = new Bundle();
        arg.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", arg);
        intent.putExtra("ID", subscription_id);
//        intent.putExtra("POSITION",mParam1);
        startActivityForResult(intent, Constant.REQUEST_NEW_STORY);

    }



    public void setUpRecyclerListener() {
        loading = true;
        loaddingDone = true;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();


                if (loading && loaddingDone)
                {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {

                        loading = false;
                        Utility.Log("inside the recly litner");
                        getStory();


                    }
                }
            }
        });
    }

}
