package com.app.session.activity.ui.expert;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.app.session.R;
import com.app.session.activity.FollowingExpertProfilePageActivity;
import com.app.session.adapter.ExpertFollowingsAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.model.BodyFollowers;
import com.app.session.model.RootFollowers;
import com.app.session.model.UserId;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Utility;

import java.util.ArrayList;


public class ExpertFragment extends BaseFragment {

    private ExpertViewModel dashboardViewModel;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<BodyFollowers> followersArrayList=new ArrayList<>();
    ExpertFollowingsAdapter expertFollowingsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(ExpertViewModel.class);
        View root = inflater.inflate(R.layout.fragment_expert, container, false);
initView(root);
        return root;
    }


    private void initView(View view)
    {
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        getFollowingUser();
    }



    public void getFollowingUser()
    {
        if (Utility.isConnectingToInternet(getContext()))
        {
            showLoading();
            UserId data=new UserId();
            data.setUser_id(userId);

            ApiInterface apiInterface = ApiClient.getClient(Urls.USERPROFILE_URL).create(ApiInterface.class);
            Call<RootFollowers> call= apiInterface.reqFollowers(accessToken,data);
            call.enqueue(new Callback<RootFollowers>() {
                @Override
                public void onResponse(Call<RootFollowers> call, Response<RootFollowers> response)
                {
                    dismiss_loading();
                    if (response.body()!=null) {
                        if(response.body().getStatus()==200)
                        {
                            RootFollowers rootFollowers=response.body();

                            followersArrayList=rootFollowers.getFollowerUsers();
                            expertFollowingsAdapter=new ExpertFollowingsAdapter(getContext(), followersArrayList, new ApiItemCallback()
                            {
                                @Override
                                public void result(int position)
                                {
                                    BodyFollowers followers=followersArrayList.get(position);
                                    Intent intent=new Intent(getContext(), FollowingExpertProfilePageActivity.class);
                                    intent.putExtra("ID",followers.getFollowerUsers().get_id());
                                    intent.putExtra("NAME",followers.getFollowerUsers().getLogin_user_id());
                                    intent.putExtra("URL", Urls.BASE_IMAGES_URL +followers.getFollowerUsers().getImageUrl());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(expertFollowingsAdapter);
                        }
                    }

                }

                @Override
                public void onFailure(Call<RootFollowers> call, Throwable t) {

                }
            });


        } else {
            showInternetConnectionToast();
        }
    }










}