package de.wpavelev.scorecounter2.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scorecounter2.R;
import com.example.scorecounter2.databinding.ActivityMainBinding;
import com.facebook.stetho.Stetho;

import de.wpavelev.scorecounter2.dialogs.MainMenuDialog;
import de.wpavelev.scorecounter2.dialogs.PlayerNumberDialog;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.ui.fragments.MainFragment;
import de.wpavelev.scorecounter2.util.DisplayUtil;
import de.wpavelev.scorecounter2.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SC2: MainActivity";

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        int mainContainer = binding.mainActivityContainer.getId();

        Stetho.initializeWithDefaults(this);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        SharedPreferences sharedPreferences = this.getPreferences(MODE_PRIVATE);
        int playerLimitDefault = getResources().getInteger(R.integer.default_value_player_limit);
        int playerLimit = sharedPreferences.getInt(getString(R.string.key_player_limit), playerLimitDefault);
        viewModel.setPlayerLimit(playerLimit);

        int activePlayerDefault = getResources().getInteger(R.integer.default_value_acitve_player);
        int activePlayer = sharedPreferences.getInt(getString(R.string.key_acitve_player), activePlayerDefault);
        viewModel.setActivePlayer(activePlayer);

        viewModel.getPlayers().observe(this, players -> {
            Log.d(TAG, "onCreate: players_size " + players.size());

            if (players.size() < 4) {
                viewModel.insertPlayer(new Player("Player " + (players.size() + 1)));
            }

        });

        viewModel.getNames().observe(this, names -> {
            if (names.size() == 0) {
                viewModel.insertName(new Name("Player"));
            }
        });

        viewModel.getShowMenuDialog().observe(this, aBoolean -> {
            if (aBoolean) {
                MainMenuDialog mainMenuDialog = new MainMenuDialog(position -> {
                    onMainMenuOptionSeleceted(position);
                });
                mainMenuDialog.show(getSupportFragmentManager(), "mainMenuDialog");
            }
        });

        viewModel.getScores().observe(this, scores -> {
            if (scores == null || scores.size() == 0) {
                viewModel.setSwapOn();
            }


        });





        DisplayUtil.setDisplayWidthPx(getScreenWidthInPXs(this));


        Fragment mainFragment = MainFragment.newInstance();


        getSupportFragmentManager()
                .beginTransaction()
                .add(mainContainer, mainFragment)
                .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_menu:
                viewModel.setShowMenuDialog(true);
            default:
                return super.onOptionsItemSelected(item);

        }


    }


    private void onMainMenuOptionSeleceted(int position) {
        switch (position) {
            case 0:
                newGame();
                break;

            case 1:
                showDialogPlayerLimit();
                break;

            case 2:
                break;

            default:

        }
    }

    // Custom method to get screen width in dp/dip using Context object
    public static int getScreenWidthInPXs(Context context){

        DisplayMetrics dm = new DisplayMetrics();


        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = Math.round(dm.widthPixels / dm.density);
        return dm.widthPixels;
    }

    // Custom method to get screen height in dp/dip using Context object
    public static int getScreenHeightInDPs(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        int heightInDP = Math.round(dm.heightPixels / dm.density);
        return heightInDP;
    }


    private void newGame() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.start_new_game)

                .setPositiveButton("OK", (dialog, id) -> {
                    viewModel.resetScores();

                })

                .setNegativeButton("NOOOOO", (dialog, id) -> {

                });

        builder.create();
        builder.show();


    }

    private void showDialogPlayerLimit() {
        PlayerNumberDialog dialog = new PlayerNumberDialog(position -> {
            if (position == 3) {
                Toast.makeText(this, "so viele?!", Toast.LENGTH_SHORT).show();
            }
            viewModel.setPlayerLimit(position+2);
        });

        dialog.show(getSupportFragmentManager(), "PlayerNumberDialog");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.key_acitve_player), viewModel.getActivePlayer().getValue());
        editor.putInt(getString(R.string.key_player_limit), viewModel.getPlayerLimit().getValue());
        editor.commit();
    }
}
