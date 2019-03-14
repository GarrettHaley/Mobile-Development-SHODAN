package com.example.shodan.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class LocationRepository {
    private LocationDao mLocationDao;

    public LocationRepository (Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mLocationDao = db.locationDao();
    }

    public void insertLocation(Location location) {
        new InsertAsyncTask(mLocationDao).execute(location);
    }

    public LiveData<List<Location>> getAllLocations() {
        return mLocationDao.getAllLocations();
    }

    private static class InsertAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDao mAsyncTaskDao;
        InsertAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            mAsyncTaskDao.insert(locations[0]);
            return null;
        }
    }
}
