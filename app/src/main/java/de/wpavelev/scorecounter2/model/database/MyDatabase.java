package de.wpavelev.scorecounter2.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.wpavelev.scorecounter2.model.data.PlayerAction;
import de.wpavelev.scorecounter2.model.data.Score;
import de.wpavelev.scorecounter2.model.repos.NameDao;
import de.wpavelev.scorecounter2.model.repos.PlayerActionDao;
import de.wpavelev.scorecounter2.model.repos.PlayerDao;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.repos.ScoreDao;


@Database(entities = {Player.class, Name.class, Score.class, PlayerAction.class}, version = 9, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase mInstance;

    public abstract PlayerDao mPlayerDao();

    public abstract NameDao mNameDao();

    public abstract ScoreDao mScoreDao();

    public abstract PlayerActionDao mPlayerActionDao();


    public static synchronized MyDatabase getInstance(Context context) {

        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    MyDatabase.class, "round_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return mInstance;

    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(mInstance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlayerDao mPlayerDao;
        private NameDao mNameDao;
        private ScoreDao mScoreDao;
        private PlayerActionDao mPlayerActionDao;

        private PopulateDbAsyncTask(MyDatabase db) {
            mPlayerDao = db.mPlayerDao();
            mNameDao = db.mNameDao();
            mScoreDao = db.mScoreDao();
            mPlayerActionDao = db.mPlayerActionDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            mPlayerDao.insert(new Player("Player 1"));
            mPlayerDao.insert(new Player("Player 2"));
            mPlayerDao.insert(new Player("Player 3"));
            mPlayerDao.insert(new Player("Player 4"));


            mNameDao.insert(new Name("Felix"));
            mNameDao.insert(new Name("Hermann"));


            return null;
        }
    }
}



