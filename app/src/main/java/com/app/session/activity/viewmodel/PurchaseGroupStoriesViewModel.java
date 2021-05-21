package com.app.session.activity.viewmodel;

import android.util.Log;

import com.app.session.data.model.PurchaseSubscribeGroup;
import com.app.session.data.model.ReqSubscriptionStories;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.repository.MainRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PurchaseGroupStoriesViewModel extends ViewModel
{

    public String id,subscriptionId="",groupName="",groupiconUrl="";

    private MutableLiveData<PurchaseSubscribeGroup>purchaseSubscribeGroupMutableLiveData=new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    MainRepository mainRepository;
    public int page=1,totalPage=0,storyPosition=-1;

    private MutableLiveData<SubscriptionStoriesRoot> subscriptionStoriesMutableLiveData=new MutableLiveData<>();

    public PurchaseGroupStoriesViewModel(String id,String token)
    {
        this.id=id;
        mainRepository = new MainRepository(token);
    }









    public void getSubscriptionsStoriesApi()
    {
        ReqSubscriptionStories subscriptionStories=new ReqSubscriptionStories();
        subscriptionStories.setLoad(page);
        subscriptionStories.setSubscriptionId(getPurchaseSubscribeGroupMutableLiveData().getValue().getSubscriptionIdData().get_id());
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

    public MutableLiveData<PurchaseSubscribeGroup> getPurchaseSubscribeGroupMutableLiveData() {
        return purchaseSubscribeGroupMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
