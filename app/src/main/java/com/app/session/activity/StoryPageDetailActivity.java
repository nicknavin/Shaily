package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.app.session.BuildConfig;
import com.app.session.R;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.activity.viewmodel.StoryPageDetailViewModel;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.RequestReadCount;
import com.app.session.data.model.RequestReadCountNormalStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.StoryData;
import com.app.session.data.model.StoryId;
import com.app.session.data.model.StoryModel;
import com.app.session.data.model.UserStory;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class StoryPageDetailActivity extends BaseActivity {
    UserStory storyData;
StoryPageDetailViewModel viewModel;
    CustomTextView txtCategory,  txtStoryTitle,txtStoryDiscription,header;
    CircleImageView imgProfile;
    ImageView  imgStory,imgVideoThumb,imgPlay;
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
    private int oTime = 0, sTime = 0, eTime = 0;
    private Handler hdlr = new Handler();
    CustomTextView txt_progress;
    LinearLayout layDocument;
    RelativeLayout layVideo;
    CustomTextView txtDoc,txtweek,txtViewCount;
    ImageView imgDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_page_detail);
        viewModel=new ViewModelProvider(this,new ViewModelFactory(userId,accessToken)).get(StoryPageDetailViewModel.class);
        if (getIntent().getParcelableExtra("DATA") != null)
        {
            storyData = getIntent().getParcelableExtra("DATA");
        }
        initView();
       // sendStoryView();
        if(!storyData.getUserDetails().getId().equals(userId)) {
            apiViewCount();
        }
        setupObserver();
    }

    private void initView() {



        layDocument = (LinearLayout) findViewById(R.id.layDocument);
        imgDoc = (ImageView) findViewById(R.id.imgDoc);
        txtViewCount = (CustomTextView) findViewById(R.id.txtViewCount);
        txtweek = (CustomTextView) findViewById(R.id.txtweek);
        txtDoc = (CustomTextView) findViewById(R.id.txtDocName);
        layVideo=(RelativeLayout)findViewById(R.id.layVideo);
        imgVideoThumb = (ImageView)findViewById(R.id.imgVideoThumb);
        imgPlay = (ImageView)findViewById(R.id.imgPlay);
        audio_seekBar = (SeekBar) findViewById(R.id.audio_seekBar);
        audio_seekBar.setClickable(false);
        lay_audio = (CardView) findViewById(R.id.lay_audio);
        img_audio_play = (CheckBox) findViewById(R.id.img_audio_play);

        imgStory = (ImageView) findViewById(R.id.imgStory);

        imgProfile = (CircleImageView) findViewById(R.id.img_profilepic);
        txtCategory = (CustomTextView) findViewById(R.id.txtLang);

        header = (CustomTextView) findViewById(R.id.header);
        txtStoryTitle = (CustomTextView) findViewById(R.id.txtStoryTitle);
        txtStoryDiscription = (CustomTextView) findViewById(R.id.txtStoryDiscription);
        txt_progress = (CustomTextView) findViewById(R.id.txt_progress);
        mPlayer = new MediaPlayer();
        isPlaying = false;



     String   profileUrl=Urls.BASE_IMAGES_URL+storyData.getUserDetails().getImageUrl();
      String  user_name=storyData.getUserDetails().getUserName();
        bindHolderToVoice(context, user_name, profileUrl, storyData , 10000);
        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void bindHolderToVoice(Context context, String userName, String userUrl, UserStory storyData, long duration)
    {

        header.setText(userName);
        txtweek.setText(storyData.getDaysAgo() + "days");
        txtViewCount.setText("" + storyData.getStoryRead().getCount());
        if(storyData.getStoryTitle()!=null&&!storyData.getStoryTitle().isEmpty())
        {
            txtStoryTitle.setText(storyData.getStoryTitle());
        }
        else
        {
            txtStoryTitle.setVisibility(View.GONE);
        }

        if(storyData.getStoryText()!=null&&!storyData.getStoryText().isEmpty())
        {
            try {
                Typeface face = Typeface.createFromAsset(context.getAssets(),
                        "Segoe_UI.ttf");
                txtStoryDiscription .setTypeface(face);
//                    txtStoryDiscription.setTrimLines(5);

                txtStoryDiscription.setText(storyData.getStoryText());
            } catch (StringIndexOutOfBoundsException  e) {
                e.printStackTrace();
            }
        }
        else
        {
            txtStoryDiscription.setVisibility(View.GONE);
        }


        if(userUrl!=null&&!userUrl.isEmpty()&&!userUrl.equals("null"))
        {
            Picasso.with(context).load(userUrl).placeholder(R.mipmap.profile_image).into(imgProfile);
        }
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(storyData.getStoryType().equals("image"))
        {
            lay_audio.setVisibility(View.GONE);
            if (storyData.getStoryUrl() != null && !storyData.getStoryUrl().isEmpty()) {
                imgStory.setVisibility(View.VISIBLE);
                String url = Urls.BASE_IMAGES_URL + storyData.getStoryUrl();
                System.out.println("url : "+url);
                Picasso.with(context).load(url).placeholder(R.drawable.black_ripple_btn_bg_squre).error(R.drawable.black_ripple_btn_bg_squre).into(imgStory);
            }
            else
            {
                imgStory.setImageResource(R.drawable.black_ripple_btn_bg_squre);
            }
        }
        if(storyData.getStoryType().equals("audio"))
        {
            String url=storyData.getStoryUrl();
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

            String path = Urls.BASE_IMAGES_URL+storyData.getStoryUrl();
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
        if (storyData.getStoryType().equals("video_url"))
        {
            layDocument.setVisibility(View.VISIBLE);
            lay_audio.setVisibility(View.GONE);
            imgStory.setVisibility(View.GONE);
            layVideo.setVisibility(View.VISIBLE);
            String url = Utility.getYoutubeThumbnailUrlFromVideoUrl(storyData.getStoryUrl());
            Picasso.with(context).load(url).into(imgVideoThumb, new Callback() {
                @Override
                public void onSuccess() {
                    imgPlay.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });



        }
        else if (storyData.getStoryType().equals("video"))
        {
            lay_audio.setVisibility(View.GONE);
            imgStory.setVisibility(View.GONE);
            layVideo.setVisibility(View.VISIBLE);
            layDocument.setVisibility(View.GONE);
            if(storyData.getThumbnail_url()!=null&&!storyData.getThumbnail_url().isEmpty())
            {
                String url = Urls.BASE_IMAGES_URL+storyData.getThumbnail_url();
                System.out.println("thumbnail url"+url);
                Picasso.with(context).load(url).into(imgVideoThumb, new Callback() {
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
        else if(storyData.getStoryType().equals("anydoc"))
        {
            lay_audio.setVisibility(View.GONE);
            imgStory.setVisibility(View.GONE);
            layVideo.setVisibility(View.GONE);
            layDocument.setVisibility(View.VISIBLE);
            txtDoc.setText(storyData.getDisplay_doc_name());
            if(storyData.getStoryUrl().contains(".doc"))
            {
                imgDoc.setImageResource(R.mipmap.docs_story);
            }
            else if(storyData.getStoryUrl().contains(".pdf"))
            {
                imgDoc.setImageResource(R.mipmap.pdf_story);
            } else if(storyData.getStoryUrl().contains(".zip"))
            {
                imgDoc.setImageResource(R.mipmap.zip_story);
            }
            layDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String url = Urls.BASE_IMAGES_URL + storyData.getStoryUrl();
                    new DownloadFile("",storyData.getDisplay_doc_name(),"show").execute(url);

                }
            });

        }



        layVideo.setTag(storyData);
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
                    Intent intent = new Intent(context, PlayerActivity.class);
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


            }
        });




    }


    private void sendStoryView()
    {
        if(isInternetConnected())
        {
            StoryId storyId =new StoryId();
            storyId.setStory_id(storyData.getId());
            storyId.setUser_id(userId);
            storyId.setStory_provider(storyData.getStory_provider());
            log("req data "+storyId.toString());
            ApiInterface apiInterface= ApiClientExplore.getClient().create(ApiInterface.class);
            Call<Root> call=apiInterface.reqSendViewsCount(storyId);
            call.enqueue(new retrofit2.Callback<Root>() {
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


    private void apiViewCount()
    {

        if(storyData.getStory_provider().equals("2"))
        {
            RequestReadCount requestReadCount=new RequestReadCount() ;
            requestReadCount.setStory_id(storyData.getId());
            requestReadCount.setSubscription_id(storyData.getSubscriptionId().get_id());
            requestReadCount.setUser_id(userId);
            requestReadCount.setStory_provider(storyData.getStory_provider());
            viewModel.setRequestReadCount(requestReadCount);
            viewModel.sendReadViewCount();
        }
        else
        {

//            RequestReadCount requestReadCount=new RequestReadCount() ;
//            requestReadCount.setStory_id(storyData.getId());
//            requestReadCount.setSubscription_id("");
//            requestReadCount.setUser_id(userId);
//            requestReadCount.setStory_provider(storyData.getStory_provider());
//            viewModel.setRequestReadCount(requestReadCount);
//            viewModel.sendReadViewCount();

            RequestReadCountNormalStory requestReadCountNormalStory=new RequestReadCountNormalStory();
            requestReadCountNormalStory.setStory_id(storyData.getId());
            requestReadCountNormalStory.setStory_provider(storyData.getStory_provider());
            requestReadCountNormalStory.setUser_id(userId);
            viewModel.setRequestReadCountNormalStory(requestReadCountNormalStory);
            viewModel.sendReadViewCountNormalStory();
        }

    }
    private void setupObserver()
    {
        viewModel.getResponseBodyMutableLiveData().observe(this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody)
            {
               log(responseBody.toString());
                try {
                    log(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPlayer!=null) {
            if (mPlayer.isPlaying())
            {
                mPlayer.stop();

            }
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

            if (file != null)
            {
            //    if(type.equals("show"))
                {
                    String sharePath = file.getPath();
                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    Utility.displayDocument(context,uri);

                }

            }

        }

    }
}
