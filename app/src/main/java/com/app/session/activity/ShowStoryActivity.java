package com.app.session.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.model.ReqDeleteStory;
import com.app.session.model.Root;
import com.app.session.model.StoryId;
import com.app.session.model.StoryModel;
import com.app.session.model.SubscriptionStories;
import com.app.session.model.UserStory;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ShowStoryActivity extends BaseActivity {
    ReadMoreTextView txtStoryDiscription;
    CustomTextView txtCategory,txt_userName,txtStoryTitle,txtweek,txtViewCount,header;
    CircleImageView imgGroupIcon,img_profilepic;
    ImageView imgRemove,imgStory ,imgVideoThumb,imgPlay;
    // YouTubePlayerView youTubeView;
    CheckBox img_audio_play;
    SeekBar audio_seekBar;
    CardView lay_audio;
    boolean isPlaying;
    public MediaPlayer mPlayer;
    double timeElapsed = 0, finalTime = 0;
    Handler durationHandler = new Handler();
    Runnable updateSeekBarTime;
    Runnable UpdateSongTime;
    private  int oTime = 0, sTime = 0, eTime = 0;
    private Handler hdlr = new Handler();
    CustomTextView txt_progress;
    RelativeLayout laySubscription;
    UserStory userStory;
    RelativeLayout layVideo;
    YouTubePlayerView youTubePlayerView;
    LinearLayout layDocument;
    ImageView imgDoc;
    CustomTextView txtDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_story);
        if(getIntent().getParcelableExtra("DATA")!=null)
        {
            userStory=getIntent().getParcelableExtra("DATA");
        }
        if(!userStory.getUserDetails().getId().equals(userId))
        {
            sendStoryView();
        }
        initView();
    }

    public void initView()
    {

        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        laySubscription=(RelativeLayout)findViewById(R.id.laySubscription);


        audio_seekBar = (SeekBar)findViewById(R.id.audio_seekBar);
        audio_seekBar.setClickable(false);
        lay_audio = (CardView) findViewById(R.id.lay_audio);
        img_audio_play = (CheckBox) findViewById(R.id.img_audio_play);
        imgRemove = (ImageView) findViewById(R.id.imgRemove);
        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
        imgStory = (ImageView) findViewById(R.id.imgStory);
        img_profilepic = (CircleImageView)findViewById(R.id.img_profilepic);
        imgGroupIcon = (CircleImageView)findViewById(R.id.imgGroupIcon);
        txtCategory = (CustomTextView)findViewById(R.id.txtLang);
        txt_userName = (CustomTextView)findViewById(R.id.txt_userName);
        header = (CustomTextView)findViewById(R.id.header);
        txtViewCount = (CustomTextView) findViewById(R.id.txtViewCount);
        txtweek = (CustomTextView) findViewById(R.id.txtweek);
        txtStoryTitle = (CustomTextView) findViewById(R.id.txtStoryTitle);
        txtStoryDiscription = (ReadMoreTextView)findViewById(R.id.txtStoryDiscription);
        txt_progress = (CustomTextView)findViewById(R.id.txt_progress);
        mPlayer = new MediaPlayer();
        isPlaying = false;
        layVideo = (RelativeLayout) findViewById(R.id.layVideo);
        layDocument = (LinearLayout) findViewById(R.id.layDocument);
        txtDoc=(CustomTextView)findViewById(R.id.txtDocName);
        imgDoc=(ImageView)findViewById(R.id.imgDoc);
        imgPlay=(ImageView)findViewById(R.id.imgPlay);
        imgVideoThumb=(ImageView)findViewById(R.id.imgVideoThumb);
        bindHolderToVoice(  10000);
    }


    public void bindHolderToVoice( long duration)
    {
        txtViewCount.setText(userStory.getViews());
//        String days = Utility.getCalculatedDate(userStory.getCreatedAt());
        txtweek.setText(userStory.getDaysAgo()+"days");
        if(userStory.getStoryTitle()!=null&&!userStory.getStoryTitle().isEmpty())
        {
            txtStoryTitle.setText(userStory.getStoryTitle());
        }
        else
        {
            txtStoryTitle.setVisibility(View.GONE);
        }

        if(userStory.getStoryText()!=null&&!userStory.getStoryText().isEmpty())
        {
            try {
                Typeface face = Typeface.createFromAsset(context.getAssets(),
                        "Segoe_UI.ttf");
                txtStoryDiscription .setTypeface(face);
//                    txtStoryDiscription.setTrimLines(5);
                txtStoryDiscription .setTrimCollapsedText(" more");
                txtStoryDiscription .setTrimExpandedText(" less");

                txtStoryDiscription.setText(userStory.getStoryText());
            } catch (StringIndexOutOfBoundsException  e) {
                e.printStackTrace();
            }
        }
        else
        {
            txtStoryDiscription.setVisibility(View.GONE);
        }
if(userStory.getSubscriptionId().getGroupName()==null)
{
    laySubscription.setVisibility(View.GONE);
}
else
{
    laySubscription.setVisibility(View.VISIBLE);
}
        txt_userName.setText(userStory.getSubscriptionId().getGroupName());
        header.setText(userStory.getUserDetails().getUserName());
        String userUrl=userStory.getUserDetails().getImageUrl();
        if(userUrl!=null&&!userUrl.isEmpty()&&!userUrl.equals("null"))
        {
            userUrl=Urls.BASE_IMAGES_URL+userStory.getUserDetails().getImageUrl();
            Picasso.with(context).load(userUrl).placeholder(R.mipmap.profile_image).into(img_profilepic);

        }

        if(userStory.getSubscriptionId().getGroupImageUrl()!=null&&!userStory.getSubscriptionId().getGroupImageUrl().isEmpty()) {
            Picasso.with(context).load(Urls.BASE_IMAGES_URL+userStory.getSubscriptionId().getGroupImageUrl()).placeholder(R.mipmap.profile_image).into(imgGroupIcon);
        }

        if(userStory.getStoryType().equals("image"))
        {
            lay_audio.setVisibility(View.GONE);
            if (userStory.getStoryUrl() != null && !userStory.getStoryUrl().isEmpty())
            {
                imgStory.setVisibility(View.VISIBLE);
                String url = Urls.BASE_IMAGES_URL + userStory.getStoryUrl();
                System.out.println("url : "+url);

                Picasso.with(context).load(url).into(imgStory);
//            Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgStory);
//                    Glide.with(context)
//                            .load(url)
//                            .placeholder(R.mipmap.profile_pic) //placeholder
//                            .into(imgStory);

            }
        }
        if(userStory.getStoryType().equals("audio"))
        {
            String url=userStory.getStoryUrl();
            imgStory.setVisibility(View.GONE);
            lay_audio.setVisibility(View.VISIBLE);

//
            // audio_seekBar.setMax(sTime);
            audio_seekBar.setClickable(false);
            audio_seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mPlayer.isPlaying()) {
                        SeekBar sb = (SeekBar) v;
                        mPlayer.seekTo(sb.getProgress());
                    }
                    return false;
                }
            });
            updateSeekBarTime = new Runnable() {
                public void run() {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            timeElapsed = mPlayer.getCurrentPosition();
                            audio_seekBar.setProgress((int) timeElapsed);
                            txt_progress.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
                            durationHandler.postDelayed(this, 100);
                        } else {
                            mPlayer.pause();
                            isPlaying = false;
                        }
                    }
                }
            };

            UpdateSongTime = new Runnable() {
                @Override
                public void run() {

                    if (mPlayer!=null) {
                        if (mPlayer.isPlaying())
                        {
                            sTime = mPlayer.getCurrentPosition();
                            audio_seekBar.setProgress(sTime);
                            hdlr.postDelayed(this, 100);
                        }
                        else {
                            mPlayer.pause();
                            isPlaying = false;
                        }
                    }
                }
            };

            String path = Urls.BASE_IMAGES_URL+userStory.getStoryUrl();
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(path);
                mPlayer.prepare();
            } catch (IOException e) {
                Log.e("tag", "Start playing prepare() failed");
                isPlaying = false;
            }
            img_audio_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (!isPlaying)
                    {


                        isPlaying = true;

                        mPlayer.start();

                        eTime = mPlayer.getDuration();
                        sTime = mPlayer.getCurrentPosition();
                        if(oTime == 0){
                            audio_seekBar.setMax(eTime);
                            oTime =1;
                        }


                        txt_progress.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                        audio_seekBar.setProgress(sTime);
                        hdlr.postDelayed(UpdateSongTime, 100);

                    } else {
                        isPlaying = false;
                        mPlayer.pause();
                    }
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            hdlr.removeCallbacksAndMessages(null);
                            isPlaying = false;
                            img_audio_play.setChecked(false);
                            audio_seekBar.setProgress(0);
                        }
                    });
                }
            });




        }
        else if (userStory.getStoryType().equals("video_url"))
        {
            lay_audio.setVisibility(View.GONE);
            imgStory.setVisibility(View.GONE);
            layVideo.setVisibility(View.VISIBLE);
            layDocument.setVisibility(View.GONE);
            String url = Utility.getYoutubeThumbnailUrlFromVideoUrl(userStory.getStoryUrl());
            Picasso.with(context).load(url).into(imgVideoThumb, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    imgPlay.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });

        }
        else if (userStory.getStoryType().equals("video"))
        {
            lay_audio.setVisibility(View.GONE);
            imgStory.setVisibility(View.GONE);
            layVideo.setVisibility(View.VISIBLE);
            layDocument.setVisibility(View.GONE);
            if(userStory.getThumbnail_url()!=null&&!userStory.getThumbnail_url().isEmpty())
            {
                String url = Urls.BASE_IMAGES_URL+userStory.getThumbnail_url();
                System.out.println("thumbnail url"+url);
                Picasso.with(context).load(url).into(imgVideoThumb, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imgPlay.setImageResource(R.mipmap.video_play);
                        imgPlay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }


        else   if(userStory.getStoryType().equals("anydoc"))
        {
            lay_audio.setVisibility(View.GONE);
            imgStory.setVisibility(View.GONE);
            layVideo.setVisibility(View.GONE);
            layDocument.setVisibility(View.VISIBLE);
            txtDoc.setText(userStory.getDisplay_doc_name());
            if(userStory.getStoryUrl().contains(".doc"))
            {
                imgDoc.setImageResource(R.mipmap.docs_story);
            }
            else if(userStory.getStoryUrl().contains(".pdf"))
            {
                imgDoc.setImageResource(R.mipmap.pdf_story);
            } else if(userStory.getStoryUrl().contains(".zip"))
            {
                imgDoc.setImageResource(R.mipmap.zip_story);
            }

        }


        layVideo.setTag(userStory);
        layVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UserStory data = (UserStory) view.getTag();

                if(data.getStoryType().equals("video"))
                {
                    //apiCallback.getObject(data, position, view);
                    String videoUrl = Urls.BASE_IMAGES_URL + data.getStoryUrl();
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("URL", videoUrl);
                    startActivityForResult(intent, Constant.PAGE_REFRESH);
                }
                if(data.getStoryType().equals("video_url"))
                {
                    String id = Utility.extractYTId(data.getStoryUrl());
                    Intent intent = new Intent(context, YoutubeActivity.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }
//                    apiCallback.getObject(data, position, view);

            }
        });



        txtStoryTitle.setTag(userStory);
        txtStoryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(mPlayer!=null)
                {
                    if(mPlayer.isPlaying())
                    {
                        mPlayer.stop();
                        //   mPlayer.release();
                    }
                }
                UserStory data =(UserStory)view.getTag();


            }
        });
        txtStoryDiscription.setTag(userStory);
        txtStoryDiscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(mPlayer!=null)
                {
                    if(mPlayer.isPlaying())
                    {
                        mPlayer.stop();
                        //   mPlayer.release();
                    }
                }
                UserStory data =(UserStory)view.getTag();


            }
        });


    }


    private void sendStoryView()
    {
        if(isInternetConnected())
        {
            StoryId storyId =new StoryId();
            storyId.setStory_id(userStory.getId());
            storyId.setUser_id(userId);
            ApiInterface apiInterface= ApiClientExplore.getClient().create(ApiInterface.class);
            Call<Root> call=apiInterface.reqSendViewsCount(storyId);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {
                    if(response.body()!=null)
                    {
                        if(response.body().getStatus()==200) {
                            System.out.println("count " + response.body().getMessage());
                        }

                    }
                    else
                    {
                        try {
                            ResponseBody responseBody=null;


                                responseBody = response.errorBody();

                            String data =responseBody.string();

                            System.out.println("error1"+data);
                            JSONObject jsonObject=new JSONObject(data);
                            System.out.println("error2"+jsonObject.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {

                }
            });
        }
        else
        {

        }
    }


    public void showMenu(View v ) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_delete:

                        callDeleteStory(userStory.getId());

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

        if(userId.equals(userStory.getUserDetails().getId()))
        {
            inflater.inflate(R.menu.menu_subscription_story, popup.getMenu());
        }
        else
        {
            inflater.inflate(R.menu.menu_view_story, popup.getMenu());
        }



        popup.show();
    }

    private void callDeleteStory(String story_id) {
        if (isInternetConnected()) {
            showLoading();

            ReqDeleteStory deleteStory=new ReqDeleteStory();
            deleteStory.setStoryId(story_id);
            ApiInterface apiInterface= ApiClientProfile.getClient().create(ApiInterface.class);
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
                            finish();

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(Constant.PAGE_REFRESH, intent);
        finish();
    }
}
