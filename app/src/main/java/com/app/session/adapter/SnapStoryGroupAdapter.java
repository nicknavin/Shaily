package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.FollowerMyStory;
import com.app.session.utility.DynamicHeightImageView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SnapStoryGroupAdapter extends RecyclerView.Adapter<SnapStoryGroupAdapter.ViewHolder> {


    Context context;

    ArrayList<FollowerMyStory> myStoriesList = new ArrayList<>();



    public SnapStoryGroupAdapter(Context context, ArrayList<FollowerMyStory> list) {
        this.context = context;
        myStoriesList = list;

    }


    @Override
    public SnapStoryGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_snap_friend_story_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(SnapStoryGroupAdapter.ViewHolder holder, final int position) {

        final FollowerMyStory storyGroup = myStoriesList.get(position);
        holder.txt_userName.setText(storyGroup.getUser_name());

        if (storyGroup.getImgUrl() != null) {
            System.out.println("getImgUrl " + storyGroup.getImgUrl());
            String imageUrl = storyGroup.getImgUrl();
            if (!imageUrl.equals("")) {
                AQuery aQuery = null;
                aQuery = new AQuery(context);
                aQuery.id(holder.profPic).image(imageUrl, false, false, 0, R.mipmap.ic_photo_cont, null, AQuery.FADE_IN);
                aQuery.id(holder.img).image(imageUrl, false, false, 0, R.mipmap.ic_photo_cont, null, AQuery.FADE_IN);


            } else {
                holder.profPic.setImageResource(R.mipmap.ic_photo_cont);
            }
        } else {
            holder.profPic.setImageResource(R.mipmap.ic_photo_cont);
        }


        holder.cardView.setTag(storyGroup);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CardView view = (CardView) v;
//                StoryGroup group=(StoryGroup)view.getTag() ;
//                Intent intent=new Intent(context, AllDefaultStoryActivity.class);
//
//               context.startActivity(intent);

            }
        });
//


    }


    @Override
    public int getItemCount() {

        return myStoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt_userName;
        CircleImageView profPic;
        DynamicHeightImageView img;
    CardView cardView;
        public ViewHolder(View view) {
            super(view);
            img = (DynamicHeightImageView) view.findViewById(R.id.img);
            txt_userName = (CustomTextView) view.findViewById(R.id.txt_userName);

            cardView = (CardView) view.findViewById(R.id.card_view);
            profPic = (CircleImageView) view.findViewById(R.id.imgProfile);


        }

    }

//    private void callRemoveMember(Map<String, Object> param, String url, final int position) {
//
//        try {
//            baseActivity.showLoading();
//            AqueryCall request = new AqueryCall(baseActivity);
//            request.commonAPI(url, param, new RequestCallback() {
//                @Override
//                public void onSuccess(JSONObject js, String success) {
//                    baseActivity.dismiss_loading();
//                    myStories.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, myStories.size());
//
//                }
//
//                @Override
//                public void onFailed(JSONObject js, String failed, int status) {
//                    baseActivity.dismiss_loading();
//                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
//                        baseActivity.unAuthorized();
//                    } else {
//                        MyDialog.iPhone(failed, context);
//                    }
//                }
//
//                @Override
//                public void onNull(JSONObject js, String nullp) {
//                    baseActivity.dismiss_loading();
//                }
//
//                @Override
//                public void onException(JSONObject js, String exception) {
//                    baseActivity.dismiss_loading();
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
////            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
//        }
//    }
//
//    private Map<String, Object> getParamRemoveMember(String consultant_cd) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("company_cd", baseActivity.userId);
//        params.put("token_id", baseActivity.accessToken);
//        params.put("consultant_cd", consultant_cd);
//        return params;
//    }


}
