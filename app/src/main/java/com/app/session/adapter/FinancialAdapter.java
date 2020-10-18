package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.model.Financial;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class FinancialAdapter extends RecyclerView.Adapter<FinancialAdapter.ViewHolder> {


    Context context;
    private ArrayList<Financial> brief_cvList = new ArrayList<>();

    public FinancialAdapter(Context context, ArrayList<Financial> list) {
        this.context = context;
        brief_cvList = list;

    }


    @Override
    public FinancialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_financial, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }

//addTextChangedListener
    @Override
    public void onBindViewHolder(FinancialAdapter.ViewHolder holder, final int position) {

        Financial financial=brief_cvList.get(position);
        holder.txt_date.setText(financial.getDate());
        holder.txt_amount.setText(financial.getAmount());
        holder.txt_userName.setText(financial.getUser_name());

    }


    @Override
    public int getItemCount() {

        return brief_cvList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt_userName,txt_date,txt_amount;
        CustomTextView edt_cv;
        public ViewHolder(View view) {
            super(view);
            txt_userName = (CustomTextView) view.findViewById(R.id.txt_userName);
            txt_date = (CustomTextView) view.findViewById(R.id.txt_date);
            txt_amount=(CustomTextView)view.findViewById(R.id.txt_amount);
        }

    }


}
