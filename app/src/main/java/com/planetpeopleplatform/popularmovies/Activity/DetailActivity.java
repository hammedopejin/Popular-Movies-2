package com.planetpeopleplatform.popularmovies.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.R;
import com.planetpeopleplatform.popularmovies.data.FavoritesContract;
import com.planetpeopleplatform.popularmovies.utilities.JsonUtils;
import com.squareup.picasso.Picasso;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.DEFAULT_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_PATH;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_STRING;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_ID;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_REVIEWS_PATH;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_VIDEOS_PATH;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.SORT_BY;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.UNKNOWN;


/**
 * Created by Hammedopejin on 4/27/2018.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private TextView originalTileTv;
    private TextView ratingsTv;
    private TextView releaseDateTv;
    private TextView synopsisTv;
    private Button reviewsBu;
    private Button trailerBu;
    private Button favoriteBu;
    ImageView mPosterImageThumbnail;

    private static String sortBy = "";
    private Movie movie;
    private Cursor mDetailCursor;
    private Uri mUri;

    private static final int CURSOR_LOADER_ID = 0;

    private static String path = "";
    private String jsonMovieResponse = "";
    private int mPosition = -1;
    private String mMovieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterImageThumbnail = findViewById(R.id.image_iv);
        originalTileTv = findViewById(R.id.original_title_tv);
        ratingsTv = findViewById(R.id.rating_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        synopsisTv = findViewById(R.id.synopsis_tv);
        reviewsBu = findViewById(R.id.reviews_button);
        trailerBu = findViewById(R.id.trailer_button);
        favoriteBu = findViewById(R.id.favorite_button);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        jsonMovieResponse = intent.getStringExtra(EXTRA_STRING);
        mPosition = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        mMovieID = intent.getStringExtra(MOVIE_ID);
        sortBy = intent.getStringExtra(SORT_BY);

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.com_planetpeopleplatform_popularmovies_preference_favorite_file_key), MODE_PRIVATE);

        final boolean[] mIsFavorited = {sharedPref.getBoolean(mMovieID, false)};
        if (mIsFavorited[0]){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteBu.setCompoundDrawablesWithIntrinsicBounds(getDrawable(android.R.drawable.star_on), null, null, null);
            }else {
                favoriteBu.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.star_on, 0, 0, 0);
            }
        }

        favoriteBu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mIsFavorited[0]){
                    mUri = FavoritesContract.FavoritesEntry.buildFavoriteUriWithMovieId(mMovieID);
                    deleteData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        favoriteBu.setCompoundDrawablesWithIntrinsicBounds(getDrawable(android.R.drawable.star_off), null, null, null);
                    }else {
                        favoriteBu.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.star_off, 0, 0, 0);
                    }
                    mIsFavorited[0] = false;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(mMovieID, mIsFavorited[0]);
                    editor.commit();
                }else {
                    insertData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        favoriteBu.setCompoundDrawablesWithIntrinsicBounds(getDrawable(android.R.drawable.star_on), null, null, null);
                    }else {
                        favoriteBu.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.star_on, 0, 0, 0);
                    }
                    mIsFavorited[0] = true;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(mMovieID, mIsFavorited[0]);
                    editor.commit();
                }
            }
        });

        reviewsBu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                path = MOVIE_REVIEWS_PATH;

                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra(EXTRA_PATH, path);
                intent.putExtra(MOVIE_ID, mMovieID);
                startActivity(intent);
            }
        });

        trailerBu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                path = MOVIE_VIDEOS_PATH;

                Intent intent = new Intent(getApplicationContext(), TrailerActivity.class);
                intent.putExtra(EXTRA_PATH, path);
                intent.putExtra(MOVIE_ID, mMovieID);
                startActivity(intent);
            }
        });

        if (!(sortBy.equals(getString(R.string.pref_sort_favorites)))) {
            if ((mPosition == DEFAULT_POSITION) || (jsonMovieResponse.isEmpty())) {
                // EXTRA_POSITION not found in intent
                closeOnError();
                return;
            }
            movie = JsonUtils.parseMovieJson(jsonMovieResponse).get(mPosition);
            if (movie == null) {
                // Movie data unavailable
                closeOnError();
                return;
            }
            populateUI();
        }else {

            mUri = FavoritesContract.FavoritesEntry.buildFavoriteUriWithMovieId(mMovieID);
            if (mUri == null || mUri == null)
                throw new NullPointerException(getString(R.string.uri_error));

            getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        }

    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        String mainNameString = movie.getOriginalTitle();
        if (mainNameString.isEmpty()){
            mainNameString = UNKNOWN;
        }
        originalTileTv.setText(mainNameString);

        String synopsisString = movie.getSynopsis();
        if (synopsisString.isEmpty()){
            synopsisString = UNKNOWN;
        }
        synopsisTv.setText(synopsisString);

        String ratingsString = movie.getRatings();
        if (ratingsString.isEmpty()){
            ratingsString = UNKNOWN;
        }
        ratingsTv.setText(ratingsString);

        String releaseDateString = movie.getReleaseDate();
        if (releaseDateString.isEmpty()){
            releaseDateString = UNKNOWN;
        }
        releaseDateTv.setText(releaseDateString);

        Picasso.with(this)
                .load(movie.getThumbnailImage())
                .into(mPosterImageThumbnail);
    }



    // insert data into database
    public void insertData(){
        ContentValues movieValues = new ContentValues();

        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, movie.getOriginalTitle());
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_RATING, movie.getRatings());
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW, movie.getSynopsis());
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, movie.getThumbnailImage());

        getApplicationContext().getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI,
                movieValues);
    }

    // delete data from database
    public void deleteData(){
        getApplicationContext().getContentResolver().delete(mUri,
                null, null);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getApplicationContext(),
                mUri,
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
        mDetailCursor = cursor;
        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(cursor);
        String origTitle = mDetailCursor.getString(mDetailCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE));
        String synopsis = mDetailCursor.getString(mDetailCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW));
        String ratings = mDetailCursor.getString(mDetailCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RATING));
        String relDate = mDetailCursor.getString(mDetailCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE));
        String thumbnailImage = mDetailCursor.getString(mDetailCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH));
        String id = mDetailCursor.getString(mDetailCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));

        originalTileTv.setText(origTitle);
        ratingsTv.setText(ratings);
        releaseDateTv.setText(relDate);
        synopsisTv.setText(synopsis);
        Picasso.with(this)
                .load(thumbnailImage)
                .into(mPosterImageThumbnail);

        movie = new Movie(origTitle, synopsis, ratings, relDate, thumbnailImage, id);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mDetailCursor = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
