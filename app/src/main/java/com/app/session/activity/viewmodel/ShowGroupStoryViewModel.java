package com.app.session.activity.viewmodel;

import android.app.Application;
import android.content.Context;

import com.app.session.data.model.RequestReadCount;
import com.app.session.data.repository.MainRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ShowGroupStoryViewModel extends ViewModel
{
    Context context;
    String userID;
    MainRepository mainRepository;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    MutableLiveData<ResponseBody> responseBodyMutableLiveData=new MutableLiveData<>();
   public RequestReadCount readCount;
    public ShowGroupStoryViewModel(String id,String token)
    {


        userID = id;
mainRepository=new MainRepository(token);
        readCount=new RequestReadCount();
    }

    public void readViewCount(RequestReadCount requestReadCount)
    {

        compositeDisposable.add(
                mainRepository.readCount(requestReadCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull ResponseBody responseBody)
                    {
                        responseBodyMutableLiveData.setValue(responseBody);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public RequestReadCount getReadCount() {
        return readCount;
    }

    public void setReadCount(RequestReadCount readCount) {
        this.readCount = readCount;
    }
}
