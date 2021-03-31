package com.app.session.data.api;

import com.app.session.data.model.StoriesFollowingUsers;
import com.app.session.data.model.SubscribedAllStroiesRoot;
import com.app.session.data.model.SubscriptionGroupRoot;
import com.app.session.data.model.UserId;
import com.app.session.data.model.UserRoot;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.ResponseBody;

public class ApiServiceImpl
{
    private CompositeDisposable disposable = new CompositeDisposable();


}
