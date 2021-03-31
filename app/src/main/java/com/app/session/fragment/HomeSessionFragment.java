package com.app.session.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.app.session.R;
import com.app.session.activity.ExpertProfilePageActivity;
import com.app.session.activity.LoginActivity;
import com.app.session.activity.SelectLanguageYouSpeakActivity;
import com.app.session.adapter.AllUserAdapter;
import com.app.session.baseFragment.BaseFragment;
import com.app.session.customview.CustomEditText;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.AllUserBody;
import com.app.session.data.model.AllUserRoot;
import com.app.session.data.model.ConsultUser;
import com.app.session.data.model.ConsultUserRoot;
import com.app.session.data.model.ConsultantData;
import com.app.session.data.model.LanguageCd;
import com.app.session.data.model.SearchUser;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Utility;

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
 * Use the {@link HomeSessionFragment#newInstance} factory method toL
 * create an instance of this fragment.
 */
public class HomeSessionFragment extends BaseFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
       View view;
     String category_cd="";
    CustomEditText edt_search;
    String key;
    ImageView imgMenu,imgBack;
    public HomeSessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment HomeStoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeSessionFragment newInstance() {
        HomeSessionFragment fragment = new HomeSessionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_story, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        initView();
        getAllUser();
    }

    private void initView()
    {
        imgMenu=(ImageView)view.findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(this);
        imgBack=(ImageView)view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        edt_search = (CustomEditText)view.findViewById(R.id.edt_search);


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                key = s.toString().trim();
                if(key.isEmpty())
                {
                    getAllUser();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    searchUser(key);
                    return true;

                }
                return false;
            }
        });

    }




    private void getAllUser()
    {
        if (Utility.isConnectingToInternet(getContext()))
        {
            showLoading();
            LanguageCd languageCd=new LanguageCd();
            ArrayList<String> list=new ArrayList<>();
            list.add("en");
            languageCd.setLanguageCd(list);
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<ConsultUserRoot> call = apiInterface.reqGetAllUserByMachineLang(accessToken, languageCd);
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
                           AllUserAdapter allUserAdapter=new AllUserAdapter(getContext(),allUserBodyArrayList, new ApiItemCallback() {
                               @Override
                               public void result(int position)
                               {
                                   ConsultUser allUserBody=  allUserBodyArrayList.get(position);
                                   Intent intent=new Intent(getContext(), ExpertProfilePageActivity.class);
                                   intent.putExtra("ID",allUserBody.getId());
                                   intent.putExtra("TYPE_REGISTER",0);
                                   startActivity(intent);
                               }
                           });
                        recyclerView.setAdapter(allUserAdapter);


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
                            AllUserAdapter allUserAdapter=new AllUserAdapter(getContext(),allUserBodyArrayList, new ApiItemCallback() {
                                @Override
                                public void result(int position)
                                {
                                    ConsultUser allUserBody=  allUserBodyArrayList.get(position);
                                    Intent intent=new Intent(getContext(), ExpertProfilePageActivity.class);
                                    intent.putExtra("ID",allUserBody.getId());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(allUserAdapter);


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


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgMenu:
                showMenu(view);
                break;

            case R.id.imgBack:
                getActivity().finish();
                break;

        }
    }

    public void showMenu(View v) {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();

            inflater.inflate(R.menu.menu_login, popup.getMenu());



        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {
        Intent intent;
        switch (menuItem.getItemId()) {

            case R.id.menu_login:
               intent=new Intent(getContext(), LoginActivity.class);
               startActivity(intent);
                return true;
            case R.id.menu_account:
                intent=new Intent(getContext(), SelectLanguageYouSpeakActivity.class);
                startActivity(intent);
                return true;

        }

        return false;
    }




}
