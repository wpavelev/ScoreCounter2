package de.wpavelev.scorecounter2.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.PlayerAction;
import de.wpavelev.scorecounter2.model.data.PlayerWithScore;
import de.wpavelev.scorecounter2.model.repos.NameRepository;
import de.wpavelev.scorecounter2.model.repos.PlayerActionRepository;
import de.wpavelev.scorecounter2.model.repos.PlayerRepository;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.model.repos.ScoreRepository;
import de.wpavelev.scorecounter2.util.SingleLiveEvent;

public class MainViewModel extends AndroidViewModel {

    public static final int INPUTMODE_DEFAULT = 0;
    public static final int INPUTMODE_EDIT_SCORE = 1;


    private final NameRepository mNameRepository;
    private final PlayerRepository mPlayerRepository;
    private final ScoreRepository mScoreRepository;
    private final PlayerActionRepository mPlayerActionRepository;

    private final LiveData<List<Name>> mNames;
    private final LiveData<List<Player>> mPlayers;
    private final LiveData<List<Score>> mScores;
    private final LiveData<List<PlayerAction>> mPlayerActions;
    private final LiveData<List<PlayerWithScore>> mPlayerWithScore;

    /**
     * Gibt an, ob die Scores in der UI angezeigt werden.
     */
    private boolean mShowScoreList = true;
    private final MutableLiveData<Boolean> mShowScoreListLive = new MutableLiveData<>();

    /**
     * Die Zahl, die gerade eingegeben wird / wurde. Diese wird einem Spieler als Punktzahl der a
     * aktuellen Runde zugewiesen
     */
    private final MutableLiveData<Integer> mCurrentScore = new MutableLiveData<>();
    private int mCurrentScoreInt = 0;

    /**
     * Indikator für einen Lonkclick auf einen Spieler X.
     * Wert zu Beginn ist -1.
     */
    private final MutableLiveData<Integer> mLongClickPlayer = new MutableLiveData<>();

    /**
     * Spieler, der aktuell an der Reihe ist.
     */
    private final MutableLiveData<Integer> mActivePlayer = new MutableLiveData<>();
    private int mActivePlayerInt = 0;

    /**
     * Beschränkung auf eine Anzahl von Spielern von 2 bis x
     */
    private final MutableLiveData<Integer> mPlayerLimit = new MutableLiveData<>();
    private int mPlayerLimitInt = 0;

    /**
     * Indikator für die Erlaubnis an Rotation der Spieler. (Darf nur vor
     * der ersten Runde geschehen)
     */
    private final MutableLiveData<Boolean> mIsSwappingAllowed = new MutableLiveData<>();

    /**
     * Indikator für das Zeigen der Score der Spieler (Endscore)
     */
    private final MutableLiveData<Boolean> isShowMainScoreAllowed = new MutableLiveData<>();

    /**
     *
     */
    private final SingleLiveEvent<Boolean> mShowEditNameDialog = new SingleLiveEvent<>();


    /**
     * Die Zuordnung des Scores zur Datenbank. Hier wird der neue Wert eingefügt, in die Datenbank
     * geschickt und geupdatet.
     */
    private Score mModifiedScore = new Score(0, 0);


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



        mNames = mNameRepository.getNamesList();
        mPlayers = mPlayerRepository.getPlayers();
        mScores = mScoreRepository.getScores();
        mPlayerActions = mPlayerActionRepository.getAllPlayerActions();
        mPlayerWithScore = mPlayerRepository.getPlayerWithScore();


        setCurrentScore(0);
        setLongClickPlayer(-1);
        setIsShowMainScoreAllowed(true);
        setShowScoreListLive(true);
        setActivePlayer(0);
        setPlayerLimit(4);
        setSwap(false);


    }


    //<editor-fold desc="Getter">


    public MutableLiveData<Boolean> getIsShowMainScoreAllowed() {
        return isShowMainScoreAllowed;
    }


    public MutableLiveData<Integer> getCurrentScore() {
        return mCurrentScore;
    }

    public MutableLiveData<Integer> getActivePlayer() {
        return mActivePlayer;
    }

    public MutableLiveData<Integer> getPlayerLimit() {
        return mPlayerLimit;
    }

    public MutableLiveData<Integer> getLongClickPlayer() {
        return mLongClickPlayer;
    }

    public SingleLiveEvent<Boolean> getShowEditNameDialog() {
        return mShowEditNameDialog;
    }


    public LiveData<List<Player>> getPlayerLimited() {
        return Transformations.switchMap(mPlayerLimit, limit -> Transformations.map(getPlayers(), list -> {
                    int min = Math.min(limit, list.size());
                    return list.subList(0, min);
                }
        ));

    }

    public LiveData<List<PlayerWithScore>> getPlayerWithScoreLimitedByPlayerCount() {
        return Transformations.switchMap(mPlayerLimit, limit -> Transformations.map(getPlayerWithScore(), list -> {
                    int min = Math.min(limit, list.size());
                    return list.subList(0, min);
                }
        ));
    }


    private int getActivePlayerId() {

        return getPlayerList().get(mActivePlayerInt).getPlayerId();
    }

    public boolean isShowScoreList() {
        return mShowScoreList;
    }

    public MutableLiveData<Boolean> getShowScoreListLive() {
        return mShowScoreListLive;
    }
    //</editor-fold>

    //---------------------------------------------------------------------------------------------

    //<editor-fold desc="Setter">


    public void setShowScoreListLive(Boolean showScoreList) {
        mShowScoreList = showScoreList;
        mShowScoreListLive.setValue(mShowScoreList);
    }

    public void setIsShowMainScoreAllowed(Boolean showMainScore) {
        this.isShowMainScoreAllowed.setValue(showMainScore);
    }


    public void toggleShowScoreList() {
        mShowScoreList = !mShowScoreList;
        setShowScoreListLive(mShowScoreList);
    }

    public void setModifiedScore(Score modifiedScore) {

        if (mModifiedScore.isSelected()) {

            mModifiedScore.setSelected(false);
            updateScore(mModifiedScore);
            setInputMode(INPUTMODE_DEFAULT);

            if (modifiedScore.getScoreId() != this.mModifiedScore.getScoreId()) {
                mModifiedScore = modifiedScore;
                mModifiedScore.setSelected(true);
                updateScore(mModifiedScore);
                setInputMode(INPUTMODE_EDIT_SCORE);

            }

        } else {

            mModifiedScore = modifiedScore;
            mModifiedScore.setSelected(true);
            updateScore(mModifiedScore);

            setInputMode(INPUTMODE_EDIT_SCORE);

        }
    }

    public void setInputMode(int inputMode) {
        this.inputMode = inputMode;
    }

    public void setCurrentScore(int score) {
        this.mCurrentScore.setValue(score);
        mCurrentScoreInt = score;
    }

    public void setLongClickPlayer(int selectedPlayer) {
        this.mLongClickPlayer.setValue(selectedPlayer);
    }

    public void setActivePlayer(int activePlayer) {

        this.mActivePlayer.setValue(activePlayer);
        mActivePlayerInt = activePlayer;

    }

    public void setPlayerLimit(int playerLimit) {
        this.mPlayerLimit.setValue(playerLimit);
        this.mPlayerLimitInt = playerLimit;
    }

    public void setShowEditNameDialog() {
        this.mShowEditNameDialog.setValue(true);
    }

    public void setPlayerName(int player, String name) {

        List<Player> allplayer = getPlayerList();

        if (allplayer != null && allplayer.size() > player) {

            Player tPlayer = allplayer.get(player);
            tPlayer.setName(name);
            updatePlayer(tPlayer);

        } else {
            Player newPlayer = new Player(name);
            insertPlayer(newPlayer);

        }
    }

    public void setSwap(boolean onOff) {
        mIsSwappingAllowed.setValue(onOff);
        // TODO: 10.08.2020 Swapbutton
    }


    //</editor-fold>

    //---------------------------------------------------------------------------------------------

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

    public LiveData<List<PlayerWithScore>> getPlayerWithScore() {
        return mPlayerWithScore;
    }

    private List<Player> getPlayerList() {
        return mPlayerRepository.getPlayerList();
    }

    public Player getPlayerById(int playerId) {
        return mPlayerRepository.getPlayerById(playerId);
    }

    public List<Player> getAllPlayerOnce() {
        return mPlayerRepository.getPlayerOnce();
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

    private List<Score> getScoreList() {
        return mScoreRepository.getScoreList();
    }


    //</editor-fold>

    //<editor-fold desc="PlayerAction (Repo)">

    public LiveData<List<PlayerAction>> getPlayerActions() {
        return mPlayerActions;
    }

    public PlayerAction getLastPlayerAction() {
        return mPlayerActionRepository.getLastPlayerAction();
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


    public void undoLastAction() {

        if (mCurrentScoreInt != 0) {
            setCurrentScore(0);
            return;
        }

        if (inputMode == INPUTMODE_EDIT_SCORE) {
            mModifiedScore.setSelected(false);
            updateScore(mModifiedScore);
            return;
        }

        PlayerAction playerAction = getLastPlayerAction();
        if (playerAction.getScoreId() == -1) {
            if (getScoreList().size() > 0) {
                Score score = getScoreList().get(0);
                setActivePlayer(score.getPlayer() - 1);
                deleteScore(score);
                deletePlayerAction(playerAction);
            }
        } else {
            Score score = new Score(playerAction.getPlayerChanged(), playerAction.getValueToRestore());
            score.setScoreId(playerAction.getScoreId());
            updateScore(score);
            deletePlayerAction(playerAction);
        }
    }

    public void onClickSubmit() {

        setSwap(false);
        int score = mCurrentScoreInt;
        switch (inputMode) {
            case INPUTMODE_EDIT_SCORE:

                //Store previous value for history
                int oldValue = mModifiedScore.getScore();



                mModifiedScore.setScore(score);     //set score-Value in temp
                mModifiedScore.setSelected(false);  //deselect score

                insertPlayerAction(new PlayerAction(oldValue, mModifiedScore.getPlayer(), mModifiedScore.getScoreId()));
                updateScore(mModifiedScore);

                setInputMode(INPUTMODE_DEFAULT);
                setCurrentScore(0);
                break;

            default:
                //Score Speichern
                int activePlayer = mActivePlayerInt;

                insertPlayerAction(new PlayerAction(score, getActivePlayerId()));
                insertScore(new Score(getActivePlayerId(), score));


                //Spieler weiterschalten
                if (activePlayer < mPlayerLimitInt - 1) {
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
        number = mCurrentScoreInt;
        number = number * 10 + digit;
        setCurrentScore(number);


    }

    public void onCLickQwirkle() {
        setCurrentScore(mCurrentScoreInt + 12);
        Player player = getPlayerById(getActivePlayerId());
        player.setQwirkle(player.getQwirkle() + 1);
        updatePlayer(player);
    }

    public void newGame() {
        List<Player> players_temp = getPlayerList();
        for (Player player : players_temp) {
            updatePlayer(player);
        }
        setSwap(true);
        setActivePlayer(0);
        deleteAllScores();
        deleteAllPlayerActions();
    }

}
