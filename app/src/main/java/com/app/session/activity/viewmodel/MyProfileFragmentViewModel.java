package com.app.session.activity.viewmodel;

import com.app.session.data.repository.MainRepository;

import androidx.lifecycle.ViewModel;

public class MyProfileFragmentViewModel extends ViewModel
{
    String id;
    MainRepository mainRepository;
    public MyProfileFragmentViewModel(String id,String token)
    {
        this.id=id;
        mainRepository=new MainRepository(id);
    }

}
