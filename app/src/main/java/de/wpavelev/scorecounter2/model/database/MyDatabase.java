package de.wpavelev.scorecounter2.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.wpavelev.scorecounter2.model.repos.NameDao;
import de.wpavelev.scorecounter2.model.repos.PlayerDao;
import de.wpavelev.scorecounter2.model.repos.ScoreDao;
import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.Score;


@Database(entities = {Player.class, Name.class, Score.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance;

    public abstract PlayerDao playerDao();

    public abstract NameDao nameDao();

    public abstract ScoreDao scoreDao();

    public static synchronized MyDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyDatabase.class, "round_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;

    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlayerDao playerDao;
        private NameDao nameDao;
        private ScoreDao scoreDao;

        private PopulateDbAsyncTask(MyDatabase db) {
            playerDao = db.playerDao();
            nameDao = db.nameDao();
            scoreDao = db.scoreDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            playerDao.insert(new Player("Player 1"));
            playerDao.insert(new Player("Player 2"));
            playerDao.insert(new Player("Player 3"));
            playerDao.insert(new Player("Player 4"));


            nameDao.insert(new Name("Felix"));
            nameDao.insert(new Name("Hermann"));


            return null;
        }
    }
}



