package com.app.session.activity.viewmodel;

import android.app.Application;

import com.app.session.data.model.ReqUnSubscribeAfter;
import com.app.session.data.model.UnDoUnSubscribe;
import com.app.session.data.model.UndoSubscriptionRoot;
import com.app.session.data.model.UnsubscribedRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserSubscriptionGroupsRoot;
import com.app.session.data.repository.MainRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ExpertProfileViewModel extends ViewModel
{
    private MutableLiveData<UserSubscriptionGroupsRoot>groupsRootMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<UnsubscribedRoot> responseBodyMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<UndoSubscriptionRoot> responseUndoUnsubscirbe=new MutableLiveData<>();
    private String id;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    MainRepository mainRepository;
    public ExpertProfileViewModel(String id,String token) {

        this.id=id;
        mainRepository = new MainRepository(token);

    }


    public void loadUserGroup(String userid)
    {
        UserId userId = new UserId();
        userId.setUser_id(userid);
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


    public void loadUserGroupForUnRegister(String userid)
    {
       MainRepository repository=new MainRepository("");
        UserId userId = new UserId();
        userId.setUser_id(userid);
        compositeDisposable.add(
                repository.userSubscriptionGroupsForUnRegister(userId).subscribeOn(Schedulers.io())
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

    public void setGroupsRootMutableLiveData(MutableLiveData<UserSubscriptionGroupsRoot> groupsRootMutableLiveData)
    {
        this.groupsRootMutableLiveData = groupsRootMutableLiveData;
    }

    public MutableLiveData<UnsubscribedRoot> getResponseBodyMutableLiveData() {
        return responseBodyMutableLiveData;
    }

    public void unSubscribeAfter(String following_user_id)
    {
        ReqUnSubscribeAfter reqUnSubscribeAfter=new ReqUnSubscribeAfter();
        reqUnSubscribeAfter.setUser_id(id);
        reqUnSubscribeAfter.setFollowing_user_id(following_user_id);
        compositeDisposable.add(
                mainRepository.unSubscribeAfter24(reqUnSubscribeAfter)
                        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UnsubscribedRoot>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull UnsubscribedRoot responseBody)
                    {
                     responseBodyMutableLiveData.setValue(responseBody);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                })
        );
    }
    public void unDoUnSubscribe24(String following_user_id)
    {

        UnDoUnSubscribe unDoUnSubscribe=new UnDoUnSubscribe();
        unDoUnSubscribe.setUser_id(id);
        unDoUnSubscribe.setUndo_user_id(following_user_id);
        compositeDisposable.add(
                mainRepository.unDoUnSubscribe24(unDoUnSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UndoSubscriptionRoot>() {
                    @Override
                    public void onSuccess(@NonNull UndoSubscriptionRoot responseBody)
                    {
                        responseUndoUnsubscirbe.setValue(responseBody);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
    }

    public MutableLiveData<UndoSubscriptionRoot> getResponseUndoUnsubscirbe() {
        return responseUndoUnsubscirbe;
    }

}
