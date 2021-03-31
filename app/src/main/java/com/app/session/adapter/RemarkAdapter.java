package com.app.session.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.app.session.R;
import com.app.session.activity.AddRemarkActivity;
import com.app.session.activity.RemarkActivity;
import com.app.session.api.AqueryCall;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.RemarkModel;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class RemarkAdapter extends RecyclerView.Adapter<RemarkAdapter.ViewHolder>  {

    Context context;
    private ArrayList<RemarkModel> remarkList ;

    RemarkActivity baseActivity;

    public RemarkAdapter(Context context, ArrayList<RemarkModel> list) {
        this.context = context;
        baseActivity = (RemarkActivity) context;
        remarkList = list;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        return ViewHolder.createVoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_remark, parent, false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int pos = position;
        final RemarkModel remarkModel = remarkList.get(position);
        holder.bindHolderToVoice(10000, remarkModel);
    }


    @Override
    public int getItemCount() {

        return remarkList.size();
    }
















    public static class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtRemark, txtRemark_date, txtRemark_header, txt_progress;
        CardView cardView;
        CheckBox audio_play;
        ImageView imgRemove;
        LinearLayout lay_audio;
        SeekBar audio_seekBar;
        double timeElapsed = 0, finalTime = 0;
        Handler durationHandler = new Handler();
        Runnable updateSeekBarTime;
        MediaPlayer mPlayer;boolean isPlaying;
        String alarm_time = "";
        public ViewHolder(final View view) {
            super(view);

        }


        public static ViewHolder createVoiceViewHolder(View convertView)
        {
            ViewHolder holder = new ViewHolder(convertView);
            holder.txtRemark = (CustomTextView) convertView.findViewById(R.id.txtRemark);
            holder.txt_progress = (CustomTextView) convertView.findViewById(R.id.txt_progress);
            holder.txtRemark_date = (CustomTextView) convertView.findViewById(R.id.txtRemark_date);
            holder.txtRemark_header = (CustomTextView) convertView.findViewById(R.id.txtRemark_header);
            holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
            holder.audio_play = (CheckBox) convertView.findViewById(R.id.img_audio_play);
            holder.imgRemove = (ImageView) convertView.findViewById(R.id.imgRemove);
            holder.lay_audio = (LinearLayout) convertView.findViewById(R.id.lay_audio);
            holder.audio_seekBar = (SeekBar) convertView.findViewById(R.id.audio_seekBar);
            holder.mPlayer = new MediaPlayer();
            holder.isPlaying = false;
            return holder;
        }

        public void bindHolderToVoice(final long duration, RemarkModel remarkModel)
        {
            final Context context = itemView.getContext();

            if (duration >= 60000)
            {
                txt_progress.setText(String.format("%d %s, %d %s", TimeUnit.MILLISECONDS.toMinutes(duration), context.getString(R.string.min),
                        TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)),
                        context.getString(R.string.sec)));
            }
            else
            {
                txt_progress.setText(String.format("%d %s",
                        TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)),
                        context.getString(R.string.sec)));
            }
            finalTime = duration;

            audio_seekBar.setMax((int) finalTime);
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
                    if (mPlayer != null)
                    {
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
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                }
            });
            String path = remarkModel.getRemark_text();
            audio_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isPlaying) {
                        isPlaying = true;
                        try {
                            mPlayer = new MediaPlayer();
                            mPlayer.setDataSource(path);
                            mPlayer.prepare();
                        } catch (IOException e) {
                            Log.e("tag", "Start playing prepare() failed");
                            isPlaying = false;
                        }
                        mPlayer.start();
                        timeElapsed = mPlayer.getCurrentPosition();
                        audio_seekBar.setProgress((int) timeElapsed);
                        durationHandler.postDelayed(updateSeekBarTime, 100);
                    } else {
                        isPlaying = false;
                        mPlayer.pause();
                    }

                }
            });

            if (remarkModel.getRemark_type().equals("audio")) {
                txtRemark.setVisibility(View.GONE);
                lay_audio.setVisibility(View.VISIBLE);
            } else {
                txtRemark.setVisibility(View.VISIBLE);
                lay_audio.setVisibility(View.GONE);
            }
            txtRemark.setText(remarkModel.getRemark_text());
//        holder.audio_seekBar.setProgress(0);

            txtRemark_header.setText(remarkModel.getClient_name());
            String[] datetime = remarkModel.getThe_date().split("T");
//        String date = remarkModel.getThe_date().replace("T"," ");
            String date = change_DateFormate(datetime[0]);
            String time = setTimeFormate(datetime[1]);
            txtRemark_date.setText(date + " " + time);

            cardView.setTag(remarkModel);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardView cardView = (CardView) v;
                    RemarkModel model = (RemarkModel) cardView.getTag();
                    if (!model.getRemark_type().equals("audio")) {
                        Intent intent = new Intent(context, AddRemarkActivity.class);
                        intent.putExtra("ID", model.getClient_cd());
                        intent.putExtra("NAME", model.getClient_name());
                        intent.putExtra("DATA", model.getRemark_text());
                        intent.putExtra("REMARKS_ID", model.getRemarks_id());
                        context.startActivity(intent);
                    }
                }
            });
            imgRemove.setTag(remarkModel);
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    RemarkModel model = (RemarkModel) imageView.getTag();
                    if (mPlayer == null) {
                      //  callRemoveLang(getParamRemoveLang(model.getRemarks_id()), Urls.DELETE_REMARK, model, position);
                    }

                }
            });

        }

    }


    public static String setTimeFormate(String time) {
        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
        DateFormat df = new SimpleDateFormat("hh:mm aa");
        Date date = null;
        String output = null;
       String alarm_time="";
        try {
            //Converting the input String to Date
            date = outputformat.parse(time);
            //Changing the format of date and storing it in String
            alarm_time = df.format(date);
            //Displaying the date

            System.out.println(output);
        } catch (ParseException pe) {
            alarm_time = time;
            pe.printStackTrace();

        }


        return alarm_time;
    }

    public static String change_DateFormate(String date_formate) {
        String change_date = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        DateFormat formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);//this show monday day also
        DateFormat formatter2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        try {
            Date date = formatter.parse(date_formate);
            change_date = formatter2.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return change_date;

    }


    /*..............................................................................*/

    private void  callRemoveLang(Map<String, Object> param, String url, final RemarkModel lang, final int position) {

        try {
            baseActivity.showLoading();
            AqueryCall request = new AqueryCall(baseActivity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    baseActivity.dismiss_loading();
                    remarkList.remove(lang);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, remarkList.size());


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    baseActivity.dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        Utility.unAuthorized(context, baseActivity);


                    } else {

                    }
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    baseActivity.dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    baseActivity.dismiss_loading();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }


    private Map<String, Object> getParamRemoveLang(String remarks_id) {
        Map<String, Object> params = new HashMap<>();

        String accessToken = DataPrefrence.getPref(context, Constant.ACCESS_TOKEN, "");
        String userId = DataPrefrence.getPref(context, Constant.USER_ID, "");
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("remarks_id", remarks_id);


        return params;
    }

    /*..............................................................................*/

    AlphaAnimation blinkanimation;

    public void animitn() {
        blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(300); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(3); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
    }


}
