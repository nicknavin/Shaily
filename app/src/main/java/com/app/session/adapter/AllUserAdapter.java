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
import com.app.session.model.AllUserBody;
import com.app.session.model.ConsultUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder> implements Filterable {


    Context context;

    ArrayList<ConsultUser> myStoriesList = new ArrayList<>();
    ArrayList<ConsultUser> mArrayList;

    ApiItemCallback callback;
    String userid="";

    public AllUserAdapter(Context context, ArrayList<ConsultUser> list, ApiItemCallback callback) {
        this.context = context;
        myStoriesList = list;
        this.callback = callback;

    }


    @Override
    public AllUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_story, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(AllUserAdapter.ViewHolder holder, final int position) {
        ConsultUser consultantData = myStoriesList.get(position);

        if (consultantData.getCountryId().getCountry_cd()!= null) {
            String urlCountry= Urls.BASE_IMAGES_URL+"userFiles/falgs-mini/"+consultantData.getCountryId().getCountry_cd()+".png";
            Picasso.with(context).load(urlCountry).into(holder.img_country_icon);
        }


        holder.txt_userName.setText(consultantData.getUserName());
//        holder.txtCategoryName.setText(consultantData.get());
            if (consultantData.getBriefCV().get(0).getVideoThumbnail() != null && !consultantData.getBriefCV().get(0).getVideoThumbnail().isEmpty())
            {
                String url = Urls.BASE_IMAGES_URL + consultantData.getBriefCV().get(0).getVideoThumbnail();
                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.black_ripple_btn_bg_squre)
                        .error(R.drawable.black_ripple_btn_bg_squre)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgStory);

            }
            if (consultantData.getImageUrl() != null && !consultantData.getImageUrl().isEmpty()) {
                String url = Urls.BASE_IMAGES_URL + consultantData.getImageUrl();


                Glide.with(context)
                        .load(url)
                        .placeholder(R.mipmap.profile_image)
                        .error(R.mipmap.profile_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgProfile);

            }

            holder.layTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.result(position);
                }
            });


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
            imgStory = (ImageView) view.findViewById(R.id.imgStory);
            imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
            img_country_icon = (ImageView) view.findViewById(R.id.img_country_icon);
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
