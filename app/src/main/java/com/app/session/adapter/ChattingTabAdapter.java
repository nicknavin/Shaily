package com.app.session.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.activity.LoginActivity;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Consultant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class ChattingTabAdapter extends RecyclerView.Adapter<ChattingTabAdapter.ViewHolder> {


    Context context;
    ArrayList<Consultant> consultantArrayList = new ArrayList<>();
    BaseActivity activity;
    String user_type;

    public ChattingTabAdapter(Context context, ArrayList<Consultant> list, String type) {
        this.context = context;
        activity = (BaseActivity) context;
        consultantArrayList = list;
        user_type = type;
    }


    @Override
    public ChattingTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_list_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ChattingTabAdapter.ViewHolder holder, final int position) {

        final Consultant consultant = consultantArrayList.get(position);
        holder.contactName.setText(consultant.getUser_name());
        holder.user_number.setText(consultant.getMobile_no());
        holder.lay_chat.setTag(consultant);
        holder.lay_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LinearLayout linearLayout = (LinearLayout) v;
                Consultant consultant1 = (Consultant) linearLayout.getTag();
                callVideoAudioStatus(consultant1);
//                callCallingStatus(getParam(consultant1), Urls.GET_CALLING_STATUS, consultant1);

            }
        });

        if (consultant.getImageUrl() != null) {
            System.out.println("patientProfile " + consultant.getImageUrl());
            String imageUrl = consultant.getImageUrl();
            if (!imageUrl.equals("")) {
                AQuery aQuery = null;
                aQuery = new AQuery(context);
                aQuery.id(holder.profPic).image(imageUrl, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
            } else {
                holder.profPic.setImageResource(R.mipmap.profile_large);
            }
        } else {
            holder.profPic.setImageResource(R.mipmap.profile_large);
        }


    }


    @Override
    public int getItemCount() {

        return consultantArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView contactName, userLastMsg, chat_time, chat_count, user_number;
        CircleImageView profPic;
        ImageView img_cam_status;
        LinearLayout lay_chat;


        public ViewHolder(View view) {
            super(view);
            lay_chat = (LinearLayout) view.findViewById(R.id.lay_chat);
            contactName = (CustomTextView) view.findViewById(R.id.contact_name);
            user_number = (CustomTextView) view.findViewById(R.id.user_number);
            profPic = (CircleImageView) view.findViewById(R.id.img_profilepic);



        }

    }

    private void callCallingStatus(Map<String, Object> param, String url, final Consultant consultant1) {

        try {

            activity.showLoading();
            AqueryCall request = new AqueryCall(activity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    activity.dismiss_loading();
//                    try {
//                        //{"Status":true,"audio_status":false,"video_status":false,"Message":"Sucessfully"}
//                        String audio_status = js.getString("audio_status");
//                        String video_status = js.getString("video_status");
//                        Intent intent = new Intent(context, ConversationActivity.class);
//                        intent.putExtra(ConversationUIService.USER_ID, consultant1.getUser_cd());
//                        intent.putExtra("ID", consultant1.getUser_cd());
//                        intent.putExtra("NAME", consultant1.getUser_name());
//                        intent.putExtra("EMAIL", consultant1.getEmail_address());
//                        intent.putExtra("IS_CONSULTANT", consultant1.getIs_consultant());
//                        intent.putExtra("USER_IS_CONSULTANT", DataPrefrence.getPref(context, Constant.IS_CONSULTANT, ""));
//                        intent.putExtra("AUDIO_STATUS", audio_status.toLowerCase());
//                        intent.putExtra("VIDEO_STATUS", video_status.toLowerCase());
//                        intent.putExtra("USER_TYPE", user_type);
//                        intent.putExtra("USER_CD", activity.userId);
//                        intent.putExtra("TOKEN_ID", activity.accessToken);
//                        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
//
//                        context.startActivity(intent);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    activity.dismiss_loading();

                    if (failed.equals("Your Not Authorised User...")) {
                        activity.dd.dismiss();
                        activity.clearDataBase();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        MyDialog.iPhone(failed, context);
                    }
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    activity.dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    activity.dismiss_loading();
                }
            });

//            request.commonAPI(url, param, new RequestCallback() {
//
//                @Override
//                public void onSuccess(JSONObject js, String success) {
//                    dismiss_loading();
//                    Log.d(TAG, "JSON: " + js.toString());
//
//                }
//
//                @Override
//                public void onFailed(JSONObject js, String failed, int status) {
//                    dismiss_loading();
//                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error)))
//                    {
//                        unAuthorized();
//                        showToast(failed);
//                    } else {
//                        MyDialog.iPhone(failed, context);
//                    }
//                }
//
//                @Override
//                public void onNull(JSONObject js, String nullp) {
//                    MyDialog.iPhone(nullp, context);
//                    dismiss_loading();
//                }
//
//                @Override
//                public void onException(JSONObject js, String exception) {
//                    MyDialog.iPhone(exception, context);
//                    dismiss_loading();
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParam(Consultant consultant) {

        Map<String, Object> params = new HashMap<>();
        params.put("from_user_cd", activity.userId);
        params.put("token_id", activity.accessToken);
        params.put("to_user_cd", consultant.getUser_cd());


        return params;
    }



    private void callVideoAudioStatus(Consultant consultant1)
    {
//        Intent intent = new Intent(context, ConversationActivity.class);
//        intent.putExtra(ConversationUIService.USER_ID, consultant1.getUser_cd());
//        intent.putExtra("ID", consultant1.getUser_cd());
//        intent.putExtra("NAME", consultant1.getUser_name());
//        intent.putExtra("EMAIL", consultant1.getEmail_address());
//        intent.putExtra("IS_CONSULTANT", consultant1.getIs_consultant());
//        intent.putExtra("USER_IS_CONSULTANT", DataPrefrence.getPref(context, Constant.IS_CONSULTANT, ""));
//        intent.putExtra("AUDIO_STATUS", "true");
//        intent.putExtra("VIDEO_STATUS", "true");
//        intent.putExtra("USER_TYPE", user_type);
//        intent.putExtra("USER_CD", activity.userId);
//        intent.putExtra("TOKEN_ID", activity.accessToken);
//        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
//
//        context.startActivity(intent);
    }



}
