package com.planetpeopleplatform.popularmovies.utilities;

import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.Model.Review;
import com.planetpeopleplatform.popularmovies.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.ID;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.OVERVIEW;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.POSTER_PATH;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.RELEASE_DATE;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.RESEULTS;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.REVIEW_AUTHOR;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.REVIEW_CONTENT;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.THUMBNAIL_URL;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TITLE;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_IMG_PREFIX;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_IMG_SURFIX;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_KEY;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_NAME;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_SITE;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_SIZE;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_TYPE;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.VOTE_AVERAGE;

public class JsonUtils {

    private static void jsonArrayToMovieArrayList (JSONObject jsonObject, String name, List<Movie> movies){
        JSONArray jsonArray = null;
        try {

            jsonArray = jsonObject.getJSONArray(name);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jObject = (JSONObject) jsonArray.get(i);
                movies.add(new Movie(jObject.getString(TITLE), jObject.getString(OVERVIEW), jObject.getString(VOTE_AVERAGE),
                        jObject.getString(RELEASE_DATE),  THUMBNAIL_URL + jObject.getString(POSTER_PATH), jObject.getString(ID)));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private static void jsonArrayToReviewArrayList (JSONObject jsonObject, String name, List<Review> reviews){
        JSONArray jsonArray = null;
        try {

            jsonArray = jsonObject.getJSONArray(name);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jObject = (JSONObject) jsonArray.get(i);
                reviews.add(new Review(jObject.getString(REVIEW_AUTHOR), jObject.getString(REVIEW_CONTENT)));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private static void jsonArrayToTrailerArrayList (JSONObject jsonObject, String name, List<Trailer> trailers){
        JSONArray jsonArray = null;
        try {

            jsonArray = jsonObject.getJSONArray(name);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jObject = (JSONObject) jsonArray.get(i);
                trailers.add(new Trailer(jObject.getString(ID), jObject.getString(TRAILER_KEY), jObject.getString(TRAILER_NAME),
                        jObject.getString(TRAILER_SITE), jObject.getString(TRAILER_SIZE), jObject.getString(TRAILER_TYPE),
                        TRAILER_IMG_PREFIX + jObject.getString(TRAILER_KEY) + TRAILER_IMG_SURFIX));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static List<Movie> parseMovieJson(String json) {

        List<Movie> movies = new ArrayList();

        JSONObject jObject = null;
        try {

            jObject = new JSONObject(json);
            jsonArrayToMovieArrayList(jObject, RESEULTS, movies);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static List<Review> parseReviewJson(String json) {
        List<Review> reviews = new ArrayList();

        JSONObject jObject = null;
        try {

            jObject = new JSONObject(json);
            jsonArrayToReviewArrayList(jObject, RESEULTS, reviews);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static List<Trailer> parseTrailerJson(String json) {
        List<Trailer> trailers = new ArrayList();

        JSONObject jObject = null;
        try {

            jObject = new JSONObject(json);
            jsonArrayToTrailerArrayList(jObject, RESEULTS, trailers);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }
}