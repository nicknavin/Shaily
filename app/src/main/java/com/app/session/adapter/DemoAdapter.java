package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.ConsultUser;
import com.app.session.data.model.ConsultantData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ViewHolder> implements Filterable {


    Context context;

    ArrayList<ConsultUser> myStoriesList = new ArrayList<>();
    ArrayList<ConsultUser> mArrayList;

    ApiItemCallback callback;
    String userid="";

    public DemoAdapter(Context context, String id,ArrayList<ConsultUser> list, ApiItemCallback callback) {
        this.context = context;
        myStoriesList = list;
        mArrayList=list;
        this.callback = callback;
        userid=id;
    }


    @Override
    public DemoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_story, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(DemoAdapter.ViewHolder holder, final int position) {
        ConsultUser consultantData = myStoriesList.get(position);

        {
            holder.txt_userName.setText(consultantData.getUserName());
//        holder.txtCategoryName.setText(consultantData.get());

            if (consultantData.getCountryId().getCountry_cd()!= null) {
                String urlCountry= Urls.BASE_IMAGES_URL+"userFiles/flags-mini/"+consultantData.getCountryId().getCountry_cd().toLowerCase()+".png";

                System.out.println("urlCountry "+urlCountry);
                Picasso.with(context).load(urlCountry).error(R.mipmap.country_icon).into(holder.img_country_icon);
            }


            if (consultantData.getBriefCV().get(0).getVideoThumbnail() != null && !consultantData.getBriefCV().get(0).getVideoThumbnail().isEmpty())
            {
                String url = Urls.BASE_IMAGES_URL + consultantData.getBriefCV().get(0).getVideoThumbnail();
                //Picasso.with(context).load(url).into(holder.imgStory);
                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.black_ripple_btn_bg_squre)
                        .error(R.drawable.black_ripple_btn_bg_squre)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgStory);
            }
            else
            {
                Picasso.with(context).load(R.drawable.black_ripple_btn_bg_squre).into(holder.imgStory);
          //      holder.imgStory.setBackgroundColor(context.getResources().getColor(R.color.gray));
            }
            if (consultantData.getImageUrl() != null && !consultantData.getImageUrl().isEmpty()) {
                String url = Urls.BASE_IMAGES_URL + consultantData.getImageUrl();

                Picasso.with(context).load(url).error(R.mipmap.profile_image).
                        placeholder(R.mipmap.profile_image).into(holder.imgProfile);

                Glide.with(context)
                        .load(url)
                        .placeholder(R.mipmap.profile_image)
                        .error(R.mipmap.profile_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgProfile);


            }
            else
            {
                Picasso.with(context).load(R.mipmap.profile_image).error(R.mipmap.profile_image).placeholder(R.mipmap.demo).into(holder.imgProfile);
            }

            holder.layTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    callback.result(position);
                }
            });
        }

    }


    @Override
    public int getItemCount() {

        return myStoriesList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    myStoriesList = mArrayList;
                } else {

                    ArrayList<ConsultUser> filteredList = new ArrayList<>();

                    for (ConsultUser Category : mArrayList) {

                        if (Category.getUserName().toLowerCase().contains(charString)) {

                            filteredList.add(Category);
                        }
                    }

                    myStoriesList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = myStoriesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                myStoriesList = (ArrayList<ConsultUser>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextView txt_userName, txtCategoryName;
        ImageView imgStory;
        CircleImageView imgProfile;
       ImageView img_country_icon;
        LinearLayout layTop;

        public ViewHolder(View view) {
            super(view);
            txt_userName = (CustomTextView) view.findViewById(R.id.txt_userName);
            txtCategoryName = (CustomTextView) view.findViewById(R.id.txtCategoryName);
            img_country_icon = (ImageView) view.findViewById(R.id.img_country_icon);
            imgStory = (ImageView) view.findViewById(R.id.imgStory);
            imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
            layTop = (LinearLayout) view.findViewById(R.id.layTop);


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
