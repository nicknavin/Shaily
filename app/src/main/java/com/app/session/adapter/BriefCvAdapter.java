package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.Brief_CV;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class BriefCvAdapter extends RecyclerView.Adapter<BriefCvAdapter.ViewHolder> {


    Context context;
    private ArrayList<Brief_CV> brief_cvList = new ArrayList<>();

    public BriefCvAdapter(Context context, ArrayList<Brief_CV> list) {
        this.context = context;
        brief_cvList = list;

    }


    @Override
    public BriefCvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_brief_cv, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }

//addTextChangedListener
    @Override
    public void onBindViewHolder(BriefCvAdapter.ViewHolder holder, final int position) {
        final Brief_CV brief_cv= brief_cvList.get(position);
        holder.txtlang.setText("Brief in "+brief_cv.getNative_name());
        holder.edt_cv.setText(brief_cv.getBrief_cv());
        holder.edt_cv.setTag(brief_cv);

    }


    @Override
    public int getItemCount() {

        return brief_cvList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtlang;
        CustomTextView edt_cv;
        public ViewHolder(View view) {
            super(view);
            txtlang = (CustomTextView) view.findViewById(R.id.txtlang);
            edt_cv=(CustomTextView)view.findViewById(R.id.edt_cv);





        }

    }


}
