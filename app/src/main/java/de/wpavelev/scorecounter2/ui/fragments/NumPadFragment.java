package de.wpavelev.scorecounter2.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scorecounter2.databinding.FragmentNumPadBinding;

import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumPadFragment extends Fragment {

    private MainViewModel viewModel;
    private FragmentNumPadBinding binding;

    public NumPadFragment() {
        // Required empty public constructor
    }


    public static NumPadFragment newInstance() {
        return new NumPadFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentNumPadBinding.inflate(inflater,container,false);

        this.viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        this.binding.setViewmodel(this.viewModel);
        this.binding.setLifecycleOwner(getViewLifecycleOwner());

        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }
}
