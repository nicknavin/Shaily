package com.app.session.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.app.session.R;
import com.app.session.activity.ChattingActivity;
import com.app.session.audioplayer.AudioStatus;
import com.app.session.audioplayer.MediaPlayerUtils;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ChatCallback;
import com.app.session.room.ChatMessage;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.rey.material.widget.ProgressView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;

    Context context;
    String senderID = "";
    LinkedList<ChatMessage> chatMessageArrayList;
    ChattingActivity mainActivity;
    ChatCallback chatCallback;
    public ChatAdapter(Context ctxt, LinkedList<ChatMessage> list, String senderID,ChatCallback callback) {
        this.context = ctxt;
        this.senderID = senderID;
        chatMessageArrayList = list;
        this.mainActivity=(ChattingActivity)ctxt;
        this.chatCallback=callback;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FRIEND_MESSAGE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
       ChatMessage chatMessage= chatMessageArrayList.get(position);

        if (holder instanceof ItemMessageFriendHolder) {
           ItemMessageFriendHolder friendHolder= (ItemMessageFriendHolder) holder;

            Glide.with(context)
                    .load(chatMessage.getReciverProfileUrl())
                    .placeholder(R.mipmap.profile_icon)
                    .error(R.mipmap.profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(friendHolder.imgFriend);

            friendHolder.txtContent.setText(chatMessage.getMessage());
            if (chatMessage.getCreatedAt() != null &&
                    !chatMessage.getCreatedAt().isEmpty()) {
                if (chatMessage.getCreatedAt().contains("T"))
                {
                    friendHolder.textMSgTime.setText(Utility.getTimeSlot(chatMessage.getCreatedAt()));
                }
            }

            if (chatMessage.getMessageType().equals("image"))
            {
                friendHolder.cardViewDoc.setVisibility(View.GONE);
                friendHolder.cardViewAudio.setVisibility(View.GONE);
                friendHolder.layImage.setVisibility(View.VISIBLE);
                friendHolder.txtContent.setVisibility(View.GONE);
                Glide.with(context)
                        .load(chatMessage.getFile())
                        .transform(new RoundedCorners(16))
                        .placeholder(R.mipmap.place_holder)
                        .error(R.mipmap.place_holder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(friendHolder.imgChat);
            }
            else if (chatMessage.getMessageType().equals("audio"))
            {
                friendHolder.cardViewDoc.setVisibility(View.GONE);
                friendHolder.cardViewAudio.setVisibility(View.VISIBLE);
                friendHolder.layImage.setVisibility(View.GONE);
                friendHolder.txtContent.setVisibility(View.GONE);
                friendHolder.txt_audio_time.setText(chatMessage.getDurationTime());
                friendHolder.txtAudioFileName.setText(chatMessage.getDisplayFileName());
//                if(mainActivity.audioStatusList.size()<chatMessageArrayList.size())
                {
                    if (mainActivity.audioStatusList.get(position).getAudioState() != AudioStatus.AUDIO_STATE.IDLE.ordinal())
                    {
                        friendHolder.progressBar2.setMax(mainActivity.audioStatusList.get(position).getTotalDuration());
                        friendHolder.progressBar2.setProgress(mainActivity.audioStatusList.get(position).getCurrentValue());
                        friendHolder.progressBar2.setEnabled(true);
                    } else {
                        friendHolder.progressBar2.setProgress(0);
                        friendHolder.progressBar2.setEnabled(false);
                    }
                    if(mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal()
                            || mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
//                        holder.btnPlay.setText(context.getString(R.string.play));
                        friendHolder.thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_audio));
                    } else {
                        friendHolder.thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_audio));
                    }

                }

            }
            else if (chatMessage.getMessageType().equals("docs"))
            {
                friendHolder.cardViewAudio.setVisibility(View.GONE);
                friendHolder.layImage.setVisibility(View.GONE);
                friendHolder.txtContent.setVisibility(View.GONE);
                friendHolder.cardViewDoc.setVisibility(View.VISIBLE);
                friendHolder.txtDocName.setText(chatMessage.getDisplayFileName());
                if(chatMessage.getDisplayFileName().contains(".doc"))
                {
                    friendHolder.imgDoc.setImageResource(R.mipmap.docs_story);
                }
                else if(chatMessage.getDisplayFileName().contains(".pdf"))
                {
                    friendHolder.imgDoc.setImageResource(R.mipmap.pdf_story);
                } else if(chatMessage.getDisplayFileName().contains(".zip"))
                {
                    friendHolder.imgDoc.setImageResource(R.mipmap.zip_story);
                }
                else if(chatMessage.getDisplayFileName().contains(".xls"))
                {
                    friendHolder.imgDoc.setImageResource(R.mipmap.xls_story);
                }

            }
            else if (chatMessage.getMessageType().equals("text"))
            {
                friendHolder.cardViewAudio.setVisibility(View.GONE);
                friendHolder.layImage.setVisibility(View.GONE);
                friendHolder.cardViewDoc.setVisibility(View.GONE);
                friendHolder.txtContent.setVisibility(View.VISIBLE);

            }

            friendHolder.layout2.setTag(chatMessage);
            friendHolder.layout2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ChatMessage message=(ChatMessage)view.getTag();
                    chatCallback.result(message, position, view);

                }
            });
        }


        else if (holder instanceof ItemMessageUserHolder)
        {

            ItemMessageUserHolder userHolder = ((ItemMessageUserHolder) holder);
            userHolder.txtContent.setText(chatMessage.getMessage());
            if (chatMessage.getCreatedAt() != null && !chatMessage.getCreatedAt().isEmpty()) {
                if (chatMessage.getCreatedAt().contains("T")) {
                    userHolder.textMSgTime.setText(Utility.getTimeSlot(chatMessage.getCreatedAt()));
                }
            }
            userHolder.rey_loading.setVisibility(View.GONE);
            System.out.println("file path " + chatMessage.getFile());


            if (chatMessage.getMessageType().equals("image"))
            {
                userHolder.layDocument.setVisibility(View.GONE);
                userHolder.txtContent.setVisibility(View.GONE);
                userHolder.layImage.setVisibility(View.VISIBLE);
                userHolder.cardViewAudio.setVisibility(View.GONE);
                if (chatMessage.getPath() != null && !chatMessage.getPath().isEmpty())
                {
                    Uri resultUri = Uri.parse(chatMessage.getPath());
                    Glide.with(context)
                            .load(new File(resultUri.getPath()))
                            .transform(new RoundedCorners(16))
                            .placeholder(R.mipmap.place_holder)
                            .error(R.mipmap.place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((ItemMessageUserHolder) holder).imgChat);

//                    if (!chatMessage.isUpload())
//                    {
//                        userHolder.rey_loading.setVisibility(View.VISIBLE);
//                    } else {
//                        userHolder.rey_loading.setVisibility(View.GONE);
//                    }
                } else {
                    Glide.with(context)
                            .load(chatMessage.getFile())
                            .transform(new RoundedCorners(16))
                            .placeholder(R.mipmap.place_holder)
                            .error(R.mipmap.place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((ItemMessageUserHolder) holder).imgChat);
                }
            }
            else if (chatMessage.getMessageType().equals("audio"))
            {
                userHolder.cardViewAudio.setVisibility(View.VISIBLE);
                userHolder.layImage.setVisibility(View.GONE);
                userHolder.txtContent.setVisibility(View.GONE);
                userHolder.layDocument.setVisibility(View.GONE);
//                if(mainActivity.audioStatusList.size()<chatMessageArrayList.size())
                {
                    userHolder.txt_audio_time.setText(chatMessage.getDurationTime());
                    userHolder.txtAudioFileName.setText(chatMessage.getDisplayFileName());
                    if (mainActivity.audioStatusList.get(position).getAudioState() != AudioStatus.AUDIO_STATE.IDLE.ordinal())
                    {


                        userHolder.progressBar2.setMax(mainActivity.audioStatusList.get(position).getTotalDuration());
                        userHolder.progressBar2.setProgress(mainActivity.audioStatusList.get(position).getCurrentValue());
                        userHolder.progressBar2.setEnabled(true);
                    } else {

                        userHolder.progressBar2.setProgress(0);
                        userHolder.progressBar2.setEnabled(false);
                    }
                    if(mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal()
                            || mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
//                        holder.btnPlay.setText(context.getString(R.string.play));
                        userHolder.thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_audio));
                    } else {
                        userHolder.thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_audio));
                    }

                }

            }
            else if (chatMessage.getMessageType().equals("docs"))
            {
                userHolder.cardViewAudio.setVisibility(View.GONE);
                userHolder.layImage.setVisibility(View.GONE);
                userHolder.txtContent.setVisibility(View.GONE);
                userHolder.layDocument.setVisibility(View.VISIBLE);
                userHolder.txtDocName.setText(chatMessage.getDisplayFileName());

                if (chatMessage.getDisplayFileName()!=null) {
                    if(chatMessage.getDisplayFileName().contains(".doc"))
                    {
                        userHolder.imgDoc.setImageResource(R.mipmap.docs_story);
                    }
                    else if(chatMessage.getDisplayFileName().contains(".pdf"))
                    {
                        userHolder.imgDoc.setImageResource(R.mipmap.pdf_story);
                    } else if(chatMessage.getDisplayFileName().contains(".zip"))
                    {
                        userHolder.imgDoc.setImageResource(R.mipmap.zip_story);
                    }
                    else if(chatMessage.getDisplayFileName().contains(".xls"))
                    {
                        userHolder.imgDoc.setImageResource(R.mipmap.xls_story);
                    }
                }


            }
            else if (chatMessage.getMessageType().equals("text"))
            {
                userHolder.layImage.setVisibility(View.GONE);
                userHolder.cardViewAudio.setVisibility(View.GONE);
                userHolder.txtContent.setVisibility(View.VISIBLE);
                userHolder.layDocument.setVisibility(View.GONE);
            }


            userHolder.layDocument.setTag(chatMessage);
            userHolder.layDocument.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    LinearLayout linearLayout=(LinearLayout)view;
                    ChatMessage message=(ChatMessage)linearLayout.getTag();
                    chatCallback.result(message,position,view);
                }
            });
            userHolder.layImage.setTag(chatMessage);
            userHolder.layImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    ChatMessage message=(ChatMessage)view.getTag();
                    chatCallback.result(message,position,view);
                }
            });




        }

    }

    @Override
    public int getItemViewType(int position)
    {
        return chatMessageArrayList.get(position).getSenderId().equals(senderID) ? VIEW_TYPE_USER_MESSAGE : VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return chatMessageArrayList.size();
    }

   public class ItemMessageUserHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtContent, textMSgTime,txt_audio_time,txtDocName,txtAudioFileName;
        public ImageView imgChat,imgDoc;
        RelativeLayout layout1,layImage;
        ProgressView rey_loading;
        CardView cardViewAudio;
       public SeekBar progressBar2;
       public ImageView thumbnail_video_icon;
       LinearLayout layDocument;


        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            layout1=(RelativeLayout)itemView.findViewById(R.id.layout1);
            layDocument=(LinearLayout)itemView.findViewById(R.id.layDocument);
            txtAudioFileName = (CustomTextView)itemView.findViewById(R.id.txtAudioFileName);
            txtDocName = (CustomTextView) itemView.findViewById(R.id.txtDocName);
            imgDoc = (ImageView) itemView.findViewById(R.id.imgDoc);
            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentUser);
            textMSgTime = (CustomTextView) itemView.findViewById(R.id.textMSgTime);
            txt_audio_time  = (CustomTextView) itemView.findViewById(R.id.txt_audio_time);
            imgChat = (ImageView) itemView.findViewById(R.id.imgChat);
            layImage = (RelativeLayout) itemView.findViewById(R.id.layImage);
            rey_loading=(ProgressView)itemView.findViewById(R.id.rey_loading);
            cardViewAudio=(CardView)itemView.findViewById(R.id.cardViewAudio);
            progressBar2=(SeekBar)itemView.findViewById(R.id.progressBar2);
            thumbnail_video_icon=(ImageView)itemView.findViewById(R.id.thumbnail_video_icon);
            progressBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    if(fromUser) MediaPlayerUtils.applySeekBarValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            thumbnail_video_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        int position = getBindingAdapterPosition();

                    // Check if any other audio is playing
                    if(mainActivity.audioStatusList.get(position).getAudioState()
                            == AudioStatus.AUDIO_STATE.IDLE.ordinal()) {

                        // Reset media player
                        MediaPlayerUtils.Listener listener = (MediaPlayerUtils.Listener) context;
                        listener.onAudioComplete();
                    }

                    String audioPath = chatMessageArrayList.get(position).getFile();
                    AudioStatus audioStatus = mainActivity.audioStatusList.get(position);
                    int currentAudioState = audioStatus.getAudioState();

                    if(currentAudioState == AudioStatus.AUDIO_STATE.PLAYING.ordinal()) {
                        // If mediaPlayer is playing, pause mediaPlayer

                        thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_audio));
                        MediaPlayerUtils.pauseMediaPlayer();

                        audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PAUSED.ordinal());
                        mainActivity.audioStatusList.set(position, audioStatus);
                    } else if(currentAudioState == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
                        // If mediaPlayer is paused, play mediaPlayer

                        thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_audio));
                        MediaPlayerUtils.playMediaPlayer();

                        audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PLAYING.ordinal());
                        mainActivity.audioStatusList.set(position, audioStatus);
                    } else {
                        // If mediaPlayer is in idle state, start and play mediaPlayer
                        thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_audio));

                        audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PLAYING.ordinal());
                        mainActivity.audioStatusList.set(position, audioStatus);

                        try {
//                            String url="http://sessionway.com/userFiles/chatFiles/chatFiles-1603975163019-320-AudioRecording.mp3";
                            String url="";
                            if (chatMessageArrayList.get(position).getPath() != null && !chatMessageArrayList.get(position).getPath().isEmpty())
                            {
                                url=chatMessageArrayList.get(position).getPath();
                            }
                            else  {
                                url = chatMessageArrayList.get(position).getFile();
                            }
                            MediaPlayerUtils.startAndPlayMediaPlayer(url, (MediaPlayerUtils.Listener) context);

                            audioStatus.setTotalDuration(MediaPlayerUtils.getTotalDuration());


                            System.out.println("MediaPlayerUtils.getTotalDuration() "+MediaPlayerUtils.getTotalDuration());
                            mainActivity.audioStatusList.set(position, audioStatus);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                    }
                }
            });

        }
    }

    public class ItemMessageFriendHolder extends RecyclerView.ViewHolder
    {
        public ImageView imgChat,imgDoc;
        public CustomTextView txtContent, textMSgTime,txtDocName,txt_audio_time,txtAudioFileName;
        public CircleImageView imgFriend;
        public CardView cardViewAudio,cardViewDoc;
        RelativeLayout layImage;
        public SeekBar progressBar2;
        public ImageView thumbnail_video_icon;
        public RelativeLayout layout2;

        public ItemMessageFriendHolder(View itemView) {
            super(itemView);

            cardViewDoc=(CardView) itemView.findViewById(R.id.cardViewDoc);
            txtDocName = (CustomTextView) itemView.findViewById(R.id.txtDocName);
            txtAudioFileName = (CustomTextView) itemView.findViewById(R.id.txtAudioFileName);
            imgDoc = (ImageView) itemView.findViewById(R.id.imgDoc);
            layout2=(RelativeLayout)itemView.findViewById(R.id.layout2);
            layImage = (RelativeLayout) itemView.findViewById(R.id.layImage);
            txt_audio_time = (CustomTextView) itemView.findViewById(R.id.txt_audio_time);
            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentFriend);
            textMSgTime = (CustomTextView) itemView.findViewById(R.id.textMSgTime);
            imgFriend = (CircleImageView) itemView.findViewById(R.id.imgFriend);
            imgChat = (ImageView) itemView.findViewById(R.id.imgChat);
            cardViewAudio=(CardView)itemView.findViewById(R.id.cardViewAudio);
            progressBar2=(SeekBar)itemView.findViewById(R.id.progressBar2);
            thumbnail_video_icon=(ImageView)itemView.findViewById(R.id.thumbnail_video_icon);
            progressBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    if(fromUser) MediaPlayerUtils.applySeekBarValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            thumbnail_video_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();

                    // Check if any other audio is playing
                    if(mainActivity.audioStatusList.get(position).getAudioState()
                            == AudioStatus.AUDIO_STATE.IDLE.ordinal()) {

                        // Reset media player
                        MediaPlayerUtils.Listener listener = (MediaPlayerUtils.Listener) context;
                        listener.onAudioComplete();
                    }

                    String audioPath = chatMessageArrayList.get(position).getFile();
                    AudioStatus audioStatus = mainActivity.audioStatusList.get(position);
                    int currentAudioState = audioStatus.getAudioState();

                    if(currentAudioState == AudioStatus.AUDIO_STATE.PLAYING.ordinal()) {
                        // If mediaPlayer is playing, pause mediaPlayer

                        thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_audio));
                        MediaPlayerUtils.pauseMediaPlayer();

                        audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PAUSED.ordinal());
                        mainActivity.audioStatusList.set(position, audioStatus);
                    } else if(currentAudioState == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
                        // If mediaPlayer is paused, play mediaPlayer

                        thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_audio));
                        MediaPlayerUtils.playMediaPlayer();

                        audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PLAYING.ordinal());
                        mainActivity.audioStatusList.set(position, audioStatus);
                    } else {
                        // If mediaPlayer is in idle state, start and play mediaPlayer
                        thumbnail_video_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_audio));

                        audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PLAYING.ordinal());
                        mainActivity.audioStatusList.set(position, audioStatus);

                        try {
//                            String url="http://sessionway.com/userFiles/chatFiles/chatFiles-1603975163019-320-AudioRecording.mp3";
                            String  url = chatMessageArrayList.get(position).getFile();


                            MediaPlayerUtils.startAndPlayMediaPlayer(url, (MediaPlayerUtils.Listener) context);

                            audioStatus.setTotalDuration(MediaPlayerUtils.getTotalDuration());
                            System.out.println("MediaPlayerUtils.getTotalDuration() "+MediaPlayerUtils.getTotalDuration());
                            mainActivity.audioStatusList.set(position, audioStatus);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

}
