package com.example.android.quakereport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    LiveData<String> infoText = new earthquakeLiveData();

    public MainViewModel(){

    }

}
