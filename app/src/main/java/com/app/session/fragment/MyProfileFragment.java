package com.app.session.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.activity.AddSubscriptionGroupActivity;
import com.app.session.activity.AddSubscriptionStoryActivity;
import com.app.session.activity.EditBriefActivity;

import com.app.session.activity.VideoPlayerActivity;
import com.app.session.activity.ui.home.HomeFragment;
import com.app.session.activity.ui.profile.ProfileFragment;
import com.app.session.adapter.UserStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customspinner.NiceSpinner;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.model.Brief_CV;
import com.app.session.model.ReqDeleteStory;
import com.app.session.model.Root;
import com.app.session.model.StoryModel;
import com.app.session.model.User;
import com.app.session.model.UserId;
import com.app.session.model.UserRoot;
import com.app.session.model.UserStory;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.service.FileUploadService;
import com.app.session.thumby.MainActivitythumby;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rey.material.widget.ProgressView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;
import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;
import static com.app.session.utility.Utility.isConnectingToInternet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends BaseFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    Context context;
    CustomTextView txtUserName, txtTitleBriefCV, txtUploading, txtBriefTab;
    ReadMoreTextView txtBriefCV;
    CircleImageView imgProfile;
    ImageView imgSetting, imgBriefCV;
    Bitmap bitUpload;
    RelativeLayout layPlaceholder;
    ArrayList<Brief_CV> brief_cvList;
    NiceSpinner spinnerBrief;
    String language_cd = "",language_id="";
    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    private ServiceResultReceiver mServiceResultReceiver;
    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    String mFileName = "", videoUrl = "", coverImg = "";
    Brief_CV briefCvData;
    int positionBrief = 0;
    ProgressBar progressBar;
    LinearLayout layProgress;
    ProgressView rey_loading;
    LinearLayout layProfile;
    RecyclerView recyclerView;
    boolean flagRefresh = true;
    Bitmap bmCover;
    View view;

   public UserStoryAdapter userStoryAdapter;
    LinkedList<StoryModel> storyDataArrayList = new LinkedList<>();
    ArrayList<UserStory> list = new ArrayList<>();
    String groupiconUrl = "", userName = "";
    FloatingActionButton fabAddStory;
    LinearLayout layBios;

    SwipeRefreshLayout swipeRefreshLayout;
    String load="1";
    public boolean loaddingDone = true;
    public boolean loading = true;
    public int pastVisiblesItems, visibleItemCount, totalItemCount;
LinearLayoutManager linearLayoutManager;
    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(int param1, ArrayList<UserStory> list) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
//        args.putSerializable(ARG_PARAM2, (Serializable) list);
        fragment.setArguments(args);
        return fragment;
    }
    ProfileFragment parentFrag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
//            list = (ArrayList<UserStory>) getArguments().getSerializable(ARG_PARAM2);
//
//            for(UserStory userStory:list)
//            {
//                storyDataArrayList.addFirst(userStory);
//            }
        }
        context = getContext();

        userName=user_name;
        groupiconUrl=profileUrl;
         parentFrag = ((ProfileFragment)this.getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
//        brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");
//        position=bundle.getInt("position");

        parentFrag.setFragmentRefreshListener(new ProfileFragment.FragmentRefreshListener() {
            @Override
            public void onRefresh(boolean flag) {
                showToast("Interface calling");
                setVisibleBio(flag);
            }
        });
//        ((MyProfileActivityNew)getActivity()).setFragmentRefreshListener(new MyProfileActivityNew.FragmentRefreshListener()
//        {
//            @Override
//            public void onRefresh(boolean flag)
//            {
//                showToast("Interface calling");
//                setVisibleBio(flag);
//            }
//
//
//        });

        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initView();
        //setupStoryRecylerview();
       // setSwipeLayout();
        System.out.println("111onViewCreated ");

        getUserDetail(0);

    }




    public void initView() {
      //  swipeRefreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout));
        layBios=(LinearLayout)view.findViewById(R.id.layBios);

        spinnerBrief = (NiceSpinner) view.findViewById(R.id.spinnerBrief);
        layProfile = (LinearLayout) view.findViewById(R.id.layProfile);
        rey_loading = (ProgressView) view.findViewById(R.id.rey_loading);
        layProgress = (LinearLayout) view.findViewById(R.id.layProgress);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        txtUserName = (CustomTextView) view.findViewById(R.id.txtUserName);
        txtTitleBriefCV = (CustomTextView) view.findViewById(R.id.txtTitleBriefCV);
        txtUploading = (CustomTextView) view.findViewById(R.id.txtUploading);
        txtBriefTab = (CustomTextView) view.findViewById(R.id.txtBriefTab);
        txtBriefCV = (ReadMoreTextView) view.findViewById(R.id.txtBriefCV);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "Segoe_UI.ttf");
        txtBriefCV.setTypeface(face);
        txtBriefCV.setTrimCollapsedText(" more");
        txtBriefCV.setTrimExpandedText(" less");
        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        layPlaceholder = (RelativeLayout) view.findViewById(R.id.layPlaceholder);
        imgBriefCV = (ImageView) view.findViewById(R.id.imgBriefCV);
        imgBriefCV.setOnClickListener(this);
        imgSetting = (ImageView) view.findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(this);



    }
    public void setSwipeLayout() {

        swipeRefreshLayout.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

              //  callGetStory();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public void setVisibleBio(boolean flag)
    {
        if(flag)
        {
            if(layBios!=null) {
  layBios.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if(layBios!=null)
            {

                layBios.setVisibility(View.GONE);
            }
        }

    }






    @Override
    public void onResume() {
        super.onResume();
        System.out.println("111onResume called");

    }

    private void setBriefLayout(Brief_CV briefCv) {
        if (briefCv != null) {

            briefCvData = briefCv;
            language_cd = briefCv.getLanguage_cd();
            language_id=briefCv.getLanguage_id().get_id();
            mFileName = language_cd + "introduction.mp4";
            if(!briefCv.getVideo_url().isEmpty()) {
                videoUrl = Urls.BASE_IMAGES_URL + briefCv.getVideo_url();
            }
            if (briefCv.getVideo_thumbnail() != null && !briefCv.getVideo_thumbnail().isEmpty() && !briefCv.getVideo_thumbnail().equals("null"))
            {
                String url = Urls.BASE_IMAGES_URL + briefCv.getVideo_thumbnail();
                System.out.println("cover_img" + url);
                layPlaceholder.setVisibility(View.GONE);
//                rey_loading.start();
                try {
                    Glide.with(context)
                            .load(url)
                            .placeholder(R.drawable.black_ripple_btn_bg_squre)
                            .error(R.drawable.black_ripple_btn_bg_squre)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgBriefCV);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Picasso.with(context).load(url).into(imgBriefCV);

            } else {
                imgBriefCV.setVisibility(View.GONE);
                layPlaceholder.setVisibility(View.GONE);
            }
            if (!briefCv.getTitle_name().equals("0"))
            {
                txtTitleBriefCV.setText(briefCv.getTitle_name());
            }

            txtBriefCV.setText(briefCv.getBrief_cv());

        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.fab:
                addNewStory();
                break;
            case R.id.layAddSubcription:
                intent = new Intent(context, AddSubscriptionGroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("List", (Serializable) brief_cvList);
                intent.putExtra("BUNDLE", bundle);
                startActivity(intent);
                break;


            case R.id.imgBriefCV:
                if (!videoUrl.isEmpty())
                {
                    intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("URL", videoUrl);
                    startActivity(intent);
                }
                break;
            case R.id.imgSetting:
                showMenu(imgSetting, 1);
//                    CustomPopupSpinner(imgSetting);


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

    public void showMenu(View v, int type) {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        if (type == 1) {
            inflater.inflate(R.menu.brief_menu, popup.getMenu());
        } else if (type == 2) {
            inflater.inflate(R.menu.menu_indivdual_story, popup.getMenu());
        }


        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.menu_report:

                return true;
            case R.id.menu_select_cover:

                 //   ((MyProfileActivityNew)getActivity()).flag=true;

                parentFrag.flag=true;
                flagRefresh = false;
                dialog();
                return true;
            case R.id.menu_replace_video:
                flagRefresh = false;
                callVideoFrameThumb();
                return true;
            case R.id.menu_edit_introduction:
                flagRefresh = false;
                Intent intent = new Intent(context, EditBriefActivity.class);
                intent.putExtra("DATA", briefCvData);
                startActivityForResult(intent, Constant.REQUEST_BRIEF);
                return true;
            case R.id.menu_cancel:

                return true;
        }

        return false;
    }

    public void callVideoFrameThumb() {
        Intent intent = new Intent(context, MainActivitythumby.class);
        startActivityForResult(intent, Constant.PICK_VIDEO_THUMB);

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

        int i = Utility.getScreenWidth(getActivity());
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





    private void callService(String path) {
        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setReceiver(this);
        Intent mIntent = new Intent(getActivity(), FileUploadService.class);
        mIntent.putExtra("mFilePath", path);
        mIntent.putExtra("FileName", mFileName);
        mIntent.putExtra("LANGUAGE_ID", language_id);
        mIntent.putExtra("USER_ID", userId);
        mIntent.putExtra("TOKEN", accessToken);
        mIntent.putExtra(RECEIVER, mServiceResultReceiver);
        mIntent.setAction(ACTION_DOWNLOAD);
        FileUploadService.enqueueWork(getActivity(), mIntent);

    }




    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SHOW_RESULT:
                if (resultData != null) {
                    initProgress(resultData.getInt("progress"));

                }
                break;

            case STATUS:
                layProgress.setVisibility(View.GONE);
                callUpdateBriefCvImage();
                break;

            case FAIL:

                showToast("uploading is fail, please try again");
                layProgress.setVisibility(View.GONE);
                break;

        }
    }






    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
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

    public void initProgress(int value) {
        txtUploading.setText("" + value + "%" + "uploaded...Please wait");
        layProgress.setVisibility(View.VISIBLE);
        progressBar.setProgress(value);

//        if (value == 100) {
//            progressBar.setVisibility(View.GONE);
//
//        }

    }




    private void getUserDetail(int refresh) {
        if (Utility.isConnectingToInternet(context))
        {  UserId user=new UserId();
            user.setUser_id(userId);
//            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<UserRoot> call = apiInterface.getUserDetails(accessToken, user);
            call.enqueue(new retrofit2.Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
//                    dismiss_loading();

                    if(response.body()!=null)
                    {
                        UserRoot root = response.body();
                        if(root.getStatus()==200)
                        {
                            User user=root.getUserBody().getUser();
                            userName = user.getUser_name();

//                            if((MyProfileActivityNew)getActivity()!=null)
//                            {
//                                if (((MyProfileActivityNew) getActivity()).txtSubsciber != null) {
//                                    ((MyProfileActivityNew) getActivity()).txtSubsciber.setText("" + root.getUserBody().getFollowers());
//                                    ((MyProfileActivityNew) getActivity()).txtUserName.setText(root.getUserBody().getUser().getUser_name());
//                                    ((MyProfileActivityNew) getActivity()).profileUrl = Urls.BASE_IMAGES_URL + root.getUserBody().getUser().getImageUrl();
//                                }
//                            }
                            if(parentFrag!=null)
                            {
                                if (parentFrag.txtSubsciber != null) {
                                    parentFrag.txtSubsciber.setText("" + root.getUserBody().getFollowers());
                                    parentFrag.txtUserName.setText(root.getUserBody().getUser().getUser_name());
                                    parentFrag.profileUrl = Urls.BASE_IMAGES_URL + root.getUserBody().getUser().getImageUrl();
                                }
                            }
                            if(!user.getImageUrl().isEmpty()&&user.getImageUrl()!=null)
                            {
                                groupiconUrl=user.getImageUrl();
                            }
                            brief_cvList=user.getBriefCV();
                            if (refresh == 0)
                            {
                                if(brief_cvList.size()>0) {
                                    setBriefLayout(brief_cvList.get(mParam1));
                                }
                            }

                        }
                    }





                }

                @Override
                public void onFailure(Call<UserRoot> call, Throwable t) {
//                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }





    private void addNewStory() {
        Intent intent = new Intent(context, AddSubscriptionStoryActivity.class);
        Bundle arg = new Bundle();
        arg.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", arg);
        intent.putExtra("ID", "");
        intent.putExtra("POSITION",mParam1);
        startActivityForResult(intent,Constant.REQUEST_NEW_STORY);

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
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE, "Camera")) {
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
    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    public void checkIfPermissionsGranted() {
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
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                {
                    showToast("onRequestPermissionsResult");
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults))
                {
                    if (isForCamera)
                        TakePic();
                    else
                        Gallery();
                } else {
                    checkIfPermissionsGranted();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




//    private void showStoryData(JSONArray jsonArray) {
//        Type type = new TypeToken<ArrayList<StoryData>>() {
//        }.getType();
//        storyDataArrayList = new Gson().fromJson(jsonArray.toString(), type);
//        subscriptionStoryAdapter = new SubscriptionStoryAdapter(context, storyDataArrayList, userName, groupiconUrl, new ObjectCallback() {
//            @Override
//            public void getObject(Object object, int position, View view) {
//                if (position != -1) {
//                    StoryData storyData = (StoryData) object;
//
//                    showMenu(view, storyData, position);
//                }
////                                      showMenu(view,storyData,position);
//            }
//        });
//        recyclerView.setAdapter(subscriptionStoryAdapter);
//    }
    File imageFile =null;

    private void callUpdateBriefCvImage()
    {
        if (isConnectingToInternet(context))
        {
            showLoading();
            RequestBody usercd=RequestBody.create( MediaType.parse("text/plain"),userId);
            RequestBody languageId=RequestBody.create( MediaType.parse("text/plain"),language_id);
            MultipartBody.Part productimg= null;
            RequestBody requestfile=null;
            if (imageFile !=null) {
                requestfile=RequestBody.create(MediaType.parse("image/jpeg"),imageFile);
                productimg = MultipartBody.Part.createFormData("image",imageFile.getName(),requestfile);
            }

            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.reqUpdateBriefThumbnail(accessToken,usercd,languageId,productimg);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
//                        errorBody(response,true);

                        try {
                            try {
                                ResponseBody responseBody=null;

                                responseBody = response.body();

                                String data =responseBody.string();

                                System.out.println("error1"+data);
                                JSONObject jsonObject=new JSONObject(data);

                                if(jsonObject.getInt("status")==200)
                                {
                                    showToast(jsonObject.getString("message"));
                                    imgBriefCV.setVisibility(View.VISIBLE);
                                    imgBriefCV.setImageBitmap(bitUpload);

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else
                    {
//                        errorBody(response,false);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
        else
        {
            showInternetConnectionToast();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:


                    CropImage.activity(mCameraImageUri)
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setAspectRatio(2, 1)
                            .start(getActivity());

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
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setAspectRatio(2, 1)
                                .start(getActivity());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                    ((MyProfileActivityNew)getActivity()).flag=true;
                    parentFrag.flag=true;
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        try {
                            ByteArray = null;
                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            bmCover = bm;

                            imgBriefCV.setImageBitmap(bm);
                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, datasecond);
                            bitUpload=bm;
                            imageFile =Utility.getFileByBitmap(context,bm,"thumb");
                            callUpdateBriefCvImage();
//
//                            ByteArray = datasecond.toByteArray();
//                            String coverImg = base64String(ByteArray);
//                            callUploadImage(coverImg, Utility.getTimeOnly() + "_thumbnail");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                    break;


                default:
                    break;


            }
        }
        if (requestCode == Constant.REQUEST_CODE_ALBUM_Gallery) {
//            ((MyProfileActivityNew)getActivity()).flag=true;
            parentFrag.flag=true;
            if (data != null) {

                Uri contentURI = data.getData();
                String selectedVideoPath = getPath(contentURI);
                callService(selectedVideoPath);
                Log.d("path", selectedVideoPath);

            }
        }
        if (requestCode == Constant.PICK_VIDEO_THUMB) {
//            ((MyProfileActivityNew)getActivity()).flag=true;
            parentFrag.flag=true;
            if (data != null) {

                long location = data.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0);
                Uri uri = data.getParcelableExtra(EXTRA_URI);
                Bitmap bitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);
                bitUpload=bitmap;
                imgBriefCV.setImageBitmap(bitmap);
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, datasecond);
                imageFile =Utility.getFileByBitmap(context,bitmap,"thumb");
                byte[] ByteArray = datasecond.toByteArray();

                String selectedVideoPath = data.getStringExtra(VIDEO_PATH);
//                imgBriefCV.setImageBitmap(bm);


                callService(selectedVideoPath);

            }

        }
        if (requestCode == Constant.REQUEST_BRIEF)
        {
//            ((MyProfileActivityNew)getActivity()).flag=true;
            parentFrag.flag=true;
            if (data != null) {

                String briefcvtxt = data.getStringExtra("DATA");
                txtBriefCV.setText(briefcvtxt);
                String title = data.getStringExtra("TITLE");
                txtTitleBriefCV.setText(title);

                Brief_CV brief_cv = briefCvData;
                brief_cv.setTitle_name(title);
                brief_cv.setBrief_cv(briefcvtxt);
                System.out.println("Update is called");
                showToast("Update is called");
                brief_cvList.set(positionBrief, brief_cv);
                getUserDetail(0);
                //  callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);

            }



        }
//        if (requestCode == Constant.REQUEST_NEW_STORY)
//        {
//            ((MyProfileActivityNew)getActivity()).flag=true;
//            if (data != null) {
//                boolean refresh = data.getBooleanExtra("REFRESH",false);
//
//
//                if(refresh) {
//
//                    if(data.getParcelableExtra("DATA")!=null)
//                    {
//                        SendStoryBody story=data.getParcelableExtra("DATA");
//                        StoryModel storyModel=new StoryModel();
//                        storyModel.set_id(story.get_id());
//                        storyModel.setCreatedAt(story.getCreatedAt());
//                        storyModel.setDaysAgo("0");
//                        storyModel.setViews("0");
//                        storyModel.setStoryText(story.getStory_text());
//                        storyModel.setStoryTitle(story.getStory_title());
//                        storyModel.setStoryType(story.getStory_type());
//                        storyModel.setStoryUrl(story.getStory_url());
//                        UserDetails details=new UserDetails();
//                        details.setId(story.get_id());
//                        storyModel.setUserDetails(details);
//                        ((MyProfileActivityNew)getActivity()).storyModelsList.addFirst(storyModel);
//                        userStoryAdapter.notifyDataSetChanged();
//                    }
////                    storyModelsList
//
//
////                    ((MyProfileActivityNew)getActivity()).getUserStory();
//
//
//                }
//            }
//        }

    }



}
