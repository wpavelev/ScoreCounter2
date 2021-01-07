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

import java.util.Objects;

import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;

public class InsertNameDialog extends DialogFragment {


    private Name mName;
    private Player mPlayer;

    private int mMode = 0;

    private static final int MODE_PLAYER = 1;
    private static final int MODE_NAME = 2;


    public interface ChangeNameListener {
        void onClickOk(Name name);

    }

    public interface ChangePlayerListener {
        void onClickOk(Player player);

    }

    ChangeNameListener mChangeNameListenerListener;
    ChangePlayerListener mChangePlayerListenerListener;
    InputMethodManager mInputMethodManager;


    public InsertNameDialog(Name name, ChangeNameListener mChangeNameListener) {

        this.mName = name;
        this.mChangeNameListenerListener = mChangeNameListener;
        this.mMode = MODE_NAME;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_edit_name, container, false);

        Objects.requireNonNull(getDialog()).setCancelable(true);

        EditText et = view.findViewById(R.id.dialog_edit_name_tv_name);


        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.requestFocus();
        mInputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Service.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


        ImageButton buttonOkay = view.findViewById(R.id.dialog_edit_name_button_commit);

        buttonOkay.setOnClickListener(v -> {
            String input = et.getText().toString();

            switch (mMode) {
                case MODE_NAME:
                    mName.setName(input);
                    mChangeNameListenerListener.onClickOk(mName);
                    break;
                case MODE_PLAYER:
                    mPlayer.setName(input);
                    mChangePlayerListenerListener.onClickOk(mPlayer);
                    break;
            }

            getDialog().dismiss();
        });

        return view;

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
