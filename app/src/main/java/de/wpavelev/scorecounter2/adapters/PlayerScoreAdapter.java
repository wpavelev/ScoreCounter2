package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ItemPlayerscoreBinding;

import java.util.ArrayList;
import java.util.List;

import de.wpavelev.scorecounter2.model.data.Score;

public class PlayerScoreAdapter extends RecyclerView.Adapter<PlayerScoreAdapter.MyViewHolder>{

    public interface ClickListener{
        void onItemClick(View v, Score score);
    }

    public interface LongClickListener {
        void onItemLongClick(View v, Score score);


    }


    private static final String TAG = "PlayerScoreAdapter";

    private List<Score> dataset;


    private ClickListener clickListener;
    private LongClickListener longClickListener;

    private int positionEditingScore = -1;


    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemPlayerscoreBinding binding = ItemPlayerscoreBinding.inflate(
                LayoutInflater.from(context), parent, false
        );



        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Score score = dataset.get(position);
        holder.bindObj(score);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public PlayerScoreAdapter(Context context) {
        this.context = context;
        this.dataset = dataset != null ? dataset : new ArrayList<>();

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void resetAllColors() {

    }

    public void setDataset(List<Score> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();

    }

    public void setEditScore(int score) {
        if (0 < positionEditingScore && positionEditingScore < dataset.size()) {
            dataset.get(positionEditingScore).setId(score);
            notifyDataSetChanged();

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        ItemPlayerscoreBinding viewHolderBinding;

        TextView textView;


        public MyViewHolder(ItemPlayerscoreBinding binding) {

            super(binding.getRoot());


            this.viewHolderBinding = binding;
            this.textView = binding.itemPlayerScoreTV;

            this.textView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }

            if (longClickListener != null) {
                itemView.setOnLongClickListener(this);
            } else {
                Log.e(TAG, "MyViewHolder: LongclickListener is null");
            }

        }



        public void bindObj(Score score) {
            this.viewHolderBinding.setScore(score);

        }



        @Override
        public void onClick(View v) {
            if (clickListener == null) {
                Log.e(TAG, "onClick: clicklistner is Null");

            } else {
                clickListener.onItemClick(v, dataset.get(getAdapterPosition()));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener == null) {
                Log.e(TAG, "onLongClick: longClickListener is Null");
            } else {

                positionEditingScore = getAdapterPosition();
                longClickListener.onItemLongClick(v, dataset.get(getAdapterPosition()));

            }


            return false;
        }


    }

}
