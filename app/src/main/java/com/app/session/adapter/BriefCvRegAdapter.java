package com.app.session.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.Brief_CV;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class BriefCvRegAdapter extends RecyclerView.Adapter<BriefCvRegAdapter.ViewHolder> {


    Context context;
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();
ApiCallback apiCallback;
    Drawable img;
    public BriefCvRegAdapter(Context context, ArrayList<Brief_CV> list, ApiCallback callback) {
        this.context = context;
        brief_cvList = list;
        apiCallback=callback;

    }


    @Override
    public BriefCvRegAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_briefcv_registeration, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }

//addTextChangedListener
    @Override
    public void onBindViewHolder(BriefCvRegAdapter.ViewHolder holder, final int position) {
        final Brief_CV brief_cv= brief_cvList.get(position);
        holder.txtlang.setText(context.getResources().getString(R.string.brief_cv_txt)+" "+brief_cv.getNative_name());
        holder.txtlang.setTag(brief_cv);



        holder.edt_cv.setTag(brief_cv);
        holder.edt_cv.setHint(context.getResources().getString(R.string.brief));
        holder.edt_cv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                brief_cv.setBrief_cv(""+s);

            }
        });
    }


    @Override
    public int getItemCount() {

        return brief_cvList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtlang;
        CustomEditText edt_cv;
        public ViewHolder(View view) {
            super(view);
            txtlang = (CustomTextView) view.findViewById(R.id.txtlang);
            edt_cv=(CustomEditText)view.findViewById(R.id.edt_cv);





        }

    }


}
