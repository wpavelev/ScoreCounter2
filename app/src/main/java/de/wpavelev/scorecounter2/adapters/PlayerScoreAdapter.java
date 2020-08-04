package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ItemPlayerscoreBinding;

import java.util.ArrayList;
import java.util.List;

import de.wpavelev.scorecounter2.model.data.Score;

public class PlayerScoreAdapter extends RecyclerView.Adapter<PlayerScoreAdapter.MyViewHolder>{

    private static final String TAG = "PlayerScoreAdapter";

    private List<Score> dataset;
    private LayoutInflater layoutInflater;

    private ClickListener clickListener;

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

    public interface ClickListener{
        void onItemClick(View v, Score score);
    }

    public PlayerScoreAdapter(Context context, List<Score> dataset) {
        this.context = context;

        this.dataset = dataset != null ? dataset : new ArrayList<>();

    }

    public void setDataset(List<Score> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemPlayerscoreBinding viewHolderBinding;

        public MyViewHolder(ItemPlayerscoreBinding binding) {
            super(binding.getRoot());

            this.viewHolderBinding = binding;

            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }

        }

        public void bindObj(Score score) {
            this.viewHolderBinding.setScore(score);
        }

        @Override
        public void onClick(View view) {
            // TODO: 03.08.2020 Clicklistener
            //clicklistener.onItemClick(v, ...);
        }
    }

}
