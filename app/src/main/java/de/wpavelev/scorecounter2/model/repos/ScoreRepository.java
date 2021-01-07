package de.wpavelev.scorecounter2.model.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.model.database.MyDatabase;


public class ScoreRepository {

    private final ScoreDao mScoreDao;

    private final LiveData<List<Score>> mScoreList;

    public ScoreRepository(Application application) {

        MyDatabase database = MyDatabase.getInstance(application);
        mScoreDao = database.mScoreDao();
        mScoreList = mScoreDao.getScores();


    }


    public void insert(Score score) {
        new InsertScoreAsynchTask(mScoreDao).execute(score);
    }
    public void update(Score score) {
        new UpdateScoreAsynchTask(mScoreDao).execute(score);
    }
    public void delete(Score score) {
        new DeleteScoreAsynchTask(mScoreDao).execute(score);
    }
    public void deleteAll() {
        new DeleteAllScoresAsynchTask(mScoreDao).execute();
    }

    public LiveData<List<Score>> getScores() {
        return mScoreList;
    }


    public List<Score> getScoreList() {
        return mScoreDao.getScoreList();
    }

    private static class InsertScoreAsynchTask extends AsyncTask<Score, Void, Score> {

        private final ScoreDao scoreDao;

        private InsertScoreAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }


        @Override
        protected Score doInBackground(Score... scores) {
            scoreDao.insert(scores[0]);
            return null;
        }
    }
    private static class UpdateScoreAsynchTask extends AsyncTask<Score, Void, Void> {

        private final ScoreDao scoreDao;

        private UpdateScoreAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {

            scoreDao.update(scores[0]);
            return null;
        }
    }
    private static class DeleteScoreAsynchTask extends AsyncTask<Score, Void, Void> {

        private final ScoreDao scoreDao;

        private DeleteScoreAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {

            scoreDao.delete(scores[0]);
            return null;
        }
    }
    private static class DeleteAllScoresAsynchTask extends AsyncTask<Score, Void, Void> {

        private final ScoreDao scoreDao;

        private DeleteAllScoresAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {

            scoreDao.deleteAll();
            return null;
        }
    }




}

