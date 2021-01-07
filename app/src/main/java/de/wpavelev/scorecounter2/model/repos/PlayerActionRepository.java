package de.wpavelev.scorecounter2.model.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.PlayerAction;
import de.wpavelev.scorecounter2.model.database.MyDatabase;

public class PlayerActionRepository {

    private PlayerActionDao mPlayerActionDao;
    
    private LiveData<List<PlayerAction>> mAllPlayerActions;


    public PlayerActionRepository(Application application) {

        MyDatabase database = MyDatabase.getInstance(application);
        mPlayerActionDao = database.mPlayerActionDao();
        mAllPlayerActions = mPlayerActionDao.getPlayerActions();

    }


    public LiveData<List<PlayerAction>> getAllPlayerActions() {
        return mAllPlayerActions;
    }

    public PlayerAction getLastPlayerAction() {
        return mPlayerActionDao.getLastAction();
    }

    public void insert(PlayerAction playerAction) {
        new PlayerActionRepository.InsertPlayerActionAsynchTask(mPlayerActionDao).execute(playerAction);
    }
    public void update(PlayerAction playerAction) {
        new PlayerActionRepository.UpdatePlayerActionAsynchTask(mPlayerActionDao).execute(playerAction);
    }
    public void delete(PlayerAction playerAction) {
        new PlayerActionRepository.DeletePlayerActionAsynchTask(mPlayerActionDao).execute(playerAction);
    }
    public void deleteAll() {
        new PlayerActionRepository.DeleteAllPlayerActionsAsynchTask(mPlayerActionDao).execute();
    }





    private static class InsertPlayerActionAsynchTask extends AsyncTask<PlayerAction, Void, Void> {

        private final PlayerActionDao playerActionDao;

        private InsertPlayerActionAsynchTask(PlayerActionDao playerActionDao) {
            this.playerActionDao = playerActionDao;
        }


        @Override
        protected Void doInBackground(PlayerAction... playerActions) {
            playerActionDao.insert(playerActions[0]);
            return null;
        }
    }
    private static class UpdatePlayerActionAsynchTask extends AsyncTask<PlayerAction, Void, Void> {

        private final PlayerActionDao playerActionDao;

        private UpdatePlayerActionAsynchTask(PlayerActionDao playerActionDao) {
            this.playerActionDao = playerActionDao;
        }

        @Override
        protected Void doInBackground(PlayerAction... playerActions) {

            playerActionDao.update(playerActions[0]);
            return null;
        }
    }
    private static class DeletePlayerActionAsynchTask extends AsyncTask<PlayerAction, Void, Void> {

        private final PlayerActionDao playerActionDao;

        private DeletePlayerActionAsynchTask(PlayerActionDao playerActionDao) {
            this.playerActionDao = playerActionDao;
        }

        @Override
        protected Void doInBackground(PlayerAction... playerActions) {

            playerActionDao.delete(playerActions[0]);
            return null;
        }
    }
    private static class DeleteAllPlayerActionsAsynchTask extends AsyncTask<PlayerAction, Void, Void> {

        private final PlayerActionDao playerActionDao;

        private DeleteAllPlayerActionsAsynchTask(PlayerActionDao playerActionDao) {
            this.playerActionDao = playerActionDao;
        }

        @Override
        protected Void doInBackground(PlayerAction... playerActions) {

            playerActionDao.deleteAll();
            return null;
        }
    }

}
