package com.planetpeopleplatform.popularmovies.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.planetpeopleplatform.popularmovies.Adapter.TrailerRecyclerViewAdapter;
import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.Model.Trailer;
import com.planetpeopleplatform.popularmovies.R;
import com.planetpeopleplatform.popularmovies.utilities.JsonUtils;
import com.planetpeopleplatform.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.DEFAULT_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_PATH;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_STRING;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_BASE_URL;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_ID;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_BASE_URL;

public class TrailerActivity extends AppCompatActivity {

    private List<Trailer> trailers = new ArrayList();

    private String mMovieID;
    private String jsonTrailerResponse = "";
    private static String path = "";
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecycleView = findViewById(R.id.trailers_rv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        mMovieID = intent.getStringExtra(MOVIE_ID);
        path = intent.getStringExtra(EXTRA_PATH);

        if ((mMovieID.isEmpty()) || path.isEmpty()) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        showLoading();


        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<List<Trailer>, List<Trailer>, List<Trailer>> {

        private static final String TAG = "TAG";

        @Override
        protected void onPreExecute() {
            //before
        }
        @Override
        protected List<Trailer>  doInBackground(List<Trailer>... lists) {

            try {

                //Retrieve movies URL
                URL movieRequestUrl = NetworkUtils.getUrl(MOVIE_BASE_URL, path, mMovieID);
                Log.i(TAG, "doInBackground: " + movieRequestUrl);

                /* Use the URL to retrieve the JSON */
                jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                /* Parse the JSON into a list of movie values */
                trailers = JsonUtils
                        .parseTrailerJson(jsonTrailerResponse);

                publishProgress(trailers);

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(List<Trailer>... lists) {

            try {

                setupRecyclerView(mRecycleView);
                showDataView();

            } catch (Exception ex) {
            }

        }

        protected void onPostExecute(String  result2){
            //after
        }

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TrailerRecyclerViewAdapter(this,
                trailers));
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.trailer_error_message, Toast.LENGTH_SHORT).show();
    }

    private void showDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the data is visible */
        mRecycleView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the data */
        mRecycleView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_trailer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                Intent shareIntent = createShareForecastIntent();
                startActivity(shareIntent);
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(TRAILER_BASE_URL + trailers.get(0).getKey())
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }
}
