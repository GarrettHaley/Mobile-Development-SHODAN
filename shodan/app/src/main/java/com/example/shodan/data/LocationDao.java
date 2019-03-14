package com.example.shodan.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Location searchLocation);

    @Query("SELECT * FROM locations")
    LiveData<List<Location>> getAllLocations();

    @Query("SELECT * FROM locations WHERE search_location = :locations LIMIT 1")
    LiveData<Location> getLocationByName (String locations);
}
