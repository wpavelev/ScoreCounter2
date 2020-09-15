package de.wpavelev.scorecounter2.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.wpavelev.scorecounter2.adapters.PlayerViewAdapter;
import de.wpavelev.scorecounter2.dialogs.InsertNameDialog;
import de.wpavelev.scorecounter2.dialogs.NameListDialog;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class PlayerViewFragment extends Fragment{

    private static final String TAG = "SC2: PlayerViewFragment";


    private MainViewModel viewModel;
    private RecyclerView recyclerView;

    private PlayerViewAdapter adapter;

    public PlayerViewFragment() {
    }

    public static PlayerViewFragment newInstance() {
        return new PlayerViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        com.example.scorecounter2.databinding.FragmentPlayerViewBinding binding = FragmentPlayerViewBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        recyclerView = binding.playerViewRecycler;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerViewAdapter(getContext(), new ArrayList<>());
        adapter.setClickListener((v, position) -> {

        });
        adapter.setLongClickListener((v, position) -> showPlayerNamesDialog(position));

        adapter.setScoreChangeListener(score -> viewModel.setEditScore(score));

        recyclerView.setAdapter(adapter);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        //aktualisiert die Liste der Spieler im Adapter
        viewModel.getPlayerLimited().observe(getViewLifecycleOwner(), list -> {
            if (list == null) {
                adapter.setPlayerList(new ArrayList<>());
            }

            adapter.setPlayerList(list);

        });

        viewModel.getScores().observe(getViewLifecycleOwner(), scoreList -> {
            Log.d(TAG, "onActivityCreated: Observer trigger!");
            adapter.setPlayerScores(convertScoreListToSparseArray(scoreList));

        });


        //sagt dem Adapter, wie viele Spieler eingestellt sind
        //wichtig für die größe der Felder
        viewModel.getPlayerLimit().observe(getViewLifecycleOwner(), integer -> adapter.setPlayerCount(integer));

        //sagt dem Adapter, welcher Spieler an der Reihe ist
        viewModel.getActivePlayer().observe(getViewLifecycleOwner(), integer -> {
            adapter.setActivePlayer(integer);
            recyclerView.smoothScrollToPosition(integer);
        });



    }

    private SparseArray<List<Score>> convertScoreListToSparseArray(List<Score> scoreList) {
        SparseArray<List<Score>> sparseArray = new SparseArray<>();
        Set<Integer> playerIdExists = new HashSet<>();
        for (Score score : scoreList) {
            int playerId = score.getPlayer();

            if (!playerIdExists.contains(playerId)) {
                sparseArray.put(playerId, new ArrayList<>());
            }
            playerIdExists.add(playerId);
            sparseArray.get(playerId).add(score);

        }

        return sparseArray;
    }

    private void  showPlayerNamesDialog(int playerPosition) {
        NameListDialog dialog = new NameListDialog(new NameListDialog.onClickListener() {
            @Override
            public void newName() {
                addNameDialog(playerPosition);
            }

            @Override
            public void clickPlayer(Name name) {
                Player player = Objects.requireNonNull(viewModel.getPlayers().getValue()).get(playerPosition);
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
            Player player = Objects.requireNonNull(viewModel.getPlayers().getValue()).get(playerPosition);
            player.setName(name.getName());
            viewModel.updatePlayer(player);

        });
        dialog.show(getChildFragmentManager(), "NameListDialog");
        Log.d(TAG, "addNameDialog: ");
    }


}
