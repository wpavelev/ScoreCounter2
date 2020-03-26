package com.example.scorecounter2.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scorecounter2.R;

public class MainFragment extends Fragment {

    private static final String TAG = "SC2: MainFragment";

    private MainViewModel mViewModel;
    TextView tv;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);



        tv = view.findViewById(R.id.textView);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mViewModel.getTestString().observe(getViewLifecycleOwner(), s -> {
            Log.d(TAG, "onActivityCreated: String verändert!" +
                    " neuer Werit: " + s);
            tv.setText(s);
        });


        // TODO: Use the ViewModel
    }

}
