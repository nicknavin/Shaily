package com.app.session.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.app.session.data.model.SubscriptionId;
import com.app.session.data.model.SubscriptionStoriesRootBody;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.database.DatabaseHelper;
import com.app.session.databinding.ActivityStoryDetailBinding;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.Root;
import com.app.session.data.model.SendStoryBody;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.SubscriptionStories;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.UserDetails;
import com.app.session.data.model.UserStory;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.service.FileUploadService;
import com.app.session.thumby.MainActivitythumby;
import com.app.session.thumby.ThumbyActivity;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;
import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;
import static com.app.session.utility.Utility.isConnectingToInternet;

public class StoryDetailActivity extends BaseActivity implements View.OnClickListener, ServiceResultReceiver.Receiver, PopupMenu.OnMenuItemClickListener {
    private StoryDetailViewModel viewModel;
    Bundle bundle;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    private ServiceResultReceiver mServiceResultReceiver;
    byte[] ByteArray;
    Bitmap videoThumbBitmap = null;
    String selectedVideoPath = "";
    File imageFile;
    Bitmap bmGroupCover = null, bmVideoCover = null;
    String fileType;


    ArrayList<Brief_CV> brief_cvList;

    ReadMoreTextView txtDiscription;
    SubscriptionStoriesAdapter subscriptionStoriesAdapter;

    String groupiconUrl = "", videoUrl = "", videoCoverImg = "", language_cd = "", currency_cd = "", category_cd = "", subscription_group_cd = "", subscription_group_name = "";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ShimmerFrameLayout mShimmerViewContainer;
    LinkedList<SubscriptionStories> storyDataArrayList = new LinkedList<>();
    CircleImageView img_profilepic;
    private DatabaseHelper db;
    SwipeRefreshLayout swipeRefreshLayout;
    String subscription_id;


    String userType = "";
    String userUrl = "", groupUrl = "", groupName = "", loginuserId = "";
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean isFlag = false;
    boolean refreshFlag = false;


    private int storyPosition = -1;
    ImageView imgSetting;

    ActivityStoryDetailBinding binding;

    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_story_detail);
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

        initView();
        getGroupData();
        setupStoryRecylerview();
        setUpRecyclerListener();
        setSwipeLayout();
    }


    private void getGroupData() {

        if (getIntent().getParcelableExtra(Constant.SUBS_GROUP) != null) {
            UserSubscriptionGroupsBody body= getIntent().getParcelableExtra(Constant.SUBS_GROUP);
            viewModel.userSubscriptionGroupsBody.setValue(body);
            ((CustomTextView) findViewById(R.id.header)).setText(user_name);
            if (userUrl != null && !userUrl.isEmpty()) {
                Picasso.with(context).load(Urls.BASE_IMAGES_URL + userUrl).into(img_profilepic);
            }
            binding.setViewmodel(viewModel);
            binding.setLifecycleOwner(this);

        }

    }


    private void initView() {
        binding.fab.setOnClickListener(this);
        swipeRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        img_profilepic = (CircleImageView) findViewById(R.id.img_profilepic);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        txtDiscription = (ReadMoreTextView) findViewById(R.id.txtDiscription);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        txtDiscription.setTypeface(face);
        txtDiscription.setTrimCollapsedText(" more");
        txtDiscription.setTrimExpandedText(" less");
        binding.imgVideoCover.setOnClickListener(this);
        binding.imgMenu.setOnClickListener(this);


        ((ImageView) findViewById(R.id.imgEditdisc)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgEditDetail)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgSetting)).setOnClickListener(this);

        if (userType.equals("0")) {
            binding.fab.hide();
        } else {
            binding.fab.show();
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


        binding.layTop.setOnClickListener(this);
        binding.txtGroupNameCheck.setOnClickListener(this);


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


                        userStory.setUserDetails(story.getUserId());
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
            case R.id.imgDrop:
                setVisibilityGroupDetail();
                break;
            case R.id.layTop:
                setVisibilityGroupDetail();
                break;

            case R.id.txtGroupNameCheck:
                setVisibilityGroupDetail();


                break;

            case R.id.imgVideoCover:
                if (!viewModel.getUserSubscriptionGroupsBody().getValue().getGroup_introduction_video_url().isEmpty())
                {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    String url = Urls.BASE_VIDEO_URL + viewModel.getUserSubscriptionGroupsBody().getValue().getGroup_introduction_video_url();
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
              UserSubscriptionGroupsBody userSubscriptionGroupsBody = viewModel.getUserSubscriptionGroupsBody().getValue();
                Intent intent = new Intent(context, EditSubscriptionGroupDetailActivity.class);
                intent.putExtra("BUNDLE", bundle);
                intent.putExtra("TYPE", "1");
                intent.putExtra(Constant.SUBS_GROUP, userSubscriptionGroupsBody);
                startActivityForResult(intent,Constant.GROUP_UPDATED);
              //


                break;
            case R.id.imgMenu:
//                flagStory = false;
                showMenu(view);
                break;
            case R.id.imgTake_photo:
                setCameraPermission();
                isForCamera = true;
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;

            case R.id.imgTake_gallery:
                setGalleryPermission();
                isForCamera = false;
                if (dialogSelect != null && dialogSelect.isShowing())
                    dialogSelect.dismiss();
                break;
            default:
                break;

        }

    }

    private void setVisibilityGroupDetail() {
        if (binding.imgDrop.isChecked()) {
            binding.imgDrop.setChecked(false);

            binding.layGroupDetail.setVisibility(View.GONE);
        } else {
            binding.imgDrop.setChecked(true);
            binding.layGroupDetail.setVisibility(View.VISIBLE);
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
            public void onChanged(SubscriptionStoriesRoot subscriptionStoriesRoot)
            {
                setupStoryData(subscriptionStoriesRoot.getSubscriptionStoriesRootBody());
            }
        });


        viewModel.getRootMutableLiveData().observe(this, new Observer<Root>() {
            @Override
            public void onChanged(Root root) {
                subscriptionStoriesAdapter.updateData(storyPosition);
            }
        });

        viewModel.getUserSubscriptionGroupsBody().observe(this, new Observer<UserSubscriptionGroupsBody>() {
            @Override
            public void onChanged(UserSubscriptionGroupsBody userSubscriptionGroupsBody)
            {
                showToast("updated");

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
            inflater.inflate(R.menu.menu_login_user_story, popup.getMenu());
        } else {
            inflater.inflate(R.menu.menu_view_story, popup.getMenu());
        }


        popup.show();
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

                    String type=resultData.getString("REQTYPE");
                    log("type "+type);
                        if (resultData.getString("REQTYPE") != null && resultData.getString("REQTYPE").equals(Constant.ADD_NEW_STORY))
                        {
                            if (!isFlag) {
                                {

                                    if (resultData.getString("TYPE").equals("anydoc")) {
                                        if (resultData.getString("FILENAME").contains(".doc"))
                                        {
                                            binding.imageStory.setImageResource(R.mipmap.docs_story);
                                        } else if (resultData.getString("FILENAME").contains(".pdf")) {
                                            binding.imageStory.setImageResource(R.mipmap.pdf_story);
                                        } else if (resultData.getString("FILENAME").contains(".zip")) {
                                            binding.imageStory.setImageResource(R.mipmap.zip_story);
                                        } else if (resultData.getString("FILENAME").contains(".xls")) {
                                            binding.imageStory.setImageResource(R.mipmap.xls_story);
                                        }

                                    }
                                    else if (resultData.getString("TYPE").equals("image") || resultData.getString("TYPE").equals("video"))
                                    {
                                        Bitmap bm = resultData.getParcelable("IMAGE");
                                        binding.imageStory.setImageBitmap(bm);

                                    } else if (resultData.getString("TYPE").equals("audio")) {
                                        binding.imageStory.setImageResource(R.mipmap.attach_audio);
                                    }


                                }
                                binding.layLoading.setVisibility(View.VISIBLE);
                                binding.reyLoading.start();
                                isFlag = true;
                            }
                        }
                            else  if (resultData.getString("REQTYPE") != null&& resultData.getString("REQTYPE").equals(Constant.UPDATE_GROUP_IMAGE))
                        {

                            {
                                Bitmap bm = resultData.getParcelable("IMAGE");
                                binding.imageStory.setImageBitmap(bm);

                            }
                            binding.layLoading.setVisibility(View.VISIBLE);
                            binding.reyLoading.start();
                            isFlag = true;
                        }


                }
                break;

            case STATUS:

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(143);


                showToast("File is uploaded ");

                //  NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();

                if (resultData != null) {
                    if (resultData.getString("REQTYPE") != null && resultData.getString("REQTYPE").equals(Constant.ADD_NEW_STORY)) {
                       String type= resultData.getString("REQTYPE");
                       log("type "+type);
                        binding.reyLoading.stop();
                        binding.layLoading.setVisibility(View.GONE);
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
                    }

                    else  if (resultData.getString("REQTYPE") != null&& resultData.getString("REQTYPE").equals(Constant.UPDATE_GROUP_IMAGE))
                        {
                            binding.reyLoading.stop();
                            binding.layLoading.setVisibility(View.GONE);
                            String type= resultData.getString("REQTYPE");
                            log("type "+type);
                            String data = resultData.getString("DATA");
                            System.out.println("data " + data);
                            try {
                                JSONObject object = new JSONObject(data);
                                JSONObject body = object.getJSONObject("body");
                                body.getString("group_image_url");
                                UserSubscriptionGroupsBody groupsBody=viewModel.getUserSubscriptionGroupsBody().getValue();
                                groupsBody.setGroup_image_url(body.getString("group_image_url"));
                                groupsBody.setGroup_video_thumbnail_url(body.getString("group_video_thumbnail_url"));
                                groupsBody.setGroup_introduction_video_url(body.getString("group_introduction_video_url"));
                                viewModel.getUserSubscriptionGroupsBody().setValue(groupsBody);
                                subscriptionStoriesAdapter.updateGroupIcon(groupsBody.getGroup_name(),groupsBody.getGroup_image_url());
                            }
                            catch (Exception e)
                            {

                            }
                        binding.txtUploading.setVisibility(View.GONE);
                        binding.layProgress.setVisibility(View.GONE);

                    }
//
                }


                break;

            case FAIL:
                showToast("uploading is fail, please try again");
                binding.layProgress.setVisibility(View.GONE);
                break;

        }
    }

    public void initProgress(int value) {
        binding.txtUploading.setText("" + value + "%" + "uploaded...Please wait");
        binding.layProgress.setVisibility(View.VISIBLE);
        binding.progressBar.setProgress(value);

//        if (value == 100) {
//            progressBar.setVisibility(View.GONE);
//
//        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshFlag = true;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:


                    CropImage.activity(mCameraImageUri)
                            .setAspectRatio(1, 1)
                            .setFixAspectRatio(true)
                            .start(this);

                    break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {
//                        String PicturePath="";
                        mImageCaptureUri = data.getData();
//                        Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageCaptureUri);
//                        bm = ConvetBitmap.Mytransform(bm);
//                        bm = Utility.rotateImage(bm, new File(mImageCaptureUri.getPath()));
//                        imgBriefCV.setImageBitmap(bm);
                        CropImage.activity(mImageCaptureUri)
                                .setAspectRatio(1, 1)
                                .setFixAspectRatio(true)
                                .start(this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        try {
                            ByteArray = null;

                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            videoThumbBitmap = bm;
                            imageFile = null;
                            imageFile = Utility.getFileByBitmap(context, bm, "subscription");

                            bmGroupCover = bm;

                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                            ByteArray = datasecond.toByteArray();


                            // imgGroupCover.setImageBitmap(bmGroupCover);
                            fileType = Constant.GROUP_IMAGE;
                            callService();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;


                default:
                    break;


            }
        }
        if (requestCode == Constant.REQUEST_CODE_ALBUM_Gallery) {
            if (data != null) {

                Uri contentURI = data.getData();
                String selectedVideoPath = getPath(contentURI);
                //   callService(selectedVideoPath);
                Log.d("path", selectedVideoPath);

            }
        }
        if (requestCode == Constant.PICK_VIDEO_THUMB) {

            if (data != null) {

                long location = data.getLongExtra(ThumbyActivity.EXTRA_THUMBNAIL_POSITION, 0);
                Uri uri = data.getParcelableExtra(ThumbyActivity.EXTRA_URI);
                Bitmap bitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);
                bmVideoCover = bitmap;
                videoThumbBitmap = bitmap;
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
                byte[] ByteArray = datasecond.toByteArray();
                videoCoverImg = base64String(ByteArray);
                selectedVideoPath = data.getStringExtra(ThumbyActivity.VIDEO_PATH);
                binding.imgVideoCover.setImageBitmap(bmVideoCover);
                fileType = Constant.GROUP_VIDEO;
                callService();
            }

        }
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
        if(requestCode==Constant.GROUP_UPDATED)
        {
            if(data!=null) {
                if (data.getParcelableExtra(Constant.SUBS_GROUP) != null) {
                    UserSubscriptionGroupsBody body=data.getParcelableExtra(Constant.SUBS_GROUP);
                    viewModel.userSubscriptionGroupsBody.setValue(body);
                }
            }
        }
    }


    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.subscriptiongroup_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_select_cover:
                dialog();
                return true;
            case R.id.menu_replace_video:
                callVideoFrameThumb();
                return true;

        }

        return false;
    }

    public void dialog() {
        dialogSelect = new Dialog(context, R.style.MaterialDialogSheet);
        dialogSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelect.setContentView(R.layout.dialog_pick_image);
        dialogSelect.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogSelect.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogSelect.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        int i = Utility.getScreenWidth(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).setTranslationX(-i);

//        ((View) dialogSelect.findViewById(R.id.view1)).setTranslationX(-i);
//        ((View) dialogSelect.findViewById(R.id.view2)).setTranslationX(-i);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).animate().translationX(0).setDuration(500).setStartDelay(400);
//        ((View) dialogSelect.findViewById(R.id.view1)).animate().translationX(0).setDuration(500).setStartDelay(500);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).animate().translationX(0).setDuration(500).setStartDelay(600);
//        ((View) dialogSelect.findViewById(R.id.view2)).animate().translationX(0).setDuration(500).setStartDelay(700);

        ((ImageView) dialogSelect.findViewById(R.id.imgTake_photo)).setOnClickListener(this);
        ((ImageView) dialogSelect.findViewById(R.id.imgTake_gallery)).setOnClickListener(this);


        dialogSelect.show();
    }

    public void callVideoFrameThumb() {
        Intent intent = new Intent(context, MainActivitythumby.class);
        startActivityForResult(intent, Constant.PICK_VIDEO_THUMB);

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);

        return imgString;
    }

    private void callService() {

        if (isConnectingToInternet(context)) {
            mServiceResultReceiver = new ServiceResultReceiver(new Handler());
            mServiceResultReceiver.setReceiver(this);
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", selectedVideoPath);
            mIntent.putExtra("FileName", "");
            mIntent.putExtra("VIDEO_THUMB", videoThumbBitmap);//this for image
            mIntent.putExtra("TYPE", Constant.UPDATE_GROUP_IMAGE);
            mIntent.putExtra("USER_ID", userId);
            mIntent.putExtra("TOKEN", accessToken);
            mIntent.putExtra(Constant.SUBSCN_GROUP_CD, subscription_group_cd);
            mIntent.putExtra("LANGUAGE_ID", language_cd);
            mIntent.putExtra(RECEIVER, mServiceResultReceiver);
            mIntent.setAction(ACTION_DOWNLOAD);
            FileUploadService.enqueueWork(context, mIntent);
        }
        else
        {
            showInternetConnectionToast();
        }

    }

    public void setCameraPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.CAMERA, "Camera")) {
                return;
            }

            if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
                if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                    return;
                }
            }
        }
        TakePic();
    }

    public void setGalleryPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage")) {
                return;
            }
        }

        Gallery();
    }

    //REQUEST_CODE_ALBUM
    public void Gallery() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.REQUEST_CODE_ALBUM);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constant.REQUEST_CODE_ALBUM);
        }
    }

    public void TakePic() {

        try {
            isForCamera = true;
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory() + "/consultlot.png");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mCameraImageUri = Uri.fromFile(f);
            } else {
                mCameraImageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", f);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, Constant.REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    public void checkIfPermissionsGranted() {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }





    /*@Override
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

                        SubscriptionStories subscriptionStories=new SubscriptionStories();
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
    }*/

}
