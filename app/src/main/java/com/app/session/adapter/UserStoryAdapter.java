package com.app.session.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.app.session.Config;
import com.app.session.R;
import com.app.session.activity.MyProfileActivityNew;
import com.app.session.activity.PlayerActivity;
import com.app.session.activity.YoutubeActivity;
import com.app.session.api.Urls;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.model.StoryModel;
import com.app.session.model.UserStory;
import com.app.session.utility.Utility;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class UserStoryAdapter extends RecyclerView.Adapter<UserStoryAdapter.ViewHolder> {
    private static final String TAG = UserStoryAdapter.class.getSimpleName();

    Context context;
    private LinkedList<StoryModel> storyDataArrayList;
    private ObjectCallback apiCallback;
    String userName, userUrl;


    public UserStoryAdapter(Context context, LinkedList<StoryModel> list, String userName, String userUrl, ObjectCallback apiCallback) {
        this.context = context;
        storyDataArrayList = list;
        this.apiCallback = apiCallback;
        this.userName = userName;
        this.userUrl = userUrl;
    }


    @Override
    public UserStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subscriptiongroup_story_layout, parent, false);
//        ViewHolder vh;
//        vh = new ViewHolder(view);
        return ViewHolder.createVoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subscriptiongroup_story_layout, parent, false));

    }


    @Override
    public void onBindViewHolder(UserStoryAdapter.ViewHolder holder, final int position) {
        StoryModel storyData = storyDataArrayList.get(position);

        if (position < storyDataArrayList.size()) {
            holder.bindHolderToVoice(context, userName, userUrl, storyData, position, apiCallback, 10000);
        }


    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void updateData(int position) {
        storyDataArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, storyDataArrayList.size());

    }


    @Override
    public int getItemCount() {

        return storyDataArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ReadMoreTextView txtStoryDiscription;
        CustomTextView txtCategory, txt_userName, txtStoryTitle, txtweek, txtViewCount;
        CircleImageView imgProfile;
        ImageView imgRemove, imgStory, imgVideoThumb,imgPlay,imgShare;
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
        LinearLayout layTop;
        RelativeLayout layVideo,layImage;
        YouTubePlayerView youTubePlayerView;
        LinearLayout layDocument;
        ImageView imgDoc;
        CustomTextView txtDoc;
        ProgressView rey_loading,rey_loading_video;
        //   private YouTubePlayer youTubePlayer;
        public ViewHolder(View view) {
            super(view);

        }


        public static ViewHolder createVoiceViewHolder(View view) {
            ViewHolder holder = new ViewHolder(view);
            holder.rey_loading=(ProgressView)view.findViewById(R.id.rey_loading);
            holder.rey_loading_video=(ProgressView)view.findViewById(R.id.rey_loading_video);
            holder.layVideo = (RelativeLayout) view.findViewById(R.id.layVideo);
            holder.layImage = (RelativeLayout) view.findViewById(R.id.layImage);
            holder.layDocument = (LinearLayout) view.findViewById(R.id.layDocument);
            holder.layTop = (LinearLayout) view.findViewById(R.id.layTops);
            holder.audio_seekBar = (SeekBar) view.findViewById(R.id.audio_seekBar);
            holder.audio_seekBar.setClickable(false);
            holder.lay_audio = (CardView) view.findViewById(R.id.lay_audio);
            holder.img_audio_play = (CheckBox) view.findViewById(R.id.img_audio_play);
            holder.imgVideoThumb = (ImageView) view.findViewById(R.id.imgVideoThumb);
            holder.imgDoc = (ImageView) view.findViewById(R.id.imgDoc);
            holder.imgPlay = (ImageView) view.findViewById(R.id.imgPlay);
            holder.imgShare = (ImageView) view.findViewById(R.id.imgShare);
            holder.imgRemove = (ImageView) view.findViewById(R.id.imgRemove);
            holder.imgStory = (ImageView) view.findViewById(R.id.imgStory);
            holder.imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
            holder.txtDoc = (CustomTextView) view.findViewById(R.id.txtDocName);
            holder.txtViewCount = (CustomTextView) view.findViewById(R.id.txtViewCount);
            holder.txtweek = (CustomTextView) view.findViewById(R.id.txtweek);
            holder.txtCategory = (CustomTextView) view.findViewById(R.id.txtLang);
            holder.txt_userName = (CustomTextView) view.findViewById(R.id.txt_userName);
            holder.txtStoryTitle = (CustomTextView) view.findViewById(R.id.txtStoryTitle);
            holder.txtStoryDiscription = (ReadMoreTextView) view.findViewById(R.id.txtStoryDiscription);
            holder.txt_progress = (CustomTextView) view.findViewById(R.id.txt_progress);
            if(holder.mPlayer!=null)
            {
                holder.mPlayer.release();
                holder.mPlayer=null;
                holder.mPlayer = new MediaPlayer();
            }
            else
            {
                holder.mPlayer = new MediaPlayer();
            }
            holder.isPlaying = false;


            return holder;
        }


        public void bindHolderToVoice(Context context, String userName, String userUrl, StoryModel userStory, int position, ObjectCallback apiCallback, long duration) {
//            String days = Utility.getCalculatedDate(userStory.getCreatedAt());

            txtweek.setText(userStory.getDaysAgo() + "days");

            txtViewCount.setText("" + userStory.getViews());
            txtStoryTitle.setVisibility(View.VISIBLE);
            if (userStory.getStoryTitle() != null && !userStory.getStoryTitle().isEmpty())
            {
                txtStoryTitle.setText(userStory.getStoryTitle());
            } else {
                txtStoryTitle.setVisibility(View.GONE);
            }

            txtStoryDiscription.setVisibility(View.VISIBLE);
            if (userStory.getStoryText() != null&&!userStory.getStoryText().equals("null") && !userStory.getStoryText().isEmpty()) {
                try {
                    Typeface face = Typeface.createFromAsset(context.getAssets(),
                            "Segoe_UI.ttf");
                    txtStoryDiscription.setTypeface(face);
//                    txtStoryDiscription.setTrimLines(5);
                    txtStoryDiscription.setTrimCollapsedText(" more");
                    txtStoryDiscription.setTrimExpandedText(" less");
                    txtStoryDiscription.setText(userStory.getStoryText());

                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                txtStoryDiscription.setVisibility(View.GONE);
            }

            txt_userName.setText(userStory.getUserDetails().getUserName());

            if (userStory.getUserDetails().getImageUrl() != null && !userStory.getUserDetails().getImageUrl().isEmpty() && !userStory.getUserDetails().getImageUrl().equals("null")) {

                Picasso.with(context).load(Urls.BASE_IMAGES_URL + userStory.getUserDetails().getImageUrl()).placeholder(R.mipmap.profile_image).into(imgProfile);
            }
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    apiCallback.getObject(userStory, -1, view);
                }
            });

            if (userStory.getStoryType().equals("image"))
            {
                lay_audio.setVisibility(View.GONE);
                layVideo.setVisibility(View.GONE);
                layDocument.setVisibility(View.GONE);
                imgStory.setVisibility(View.VISIBLE);
                layImage.setVisibility(View.VISIBLE);
                if (userStory.getStoryUrl() != null && !userStory.getStoryUrl().isEmpty())
                {


                    String url = Urls.BASE_IMAGES_URL + userStory.getStoryUrl();
                    System.out.println("url : " + url);
                    rey_loading.start();

                    Glide.with(context)
                            .load(url)
                            .placeholder(R.drawable.black_ripple_btn_bg_squre)
                            .error(R.drawable.black_ripple_btn_bg_squre)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgStory);

//                    Picasso.with(context).load(url).placeholder(R.drawable.black_ripple_btn_bg_squre).error(R.drawable.black_ripple_btn_bg_squre).into(imgStory, new Callback() {
//                        @Override
//                        public void onSuccess() {
////                            rey_loading.stop();
//
//                        }
//
//                        @Override
//                        public void onError() {
//                            imgStory.setImageResource(R.drawable.black_ripple_btn_bg_squre);
//                        }
//                    });


                }
                else
                {
                    imgStory.setImageResource(R.drawable.black_ripple_btn_bg_squre);
                }
            }

          else if (userStory.getStoryType().equals("video_url"))
          {
                lay_audio.setVisibility(View.GONE);
              layImage.setVisibility(View.GONE);
                layVideo.setVisibility(View.VISIBLE);
                layDocument.setVisibility(View.GONE);
                layDocument.setVisibility(View.GONE);
                String url = Utility.getYoutubeThumbnailUrlFromVideoUrl(userStory.getStoryUrl());

                Picasso.with(context).load(url).into(imgVideoThumb, new Callback() {
                    @Override
                    public void onSuccess() {
                        imgPlay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
//              Glide.with(context)
//                      .load(url)
//                      .listener(new RequestListener<Drawable>() {
//                          @Override
//                          public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                              imgPlay.setVisibility(View.GONE);
//                              return false;
//                          }
//
//                          @Override
//                          public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                              imgPlay.setVisibility(View.VISIBLE);
//                              return false;
//                          }
//                      })
//                      .into(imgVideoThumb);


                layVideo.setTag(userStory);
                layVideo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        StoryModel data = (StoryModel) view.getTag();
                        apiCallback.getObject(data, position, view);

                    }
                });
            }
          else if (userStory.getStoryType().equals("video"))
          {
                lay_audio.setVisibility(View.GONE);
              layImage.setVisibility(View.GONE);
                layVideo.setVisibility(View.VISIBLE);
                layDocument.setVisibility(View.GONE);
                if(userStory.getThumbnail_url()!=null&&!userStory.getThumbnail_url().isEmpty()) {
                    String url = Urls.BASE_IMAGES_URL+userStory.getThumbnail_url();
                    System.out.println("thumbnail url"+url);
                    rey_loading_video.start();
                    Picasso.with(context).load(url).into(imgVideoThumb, new Callback() {
                        @Override
                        public void onSuccess()
                        {
                            imgPlay.setImageResource(R.mipmap.video_play);
                            imgPlay.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

//                    Glide.with(context)
//                            .load(url)
//                            .listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    imgPlay.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    imgPlay.setImageResource(R.mipmap.video_play);
//                                    imgPlay.setVisibility(View.VISIBLE);
//                                    return false;
//                                }
//                            })
//                            .into(imgVideoThumb);


                }



                layVideo.setTag(userStory);
                layVideo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        StoryModel data = (StoryModel) view.getTag();
                        apiCallback.getObject(data, position, view);

                    }
                });
            }

          else   if(userStory.getStoryType().equals("anydoc"))
          {
                lay_audio.setVisibility(View.GONE);
              layImage.setVisibility(View.GONE);
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
                else if(userStory.getStoryUrl().contains(".xls"))
                {
                    imgDoc.setImageResource(R.mipmap.xls_story);
                }
              layDocument.setTag(userStory);
              layDocument.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      StoryModel data = (StoryModel) view.getTag();
                      apiCallback.getObject(data, position, view);
                  }
              });


            }
            else  if (userStory.getStoryType().equals("audio"))
            {
                layVideo.setVisibility(View.GONE);
                layImage.setVisibility(View.GONE);
                lay_audio.setVisibility(View.VISIBLE);
                layDocument.setVisibility(View.GONE);
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

                        if (mPlayer != null) {
                            if (mPlayer.isPlaying()) {
                                sTime = mPlayer.getCurrentPosition();
                                audio_seekBar.setProgress(sTime);
                                hdlr.postDelayed(this, 100);
                            } else {
                                mPlayer.pause();
                                isPlaying = false;
                            }
                        }
                    }
                };

                String path = Urls.BASE_IMAGES_URL + userStory.getStoryUrl();
                System.out.println("audio path "+path);
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
                    public void onClick(View view) {
                        if (!isPlaying) {

                            isPlaying = true;
                            mPlayer.start();
                            eTime = mPlayer.getDuration();
                            sTime = mPlayer.getCurrentPosition();
                            if (oTime == 0) {
                                audio_seekBar.setMax(eTime);
                                oTime = 1;
                            }

                            txt_progress.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
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
            imgRemove.setTag(userStory);
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StoryModel data = (StoryModel) view.getTag();
                    apiCallback.getObject(data, position, view);
                }
            });

            layTop.setTag(userStory);
            layTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.stop();
                            mPlayer.release();
                        }
                    }
                    StoryModel data = (StoryModel) view.getTag();
                    apiCallback.getObject(data, position, view);

                }
            });
            txtStoryTitle.setTag(userStory);
            txtStoryTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.stop();
                               mPlayer.release();
                        }
                    }
                    StoryModel data = (StoryModel) view.getTag();
                    apiCallback.getObject(data, position, view);

                }
            });
            txtStoryDiscription.setTag(userStory);
            txtStoryDiscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.stop();
                               mPlayer.release();
                        }
                    }
                    StoryModel data = (StoryModel) view.getTag();
                    apiCallback.getObject(data, position, view);

                }
            });

if(userStory.getSubscriptionIds()!=null)
{
    if (userStory.getSubscriptionIds().size() > 0) {
        imgShare.setVisibility(View.GONE);
    } else {
        imgShare.setVisibility(View.VISIBLE);
    }
}
else
{
    imgShare.setVisibility(View.VISIBLE);
}
            imgShare.setTag(userStory);
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    StoryModel data = (StoryModel) view.getTag();
                    apiCallback.getObject(data, position, view);
                }
            });

        }


    }



    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        inUrl = inUrl.replace("&feature=youtu.be", "");
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public void mpl(ViewHolder holder)
    {
        if(holder.mPlayer!=null) {
            holder.mPlayer.release();
            holder.mPlayer=null;
        }
    }

}
