package de.wpavelev.scorecounter2.model.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.wpavelev.scorecounter2.model.database.MyDatabase;
import de.wpavelev.scorecounter2.model.stuff.Player;


public class PlayerRepository {


    private PlayerDao playerDao;
    private LiveData<Player> selectedPlayer;

    private LiveData<List<Player>> allPlayers;

    private LiveData[] player = new LiveData[4];
    public PlayerRepository(Application application) {
        MyDatabase database = MyDatabase.getInstance(application);
        playerDao = database.playerDao();
        allPlayers = playerDao.getAllPlayer();
        player[0]= playerDao.getPlayer(1);
        player[1] = playerDao.getPlayer(2);
        player[2]= playerDao.getPlayer(3);
        player[3] = playerDao.getPlayer(4);


    }


    public void insert(Player player) {
        new InsertPlayerAsynchTask(playerDao).execute(player);
    }

    public void update(Player player) {
        new UpdatePlayerAsynchTask(playerDao).execute(player);
    }

    public void delete(Player player) {
        new DeletePlayerAsynchTask(playerDao).execute(player);
    }

    public void deleteAll() {
        new DeleteAllPlayersAsynchTask(playerDao).execute();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }


    private static class InsertPlayerAsynchTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao playerDao;

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

        private PlayerDao playerDao;

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

        private PlayerDao playerDao;

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

        private PlayerDao playerDao;

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

