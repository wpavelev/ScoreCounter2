package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;

import java.util.ArrayList;
import java.util.List;



public class MainMenuDialogAdapter extends RecyclerView.Adapter<MainMenuDialogAdapter.MyViewHolder> {

    public static final String TAG = "OptionIconsAdapter";
    List<IconTextWrapper> dataSet;
    LayoutInflater layoutInflater;
    ClickListener clickListener;

    public MainMenuDialogAdapter(Context context, List<IconTextWrapper> dataSet, ClickListener clickListener) {

        this.layoutInflater = LayoutInflater.from(context);
        this.dataSet = dataSet == null ? new ArrayList<>() : dataSet;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_dialog_menu_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.image.setImageResource(dataSet.get(position).getDrawable());
        holder.text.setText(dataSet.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.itemDialogOptionIcons_text);
            image = itemView.findViewById(R.id.itemDialogOptionIcons_image);

            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
