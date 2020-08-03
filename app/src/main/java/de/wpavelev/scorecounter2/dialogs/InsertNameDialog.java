package de.wpavelev.scorecounter2.dialogs;

import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.scorecounter2.R;

import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class InsertNameDialog extends DialogFragment {

    private static final String TAG = "SC2: InsertNameDialog";

    private MainViewModel viewModel;
    private Name name;
    private Player player;

    private  int mode = 0;

    private static final int MODE_PLAYER = 1;
    private static final int MODE_NAME = 2;


    public interface ChangeName {
        void onClickOk(Name name);

    }
    public interface ChangePlayer {
        void onClickOk(Player player);

    }

    ChangeName nameListener;
    ChangePlayer playerListener;
    InputMethodManager inputMethodManager;


    public InsertNameDialog(Name name, ChangeName nameListener) {

        this.name = name;
        this.nameListener = nameListener;
        this.mode = MODE_NAME;
    }

    public InsertNameDialog(Player player, ChangePlayer playerListener) {

        this.player = player;
        this.playerListener = playerListener;
        this.mode = MODE_PLAYER;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.dialog_edit_name, container, false);

        getDialog().setCancelable(true);

        EditText et = view.findViewById(R.id.dialog_edit_name_tv_name);



        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.requestFocus();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


        ImageButton buttonOkay = view.findViewById(R.id.dialog_edit_name_button_commit);

        buttonOkay.setOnClickListener(v -> {
            String input = et.getText().toString();

            switch (mode) {
                case MODE_NAME:
                    name.setName(input);
                    nameListener.onClickOk(name);
                    break;
                case MODE_PLAYER:
                    player.setName(input);
                    playerListener.onClickOk(player);
                    break;
            }

            getDialog().dismiss();
        });

        return view;

    }




    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
