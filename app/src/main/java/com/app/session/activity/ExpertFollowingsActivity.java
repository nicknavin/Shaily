package com.app.session.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;

import com.app.session.R;
import com.app.session.adapter.AllChatUserAdapter;
import com.app.session.adapter.ExpertFollowingsAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.model.BodyFollowers;
import com.app.session.model.ChatUserId;
import com.app.session.model.ChatedBody;
import com.app.session.model.ChatedPersonsResponse;
import com.app.session.model.RootFollowers;
import com.app.session.model.UserId;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Utility;

import java.util.ArrayList;

public class ExpertFollowingsActivity extends BaseActivity
{
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<BodyFollowers> followersArrayList=new ArrayList<>();
    ExpertFollowingsAdapter expertFollowingsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_followings);
        initView();
    }

    private void initView()
    {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFollowingUser();
    }

    public void getFollowingUser()
    {
        if (Utility.isConnectingToInternet(context))
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
                            expertFollowingsAdapter=new ExpertFollowingsAdapter(context, followersArrayList, new ApiItemCallback()
                            {
                                @Override
                                public void result(int position)
                                {
                                    BodyFollowers followers=followersArrayList.get(position);
                                    Intent intent=new Intent(context, FollowingExpertProfilePageActivity.class);
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