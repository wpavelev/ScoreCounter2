package de.wpavelev.scorecounter2.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.FragmentPlayerViewBinding;

import java.util.Objects;

import de.wpavelev.scorecounter2.adapters.PlayerViewAdapter;
import de.wpavelev.scorecounter2.dialogs.InsertNameDialog;
import de.wpavelev.scorecounter2.dialogs.NameListDialog;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class PlayerViewFragment extends Fragment{


    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;

    private PlayerViewAdapter mPlayerViewAdapter;

    public PlayerViewFragment() {
    }

    public static PlayerViewFragment newInstance() {
        return new PlayerViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        com.example.scorecounter2.databinding.FragmentPlayerViewBinding binding = FragmentPlayerViewBinding.inflate(inflater, container, false);
        mRecyclerView = binding.playerViewRecycler;

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mPlayerViewAdapter = new PlayerViewAdapter(getContext());

        mPlayerViewAdapter.setListener(
                (v, position) -> mMainViewModel.setActivePlayer(position),
                (v, position) -> showPlayerNamesDialog(position),
                score -> mMainViewModel.setModifiedScore(score));

        mMainViewModel.getShowScoreListLive().observe(
                getViewLifecycleOwner(),
                aBoolean -> mPlayerViewAdapter.setMShowScoreList(aBoolean)
        );

        //sagt dem Adapter, wie viele Spieler eingestellt sind
        //wichtig für die größe der Felder
        mMainViewModel.getPlayerLimit().observe(
                getViewLifecycleOwner(),
                integer -> mPlayerViewAdapter.setPlayerCount(integer));


        mMainViewModel.getPlayerWithScoreLimitedByPlayerCount().observe(
                getViewLifecycleOwner(),
                playerWithScores -> mPlayerViewAdapter.submitList(playerWithScores)
        );

        mMainViewModel.getIsShowMainScoreAllowed().observe(
                getViewLifecycleOwner(),
                aBoolean -> mPlayerViewAdapter.setShowMainScore(aBoolean));

        mMainViewModel.getActivePlayer().observe(
                getViewLifecycleOwner(),
                integer -> mPlayerViewAdapter.setActivePlayer(integer)
        );

        mRecyclerView.setAdapter(mPlayerViewAdapter);





    }

    private void  showPlayerNamesDialog(int playerPosition) {


        NameListDialog dialog = new NameListDialog(new NameListDialog.OnClickListener() {
            @Override
            public void newName() {
                addNameDialog(playerPosition);
            }

            @Override
            public void clickPlayer(Name name) {
                Player player = Objects.requireNonNull(mMainViewModel.getPlayers().getValue()).get(playerPosition);
                player.setName(name.getName());
                mMainViewModel.updatePlayer(player);
                mPlayerViewAdapter.notifyDataSetChanged();


            }

            @Override
            public void editName(Name name) {
                editNameDialog(name);
            }

            @Override
            public void deleteName(Name name) {
                mMainViewModel.deleteName(name);
            }
        });


        dialog.show(getChildFragmentManager(), "longClickPlayerDialog");
    }

    private void editNameDialog(Name name) {
        InsertNameDialog dialog = new InsertNameDialog(name, name1 -> mMainViewModel.updateName(name1));
        dialog.show(getChildFragmentManager(), "EditNameDialog");
    }


    private void addNameDialog(int playerPosition) {
        InsertNameDialog dialog = new InsertNameDialog(new Name(""), name -> {
            mMainViewModel.insertName(name);
            Player player = Objects.requireNonNull(mMainViewModel.getPlayers().getValue()).get(playerPosition);
            player.setName(name.getName());
            mMainViewModel.updatePlayer(player);

        });
        dialog.show(getChildFragmentManager(), "NameListDialog");
    }


}
