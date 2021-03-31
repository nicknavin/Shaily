package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;

import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.AddressLocation;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class OfficeEditAdapters extends RecyclerView.Adapter<OfficeEditAdapters.ViewHolder> {


    Context context;

    ArrayList<AddressLocation> addressLocationsList = new ArrayList<>();
ApiCallback apiCallback;
    public OfficeEditAdapters(Context context, ArrayList<AddressLocation> list, ApiCallback apiCallback) {
        this.context = context;
        addressLocationsList = list;
        this.apiCallback=apiCallback;
    }


    @Override
    public OfficeEditAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_office, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(OfficeEditAdapters.ViewHolder holder, final int position) {

        final AddressLocation addressLocation = addressLocationsList.get(position);
        holder.txt_adrress.setText(addressLocation.getAddress());
        holder.txt_address_name.setText(addressLocation.getLocation_name());
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallback.result(""+position);

            }
        });
        holder.imgLocation.setTag(addressLocation);

        holder.imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView=(ImageView)v;
//                AddressLocation location=(AddressLocation)v.getTag();
//                Intent intent=new Intent(context, MyLocationActivity.class);
//                intent.putExtra("lat",location.getLatitude());
//                intent.putExtra("lon",location.getLongitude());
//                context.startActivity(intent);
            }
        });




    }


    @Override
    public int getItemCount() {

        return addressLocationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt_adrress, txt_address_name;
        ImageView imgEdit, imgDelete,imgLocation;
        LinearLayout lay;


        public ViewHolder(View view) {
            super(view);
            txt_adrress = (CustomTextView) view.findViewById(R.id.txt_adrress);
            txt_address_name = (CustomTextView) view.findViewById(R.id.txt_address_name);
            imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
            imgLocation = (ImageView) view.findViewById(R.id.imgLocation);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            lay = (LinearLayout) view.findViewById(R.id.lay);


        }

    }


}
