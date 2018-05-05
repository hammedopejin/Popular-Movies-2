package com.planetpeopleplatform.popularmovies.utilities;

import com.planetpeopleplatform.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.OVERVIEW;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.POSTERPATH;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.RELEASEDATE;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.RESEULTS;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.THUMBNAILURL;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.TITLE;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.VOTEAVERAGE;

public class JsonUtils {

    public static List<Movie> parseMovieJson(String json) {

        List<Movie> movies = new ArrayList();

        JSONObject jObject = null;
        try {

            jObject = new JSONObject(json);
            jsonArrayToArrayList(jObject, RESEULTS, movies);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private static void jsonArrayToArrayList (JSONObject jsonObject, String name, List<Movie> movies){
        JSONArray jsonArray = null;
        try {

            jsonArray = jsonObject.getJSONArray(name);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jObject = (JSONObject) jsonArray.get(i);
                movies.add(new Movie(jObject.getString(TITLE), jObject.getString(OVERVIEW), jObject.getString(VOTEAVERAGE),
                        jObject.getString(RELEASEDATE),  THUMBNAILURL + jObject.getString(POSTERPATH)));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
