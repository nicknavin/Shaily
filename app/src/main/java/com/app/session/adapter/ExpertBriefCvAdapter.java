package com.app.session.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.Brief_CV;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class ExpertBriefCvAdapter extends RecyclerView.Adapter<ExpertBriefCvAdapter.ViewHolder> {


    Context context;

    ArrayList<Brief_CV> brief_cvArrayList;

    public ExpertBriefCvAdapter(Context context, ArrayList<Brief_CV> list) {
        this.context = context;
brief_cvArrayList =list;

    }


    @Override
    public ExpertBriefCvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expert_briefcv, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ExpertBriefCvAdapter.ViewHolder holder, final int position)
    {
        Brief_CV briefCv= brief_cvArrayList.get(position);

        holder.txtBio.setText(briefCv.getEnglish_name());
        if(!briefCv.getTitle_name().equals("0")) {
            holder.txtTitleName.setText(briefCv.getTitle_name());
        }
        holder.txtTitleBriefCV.setText(briefCv.getBrief_cv());
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "Segoe_UI.ttf");
        holder.txtTitleBriefCV.setTypeface(face);
        holder.txtTitleBriefCV.setTrimCollapsedText(" more");
        holder.txtTitleBriefCV.setTrimExpandedText(" less");
        if (briefCv.getVideo_thumbnail() != null && !briefCv.getVideo_thumbnail().isEmpty() && !briefCv.getVideo_thumbnail().equals("null")) {
            String url = Urls.BASE_IMAGES_URL + briefCv.getVideo_thumbnail();
            Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.imgStory);
        }
        else
        {
            holder.imgStory.setVisibility(View.GONE);
        }
holder.txtSubscriber.setTag(briefCv);
        holder.txtSubscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Brief_CV cv=(Brief_CV)view.getTag();
                if(cv.isChecked())
                {
                    cv.setChecked(false);
                    holder.txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber));
                }
                else
                {
                    cv.setChecked(true);
                    holder.txtSubscriber.setTextColor(context.getResources().getColor(R.color.txt_subscriber_select));
                }


            }
        });



    }


    @Override
    public int getItemCount() {

        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CustomTextView txtBio,txtTitleName,txtSubscriber;

        ReadMoreTextView txtTitleBriefCV;
        ImageView imgStory;


        public ViewHolder(View view)
        {
            super(view);
            imgStory = (ImageView) view.findViewById(R.id.imgStory);
            txtBio = (CustomTextView) view.findViewById(R.id.txtBio);
            txtTitleName = (CustomTextView) view.findViewById(R.id.txtTitleName);
            txtSubscriber = (CustomTextView) view.findViewById(R.id.txtSubscriber);
            txtTitleBriefCV = (ReadMoreTextView) view.findViewById(R.id.txtTitleBriefCV);

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
