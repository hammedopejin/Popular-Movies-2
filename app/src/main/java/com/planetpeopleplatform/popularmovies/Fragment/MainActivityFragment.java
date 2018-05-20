package com.planetpeopleplatform.popularmovies.Fragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.planetpeopleplatform.popularmovies.Adapter.MovieRecyclerViewAdapter;
import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.R;
import com.planetpeopleplatform.popularmovies.data.FavoritesContract;
import com.planetpeopleplatform.popularmovies.utilities.JsonUtils;
import com.planetpeopleplatform.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_BASE_URL;

/**
 * Created by Hammedopejin on 4/27/2018.
 */

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private List<Movie> movies;

    private static final int CURSOR_LOADER_ID = 0;

    private static String sortBy = "";
    private String jsonMovieResponse = "";

    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecycleView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private Cursor mCursor;


    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

        setupSharedPreferences();

        movies = new ArrayList();
        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        mRecycleView = rootView.findViewById(R.id.movies_rv);

        showLoading();

        if (sortBy.equals(getString(R.string.pref_sort_favorites))){
            loadFavorite();
        }else {
            new MyAsyncTask().execute();
        }

        return rootView;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {

        if ( getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //portraitView
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        }else {
            //landscapeView
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),3));
        }
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getActivity(),
                movies, jsonMovieResponse, sortBy);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        showDataView();
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

        if (sortBy.equals(getString(R.string.pref_sort_favorites))){
            //Toast.makeText(getActivity(), "favorites are called", Toast.LENGTH_LONG).show();
            loadFavorite();
        }else {
            //Toast.makeText(getActivity(), "others are called", Toast.LENGTH_LONG).show();
            new MyAsyncTask().execute();
        }
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
                URL movieRequestUrl = NetworkUtils.getUrl(MOVIE_BASE_URL, sortBy, "");

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

                setupRecyclerView(mRecycleView);

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
        mRecycleView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the data */
        mRecycleView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void loadFavorite() {

        boolean cursorHasValidData = false;
        Cursor cursor =
                getActivity().getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                        new String[]{FavoritesContract.FavoritesEntry._ID},
                        null,
                        null,
                        null);

        if (cursor != null && cursor.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            Toast.makeText(getActivity(), getString(R.string.no_favorites_saved), Toast.LENGTH_LONG).show();
        }
        movies.clear();
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        setupRecyclerView(mRecycleView);
        DatabaseUtils.dumpCursor(cursor);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(),
                FavoritesContract.FavoritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        boolean cursorHasValidData = false;
        if (cursor != null && cursor.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }
        mCursor = cursor;
        mCursor.moveToFirst();
        DatabaseUtils.dumpCursor(cursor);

            for (int i = 0; i < mCursor.getCount(); i++) {

                final String origTitle = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE));
                final String synopsis = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW));
                final String ratings = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RATING));
                final String relDate = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE));
                final String id = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));
                final String thumbnailImage = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH));

                movies.add(new Movie(origTitle, synopsis, ratings, relDate, thumbnailImage, id));
                mCursor.moveToNext();
            }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursor = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        if (sortBy.equals(getString(R.string.pref_sort_favorites))){
            loadFavorite();
        }else {
            new MyAsyncTask().execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the listener
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
