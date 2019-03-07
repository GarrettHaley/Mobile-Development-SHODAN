package com.example.shodan.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.example.shodan.R;
import com.example.shodan.ShodanItem;
import com.example.shodan.AsyncTasks.ShodanSearchLoader;
import com.example.shodan.utils.ShodanAdapter;
import com.example.shodan.utils.ShodanUtils;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,ShodanAdapter.OnShodanItemClickListener {
    private static final String SEARCH_URL_KEY = ShodanUtils.buildShodanURL("Seattle"); //baseline prior to user entering location preference
    private static final int SHODAN_SEARCH_LOADER_ID = 0;
    private ShodanAdapter mAdapter;
    private RecyclerView mRecyclerView;
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
        getSupportLoaderManager().initLoader(SHODAN_SEARCH_LOADER_ID, null, this);
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
            return true;
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
            ArrayList<ShodanItem> shodanItems = ShodanUtils.parseShodanJSON(shodanJSON);
            mAdapter.updateShodanItems(shodanItems);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onShodanItemClick(ShodanItem shodanItem) {
        Intent intent = new Intent(this, ShodanDetails.class);
        intent.putExtra(ShodanUtils.EXTRA_SHODAN_ITEM, shodanItem);
        startActivity(intent);
    }
}
