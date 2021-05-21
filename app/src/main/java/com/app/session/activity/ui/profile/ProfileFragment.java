package com.app.session.activity.ui.profile;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
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
import com.app.session.activity.AddSubscriptionGroupActivity;
import com.app.session.activity.AddSubscriptionStoryActivity;
import com.app.session.activity.ChangePasswordActivity;
import com.app.session.activity.CouresesActivity;
import com.app.session.activity.CreateSpecialGroupActivity;
import com.app.session.activity.FinancialActivity;
import com.app.session.activity.LoginActivity;
import com.app.session.activity.ProfileCompanyEditActivity;
import com.app.session.activity.ProfileEditConsultantActivity;
import com.app.session.activity.ProfileEditUserActivity;
import com.app.session.activity.ReportProblemActivity;
import com.app.session.activity.ShowStoryActivity;
import com.app.session.activity.StoryDetailActivity;
import com.app.session.activity.StoryPageDetailActivity;
import com.app.session.activity.UpdateSubscriptionStoryActivity;
import com.app.session.activity.VideoPlayerActivity;
import com.app.session.activity.YoutubeActivity;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.adapter.OtherSubscribeGroupAdapter;
import com.app.session.adapter.SampleDemoAdapter;
import com.app.session.adapter.UserStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomPager;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.DynamicHeightViewPager;
import com.app.session.data.api.ApiClients;
import com.app.session.data.model.StoryRead;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.data.model.UserSubscriptionGroupsRoot;
import com.app.session.fragment.MyProfileFragment;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.SendStoryBody;
import com.app.session.data.model.StoryBody;
import com.app.session.data.model.StoryModel;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.UserDetails;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserStory;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
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

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;

public class ProfileFragment extends BaseFragment implements View.OnClickListener, ServiceResultReceiver.Receiver {

    private ProfileViewModel mViewModel;
    public CustomTextView txtUserName, txt_follwr_count, txt_follwng_count;
    CircleImageView imgProfileProfile;
    private ViewPagerAdapter adapter;
    ArrayList<Brief_CV> brief_cvList;
    ArrayList<UserSubscriptionGroupsBody> groupsBodyArrayList;
    RecyclerView recyclerViewtop;

    private TabLayout tabLayout;
    private DynamicHeightViewPager viewPager;
    CheckBox imgDrop;
    LinearLayout layDrop;
    private FragmentRefreshListener fragmentRefreshListener;
    ArrayList<UserStory> storyDataArrayList = new ArrayList<>();
    MyProfileFragment myProfileFragment;
    public boolean flag = false;
    public CustomTextView txtSubsciber = null;

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
    Context context;
    View root;
    ImageView imgSetting;
    ConstraintLayout layBio;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory( userId,accessToken)).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.profile_fragment_demo, container, false);
        initView();

        setObserver();

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }


    public void initView() {

        layBio = (ConstraintLayout) root.findViewById(R.id.layBio);
        layLoading = (RelativeLayout) root.findViewById(R.id.layLoading);
        imageStory = (ImageView) root.findViewById(R.id.imgStory);
        imgSetting = root.findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(this);
        rey_loading = (ProgressView) root.findViewById(R.id.rey_loading);

        fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        layDrop = (LinearLayout) root.findViewById(R.id.layDrop);
        layDrop.setOnClickListener(this);
        imgDrop = (CheckBox) root.findViewById(R.id.imgDrop);




        txtSubsciber = (CustomTextView) root.findViewById(R.id.txtSubsciber);
        txt_follwr_count = (CustomTextView) root.findViewById(R.id.txt_follwr_count);
        txt_follwng_count = (CustomTextView) root.findViewById(R.id.txt_follwng_count);

        txtUserName = (CustomTextView) root.findViewById(R.id.txtUserName);

        imgProfileProfile = (CircleImageView) root.findViewById(R.id.imgProfileProfile);
        recyclerViewtop = (RecyclerView) root.findViewById(R.id.recyclerViewtop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewtop.setLayoutManager(layoutManager);
        viewPager = (DynamicHeightViewPager) root.findViewById(R.id.viewpager_favourite);
        tabLayout = (TabLayout) root.findViewById(R.id.tabLayout_favourite);
        brief_cvList = DataPrefrence.getBriefLanguage(context, Constant.BRIEF_CV_DB);


    }

    public void setupStoryRecylerview() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        userStoryAdapter = new UserStoryAdapter(context, storyModelsList, user_name, profileUrl, new ObjectCallback() {
            @Override
            public void getObject(Object object, int position, View view) {
                storyShare = null;
                storyShare = (StoryModel) object;

                if (view.getId() == R.id.imgRemove)
                {
                    showMenu(view, storyShare, position);
                }

               else if (view.getId() == R.id.layDocument) {

                    String url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();

                    new DownloadFile("", storyShare.getDisplay_doc_name(), "show").execute(url);
//                    Utility.openFile(context,Urls.BASE_IMAGES_URL+storyShare.getStoryUrl());
                }

                else if (view.getId() == R.id.layVideo) {
                    if (storyShare.getStoryType().equals("video_url")) {
                        String id = Utility.extractYTId(storyShare.getStoryUrl());
                        Intent intent = new Intent(context, YoutubeActivity.class);
                        intent.putExtra("ID", id);
                        startActivityForResult(intent, Constant.PAGE_REFRESH);
                    }
                    if (storyShare.getStoryType().equals("video")) {
                        String videoUrl = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("URL", videoUrl);
                        startActivityForResult(intent, Constant.PAGE_REFRESH);
                    }
                }
                else if (view.getId() == R.id.imgShare) {

                    flag = true;
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
                    } else if (storyShare.getStoryType().equals("video_url")) {
                        Utility.shareVideoUrl(storyShare, context);
//                            shareIt();

                    } else if (storyShare.getStoryType().equals("video")) {
                        Utility.shareVideoUrl(storyShare, context);
                    } else if (storyShare.getStoryType().equals("anydoc")) {
                        url = Urls.BASE_IMAGES_URL + storyShare.getStoryUrl();
                        String msg = storyShare.getStoryTitle() + "\n" + storyShare.getStoryText();
                        new DownloadFile(msg, storyShare.getDisplay_doc_name(), "").execute(url);
                    }


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


                    Intent intent = new Intent(context, ShowStoryActivity.class);
                    intent.putExtra("DATA", data);
                    intent.putExtra(Constant.POSITION,position);

                    startActivityForResult(intent, Constant.PAGE_REFRESH);
                }

//                                      showMenu(view,storyData,position);
            }
        });
        recyclerView.setAdapter(userStoryAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();

        profileUrl = DataPrefrence.getPref(context, Constant.PROFILE_IMAGE, "");

        if (profileUrl != null && !profileUrl.isEmpty()) {
            System.out.println("resume " + profileUrl);
            Picasso.with(context).load(profileUrl).placeholder(R.mipmap.profile_large).into(imgProfileProfile);
        }
        txtUserName.setText(user_name);
        brief_cvList = DataPrefrence.getBriefLanguage(context, Constant.BRIEF_CV_DB);

        if (!flag) {
            mViewModel.page = 1;
            storyModelsList = new LinkedList<>();
            initTablayout();
            setupStoryRecylerview();
            setUpRecyclerListener();
            callApis();

        }

    }


    private void callApis() {
        mViewModel.loadUserGroup();
        mViewModel.getUserStoriesApi();
    }

    public void setCustomTextView() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            CustomTextView tv = (CustomTextView) LayoutInflater.from(context).inflate(R.layout.custom_tab_brief, null);
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            // p.setMargins(10, 0, 10, 0);
            tab.requestLayout();
            tabLayout.getTabAt(i).setCustomView(tv);
        }
    }


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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layDrop:

                if (imgDrop.isChecked()) {

//                    imgDrop.setChecked(false);
//                    MyProfileFragment fragment = (MyProfileFragment) viewPager
//                            .getAdapter()
//                            .instantiateItem(viewPager, viewPager.getCurrentItem());
//
//                    viewPager.setVisibility(View.GONE);
                    visibleBioSection(0);

                } else {
                    visibleBioSection(1);
//
//                    imgDrop.setChecked(true);
//                    MyProfileFragment fragment = (MyProfileFragment) viewPager
//                            .getAdapter()
//                            .instantiateItem(viewPager, viewPager.getCurrentItem());
//                    viewPager.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.fab:

                addNewStory();
                break;

            case R.id.imgSetting:
                showMenu(imgSetting);
                break;

        }
    }

    private void initTablayout() {

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setCustomTextView();

    }

    private void setupViewPager(DynamicHeightViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        if(brief_cvList!=null) {
            for (int i = 0; i < brief_cvList.size(); i++) {
                myProfileFragment = MyProfileFragment.newInstance(i, storyDataArrayList);
                Utility.log("frag name " + brief_cvList.get(i).getLanguage_id().getNativeName());
                adapter.addFragment(myProfileFragment, brief_cvList.get(i).getLanguage_id().getNativeName());
            }
        }
        viewPager.setAdapter(adapter);

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


    public void showMenu(View v, final StoryModel storyData, final int position)
    {
        PopupMenu popup = new PopupMenu(context, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_delete:

                        callDeleteStory(storyData, position);

                        return true;


                    case R.id.menu_edit:
                        Intent intent = new Intent(context, UpdateSubscriptionStoryActivity.class);
                        intent.putExtra("DATA", storyData);
                        Bundle arg = new Bundle();
                        arg.putSerializable("List", (Serializable) brief_cvList);
                        intent.putExtra("BUNDLE", arg);
                        intent.putExtra("ID", storyData.get_id());
                        startActivity(intent);
                        return true;


                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_login_user_story, popup.getMenu());


        popup.show();
    }

    private void callDeleteStory(StoryModel storyModel, int position) {
        if (isInternetConnected()) {
            showLoading();
            ReqDeleteStory deleteStory = new ReqDeleteStory();
            deleteStory.setStoryId(storyModel.get_id());
            deleteStory.setStory_provider(storyModel.getStory_provider());
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
//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
//
//            if (position != mCurrentPosition && container instanceof CustomPager) {
//                Fragment fragment = (Fragment) object;
//                CustomPager pager = (CustomPager) container;
//
//                if (fragment != null && fragment.getView() != null) {
//                    mCurrentPosition = position;
//                    pager.measureCurrentView(   fragment.getView());
//                }
//            }
//        }


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

    private void addStoryGroup() {
        Intent intent = new Intent(context, AddSubscriptionGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);
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

                        getUserStoriesApi();


                    }
                }
            }
        });
    }

    private void addNewStory() {
        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setReceiver(this);
        Intent intent = new Intent(context, AddSubscriptionStoryActivity.class);
        Bundle arg = new Bundle();
        arg.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", arg);
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
                if (type.equals("show")) {
                    String sharePath = file.getPath();
                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    Utility.displayDocument(context, uri);

                } else {
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment myFragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (myFragment != null) {
            myFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == Constant.PAGE_REFRESH)
        {
            if(data!=null)
            {
                int position=data.getIntExtra(Constant.POSITION,-1);
                if(data.getStringExtra(Constant.TYPE).equals(Constant.DELETE)) {
                    userStoryAdapter.updateData(position);
                }

            }

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
            if (data != null) {
                String appName = data.getComponent().flattenToShortString();
                if (appName.toLowerCase().contains("session")) {


                } else {
                    Utility.shareVideoUrl(storyShare, context);
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
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof MyProfileFragment)
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }






    public void shareVideoUrl(StoryModel storyData, Context context) {
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
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (!info.activityInfo.packageName.toLowerCase().equals("com.app.session")) {
                    Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                    String[] mimetypes = {"image/*", "text/plain"};
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

    boolean isFlag = false;

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

                      //  sendStoryBody.setViews(body.getString("views"));
                        sendStoryBody.set_id(body.getString("_id"));
                        sendStoryBody.setUser_id(body.getString("user_id"));
                        if(!body.isNull("subscription_id")) {
                            sendStoryBody.setSubscription_id(body.getString("subscription_id"));
                        }
                        sendStoryBody.setDisplay_doc_name(body.getString("display_doc_name"));
                        StoryModel storyModel = new StoryModel();
                        storyModel.set_id(sendStoryBody.get_id());
                        storyModel.setCreatedAt(sendStoryBody.getCreatedAt());
                        storyModel.setDaysAgo("0");
                        storyModel.setViews("0");
                        StoryRead storyRead=new StoryRead();
                        storyRead.setCount(0);
                        storyModel.setStoryRead(storyRead);
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
               // showToast("uploading is fail, please try again");
//                layProgress.setVisibility(View.GONE);
                break;

        }
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_edit_profile:
                        if (baseActivity.isConnectingToInternet(context)) {
                            if (is_consultant.equals("0") && is_company.equals("1")) {
                                startActivity(new Intent(context, ProfileCompanyEditActivity.class));
                            } else if (is_consultant.equals("1")) {
                                startActivity(new Intent(context, ProfileEditConsultantActivity.class));
                            } else if (is_consultant.equals("0")) {
                                startActivity(new Intent(context, ProfileEditUserActivity.class));
                            }
                        } else {
                            showInternetPop(context);
                        }

                        return true;
                    case R.id.menu_edit_mystory:

                        if (baseActivity.isConnectingToInternet(context)) {
                            startActivity(new Intent(context, CreateSpecialGroupActivity.class));
                        } else {
                            showInternetPop(context);
                        }

                        return true;
                    case R.id.menu_pwd:
                        if (baseActivity.isConnectingToInternet(context)) {
                            startActivity(new Intent(context, ChangePasswordActivity.class));
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_status:
//                File file = new File(Environment.getExternalStorageDirectory(),
//                        "Report.pdf");
//                Uri path = Uri.fromFile(file);
//                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pdfOpenintent.setDataAndType(path, "application/pdf");
//                try {
//                    startActivity(pdfOpenintent);
//                }
//                catch (ActivityNotFoundException e) {
//
//                }

//
//                AssetManager assetManager = getAssets();
//
//                InputStream in = null;
//                OutputStream out = null;
//                File file = new File(getFilesDir(), "mypdf.pdf");
//                try
//                {
//                    in = assetManager.open("mypdf.pdf");
//                    out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
//
//                    copyFile(in, out);
//                    in.close();
//                    in = null;
//                    out.flush();
//                    out.close();
//                    out = null;
//                } catch (Exception e)
//                {
//                    Log.e("tag", e.getMessage());
//                }
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(
//                        Uri.parse("file://" + getFilesDir() + "/mypdf.pdf"),
//                        "application/pdf");
//
//                startActivity(intent);
//                return true;
                    case R.id.menu_edit_financial:
                        startActivity(new Intent(context, FinancialActivity.class));
                        return true;
                    case R.id.menu_edit_coureses:
                        startActivity(new Intent(context, CouresesActivity.class));
                        return true;
                    case R.id.menu_edit_report:
                        startActivity(new Intent(context, ReportProblemActivity.class));
                        return true;
                    case R.id.menu_invite:
                        if (baseActivity.isConnectingToInternet(context)) {
                            shareTextUrl();
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_logout:
                        if (baseActivity.isConnectingToInternet(context)) {
                            logoutDialog(1);
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_delete:
                        if (baseActivity.isConnectingToInternet(context)) {
                            logoutDialog(0);
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        if (is_company.equals("0") && is_consultant.equals("0")) {
            inflater.inflate(R.menu.menu_user, popup.getMenu());
        } else {
            inflater.inflate(R.menu.menu_company, popup.getMenu());
        }
        popup.show();
    }

    private void shareTextUrl() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: http://www.consultlot.com/");
        startActivity(Intent.createChooser(shareIntent, "http://www.consultlot.com/"));
    }

    public void logoutDialog(int type) {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_iphone_confirm);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (type == 1) {
                ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setText("Logout");
            } else {
                ((CustomTextView) dd.findViewById(R.id.tvConfirmTitle)).setText("Do you want delete account");
            }

            ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (type == 1) {
                        getUserLogout();

                    } else {
                        getUserDeleteAccount();
                    }
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

    private void getUserDeleteAccount() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.DELETE_MY_ACCOUNT, getDeleteObj(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (!jsonObject.getString("rstatus").equals("0")) {
                            baseActivity.clearDataBase();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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

    private String getDeleteObj() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_cd", userId);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getUserLogout() {

        if (Utility.isConnectingToInternet(context)) {

            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            UserId userID = new UserId();
            userID.setUser_id(userId);
            Call<ResponseBody> call = apiInterface.callLogoutRequest(accessToken, userID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();

                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            //if(js.getBoolean("Status"))
                            {



                                baseActivity.clearDataBase();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                getActivity().startActivity(intent);
                                getActivity().finish();

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

    private void getUserStoriesApi() {
        mViewModel.getUserStoriesApi();
    }

    private void setObserver() {
        mViewModel.getStoryRootMutableLiveData().observe(getViewLifecycleOwner(), new Observer<StoryRoot>() {
            @Override
            public void onChanged(StoryRoot storyRoot) {

                setupUserStoryDataUI(storyRoot);
            }
        });
        mViewModel.getGroupsRootMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserSubscriptionGroupsRoot>() {
            @Override
            public void onChanged(UserSubscriptionGroupsRoot userSubscriptionGroupsRoot)
            {
                setupGroupData(userSubscriptionGroupsRoot);

            }
        });

    }



    private void setupUserStoryDataUI(StoryRoot response) {
        if (response.getStatus() == 200) {
            StoryBody storyBody = response.getStoryBody();
            mViewModel.page = storyBody.getTotal_Page();
            mViewModel.totalPage = storyBody.getTotalOverAllStories();
            total_pages = storyBody.getTotal_Page();
            LinkedList<StoryModel> temp = new LinkedList<>();
            temp = storyBody.getUserStories();
            System.out.println("temp size " + temp.size());
            System.out.println("temp page " + pageno);


            if (temp.size() > 0) {
                visibleBioSection(0);
                if (mViewModel.page <= mViewModel.totalPage) {
                    loading = true;
                    mViewModel.page++;
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

            } else {
                visibleBioSection(1);
            }
        }
    }

    private void setupGroupData(UserSubscriptionGroupsRoot root) {
        if (root.getStatus() == 200) {
            groupsBodyArrayList = root.getGroupsBodyArrayList();
            UserSubscriptionGroupsBody groupsBody=new UserSubscriptionGroupsBody();
            groupsBody.setGroup_name("New");
            groupsBody.set_id("-0new");
            groupsBodyArrayList.add(groupsBody);
            OtherSubscribeGroupAdapter otherSubscribeGroupAdapter = new OtherSubscribeGroupAdapter(context, groupsBodyArrayList, new ApiItemCallback() {
                @Override
                public void result(int position) {
                    UserSubscriptionGroupsBody group = groupsBodyArrayList.get(position);

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
                        intent.putExtra(Constant.SUBS_GROUP,group);
                        intent.putExtra("BUNDLE", bundle);
                        startActivity(intent);
                    } else {
                        addStoryGroup();
                    }
                }
            });
            recyclerViewtop.setAdapter(otherSubscribeGroupAdapter);


        }
    }









}