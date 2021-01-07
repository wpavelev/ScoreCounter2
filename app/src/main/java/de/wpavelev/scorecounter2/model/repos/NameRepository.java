package de.wpavelev.scorecounter2.model.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.wpavelev.scorecounter2.model.database.MyDatabase;
import de.wpavelev.scorecounter2.model.data.Name;


public class NameRepository {

    private final NameDao nameDao;

    private final LiveData<List<Name>> mNamesList;


    public NameRepository(Application application) {
        MyDatabase database = MyDatabase.getInstance(application);
        nameDao = database.mNameDao();
        mNamesList = nameDao.getNames();
    }

    public void insert(Name name) {
        new InsertNameAsynchTask(nameDao).execute(name);
    }

    public void update(Name name) {
        new UpdateNameAsynchTask(nameDao).execute(name);
    }

    public void delete(Name name) {
        new DeleteNameAsynchTask(nameDao).execute(name);
    }

    public void deleteAll() {
        new DeleteAllNamesAsynchTask(nameDao).execute();
    }


    public LiveData<List<Name>> getNamesList() {
        return mNamesList;
    }


    private static class InsertNameAsynchTask extends AsyncTask<Name, Void, Void> {

        private final NameDao nameDao;

        private InsertNameAsynchTask(NameDao nameDao) {
            this.nameDao = nameDao;
        }


        @Override
        protected Void doInBackground(Name... names) {
            nameDao.insert(names[0]);
            return null;
        }
    }

    private static class UpdateNameAsynchTask extends AsyncTask<Name, Void, Void> {

        private final NameDao nameDao;

        private UpdateNameAsynchTask(NameDao nameDao) {
            this.nameDao = nameDao;
        }

        @Override
        protected Void doInBackground(Name... names) {

            nameDao.update(names[0]);
            return null;
        }
    }

    private static class DeleteNameAsynchTask extends AsyncTask<Name, Void, Void> {

        private final NameDao nameDao;

        private DeleteNameAsynchTask(NameDao nameDao) {
            this.nameDao = nameDao;
        }

        @Override
        protected Void doInBackground(Name... names) {

            nameDao.delete(names[0]);
            return null;
        }
    }

    private static class DeleteAllNamesAsynchTask extends AsyncTask<Name, Void, Void> {

        private final NameDao nameDao;

        private DeleteAllNamesAsynchTask(NameDao nameDao) {
            this.nameDao = nameDao;
        }

        @Override
        protected Void doInBackground(Name... names) {

            nameDao.deleteAll();
            return null;
        }
    }


}

