package com.app.session.activity.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.app.session.R;
import com.app.session.activity.ConsultantUserActivity;
import com.app.session.activity.HomeConsultantActivity;
import com.app.session.activity.StoryDetailActivity;
import com.app.session.activity.StoryPageDetailActivity;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.adapter.AllSubscriptionStoryAdapter;
import com.app.session.adapter.SampleDemoAdapter;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.SubscribedAllStroiesBody;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.User;
import com.app.session.data.model.UserRoot;
import com.app.session.data.model.UserStory;
import com.app.session.database.DatabaseHelper;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener {

    private HomeViewModel homeViewModel;
    View root;
    RecyclerView recyclerViewtop, recyclerViewbottom;
    CustomTextView txtProfileName;
    ImageView imgSearch;
    ArrayList<Brief_CV> brief_cvList;
    ArrayList<SubscriptionGroup> subscriptionGroupsList = new ArrayList<>();


    AllSubscriptionStoryAdapter subscriptionStoryAdapter;
    private ShimmerFrameLayout mShimmerViewContainer, shimmer_view_container2;
    CircleImageView img_profilepic;
    private DatabaseHelper db;
    String userName = "", userUrl = "";
    SwipeRefreshLayout swipeRefreshLayout;
    String load = "1";
    private boolean loaddingDone = true;
    private boolean loading = true;
    private boolean unfollowing=false;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    int pageno = 1;
    int total_pages = 0;
    LinearLayoutManager linearLayoutManager;
    LinkedList<UserStory> allStories = new LinkedList<>();
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        setupViewModel();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        db = new DatabaseHelper(context);
        userName = user_name;
        userUrl = profileUrl;
        initView();

        img_profilepic.setClickable(true);
        setupStoryRecylerview();

        setupObserver();
        setUpRecyclerListener();
        setSwipeLayout();

        return root;
    }


    private void setupViewModel() {
        homeViewModel = new ViewModelProvider(this, new ViewModelFactory(getActivity().getApplication(), userId, accessToken)).get(HomeViewModel.class);
    }


    @Override
    public void onResume() {
        super.onResume();
        img_profilepic.setClickable(true);
        if (isInternetConnected()) {
            mShimmerViewContainer.startShimmerAnimation();
            shimmer_view_container2.startShimmerAnimation();
            apiCall();
        }


    }

    private void initView() {
        swipeRefreshLayout = ((SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout));
        img_profilepic = (CircleImageView) root.findViewById(R.id.img_profilepic);
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);
        shimmer_view_container2 = root.findViewById(R.id.shimmer_view_container2);
        recyclerViewtop = (RecyclerView) root.findViewById(R.id.recyclerViewtop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewtop.setLayoutManager(layoutManager);
        imgSearch = (ImageView) root.findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeConsultantActivity.class);
                startActivity(intent);
            }
        });

        txtProfileName = (CustomTextView) root.findViewById(R.id.txtProfileName);
        img_profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setCameraPermission();
            }
        });

        if (profileUrl != null && !profileUrl.isEmpty()) {
            //log(profileUrl);
            Picasso.with(context).load(profileUrl).placeholder(R.mipmap.profile_large).error(R.mipmap.profile_large).into(img_profilepic);
        } else {
            img_profilepic.setImageResource(R.mipmap.profile_large);
        }


    }

    private void setupStoryRecylerview() {
        recyclerViewbottom = (RecyclerView) root.findViewById(R.id.recyclerViewstory);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerViewbottom.setLayoutManager(linearLayoutManager);
        recyclerViewbottom.setHasFixedSize(true);
        subscriptionStoryAdapter = new AllSubscriptionStoryAdapter(context, allStories, new ObjectCallback() {
            @Override
            public void getObject(Object object, int position, View view) {

                if (view.getId() == R.id.imgRemove) {
                    showMenu(view);
                } else {
                    UserStory storyData = allStories.get(position);
                    Intent intent = new Intent(context, StoryPageDetailActivity.class);
                    intent.putExtra("DATA", storyData);
                    startActivity(intent);
                }


            }
        });
        recyclerViewbottom.setAdapter(subscriptionStoryAdapter);
    }

    public void setCameraPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).requiredPermissionsGrantedComman(context)) {
                return;
            }


        }
        callActivity();
    }

    private void callActivity() {
        img_profilepic.setClickable(false);
        Intent intent = new Intent(context, ConsultantUserActivity.class);
        startActivity(intent);
    }


    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(context, v);
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


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
                    callActivity();
                } else {
                    checkIfPermissionsGranted();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void checkIfPermissionsGranted() {
        AlertDialog alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
        alertDialogBuilder.setMessage(getString(R.string.permission));

        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                goToSettings();
            }
        });

        alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }


    public void setSwipeLayout() {

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                pageno = 1;
                homeViewModel.page = 1;
                homeViewModel.stories=0;
                allStories = new LinkedList<>();
                homeViewModel.getFollowingUserStoryApi();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void apiCall()
    {
        homeViewModel.getUserDetailApi();
        homeViewModel.getSubscriptinGroupApi();
        homeViewModel.getFollowingUserStoryApi();
      //  homeViewModel.getStoryoFUnFollowingUserApi();

    }

    public void setUpRecyclerListener() {
//        loading = true;
//        loaddingDone = true;
        recyclerViewbottom.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();


                if (loading && loaddingDone) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {
                        loading = false;
                        Utility.Log("inside the recly litner");
                      if(!unfollowing) {
                          homeViewModel.getFollowingUserStoryApi();
                      }else
                      {
                          homeViewModel.getUnFollowingUserStoryApi();
                      }
                    }
                }
            }
        });
    }


    private void setupObserver() {
        homeViewModel.getSubscriptionGroupRootMutableLiveData().observe(getViewLifecycleOwner(), new Observer<SubscriptionGroupRoot>() {
            @Override
            public void onChanged(SubscriptionGroupRoot root) {
                setupGroupData(root);

            }
        });


        homeViewModel.getSubscriptionGroupStoryData().observe(getViewLifecycleOwner(), new Observer<SubscribedAllStroiesRoot>() {
            @Override
            public void onChanged(SubscribedAllStroiesRoot subscribedAllStroiesRoot) {
                shimmer_view_container2.stopShimmerAnimation();
                shimmer_view_container2.setVisibility(View.GONE);
                SubscribedAllStroiesBody body = subscribedAllStroiesRoot.getmBody();
                homeViewModel.totalPage = body.getTotal_Page();
                setupGroupStoryUI(subscribedAllStroiesRoot);

//                if(homeViewModel.totalPage==0)
//                {
//                    homeViewModel.getStoryoFUnFollowingUserApi();
//                }
//                else {
//                    setupGroupStoryUI(subscribedAllStroiesRoot);
//                }
            }
        });


        homeViewModel.getUnSubscriptionGroupStoryData().observe(getViewLifecycleOwner(), new Observer<SubscribedAllStroiesRoot>() {
            @Override
            public void onChanged(SubscribedAllStroiesRoot subscribedAllStroiesRoot) {
                Log.d("TAG", subscribedAllStroiesRoot.getMessage());
                SubscribedAllStroiesBody body = subscribedAllStroiesRoot.getmBody();
                homeViewModel.unsubscribeTotalPage = body.getTotal_Page();
                if(homeViewModel.unsubscribeTotalPage>0) {
                    setupUnGroupStoryUI(subscribedAllStroiesRoot);
                }
            }
        });


        homeViewModel.getMutableLiveUserDetailData().observe(getViewLifecycleOwner(), new Observer<UserRoot>() {
            @Override
            public void onChanged(UserRoot userRoot) {

                baseActivity.log(userRoot.getUserBody().getUser().toString());
                setupUserUI(userRoot);
            }
        });

    }


    private void setupGroupData(SubscriptionGroupRoot root) {
        if (root.getStatus() == 200) {
            subscriptionGroupsList = root.getSubscriptionGroupBodies();
            SampleDemoAdapter sampleDemoAdapter = new SampleDemoAdapter(context, subscriptionGroupsList, new ApiItemCallback() {
                @Override
                public void result(int position) {
                    SubscriptionGroup group = subscriptionGroupsList.get(position);
                    Intent intent = new Intent(context, StoryDetailActivity.class);
                    intent.putExtra("ID", group.get_id());
                    intent.putExtra("GROUP_NAME", group.getGroup_name());
                    intent.putExtra("GROUP_IMAGE", group.getGroup_image_url());
                    intent.putExtra("USER_NAME", group.getUserDetails().getUserName());
                    intent.putExtra("USER_URL", group.getUserDetails().getImageUrl());
                    intent.putExtra("USER_ID", group.getUserDetails().getId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("List", (Serializable) brief_cvList);
                    intent.putExtra("BUNDLE", bundle);
                    intent.putExtra("OTHER", "0");
                    startActivity(intent);
                }
            });
            recyclerViewtop.setAdapter(sampleDemoAdapter);
            if (subscriptionGroupsList.size() > 0) {


//                StoriesFollowingUsers storiesFollowingUsers=new StoriesFollowingUsers();
//                storiesFollowingUsers.setUser_id(userId);
//                storiesFollowingUsers.setLoad(homeViewModel.page);
//                ArrayList<String> subIDList=new ArrayList<>();
//
//                for(int i=0;i<subscriptionGroupsList.size();i++)
//                {
//                    subIDList.add(subscriptionGroupsList.get(i).getSubscription_group_cd());
//                }
//                storiesFollowingUsers.setFollower_user_id(subIDList);


                //   db.deleteNote();
//                        if(db.getNotesCount()>0)
//                        {
//                            List<StoryDb> sd=   db.getAllNotes();
//                            String data= sd.get(0).getNote();
//
//                            showStoryData(Utility.getStoryList(data));
//                        }
//                        else {
//                            getStory();
//                        }
            } else {
                swipeRefreshLayout.setRefreshing(false);
                shimmer_view_container2.stopShimmerAnimation();
                shimmer_view_container2.setVisibility(View.GONE);
//                            subscriptionStoryAdapter=new AllSubscriptionStoryAdapter(context,allStories, new ObjectCallback()
//                            {
//                                @Override
//                                public void getObject(Object object, int position,View view)
//                                {
//
//                                }
//                            });
//                            recyclerViewbottom.setAdapter(subscriptionStoryAdapter);
            }
            //    System.out.println("data list"+subscriptionGroups.get(0).getUser_id());
        }
    }

    private void setupGroupStoryUI(SubscribedAllStroiesRoot response) {
        SubscribedAllStroiesBody subscribedAllStroiesBody = response.getmBody();
        homeViewModel.totalPage = subscribedAllStroiesBody.getTotal_Page();
        homeViewModel.stories=subscribedAllStroiesBody.getmTotalOverAllStories();
        ArrayList<UserStory> storyDataArrayList = subscribedAllStroiesBody.getSubscriptionStories();
        if (storyDataArrayList.size() > 0)
        {
            if (homeViewModel.page <= homeViewModel.totalPage) {
                loading = true;
                homeViewModel.page++;
            }

            for (UserStory userStory : storyDataArrayList)
            {
                allStories.addLast(userStory);

            }

           // Utility.getStringFromList(allStories);
//                if(db.getNotesCount()>0)
//                {
//                    db.deleteNote();
//                }
//                db.insertNote(Utility.getStringFromList(allStories));

            subscriptionStoryAdapter.notifyDataSetChanged();
            if(allStories.size()==homeViewModel.stories)
            {
                unfollowing=true;

            }

        }
    }

private void setupUnGroupStoryUI(SubscribedAllStroiesRoot response) {
        SubscribedAllStroiesBody subscribedAllStroiesBody = response.getmBody();

        ArrayList<UserStory> storyDataArrayList = subscribedAllStroiesBody.getSubscriptionStories();
        if (storyDataArrayList.size() > 0) {
            if (homeViewModel.unsubscribePage <= homeViewModel.unsubscribeTotalPage) {
                loading = true;
                homeViewModel.unsubscribePage++;
            }

            for (UserStory userStory : storyDataArrayList) {
                allStories.addLast(userStory);

            }

           // Utility.getStringFromList(allStories);
//                if(db.getNotesCount()>0)
//                {
//                    db.deleteNote();
//                }
//                db.insertNote(Utility.getStringFromList(allStories));

            subscriptionStoryAdapter.notifyDataSetChanged();


        }
    }


    private void setupUserUI(UserRoot root) {

        if (root.getStatus() == 200) {
            User user = root.getUserBody().getUser();
            userName = user.getUser_name();
            txtProfileName.setText("" + userName.toUpperCase().charAt(0));
            userUrl = Urls.BASE_IMAGES_URL + user.getImageUrl();
            DataPrefrence.setPref(context, Constant.PROFILE_IMAGE, userUrl);
            if (!userUrl.isEmpty() && userUrl != null) {
                Picasso.with(context).load(userUrl).error(R.mipmap.profile_image).into(img_profilepic);
            }
            brief_cvList = user.getBriefCV();
            user.getUserLangauges();
            DataPrefrence.setBriefLanguage(context, Constant.BRIEF_CV_DB, brief_cvList);
            DataPrefrence.setLanguageDb(context, Constant.LANG_DB, user.getUserLangauges());
        }

    }


}