package de.wpavelev.scorecounter2.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scorecounter2.databinding.FragmentScoreViewBinding;

import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class ScoreViewFragment extends Fragment {

    FragmentScoreViewBinding mBinding;


    public ScoreViewFragment() {

    }

    public static ScoreViewFragment newInstance() {
        return new ScoreViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentScoreViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mBinding.setViewmodel(viewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        return view;
    }
}
