package com.app.session.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.activity.ExpertProfilePageActivity;
import com.app.session.adapter.DemoAdapter;
import com.app.session.api.Urls;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.ConsultUser;
import com.app.session.data.model.ConsultUserRoot;
import com.app.session.data.model.ConsultantData;
import com.app.session.data.model.ReqAllUser;
import com.app.session.data.model.SearchUser;
import com.app.session.data.model.UserLangauges;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllStoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllStoryFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
DemoAdapter demoAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    View view;
    String category_cd="";
    ArrayList<ConsultantData> consultantDataArrayList=new ArrayList<>();
    public AllStoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment HomeStoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllStoryFragment newInstance(String param1) {
        AllStoryFragment fragment = new AllStoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_cd = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_story_new, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        initView();
        getAllUser();
    }
ArrayList<String> arrayList=new ArrayList<>();
    private void initView()
    {
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }
    public void getFilter(String key)
    {
//        if(demoAdapter!=null) {
//            demoAdapter.getFilter().filter(key);
//        }

        searchUser(key);

    }

   public ArrayList<ConsultUser> consultUserArrayList =new ArrayList<>();

    public void getAllUser() {
        if (isInternetConnected()) {
            showLoading();
            ReqAllUser reqAllUser=new ReqAllUser();
            ArrayList<String> list=new ArrayList<>();
            ArrayList<UserLangauges> userLangauges= DataPrefrence.getLanguageDb(getContext(),Constant.LANG_DB);
            for(int i=0;i<userLangauges.size();i++)
            {
                list.add(userLangauges.get(i).get_id());
            }
            reqAllUser.setLanguageId(list);
            ApiInterface apiInterface= ApiClientExplore.getClient().create(ApiInterface.class);
       Call<ConsultUserRoot> call= apiInterface.reqGetAllUsers(accessToken,reqAllUser);
     call.enqueue(new Callback<ConsultUserRoot>() {
         @Override
         public void onResponse(Call<ConsultUserRoot> call, Response<ConsultUserRoot> response)
         {
             dismiss_loading();
             if(response.body()!=null)
             {
                 if(response.body().getStatus()==200)
                 {
                    consultUserArrayList=response.body().getConsultUsersList();
                     System.out.println("size "+consultUserArrayList.size());
//                     for(Iterator<ConsultUser> iterator = consultUserArrayList.iterator(); iterator.hasNext(); ) {
//                         if(iterator.next().getId().equals(userId))
//                             iterator.remove();
//                     }
                     demoAdapter=new DemoAdapter(getContext(), userId,consultUserArrayList, new ApiItemCallback() {
                         @Override
                         public void result(int position)
                         {
                             Intent intent=new Intent(getContext(), ExpertProfilePageActivity.class);
                             intent.putExtra("ID",consultUserArrayList.get(position).getId());
                             intent.putExtra("NAME",consultUserArrayList.get(position).getLoginUserId());
                             intent.putExtra("URL", Urls.BASE_IMAGES_URL +consultUserArrayList.get(position).getImageUrl());
                             startActivity(intent);
                         }
                     });
                     recyclerView.setAdapter(demoAdapter);
                 }
             }
         }

         @Override
         public void onFailure(Call<ConsultUserRoot> call, Throwable t) {
dismiss_loading();
         }
     });

        } else {
            showInternetConnectionToast();
        }
    }
    private String getConsultantCatgparam() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_cd", userId);
            jsonObject.put("language_cd", "en");

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public interface SearchStory
    {
        public void searchKey();
    }



    private void searchUser(String key)
    {
        if (Utility.isConnectingToInternet(getContext()))
        {
            showLoading();
            SearchUser searchUser=new SearchUser();
            searchUser.setSearchUser(key);
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<ConsultUserRoot> call = apiInterface.reqSearchUser(searchUser);
            call.enqueue(new Callback<ConsultUserRoot>() {
                @Override
                public void onResponse(Call<ConsultUserRoot> call, Response<ConsultUserRoot> response)
                {
                    dismiss_loading();
                    if(response.body()!=null)
                    {
                        ConsultUserRoot allUserRoot =response.body();
                        if(allUserRoot.getStatus()==200)
                        {
                            ArrayList<ConsultUser> allUserBodyArrayList=   allUserRoot.getConsultUsersList();
                            demoAdapter=new DemoAdapter(getContext(), userId,allUserBodyArrayList, new ApiItemCallback() {
                                @Override
                                public void result(int position)
                                {
                                    Intent intent=new Intent(getContext(), ExpertProfilePageActivity.class);
                                    intent.putExtra("ID",consultUserArrayList.get(position).getId());
                                    intent.putExtra("NAME",consultUserArrayList.get(position).getLoginUserId());
                                    intent.putExtra("URL", Urls.BASE_IMAGES_URL +consultUserArrayList.get(position).getImageUrl());
                                    startActivity(intent);
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(demoAdapter);


                        }
                    }
                }

                @Override
                public void onFailure(Call<ConsultUserRoot> call, Throwable t)
                {
                    dismiss_loading();
                }
            });





        } else {
            showInternetConnectionToast();
        }
    }









}
