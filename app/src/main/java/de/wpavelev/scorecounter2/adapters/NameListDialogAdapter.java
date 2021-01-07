package de.wpavelev.scorecounter2.adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;

import java.util.ArrayList;
import java.util.List;

import de.wpavelev.scorecounter2.model.data.Name;

public class NameListDialogAdapter extends RecyclerView.Adapter<NameListDialogAdapter.MyViewHolder> {

    private static final String TAG = "NameListDialogAdapter";
    private List<Name> mNameList;
    private final LayoutInflater mLayoutInflater;

    private final ClickListener mClickListener;
    public interface ClickListener{

        void onItemClick(View v, Name name);

        void onItemEdit(View v, Name name);

        void onItemDelete(View v, Name name);
    }



    public NameListDialogAdapter(Context context, List<Name> nameList, ClickListener clickListener) {

        this.mClickListener = clickListener;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mNameList = nameList == null ? new ArrayList<>() : nameList;

    }

    public void setNameList(List<Name> nameList) {
        this.mNameList = nameList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.item_namelist, parent, false);

        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textViewItem.setText(mNameList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return mNameList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewItem = itemView.findViewById(R.id.tv_name_item);
            ImageButton editName = itemView.findViewById(R.id.imageButton_EditName);
            ImageButton deleteName = itemView.findViewById(R.id.imageButton_DeleteName);
            if (mClickListener != null) {
                textViewItem.setOnClickListener(this);
                editName.setOnClickListener(this);
                deleteName.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if (mClickListener == null) {
                Log.d(TAG, "onClick: ");
                return;
            }

            switch (v.getId()) {
                case R.id.tv_name_item:
                    Log.d(TAG, "onClick: TV");
                    mClickListener.onItemClick(v, mNameList.get(getAdapterPosition()));
                    break;
                case R.id.imageButton_EditName:
                    Log.d(TAG, "onClick: Edit");
                    mClickListener.onItemEdit(v, mNameList.get(getAdapterPosition()));
                    break;

                case R.id.imageButton_DeleteName:
                    Log.d(TAG, "onClick: Delete");
                    mClickListener.onItemDelete(v, mNameList.get(getAdapterPosition()));
                    break;
            }

        }



    }


}
