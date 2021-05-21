package com.app.session.activity.viewmodel;

import android.app.Application;
import android.util.Log;

import com.app.session.data.model.ReqPurchaseSubscriptionGroups;
import com.app.session.data.model.ReqSubscriptionStories;
import com.app.session.data.model.Root;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.data.model.UserSubscriptionGroupsRoot;
import com.app.session.data.repository.MainRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class OtherUserSubscripionGroupViewModel extends ViewModel
{

    public String id,subscriptionId="",groupName="",groupiconUrl="";

    private MutableLiveData<UserSubscriptionGroupsBody>userSubscriptionGroups=new MutableLiveData<>();
    private MutableLiveData<Root> rootMutableLiveData=new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    MainRepository mainRepository;
    public int page=1,totalPage=0,storyPosition=-1;

    private MutableLiveData<SubscriptionStoriesRoot> subscriptionStoriesMutableLiveData=new MutableLiveData<>();
    public boolean isPurchased;
    public OtherUserSubscripionGroupViewModel(String id,String token) {

        this.id=id;
        mainRepository = new MainRepository(token);
    }



    public MutableLiveData<UserSubscriptionGroupsBody> getUserSubscriptionGroups() {
        return userSubscriptionGroups;
    }

    public void setUserSubscriptionGroups(MutableLiveData<UserSubscriptionGroupsBody> userSubscriptionGroups) {
        this.userSubscriptionGroups = userSubscriptionGroups;
    }



    public void purchaseSubsciptionGroup()
    {

        ReqPurchaseSubscriptionGroups reqPurchaseSubscriptionGroups=new ReqPurchaseSubscriptionGroups();
        reqPurchaseSubscriptionGroups.setUser_id(id);
        reqPurchaseSubscriptionGroups.setSubscription_id(userSubscriptionGroups.getValue().get_id());
        compositeDisposable.add(
                mainRepository.purchaseSubscribeGroup(reqPurchaseSubscriptionGroups)
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
    public void getSubscriptionsStoriesApi()
    {
        ReqSubscriptionStories subscriptionStories=new ReqSubscriptionStories();
        subscriptionStories.setLoad(page);
        subscriptionStories.setSubscriptionId(subscriptionId);
        subscriptionStories.setUser_id(id);
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


}
