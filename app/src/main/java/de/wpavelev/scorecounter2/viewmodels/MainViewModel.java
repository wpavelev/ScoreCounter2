package de.wpavelev.scorecounter2.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import de.wpavelev.scorecounter2.model.data.PlayerAction;
import de.wpavelev.scorecounter2.model.repos.NameRepository;
import de.wpavelev.scorecounter2.model.repos.PlayerActionRepository;
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


    private NameRepository mNameRepository;
    private PlayerRepository mPlayerRepository;
    private ScoreRepository mScoreRepository;
    private PlayerActionRepository mPlayerActionRepository;

    private LiveData<List<Name>> mNames;
    private LiveData<List<Player>> mPlayers;
    private LiveData<List<Score>> mScores;
    private LiveData<List<PlayerAction>> mPlayerActions;

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

    /**
     * Indikator für das Zeigen der Score der Spieler (Endscore)
     */
    private MutableLiveData<Boolean> isShowMainScore = new MutableLiveData<>();

    private SingleLiveEvent<Boolean> showEditNameDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> showMenuDialog = new SingleLiveEvent<>();


    /**
     * Die Zuordnung des Scores zur Datenbank. Hier wird der neue Wert eingefügt, in die Datenbank
     * geschickt und geupdatet.
     */
    private Score editScore = new Score(0, 0);


    /**
     * Gibt an, wohin die eingebeben Zahlen geschickt werden
     * 0 = Standard (Current Score)
     * 1 = Score Bearbeiten
     */
    private int inputMode = INPUTMODE_DEFAULT;


    public MainViewModel(@NonNull Application application) {
        super(application);

        mPlayerRepository = new PlayerRepository(application);
        mNameRepository = new NameRepository(application);
        mScoreRepository = new ScoreRepository(application);
        mPlayerActionRepository = new PlayerActionRepository(application);

        mNames = mNameRepository.getAllNames();
        mPlayers = mPlayerRepository.getAllPlayers();
        mScores = mScoreRepository.getAllScores();
        mPlayerActions = mPlayerActionRepository.getAllPlayerActions();


        currentScore.setValue(0);
        longClickPlayer.setValue(-1);
        activePlayer.setValue(0);
        showScore.setValue(false);
        isShowMainScore.setValue(true);


        Log.w(TAG, "MainViewModel: playerLimit wird zu Beginn auf 4 gesetzt");
        setPlayerLimit(4);
        isSwappingAllowed.setValue(false);


    }


    //<editor-fold desc="Getter">


    public MutableLiveData<Boolean> getIsShowMainScore() {
        return isShowMainScore;
    }

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


    public void setIsShowMainScore(Boolean showMainScore) {
        this.isShowMainScore.setValue(showMainScore);
    }

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

    public void undoLastAction() {
        Log.d(TAG, "undoLastAction");


        PlayerAction playerAction = getPlayerActions().getValue().get(getPlayerActions().getValue().size() - 1);
        if (playerAction.getScoreId() == -1) {
            Score score = getScores().getValue().get(0);
            deleteScore(score);
        }

    }

    public void onClickSubmit() {

        setSwap(false);
        int score = getCurrentScore().getValue();
        switch (inputMode) {
            case INPUTMODE_EDIT_SCORE:

                int oldValue = editScore.getScore();
                int newValue = score;
                int scoreDif = newValue - oldValue;

                editScore.setScore(score);
                editScore.setSelected(false);

                insertPlayerAction(new PlayerAction(scoreDif, editScore.getPlayer(), editScore.getId()));
                updateScore(editScore);

                setInputMode(INPUTMODE_DEFAULT);
                setCurrentScore(0);
                break;

            default:
                //Score Speichern
                int activePlayer = getActivePlayer().getValue();

                insertPlayerAction(new PlayerAction(score, getActivePlayerId()));
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

    public void onCLickQwirkle() {
        setCurrentScore(currentScore.getValue() + 12);
        // TODO: 02.10.2020 Qwirkle dem SPieler dazuaddieren
    }

    public void newGame() {
        List<Player> players_temp = mPlayers.getValue();
        for (Player player : players_temp) {
            player.setScore(0);
            updatePlayer(player);
        }
        setSwap(true);
        setActivePlayer(0);
        deleteAllScores();
        deleteAllPlayerActions();
    }

    //<editor-fold desc="Names (Repo)">
    public LiveData<List<Name>> getNames() {
        return mNames;
    }

    public void insertName(Name name) {
        mNameRepository.insert(name);

    }

    public void updateName(Name name) {
        mNameRepository.update(name);

    }

    public void deleteName(Name name) {
        mNameRepository.delete(name);

    }

    public void deleteAllNames() {
        mNameRepository.deleteAll();
    }
    //</editor-fold>

    //<editor-fold desc="Players (Repo)">
    public void insertPlayer(Player player) {
        mPlayerRepository.insert(player);
    }

    public void updatePlayer(Player player) {
        mPlayerRepository.update(player);
    }

    public void deletePlayer(Player player) {
        mPlayerRepository.delete(player);
    }

    public void deleteAllPlayer() {
        mPlayerRepository.deleteAll();
    }

    public LiveData<List<Player>> getPlayers() {
        return mPlayers;
    }
    //</editor-fold>

    //<editor-fold desc="Scores (Repo)">

    public LiveData<List<Score>> getScores() {
        return mScores;
    }

    public void insertScore(Score score) {
        mScoreRepository.insert(score);
    }

    public void updateScore(Score score) {

        mScoreRepository.update(score);
    }

    public void deleteScore(Score score) {
        mScoreRepository.delete(score);
    }

    public void deleteAllScores() {
        mScoreRepository.deleteAll();

    }


    //</editor-fold>

    //<editor-fold desc="PlayerAction (Repo)">

    public LiveData<List<PlayerAction>> getPlayerActions() {
        return mPlayerActions;
    }

    public void insertPlayerAction(PlayerAction playerAction) {
        mPlayerActionRepository.insert(playerAction);
    }

    public void updatePlayerAction(PlayerAction playerAction) {
        mPlayerActionRepository.update(playerAction);

    }
    public void deletePlayerAction(PlayerAction playerAction) {
        mPlayerActionRepository.delete(playerAction);

    }
    public void deleteAllPlayerActions() {
        mPlayerActionRepository.deleteAll();

    }

    //</editor-fold>

}
