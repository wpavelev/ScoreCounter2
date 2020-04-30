package de.wpavelev.scorecounter2.model.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.wpavelev.scorecounter2.model.database.MyDatabase;
import de.wpavelev.scorecounter2.model.stuff.Score;

public class ScoreRepository {

    private ScoreDao scoreDao;

    private LiveData<List<Score>> allScores;

    public ScoreRepository(Application application) {

        MyDatabase database = MyDatabase.getInstance(application);

        this.scoreDao = database.scoreDao();
        this.allScores = scoreDao.getAllScores();
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
        new DeleteAllScoreAsynchTask(scoreDao).execute();

    }

    public LiveData<List<Score>> getAllScores() {
        return allScores;
    }

    private static class InsertScoreAsynchTask extends AsyncTask<Score, Void, Void> {

        ScoreDao scoreDao;

        public InsertScoreAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {
            scoreDao.insert(scores[0]);
            return null;
        }
    }

    private static class UpdateScoreAsynchTask extends AsyncTask<Score, Void, Void> {

        ScoreDao scoreDao;

        public UpdateScoreAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {
            scoreDao.update(scores[0]);
            return null;
        }
    }

    private static class DeleteScoreAsynchTask extends AsyncTask<Score, Void, Void> {

        ScoreDao scoreDao;

        public DeleteScoreAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {
            scoreDao.delete(scores[0]);
            return null;
        }
    }

    private static class DeleteAllScoreAsynchTask extends AsyncTask<Score, Void, Void> {

        ScoreDao scoreDao;

        public DeleteAllScoreAsynchTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {
            scoreDao.deleteAll();
            return null;
        }
    }



}
