package de.wpavelev.scorecounter2.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.scorecounter2.R;

import java.util.Objects;

public class PlayerCountDialog extends DialogFragment {

    PlayerNumberSelectedListener mPlayerNumberSelectedListener;

    public interface PlayerNumberSelectedListener {
        void playerSelected(int count);
    }

    public PlayerCountDialog(PlayerNumberSelectedListener mPlayerNumberSelectedListener) {
        this.mPlayerNumberSelectedListener = mPlayerNumberSelectedListener;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_player_count, null);

        ImageButton[] buttons = new ImageButton[4];

        buttons[0] = view.findViewById(R.id.imageButton6);
        buttons[1] = view.findViewById(R.id.imageButton7);
        buttons[2] = view.findViewById(R.id.imageButton8);

        buttons[0].setOnClickListener(v -> setPlayerCount(2));
        buttons[1].setOnClickListener(v -> setPlayerCount(3));
        buttons[2].setOnClickListener(v -> setPlayerCount(4));

        builder.setView(view).setTitle("Spieleranzahl ändern")

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        Dialog dialog = builder.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    private void setPlayerCount(int count) {
        if (mPlayerNumberSelectedListener != null) {
            mPlayerNumberSelectedListener.playerSelected(count);
        }
        Objects.requireNonNull(getDialog()).cancel();
    }

}
