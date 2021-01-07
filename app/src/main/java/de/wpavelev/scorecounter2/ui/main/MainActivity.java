package de.wpavelev.scorecounter2.ui.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ActivityMainBinding;

import java.util.List;

import de.wpavelev.scorecounter2.dialogs.ChartDialog;
import de.wpavelev.scorecounter2.dialogs.PlayerCountDialog;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.PlayerAction;
import de.wpavelev.scorecounter2.model.data.PlayerWithScore;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.ui.fragments.MainFragment;
import de.wpavelev.scorecounter2.util.ColorUtil;
import de.wpavelev.scorecounter2.util.DisplayUtil;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {


    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.scorecounter2.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        int mainContainer = binding.mainActivityContainer.getId();


        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);


        SharedPreferences sharedPreferences = this.getPreferences(MODE_PRIVATE);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            DisplayUtil.setDisplayWidthPx(getScreenWidthInPXs(this) / 2);
        } else {

            DisplayUtil.setDisplayWidthPx(getScreenWidthInPXs(this));
        }
        //DisplayUtil.setDisplayWidthPx(getScreenWidthInPXs(this));

        ColorUtil.setActivePlayerArrayColor(getResources().obtainTypedArray(R.array.player_color));
        ColorUtil.setInactivePlayerArrayColor(getResources().obtainTypedArray(R.array.inactive_player_color));

        int playerLimitDefault = getResources().getInteger(R.integer.default_value_player_limit);
        int playerLimit = sharedPreferences.getInt(getString(R.string.key_player_limit), playerLimitDefault);
        mViewModel.setPlayerLimit(playerLimit);

        int activePlayerDefault = getResources().getInteger(R.integer.default_value_acitve_player);
        int activePlayer = sharedPreferences.getInt(getString(R.string.key_acitve_player), activePlayerDefault);
        mViewModel.setActivePlayer(activePlayer);

        boolean showMainScoreDefault = true;
        boolean showMainScore = sharedPreferences.getBoolean(getString(R.string.key_showmainscore), showMainScoreDefault);
        mViewModel.setIsShowMainScoreAllowed(showMainScore);

        mViewModel.getPlayers().observe(this, players -> {
            if (players.size() < 4) {
                mViewModel.insertPlayer(new Player("Player " + players.size() + 1));

            }
        });

        mViewModel.getPlayerWithScore().observe(this, playerWithScoreList -> {

            for (PlayerWithScore playerWithScore : playerWithScoreList) {
                playerWithScore.mPlayer.setScore(getPlayerEndScore(playerWithScore.mPlayerScores));
            }


        });

        mViewModel.getNames().observe(this, names -> {
            if (names.size() == 0) {
                mViewModel.insertName(new Name("Player"));
            }
        });


        mViewModel.getScores().observe(this, scores -> {
            if (scores == null || scores.size() == 0) {
                mViewModel.setSwap(true);
            }

        });

        mViewModel.getPlayerActions().observe(this, playerActions -> {
            if (playerActions.size() == 0) {
                mViewModel.insertPlayerAction(new PlayerAction(0, 0));
            }

        });


        Fragment mainFragment = MainFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(mainContainer, mainFragment)
                .commit();


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                mViewModel.onClickDigit(0);
                return true;

            case KeyEvent.KEYCODE_1:
                mViewModel.onClickDigit(1);
                return true;

            case KeyEvent.KEYCODE_2:
                mViewModel.onClickDigit(2);
                return true;

            case KeyEvent.KEYCODE_3:
                mViewModel.onClickDigit(3);
                return true;

            case KeyEvent.KEYCODE_4:
                mViewModel.onClickDigit(4);
                return true;

            case KeyEvent.KEYCODE_5:
                mViewModel.onClickDigit(5);
                return true;

            case KeyEvent.KEYCODE_6:
                mViewModel.onClickDigit(6);
                return true;

            case KeyEvent.KEYCODE_7:
                mViewModel.onClickDigit(7);
                return true;

            case KeyEvent.KEYCODE_8:
                mViewModel.onClickDigit(8);
                return true;

            case KeyEvent.KEYCODE_9:
                mViewModel.onClickDigit(9);
                return true;

            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                mViewModel.onClickSubmit();
                return true;

            case KeyEvent.KEYCODE_DEL:
            case KeyEvent.KEYCODE_FORWARD_DEL:
                mViewModel.undoLastAction();
                return true;

            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    private int getPlayerEndScore(List<Score> scoreList) {
        int endscore = 0;
        for (Score playerScore : scoreList) {
            endscore += playerScore.getScore();
        }
        return endscore;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_show_scores:
                if (mViewModel.getIsShowMainScoreAllowed().getValue() != null) {
                    mViewModel.setIsShowMainScoreAllowed(!mViewModel.getIsShowMainScoreAllowed().getValue());
                }
                break;
            case R.id.menu_player_count:
                showDialogPlayerLimit();
                break;
            case R.id.menu_newgame:
                newGame();
                break;
            case R.id.menu_chart:
                showDialogChart();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);

    }

    private void newGame() {
        new AlertDialog.Builder(this).setTitle("Neues Spiel starten?")
                .setPositiveButton("Yes", (dialog, which) -> mViewModel.newGame())
                .setNegativeButton("Nooooo", (dialog, which) -> dialog.cancel())
                .show();


    }


    // Custom method to get screen width in dp/dip using Context object
    public static int getScreenWidthInPXs(Context context) {

        DisplayMetrics dm = new DisplayMetrics();


        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
//        int widthInDP = Math.round(dm.widthPixels / dm.density);
        return dm.widthPixels;
    }

    // Custom method to get screen height in dp/dip using Context object
    public static int getScreenHeightInPXs(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        //int heightInDP = Math.round(dm.heightPixels / dm.density);
        return dm.heightPixels;
    }

    private void showDialogChart() {
        ChartDialog dialog = new ChartDialog(mViewModel.getPlayerWithScore().getValue());
        dialog.show(getSupportFragmentManager(), "ChartDialog");

    }


    private void showDialogPlayerLimit() {
        PlayerCountDialog dialog = new PlayerCountDialog(count -> mViewModel.setPlayerLimit(count));

        dialog.show(getSupportFragmentManager(), "PlayerNumberDialog");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (mViewModel != null) {

            if (mViewModel.getActivePlayer().getValue() != null) {
                editor.putInt(getString(R.string.key_acitve_player), mViewModel.getActivePlayer().getValue());
            }
            if (mViewModel.getPlayerLimit().getValue() != null) {
                editor.putInt(getString(R.string.key_player_limit), mViewModel.getPlayerLimit().getValue());
            }
            if (mViewModel.getIsShowMainScoreAllowed().getValue() != null) {
                editor.putBoolean(getString(R.string.key_showmainscore), mViewModel.getIsShowMainScoreAllowed().getValue());
            }
        }
        editor.apply();
    }
}
