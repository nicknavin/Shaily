package com.app.session.activity.ui.baseviewmodels;

import android.app.Application;

import com.app.session.activity.ui.home.HomeViewModel;
import com.app.session.activity.ui.profile.ProfileViewModel;
import com.app.session.activity.viewmodel.EditSubscriptionGroupViewModel;
import com.app.session.activity.viewmodel.ExpertProfileViewModel;
import com.app.session.activity.viewmodel.OtherUserSubscripionGroupViewModel;
import com.app.session.activity.viewmodel.PurchaseGroupStoriesViewModel;
import com.app.session.activity.viewmodel.ShowGroupStoryViewModel;
import com.app.session.activity.viewmodel.StoryDetailViewModel;
import com.app.session.activity.viewmodel.StoryPageDetailViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    

    private  String id,token;

    public ViewModelFactory(String id,String token)
    {

        this.id = id;
        this.token=token;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ProfileViewModel.class) {
            return (T) new ProfileViewModel( id,token);
        } else if (modelClass == HomeViewModel.class) {
            return (T) new HomeViewModel( id,token);
        }else if (modelClass == StoryDetailViewModel.class) {
            return (T) new StoryDetailViewModel( id,token);
        }
        else if (modelClass == ExpertProfileViewModel.class) {
            return (T) new ExpertProfileViewModel( id,token);
        }
        else if (modelClass == EditSubscriptionGroupViewModel.class) {
            return (T) new EditSubscriptionGroupViewModel( id,token);
        } else if (modelClass == OtherUserSubscripionGroupViewModel.class) {
            return (T) new OtherUserSubscripionGroupViewModel( id,token);
        }else if (modelClass == ShowGroupStoryViewModel.class) {
            return (T) new ShowGroupStoryViewModel( id,token);
        }else if (modelClass == PurchaseGroupStoriesViewModel.class) {
            return (T) new PurchaseGroupStoriesViewModel( id,token);
        }
else if (modelClass == StoryPageDetailViewModel.class) {
            return (T) new StoryPageDetailViewModel( id,token);
        }

        return null;
    }
}
