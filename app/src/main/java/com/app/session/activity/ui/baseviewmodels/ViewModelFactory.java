package com.app.session.activity.ui.baseviewmodels;

import android.app.Application;

import com.app.session.activity.StoryDetailActivity;
import com.app.session.activity.ui.home.HomeViewModel;
import com.app.session.activity.ui.profile.ProfileViewModel;
import com.app.session.activity.viewmodel.StoryDetailViewModel;
import com.app.session.data.api.ApiHelper;
import com.app.session.data.repository.MainRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @NonNull
    private final Application application;
    private final String id;
    private final String token;

    public ViewModelFactory(@NonNull Application application, String id, String token)
    {
        this.application = application;
        this.id = id;
        this.token = token;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ProfileViewModel.class) {
            return (T) new ProfileViewModel(application, id, token);
        } else if (modelClass == HomeViewModel.class) {
            return (T) new HomeViewModel(application, id, token);
        }else if (modelClass == StoryDetailViewModel.class) {
            return (T) new StoryDetailViewModel(application, id, token);
        }

        return null;
    }
}
