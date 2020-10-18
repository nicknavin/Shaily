package com.app.session.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.activity.CreateSpecialGroupActivity;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.MyStory;
import com.app.session.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class MyStoryAdapter extends RecyclerView.Adapter<MyStoryAdapter.ViewHolder> {
    Context context;
    ArrayList<MyStory> myStoriesList = new ArrayList<>();
    BaseActivity baseActivity;
    String follower_user_cd = "";
    ViewHolder viewHolder;

    public MyStoryAdapter(Context context, ArrayList<MyStory> list, String follower_cd) {
        this.context = context;
        myStoriesList = list;
        baseActivity = (BaseActivity) context;
        follower_user_cd = follower_cd;

    }

    @Override
    public MyStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_story_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        MyStory myStory = myStoriesList.get(position);
        holder.story_title.setText(myStory.getStory_title());
        holder.txt_userName.setText(myStory.getUser_name());

        if (myStory.getType().equals("1")) {

            holder.img_story.setVisibility(View.GONE);
//            holder.jzvdStd.setVisibility(View.GONE);
            holder.lay_video.setVisibility(View.GONE);
            holder.txt_story_msg.setText(myStory.getStory_text());


        } else if (myStory.getType().equals("2")) {

            holder.img_story.setVisibility(View.VISIBLE);
//            holder.jzvdStd.setVisibility(View.GONE);
            holder.lay_video.setVisibility(View.GONE);
            if (myStory.getStory_text() != null && !myStory.getStory_text().equals("")) {
                String url = myStory.getStory_text();

                AQuery aQuery = null;
                aQuery = new AQuery(context);
                aQuery.id(holder.img_story).image(url, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);


            } else {
                holder.img_story.setImageResource(R.mipmap.profile_large);
            }
            holder.txt_story_contain.setText(myStory.getStory_caption());
        } else if (myStory.getType().equals("3")) {
            try {
                holder.txt_story_contain.setText(myStory.getStory_caption());
                holder.img_story.setVisibility(View.GONE);
//                holder.jzvdStd.setVisibility(View.VISIBLE);
                holder.lay_video.setVisibility(View.VISIBLE);
                viewHolder = holder;

                if (myStory.getStory_text() != null) {
                    String img_url = "", video_url = "";
                    if (myStory.getThumbnail_text().contains("mp4") && myStory.getThumbnail_text() != null) {
                        img_url = myStory.getStory_text();
                        video_url = myStory.getThumbnail_text();
                    } else {
                        video_url = myStory.getStory_text();

                        img_url = myStory.getThumbnail_text();
                    }

                    System.out.println("video_url is " + video_url);
//                    holder.jzvdStd.setUp(video_url
//                            , "", JzvdStd.SCREEN_NORMAL);
//
//                    AQuery aQuery = null;
//                    aQuery = new AQuery(context);
//                    //aQuery.id(imgProfile).image(imageUrl, false, false, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
//                    aQuery.id(viewHolder.jzvdStd.thumbImageView).image(img_url, true, true, 0, 0, new BitmapAjaxCallback() {
//                        @Override
//                        public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
//
//                            if (status.getCode() == 200) {
//                                if (bm != null) {
//                                    viewHolder.jzvdStd.thumbImageView.setImageBitmap(bm);
//                                }
//                            } else {
//                                iv.setImageResource(R.mipmap.ic_photo_cont);
//                            }
//                        }
//                    });



                }


            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }


        }
        holder.img_story.setTag(myStory);
        holder.img_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                MyStory story = (MyStory) imageView.getTag();
                if (story.getType().equals("3")) {
//                    Intent intent = new Intent(context, VideoViewActivity.class);
//                    intent.putExtra("URL", story.getStory_text());
//                    context.startActivity(intent);
                }
            }
        });

//        String url = myStory.getImgUrl();

        if (myStory.getImgUrl() != null) {
            System.out.println("getImgUrl " + myStory.getImgUrl());
            String imageUrl = myStory.getImgUrl();
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
        holder.imgMenu.setTag(myStory);
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                MyStory story = (MyStory) imageView.getTag();
                showMenu(v, story, position);
            }
        });


        if (holder.txt_story_contain.getText().toString().isEmpty()) {
            holder.txt_story_contain.setVisibility(View.GONE);
        } else if (!holder.txt_story_contain.getText().toString().isEmpty()) {
            holder.txt_story_contain.setVisibility(View.VISIBLE);
        }
        if (holder.txt_story_msg.getText().toString().isEmpty()) {
            holder.txt_story_msg.setVisibility(View.GONE);
        } else if (!holder.txt_story_msg.getText().toString().isEmpty()) {
            holder.txt_story_msg.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public int getItemCount() {

        return myStoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        CustomTextView txt_userName, story_title, txt_story_contain, txt_story_msg;
        CircleImageView profPic;
        ImageView img_story, imgMenu;
        RelativeLayout lay_center;
        LinearLayout lay_video;
//        JzvdStd jzvdStd;

        WebView webView;

        public ViewHolder(View view) {
            super(view);
            webView = (WebView) view.findViewById(R.id.webview);
            txt_userName = (CustomTextView) view.findViewById(R.id.txt_userName);
            story_title = (CustomTextView) view.findViewById(R.id.story_title);
            img_story = (ImageView) view.findViewById(R.id.img_story);
            imgMenu = (ImageView) view.findViewById(R.id.imgRemove);
            lay_video = (LinearLayout) view.findViewById(R.id.lay_video);
            txt_story_contain = (CustomTextView) view.findViewById(R.id.txt_story_contain);
            txt_story_msg = (CustomTextView) view.findViewById(R.id.txt_story_msg);
            profPic = (CircleImageView) view.findViewById(R.id.imgProfile);
            //jzvdStd = (JzvdStd) view.findViewById(R.id.videoplayer);

        }


    }

    public void showMenu(View v, final MyStory myStory, final int position) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_report:
                        if (Utility.isConnectingToInternet(context)) {
                            context.startActivity(new Intent(context, CreateSpecialGroupActivity.class));
                        } else {
                            Utility.showInternetPop(context);
                        }
                        return true;

                    case R.id.menu_unfollwer:
                        if (Utility.isConnectingToInternet(context)) {
//                            context.startActivity(new Intent(context, ProfileEditConsultantActivity.class));
                        } else {
                            Utility.showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_delete:
                        if (Utility.isConnectingToInternet(context)) {
                            callRemoveMember(getParamRemoveMember(myStory.getStory_id()), Urls.DELETE_STORY, position);
                        } else {
                            Utility.showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_edit:
                        if (Utility.isConnectingToInternet(context)) {
//                            context.startActivity(new Intent(context, ProfileEditConsultantActivity.class));
                        } else {
                            Utility.showInternetPop(context);
                        }
                        return true;


                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        if (follower_user_cd.isEmpty()) {
            inflater.inflate(R.menu.menu_indivdual_story, popup.getMenu());
        } else {
            inflater.inflate(R.menu.menu_followr_story, popup.getMenu());
        }

        popup.show();
    }

    private void callRemoveMember(Map<String, Object> param, String url, final int position) {

        try {
            baseActivity.showLoading();
            AqueryCall request = new AqueryCall(baseActivity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    baseActivity.dismiss_loading();
                    myStoriesList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, myStoriesList.size());

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    baseActivity.dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        baseActivity.unAuthorized();
                    } else {
                        MyDialog.iPhone(failed, context);
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

    private Map<String, Object> getParamRemoveMember(String story_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", baseActivity.userId);
        params.put("token_id", baseActivity.accessToken);
        params.put("story_id", story_id);
        return params;
    }








}
