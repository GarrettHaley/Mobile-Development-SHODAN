package com.example.shodan.activities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.shodan.data.Location;
import com.example.shodan.data.LocationRepository;

import java.util.List;

public class LocationsViewModel extends AndroidViewModel {
    private LocationRepository mLocationRepository;

    public LocationsViewModel(Application application) {
        super(application);
        mLocationRepository = new LocationRepository(application);
    }

    public void insertLocation(Location location) {
        mLocationRepository.insertLocation(location);
    }
    public void delete(Location location){mLocationRepository.deleteLocation(location);}

    public LiveData<List<Location>> getAllLocations() {
        return mLocationRepository.getAllLocations();
    }
}
