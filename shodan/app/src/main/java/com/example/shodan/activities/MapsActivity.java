package com.example.shodan.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.shodan.R;
import com.example.shodan.ShodanItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String SHODAN_ITEMS_KEY = "shodanItems";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SHODAN_ITEMS_KEY)) {
            ArrayList<ShodanItem> shodanItems = (ArrayList<ShodanItem>) intent.getSerializableExtra(SHODAN_ITEMS_KEY);
            ArrayList<LatLng> latlongs = new ArrayList();
            if (shodanItems != null) {
                MarkerOptions markerOptions = new MarkerOptions();
                double offset = 0.00;
                for (ShodanItem shodanItem : shodanItems) {
                    if(shodanItem.longitude != null && shodanItem.latitude != null) {
                        LatLng latLng = new LatLng(shodanItem.latitude+offset, shodanItem.longitude+offset);
                        mMap.addMarker(markerOptions.position(latLng).title(shodanItem.organization + ":" + shodanItem.title));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        offset = offset + 0.000001;
                    }
                }
            }
        }
    }
}

