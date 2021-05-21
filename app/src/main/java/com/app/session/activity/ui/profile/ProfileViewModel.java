package com.app.session.activity.ui.profile;

import android.app.Application;
import android.content.Context;


import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserSubscriptionGroupsRoot;
import com.app.session.data.repository.MainRepository;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public MutableLiveData<ArrayList<SubscriptionGroup>> listMutableLiveData;
    private MutableLiveData<UserSubscriptionGroupsRoot> subscriptionGroupMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<StoryRoot> storyRootMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<UserSubscriptionGroupsRoot>groupsRootMutableLiveData=new MutableLiveData<>();

    boolean flagApi;
    String userID;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    MainRepository mainRepository;
    public int page=1,totalPage=0;

    public ProfileViewModel(String id,String token)
    {
        userID = id;

        mainRepository=new MainRepository(token);
        System.out.println("userID : " + userID);


    }




    public void getUserStoriesApi()
    {
        ReqUserStory reqUserStory=new ReqUserStory();
        reqUserStory.setmUserId(userID);
        reqUserStory.setmLoad(""+page);
        compositeDisposable.add(
                mainRepository.getUserStory(reqUserStory).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<StoryRoot>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull StoryRoot storyRoot)
                    {
                        storyRootMutableLiveData.setValue(storyRoot);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                })
        );

    }

    public MutableLiveData<StoryRoot> getStoryRootMutableLiveData()
    {
        return storyRootMutableLiveData;
    }


    public void loadUserGroup()
    {
        UserId userId = new UserId();
        userId.setUser_id(userID);
        compositeDisposable.add(
                mainRepository.getOtherSubscriptionGroups(userId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<UserSubscriptionGroupsRoot>() {
                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull UserSubscriptionGroupsRoot userSubscriptionGroupsRoot)
                            {
                                groupsRootMutableLiveData.setValue(userSubscriptionGroupsRoot);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            }
                        })
        );
    }
    public MutableLiveData<UserSubscriptionGroupsRoot> getGroupsRootMutableLiveData()
    {
        return groupsRootMutableLiveData;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}