package com.app.session.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.activity.ExpertProfilePageActivity;
import com.app.session.activity.MyProfileActivityNew;
import com.app.session.activity.ShowStoryActivity;
import com.app.session.activity.SubscriptionGroupProfileActivity;
import com.app.session.adapter.UserSubscriptionStoryAdapter;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customspinner.NiceSpinner;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.database.DatabaseHelper;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.model.BriefCV;
import com.app.session.model.Brief_CV;
import com.app.session.model.UserStory;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserBriefCVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserBriefCVFragment extends BaseFragment implements  PopupMenu.OnMenuItemClickListener  {
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
    BriefCV briefCvData;
    int positionBrief = 0;
    ProgressBar progressBar;
    LinearLayout layProgress;
    ProgressView rey_loading;
    LinearLayout layProfile;
    RecyclerView recyclerView;
    boolean flagRefresh = true;
    Bitmap bmCover;
    View view;
    UserSubscriptionStoryAdapter subscriptionStoryAdapter;

    String groupiconUrl = "", userName = "";
    FloatingActionButton fabAddStory;
LinearLayout layBios;
    private DatabaseHelper db;
    SwipeRefreshLayout swipeRefreshLayout;
    String load="1";
    BriefCV briefCV;
    LinkedList<UserStory> storyDataArrayList=new LinkedList<>();
    public boolean loaddingDone = true;
    public boolean loading = true;
    public int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;

    public UserSubscriptionStoryAdapter userSubscriptionStoryAdapter;

    public UserBriefCVFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserBriefCVFragment newInstance(BriefCV param1)
    {
        UserBriefCVFragment fragment = new UserBriefCVFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            briefCV = getArguments().getParcelable(ARG_PARAM1);
//            storyDataArrayList = (ArrayList<UserStory>) getArguments().getSerializable(ARG_PARAM2);
        }
        context = getContext();
        db = new DatabaseHelper(context);
        userName=user_name;
        groupiconUrl=profileUrl;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
//        brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");
//        position=bundle.getInt("position");

        ((ExpertProfilePageActivity)getActivity()).setFragmentRefreshListener(new MyProfileActivityNew.FragmentRefreshListener() {
            @Override
            public void onRefresh(boolean flag)
            {
                showToast("Interface calling");
                setVisibleBio(flag);
            }


        });

        return inflater.inflate(R.layout.fragment_briefcv_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initView();

        setBriefLayout(briefCV);

    }

    public void initView() {

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

    }
    private void setBriefLayout(BriefCV briefCv) {
        if (briefCv != null)
        {

            briefCvData = briefCv;



            language_id=briefCv.getmLanguageId().get_id();
            //  ((MyProfileActivityNew)getActivity()).setTitleName(briefCv.getEnglish_name());


            mFileName = language_cd + "introduction.mp4";
            videoUrl = Urls.BASE_IMAGES_URL + briefCv.getVideoUrl();
            if (briefCv.getVideoThumbnail() != null && !briefCv.getVideoThumbnail().isEmpty() && !briefCv.getVideoThumbnail().equals("null")) {
                String url = Urls.BASE_IMAGES_URL + briefCv.getVideoThumbnail();
                System.out.println("cover_img" + url);
                layPlaceholder.setVisibility(View.GONE);
                rey_loading.start();
                Picasso.with(context).load(url).into(imgBriefCV);
            } else {
                imgBriefCV.setVisibility(View.GONE);
                layPlaceholder.setVisibility(View.GONE);
            }
            if (!briefCv.getTitleName().equals("0")) {
                txtTitleBriefCV.setText(briefCv.getTitleName());
            }

            System.out.println("DATA 0 " + briefCv.getBriefCv());
            txtBriefCV.setText(briefCv.getBriefCv());


        }
    }


    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_view_story, popup.getMenu());
        popup.show();
    }

    public void setupStoryRecylerview()
    {
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        userSubscriptionStoryAdapter=new UserSubscriptionStoryAdapter(context, storyDataArrayList,userName,groupiconUrl, new ObjectCallback()
        {
            @Override
            public void getObject(Object object, int position,View view)
            {
                if (!userId.isEmpty()) {
                    if(position==-1) {
                        startActivity(new Intent(context, SubscriptionGroupProfileActivity.class));
                    }
                    else {

                        if(view.getId()==R.id.imgRemove) {
                            showMenu(view);
                        }
                        else
                        {
                            UserStory userStory=(UserStory)object;
                            Intent intent=new Intent(context, ShowStoryActivity.class);
                            intent.putExtra("DATA",userStory);
                            startActivity(intent);
                        }

                    }
                }

            }
        });
        recyclerView.setAdapter(userSubscriptionStoryAdapter);


    }

    public void setUpRecyclerListener()
    {
        loading = true;
        loaddingDone = true;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();


                if (loading && loaddingDone)
                {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {

                        loading = false;
                        Utility.Log("inside the recly litner");






                    }
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_report:
                return true;
        }

        return false;
    }






}
