package com.app.session.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.app.session.R;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.activity.viewmodel.StoryDetailViewModel;
import com.app.session.adapter.SubscriptionStoriesAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.SendStoryBody;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.SubscriptionStories;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.SubscriptionStoriesRootBody;
import com.app.session.data.model.UserDetails;
import com.app.session.data.model.UserStory;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.database.DatabaseHelper;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;

public class OtherUserStoryDetailActivity extends BaseActivity implements View.OnClickListener, ServiceResultReceiver.Receiver {
    private StoryDetailViewModel viewModel;
    Bundle bundle;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    private ServiceResultReceiver mServiceResultReceiver;
    ArrayList<Brief_CV> brief_cvList;
    CustomTextView txtGroupName, txtPrice, txtGroupName2, txtLanguageName, txtCategoryName, txtCategoryName1, txtCurrency, txtGroupName3;
    ReadMoreTextView txtDiscription;
    SubscriptionStoriesAdapter subscriptionStoriesAdapter;
    ImageView imgGroupCover, imgVideoCover, imgProfileCover;
    CustomTextView txtUploading;
    ProgressBar progressBar;
    LinearLayout layProgress;
    ScrollView layMid;
    SubscriptionGroup subscriptionGroup = null;
    String groupiconUrl = "", imgCoverName = "", videoUrl = "", videoCoverImg = "", videoCoverImgName = "", language_cd = "", currency_cd = "", category_cd = "", subscription_group_cd = "", subscription_group_name = "";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ShimmerFrameLayout mShimmerViewContainer;
    LinkedList<SubscriptionStories> storyDataArrayList = new LinkedList<>();
    CircleImageView img_profilepic;
    private DatabaseHelper db;
    SwipeRefreshLayout swipeRefreshLayout;
    String subscription_id;

    FloatingActionButton fab;

    String userType = "";
    String userUrl = "", groupUrl = "", groupName = "", loginuserId = "";
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean isFlag = false;
    boolean refreshFlag = false;

    ImageView imageStory;
    RelativeLayout layLoading;
    ProgressView rey_loading;
    private int storyPosition = -1;
    ImageView imgSetting;
    UserSubscriptionGroupsBody UserSubscriptionGroupsBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        setupViewModel();
        setupObserver();
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("BUNDLE");
        if (bundle != null) {
            brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");
        }
        if (getIntent().getStringExtra("ID") != null) {
            subscription_group_cd = getIntent().getStringExtra("ID");
            subscription_id = getIntent().getStringExtra("ID");
            log("subscription_id: " + subscription_id);
            ((CustomTextView) findViewById(R.id.header)).setText(getIntent().getStringExtra("USER_NAME"));
        }

        if (getIntent().getStringExtra("USER_URL") != null) {
            userUrl = getIntent().getStringExtra("USER_URL");
        }
        if (getIntent().getStringExtra("GROUP_IMAGE") != null) {
            groupiconUrl = getIntent().getStringExtra("GROUP_IMAGE");
        }
        if (getIntent().getStringExtra("GROUP_NAME") != null) {
            groupName = getIntent().getStringExtra("GROUP_NAME");
        }
        if (getIntent().getStringExtra("USER_ID") != null) {
            loginuserId = getIntent().getStringExtra("USER_ID");
        }

        if (getIntent().getStringExtra("OTHER") != null) {
            userType = getIntent().getStringExtra("OTHER");
        }
        getGroupData();
        initView();
        setupStoryRecylerview();
        setUpRecyclerListener();
        setSwipeLayout();
    }

    private void getGroupData() {

        if (getIntent().getParcelableExtra(Constant.SUBS_GROUP) != null) {
            UserSubscriptionGroupsBody = getIntent().getParcelableExtra(Constant.SUBS_GROUP);
        }

    }

    private void initView() {
        imgSetting = (ImageView) findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(this);
        layLoading = (RelativeLayout) findViewById(R.id.layLoading);
        imageStory = (ImageView) findViewById(R.id.imgStory);
        rey_loading = (ProgressView) findViewById(R.id.rey_loading);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        swipeRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        img_profilepic = (CircleImageView) findViewById(R.id.img_profilepic);
        if (profileUrl != null && !profileUrl.isEmpty()) {
            Picasso.with(context).load(Urls.BASE_IMAGES_URL + userUrl).into(img_profilepic);
        }
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        txtGroupName3 = (CustomTextView) findViewById(R.id.txtGroupName3);
        txtCurrency = (CustomTextView) findViewById(R.id.txtCurrency);
        txtCategoryName = (CustomTextView) findViewById(R.id.txtCategoryName);
        txtCategoryName1 = (CustomTextView) findViewById(R.id.txtCategoryName1);
        txtLanguageName = (CustomTextView) findViewById(R.id.txtLanguageName);
        txtGroupName2 = (CustomTextView) findViewById(R.id.txtGroupName2);
        txtPrice = (CustomTextView) findViewById(R.id.txtPrice);
        txtGroupName = (CustomTextView) findViewById(R.id.txtGroupName);
        txtDiscription = (ReadMoreTextView) findViewById(R.id.txtDiscription);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        txtDiscription.setTypeface(face);


        txtDiscription.setTrimCollapsedText(" more");
        txtDiscription.setTrimExpandedText(" less");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtUploading = (CustomTextView) findViewById(R.id.txtUploading);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        imgGroupCover = (ImageView) findViewById(R.id.imgGroupCover);
        imgProfileCover = (ImageView) findViewById(R.id.imgProfileCover);
        imgVideoCover = (ImageView) findViewById(R.id.imgVideoCover);
        imgVideoCover.setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgMenu)).setOnClickListener(this);

        layMid = (ScrollView) findViewById(R.id.layMid);
        ((ImageView) findViewById(R.id.imgEditdisc)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgEditDetail)).setOnClickListener(this);

        if (userType.equals("0")) {
            fab.hide();
        } else {
            fab.show();
        }
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
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

    private void setupStoryRecylerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        subscriptionStoriesAdapter = new SubscriptionStoriesAdapter(context, storyDataArrayList, groupName, groupiconUrl, new ObjectCallback() {
            @Override
            public void getObject(Object object, int position, View view) {
                SubscriptionStories story = (SubscriptionStories) object;
                story.setStory_provider("2");
                storyPosition = position;
                if (position == -1) {
                    startActivity(new Intent(context, SubscriptionGroupProfileActivity.class));
                } else {

                    if (view.getId() == R.id.imgRemove) {
                        if (story.getUserId().getId().equals(userId)) {
                            showMenu(view, story, position, 1);
                        } else {
                            showMenu(view, story, position, 0);
                        }
                    } else {

                        UserStory userStory = new UserStory();
                        userStory.setStory_provider("2");
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
                        UserDetails details = new UserDetails();
                        details.setId(story.getUserId().getId());
                        userStory.setUserDetails(details);
                        Intent intent = new Intent(context, ShowStoryActivity.class);
                        intent.putExtra("DATA", userStory);
                        startActivityForResult(intent, Constant.PAGE_REFRESH);
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

                viewModel.page = 1;
                swipeRefreshLayout.setRefreshing(true);
                storyDataArrayList = new LinkedList<>();
                callApi();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        if (!refreshFlag) {
            //storyDataArrayList=new LinkedList<>();
            viewModel.page = 1;
            callApi();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imgVideoCover:
                if (!videoUrl.isEmpty()) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    String url = Urls.BASE_VIDEO_URL + videoUrl;
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                break;


            case R.id.fab:
                refreshFlag = true;
                addNewStory();
                break;

            case R.id.imgSetting:


//                intent.putExtra("ID", subscription_group_cd);
//                intent.putExtra("NAME", subscription_group_name);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("List", (Serializable) brief_cvList);
//                intent.putExtra("BUNDLE", bundle);
//                intent.putExtra(Constant.SUBS_GROUP,UserSubscriptionGroupsBody);
//                startActivity(intent);
                refreshFlag = true;
                Intent intent = new Intent(context, EditSubscriptionGroupDetailActivity.class);
                intent.putExtra("BUNDLE", bundle);
                intent.putExtra("TYPE", "1");
                intent.putExtra(Constant.SUBS_GROUP, UserSubscriptionGroupsBody);
                startActivity(intent);


                break;

        }

    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this, new ViewModelFactory(userId, accessToken)).get(StoryDetailViewModel.class);
    }

    private void callApi() {
        viewModel.getSubscriptionsStoriesApi(subscription_id);
    }

    private void setupObserver() {
        viewModel.getSubscriptionStoriesMutableLiveData().observe(this, new Observer<SubscriptionStoriesRoot>() {
            @Override
            public void onChanged(SubscriptionStoriesRoot subscriptionStoriesRoot) {
                setupStoryData(subscriptionStoriesRoot.getSubscriptionStoriesRootBody());
            }
        });


        viewModel.getRootMutableLiveData().observe(this, new Observer<Root>() {
            @Override
            public void onChanged(Root root) {
                subscriptionStoriesAdapter.updateData(storyPosition);
            }
        });
    }

    private void setupStoryData(SubscriptionStoriesRootBody body) {
        swipeRefreshLayout.setRefreshing(false);
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        viewModel.totalPage = body.getTotal_Page();
        LinkedList<SubscriptionStories> list = new LinkedList<>();
        if (body.getSubscriptionStories() != null) {
            list = body.getSubscriptionStories();

            if (list.size() > 0) {
                if (viewModel.page <= viewModel.totalPage) {
                    loading = true;
                    viewModel.page++;
                }

                for (SubscriptionStories subscriptionStories : list) {
                    storyDataArrayList.addLast(subscriptionStories);
                }

                subscriptionStoriesAdapter.notifyDataSetChanged();


            }
        }
    }

    public void showMenu(View v, final SubscriptionStories storyData, final int position, int userType) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_delete:

                        storyPosition = position;
                        storyData.setStory_provider("2");
                        viewModel.deleteStory(storyData);


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

        if (userType == 1) {
            inflater.inflate(R.menu.menu_subscription_story, popup.getMenu());
        } else {
            inflater.inflate(R.menu.menu_view_story, popup.getMenu());
        }


        popup.show();
    }

    private void callDeleteStory(SubscriptionStories subscriptionStories, int position) {
        if (isInternetConnected()) {
            showLoading();

            ReqDeleteStory deleteStory = new ReqDeleteStory();
            deleteStory.setStoryId(subscriptionStories.get_id());
            deleteStory.setStory_provider(subscriptionStories.getStory_provider());
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<Root> call = apiInterface.reqDeleteStory(accessToken, deleteStory);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
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

    private void addNewStory() {
        //storyDataArrayList=new LinkedList<>();
        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setReceiver(this);
        Intent intent = new Intent(context, AddSubscriptionStoryActivity.class);
        Bundle arg = new Bundle();
        arg.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", arg);
        intent.putExtra("ID", subscription_id);
//        intent.putExtra("POSITION",mParam1);
        intent.putExtra(RECEIVER, mServiceResultReceiver);
        intent.setAction(ACTION_DOWNLOAD);
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


                if (loading && loaddingDone) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                        loading = false;
                        Utility.Log("inside the recly litner");
                        callApi();


                    }
                }
            }
        });
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case SHOW_RESULT:
                if (resultData != null) {
                    if (!isFlag) {

                        if (resultData.getString("TYPE").equals("anydoc")) {
                            if (resultData.getString("FILENAME").contains(".doc")) {
                                imageStory.setImageResource(R.mipmap.docs_story);
                            } else if (resultData.getString("FILENAME").contains(".pdf")) {
                                imageStory.setImageResource(R.mipmap.pdf_story);
                            } else if (resultData.getString("FILENAME").contains(".zip")) {
                                imageStory.setImageResource(R.mipmap.zip_story);
                            } else if (resultData.getString("FILENAME").contains(".xls")) {
                                imageStory.setImageResource(R.mipmap.xls_story);
                            }

                        } else if (resultData.getString("TYPE").equals("image") || resultData.getString("TYPE").equals("video")) {
                            Bitmap bm = resultData.getParcelable("IMAGE");
                            imageStory.setImageBitmap(bm);
                            imageStory.setImageBitmap(bm);
                        } else if (resultData.getString("TYPE").equals("audio")) {
                            imageStory.setImageResource(R.mipmap.attach_audio);
                        }


                        isFlag = true;
                    }
                    layLoading.setVisibility(View.VISIBLE);
                    rey_loading.start();
                }
                break;

            case STATUS:
                rey_loading.stop();
                layLoading.setVisibility(View.GONE);
                showToast("File is uploaded ");

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();

                if (resultData != null) {
                    String data = resultData.getString("DATA");
                    System.out.println("data " + data);
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONObject body = object.getJSONObject("body");
                        SendStoryBody sendStoryBody = new SendStoryBody();
                        sendStoryBody.setStory_text(body.getString("story_text"));
                        sendStoryBody.setStory_title(body.getString("story_title"));
                        sendStoryBody.setStory_type(body.getString("story_type"));
                        sendStoryBody.setStory_url(body.getString("story_url"));
                        sendStoryBody.setThumbnail_url(body.getString("thumbnail_url"));
                        //sendStoryBody.setViews(body.getString("views"));
                        sendStoryBody.set_id(body.getString("_id"));
                        sendStoryBody.setUser_id(body.getString("user_id"));
                        sendStoryBody.setSubscription_id(body.getString("subscription_id"));
                        sendStoryBody.setDisplay_doc_name(body.getString("display_doc_name"));

                        SubscriptionStories subscriptionStories = new SubscriptionStories();
                        subscriptionStories.set_id(sendStoryBody.get_id());
                        subscriptionStories.setCreatedAt(sendStoryBody.getCreatedAt());
                        subscriptionStories.setDaysAgo("0");
                        subscriptionStories.setViews("0");
                        subscriptionStories.setStoryText(sendStoryBody.getStory_text());
                        subscriptionStories.setStoryTitle(sendStoryBody.getStory_title());
                        subscriptionStories.setStoryType(sendStoryBody.getStory_type());
                        subscriptionStories.setStoryUrl(sendStoryBody.getStory_url());
                        subscriptionStories.setDisplay_doc_name(sendStoryBody.getDisplay_doc_name());
                        subscriptionStories.setThumbnail_url(sendStoryBody.getThumbnail_url());


                        UserDetails details = new UserDetails();
                        details.setId(sendStoryBody.get_id());
                        details.setUserName(user_name);
                        details.setImageUrl(profileUrl);
                        //subscriptionStories.setUserDetails(details);

                        storyDataArrayList.addFirst(subscriptionStories);

                        recyclerView.smoothScrollToPosition(0);
                        recyclerView.scrollToPosition(0);
                        subscriptionStoriesAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//
                }


                break;

            case FAIL:

                rey_loading.stop();
                layLoading.setVisibility(View.GONE);
                showToast("uploading is fail, please try again");
//                layProgress.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_NEW_STORY) {

            if (data != null) {
                {

                    if (data.getParcelableExtra("DATA") != null) {
                        refreshFlag = true;
                        SendStoryBody story = data.getParcelableExtra("DATA");
                        SubscriptionStories storyModel = new SubscriptionStories();
                        storyModel.set_id(story.get_id());
                        storyModel.setCreatedAt(story.getCreatedAt());
                        storyModel.setDaysAgo("0");
                        storyModel.setViews("0");
                        storyModel.setStoryText(story.getStory_text());
                        storyModel.setStoryTitle(story.getStory_title());
                        storyModel.setStoryType(story.getStory_type());
                        storyModel.setStoryUrl(story.getStory_url());
                        UserDetails details = new UserDetails();
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
        if (requestCode == Constant.PAGE_REFRESH) {
            refreshFlag = true;
            if (data != null) {
                subscriptionStoriesAdapter.updateData(storyPosition);
                // refreshFlag = data.getBooleanExtra("REFRESH", false);
                Utility.log("refreshFlag : " + refreshFlag);
            }


        }
    }
}
