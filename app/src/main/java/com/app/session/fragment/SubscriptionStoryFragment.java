package com.app.session.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.activity.AddSubscriptionGroupActivity;
import com.app.session.activity.SubscriptionGroupDetialActivity;
import com.app.session.adapter.SubscriptionGroupAdapters;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.interfaces.RequestCallback;
import com.app.session.model.Brief_CV;
import com.app.session.model.SubscriptionGroup;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionStoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionStoryFragment extends BaseFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
View view;
    ArrayList<Brief_CV> brief_cvList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout layAddSubcription;
    FloatingActionButton fabAddStory;
    RecyclerView recyclerView;
    ArrayList<SubscriptionGroup> subscriptionGroupsList = new ArrayList<>();
    SubscriptionGroupAdapters subscriptionGroupAdapters;
    Context context;
    public SubscriptionStoryFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionStoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionStoryFragment newInstance(String param1, String param2) {
        SubscriptionStoryFragment fragment = new SubscriptionStoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }



        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        brief_cvList = (ArrayList<Brief_CV>) bundle.getSerializable("List");

        return inflater.inflate(R.layout.fragment_subscription_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;

        initView(view);
    }

    public void initView(View view)
    {
        layAddSubcription = (LinearLayout) view.findViewById(R.id.layAddSubcription);
        layAddSubcription.setOnClickListener(this);
        fabAddStory = (FloatingActionButton) view.findViewById(R.id.fab);
        fabAddStory.setOnClickListener(this);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onResume() {
        super.onResume();
        callGetSubscriptionGroup();
    }

    private void callGetSubscriptionGroup() {
        if (Utility.isConnectingToInternet(context)) {
            showLoading();
            new BaseAsych(Urls.GET_SUBSCRIPTION_GROUP, getSubcriptionGroup(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        System.out.println("subscription_list" + js.toString());
                        subscriptionGroupsList = new ArrayList<>();
                        JSONObject jsonObject = js.getJSONObject("result");
                        if (jsonObject.getString("rstatus").equals("1")) {

                            if (js.getJSONArray("subscription_group_list") != null) {
                                JSONArray jsonArray = js.getJSONArray("subscription_group_list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    SubscriptionGroup group = new SubscriptionGroup();
                                    group.setCategory_cd(object.getString("category_cd"));
                                    group.setCurrency_cd(object.getString("currency_cd"));
                                    group.setGroup_desc(object.getString("group_desc"));
                                    group.setGroup_introduction_video_url(object.getString("group_introduction_video_url"));
                                    group.setGroup_name(object.getString("group_name"));
                                    group.setGroup_url_image_address(object.getString("group_url_image_address"));
                                    group.setLanguage_cd(object.getString("language_cd"));
                                    group.setSubscription_group_cd(object.getString("subscription_group_cd"));
                                    group.setSubscription_price(object.getDouble("subscription_price"));
                                    group.setUser_cd(object.getString("user_cd"));
                                    subscriptionGroupsList.add(group);
                                }

                                subscriptionGroupAdapters = new SubscriptionGroupAdapters(context, subscriptionGroupsList, new ApiItemCallback() {
                                    @Override
                                    public void result(int position) {
                                        SubscriptionGroup group = subscriptionGroupsList.get(position);
                                        Intent intent = new Intent(context, SubscriptionGroupDetialActivity.class);
                                        intent.putExtra("ID", group.getSubscription_group_cd());
                                        intent.putExtra("NAME", group.getGroup_name());
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("List", (Serializable) brief_cvList);
                                        intent.putExtra("BUNDLE", bundle);
                                        startActivity(intent);
                                    }
                                });

                                recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                recyclerView.setAdapter(subscriptionGroupAdapters);
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    showToast(success);

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    showToast(failed);


                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            }).execute();

        } else {
            showInternetConnectionToast();
        }
    }

    private String getSubcriptionGroup() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_cd", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();


    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fab:
                addStoryGroup();
                break;
            case R.id.layAddSubcription:

                break;
        }
    }


    private void addStoryGroup()
    {
        Intent intent = new Intent(context, AddSubscriptionGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", (Serializable) brief_cvList);
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);
    }

}
