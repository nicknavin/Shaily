package com.app.session.activity.ui.profile;

import android.app.Application;
import android.content.Context;


import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscriptionGroup;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.repository.MainRepository;
import com.app.session.network.ApiClientProfile;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    public MutableLiveData<ArrayList<SubscriptionGroup>> listMutableLiveData;
    private MutableLiveData<SubscriptionGroupRoot> subscriptionGroupMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<StoryRoot> storyRootMutableLiveData=new MutableLiveData<>();

    Context context;
    boolean flagApi;
    String userID, accessToken;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    MainRepository mainRepository;
    public int page=1,totalPage=0;
    public ProfileViewModel(@NonNull Application application, String id, String token) {
        super(application);
        context = application.getApplicationContext();
        userID = id;
        this.accessToken = token;
        mainRepository=MainRepository.getInstance(context);
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


    public void loadUserSubcriptionGroupApi()
    {
        UserId userId=new UserId();
        userId.setUser_id(userID);
        compositeDisposable.add(
                mainRepository.getUserSubscriptionGroups(userId)
                        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SubscriptionGroupRoot>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull SubscriptionGroupRoot root) {
                        subscriptionGroupMutableLiveData.setValue(root);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                })
        );
    }


    public MutableLiveData<SubscriptionGroupRoot> getSubscriptionGroupMutableLiveData()
    {
        return subscriptionGroupMutableLiveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}