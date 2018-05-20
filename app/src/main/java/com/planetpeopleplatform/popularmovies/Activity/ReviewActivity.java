package com.planetpeopleplatform.popularmovies.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.planetpeopleplatform.popularmovies.Adapter.ReviewRecyclerViewAdapter;
import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.Model.Review;
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

/**
 * Created by Hammedopejin on 5/11/2018.
 */

public class ReviewActivity extends AppCompatActivity {

    private List<Review> reviews = new ArrayList();

    private String mMovieID;
    private String jsonReviewResponse = "";
    private static String path = "";

    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecycleView = findViewById(R.id.reviews_rv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        mMovieID = intent.getStringExtra(MOVIE_ID);
        path = intent.getStringExtra(EXTRA_PATH);

        if ( (mMovieID.isEmpty()) || path.isEmpty()) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        showLoading();


        new MyAsyncTask().execute();
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReviewRecyclerViewAdapter(this,
                reviews));
    }


    private class MyAsyncTask extends AsyncTask<List<Review>, List<Review>, List<Review>> {

        private static final String TAG = "TAG";

        @Override
        protected void onPreExecute() {
            //before
        }
        @Override
        protected List<Review>  doInBackground(List<Review>... lists) {

            try {

                //Retrieve movies URL
                URL movieRequestUrl = NetworkUtils.getUrl(MOVIE_BASE_URL, path, mMovieID);

                /* Use the URL to retrieve the JSON */
                jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                /* Parse the JSON into a list of movie values */
                reviews = JsonUtils
                        .parseReviewJson(jsonReviewResponse);

                publishProgress(reviews);

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(List<Review>... lists) {

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

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.review_error_message, Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
