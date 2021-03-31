package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.Brief_CV;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class BriefCvEditAdapter extends RecyclerView.Adapter<BriefCvEditAdapter.ViewHolder> {


    Context context;
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
    ApiCallback apiCallback;

    public BriefCvEditAdapter(Context context, ArrayList<Brief_CV> list, ApiCallback callback) {
        this.context = context;
        brief_cvList = list;
        apiCallback = callback;
    }


    @Override
    public BriefCvEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_brief_edit_cv, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }

    //addTextChangedListener
    @Override
    public void onBindViewHolder(BriefCvEditAdapter.ViewHolder holder, final int position) {
        final Brief_CV brief_cv = brief_cvList.get(position);
        holder.txtlang.setText("Bio "+ " " + brief_cv.getLanguage_id().getName());
        holder.txtlang.setTag(brief_cv);
        holder.txtlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallback.result("" + position);
            }
        });
        holder.edt_cv.setText(brief_cv.getBrief_cv());
        holder.edt_cv.setEnabled(false);
        holder.edt_cv.setClickable(false);
        holder.edt_cv.setFocusable(false);

        if(brief_cv.getVideo_thumbnail()!=null&&!brief_cv.getVideo_thumbnail().isEmpty())
        {
            String url = Urls.BASE_IMAGES_URL+brief_cv.getVideo_thumbnail();

            Picasso.with(context).load(url).placeholder(R.mipmap.profile_large).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.imgVideoCover);


//            Picasso.with(context).load(url).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    int width = bitmap.getWidth();
//                    int height = bitmap.getHeight();
//
//                    holder.imgVideoCover.setImageBitmap(bitmap);
//                    Log.d("Bitmap Dimensions: ", width + "x" + height);
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//                }
//            });

        }

//        holder.edt_cv.setTag(brief_cv);
//        holder.edt_cv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                brief_cv.setBrief_cv(""+s);
//
//            }
//        });
    }


    @Override
    public int getItemCount() {

        return brief_cvList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtlang;
        CustomEditText edt_cv;
        ImageView imgVideoCover;

        public ViewHolder(View view) {
            super(view);
            txtlang = (CustomTextView) view.findViewById(R.id.txtlang);
            edt_cv = (CustomEditText) view.findViewById(R.id.edt_cv);
            imgVideoCover=(ImageView)view.findViewById(R.id.imgVideoCover);

        }

    }


}
