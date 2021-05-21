package com.app.session.activity.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.app.session.data.model.ReqDeleteGroupStory;
import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.ReqSubscriptionStories;
import com.app.session.data.model.ReqUserStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.StoryRoot;
import com.app.session.data.model.SubscriptionStories;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.data.repository.MainRepository;
import com.app.session.utility.Utility;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StoryDetailViewModel  extends ViewModel {
    Context context;
    String userID;
    MainRepository mainRepository;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private MutableLiveData<SubscriptionStoriesRoot> subscriptionStoriesMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<Root> rootMutableLiveData=new MutableLiveData<>();
    public int page=1,totalPage=0;
   //public UserSubscriptionGroupsBody userSubscriptionGroupsBody;
   public MutableLiveData<UserSubscriptionGroupsBody> userSubscriptionGroupsBody=new MutableLiveData<>();

    public StoryDetailViewModel(String id,String token)
    {
        userID = id;

        mainRepository= new MainRepository(token);
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
        ReqDeleteGroupStory reqDeleteStory= new ReqDeleteGroupStory();
        reqDeleteStory.setStoryId(subscriptionStories.get_id());
        reqDeleteStory.setSubscription_id(subscriptionStories.getSubscriptionId().get_id());
        reqDeleteStory.setStory_provider(subscriptionStories.getStory_provider());
        Utility.log(reqDeleteStory.toString());
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

    public MutableLiveData<UserSubscriptionGroupsBody> getUserSubscriptionGroupsBody() {
        return userSubscriptionGroupsBody;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
