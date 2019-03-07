package com.example.shodan.AsyncTasks;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import java.io.IOException;

import com.example.shodan.utils.*;

public class ShodanSearchLoader extends AsyncTaskLoader<String> {
    private static final String TAG = ShodanSearchLoader.class.getSimpleName();
    private String mSearchResultsJSON;
    String mShodanSearchURL = null;

    public ShodanSearchLoader(Context context, String url) {
        super(context);
        mShodanSearchURL = url;
    }

    @Override
    protected void onStartLoading() {
        if (mShodanSearchURL != null) {
            if (mSearchResultsJSON != null) {
                Log.d(TAG, "-------------LOADER CACHED RESULTS -----------------");
                deliverResult(mSearchResultsJSON);
            } else {
                forceLoad();
            }
        }
    }


    @Override
    public String loadInBackground() {
        if (mShodanSearchURL != null) {
            String searchResults = null;
            try {
                Log.d(TAG, "Loading results from the shodan API with URL: " + mShodanSearchURL);
                searchResults =
                        NetworkUtils.doHTTPGet(mShodanSearchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        } else {
            return null;
        }
    }
    @Override
    public void deliverResult(String data) {
        mSearchResultsJSON = data;
        super.deliverResult(data);
    }

}