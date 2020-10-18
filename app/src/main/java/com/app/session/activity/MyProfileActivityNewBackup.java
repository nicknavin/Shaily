package com.app.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.adapter.SampleDemoAdapter;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.fragment.MyProfileFragment;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.model.Brief_CV;
import com.app.session.model.ReqStory;
import com.app.session.model.ReqSubscribeGroupStories;
import com.app.session.model.StoryBody;
import com.app.session.model.StoryModel;
import com.app.session.model.StoryRoot;
import com.app.session.model.SubscribedAllStroiesBody;
import com.app.session.model.SubscribedAllStroiesRoot;
import com.app.session.model.SubscriptionGroup;
import com.app.session.model.SubscriptionGroupRoot;
import com.app.session.model.UserId;
import com.app.session.model.UserStory;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivityNewBackup extends BaseActivity implements View.OnClickListener {

    String load = "1";
   public CustomTextView txtUserName, txt_follwr_count, txt_follwng_count;
    CircleImageView imgProfileProfile;
    private ViewPagerAdapter adapter;
    ArrayList<Brief_CV> brief_cvList;

    ArrayList<SubscriptionGroup> subscriptionGroupsList = new ArrayList<>();
    RecyclerView recyclerViewtop;

    private ShimmerFrameLayout mShimmerViewContainer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CheckBox imgDrop;
    LinearLayout layDrop;
    private FragmentRefreshListener fragmentRefreshListener;
    ArrayList<UserStory> storyDataArrayList = new ArrayList<>();
    MyProfileFragment myProfileFragment;
    public boolean flag = false,flagEdit=false;
    public CustomTextView txtSubsciber;
    public LinkedList<UserStory> allStories = new LinkedList<>();
   public int pageno = 1;
   public int total_pages=0;
   public LinkedList<StoryModel> storyModelsList=new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_new);
        fragmentRefreshListener = getFragmentRefreshListener();
        initView();




    }

    public void initView() {
        layDrop = (LinearLayout) findViewById(R.id.layDrop);
        layDrop.setOnClickListener(this);
        imgDrop = (CheckBox) findViewById(R.id.imgDrop);

        recyclerViewtop = (RecyclerView) findViewById(R.id.recyclerViewtop);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        txtSubsciber = (CustomTextView) findViewById(R.id.txtSubsciber);
        txt_follwr_count = (CustomTextView) findViewById(R.id.txt_follwr_count);
        txt_follwng_count = (CustomTextView) findViewById(R.id.txt_follwng_count);

        txtUserName = (CustomTextView) findViewById(R.id.txtUserName);

        imgProfileProfile = (CircleImageView) findViewById(R.id.imgProfileProfile);
        recyclerViewtop = (RecyclerView) findViewById(R.id.recyclerViewtop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewtop.setLayoutManager(layoutManager);
        viewPager = (ViewPager) findViewById(R.id.viewpager_favourite);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_favourite);



    }

    @Override
    protected void onResume() {
        super.onResume();

       profileUrl= DataPrefrence.getPref(context,Constant.PROFILE_IMAGE,"");

        if (profileUrl != null && !profileUrl.isEmpty())
        {
            System.out.println("resume "+profileUrl);
            Picasso.with(context).load(profileUrl).placeholder(R.mipmap.profile_large).into(imgProfileProfile);
        }
        txtUserName.setText(user_name);
        brief_cvList = DataPrefrence.getBriefLanguage(context, Constant.BRIEF_CV_DB);

        System.out.println("Myprofil" + flag);
        if (!flag)
        {
            pageno=1;
            storyModelsList=new LinkedList<>();
            getSubscriptionGroup();
getUserStory();
        }



    }







    private void getSubscriptionGroup() {
        if (Utility.isConnectingToInternet(context)) {
            //showLoading();
            UserId user = new UserId();
            user.setUser_id(userId);
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SubscriptionGroupRoot> call = apiInterface.reqGetUserSubscriptionGroups(accessToken, user);
            call.enqueue(new Callback<SubscriptionGroupRoot>() {
                @Override
                public void onResponse(Call<SubscriptionGroupRoot> call, Response<SubscriptionGroupRoot> response) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                   // dismiss_loading();
                    if (response.body() != null) {
                        SubscriptionGroupRoot root = response.body();
                        subscriptionGroupsList = root.getSubscriptionGroupBodies();
                        SubscriptionGroup group = new SubscriptionGroup();
                        group.setGroup_name("New");
                        group.set_id("-0new");
                        subscriptionGroupsList.add(group);
                        SampleDemoAdapter sampleDemoAdapter = new SampleDemoAdapter(context, subscriptionGroupsList, new ApiItemCallback() {
                            @Override
                            public void result(int position) {
                                SubscriptionGroup group = subscriptionGroupsList.get(position);
                                if (!group.get_id().equals("-0new")) {
                                    Intent intent = new Intent(context, StoryDetailActivity.class);
                                    intent.putExtra("ID", group.get_id());
                                    intent.putExtra("GROUP_NAME", group.getGroup_name());
                                    intent.putExtra("GROUP_IMAGE", group.getGroup_image_url());
                                    intent.putExtra("USER_NAME", user_name);
                                    intent.putExtra("USER_ID", userId);
                                    intent.putExtra("USER_URL", group.getUserDetails().getImageUrl());
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("List", (Serializable) brief_cvList);
                                    intent.putExtra("BUNDLE", bundle);
                                    startActivity(intent);
                                } else {
                                    addStoryGroup();
                                }
                            }
                        });
                        recyclerViewtop.setAdapter(sampleDemoAdapter);
                        initTablayout();


                        //    System.out.println("data list"+subscriptionGroups.get(0).getUser_id());
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            String data = responseBody.string();

                            JSONObject jsonObject = new JSONObject(data);
                            System.out.println("error body " + jsonObject.toString());
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

    public void setCustomTextView() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            CustomTextView tv = (CustomTextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10, 0, 10, 0);
            tab.requestLayout();
            tabLayout.getTabAt(i).setCustomView(tv);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layDrop:

                if (imgDrop.isChecked()) {
                    imgDrop.setChecked(false);
                    MyProfileFragment fragment = (MyProfileFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    //  fragmentRefreshListener.onRefresh(true);
                    fragment.setVisibleBio(false);


                } else {
                    imgDrop.setChecked(true);
//                    fragmentRefreshListener.onRefresh(false);


                    MyProfileFragment fragment = (MyProfileFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    //  fragmentRefreshListener.onRefresh(true);
                    fragment.setVisibleBio(true);

                }
                break;
        }
    }

    private void initTablayout() {


        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
//        setCustomTextView();

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < brief_cvList.size(); i++) {
            myProfileFragment = MyProfileFragment.newInstance(i, storyDataArrayList);
            adapter.addFragment(myProfileFragment, brief_cvList.get(i).getLanguage_id().getName());
        }

        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.getItem(position).isVisible();
//                myProfileFragment.setStoriesData(storyDataArrayList);
                if (imgDrop.isChecked()) {
//                    fragmentRefreshListener.onRefresh(false);
                    MyProfileFragment fragment = (MyProfileFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    //  fragmentRefreshListener.onRefresh(true);
                    fragment.setVisibleBio(true);

                } else {
//                    fragmentRefreshListener.onRefresh(false);
                    MyProfileFragment fragment = (MyProfileFragment) viewPager
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
//        setBookAnimation();
        // setCardAnimation();

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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
    }


    private void addStoryGroup() {
        Intent intent = new Intent(context, AddSubscriptionGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Fragment myFragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (myFragment != null) {
            myFragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("profile");
//        if (fragment != null) {
//            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        }
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof MyProfileFragment)
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public interface FragmentRefreshListener {
        void onRefresh(boolean flag);


    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }


    public void getStoryNew() {
        if (Utility.isConnectingToInternet(context)) {
            ReqSubscribeGroupStories reqFollowUser = new ReqSubscribeGroupStories();
            reqFollowUser.setmLoad(""+pageno);
            reqFollowUser.setUser_id(userId);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < subscriptionGroupsList.size(); i++) {
                if (!subscriptionGroupsList.get(i).get_id().startsWith("-")) {
                    list.add(subscriptionGroupsList.get(i).get_id());
                }
            }
            reqFollowUser.setmSubscriptionId(list);
           // showLoading();
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SubscribedAllStroiesRoot> call = apiInterface.reqGetuserSubscribedAllStroies(accessToken, reqFollowUser);
            call.enqueue(new Callback<SubscribedAllStroiesRoot>() {
                @Override
                public void onResponse(Call<SubscribedAllStroiesRoot> call, Response<SubscribedAllStroiesRoot> response) {
                    MyProfileFragment fragment = (MyProfileFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
//                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getmStatus() == 200) {
                            SubscribedAllStroiesBody subscribedAllStroiesBody = response.body().getmBody();
                            total_pages = subscribedAllStroiesBody.getTotal_Page();
                            storyDataArrayList = subscribedAllStroiesBody.getSubscriptionStories();

                            if (storyDataArrayList.size() > 0) {
                                if (pageno <= total_pages) {
                                    fragment.loading = true;
                                    pageno++;
                                }
                                for (UserStory userStory : storyDataArrayList) {
                                    allStories.addFirst(userStory);
                                }

                                fragment.userStoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SubscribedAllStroiesRoot> call, Throwable t) {

                }
            });

        } else {
            showInternetConnectionToast();
        }
    }


    public void getUserStory() {
        if (Utility.isConnectingToInternet(context))
        {
//            swipeRefreshLayout.setRefreshing(false);

            ReqStory user=new ReqStory();
            user.setmUserId(userId);
            user.setmLoad(""+pageno);
//            showLoading();
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<StoryRoot> call = apiInterface.reqGetAllStroies(accessToken, user);
            call.enqueue(new Callback<StoryRoot>() {
                @Override
                public void onResponse(Call<StoryRoot> call, Response<StoryRoot> response)
                {
                    MyProfileFragment fragment=null;
                    if(viewPager!=null) {
                        fragment = (MyProfileFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    }
//                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            StoryBody storyBody=response.body().getStoryBody();

                            total_pages = storyBody.getTotal_Page();
                            LinkedList<StoryModel> temp=new LinkedList<>();
                            temp=storyBody.getUserStories();


                            if (temp.size() > 0) {
                                if (pageno <= total_pages) {
                                    fragment.loading = true;
                                    pageno++;
                                }
                                for (StoryModel storyModel : temp)
                                {
                                    storyModelsList.addFirst(storyModel);
                                }
                                    if(fragment!=null) {
                                        fragment.userStoryAdapter.notifyDataSetChanged();
                                    }
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

}



