package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;

import java.util.ArrayList;
import java.util.List;

import de.wpavelev.scorecounter2.model.stuff.Name;

public class NameListDialogAdapter extends RecyclerView.Adapter<NameListDialogAdapter.MyViewHolder> {

    private String TAG = "SimpleNameListAdapter";
    private List<Name> dataset;
    private LayoutInflater layoutInflater;

    private ClickListener clickListener;

    public interface ClickListener{

        void onItemClick(View v, Name name);
    }

    public NameListDialogAdapter(Context context, List<Name> nameList, ClickListener clickListener) {

        this.clickListener = clickListener;
        this.layoutInflater = LayoutInflater.from(context);
        this.dataset = nameList == null ? new ArrayList<Name>() : nameList;

    }

    public void setDataset(List<Name> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.item_namelist, parent, false);

        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textViewItem.setText(dataset.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewItem = itemView.findViewById(R.id.tv_name_item);

            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, dataset.get(getAdapterPosition()));

            }
        }

    }


}
