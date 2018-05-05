package com.planetpeopleplatform.popularmovies.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.planetpeopleplatform.popularmovies.Activity.DetailActivity;
import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.Adapter.MovieAdapter;
import com.planetpeopleplatform.popularmovies.R;
import com.planetpeopleplatform.popularmovies.utilities.JsonUtils;
import com.planetpeopleplatform.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_STRING;

/**
 * Created by Hammedopejin on 4/27/2018.
 */

public class MainActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MovieAdapter movieAdapter;
    private List<Movie> movies = new ArrayList();

    private static String sortBy = "";
    private String jsonMovieResponse = "";

    private ProgressBar mLoadingIndicator;
    private GridView mGridView;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

        setupSharedPreferences();

        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        mGridView = rootView.findViewById(R.id.movies_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });

        showLoading();

        new MyAsyncTask().execute();

        return rootView;

    }

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        loadSortFromPreferences(sharedPreferences);

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadSortFromPreferences(SharedPreferences sharedPreferences) {
        sortBy = sharedPreferences.getString(getString(R.string.pref_sort_list_key),
                getString(R.string.pref_sort_popular));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sortBy = sharedPreferences.getString(getString(R.string.pref_sort_list_key),
                getString(R.string.pref_sort_popular));
        showLoading();
        new MainActivityFragment.MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<List<Movie>, List<Movie>, List<Movie>> {

        @Override
        protected void onPreExecute() {
            //before
        }
        @Override
        protected List<Movie>  doInBackground(List<Movie>... lists) {

            try {

                //Retrieve movies URL
                URL movieRequestUrl = NetworkUtils.getUrl(sortBy);

                /* Use the URL to retrieve the JSON */
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                /* Parse the JSON into a list of movie values */
                movies = JsonUtils
                        .parseMovieJson(jsonMovieResponse);

                publishProgress(movies);

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(List<Movie>... lists) {

            try {

                movieAdapter = new MovieAdapter(getActivity(), movies);
                mGridView.setAdapter(movieAdapter);
                showDataView();

            } catch (Exception ex) {
            }

        }

        protected void onPostExecute(String  result2){
            //after
        }

    }

    private void showDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the data is visible */
        mGridView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the data */
        mGridView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_STRING, jsonMovieResponse);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the listener
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
