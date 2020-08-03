package de.wpavelev.scorecounter2.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;

import java.util.ArrayList;

import de.wpavelev.scorecounter2.adapters.NameListDialogAdapter;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class NameListDialog extends DialogFragment {

    private static final String TAG = "SC2: LongClickPlayerDia";

    private NameListDialogAdapter adapter;

    public interface onClickListener {
        void newName();

        void clickPlayer(Name name);
    }

    onClickListener listener;

    MainViewModel viewModel;

    public NameListDialog(onClickListener listener) {
        this.listener = listener;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_namelist, container, false);

        getDialog().setCancelable(true);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        Button addName = view.findViewById(R.id.btn_addName);
        addName.setOnClickListener(v -> {
            listener.newName();
            getDialog().dismiss();
            Log.d(TAG, "onCreateView: newname");
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyler_namelist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.hasFixedSize();

        adapter = new NameListDialogAdapter(getContext(), new ArrayList<>(), (v, name) -> {
            listener.clickPlayer(name);
            getDialog().dismiss();
            Log.d(TAG, "onCreateView: Name selected: " + name.getName());

        });

        recyclerView.setAdapter(adapter);



        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getNames().observe(getViewLifecycleOwner(), names -> {
            adapter.setDataset(names);
            adapter.notifyDataSetChanged();
        });


        super.onActivityCreated(savedInstanceState);
    }


}
