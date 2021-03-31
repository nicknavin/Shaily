package com.app.session.activity.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.ReqSubscriptionStories;
import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscriptionStories;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.repository.MainRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StoryDetailViewModel  extends AndroidViewModel {
    Context context;
    String userID, accessToken;
    MainRepository mainRepository;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private MutableLiveData<SubscriptionStoriesRoot> subscriptionStoriesMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<Root> rootMutableLiveData=new MutableLiveData<>();
    public int page=1,totalPage=0;

    public StoryDetailViewModel(@NonNull Application application,String id, String token)
    {
        super(application);
        context = application.getApplicationContext();
        userID = id;
        this.accessToken = token;
        mainRepository= MainRepository.getInstance(context);
    }


    public void getSubscriptionsStoriesApi(String subscriptionId)
    {
        ReqSubscriptionStories subscriptionStories=new ReqSubscriptionStories();
        subscriptionStories.setLoad(page);
        subscriptionStories.setSubscriptionId(subscriptionId);
        subscriptionStories.setUser_id(userID);
        Log.d("tag","req "+subscriptionStories.toString());
        compositeDisposable.add(
                mainRepository.getSubscriptionStories(subscriptionStories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SubscriptionStoriesRoot>() {


                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull SubscriptionStoriesRoot subscriptionStoriesRoot)
                    {
                     subscriptionStoriesMutableLiveData.setValue(subscriptionStoriesRoot);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                })
        );
    }


    public MutableLiveData<SubscriptionStoriesRoot> getSubscriptionStoriesMutableLiveData() {
        return subscriptionStoriesMutableLiveData;
    }


    public void deleteStory(SubscriptionStories subscriptionStories)
    {
        ReqDeleteStory reqDeleteStory= new ReqDeleteStory();
        reqDeleteStory.setStoryId(subscriptionStories.get_id());
        reqDeleteStory.setStory_provider(subscriptionStories.getStory_provider());
        compositeDisposable.add(
          mainRepository.deleteStory(reqDeleteStory)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(new DisposableSingleObserver<Root>() {
              @Override
              public void onSuccess(@io.reactivex.annotations.NonNull Root root)
              {
                  rootMutableLiveData.setValue(root);
              }

              @Override
              public void onError(@io.reactivex.annotations.NonNull Throwable e) {

              }
          })
        );

    }

    public MutableLiveData<Root> getRootMutableLiveData() {
        return rootMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
