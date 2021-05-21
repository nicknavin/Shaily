package com.app.session.activity.viewmodel;

import android.util.Log;

import com.app.session.data.model.RequestReadCount;
import com.app.session.data.model.RequestReadCountNormalStory;
import com.app.session.data.model.StoryId;
import com.app.session.data.model.UserStory;
import com.app.session.data.repository.MainRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class StoryPageDetailViewModel extends ViewModel
{
    String id;
    MainRepository mainRepository;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    private RequestReadCount requestReadCount=new RequestReadCount();
    private RequestReadCountNormalStory requestReadCountNormalStory=new RequestReadCountNormalStory();
    private UserStory storyData=new UserStory();

    MutableLiveData<ResponseBody> responseBodyMutableLiveData=new MutableLiveData<>();
    public StoryPageDetailViewModel(String id,String token)
    {
        this.id =id;
        mainRepository=new MainRepository(token);

    }

    public void sendReadViewCount()
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
    public void sendReadViewCountNormalStory()
    {
RequestReadCountNormalStory requestReadCountNormalStory=getRequestReadCountNormalStory();
        Log.d("TAG", "sendReadViewCountNormalStory: "+requestReadCountNormalStory.toString());
        compositeDisposable.add(
                mainRepository.readCountNormalStory(requestReadCountNormalStory)
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




    public UserStory getStoryData() {
        return storyData;
    }

    public RequestReadCount getRequestReadCount() {
        return requestReadCount;
    }

    public void setRequestReadCount(RequestReadCount requestReadCount) {
        this.requestReadCount = requestReadCount;
    }

    public MutableLiveData<ResponseBody> getResponseBodyMutableLiveData() {
        return responseBodyMutableLiveData;
    }

    public RequestReadCountNormalStory getRequestReadCountNormalStory() {
        return requestReadCountNormalStory;
    }

    public void setRequestReadCountNormalStory(RequestReadCountNormalStory requestReadCountNormalStory) {
        this.requestReadCountNormalStory = requestReadCountNormalStory;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
