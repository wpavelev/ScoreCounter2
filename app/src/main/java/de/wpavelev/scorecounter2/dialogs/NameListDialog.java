package de.wpavelev.scorecounter2.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.wpavelev.scorecounter2.adapters.NameListDialogAdapter;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class NameListDialog extends DialogFragment {


    private NameListDialogAdapter mNameListDialogAdapter;

    public interface OnClickListener {
        void newName();

        void clickPlayer(Name name);

        void editName(Name name);

        void deleteName(Name name);
    }

    OnClickListener mListener;

    MainViewModel mMainViewModel;

    public NameListDialog(OnClickListener listener) {
        this.mListener = listener;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_namelist, container, false);


        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        FloatingActionButton addName = view.findViewById(R.id.floatingActionButton2);
        addName.setOnClickListener(v -> {
            mListener.newName();
            getDialog().dismiss();

        });

        RecyclerView recyclerView = view.findViewById(R.id.recyler_namelist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.hasFixedSize();

        mNameListDialogAdapter = new NameListDialogAdapter(getContext(), new ArrayList<>(), new NameListDialogAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, Name name) {
                mListener.clickPlayer(name);
                getDialog().dismiss();
            }

            @Override
            public void onItemEdit(View v, Name name) {
                mListener.editName(name);

            }

            @Override
            public void onItemDelete(View v, Name name) {
                mListener.deleteName(name);
            }
        });



        recyclerView.setAdapter(mNameListDialogAdapter);


        return view;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mMainViewModel.getNames().observe(getViewLifecycleOwner(), names -> {
            mNameListDialogAdapter.setNameList(names);
            mNameListDialogAdapter.notifyDataSetChanged();
        });


        super.onActivityCreated(savedInstanceState);
    }



}
