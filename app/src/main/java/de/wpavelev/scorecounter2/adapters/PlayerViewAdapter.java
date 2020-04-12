package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ItemPlayerViewRecyclerBinding;

import java.util.List;

import de.wpavelev.scorecounter2.model.stuff.Player;
import de.wpavelev.scorecounter2.util.DisplayUtil;

public class PlayerViewAdapter extends RecyclerView.Adapter<PlayerViewAdapter.MyViewHolder> {

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
    public interface LongClickListener {
        void onItemLongClick(View v, int position);
    }

    private String TAG = "SimpleNameListAdapter";

    private List<Player> dataset;
    private List<Player> datasetOrigin;

    private int playerLimit = 0;
    private int activePlayer = 0;
    private ClickListener clickListener;
    private LongClickListener longClickListener;




    private Context context;



    public PlayerViewAdapter(Context context, List<Player> dataset) {
        this.context = context;
        setDataset(dataset);
        Log.d(TAG, "PlayerViewAdapter: dataset_size " + dataset.size());




    }

    public void setPlayerLimit(int playerLimit) {
        this.playerLimit = playerLimit;
        if (this.dataset.size() != 0) {
            setDataset(this.datasetOrigin);

        }
    }

    public void setDataset(List<Player> dataset) {
        this.datasetOrigin = dataset;
        if (playerLimit == 0  && datasetOrigin.size() == 0) {
            this.dataset = datasetOrigin.subList(0, 0);
        } else {
            this.dataset = datasetOrigin.subList(0, playerLimit);
        }
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
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
        Player player = dataset.get(position);
        holder.setPlayerPosition(position, activePlayer);
        holder.bind(player);


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ConstraintLayout layout;
        private TypedArray colorArray;
        private ItemPlayerViewRecyclerBinding viewHolderBinding;

        Drawable background;
        Drawable border;


        public MyViewHolder(ItemPlayerViewRecyclerBinding binding) {
            super(binding.getRoot());


            layout = binding.itemPlayerViewRecyclerView;
            layout.setMaxWidth((int) Math.round(((double) DisplayUtil.getDisplayWidthPx()) / (0.5 + playerLimit) ));

            colorArray = context.getResources().obtainTypedArray(R.array.player_color);



            background = context.getResources().getDrawable(R.drawable.item_player_view_background_0);
            border = context.getResources().getDrawable(R.drawable.border);


            this.viewHolderBinding = binding;

            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }
            if (longClickListener != null) {
                itemView.setOnLongClickListener(this);
            }

        }

        public void bind(Player player) {
            Log.d(TAG, "Player_bind:" + player.getName());
            this.viewHolderBinding.setPlayer(player);
        }

        public void setPlayerPosition(int playerPosition, int activePlayer) {


            int arrayPosition;
            if (playerPosition >= 0) {
                if (playerLimit < 4) {
                    arrayPosition = playerPosition;
                } else {
                    arrayPosition = playerPosition % 4;

                }

                int colorId = colorArray.getResourceId(arrayPosition, 0);

                int playerBackground = context.getResources().getColor(colorId);
                int activePlayerBackground = context.getResources().getColor(R.color.colorAccent);
                int backgroundColor = activePlayer == playerPosition ? activePlayerBackground : playerBackground;

                layout.setBackgroundColor(backgroundColor);

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
