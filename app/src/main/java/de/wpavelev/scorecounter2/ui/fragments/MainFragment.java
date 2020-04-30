package de.wpavelev.scorecounter2.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.scorecounter2.databinding.FragmentMainBinding;

import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class MainFragment extends Fragment  {

    private static final String TAG = "SC2: MainFragment";


    private FragmentMainBinding binding;
    private MainViewModel viewModel;


    private TextView tv;



    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        //TODO: ConstraintSet Transitions:
        //TODO https://developer.android.com/training/constraint-layout




        this.binding = FragmentMainBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        int containerNumPad = binding.mainFragmentFrameNumPad.getId();
        NumPadFragment numPadFragment = NumPadFragment.newInstance();

        int containerPlayerView = binding.mainFragmentFramePlayer.getId();
        PlayerViewFragment playerViewFragment = PlayerViewFragment.newInstance();

        int containerScoreView = binding.mainFragmentFrameScore.getId();
        ScoreViewFragment scoreViewFragment = ScoreViewFragment.newInstance();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(containerNumPad, numPadFragment);
        transaction.add(containerPlayerView, playerViewFragment);
        transaction.add(containerScoreView, scoreViewFragment);

        transaction.commit();

        return view;
    }



}
