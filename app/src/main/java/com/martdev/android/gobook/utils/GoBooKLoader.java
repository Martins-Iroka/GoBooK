package com.martdev.android.gobook.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;

import com.martdev.android.gobook.model.GoBooK;

import java.util.List;

public class GoBooKLoader extends AsyncTaskLoader<List<GoBooK>> {

    private static final String LOG_TAG = GoBooKLoader.class.getName();

    private String mUrl;

    public GoBooKLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading called");
        forceLoad();
    }

    @Nullable
    @Override
    public List<GoBooK> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground called");
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.getGoBooKData(mUrl);
    }
}
