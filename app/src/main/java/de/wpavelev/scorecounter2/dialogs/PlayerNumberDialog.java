package de.wpavelev.scorecounter2.dialogs;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scorecounter2.R;

import java.util.ArrayList;
import java.util.List;

import de.wpavelev.scorecounter2.adapters.MainMenuDialogAdapter;
import de.wpavelev.scorecounter2.adapters.IconTextWrapper;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class PlayerNumberDialog extends DialogFragment {

    private static final String TAG = "SC2: PlayerNumberDialog";

    PlayerNumberSelectedListener listener;

    public interface PlayerNumberSelectedListener {
        void playerSelected(int position);
    }

    public PlayerNumberDialog(PlayerNumberSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_menu_main, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.dialogOptionIconsRecycler);
        MainMenuDialogAdapter mainMenuDialogAdapter = new MainMenuDialogAdapter(getContext(),
                createPlayerNumberData(), (v, position) -> {
            listener.playerSelected(position);
            getDialog().dismiss();

        });

        recyclerView.setAdapter(mainMenuDialogAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));


        return view;

    }


    private List<IconTextWrapper> createPlayerNumberData() {
        TypedArray array = getResources().obtainTypedArray(R.array.player_number_image);

        String[] text = getResources().getStringArray(R.array.player_number);

        List<IconTextWrapper> data = new ArrayList<>();

        for (int i = 0; i < text.length; i++) {
            data.add(new IconTextWrapper(text[i], array.getResourceId(i, -1)));
        }

        return data;

    }
}
