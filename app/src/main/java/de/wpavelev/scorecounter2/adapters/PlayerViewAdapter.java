package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ItemPlayerViewRecyclerBinding;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.PlayerWithScore;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.util.ColorUtil;
import de.wpavelev.scorecounter2.util.DisplayUtil;

import static com.example.scorecounter2.R.string.score_click_info;

public class PlayerViewAdapter extends ListAdapter<PlayerWithScore, PlayerViewAdapter.MyViewHolder> {

    public interface ClickListener {

        void onItemClick(View v, int position);
    }

    public interface LongClickListener {

        void onItemLongClick(View v, int position);
    }

    public interface ScoreChangeListener {

        void onScoreChange(Score score);
    }

    protected boolean mShowMainScore = true;
    protected boolean mMShowScoreList = true;




    /**
     * Anzahl der Spieler
     */
    private int mPlayerCount = 0;

    private ScoreChangeListener mScoreChangeListener;
    private ClickListener mClickListener;
    private LongClickListener mLongClickListener;

    private int mActivePlayer;

    Context mContext;


    public PlayerViewAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.mContext = context;




    }


    private static final DiffUtil.ItemCallback<PlayerWithScore> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlayerWithScore>() {

        @Override
        public boolean areItemsTheSame(@NonNull PlayerWithScore oldItem, @NonNull PlayerWithScore newItem) {

            return oldItem.mPlayer.getPlayerId() == newItem.mPlayer.getPlayerId();

        }

        @Override
        public boolean areContentsTheSame(@NonNull PlayerWithScore oldItem, @NonNull PlayerWithScore newItem) {

            if (!oldItem.mPlayer.getName().equals(newItem.mPlayer.getName())) {
                return false;
            }



            if (oldItem.mPlayerScores.size() != newItem.mPlayerScores.size()) {
                return false;
            }


            for (int i = 0; i < oldItem.mPlayerScores.size(); i++) {


                if (oldItem.mPlayerScores.get(i).getScore() != newItem.mPlayerScores.get(i).getScore()) {
                    return false;
                }
                if (oldItem.mPlayerScores.get(i).isSelected() != newItem.mPlayerScores.get(i).isSelected()) {
                    return false;
                }
            }

            return true;
        }
    };

    public void setPlayerCount(int playerCount) {
        this.mPlayerCount = playerCount;
    }


    public void setListener(ClickListener clickListener, LongClickListener longClickListener, ScoreChangeListener scoreChangeListener) {
        this.mClickListener = clickListener;
        this.mLongClickListener = longClickListener;
        this.mScoreChangeListener = scoreChangeListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ItemPlayerViewRecyclerBinding binding = ItemPlayerViewRecyclerBinding.inflate(layoutInflater,
                parent, false);


        return new MyViewHolder(binding);


    }

    public void setActivePlayer(int activePlayerOrder) {
        mActivePlayer = activePlayerOrder;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Player player = getItem(position).mPlayer;
        holder.setPlayerBackgroundColor(position, mActivePlayer);
        holder.setPlayer(player);

        holder.setShowMainScore(mShowMainScore);

        holder.setPlayerScoreList(getItem(position).mPlayerScores);


    }

    public void setShowMainScore(boolean showMainScore) {
        this.mShowMainScore = showMainScore;
        notifyDataSetChanged();
    }

    public void setMShowScoreList(boolean MShowScoreList) {

        mMShowScoreList = MShowScoreList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final ConstraintLayout layout;


        private final ItemPlayerViewRecyclerBinding viewHolderBinding;

        private final ScoreListAdapter mScoreListAdapter;


        public MyViewHolder(ItemPlayerViewRecyclerBinding binding) {

            super(binding.getRoot());

            this.viewHolderBinding = binding;


            layout = binding.itemPlayerViewRecyclerView;
            int layoutWidth = (int) Math.round(((double) DisplayUtil.getDisplayWidthPx()) / (mPlayerCount) - 40);
            layout.setMaxWidth(layoutWidth);
            layout.setMinWidth(layoutWidth);


            RecyclerView recyclerPlayerScore = binding.itemPlayerViewRecyclerPlayerScore;
            if (mMShowScoreList) {
                recyclerPlayerScore.setVisibility(View.VISIBLE);
            } else {
                recyclerPlayerScore.setVisibility(View.GONE);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            layoutManager.setStackFromEnd(false);
            recyclerPlayerScore.setLayoutManager(layoutManager);

            mScoreListAdapter = new ScoreListAdapter(mContext);

            mScoreListAdapter.setClickListener((v, score) -> {

                Toast.makeText(mContext, score_click_info, Toast.LENGTH_SHORT).show();
                mScoreListAdapter.notifyDataSetChanged();
            });
            mScoreListAdapter.setLongClickListener((v, score) -> mScoreChangeListener.onScoreChange(score));


            recyclerPlayerScore.setAdapter(mScoreListAdapter);

            // TODO: 29.12.2020

            binding.setShowScores(true);


            if (mClickListener != null) {
                itemView.setOnClickListener(this);
            }

            if (mLongClickListener != null) {
                itemView.setOnLongClickListener(this);
            }


        }

        public void setPlayer(Player player) {
            viewHolderBinding.setPlayer(player);



        }


        public void setShowMainScore(boolean showMainScore) {
            viewHolderBinding.setShowMainScore(showMainScore);
        }

        public void setPlayerScoreList(List<Score> playerScores) {
            RecyclerView recyclerPlayerScore;
            recyclerPlayerScore = this.viewHolderBinding.itemPlayerViewRecyclerPlayerScore;
            if (mMShowScoreList) {
                recyclerPlayerScore.setVisibility(View.VISIBLE);
            } else {
                recyclerPlayerScore.setVisibility(View.GONE);
            }
            mScoreListAdapter.submitList(playerScores);
            recyclerPlayerScore.scrollToPosition(0);


        }

        public void setPlayerBackgroundColor(int playerPosition, int activePlayer) {
            int arrayPosition;
            if (playerPosition >= 0) {
                if (mPlayerCount < 4) {
                    arrayPosition = playerPosition;
                } else {
                    arrayPosition = playerPosition % 4;

                }

                int activePlayerColorId = ColorUtil.getActivePlayerArrayColor().getResourceId(arrayPosition, 0);
                int inactivePlayerColorId = ColorUtil.getInactivePlayerArrayColor().getResourceId(arrayPosition, 0);

                int playerBackground = ContextCompat.getColor(mContext, inactivePlayerColorId);
                if (activePlayer == playerPosition) {
                    playerBackground = ContextCompat.getColor(mContext, activePlayerColorId);
                }

                layout.setBackgroundColor(playerBackground);

            }


        }


        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getAdapterPosition());
            }

            return false;
        }
    }


}
