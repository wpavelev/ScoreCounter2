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

import de.wpavelev.scorecounter2.adapters.IconTextWrapper;
import de.wpavelev.scorecounter2.adapters.MainMenuDialogAdapter;

public class MainMenuDialog extends DialogFragment {

    private static final String TAG = "SC2: MainMenuDialog";

    MenuOptionSelected listener;

    public interface MenuOptionSelected {
        void onOptionSelected(int position);


    }

    public MainMenuDialog(MenuOptionSelected listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_menu_main, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.dialogOptionIconsRecycler);
        MainMenuDialogAdapter mainMenuDialogAdapter = new MainMenuDialogAdapter(getContext(),
                createMainMenuDialogData(), (v, position) -> {
            customOptionMenuSelected(position);
            getDialog().dismiss();
        });

        recyclerView.setAdapter(mainMenuDialogAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));


        return view;

    }

    private List<IconTextWrapper> createMainMenuDialogData() {
        TypedArray array = getResources().obtainTypedArray(R.array.option_drawable_ids);

        String[] text = getResources().getStringArray(R.array.option_caption);

        List<IconTextWrapper> data = new ArrayList<>();

        for (int i = 0; i < text.length; i++) {
            data.add(new IconTextWrapper(text[i], array.getResourceId(i, -1)));
        }

        return data;

    }


    private void customOptionMenuSelected(int position) {
        this.listener.onOptionSelected(position);


    }


}
