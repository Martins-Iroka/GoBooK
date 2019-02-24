package com.martdev.android.gobook.activity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.martdev.android.gobook.R;
import com.martdev.android.gobook.adapter.GoBooKAdapter;
import com.martdev.android.gobook.model.GoBooK;
import com.martdev.android.gobook.utils.GoBooKLoader;

import java.util.ArrayList;
import java.util.List;

public class GoBooKActivity extends AppCompatActivity implements LoaderCallbacks<List<GoBooK>> {

    private GoBooKAdapter mAdapter;

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private EditText mKeywordSearch;
    private Button mSearchButton;
    private ListView mBookList;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;

    String mKeyword;
    String mURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_book);

        mKeywordSearch = findViewById(R.id.keyword);

        mEmptyTextView = findViewById(R.id.empty_view);

        mProgressBar = findViewById(R.id.loading_indicator);
        mProgressBar.setVisibility(View.GONE);

        mBookList = findViewById(R.id.list);
        mBookList.setEmptyView(mEmptyTextView);

        mAdapter = new GoBooKAdapter(this, new ArrayList<GoBooK>());
        mBookList.setAdapter(mAdapter);
        mBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoBooK goBooK = mAdapter.getItem(position);

                Uri bookInfoLink = Uri.parse(goBooK.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, bookInfoLink);
                startActivity(intent);
            }
        });

        mSearchButton = findViewById(R.id.search_button);
        checkNetwork();

    }

    private void checkNetwork() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyword = mKeywordSearch.getText().toString().trim();
                mURL =  getUrl(mKeyword);

                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getLoaderManager();
                    mProgressBar.setVisibility(View.VISIBLE);

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, GoBooKActivity.this);
                    Log.i("MainActivity", "initLoader called");
                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    mProgressBar.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    mEmptyTextView.setText(R.string.no_internet_connection);
                }
            }
        });
    }

    private String getUrl(String keyword) {
        return "https://www.googleapis.com/books/v1/volumes?q="+keyword+
                "&maxResults=20&AIzaSyCYgkoE5Dr07aRi4XGtv6W1ypOd3A8aci0";
    }

    @Override
    public Loader<List<GoBooK>> onCreateLoader(int id, Bundle args) {
        Log.i("MainActivity", "onCreateLoader called");
        return new GoBooKLoader(this, mURL);
    }

    @Override
    public void onLoadFinished(Loader<List<GoBooK>> loader, List<GoBooK> goBooKS) {
        Log.i("MainActivity", "onLoadFinished called");
        mProgressBar.setVisibility(View.GONE);

        mEmptyTextView.setText(R.string.no_books);

        mAdapter.clear();


        if (goBooKS != null && !goBooKS.isEmpty()) {
            //mAdapter.addAll(earthquakes);
            mAdapter.addAll(goBooKS);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<GoBooK>> loader) {
        Log.i("MainActivity", "onLoaderReset called");
        mAdapter.clear();
    }
}
