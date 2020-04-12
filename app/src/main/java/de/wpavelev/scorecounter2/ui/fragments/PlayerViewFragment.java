package de.wpavelev.scorecounter2.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.databinding.FragmentPlayerViewBinding;

import java.util.ArrayList;

import de.wpavelev.scorecounter2.adapters.PlayerViewAdapter;
import de.wpavelev.scorecounter2.dialogs.InsertNameDialog;
import de.wpavelev.scorecounter2.dialogs.NameListDialog;
import de.wpavelev.scorecounter2.model.stuff.Name;
import de.wpavelev.scorecounter2.model.stuff.Player;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class PlayerViewFragment extends Fragment {

    private static final String TAG = "SC2: PlayerViewFragment";


    private FragmentPlayerViewBinding binding;
    private MainViewModel viewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private PlayerViewAdapter adapter;

    public PlayerViewFragment() {
    }

    public static PlayerViewFragment newInstance() {
        return new PlayerViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentPlayerViewBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        recyclerView = binding.playerViewRecycler;
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerViewAdapter(getContext(), new ArrayList<>());
        adapter.setClickListener((v, position) -> {

        });
        adapter.setLongClickListener((v, position) -> {
            showPlayerNamesDialog(position);
        });

        recyclerView.setAdapter(adapter);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getPlayers().observe(getViewLifecycleOwner(), players -> {
            adapter.setDataset(players);
            adapter.notifyDataSetChanged();
        });


        viewModel.getPlayerLimit().observe(getViewLifecycleOwner(), integer -> {
            adapter.setPlayerLimit(integer);
            adapter.notifyDataSetChanged();
        });

        viewModel.getActivePlayer().observe(getViewLifecycleOwner(), integer -> {
            adapter.setActivePlayer(integer);
            adapter.notifyDataSetChanged();
        });


    }

    private void showPlayerNamesDialog(int playerPosition) {
        NameListDialog dialog = new NameListDialog(new NameListDialog.onClickListener() {
            @Override
            public void newName() {
                addNameDialog(playerPosition);
            }

            @Override
            public void clickPlayer(Name name) {
                Player player = viewModel.getPlayers().getValue().get(playerPosition);
                player.setName(name.getName());
                viewModel.updatePlayer(player);


            }
        });

        dialog.show(getChildFragmentManager(), "longClickPlayerDialog");
        Log.d(TAG, "showPlayerNamesDialog: ");
    }

    private void addNameDialog(int playerPosition) {
        InsertNameDialog dialog = new InsertNameDialog(new Name(""), name -> {
            viewModel.insertName(name);
            Player player = viewModel.getPlayers().getValue().get(playerPosition);
            player.setName(name.getName());
            viewModel.updatePlayer(player);

        });
        dialog.show(getChildFragmentManager(), "NameListDialog");
        Log.d(TAG, "addNameDialog: ");
    }


}
