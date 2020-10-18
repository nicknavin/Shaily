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
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.model.ChatUserId;
import com.app.session.model.ChatedBody;
import com.app.session.model.ChatedPersonsBody;
import com.app.session.model.ChatedPersonsResponse;
import com.app.session.model.ClearChat;
import com.app.session.model.Root;
import com.app.session.model.UserId;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Utility;

import java.util.ArrayList;

public class ChatUserActivity extends BaseActivity
{
    ArrayList<ChatedBody> chatedPersonsList =new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AllChatUserAdapter allChatUserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);
        initView();
        getChatedPersons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChatedPersons();
    }

    private void initView()
    {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

    }


    public void getChatedPersons()
    {
        if (Utility.isConnectingToInternet(context))
        {
            ChatUserId data=new ChatUserId();
            data.setUserId(userId);

            ApiInterface apiInterface = ApiClient.getClient(Urls.PRIVATEMESSAGE_URL).create(ApiInterface.class);
           Call<ChatedPersonsResponse> call= apiInterface.reqChatedPersons(accessToken,data);
           call.enqueue(new Callback<ChatedPersonsResponse>()
           {
               @Override
               public void onResponse(Call<ChatedPersonsResponse> call, Response<ChatedPersonsResponse> response)
               {

                   if(response.body().getStatus()==200)
                   {
                       ArrayList<ChatedBody> chatedPersonsList =response.body().getChatedPersonsBody();
                       allChatUserAdapter =new AllChatUserAdapter(context, chatedPersonsList, userId, new ApiItemCallback() {
                           @Override
                           public void result(int position)
                           {
                               ChatedBody chatedBody=chatedPersonsList.get(position);
                               String id="",url="",name="";
                               if(userId.equals(chatedBody.getChatedPersonsBody().getSenderId()))
                               {
                                   id=chatedBody.getChatedPersonsBody().getReciverId();
                                   name=chatedBody.getChatedPersonsBody().getReciverName();
                                   url=chatedBody.getChatedPersonsBody().getReciverProfileUrl();
                               }
                               else
                               {
                                   id=chatedBody.getChatedPersonsBody().getSenderId();
                                   name=chatedBody.getChatedPersonsBody().getSenderName();
                                   url=chatedBody.getChatedPersonsBody().getSenderProfileUrl();
                               }
                               Intent intent=new Intent(context,ChattingActivity.class);
                               intent.putExtra("ID",id);
                               intent.putExtra("NAME",name);
                               intent.putExtra("URL",url);
                               startActivity(intent);

                           }
                       });
                       recyclerView.setAdapter(allChatUserAdapter);
                   }
               }

               @Override
               public void onFailure(Call<ChatedPersonsResponse> call, Throwable t)
               {

               }
           });


        } else {
            showInternetConnectionToast();
        }
    }


}