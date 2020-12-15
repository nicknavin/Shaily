package com.app.session.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.app.session.Circle;
import com.app.session.R;
import com.app.session.adapter.AllSubscriptionStoryAdapter;
import com.app.session.adapter.SampleDemoAdapter;
import com.app.session.adapter.UserSubscriptionStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.database.DatabaseHelper;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.model.Brief_CV;
import com.app.session.model.ReqSubscribeGroupStories;
import com.app.session.model.StoryDb;
import com.app.session.model.SubscribedAllStroiesBody;
import com.app.session.model.SubscribedAllStroiesRoot;
import com.app.session.model.SubscriptionGroup;
import com.app.session.model.SubscriptionGroupBody;
import com.app.session.model.SubscriptionGroupRoot;
import com.app.session.model.SubscriptionStories;
import com.app.session.model.User;
import com.app.session.model.UserId;
import com.app.session.model.UserRoot;
import com.app.session.model.UserStory;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultantStoryActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerViewtop,recyclerViewbottom;
    CustomTextView txtProfileName;
    ImageView imgSearch;
    ArrayList<Brief_CV> brief_cvList;
    ArrayList<SubscriptionGroup> subscriptionGroupsList = new ArrayList<>();
    ArrayList<SubscriptionGroupBody> subscriptionGroups = new ArrayList<>();

    AllSubscriptionStoryAdapter subscriptionStoryAdapter;
    private ShimmerFrameLayout mShimmerViewContainer,shimmer_view_container2;
    CircleImageView img_profilepic;
    private DatabaseHelper db;
    String userName="",userUrl="";
    SwipeRefreshLayout swipeRefreshLayout;
    String load="1";
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    int pageno = 1;
    int total_pages=0;
    LinearLayoutManager linearLayoutManager;
    LinkedList<UserStory> allStories=new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conusultant_story);
        db = new DatabaseHelper(this);
        userName=user_name;
        userUrl=profileUrl;
        initView();
        img_profilepic.setClickable(true);

        setupStoryRecylerview();
        setUpRecyclerListener();
        setSwipeLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        img_profilepic.setClickable(true);
        if(isInternetConnected()) {
            mShimmerViewContainer.startShimmerAnimation();
            shimmer_view_container2.startShimmerAnimation();
        }

        getUserDetail();

        getSubscriptionGroup();
    }

    private void initView() {
        swipeRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        img_profilepic=(CircleImageView)findViewById(R.id.img_profilepic);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        shimmer_view_container2 = findViewById(R.id.shimmer_view_container2);
        recyclerViewtop=(RecyclerView)findViewById(R.id.recyclerViewtop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewtop.setLayoutManager(layoutManager);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context,HomeConsultantActivity.class);
                startActivity(intent);
            }
        });

        txtProfileName=(CustomTextView)findViewById(R.id.txtProfileName);
        img_profilepic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                setCameraPermission();
            }
        });

        if(profileUrl!=null&&!profileUrl.isEmpty())
        {
            Picasso.with(context).load(profileUrl).placeholder(R.mipmap.profile_large).into(img_profilepic);
        }



    }

    private void setupStoryRecylerview()
    {
        recyclerViewbottom=(RecyclerView)findViewById(R.id.recyclerViewstory);
        linearLayoutManager=new LinearLayoutManager(context);
        recyclerViewbottom.setLayoutManager( linearLayoutManager);
        subscriptionStoryAdapter=new AllSubscriptionStoryAdapter(context,allStories, new ObjectCallback()
        {
            @Override
            public void getObject(Object object, int position,View view)
            {

                if(view.getId()==R.id.imgRemove)
                {
                    showMenu(view);
                }
                else
                {
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
            if (!PermissionsUtils.getInstance(context).requiredPermissionsGrantedComman(context))
            {
                return;
            }


        }
        callActivity();
    }

    private void callActivity()
    {
        img_profilepic.setClickable(false);
        Intent intent=new Intent(context,ConsultantUserActivity.class);
        startActivity(intent);
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
                            userName = user.getUser_name();
                            txtProfileName.setText("" + userName.toUpperCase().charAt(0));
                            userUrl=Urls.BASE_IMAGES_URL+user.getImageUrl();
                            DataPrefrence.setPref(context, Constant.PROFILE_IMAGE,userUrl);
                            if(!userUrl.isEmpty()&&userUrl!=null) {
                                Picasso.with(context).load(userUrl).error(R.mipmap.profile_image).into(img_profilepic);
                            }
                            brief_cvList=user.getBriefCV();
                            user.getUserLangauges();

                            DataPrefrence.setBriefLanguage(context,Constant.BRIEF_CV_DB,brief_cvList);
                            DataPrefrence.setLanguageDb(context,Constant.LANG_DB,user.getUserLangauges());



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
    public void checkIfPermissionsGranted()     {
        AlertDialog alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.CustomDialogTheme);
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
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
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
                pageno=1;
                allStories=new LinkedList<>();
                getStory();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void showStoryData(LinkedList<UserStory> list)
    {


        if (list.size()>0) {
            subscriptionStoryAdapter=new AllSubscriptionStoryAdapter(context,list, new ObjectCallback()
            {
                @Override
                public void getObject(Object object, int position,View view)
                {

                    if(view.getId()==R.id.imgRemove)
                    {
                        showMenu(view);
                    }
                    else  if(view.getId()==R.id.imgProfile){
                        if(list.size()>0)
                        {

                            UserStory storyData = list.get(position);

                            Intent intent=new Intent(context, ExpertProfilePageActivity.class);
                            intent.putExtra("ID",storyData.getUserDetails().getId());
                            intent.putExtra("NAME",storyData.getUserDetails().getLoginUserId());
                            intent.putExtra("URL", Urls.BASE_IMAGES_URL +storyData.getUserDetails().getImageUrl());
                            startActivity(intent);
                        }
                    }

                }
            });
            recyclerViewbottom.setAdapter(subscriptionStoryAdapter);

        }
        else
        {
            getStory();
        }
    }

    private void getSubscriptionGroup() {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            UserId user=new UserId();
            user.setUser_id(userId);
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<SubscriptionGroupRoot> call = apiInterface.reqGetSubscribedSubscriptionGroups(accessToken, user);
            call.enqueue(new Callback<SubscriptionGroupRoot>() {
                @Override
                public void onResponse(Call<SubscriptionGroupRoot> call, Response<SubscriptionGroupRoot> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        SubscriptionGroupRoot root=response.body();
                        subscriptionGroupsList=root.getSubscriptionGroupBodies();
                      //  System.out.println("subsID "+subscriptionGroupsList.get(0).get_id());
                       // System.out.println("subsGrp "+subscriptionGroupsList.get(0).getSubscription_group_cd());
                        SampleDemoAdapter sampleDemoAdapter=new SampleDemoAdapter(context, subscriptionGroupsList, new ApiItemCallback() {
                            @Override
                            public void result(int position)
                            {
                                SubscriptionGroup group=subscriptionGroupsList.get(position);
                                Intent intent=new Intent(context,StoryDetailActivity.class);
                                intent.putExtra("ID",group.get_id());
                                intent.putExtra("GROUP_NAME",group.getGroup_name());
                                intent.putExtra("GROUP_IMAGE",group.getGroup_image_url());
                                intent.putExtra("USER_NAME",group.getUserDetails().getUserName());
                                intent.putExtra("USER_URL",group.getUserDetails().getImageUrl());
                                intent.putExtra("USER_ID",group.getUserDetails().getId());
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("List", (Serializable) brief_cvList);
                                intent.putExtra("BUNDLE", bundle);
                                intent.putExtra("OTHER","0");
                                startActivity(intent);
                            }
                        });
                        recyclerViewtop.setAdapter(sampleDemoAdapter);
                        if(subscriptionGroupsList.size()>0)
                        {

                         //   db.deleteNote();
                            if(db.getNotesCount()>0)
                            {
                                List<StoryDb> sd=   db.getAllNotes();
                                String data= sd.get(0).getNote();

                                showStoryData(Utility.getStoryList(data));
                            }
                            else {
                                getStory();
                            }
                        }
                        else {
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

    private void getStory() {
        if (Utility.isConnectingToInternet(context))
        {
            swipeRefreshLayout.setRefreshing(false);
            shimmer_view_container2.stopShimmerAnimation();
            shimmer_view_container2.setVisibility(View.GONE);
            ReqSubscribeGroupStories reqFollowUser =new ReqSubscribeGroupStories();
            reqFollowUser.setmLoad(""+pageno);
            reqFollowUser.setUser_id(userId);
            ArrayList<String> list=new ArrayList<>();
            for(int i=0;i<subscriptionGroupsList.size();i++) {
                list.add(subscriptionGroupsList.get(i).get_id());
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
                        if(response.body().getmStatus()==200)
                        {
                            SubscribedAllStroiesBody  subscribedAllStroiesBody = response.body().getmBody();
                            total_pages=subscribedAllStroiesBody.getTotal_Page();
                            ArrayList<UserStory> storyDataArrayList= subscribedAllStroiesBody.getSubscriptionStories();

                            if(storyDataArrayList.size()>0)
                            {
                                if(pageno <=total_pages)
                                {
                                    loading=true;
                                    pageno++;
                                }

                                for(UserStory userStory:storyDataArrayList)
                                {
                                    allStories.addLast(userStory);

                                }

                                Utility.getStringFromList(allStories);
                                if(db.getNotesCount()>0)
                                {
                                    db.deleteNote();
                                }
                                db.insertNote(Utility.getStringFromList(allStories));





                                subscriptionStoryAdapter.notifyDataSetChanged();


                            }






                        }
                    }
                }

                @Override
                public void onFailure(Call<SubscribedAllStroiesRoot> call, Throwable t) {
dismiss_loading();
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


    public void setUpRecyclerListener() {
//        loading = true;
//        loaddingDone = true;
        recyclerViewbottom.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
