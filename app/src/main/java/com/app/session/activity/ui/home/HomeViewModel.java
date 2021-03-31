package com.app.session.activity.ui.home;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.app.session.data.model.StoriesFollowingUsers;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserRoot;
import com.app.session.data.repository.MainRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<SubscribedAllStroiesRoot>subscribedAllStroiesRootMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<SubscribedAllStroiesRoot>unSubscribedAllStroiesRootMutableLiveData=new MutableLiveData<>();

    private MutableLiveData<SubscriptionGroupRoot>subscriptionGroupRootMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<UserRoot> userRootMutableLiveData=new MutableLiveData<>();

    Context context;
    String userid,accessToken;
    MainRepository mainRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
   public int page=1,totalPage=0,stories=0;
   public int unsubscribePage=1,unsubscribeTotalPage=0;

    public HomeViewModel(@NonNull Application application, String id, String token)
    {
        super(application);
        context=application.getApplicationContext();
        userid =id;
        this.accessToken=token;
        mainRepository=MainRepository.getInstance(context);
    }



    public void getUserDetailApi()
    {

        UserId userId=new UserId();
        userId.setUser_id(userid);

        mainRepository.getUserDetails(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserRoot>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull UserRoot userRoot)
                    {
                        userRootMutableLiveData.setValue(userRoot);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    //<editor-fold desc="getSubscriptionGroup">
    public void getSubscriptinGroupApi()
    {
        mainRepository=MainRepository.getInstance(context);
        UserId user=new UserId();
        user.setUser_id(userid);

        compositeDisposable.add(
                mainRepository.getSubscribedSubscriptionGroups(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SubscriptionGroupRoot>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull SubscriptionGroupRoot subscriptionGroupRoot)
                    {
                        subscriptionGroupRootMutableLiveData.setValue(subscriptionGroupRoot);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                })

        );
    }

    //</editor-fold>

    //<editor-fold desc="Get Story of Following Story ">
    public void getFollowingUserStoryApi()
    {
        mainRepository=MainRepository.getInstance(context);
        StoriesFollowingUsers storiesFollowingUsers=new StoriesFollowingUsers();
        storiesFollowingUsers.setUser_id(userid);
        storiesFollowingUsers.setLoad(page);
        compositeDisposable.add(
                mainRepository.getStoriesOFFollowingUser(storiesFollowingUsers)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).
                        subscribeWith(
                                new DisposableSingleObserver<SubscribedAllStroiesRoot>()
                                {
                                    @Override
                                    public void onSuccess(@io.reactivex.annotations.NonNull SubscribedAllStroiesRoot subscribedAllStroiesRoot)
                                    {
                                        subscribedAllStroiesRootMutableLiveData.setValue(subscribedAllStroiesRoot);
                                    }

                                    @Override
                                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                        Log.d("TAG",e.toString());
                                    }
                                }
                        ));
    }
    //</editor-fold>

    public void getUnFollowingUserStoryApi()
    {
        mainRepository=MainRepository.getInstance(context);
        StoriesFollowingUsers storiesFollowingUsers=new StoriesFollowingUsers();
        storiesFollowingUsers.setUser_id(userid);
        storiesFollowingUsers.setLoad(unsubscribePage);
        compositeDisposable.add(
                mainRepository.getStoriesOFuNFollowingUser(storiesFollowingUsers)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).
                        subscribeWith(
                                new DisposableSingleObserver<SubscribedAllStroiesRoot>()
                                {
                                    @Override
                                    public void onSuccess(@io.reactivex.annotations.NonNull SubscribedAllStroiesRoot subscribedAllStroiesRoot)
                                    {
                                        unSubscribedAllStroiesRootMutableLiveData.setValue(subscribedAllStroiesRoot);
                                    }

                                    @Override
                                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                        Log.d("TAG",e.toString());
                                    }
                                }
                        ));
    }







    public MutableLiveData<SubscribedAllStroiesRoot> getSubscriptionGroupStoryData()
    {
        return subscribedAllStroiesRootMutableLiveData;
    }
   public MutableLiveData<SubscribedAllStroiesRoot> getUnSubscriptionGroupStoryData()
    {
        return unSubscribedAllStroiesRootMutableLiveData;
    }


//


    public MutableLiveData<SubscriptionGroupRoot> getSubscriptionGroupRootMutableLiveData() {
        return subscriptionGroupRootMutableLiveData;
    }

    public MutableLiveData<SubscribedAllStroiesRoot> getStoriesofAllUserMutData()
    {
        return  unSubscribedAllStroiesRootMutableLiveData;
    }

    public MutableLiveData<UserRoot> getMutableLiveUserDetailData()
    {
        return userRootMutableLiveData;
    }





    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}