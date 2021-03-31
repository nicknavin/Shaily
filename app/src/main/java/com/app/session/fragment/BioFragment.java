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
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
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
import com.app.session.activity.AddSubscriptionStoryActivity;
import com.app.session.activity.EditBriefActivity;
import com.app.session.activity.PlayerActivity;
import com.app.session.activity.UpdateSubscriptionStoryActivity;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.StoryData;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.thumby.MainActivitythumby;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;
import static com.app.session.utility.Utility.isConnectingToInternet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BioFragment extends BaseFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver{
        // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DATA = "data";
Context context;
    ArrayList<Brief_CV> brief_cvList;
    // TODO: Rename and change types of parameters
    private int positionBrief;
    private String mParam2;
    Brief_CV briefCvData;
    ImageView imgBriefCV,imgSetting;
    CustomTextView txtTitleBriefCV,txtUploading;
    ReadMoreTextView txtBriefCV;
    RelativeLayout layPlaceholder;
    ProgressBar progressBar;
    LinearLayout layProgress;
    ProgressView rey_loading;
    String language_cd = "";
    private Dialog dialogSelect;
    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri;
    byte[] ByteArray;
    private ServiceResultReceiver mServiceResultReceiver;

    public static final String RECEIVER = "receiver";
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    String mFileName = "", videoUrl = "", coverImg = "";
    Bitmap bmCover;
    boolean flagRefresh = true;
    public BioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BioFragment newInstance(int param1, String param2,Brief_CV brief_cv) {
        BioFragment fragment = new BioFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelable(DATA, brief_cv);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            positionBrief = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            briefCvData = getArguments().getParcelable(DATA);
        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getUserDetail(0);
    }

    private void initView(View view)
    {
        rey_loading = (ProgressView) view.findViewById(R.id.rey_loading);
        imgBriefCV=(ImageView)view.findViewById(R.id.imgBriefCV);
        imgSetting=(ImageView)view.findViewById(R.id.imgSetting);
        txtBriefCV=(ReadMoreTextView)view.findViewById(R.id.txtBriefCV);
        Typeface face = Typeface.createFromAsset(getContext().getAssets(),
                "Segoe_UI.ttf");
        txtBriefCV .setTypeface(face);
        txtBriefCV .setTrimCollapsedText(" more");
        txtBriefCV .setTrimExpandedText(" less");
        txtTitleBriefCV=(CustomTextView)view.findViewById(R.id.txtTitleBriefCV);
        layPlaceholder = (RelativeLayout) view.findViewById(R.id.layPlaceholder);
        txtUploading = (CustomTextView) view.findViewById(R.id.txtUploading);




    }


    private void setBriefValue()
    {
//        if(briefCv !=null)
//        {
//            txtTitleBriefCV.setText(briefCv.getTitle_name());
//            txtTitleBriefCV.setText(briefCv.getBrief_cv());
//            if (briefCv.getVideo_thumbnail() != null && !briefCv.getVideo_thumbnail().isEmpty() && !briefCv.getVideo_thumbnail().equals("null"))
//            {
//                String url = Urls.BASE_IMAGES_URL + briefCv.getVideo_thumbnail();
//                System.out.println("cover_img" + url);
//                layPlaceholder.setVisibility(View.GONE);
//                rey_loading.start();
//
//                Picasso.with(getContext()).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgBriefCV);
//
//            } else {
//                imgBriefCV.setVisibility(View.GONE);
//                layPlaceholder.setVisibility(View.GONE);
//            }
//            if(!briefCv.getTitle_name().equals("0"))
//            {
//                txtTitleBriefCV.setText(briefCv.getTitle_name());
//            }
//
//            txtBriefCV.setText(briefCv.getBrief_cv());
//        }
    }

    private void setBriefLayout(Brief_CV briefCv, int position)
    {
        if (briefCv != null) {

            briefCvData = briefCv;
            positionBrief = position;

            language_cd = briefCv.getLanguage_cd();
            //  ((MyProfileActivityNew)getActivity()).setTitleName(briefCv.getEnglish_name());




            mFileName = language_cd + "introduction.mp4";
            videoUrl = Urls.BASE_VIDEO_URL + briefCv.getVideo_url();
            if (briefCv.getVideo_thumbnail() != null && !briefCv.getVideo_thumbnail().isEmpty() && !briefCv.getVideo_thumbnail().equals("null"))
            {
                String url = Urls.BASE_IMAGES_URL + briefCv.getVideo_thumbnail();
                System.out.println("cover_img" + url);
                layPlaceholder.setVisibility(View.GONE);
                rey_loading.start();
//                Glide.with(getActivity())
//                        .load(url)
//                        .into(imgBriefCV);
//                Glide.with(getActivity())
//                        .load(url)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .into(imgBriefCV);
                Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgBriefCV);
//                Picasso.with(context).invalidate(url);
//                Picasso.with(context).load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
                //  .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
            } else {
                imgBriefCV.setVisibility(View.GONE);
                layPlaceholder.setVisibility(View.GONE);
            }
            if(!briefCv.getTitle_name().equals("0"))
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
//                intent = new Intent(context, AddSubscriptionGroupActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("List", (Serializable) brief_cvList);
//                intent.putExtra("BUNDLE", bundle);
//                startActivity(intent);
                break;


            case R.id.imgBriefCV:
                if (!videoUrl.isEmpty()) {
                    intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("URL", videoUrl);
                    startActivity(intent);
                }
                break;
            case R.id.imgSetting:
                showMenu(imgSetting,1);
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






    public void showMenu(View v, final StoryData storyData, final int position)
    {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_delete:
                        if (isConnectingToInternet(context))
                        {
                            callDeleteStory(storyData.getStory_id(),position);
                        } else {
                            showInternetPop(context);
                        }
                        return true;

                    case R.id.menu_edit:
                        Intent intent=new Intent(context, UpdateSubscriptionStoryActivity.class);
                        intent.putExtra("DATA",storyData);
                        Bundle arg = new Bundle();
                        arg.putSerializable("List", (Serializable) brief_cvList);
                        intent.putExtra("BUNDLE", arg);
                        intent.putExtra("ID",storyData.getSubscription_group_cd());
                        startActivity(intent);
                        return  true;


                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_subscription_story, popup.getMenu());


        popup.show();
    }



    public void showMenu(View v,int type) {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        if(type==1) {
            inflater.inflate(R.menu.brief_menu, popup.getMenu());
        }
        else if(type==2)
        {
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

    public void TakePic() {

        try {
            isForCamera = true;
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory() + "/sessionway.png");
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

    public void setGalleryPermission()
    {
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

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
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





    private void addNewStory()
    {
        Intent  intent=new Intent(context, AddSubscriptionStoryActivity.class);
        Bundle arg = new Bundle();
        arg.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", arg);
        intent.putExtra("ID","0");

        startActivity(intent);
    }

    private void callDeleteStory(String story_id, int position) {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.SUBSCRIPTION_DELETE_STORY, getDeleteParameter(story_id), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        System.out.println("");
                        JSONObject jsonObject = js.getJSONObject("result");

                        if (jsonObject.getString("rstatus").equals("1"))
                        {
                            //subscriptionStoryAdapter.updateData(position);


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
    private String getDeleteParameter(String story_id) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("story_id",story_id);


            return jsonObject.toString();



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  System.out.println("input : " + gsonObject.toString());
        return null ;
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
                            ByteArray = datasecond.toByteArray();
                            String coverImg = base64String(ByteArray);
                            callUploadImage(coverImg, Utility.getTimeOnly() + "_thumbnail");

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
            if (data != null) {

                Uri contentURI = data.getData();
                String selectedVideoPath = getPath(contentURI);
               // callService(selectedVideoPath);
                Log.d("path", selectedVideoPath);

            }
        }
        if (requestCode == Constant.PICK_VIDEO_THUMB) {

            if (data != null) {

                long location = data.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0);
                Uri uri = data.getParcelableExtra(EXTRA_URI);
                Bitmap bitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);
                imgBriefCV.setImageBitmap(bitmap);
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, datasecond);
                byte[] ByteArray = datasecond.toByteArray();
                coverImg = base64String(ByteArray);
                String selectedVideoPath = data.getStringExtra(VIDEO_PATH);
//                imgBriefCV.setImageBitmap(bm);


                //callService(selectedVideoPath);

            }

        }
        if (requestCode == Constant.REQUEST_BRIEF) {

            if (data != null) {

                String briefcvtxt = data.getStringExtra("DATA");
                txtBriefCV.setText(briefcvtxt);
                String title = data.getStringExtra("TITLE");
                txtTitleBriefCV.setText(title);

                Brief_CV brief_cv = briefCvData;
                brief_cv.setTitle_name(title);
                brief_cv.setBrief_cv(briefcvtxt);

                brief_cvList.set(positionBrief, brief_cv);

                //  callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);

            }

        }

    }


    private String base64String(byte[] b) {
        String imgString = Base64.encodeToString(b, Base64.NO_WRAP);

        return imgString;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
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


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    private void callUploadImage(String baseImg, String imgName) {
        if (isConnectingToInternet(context)) {
            showLoading();
            new BaseAsych(Urls.UPLOADIMAGE, getParamUplodImage(baseImg, imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1")) {
                            callUploadImageFinal(imgName);
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
    private String getParamUplodImage(String baseImg, String imgName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Img", baseImg);
            jsonObject.put("fname", imgName);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void callUploadImageFinal(String imgName) {
        if (isConnectingToInternet(context)) {
            showLoading();
            new BaseAsych(Urls.UPLOAD_IMAGE, getParamUplodImageFinal(imgName), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {

                        //{"result":{"rdescription":"Success","rstatus":"1","url":"af_157_thumbnail.png"}}
                        JSONObject jsonObject = js.getJSONObject("result");
                        System.out.println("result "+jsonObject.toString());
                        if (jsonObject.getString("rstatus").equals("1")) {


                            getUserDetail(1);


//                            rey_loading.start();
//                            Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).into(imgBriefCV, new Callback() {
//                                @Override
//                                public void onSuccess() {
//                                    rey_loading.stop();
//                                }
//
//                                @Override
//                                public void onError() {
//                                    rey_loading.stop();
//                                }
//                            });
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

    private String getParamUplodImageFinal(String imgName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("language_cd", language_cd);
            jsonObject.put("user_cd", userId);
            jsonObject.put("thumbnail_url", imgName + ".png");
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void getUserDetail(int refresh) {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getUserDetailRequest(userId, accessToken);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {

                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getBoolean("Status")) {
                                try {
                                    JSONArray jsonArray = js.getJSONArray("User_Detail");
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                    userName= jsonObject.getString("user_name");
//                                    if (jsonObject.getString("imageUrl") != null && !jsonObject.getString("imageUrl").isEmpty()) {
//                                        groupiconUrl=jsonObject.getString("imageUrl");
//                                    }

                                    JSONArray arrayBrief = js.getJSONArray("user_brief");
                                    if(brief_cvList!=null){
                                        brief_cvList.clear();}
                                    brief_cvList = new ArrayList<>();

                                    for (int i = 0; i < arrayBrief.length(); i++) {
                                        JSONObject object = arrayBrief.getJSONObject(i);
                                        Brief_CV brief_cv = new Brief_CV();
                                        brief_cv.setBrief_cv(object.getString("breaf_cv"));
                                        brief_cv.setLanguage_cd(object.getString("language_cd"));
                                        brief_cv.setNative_name(object.getString("native_name"));
                                        brief_cv.setEnglish_name(object.getString("english_name"));
                                        brief_cv.setSer_no(object.getString("ser_no"));
                                        brief_cv.setVideo_thumbnail(object.getString("video_thumbnail"));
                                        brief_cv.setVideo_duration(object.getString("video_duration"));
                                        brief_cv.setVideo_url(object.getString("video_url"));
//                                        brief_cv.setTitle_cd(object.getString("titele_cd"));
                                        brief_cv.setTitle_name(object.getString("title_name"));
                                        brief_cvList.add(brief_cv);
                                    }


                                    if (refresh==0)
                                    {
                                        setBriefLayout(brief_cvList.get(positionBrief), positionBrief);
                                    }



//                                    if (brief_cvList.size() > 0) {
//
//                                        setBriefLayout(brief_cvList.get(0), 0);
//                                        spinnerBriefTab.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
//                                            @Override
//                                            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
//                                                Brief_CV brief_cv = (Brief_CV) spinnerBriefTab.getSelectedItem();
//                                                dfd();
//                                                setBriefLayout(brief_cv, position);
//                                            }
//                                        });
//
//
//                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
}
