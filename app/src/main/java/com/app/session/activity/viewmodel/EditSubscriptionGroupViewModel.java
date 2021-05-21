package com.app.session.activity.viewmodel;

import android.app.Application;
import android.util.Log;

import com.app.session.data.model.ReqDeleteStory;
import com.app.session.data.model.Root;
import com.app.session.data.model.SubscriptionStories;
import com.app.session.data.model.UpdateSubscriptionGroupDetail;
import com.app.session.data.model.UserLangauges;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.data.repository.MainRepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EditSubscriptionGroupViewModel extends ViewModel {

    private String id;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Root> rootMutableLiveData=new MutableLiveData<>();
    MainRepository mainRepository;
    ArrayList<UserLangauges> langaugesArrayList;

   private MutableLiveData<UserSubscriptionGroupsBody>userSubscriptionGroups=new MutableLiveData<>();

    public EditSubscriptionGroupViewModel(String id,String token) {

        mainRepository = new MainRepository(token);

    }


    public MutableLiveData<UserSubscriptionGroupsBody> getUserSubscriptionGroups() {
        return userSubscriptionGroups;
    }

    public void updateSubscriptionGroupDetail(UpdateSubscriptionGroupDetail groupDetail)
    {

        compositeDisposable.add(
                mainRepository.updateSubscriptionGroup(groupDetail)
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


    public Observable<UserLangauges>getSelectedLangObservable()
    {
        ArrayList<UserLangauges> userLangauges =langaugesArrayList;
        return Observable.create(new ObservableOnSubscribe<UserLangauges>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<UserLangauges> emitter) throws Exception
            {
                for (UserLangauges note : userLangauges) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }
    public DisposableObserver<UserLangauges> getUserLangaugesObserver() {
        return new DisposableObserver<UserLangauges>() {

            @Override
            public void onNext(UserLangauges note) {
                Log.d("TAG", "Note: " + note.getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("TAG", "All notes are emitted!");
            }
        };
    }

    public void getSelectedLanguageIndex()
    {
        compositeDisposable.add(getSelectedLangObservable().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

                .subscribeWith(getUserLangaugesObserver())
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
