package de.wpavelev.scorecounter2.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.scorecounter2.R;

import java.util.List;

import de.wpavelev.scorecounter2.model.repos.NameRepository;
import de.wpavelev.scorecounter2.model.repos.PlayerRepository;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.model.repos.ScoreRepository;
import de.wpavelev.scorecounter2.util.SingleLiveEvent;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    public static final int INPUTMODE_DEFAULT = 0;
    public static final int INPUTMODE_EDIT_SCORE = 1;


    private NameRepository nameRepository;
    private PlayerRepository playerRepository;
    private ScoreRepository scoreRepository;

    private LiveData<List<Name>> names;
    private LiveData<List<Player>> players;
    private LiveData<List<Score>> scores;

    /**
     * Gibt an, ob die Scores in der UI angezeigt werden.
     */
    private MutableLiveData<Boolean> showScore = new MutableLiveData<>();

    /**
     * Die Zahl, die gerade eingegeben wird / wurde. Diese wird einem Spieler als Punktzahl der a
     * aktuellen Runde zugewiesen
     */
    private MutableLiveData<Integer> currentScore = new MutableLiveData<>();

    /**
     * Indikator für einen Lonkclick auf einen Spieler X.
     * Wert zu Beginn ist -1.
     */
    private MutableLiveData<Integer> longClickPlayer = new MutableLiveData<>();

    /**
     * Spieler, der aktuell an der Reihe ist.
     */
    private MutableLiveData<Integer> activePlayer = new MutableLiveData<>();

    /**
     * Beschränkung auf eine Anzahl von Spielern von 2 bis x
     */
    private MutableLiveData<Integer> playerLimit = new MutableLiveData<>();

    /**
     * Indikator für die Erlaubnis an Rotation der Spieler. (Darf nur vor
     * der ersten Runde geschehen)
     */
    private MutableLiveData<Boolean> isSwappingAllowed = new MutableLiveData<>();


    private SingleLiveEvent<Boolean> showEditNameDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> showMenuDialog = new SingleLiveEvent<>();

    /**
     * Die Zuordnung des Scores zur Datenbank. Hier wird der neue Wert eingefügt, in die Datenbank
     * geschickt und geupdatet.
     */
    private Score editScore = new Score(0,0);


    /**
     * Gibt an, wohin die eingebeben Zahlen geschickt werden
     * 0 = Standard (Current Score)
     * 1 = Score Bearbeiten
     */
    private int inputMode = INPUTMODE_DEFAULT;

    public MainViewModel(@NonNull Application application) {
        super(application);

        playerRepository = new PlayerRepository(application);
        nameRepository = new NameRepository(application);
        scoreRepository = new ScoreRepository(application);

        names = nameRepository.getAllNames();
        players = playerRepository.getAllPlayers();
        scores = scoreRepository.getAllScores();


        currentScore.setValue(0);
        longClickPlayer.setValue(-1);
        activePlayer.setValue(0);
        showScore.setValue(false);

        Log.w(TAG, "MainViewModel: playerLimit wird zu Beginn auf 4 gesetzt");
        setPlayerLimit(4);
        isSwappingAllowed.setValue(false);


    }


    //<editor-fold desc="Getter">


    public MutableLiveData<Boolean> getShowScore() {
        return showScore;
    }

    public MutableLiveData<Integer> getCurrentScore() {
        return currentScore;
    }

    public MutableLiveData<Integer> getActivePlayer() {
        return activePlayer;
    }

    public MutableLiveData<Integer> getPlayerLimit() {
        return playerLimit;
    }

    public MutableLiveData<Integer> getLongClickPlayer() {
        return longClickPlayer;
    }

    public SingleLiveEvent<Boolean> getShowEditNameDialog() {
        return showEditNameDialog;
    }

    public SingleLiveEvent<Boolean> getShowMenuDialog() {
        return showMenuDialog;
    }

    public LiveData<List<Player>> getPlayerLimited() {
        return Transformations.switchMap(playerLimit, limit -> Transformations.map(getPlayers(), list -> {
                    int min = Math.min(limit, list.size());
                    return list.subList(0, min);
                }
        ));

    }


    public void setShowMenuDialog(Boolean showMenuDialog) {
        this.showMenuDialog.setValue(showMenuDialog);

    }

    private int getActivePlayerId() {
        return getPlayers().getValue().get(activePlayer.getValue()).getId();
    }
    //</editor-fold>

    //<editor-fold desc="Setter">


    public void setShowScore(Boolean showScore) {
        this.showScore.setValue(showScore);
    }

    public void setEditScore(Score editScore) {
        if (this.editScore.isSelected()) {
            this.editScore.setSelected(false);
            updateScore(this.editScore);
        }

        editScore.setSelected(true);
        this.editScore = editScore;
        updateScore(this.editScore);

        setInputMode(INPUTMODE_EDIT_SCORE);
    }

    public void setInputMode(int inputMode) {
        this.inputMode = inputMode;
    }

    public void setCurrentScore(int score) {
        this.currentScore.setValue(score);
    }

    public void setLongClickPlayer(int selectedPlayer) {
        this.longClickPlayer.setValue(selectedPlayer);
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer.setValue(activePlayer);
    }

    public void setPlayerLimit(int playerLimit) {
        this.playerLimit.setValue(playerLimit);
    }

    public void setShowEditNameDialog() {
        this.showEditNameDialog.setValue(true);
    }

    public void setPlayerName(int player, String name) {
        List<Player> allplayer = getPlayers().getValue();

        if (allplayer != null && allplayer.size() > player) {
            Log.d(TAG, "updatePlayer");


            Player tPlayer = allplayer.get(player);
            tPlayer.setName(name);
            updatePlayer(tPlayer);

        } else {
            Log.d(TAG, "inserPlayer");
            Player newPlayer = new Player(name);
            insertPlayer(newPlayer);

        }
    }

    public void setSwap(boolean onOff) {
        isSwappingAllowed.setValue(onOff);
        // TODO: 10.08.2020 Swapbutton
    }



    //</editor-fold>


    public void onClickSubmit() {

        setSwap(false);
        int score = getCurrentScore().getValue();
        switch (inputMode) {
            case INPUTMODE_EDIT_SCORE:

                editScore.setScore(score);
                editScore.setSelected(false);
                updateScore(editScore);
                setInputMode(INPUTMODE_DEFAULT);
                setCurrentScore(0);
                break;

            default:
                //Score Speichern
                int activePlayer = getActivePlayer().getValue();

                insertScore(new Score(getActivePlayerId(), score));

                //update Player Values
                Player player = getPlayers().getValue().get(activePlayer);
                player.setScore(player.getScore() + score);

                updatePlayer(player);


                //Spieler weiterschalten
                if (activePlayer < getPlayerLimit().getValue() - 1) {
                    activePlayer++;
                } else {
                    activePlayer = 0;
                }


                setCurrentScore(0);
                setActivePlayer(activePlayer);
        }



    }

    public void onClickDigit(int digit) {
        int number;
        number = getCurrentScore().getValue();
        number = number * 10 + digit;
        setCurrentScore(number);



     /*   switch (inputMode) {
            case INPUTMODE_EDIT_SCORE:
                int score = getCurrentScore().getValue();
                editScore.setScore(score);
                updateScore(editScore);
                break;

            default:
                setCurrentScore(number);
                break;

        }*/


    }

    public void newGame() {
        List<Player> players_temp = players.getValue();
        for (Player player : players_temp) {
            player.setScore(0);
            updatePlayer(player);
        }
        setSwap(true);
        setActivePlayer(0);
        deleteAllScores();
    }

    //<editor-fold desc="Names (Repo)">
    public LiveData<List<Name>> getNames() {
        return names;
    }

    public void insertName(Name name) {
        nameRepository.insert(name);

    }

    public void updateName(Name name) {
        nameRepository.update(name);

    }

    public void deleteName(Name name) {
        nameRepository.delete(name);

    }

    public void deleteAllNames() {
        nameRepository.deleteAll();
    }
    //</editor-fold>

    //<editor-fold desc="Players (Repo)">
    public void insertPlayer(Player player) {
        playerRepository.insert(player);
    }

    public void updatePlayer(Player player) {
        playerRepository.update(player);
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    public void deleteAllPlayer() {
        playerRepository.deleteAll();
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }
    //</editor-fold>

    //<editor-fold desc="Scores (Repo)">

    public LiveData<List<Score>> getScores() {
        return scores;
    }

    public void insertScore(Score score) {
        scoreRepository.insert(score);
    }

    public void updateScore(Score score) {
        scoreRepository.update(score);
    }

    public void deleteScore(Score score) {
        scoreRepository.delete(score);
    }

    public void deleteAllScores() {
        scoreRepository.deleteAll();

    }


    //</editor-fold>


}
