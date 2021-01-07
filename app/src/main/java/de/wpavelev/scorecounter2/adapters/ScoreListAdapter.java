package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ItemPlayerscoreBinding;

import de.wpavelev.scorecounter2.model.data.Score;

public class ScoreListAdapter extends ListAdapter<Score, ScoreListAdapter.MyViewHolder> {

    public interface ClickListener {
        void onItemClick(View v, Score score);
    }

    public interface LongClickListener {
        void onItemLongClick(View v, Score score);


    }


    private static final DiffUtil.ItemCallback<Score> DIFF_CALLBACK = new DiffUtil.ItemCallback<Score>() {
        @Override
        public boolean areItemsTheSame(@NonNull Score oldItem, @NonNull Score newItem) {
            return oldItem.getScoreId() == newItem.getScoreId();

        }

        @Override
        public boolean areContentsTheSame(@NonNull Score oldItem, @NonNull Score newItem) {
            return oldItem.getScore() == newItem.getScore() &&
                    oldItem.isSelected() == newItem.isSelected();

        }
    };


    private ClickListener mClickListener;
    private LongClickListener mLongClickListener;


    private final Context mContext;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemPlayerscoreBinding binding = ItemPlayerscoreBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );


        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Score score = getItem(position);
        holder.bindObj(score);


    }


    public ScoreListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.mContext = context;


    }


    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ItemPlayerscoreBinding viewHolderBinding;

        TextView textView;


        public MyViewHolder(ItemPlayerscoreBinding binding) {

            super(binding.getRoot());


            this.viewHolderBinding = binding;
            this.textView = binding.itemPlayerScoreTV;

            this.textView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));

            if (mClickListener != null) {
                itemView.setOnClickListener(this);
            }

            if (mLongClickListener != null) {
                itemView.setOnLongClickListener(this);
            }

        }


        public void bindObj(Score score) {
            this.viewHolderBinding.setScore(score);
            if (score.isSelected()) {
                this.textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }

        }


        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getItem(getAdapterPosition()));
                notifyDataSetChanged();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getItem(getAdapterPosition()));
                notifyDataSetChanged();
            }


            return false;
        }


    }

}
