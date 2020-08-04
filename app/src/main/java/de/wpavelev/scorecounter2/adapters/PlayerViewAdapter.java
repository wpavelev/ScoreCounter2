package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ItemPlayerViewRecyclerBinding;

import java.util.ArrayList;
import java.util.List;

import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.util.DisplayUtil;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class PlayerViewAdapter extends RecyclerView.Adapter<PlayerViewAdapter.MyViewHolder> {

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    public interface LongClickListener {
        void onItemLongClick(View v, int position);
    }

    private String TAG = "PlayerViewAdapter";

    private List<Player> playerList;
    private SparseArray<List<Score>> playerScores;


    private int playerLimit = 0;
    private int activePlayer = 0;
    private ClickListener clickListener;
    private LongClickListener longClickListener;


    private Context context;
    private MainViewModel viewModel;

    public PlayerViewAdapter(Context context, List<Player> playerList) {

        this.context = context;
        this.viewModel = viewModel;

        this.playerList = playerList;
        Log.d(TAG, "PlayerViewAdapter: dataset_size " + playerList.size());


    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;

        notifyDataSetChanged();
    }

    public void setPlayerScores(SparseArray<List<Score>> playerScores) {
        this.playerScores = playerScores;
        notifyDataSetChanged();
    }

    public void setPlayerLimit(int playerLimit) {

        this.playerLimit = playerLimit;
        notifyDataSetChanged();

    }


    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ItemPlayerViewRecyclerBinding binding = ItemPlayerViewRecyclerBinding.inflate(layoutInflater,
                parent, false);


        return new MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.setPlayerPosition(position, activePlayer);
        holder.bindPlayer(player);


        if (playerScores != null) {
            holder.bindPlayerScore(playerScores.get(player.getId()));
        }


    }


    @Override
    public int getItemCount() {
        return playerList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ConstraintLayout layout;
        private TypedArray activePlayerArrayColor;
        private TypedArray inactivePlayerArrayColor;

        private RecyclerView recyclerPlayerScore;

        private ItemPlayerViewRecyclerBinding viewHolderBinding;

        PlayerScoreAdapter playerScoreAdapter;

        List<Score> playerScores;

        Drawable border;


        public MyViewHolder(ItemPlayerViewRecyclerBinding binding) {
            super(binding.getRoot());

            this.viewHolderBinding = binding;

            layout = binding.itemPlayerViewRecyclerView;
            int layoutWidth = (int) Math.round(((double) DisplayUtil.getDisplayWidthPx()) / (-0.5 + playerLimit));
            layout.setMaxWidth(layoutWidth);
            layout.setMinWidth(layoutWidth);


            activePlayerArrayColor = context.getResources().obtainTypedArray(R.array.player_color);
            inactivePlayerArrayColor = context.getResources().obtainTypedArray(R.array.inactive_player_color);

            border = context.getResources().getDrawable(R.drawable.border);


            playerScores = new ArrayList<>();
            recyclerPlayerScore = binding.itemPlayerViewRecyclerPlayerScore;
            recyclerPlayerScore.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            playerScoreAdapter = new PlayerScoreAdapter(context, playerScores);
            recyclerPlayerScore.setAdapter(playerScoreAdapter);


            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }
            if (longClickListener != null) {
                itemView.setOnLongClickListener(this);
            }

        }

        public void bindPlayer(Player player) {
            this.viewHolderBinding.setPlayer(player);

        }

        public void bindPlayerScore(List<Score> playerScores) {
            if (playerScores != null) {
                this.playerScores = playerScores;
                playerScoreAdapter.setDataset(playerScores);
                playerScoreAdapter.notifyDataSetChanged();
                Log.d(TAG, "playerscores: ");
                for (Score playerScore : playerScores) {
                    Log.d(TAG, "score.player=" + playerScore.getPlayer() + " " +
                            "score.score=" + playerScore.getScore());
                }

            } else {
                Log.w(TAG, "bindPlayerScore: playerScores is null!");
            }
        }

        public void setPlayerPosition(int playerPosition, int activePlayer) {


            int arrayPosition;
            if (playerPosition >= 0) {
                if (playerLimit < 4) {
                    arrayPosition = playerPosition;
                } else {
                    arrayPosition = playerPosition % 4;

                }

                int activePlayerColorId = activePlayerArrayColor.getResourceId(arrayPosition, 0);
                int inactivePlayerColorId = inactivePlayerArrayColor.getResourceId(arrayPosition, 0);

                int playerBackground = context.getResources().getColor(inactivePlayerColorId);
                if (activePlayer == playerPosition) {
                    playerBackground = context.getResources().getColor(activePlayerColorId);
                }

                layout.setBackgroundColor(playerBackground);

            }


        }


        @Override
        public void onClick(View v) {
            if (clickListener == null) {
                Log.e(TAG, "onClick: clickListener is Null");
            } else {
                clickListener.onItemClick(v, getAdapterPosition());

            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener == null) {
                Log.e(TAG, "onLongClick: longClickListener is Null");
            } else {
                longClickListener.onItemLongClick(v, getAdapterPosition());

            }

            return false;
        }
    }


}
