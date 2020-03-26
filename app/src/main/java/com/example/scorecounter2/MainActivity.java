package com.example.scorecounter2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.scorecounter2.ui.main.MainFragment;
import com.example.scorecounter2.ui.main.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SC2: MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        Log.d(TAG, "onCreate: ViewModel wird geladen");
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Log.d(TAG, "onCreate: TestString wird verändert");
        viewModel.setTestString("test");

    }
}
