package com.example.shodan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shodan.R;
import com.example.shodan.ShodanItem;
import com.example.shodan.asyncTasks.ShodanSearchLoader;
import com.example.shodan.utils.ShodanAdapter;
import com.example.shodan.utils.ShodanUtils;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<String>,
        ShodanAdapter.OnShodanItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final int SHODAN_SEARCH_LOADER_ID = 0;
    private static final String SHODAN_ITEMS_KEY = "shodanItems";
    private static final String SEARCH_URL_KEY = "shodanSearchPref"; // Baseline prior to user entering location preference

    private ShodanAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private ArrayList<ShodanItem> mShodanItems;
    private TextView mErrorMessage;
    private TextView mNoContentMessage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mErrorMessage = findViewById(R.id.tv_loading_error_message);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.shodanItem_recycler_view);
        mNoContentMessage = findViewById(R.id.tv_no_content);

        mAdapter = new ShodanAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(SHODAN_ITEMS_KEY)) {
            mShodanItems = (ArrayList<ShodanItem>) savedInstanceState.getSerializable(SHODAN_ITEMS_KEY);
        }


        getSupportLoaderManager().initLoader(SHODAN_SEARCH_LOADER_ID, null, this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        loadShodanData(preferences);
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
            googleMapsIntent.putExtra(SHODAN_ITEMS_KEY,mShodanItems);
            startActivity(googleMapsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String shodanSearchUrl = null;
        if(bundle != null) {
            shodanSearchUrl = bundle.getString(SEARCH_URL_KEY);
        }
        return new ShodanSearchLoader(this, shodanSearchUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String shodanJSON) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(shodanJSON != null){
            mShodanItems = ShodanUtils.parseShodanJSON(shodanJSON);
            mAdapter.updateShodanItems(mShodanItems);
            if(mShodanItems.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mNoContentMessage.setVisibility(View.INVISIBLE);
                mErrorMessage.setVisibility(View.INVISIBLE);

                mAdapter.updateShodanItems(mShodanItems);
            } else {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mNoContentMessage.setVisibility(View.VISIBLE);
            }
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mShodanItems != null) {
            outState.putSerializable(SHODAN_ITEMS_KEY, mShodanItems);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Nothing...
    }

    @Override
    public void onShodanItemClick(ShodanItem shodanItem) {
        Intent intent = new Intent(this, ShodanDetailsActivity.class);
        intent.putExtra(ShodanUtils.EXTRA_SHODAN_ITEM, shodanItem);
        startActivity(intent);
    }

    private void loadShodanData(SharedPreferences pref) {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mNoContentMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        String url = ShodanUtils.buildShodanURL(pref.getString(
                        getString(R.string.pref_search_key),
                        getString(R.string.pref_search_default)));

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, url);
        getSupportLoaderManager()
                .restartLoader(SHODAN_SEARCH_LOADER_ID, loaderArgs, this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        loadShodanData(sharedPreferences);

    }
}
