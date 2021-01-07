package de.wpavelev.scorecounter2.model.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.PlayerWithScore;
import de.wpavelev.scorecounter2.model.database.MyDatabase;
import de.wpavelev.scorecounter2.model.data.Player;


public class PlayerRepository {


    private final PlayerDao mPlayerDao;

    private final LiveData<List<Player>> mPlayerList;
    private final LiveData<List<PlayerWithScore>> mPlayerWithScore;

    public PlayerRepository(Application application) {
        MyDatabase database = MyDatabase.getInstance(application);
        mPlayerDao = database.mPlayerDao();
        mPlayerList = mPlayerDao.getPlayers();
        mPlayerWithScore = mPlayerDao.getPlayerWithScore();

    }


    public void insert(Player player) {
        new InsertPlayerAsynchTask(mPlayerDao).execute(player);
    }

    public void update(Player player) {
        new UpdatePlayerAsynchTask(mPlayerDao).execute(player);
    }

    public void delete(Player player) {
        new DeletePlayerAsynchTask(mPlayerDao).execute(player);
    }

    public void deleteAll() {
        new DeleteAllPlayersAsynchTask(mPlayerDao).execute();
    }

    public LiveData<List<Player>> getPlayers() {
        return mPlayerList;
    }
    public List<Player> getPlayerList() {
        return mPlayerDao.getPlayerList();
    }

    public LiveData<List<PlayerWithScore>> getPlayerWithScore() {
        return mPlayerWithScore;
    }
    public Player getPlayerById (int playerId) {
        return mPlayerDao.getPlayerById(playerId);
    }
    public List<Player> getPlayerOnce() {
        return mPlayerDao.getPlayerList();
    }
    private static class InsertPlayerAsynchTask extends AsyncTask<Player, Void, Void> {

        private final PlayerDao playerDao;

        private InsertPlayerAsynchTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }


        @Override
        protected Void doInBackground(Player... players) {
            playerDao.insert(players[0]);
            return null;
        }
    }

    private static class UpdatePlayerAsynchTask extends AsyncTask<Player, Void, Void> {

        private final PlayerDao playerDao;

        private UpdatePlayerAsynchTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... players) {

            playerDao.update(players[0]);
            return null;
        }
    }

    private static class DeletePlayerAsynchTask extends AsyncTask<Player, Void, Void> {

        private final PlayerDao playerDao;

        private DeletePlayerAsynchTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... players) {

            playerDao.delete(players[0]);
            return null;
        }
    }

    private static class DeleteAllPlayersAsynchTask extends AsyncTask<Player, Void, Void> {

        private final PlayerDao playerDao;

        private DeleteAllPlayersAsynchTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... players) {

            playerDao.deleteAll();
            return null;
        }
    }


}

