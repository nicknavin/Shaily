package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.SubscriptionGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SampleDemoAdapter extends RecyclerView.Adapter<SampleDemoAdapter.ViewHolder> {


    Context context;

    ArrayList<SubscriptionGroup> subscriptionGroupsList = new ArrayList<>();
ApiItemCallback apiItemCallback;
String newGroup;
    public SampleDemoAdapter(Context context, ArrayList<SubscriptionGroup> list,ApiItemCallback callback) {
        this.context = context;
subscriptionGroupsList=list;
apiItemCallback=callback;

    }


    @Override
    public SampleDemoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cunstant_story_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(SampleDemoAdapter.ViewHolder holder, final int position)
    {


        SubscriptionGroup group = subscriptionGroupsList.get(position);
//        System.out.println("imageurl "+position+": "+ Urls.BASE_IMAGES_URL +group.getGroup_url_image_address());
        if (!group.get_id().equals("-0new")) {
            holder.layCircleAdd.setVisibility(View.GONE);

            if (group.getGroup_image_url() != null && !group.getGroup_image_url().isEmpty()) {

                String url = Urls.BASE_IMAGES_URL +group.getGroup_image_url();
                System.out.println("url story "+url);
                 Picasso.with(context).load(url).error(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.imgGroup);

            }else
            {
                holder.imgGroup.setImageResource(R.mipmap.place_holder);
            }

        }
        else if (group.get_id().equals("-0new"))
        {
            holder.layCircleAdd.setVisibility(View.VISIBLE);
          holder.layCircle.setVisibility(View.GONE);
        }


        holder.txtGroupName.setText(group.getGroup_name());
        holder.layGroup.setTag(group);
        holder.layGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                apiItemCallback.result(position);
            }
        });

    }


    @Override
    public int getItemCount() {

        return subscriptionGroupsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CustomTextView txtGroupName;
        CircleImageView imgGroup;
        LinearLayout layGroup;
        ImageView imgAddGroup;
        RelativeLayout layCircle,layCircleAdd;

        public ViewHolder(View view)
        {
            super(view);
            txtGroupName = (CustomTextView) view.findViewById(R.id.txtGroupName);
            imgGroup = (CircleImageView) view.findViewById(R.id.imgGroup);
            layGroup = (LinearLayout) view.findViewById(R.id.layGroup);
            imgAddGroup = (ImageView) view.findViewById(R.id.imgAddGroup);
            layCircle = (RelativeLayout) view.findViewById(R.id.layCircle);
            layCircleAdd = (RelativeLayout) view.findViewById(R.id.layCircleAdd);

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
