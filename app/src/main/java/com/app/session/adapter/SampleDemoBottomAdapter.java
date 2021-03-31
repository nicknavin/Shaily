package com.app.session.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.Brief_CV;
import com.borjabravo.readmoretextview.ReadMoreTextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SampleDemoBottomAdapter extends RecyclerView.Adapter<SampleDemoBottomAdapter.ViewHolder> {


    Context context;

    ArrayList<Brief_CV> brief_cvList;
    ApiCallback callback;

    public SampleDemoBottomAdapter(Context context, ArrayList<Brief_CV> list, ApiCallback callback) {
        this.context = context;
        this.brief_cvList=list;
        this.callback=callback;


    }


    @Override
    public SampleDemoBottomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_consultant_bottom_story, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(SampleDemoBottomAdapter.ViewHolder holder, final int position)
    {
//        Brief_CV brief_cv=brief_cvList.get(position);
//        holder.txtBriefCV.setText(brief_cv.getEnglish_name());
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "Segoe_UI.ttf");
        holder.txtTitleBriefCV.setTypeface(face);
        holder.txtTitleBriefCV.setTrimCollapsedText(" more");
        holder.txtTitleBriefCV.setTrimExpandedText(" less");

        holder.layExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                callback.result("");
            }
        });


//holder.txtTitleBriefCV.setText(brief_cv.getBrief_cv());
//        if(!brief_cv.getTitle_name().equals("0")) {
//            holder.txtTitleName.setText(brief_cv.getTitle_name());
//        }
//
//        if (brief_cv.getVideo_thumbnail() != null && !brief_cv.getVideo_thumbnail().isEmpty()) {
//
//            String url = Urls.BASE_IMAGES_URL +brief_cv.getVideo_thumbnail();
//            System.out.println("url "+url);
//            // Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.imgGroup);
//            Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.imgStory);
//        }



    }


    @Override
    public int getItemCount() {

        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

            CustomTextView txtTitleName,txtBriefCV;
            ImageView imgStory;
        ReadMoreTextView txtTitleBriefCV;
        LinearLayout layExpert;

        public ViewHolder(View view) {
            super(view);

            txtTitleName=(CustomTextView)view.findViewById(R.id.txtTitleName);
            txtTitleBriefCV=(ReadMoreTextView)view.findViewById(R.id.txtTitleBriefCV);
            txtBriefCV=(CustomTextView)view.findViewById(R.id.txtBriefCV);
            imgStory=(ImageView)view.findViewById(R.id.imgStory);
            layExpert=(LinearLayout)view.findViewById(R.id.layExpert);


        }

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
