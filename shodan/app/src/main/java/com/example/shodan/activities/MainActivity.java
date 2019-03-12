package com.example.shodan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.example.shodan.R;
import com.example.shodan.ShodanItem;
import com.example.shodan.asyncTasks.ShodanSearchLoader;
import com.example.shodan.utils.ShodanAdapter;
import com.example.shodan.utils.ShodanUtils;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,ShodanAdapter.OnShodanItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{
    private static String SEARCH_URL_KEY = ShodanUtils.buildShodanURL("Seattle"); //baseline prior to user entering location preference
    private static final int SHODAN_SEARCH_LOADER_ID = 0;
    private ShodanAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private ArrayList<ShodanItem> shodanItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView)findViewById(R.id.shodanItem_recycler_view);
        mAdapter = new ShodanAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getSupportLoaderManager().initLoader(SHODAN_SEARCH_LOADER_ID, null, this);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                loadShodanData(prefs);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_preferences) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        if (id == R.id.google_maps_button) {
            Intent googleMapsIntent = new Intent(this, MapsActivity.class);
            googleMapsIntent.putExtra("shodan_items",shodanItems);
            startActivity(googleMapsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String shodanSearchURL = SEARCH_URL_KEY;
        if (bundle != null) {
            shodanSearchURL = bundle.getString(SEARCH_URL_KEY);
        }
        return new ShodanSearchLoader(this, shodanSearchURL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String shodanJSON) {
        if(shodanJSON != null){
            shodanItems = ShodanUtils.parseShodanJSON(shodanJSON);
            mAdapter.updateShodanItems(shodanItems);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onShodanItemClick(ShodanItem shodanItem) {
        Intent intent = new Intent(this, ShodanDetailsActivity.class);
        intent.putExtra(ShodanUtils.EXTRA_SHODAN_ITEM, shodanItem);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadShodanData(sharedPreferences);
    }

    private void loadShodanData(SharedPreferences preferences) {
        String location = preferences.getString("pref_location","");
        String url = ShodanUtils.buildShodanURL(location);
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, url);
        getSupportLoaderManager()
                .restartLoader(SHODAN_SEARCH_LOADER_ID, loaderArgs, this);

    }
}
