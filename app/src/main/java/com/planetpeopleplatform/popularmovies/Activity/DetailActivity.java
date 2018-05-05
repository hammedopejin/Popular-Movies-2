package com.planetpeopleplatform.popularmovies.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.R;
import com.planetpeopleplatform.popularmovies.utilities.JsonUtils;
import com.squareup.picasso.Picasso;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.DEFAULT_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_STRING;


/**
 * Created by Hammedopejin on 4/27/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView originalTile;
    private TextView ratings;
    private TextView releaseDate;
    private TextView synopsis;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterImageThumbnail = findViewById(R.id.image_iv);

        originalTile = findViewById(R.id.original_title_tv);
        ratings = findViewById(R.id.rating_tv);
        releaseDate = findViewById(R.id.release_date_tv);
        synopsis = findViewById(R.id.synopsis_tv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        String jsonMovieResponse = intent.getStringExtra(EXTRA_STRING);
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if ((position == DEFAULT_POSITION) || (jsonMovieResponse.isEmpty())) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        movie = JsonUtils.parseMovieJson(jsonMovieResponse).get(position);
        if (movie == null) {
            // Movie data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(movie.getThumdnailImage())
                .into(posterImageThumbnail);

        setTitle(movie.getOriginalTitle());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        String mainNameString = movie.getOriginalTitle();
        if (mainNameString.isEmpty()){
            mainNameString = "Unknown";
        }
        originalTile.setText(mainNameString);

        String synopsisString = movie.getSynopsis();
        if (synopsisString.isEmpty()){
            synopsisString = "Unknown";
        }
        synopsis.setText(synopsisString);

        String ratingsString = movie.getRatings();
        if (ratingsString.isEmpty()){
            ratingsString = "Unknown";
        }
        ratings.setText(ratingsString);

        String releaseDateString = movie.getReleaseDate();
        if (releaseDateString.isEmpty()){
            releaseDateString = "Unknown";
        }
        releaseDate.setText(releaseDateString);
    }
}
