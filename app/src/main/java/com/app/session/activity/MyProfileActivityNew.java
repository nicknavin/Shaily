package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.app.session.BuildConfig;
import com.app.session.R;
import com.app.session.adapter.SampleDemoAdapter;
import com.app.session.adapter.UserStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.fragment.MyProfileFragment;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.model.Brief_CV;
import com.app.session.model.ReqDeleteStory;
import com.app.session.model.ReqStory;
import com.app.session.model.Root;
import com.app.session.model.SendStoryBody;
import com.app.session.model.StoryBody;
import com.app.session.model.StoryModel;
import com.app.session.model.StoryRoot;
import com.app.session.model.SubscriptionGroup;
import com.app.session.model.SubscriptionGroupRoot;
import com.app.session.model.UserDetails;
import com.app.session.model.UserId;
import com.app.session.model.UserStory;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;

public class MyProfileActivityNew extends BaseActivity implements View.OnClickListener,ServiceResultReceiver.Receiver {


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
    public boolean flag = false;
    public CustomTextView txtSubsciber=null;

    public int pageno = 1;
    public int total_pages = 0;
    public LinkedList<StoryModel> storyModelsList = new LinkedList<>();
    public boolean loaddingDone = true;
    public boolean loading = true;
    public int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    UserStoryAdapter userStoryAdapter;
    RecyclerView recyclerView;
    FloatingActionButton fab;
//    CallbackManager callbackManager;
    StoryModel storyShare;
    private ServiceResultReceiver mServiceResultReceiver;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    ProgressView rey_loading;
    ImageView imageStory;
    RelativeLayout layLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_new);
        fragmentRefreshListener = getFragmentRefreshListener();
//        callbackManager = CallbackManager.Factory.create();
//        shareDialog = new ShareDialog(this);
        initView();
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onSuccess(Sharer.Result result) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

    }

//    ShareDialog shareDialog;

    private void shareOnFacebook()
    {
//        String url="";
//        if(storyShare.getStoryType().equals("video")) {
//            url = storyShare.getStoryUrl();
//        }if(storyShare.getStoryType().equals("image"))
//        {
//            url = Urls.BASE_IMAGES_URL+storyShare.getStoryUrl();
//        }
//
//        ShareLinkContent content = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse(url))
//                .setQuote(storyShare.getStoryText()+"\n"+storyShare.getStoryTitle())
//                .build();
//        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    public void initView() {

        layLoading=(RelativeLayout)findViewById(R.id.layLoading);
        imageStory=(ImageView)findViewById(R.id.imgStory);
        rey_loading=(ProgressView)findViewById(R.id.rey_loading);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

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
        brief_cvList = DataPrefrence.getBriefLanguage(context, Constant.BRIEF_CV_DB);


    }

    public void setupStoryRecylerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        userStoryAdapter = new UserStoryAdapter(context, storyModelsList, user_name, profileUrl, new ObjectCallback() {
            @Override
            public void getObject(Object object, int position, View view)
            {
                storyShare=null;
               storyShare = (StoryModel) object;

                if (view.getId() == R.id.imgRemove) {


                    showMenu(view, storyShare, position);
                }

                if(view.getId()==R.id.layDocument)
                {

                   String url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();

                    new DownloadFile("",storyShare.getDisplay_doc_name(),"show").execute(url);
//                    Utility.openFile(context,Urls.BASE_IMAGES_URL+storyShare.getStoryUrl());
                }

                if(view.getId()==R.id.layVideo)
                {
                    if (storyShare.getStoryType().equals("video_url"))
                    {
                        String id = Utility.extractYTId(storyShare.getStoryUrl());
                        Intent intent=new Intent(context, YoutubeActivity.class);
                        intent.putExtra("ID",id);
                        startActivityForResult(intent, Constant.PAGE_REFRESH);
                    }
                    if (storyShare.getStoryType().equals("video"))
                    {
                        String videoUrl = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("URL", videoUrl);
                        startActivityForResult(intent, Constant.PAGE_REFRESH);
                    }
                }
                if (view.getId() == R.id.imgShare)
                {

                    flag = true;
                    String url = "";
                    if (storyShare.getStoryType().equals("audio")) {
                        url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        String msg = storyShare.getStoryTitle() + "\n" + storyShare.getStoryText();
                        new DownloadFile(msg,"audio.mp3","").execute(url);
                    }
                   if (storyShare.getStoryType().equals("image"))
                    {
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
                    else if (storyShare.getStoryType().equals("video_url"))
                    {
                            Utility.shareVideoUrl(storyShare, context);
//                            shareIt();

                        }
                   else if (storyShare.getStoryType().equals("video"))
                   {
                       Utility.shareVideoUrl(storyShare, context);
                   }
                   else if (storyShare.getStoryType().equals("anydoc"))
                   {
                        url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        String msg = storyShare.getStoryTitle() + "\n" + storyShare.getStoryText();
                        new DownloadFile(msg,storyShare.getDisplay_doc_name(),"").execute(url);
                    }


                }

//                                      showMenu(view,storyData,position);
            }
        });
        recyclerView.setAdapter(userStoryAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        profileUrl = DataPrefrence.getPref(context, Constant.PROFILE_IMAGE, "");

        if (profileUrl != null && !profileUrl.isEmpty()) {
            System.out.println("resume " + profileUrl);
            Picasso.with(context).load(profileUrl).placeholder(R.mipmap.profile_large).into(imgProfileProfile);
        }
        txtUserName.setText(user_name);
        brief_cvList = DataPrefrence.getBriefLanguage(context, Constant.BRIEF_CV_DB);
//        initTablayout();
        System.out.println("Myprofil" + flag);
        if (!flag) {
            pageno = 1;
            storyModelsList = new LinkedList<>();
            getSubscriptionGroup();
            initTablayout();
            setupStoryRecylerview();
            setUpRecyclerListener();
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
            CustomTextView tv = (CustomTextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_brief, null);
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            // p.setMargins(10, 0, 10, 0);
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

                    viewPager.setVisibility(View.GONE);

                } else {
                    imgDrop.setChecked(true);
                    MyProfileFragment fragment = (MyProfileFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    viewPager.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.fab:

                             addNewStory();
                break;
        }
    }

    private void initTablayout()
    {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setCustomTextView();

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

                MyProfileFragment fragment = (MyProfileFragment) viewPager
                        .getAdapter()
                        .instantiateItem(viewPager, viewPager.getCurrentItem());
                if (imgDrop.isChecked()) {
                    viewPager.setVisibility(View.VISIBLE);
//                    fragment.setVisibleBio(true);

                } else {
//                    fragment.setVisibleBio(false);
                    viewPager.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void showMenu(View v, final StoryModel storyData, final int position) {
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
//                        intent.putExtra("ID", storyData.get_id());
//                        startActivity(intent);
                        return true;


                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_subscription_story, popup.getMenu());


        popup.show();
    }

    private void callDeleteStory(String story_id, int position) {
        if (isInternetConnected()) {
            showLoading();
            ReqDeleteStory deleteStory = new ReqDeleteStory();
            deleteStory.setStoryId(story_id);
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<Root> call = apiInterface.reqDeleteStory(accessToken, deleteStory);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            userStoryAdapter.updateData(position);
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

    public void getUserStory() {
        if (Utility.isConnectingToInternet(context)) {


            ReqStory user = new ReqStory();
            user.setmUserId(userId);
            user.setmLoad("" + pageno);
//            showLoading();
            ApiInterface apiInterface = ApiClientProfile.getClient().create(ApiInterface.class);
            Call<StoryRoot> call = apiInterface.reqGetAllStroies(accessToken, user);
            call.enqueue(new Callback<StoryRoot>() {
                @Override
                public void onResponse(Call<StoryRoot> call, Response<StoryRoot> response) {

//                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            StoryBody storyBody = response.body().getStoryBody();

                            total_pages = storyBody.getTotal_Page();
                            LinkedList<StoryModel> temp = new LinkedList<>();
                            temp = storyBody.getUserStories();
                            System.out.println("temp size " + temp.size());
                            System.out.println("temp page " + pageno);


                            if (temp.size() > 0) {
                                if (pageno <= total_pages) {
                                    loading = true;
                                    pageno++;
                                }
                                for (StoryModel storyModel : temp) {
                                    storyModelsList.addLast(storyModel);
                                }
                                temp.clear();
                                userStoryAdapter.notifyDataSetChanged();
//                                userStoryAdapter = new UserStoryAdapter(context, storyModelsList, user_name, profileUrl, new ObjectCallback() {
//                                    @Override
//                                    public void getObject(Object object, int position, View view) {
//                                        if (position != -1) {
//                                            StoryModel storyData = (StoryModel) object;
//
//                                            //  showMenu(view, storyData, position);
//                                        }
////                                      showMenu(view,storyData,position);
//                                    }
//                                });
//                                recyclerView.setAdapter(userStoryAdapter);

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

                        getUserStory();


                    }
                }
            }
        });
    }

    private void addNewStory()
    {
        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setReceiver(this);
        Intent intent = new Intent(context, AddSubscriptionStoryActivity.class);
        Bundle arg = new Bundle();
        arg.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", arg);
        intent.putExtra("ID", "");
        intent.putExtra("POSITION", 0);
        intent.putExtra(RECEIVER, mServiceResultReceiver);
        intent.setAction(ACTION_DOWNLOAD);
        startActivityForResult(intent, Constant.REQUEST_NEW_STORY);

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

    class GetAudioFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    private class DownloadFile extends AsyncTask<String, Integer, File> {

        String msg = "";
        String fileName="";
        String type="";
        DownloadFile(String msg,String fileName,String type)
        {
            this.msg = msg;
            this.fileName=fileName;
            this.type=type;
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
                if(audioFile.exists())
                {
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
            flag=true;
            if (file != null) {
                if(type.equals("show"))
                {

//                        Utility.openFile(context,file);
//                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(uri, "application/pdf");
//                    intent = Intent.createChooser(intent, "Open File");
//
//
//
//                    Intent chooser = Intent.createChooser(intent, "open file");
//                    chooser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // optional
//
//                    startActivity(chooser);

                }
                else {
                    String sharePath = file.getPath();
                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("audio/*");
                    share.putExtra(Intent.EXTRA_TEXT, msg);
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(share, "Share Sound File"));
                }
            }

        }

    }

    private void initAudioPlayer(File file) {
        try {
            String path = file.getPath();


            MediaPlayer mPlayer = new MediaPlayer();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
            mPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shareIt() {
        //sharing implementation
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        // Set title and text to share when the user selects an option.
        shareIntent.putExtra(Intent.EXTRA_TITLE, "title");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "url");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "text");
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                    String[] mimetypes = {"image/*","text/plain"};
                targetedShare.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//                targetedShare.setType("video/*","image/*"); // put here your mime type
                targetedShare.setPackage(info.activityInfo.packageName.toLowerCase());
                targetedShareIntents.add(targetedShare);
            }
            // Then show the ACTION_PICK_ACTIVITY to let the user select it
            Intent intentPick = new Intent();
            intentPick.setAction(Intent.ACTION_PICK_ACTIVITY);
            // Set the title of the dialog
            intentPick.putExtra(Intent.EXTRA_TITLE, "Share Story");
            intentPick.putExtra(Intent.EXTRA_INTENT, shareIntent);
            intentPick.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray());
            // Call StartActivityForResult so we can get the app name selected by the user
            this.startActivityForResult(intentPick, Constant.REQUEST_CODE_MY_PICK);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment myFragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (myFragment != null) {
            myFragment.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode==Constant.PAGE_REFRESH)
        {
            flag = true;
        }
        if (requestCode == Constant.REQUEST_NEW_STORY) {
            flag = true;
            if (data != null) {
                boolean refresh = data.getBooleanExtra("REFRESH", false);


                if (refresh) {

                    if (data.getParcelableExtra("DATA") != null) {
                        SendStoryBody story = data.getParcelableExtra("DATA");
                        StoryModel storyModel = new StoryModel();
                        storyModel.set_id(story.get_id());
                        storyModel.setCreatedAt(story.getCreatedAt());
                        storyModel.setDaysAgo("0");
                        storyModel.setViews("0");
                        storyModel.setStoryText(story.getStory_text());
                        storyModel.setStoryTitle(story.getStory_title());
                        storyModel.setStoryType(story.getStory_type());
                        storyModel.setStoryUrl(story.getStory_url());
                        storyModel.setDisplay_doc_name(story.getDisplay_doc_name());
                        storyModel.setThumbnail_url(story.getThumbnail_url());
                        UserDetails details = new UserDetails();
                        details.setId(story.get_id());
                        details.setUserName(user_name);
                        details.setImageUrl(profileUrl);
                        storyModel.setUserDetails(details);
                        storyModelsList.addFirst(storyModel);
                        userStoryAdapter.notifyDataSetChanged();

                    }
//                    storyModelsList


//                    ((MyProfileActivityNew)getActivity()).getUserStory();


                }
            }
        }
        if (requestCode == Constant.REQUEST_CODE_MY_PICK) {
            if(data!=null)
            {
                String appName = data.getComponent().flattenToShortString();
                if(appName.toLowerCase().contains("session"))
                {


                }
                else
                {
                    Utility.shareVideoUrl(storyShare,context);
                }
                System.out.println("app name ");
            }
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


    public  void shareVideoUrl(StoryModel storyData, Context context) {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        String msg = storyData.getStoryTitle() + "\n" + storyData.getStoryText();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//
//     i.setType("*/*");
//
//    String[] mimetypes = {"image/*", "video/*","text/plain"};
        shareIntent.setType("text/plain");

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, msg);
//    i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        shareIntent.putExtra(Intent.EXTRA_TEXT, storyData.getStoryUrl());
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfo.isEmpty())
        {
            for (ResolveInfo info : resInfo) {
                if(!info.activityInfo.packageName.toLowerCase().equals("com.app.session"))
                {
                    Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                    String[] mimetypes = {"image/*","text/plain"};
                    targetedShare.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                    targetedShare.setPackage(info.activityInfo.packageName.toLowerCase());
                    targetedShareIntents.add(targetedShare);
                }



                Intent intentPick = new Intent();
                intentPick.setAction(Intent.ACTION_PICK_ACTIVITY);
                // Set the title of the dialog
                intentPick.putExtra(Intent.EXTRA_TITLE, "Share Story");
                intentPick.putExtra(Intent.EXTRA_TEXT, storyData.getStoryUrl());
                intentPick.putExtra(Intent.EXTRA_INTENT, shareIntent);
                intentPick.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray());
                // Call StartActivityForResult so we can get the app name selected by the user
                startActivityForResult(intentPick, Constant.REQUEST_CODE_MY_PICK);
            }


        }

//        i.putExtra(Intent.EXTRA_TEXT, storyData.getStoryText());

    }

    boolean isFlag=false;
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case SHOW_RESULT:
                if (resultData != null) {
                    if(!isFlag) {

                        if(resultData.getString("TYPE").equals("anydoc"))
                        {
                            if(resultData.getString("FILENAME").contains(".doc"))
                            {
                                imageStory.setImageResource(R.mipmap.docs_story);
                            }
                            else if(resultData.getString("FILENAME").contains(".pdf"))
                            {
                                imageStory.setImageResource(R.mipmap.pdf_story);
                            }
                            else if(resultData.getString("FILENAME").contains(".zip"))
                            {
                                imageStory.setImageResource(R.mipmap.zip_story);
                            }
                            else if(resultData.getString("FILENAME").contains(".xls"))
                            {
                                imageStory.setImageResource(R.mipmap.xls_story);
                            }

                        }
                        else if(resultData.getString("TYPE").equals("image")||resultData.getString("TYPE").equals("video"))
                        {
                           Bitmap bm = resultData.getParcelable("IMAGE");
                            imageStory.setImageBitmap(bm);
                            imageStory.setImageBitmap(bm);
                        }
                        else if(resultData.getString("TYPE").equals("audio"))
                        {
                            imageStory.setImageResource(R.mipmap.attach_audio);
                        }


                        isFlag=true;
                    }
                    layLoading.setVisibility(View.VISIBLE);
                    rey_loading.start();
                }
                break;

            case STATUS:
                rey_loading.stop();
                layLoading.setVisibility(View.GONE);
                showToast("File is uploaded ");

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
                        sendStoryBody.setViews(body.getString("views"));
                        sendStoryBody.set_id(body.getString("_id"));
                        sendStoryBody.setUser_id(body.getString("user_id"));
                        sendStoryBody.setSubscription_id(body.getString("subscription_id"));
                        sendStoryBody.setDisplay_doc_name(body.getString("display_doc_name"));
                        StoryModel storyModel = new StoryModel();
                        storyModel.set_id(sendStoryBody.get_id());
                        storyModel.setCreatedAt(sendStoryBody.getCreatedAt());
                        storyModel.setDaysAgo("0");
                        storyModel.setViews("0");
                        storyModel.setStoryText(sendStoryBody.getStory_text());
                        storyModel.setStoryTitle(sendStoryBody.getStory_title());
                        storyModel.setStoryType(sendStoryBody.getStory_type());
                        storyModel.setStoryUrl(sendStoryBody.getStory_url());
                        storyModel.setDisplay_doc_name(sendStoryBody.getDisplay_doc_name());
                        storyModel.setThumbnail_url(sendStoryBody.getThumbnail_url());
                        UserDetails details = new UserDetails();
                        details.setId(sendStoryBody.get_id());
                        details.setUserName(user_name);
                        details.setImageUrl(profileUrl);
                        storyModel.setUserDetails(details);
                        storyModelsList.addFirst(storyModel);

                        recyclerView.smoothScrollToPosition(0);
                        recyclerView.scrollToPosition(0);
                        userStoryAdapter.notifyDataSetChanged();


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


}





