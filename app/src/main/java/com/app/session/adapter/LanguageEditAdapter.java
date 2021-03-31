package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.Brief_CV;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class LanguageEditAdapter extends RecyclerView.Adapter<LanguageEditAdapter.ViewHolder> {


    Context context;
    ArrayList<Brief_CV> briefCvArrayList;
    BaseActivity baseActivity;
    ApiCallback apiCallback;
    ObjectCallback objectCallback;
boolean flag;

    public LanguageEditAdapter(Context context, ArrayList<Brief_CV> list, ApiCallback callback) {
        this.context = context;
        briefCvArrayList = list;
        baseActivity = (BaseActivity) context;
        apiCallback = callback;


    }
public LanguageEditAdapter(Context context, ArrayList<Brief_CV> list, boolean flag, ApiCallback callback) {
        this.context = context;
        briefCvArrayList = list;
        baseActivity = (BaseActivity) context;
        apiCallback = callback;
        this.flag=flag;


    }


    public LanguageEditAdapter(Context context, ArrayList<Brief_CV> list, boolean flag, ObjectCallback callback) {
        this.context = context;
        briefCvArrayList = list;
        baseActivity = (BaseActivity) context;
        this.objectCallback = callback;
        this.flag=flag;


    }



    @Override
    public LanguageEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_language, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(LanguageEditAdapter.ViewHolder holder, final int position) {
        Brief_CV language = briefCvArrayList.get(position);
        holder.edt_selectLang.setText(language.getLanguage_id().getName());

        if(flag)
        {
            holder.imgCross.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgCross.setVisibility(View.GONE);
        }


//        if (position == 0) {
//            holder.imgCross.setVisibility(View.GONE);
//        } else {
//            holder.imgCross.setVisibility(View.VISIBLE);
//        }
//
//        if (language.getBrief_cv() != null) {
//            if (!language.getBrief_cv().isEmpty()) {
//                holder.imgCross.setVisibility(View.GONE);
//            } else {
//                holder.imgCross.setVisibility(View.VISIBLE);
//            }
//        } else {
//            holder.imgCross.setVisibility(View.VISIBLE);
//        }

        holder.imgCross.setTag(language);
        holder.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = (ImageView) v;
                Brief_CV lang = (Brief_CV) imageView.getTag();

                objectCallback.getObject(lang,position,v);

//                if (baseActivity.isConnectingToInternet(context)) {
//                    callRemoveLang(getParamRemoveLang(lang.getLanguage_cd()), Urls.REMOVE_LANGUAGE, lang, position);
//                } else {
//                    baseActivity.showInternetConnectionToast();
//                }
            }
        });

    }


    @Override
    public int getItemCount() {

        return briefCvArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView edt_selectLang;


        ImageView imgCross;


        public ViewHolder(View view) {
            super(view);
            edt_selectLang = (CustomTextView) view.findViewById(R.id.edt_selectLang);


            imgCross = (ImageView) view.findViewById(R.id.imgCross);


        }

    }


    public void refreshList(Brief_CV lang,int position)
    {
        baseActivity.dismiss_loading();
        briefCvArrayList.remove(lang);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, briefCvArrayList.size());

    }


    /*..............................................................................*/

    private void callRemoveLang(Map<String, Object> param, String url, final Brief_CV lang, final int position) {

        try {
            baseActivity.showLoading();
            AqueryCall request = new AqueryCall(baseActivity);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    baseActivity.dismiss_loading();
                    briefCvArrayList.remove(lang);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, briefCvArrayList.size());
                    apiCallback.result("");


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    baseActivity.dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        baseActivity.unAuthorized();

                    } else {

                    }
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    baseActivity.dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    baseActivity.dismiss_loading();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamRemoveLang(String language_cd) {
        Map<String, Object> params = new HashMap<>();


        params.put("user_cd", baseActivity.userId);
        params.put("token_id", baseActivity.accessToken);
        params.put("language_cd", language_cd);


        return params;
    }

    /*..............................................................................*/


}
