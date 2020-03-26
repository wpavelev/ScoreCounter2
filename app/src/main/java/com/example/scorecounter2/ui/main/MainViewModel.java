package com.example.scorecounter2.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "SC2: MainViewModel";
    MutableLiveData<String> testString = new MutableLiveData<>();




    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        Log.d(TAG, "setTestString: alter Wert:" + this.testString.getValue());
        this.testString.setValue(testString);
        Log.d(TAG, "setTestString: neuer Wert:" + this.testString.getValue());
    }
}
