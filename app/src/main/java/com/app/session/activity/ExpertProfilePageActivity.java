package com.app.session.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.app.session.BuildConfig;
import com.app.session.R;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.activity.viewmodel.ExpertProfileViewModel;
import com.app.session.adapter.ExpertUserSubscriptionStoryAdapter;
import com.app.session.adapter.OtherSubscribeGroupAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.DynamicHeightViewPager;
import com.app.session.data.model.UndoSubscriptionRoot;
import com.app.session.data.model.UnsubscribedRoot;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.data.model.UserSubscriptionGroupsRoot;
import com.app.session.fragment.UserBriefCVFragment;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.data.model.BriefCV;
import com.app.session.data.model.OtherUserId;
import com.app.session.data.model.OtherUserProfile;
import com.app.session.data.model.PersonalUserStory;
import com.app.session.data.model.ReqFollowUser;
import com.app.session.data.model.ReqUserProfile;
import com.app.session.data.model.Root;
import com.app.session.data.model.StoryBody;
import com.app.session.data.model.StoryModel;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscriptionGroupId;
import com.app.session.data.model.SubscriptionId;
import com.app.session.data.model.UserLangauges;
import com.app.session.data.model.UserProfileBody;
import com.app.session.data.model.UserProfileRoot;
import com.app.session.data.model.UserStory;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpertProfilePageActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {



    RecyclerView recyclerView, recyclerViewGroup;
    LinearLayout laySubsView;
    CustomTextView txtSubscriber1;
    ExpertUserSubscriptionStoryAdapter userSubscriptionStoryAdapter;
    CustomTextView txtUserName, txtExplore;
    CircleImageView imgProfile;
    CheckBox imgDrop;
    String groupiconUrl = "";
    CustomTextView txtBio,  txtSubscriber;
    //    ImageView imgStory;
    ShimmerFrameLayout mShimmerViewContainer;
    ImageView imgVideoThumb;
    ArrayList<SubscriptionGroupId> listSubsIds = new ArrayList<>();
    LinkedList<UserStory> storyDataArrayList = new LinkedList<>();
    public boolean flag = false;
    String id = "", name = "", url = "", loginIDName = "";
    boolean followStatus,isEligiblefor24;

    ExpertProfileViewModel viewModel;
    ArrayList<UserLangauges> userLangaugesArrayList = new ArrayList<>();
    ArrayList<BriefCV> briefCVArrayList = new ArrayList<>();
    private TabLayout tabLayout;
    private DynamicHeightViewPager viewPager;
    private ViewPagerAdapter adapter;
    private MyProfileActivityNew.FragmentRefreshListener fragmentRefreshListener;
    LinearLayout layDrop;
    int userRegister = 0;

    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    int pageno = 1;
    int total_pages = 0;
    public LinkedList<StoryModel> allStories = new LinkedList<>();
    LinearLayoutManager linearLayoutManager;
    UserBriefCVFragment userBriefCVFragment;

    ImageView imgChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_expert_profile_page);

        if (getIntent().getStringExtra("ID") != null) {
            id = getIntent().getStringExtra("ID");
        }
        setupViewModel();

        if (getIntent().getStringExtra("NAME") != null) {
            loginIDName = getIntent().getStringExtra("NAME");
        }
        if (getIntent().getStringExtra("URL") != null) {
            url = getIntent().getStringExtra("URL");
        }

        initView();

        if (userId.isEmpty()) {
            getUserProfile();
        } else {
            getOtherUserDetail();
        }

    }


    private void initView() {
        recyclerViewGroup = (RecyclerView) findViewById(R.id.recyclerViewGroup);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewGroup.setLayoutManager(layoutManager);
        layBio = (ConstraintLayout) findViewById(R.id.layBio);
        imgChat = (ImageView) findViewById(R.id.imgChat);
        imgChat.setVisibility(View.VISIBLE);
        laySubsView = (LinearLayout) findViewById(R.id.laySubsView);
        layDrop = (LinearLayout) findViewById(R.id.layDrop);
        layDrop.setOnClickListener(this);
        imgDrop = (CheckBox) findViewById(R.id.imgDrop);

        fragmentRefreshListener = getFragmentRefreshListener();
        viewPager = (DynamicHeightViewPager) findViewById(R.id.viewpager_favourite);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_favourite);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
//        imgStory = (ImageView) findViewById(R.id.imgStory);

        txtBio = (CustomTextView) findViewById(R.id.txtBio);

        txtSubscriber1 = (CustomTextView) findViewById(R.id.txtSubscriber1);
        txtSubscriber = (CustomTextView) findViewById(R.id.txtSubscriber);
        txtSubscriber.setOnClickListener(this);

        if (userId.equals(id)) {
            txtSubscriber.setVisibility(View.GONE);
            imgChat.setVisibility(View.GONE);
        }

        if (userId.isEmpty()) {
            imgChat.setVisibility(View.GONE);
        }
        txtExplore = (CustomTextView) findViewById(R.id.txtExplore);
        txtUserName = (CustomTextView) findViewById(R.id.txtGroupName);
        imgProfile = (CircleImageView) findViewById(R.id.imgGroupCover);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(context,SubscriptionGroupProfileActivity.class));
            }
        });
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("NAME", loginIDName);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });


        if (userId.isEmpty()) {

            laySubsView.setVisibility(View.GONE);


        } else {
            laySubsView.setVisibility(View.VISIBLE);
        }

        setupObserver();
//        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));


    }

    private void initTablayout() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setCustomTextView();
        setupStoryRecylerview();
        setUpRecyclerListener();
        getUserStoryNew();
    }

    public void setCustomTextView() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            CustomTextView tv = (CustomTextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_brief, null);
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            // p.setMargins(10, 0, 10, 0);
            tab.requestLayout();
            tabLayout.getTabAt(i).setCustomView(tv);
        }
    }



    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < briefCVArrayList.size(); i++) {
            BriefCV briefCV = briefCVArrayList.get(i);
            userBriefCVFragment = UserBriefCVFragment.newInstance(briefCV);
            adapter.addFragment(userBriefCVFragment, briefCVArrayList.get(i).getmLanguageId().getNativeName());

        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.getItem(position).isVisible();

                if (imgDrop.isChecked()) {
//                    fragmentRefreshListener.onRefresh(false);
                    UserBriefCVFragment fragment = (UserBriefCVFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    //  fragmentRefreshListener.onRefresh(true);
                    fragment.setVisibleBio(true);

                } else {
//                    fragmentRefreshListener.onRefresh(false);
                    UserBriefCVFragment fragment = (UserBriefCVFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    //  fragmentRefreshListener.onRefresh(true);
                    fragment.setVisibleBio(false);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private int mCurrentPosition = -1;
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);

            if (position != mCurrentPosition && container instanceof DynamicHeightViewPager) {
                Fragment fragment = (Fragment) object;
                DynamicHeightViewPager pager = (DynamicHeightViewPager) container;

                if (fragment != null && fragment.getView() != null) {
                    mCurrentPosition = position;
                    pager.measureCurrentView(fragment.getView());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void statusSubscribe() {

        if (followStatus) {
            txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber_select));
        } else {
            txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber));
        }
        if(isEligiblefor24)
                            {
                                txtSubscriber.setText("Panding to Unsubscribe");
                                txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber_select));
                            }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {

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


    private void getOtherUserDetail() {
        if (Utility.isConnectingToInternet(context)) {
            showLoading();
            ReqUserProfile user = new ReqUserProfile();
            user.setUserId(userId);
            user.setPersonUserId(id);
            log(user.toString());
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<UserProfileRoot> call = apiInterface.reqGetUserProfile(user);
            call.enqueue(new retrofit2.Callback<UserProfileRoot>() {
                @Override
                public void onResponse(Call<UserProfileRoot> call, Response<UserProfileRoot> response) {
                    dismiss_loading();

                    if (response.body() != null) {
                        UserProfileRoot root = response.body();
                        if (root.getStatus() == 200) {
                            UserProfileBody userBody = root.getUserBody();
                            OtherUserProfile user = userBody.getUser();
                            log("user profile isEligiblefor24 : "+userBody.isEligiblefor24());
                            txtSubscriber1.setText("" + userBody.getFollowers());
                            isEligiblefor24=root.getUserBody().isEligiblefor24();

                            followStatus = root.getUserBody().isFollow();
                            statusSubscribe();
                            txtUserName.setText(user.getUser_name());
                            loginIDName = user.getLogin_user_id();
                            txtExplore.setText(user.getUser_name());
                            if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                groupiconUrl = Urls.BASE_IMAGES_URL + user.getImageUrl();
                                Picasso.with(context).load(groupiconUrl).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(imgProfile);
                            }

                            listSubsIds = user.getSubscription_group_id();

                            userLangaugesArrayList = user.getUserLangauges();
                            briefCVArrayList = user.getBriefCV();

                            txtBio.setText(user.getUserLangauges().get(0).getName());
                            initTablayout();


                        }
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            String data = responseBody.string();

                            System.out.println("error1" + data);
                            JSONObject jsonObject = new JSONObject(data);
                            System.out.println("error2" + jsonObject.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void getUserProfile() {
        if (Utility.isConnectingToInternet(context)) {
            showLoading();
            OtherUserId user = new OtherUserId();
            user.setPerson_user_id(id);
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<UserProfileRoot> call = apiInterface.reqGetPersonProfile(user);
            call.enqueue(new retrofit2.Callback<UserProfileRoot>() {
                @Override
                public void onResponse(Call<UserProfileRoot> call, Response<UserProfileRoot> response) {
                    dismiss_loading();

                    if (response.body() != null) {
                        UserProfileRoot root = response.body();
                        if (root.getStatus() == 200) {
                            UserProfileBody userBody = root.getUserBody();
                            OtherUserProfile user = userBody.getUser();
                            txtSubscriber1.setText("" + userBody.getFollowers());
                            followStatus = root.getUserBody().isFollow();
                            statusSubscribe();
                            txtUserName.setText(user.getUser_name());
                            txtExplore.setText(user.getUser_name());
                            if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                groupiconUrl = Urls.BASE_IMAGES_URL + user.getImageUrl();
                                Picasso.with(context).load(groupiconUrl).placeholder(R.mipmap.profile_large).into(imgProfile);
                            }

                            listSubsIds = user.getSubscription_group_id();
                            userLangaugesArrayList = user.getUserLangauges();
                            briefCVArrayList = user.getBriefCV();
                            txtBio.setText(user.getUserLangauges().get(0).getName());
                            initTablayout();


                        }
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            String data = responseBody.string();

                            System.out.println("error1" + data);
                            JSONObject jsonObject = new JSONObject(data);
                            System.out.println("error2" + jsonObject.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
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



    private void callFollowUser() {
        showLoading();
        ReqFollowUser reqFollowUser = new ReqFollowUser();
        reqFollowUser.setUserId(userId);
        reqFollowUser.setFollowerUserId(id);
        ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
        Call<Root> call;

        call = apiInterface.reqsendUserFollow(accessToken, reqFollowUser);


        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                dismiss_loading();
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        followStatus = true;
                        statusSubscribe();
                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });


    }

    private void callUnFollowUser() {
        showLoading();
        ReqFollowUser reqFollowUser = new ReqFollowUser();
        reqFollowUser.setUserId(userId);
        reqFollowUser.setFollowerUserId(id);
        ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
        Call<Root> call = apiInterface.reqsendUserUnFollow(accessToken, reqFollowUser);


        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                dismiss_loading();
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        followStatus = false;
                        statusSubscribe();
                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });


    }


    public interface FragmentRefreshListener {
        void onRefresh(boolean flag);


    }

    public MyProfileActivityNew.FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(MyProfileActivityNew.FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSubscriber:
                if (followStatus)//true means login user follow this user
                {
                    if(!isEligiblefor24) {
                        dialogUnSubscribeDialog();
                    }


                } else {

                    if(isEligiblefor24&&!followStatus)
                    {
                        dialogUndoSubscribeDialog();

                    }
                    else
                    {
                        callFollowUser();//this for follow
                    }


                }
                break;
            case R.id.layDrop:

                if (imgDrop.isChecked()) {
                    imgDrop.setChecked(false);
                    visibleBioSection(0);
                    UserBriefCVFragment fragment = (UserBriefCVFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    //  fragmentRefreshListener.onRefresh(true);
                    fragment.setVisibleBio(false);

                } else {
                    imgDrop.setChecked(true);
//                    fragmentRefreshListener.onRefresh(false);


                    UserBriefCVFragment fragment = (UserBriefCVFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    //  fragmentRefreshListener.onRefresh(true);
                    fragment.setVisibleBio(true);
                    visibleBioSection(1);
                }
                break;
        }
    }

    private void getStoryNew() {
//        if (Utility.isConnectingToInternet(context))
//        {
//            ReqSubscribeGroupStories reqFollowUser =new ReqSubscribeGroupStories();
//            reqFollowUser.setmLoad(""+pageno);
//            reqFollowUser.setUser_id(userId);
//            ArrayList<String> list=new ArrayList<>();
//            for(int i=0;i<listSubsIds.size();i++)
//            {
//                list.add(listSubsIds.get(i).get_id());
//            }
//            reqFollowUser.setmSubscriptionId(list);
//            showLoading();
//            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
//            Call<SubscribedAllStroiesRoot> call  =apiInterface.reqGetuserSubscribedAllStroies(accessToken,reqFollowUser);
//            call.enqueue(new Callback<SubscribedAllStroiesRoot>() {
//                @Override
//                public void onResponse(Call<SubscribedAllStroiesRoot> call, Response<SubscribedAllStroiesRoot> response)
//                {
//                    UserBriefCVFragment fragment = (UserBriefCVFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
//                    dismiss_loading();
//                    if(response.body()!=null)
//                    {
//                        if(response.body().getmStatus()==200) {
//                            SubscribedAllStroiesBody subscribedAllStroiesBody = response.body().getmBody();
//                            total_pages=subscribedAllStroiesBody.getTotal_Page();
//                            ArrayList<UserStory> subscriptionStories;
//                            subscriptionStories= subscribedAllStroiesBody.getSubscriptionStories();
//
//                            if(storyDataArrayList.size()>0)
//                            {
//                                if(pageno <=total_pages)
//                                {
//                                    loading=true;
//                                    pageno++;
//                                }
//                                for(UserStory userStory:storyDataArrayList)
//                                {
//                                    allStories.addFirst(userStory);
//                                }
//
//                                userSubscriptionStoryAdapter.notifyDataSetChanged();
//                            }
//
//
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<SubscribedAllStroiesRoot> call, Throwable t) {
//
//                }
//            });
//
//
//
//
//
//
//
//        } else {
//            showInternetConnectionToast();
//        }
    }

    ConstraintLayout layBio;

    private void visibleBioSection(int visibleFlag) {
        if (visibleFlag == 0) {
            tabLayout.setVisibility(View.INVISIBLE);
            imgDrop.setChecked(false);
            viewPager.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
            imgDrop.setChecked(true);
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    public void getUserStoryNew() {
        if (Utility.isConnectingToInternet(context)) {
            PersonalUserStory personalUserStory = new PersonalUserStory();
            personalUserStory.setLoad("" + pageno);
            personalUserStory.setPersonUserId(id);
//            showLoading();
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<StoryRoot> call = apiInterface.reqGetPersonAllStrories(personalUserStory);
            call.enqueue(new Callback<StoryRoot>() {
                @Override
                public void onResponse(Call<StoryRoot> call, Response<StoryRoot> response) {

//                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            StoryBody storyBody = response.body().getStoryBody();


                            total_pages = storyBody.getTotal_Page();
                            LinkedList<StoryModel> userStories = new LinkedList<>();
                            userStories = storyBody.getUserStories();
                            System.out.println("temp size " + userStories.size());
                            System.out.println("temp page " + pageno);
                            if (userStories.size() > 0) {
                                visibleBioSection(0);
                                if (pageno <= total_pages) {
                                    loading = true;
                                    pageno++;
                                }

                                for (StoryModel storyModel : userStories) {
                                    allStories.addLast(storyModel);
                                }
                                userStories.clear();
                                userSubscriptionStoryAdapter.notifyDataSetChanged();
                            } else {
                                visibleBioSection(1);
                            }


                        }
                    }
                }

                @Override
                public void onFailure(Call<StoryRoot> call, Throwable t) {

                }
            });


        } else {
            showInternetConnectionToast();
        }
    }

    public void setupStoryRecylerview() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        userSubscriptionStoryAdapter = new ExpertUserSubscriptionStoryAdapter(context, allStories, "userName", groupiconUrl, new ObjectCallback() {
            @Override
            public void getObject(Object object, int position, View view) {
                StoryModel storyShare = (StoryModel) object;
                if (view.getId() == R.id.layDocument) {
                    url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                    String msg = storyShare.getStoryTitle() + "\n" + storyShare.getStoryText();
                    new DownloadFile(msg, storyShare.getDisplay_doc_name(), "").execute(url);
                }
               else if (view.getId() == R.id.layVideo) {

                    if (storyShare.getStoryType().equals("video_url") || storyShare.getStoryUrl().contains("youtu.be")) {
                        String id = Utility.extractYTId(storyShare.getStoryUrl());
                        Intent intent = new Intent(context, YoutubeActivity.class);
                        intent.putExtra("ID", id);
                        startActivityForResult(intent, Constant.PAGE_REFRESH);
                    }
                    if (storyShare.getStoryType().equals("video") && !storyShare.getStoryUrl().contains("youtu.be")) {
                        String videoUrl = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("URL", videoUrl);
                        startActivityForResult(intent, Constant.PAGE_REFRESH);
                    }
                }
               else if (view.getId() == R.id.imgShare) {
                    String url = "";
                    if (storyShare.getStoryType().equals("audio")) {
                        url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        String msg = storyShare.getStoryTitle() + "\n" + storyShare.getStoryText();
                        new DownloadFile(msg, "audio.mp3", "").execute(url);
                    }

                    if (storyShare.getStoryType().equals("image")) {
                        url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        showLoading();

                        Glide.with(context)
                                .asBitmap()
                                .load(url)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        dismiss_loading();
                                        if (resource != null) {
                                            Utility.sharePost(resource, storyShare, context);
                                        }
//                                    sendData(resource,lostAllPost);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                    }
                    if (storyShare.getStoryType().equals("video") && !storyShare.getStoryUrl().contains("youtu.be")) {

                    }
                    if (storyShare.getStoryType().equals("video") || storyShare.getStoryUrl().contains("youtu.be")) {
                        Utility.shareVideoUrl(storyShare, context);
                    } else if (storyShare.getStoryType().equals("anydoc")) {
                        url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        String msg = storyShare.getStoryTitle() + "\n" + storyShare.getStoryText();
                        new DownloadFile(msg, storyShare.getDisplay_doc_name(), "").execute(url);
                    }


                }
               else if (view.getId() == R.id.imgRemove) {
                    showMenu(view);
                }
                else
                {
                     UserStory data = new UserStory();

                     data.setDaysAgo(storyShare.getDaysAgo());

                     data.setCreatedAt(storyShare.getCreatedAt());
                     data.setDisplay_doc_name(storyShare.getDisplay_doc_name());
                     data.setId(storyShare.get_id());

                     data.setStoryText(storyShare.getStoryText());
                     data.setStoryTitle(storyShare.getStoryTitle());
                     data.setStoryUrl(storyShare.getStoryUrl());
                     data.setStoryType(storyShare.getStoryType());
                     data.setStoryRead(storyShare.getStoryRead());
                     data.setStory_provider(storyShare.getStory_provider());
                     //data.setStoryViewed(storyShare.getViews());
                     data.setThumbnail_url(storyShare.getThumbnail_url());
                     data.setUserDetails(storyShare.getUserDetails());

                    Intent intent = new Intent(context, StoryPageDetailActivity.class);
                    intent.putExtra("DATA", data);
                    startActivity(intent);
                }
//                else if (!userId.isEmpty())
//                {
//                    if (position == -1) {
//                        startActivity(new Intent(context, SubscriptionGroupProfileActivity.class));
//                    }
//
//                } else
//                    {
//
//
//                    StoryModel storyModel = (StoryModel) object;
//                    UserStory userStory = new UserStory();
//                    SubscriptionId subscriptionId = new SubscriptionId();
//                    userStory.setSubscriptionId(subscriptionId);
//                    userStory.setCreatedAt(storyModel.getCreatedAt());
//                    userStory.setDaysAgo(storyModel.getDaysAgo());
//                    userStory.setId(storyModel.get_id());
//                    userStory.setStoryText(storyModel.getStoryText());
//                    userStory.setStoryTitle(storyModel.getStoryTitle());
//                    userStory.setStoryType(storyModel.getStoryType());
//                    userStory.setStoryUrl(storyModel.getStoryUrl());
//                    userStory.setViews(storyModel.getViews());
//                    userStory.setUserDetails(storyModel.getUserDetails());
//                    Intent intent = new Intent(context, ShowStoryActivity.class);
//                    intent.putExtra("DATA", userStory);
//                    startActivity(intent);
//
//                }

            }
        });
        recyclerView.setAdapter(userSubscriptionStoryAdapter);


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

                        getUserStoryNew();


                    }
                }
            }
        });
    }

    private class DownloadFile extends AsyncTask<String, Integer, File> {

        String msg = "";
        String fileName = "";
        String type = "";

        DownloadFile(String msg, String fileName, String type) {
            this.msg = msg;
            this.fileName = fileName;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected File doInBackground(String... strings) {
            int count;
            try {
                URL url = new URL(strings[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();

                // downlod the file
                File filesDir = context.getFilesDir();
                File audioFile = new File(filesDir, fileName);
                if (audioFile.exists()) {
                    audioFile.delete();

                }
                audioFile.createNewFile();
                InputStream input = new BufferedInputStream(url.openStream());
//                OutputStream output = new FileOutputStream("/sdcard/somewhere/nameofthefile.mp3");
                OutputStream output = new FileOutputStream(audioFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return audioFile;
            } catch (Exception e) {
                String s = e.toString();
                System.out.println(e.toString());
            }
            return null;
        }


        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            dismiss_loading();
            flag = true;
            if (file != null) {

                String sharePath = file.getPath();
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                Utility.displayDocument(context, uri);


            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PAGE_REFRESH) {
            flag = true;
        }
    }


    private void setupViewModel()
    {

        viewModel = new ViewModelProvider(this, new ViewModelFactory( userId,accessToken)).get(ExpertProfileViewModel.class);
        if(userId.isEmpty())
        {
         viewModel.loadUserGroupForUnRegister(id);
        }
        else {
            viewModel.loadUserGroup(id);
        }


    }

    private void setupObserver() {
        viewModel.getGroupsRootMutableLiveData().observe(this, new Observer<UserSubscriptionGroupsRoot>() {
            @Override
            public void onChanged(UserSubscriptionGroupsRoot userSubscriptionGroupsRoot) {
                setupGroupData(userSubscriptionGroupsRoot);
            }
        });

        viewModel.getResponseBodyMutableLiveData().observe(this, new Observer<UnsubscribedRoot>() {
            @Override
            public void onChanged(UnsubscribedRoot responseBody)
            {
             if(responseBody.getUnsubscribedBody().isEligiblefor24())
             {
                 showToast(responseBody.getUnsubscribedBody().getUnsubscribeText());
                 isEligiblefor24=responseBody.getUnsubscribedBody().isEligiblefor24();
                 followStatus=false;
                 txtSubscriber.setText("Panding to Unsubscribe");

             }
             else
             {
                 isEligiblefor24=false;
                 followStatus=false;
                 txtSubscriber.setText("Subscribe");
                 txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber));
             }

                log(responseBody.toString());
            }
        });
        viewModel.getResponseUndoUnsubscirbe().observe(this, new Observer<UndoSubscriptionRoot>() {
            @Override
            public void onChanged(UndoSubscriptionRoot responseBody)
            {
                isEligiblefor24=false;
                followStatus=true;
               log(responseBody.toString());
                txtSubscriber.setText("SUBSCRIBE");
                txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber_select));
            }
        });
    }

    ArrayList<UserSubscriptionGroupsBody> groupsBodyArrayList;

    private void setupGroupData(UserSubscriptionGroupsRoot root) {
        if (root.getStatus() == 200) {
            groupsBodyArrayList = root.getGroupsBodyArrayList();
            OtherSubscribeGroupAdapter sampleDemoAdapter = new OtherSubscribeGroupAdapter(context, groupsBodyArrayList, new ApiItemCallback() {
                @Override
                public void result(int position)
                {
                    UserSubscriptionGroupsBody group = groupsBodyArrayList.get(position);
                    log("subscription id: "+group.get_id());
                    Intent intent = new Intent(context, OtheUserSubscriptionGroupDetailActivity.class);
                    intent.putExtra(Constant.SUBS_GROUP,group);
                    startActivity(intent);
                }
            });
            recyclerViewGroup.setAdapter(sampleDemoAdapter);


        }
    }


    public void dialogUnSubscribeDialog() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_iphone_confirm);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setText("Unsubscribe");

                ((CustomTextView) dd.findViewById(R.id.tvConfirmTitle)).setText("Do you want to Unsubscribe");


            ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   viewModel.unSubscribeAfter(id);
                    dd.dismiss();
                }
            });

            ((CustomTextView) dd.findViewById(R.id.tvConfirmCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }

      public void dialogUndoSubscribeDialog() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_iphone_confirm);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setText("Subscribe");

            ((CustomTextView) dd.findViewById(R.id.tvConfirmTitle)).setText("Do you want to Subscribe again");


            ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewModel.unDoUnSubscribe24(id);
                    dd.dismiss();
                }
            });

            ((CustomTextView) dd.findViewById(R.id.tvConfirmCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }


}
