package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ItemPlayerViewRecyclerBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.util.DisplayUtil;

public class PlayerViewAdapter extends RecyclerView.Adapter<PlayerViewAdapter.MyViewHolder> {

    public interface ClickListener {

        void onItemClick(View v, int position);
    }
    public interface LongClickListener {

        void onItemLongClick(View v, int position);
    }
    public interface ScoreChangeListener {

        void onScoreChange(Score score);
    }

    protected boolean showMainScore = true;



    SparseArray<Integer> mainScores = new SparseArray<>();

    private String TAG = "PlayerViewAdapter";

    /**
     * Liste der Spieler, die angezeigt werden
     */
    //private List<Player> playerList;

    /**
     * Listen von Scores, die in Tabellenform angezeigt werden
     */
    private SparseArray<List<Score>> playerScores;


    /**
     * Anzahl der Spieler
     */
    private int playerCount = 0;

    /**
     * Spieler, der an der Reihe ist
     */
    private int activePlayer = 0;


    private int editingScore = 0;

    private ScoreChangeListener scoreChangeListener;
    private ClickListener clickListener;
    private LongClickListener longClickListener;

    private LifecycleOwner lifecycleOwner;
    private LiveData<List<Player>> playerListLiveData;
    private LiveData<Boolean> showScoresLiveData;
    private LiveData<Boolean> showMainScoreLiveData;

    private List<Player> playerList = new ArrayList<>();



    private Context context;

    public PlayerViewAdapter(Context context,
                             LifecycleOwner lifecycleOwner,
                             LiveData<List<Player>> playerListLiveData,
                             LiveData<List<Score>> scoreListLiveData,
                             LiveData<Boolean> showScoresLiveData,
                             LiveData<Boolean> showMainScoreLiveData) {

        this.context = context;

        this.lifecycleOwner = lifecycleOwner;

        this.showScoresLiveData = showScoresLiveData;

        this.playerListLiveData = playerListLiveData;

        this.showMainScoreLiveData = showMainScoreLiveData;

        playerListLiveData.observe(lifecycleOwner, players -> playerList = new ArrayList<>(players));

        scoreListLiveData.observe(lifecycleOwner, scoreList -> setPlayerScores(scoreList));

        showMainScoreLiveData.observe(lifecycleOwner, aBoolean -> setShowPlayerMainScor(aBoolean));




    }



    private void setPlayerScores(List<Score> scoreList) {
        this.playerScores = convertScoreListToSparseArray(scoreList);
        notifyDataSetChanged();
    }

    private SparseArray<List<Score>> convertScoreListToSparseArray(List<Score> scoreList) {

        mainScores = new SparseArray<>();
        SparseArray<List<Score>> sparseArray = new SparseArray<>();
        Set<Integer> playerIdExists = new HashSet<>();
        for (Score score : scoreList) {
            int playerId = score.getPlayer();

            if (!playerIdExists.contains(playerId)) {
                sparseArray.put(playerId, new ArrayList<>());
                mainScores.put(playerId, 0);
            }
            playerIdExists.add(playerId);
            sparseArray.get(playerId).add(score);
            mainScores.put(playerId, mainScores.get(playerId) + score.getScore());


        }

        return sparseArray;
    }

    public void setPlayerCount(int playerCount) {

        this.playerCount = playerCount;
        notifyDataSetChanged();

    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
        notifyDataSetChanged();
    }

    public void setListener(ClickListener clickListener, LongClickListener longClickListener, ScoreChangeListener scoreChangeListener) {
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.scoreChangeListener = scoreChangeListener;
    }

    public void setShowPlayerMainScor(boolean showPlayerMainScore) {
        this.showMainScore = showPlayerMainScore;
        notifyDataSetChanged();
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
        if (mainScores != null && mainScores.size() >= player.getId()) {
            player.setScore(mainScores.get(player.getId()));
        }
        holder.setPlayer(player);


        holder.setShowMainScore(showMainScore);



        if (playerScores != null) {
            holder.setPlayerScoreList(playerScores.get(player.getId()));
        }

        if (editingScore != -1) {
            holder.setEditScore(editingScore);

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

        private ItemPlayerViewRecyclerBinding viewHolderBinding;

        PlayerScoreAdapter playerScoreAdapter;

        Drawable border;


        public MyViewHolder(ItemPlayerViewRecyclerBinding binding) {
            super(binding.getRoot());

            this.viewHolderBinding = binding;




            layout = binding.itemPlayerViewRecyclerView;
            int layoutWidth = (int) Math.round(((double) DisplayUtil.getDisplayWidthPx()) / (0.5 + playerCount));
            layout.setMaxWidth(layoutWidth);
            layout.setMinWidth(layoutWidth);


            activePlayerArrayColor = context.getResources().obtainTypedArray(R.array.player_color);
            inactivePlayerArrayColor = context.getResources().obtainTypedArray(R.array.inactive_player_color);

            border = context.getResources().getDrawable(R.drawable.border);


            RecyclerView recyclerPlayerScore = binding.itemPlayerViewRecyclerPlayerScore;
            recyclerPlayerScore.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

            playerScoreAdapter = new PlayerScoreAdapter(context);

            playerScoreAdapter.setClickListener((v, score) -> Toast.makeText(context, "" + score.getScore(), Toast.LENGTH_SHORT).show());

            playerScoreAdapter.setLongClickListener((v, score) -> scoreChangeListener.onScoreChange(score));


            recyclerPlayerScore.setAdapter(playerScoreAdapter);

            showScoresLiveData.observe(lifecycleOwner, aBoolean -> binding.setShowScores(aBoolean));



            if (clickListener != null) {
                itemView.setOnClickListener(this);
            } else {
                Log.d(TAG, "MyViewHolder: clickListener is null");

            }

            if (longClickListener != null) {
                itemView.setOnLongClickListener(this);
            } else {
                Log.d(TAG, "MyViewHolder: longClickListener is null");

            }


        }

        public void setPlayer(Player player) {
            viewHolderBinding.setPlayer(player);
        }
        public void setEditScore(int score) {
            playerScoreAdapter.setEditScore(score);

        }

        public void setShowMainScore(boolean showMainScore) {
            viewHolderBinding.setShowMainScore(showMainScore);
        }

        public void setPlayerScoreList(List<Score> playerScores) {
            if (playerScores != null) {
                playerScoreAdapter.setDataset(playerScores);
                playerScoreAdapter.notifyDataSetChanged();
                Log.d(TAG, "playerscores: ");
                for (Score playerScore : playerScores) {
                    Log.d(TAG, "score.player=" + playerScore.getPlayer() + " " +
                            "score.score=" + playerScore.getScore());
                }

            } else {
                playerScoreAdapter.setDataset(new ArrayList<>());
                playerScoreAdapter.notifyDataSetChanged();
                Log.w(TAG, "bindPlayerScore: playerScores is null!");
            }
        }

        public void setPlayerPosition(int playerPosition, int activePlayer) {

            int arrayPosition;
            if (playerPosition >= 0) {
                if (playerCount < 4) {
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
