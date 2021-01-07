package de.wpavelev.scorecounter2.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.FragmentMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class MainFragment extends Fragment {


    private MainViewModel mMainViewModel;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        //TODO: ConstraintSet Transitions:
        //TODO https://developer.android.com/training/constraint-layout


        com.example.scorecounter2.databinding.FragmentMainBinding binding = FragmentMainBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.setViewmodel(mMainViewModel);

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

        FloatingActionButton showScoresButton = view.findViewById(R.id.floatingActionButton);

        mMainViewModel.getShowScoreListLive().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showScoresButton.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.arrow_up));
            } else {
                showScoresButton.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.arrow_down));
            }
        });

        showScoresButton.setOnClickListener(view1 -> {
            if (mMainViewModel != null) {
                if (mMainViewModel.getShowScoreListLive().getValue() != null) {
                    boolean showScore = mMainViewModel.getShowScoreListLive().getValue();
                    mMainViewModel.setShowScoreListLive(!showScore);
                }
            }
        });




        return view;
    }




}
