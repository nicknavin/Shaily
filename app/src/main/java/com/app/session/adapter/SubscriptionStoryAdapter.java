package com.app.session.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.data.model.StoryData;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SubscriptionStoryAdapter extends RecyclerView.Adapter<SubscriptionStoryAdapter.ViewHolder> {


    Context context;
    private ArrayList<StoryData> storyDataArrayList;
private ObjectCallback apiCallback;
String userName,userUrl;

    public SubscriptionStoryAdapter(Context context, ArrayList<StoryData> list,String userName ,String userUrl , ObjectCallback apiCallback) {
        this.context = context;
        storyDataArrayList = list;
        this.apiCallback=apiCallback;
        this.userName=userName;
        this.userUrl=userUrl;
    }


    @Override
    public SubscriptionStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subscriptiongroup_story_layout, parent, false);
//        ViewHolder vh;
//        vh = new ViewHolder(view);
        return ViewHolder.createVoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subscriptiongroup_story_layout, parent, false));

    }


    @Override
    public void onBindViewHolder(SubscriptionStoryAdapter.ViewHolder holder, final int position)
    {
        StoryData storyData = storyDataArrayList.get(position);

        if(position<storyDataArrayList.size())
        {
            holder.bindHolderToVoice(context, userName, userUrl, storyData, position, apiCallback, 10000);
        }



//holder.youTubeView.initialize(Config.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
////        String youtube="https://youtu.be/GtJizVWPYBA";
////            String id =extractYTId(youtube);
////            youTubePlayer.loadVideo(id);
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//    }
//});
    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void updateData(int position)
    {
        storyDataArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,storyDataArrayList.size());

    }



    @Override
    public int getItemCount() {

        return storyDataArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        ReadMoreTextView txtStoryDiscription;
        CustomTextView txtCategory,txt_userName,txtStoryTitle;
        CircleImageView imgProfile;
        ImageView imgRemove,imgStory;
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
        LinearLayout layTop;

        public ViewHolder(View view) {
            super(view);

            }
        public static ViewHolder createVoiceViewHolder(View view)
        {
            ViewHolder holder = new ViewHolder(view);
            holder.layTop=(LinearLayout)view.findViewById(R.id.layTops);
            holder.audio_seekBar = (SeekBar) view.findViewById(R.id.audio_seekBar);
            holder.audio_seekBar.setClickable(false);
            holder.lay_audio = (CardView) view.findViewById(R.id.lay_audio);
            holder.img_audio_play = (CheckBox) view.findViewById(R.id.img_audio_play);
            holder.imgRemove = (ImageView) view.findViewById(R.id.imgRemove);
            holder.imgStory = (ImageView) view.findViewById(R.id.imgStory);
            holder.imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
            holder.txtCategory = (CustomTextView) view.findViewById(R.id.txtLang);
            holder.txt_userName = (CustomTextView) view.findViewById(R.id.txt_userName);
            holder.txtStoryTitle = (CustomTextView) view.findViewById(R.id.txtStoryTitle);
            holder.txtStoryDiscription = (ReadMoreTextView) view.findViewById(R.id.txtStoryDiscription);
            holder.txt_progress = (CustomTextView) view.findViewById(R.id.txt_progress);
            holder.mPlayer = new MediaPlayer();
            holder.isPlaying = false;
            return holder;
        }



        public void bindHolderToVoice(Context context,String userName,String userUrl,StoryData storyData,int position,ObjectCallback apiCallback,long duration)
        {

            if(storyData.getStory_title()!=null&&!storyData.getStory_title().isEmpty())
            {
                txtStoryTitle.setText(storyData.getStoryTitle());
            }
            else
            {
                txtStoryTitle.setVisibility(View.GONE);
            }

            if(storyData.getStory_text()!=null&&!storyData.getStory_text().isEmpty())
            {
                try {
                    Typeface face = Typeface.createFromAsset(context.getAssets(),
                            "Segoe_UI.ttf");
                    txtStoryDiscription .setTypeface(face);
//                    txtStoryDiscription.setTrimLines(5);
                    txtStoryDiscription .setTrimCollapsedText(" more");
                    txtStoryDiscription .setTrimExpandedText(" less");

                    txtStoryDiscription.setText(storyData.getStoryText());
                } catch (StringIndexOutOfBoundsException  e) {
                    e.printStackTrace();
                }
            }
            else
            {
                txtStoryDiscription.setVisibility(View.GONE);
            }

            txt_userName.setText(userName);
            if(userUrl!=null&&!userUrl.isEmpty()&&!userUrl.equals("null"))
            {

                Picasso.with(context).load(userUrl).placeholder(R.mipmap.profile_image).into(imgProfile);
            }
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    apiCallback.getObject(storyData,-1,view);
                }
            });

            if(storyData.getStory_type().equals("image"))
            {
                lay_audio.setVisibility(View.GONE);
                if (storyData.getStoryUrl() != null && !storyData.getStoryUrl().isEmpty()) {
                    imgStory.setVisibility(View.VISIBLE);
                    String url = Urls.BASE_IMAGES_URL + storyData.getStoryUrl();
                    System.out.println("url : "+url);
//                    Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imgStory);
                    Picasso.with(context).load(url).into(imgStory);
//            Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.imgStory);
//                    Glide.with(context)
//                            .load(url)
//                            .placeholder(R.mipmap.profile_pic) //placeholder
//                            .into(imgStory);

                }
            }
            if(storyData.getStory_type().equals("audio"))
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


            imgRemove.setTag(storyData);
            imgRemove.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    StoryData data=(StoryData)view.getTag();
                    apiCallback.getObject(data,position,view);
                }
            });

            layTop.setTag(storyData);
            layTop.setOnClickListener(new View.OnClickListener() {
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
                    StoryData data =(StoryData)view.getTag();
                    apiCallback.getObject(data,position,view);

                }
            });
            txtStoryTitle.setTag(storyData);
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
                    StoryData data =(StoryData)view.getTag();
                    apiCallback.getObject(data,position,view);

                }
            });
            txtStoryDiscription.setTag(storyData);
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
                    StoryData data =(StoryData)view.getTag();
                    apiCallback.getObject(data,position,view);

                }
            });


            }


        }






}
