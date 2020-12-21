package de.wpavelev.scorecounter2.model.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.model.database.MyDatabase;


public class ScoreRepository {

    private static final String TAG = "ScoreRepository";

    private ScoreDao scoreDao;

    private LiveData<List<Score>> allScores;
    private LiveData<List<Integer>> scoreSums;
    Score mScore;


    public ScoreRepository(Application application) {

        MyDatabase database = MyDatabase.getInstance(application);
        scoreDao = database.mScoreDao();
        allScores = scoreDao.getAllScores();


    }


    public void insert(Score score) {
        new InsertScoreAsynchTask(scoreDao).execute(score);
    }
    public void update(Score score) {
        new UpdateScoreAsynchTask(scoreDao).execute(score);
    }
    public void delete(Score score) {
        new DeleteScoreAsynchTask(scoreDao).execute(score);
    }
    public void deleteAll() {
        new DeleteAllScoresAsynchTask(scoreDao).execute();
    }

    public LiveData<List<Score>> getAllScores() {
        return allScores;
    }



    private static class InsertScoreAsynchTask extends AsyncTask<Score, Void, Score> {

        private ScoreDao scoreDao;

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

        private ScoreDao scoreDao;

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

        private ScoreDao scoreDao;

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

        private ScoreDao scoreDao;

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

