package com.planetpeopleplatform.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.KEY;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_BASE_URL;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MYKEY;

/**
 * Created by Hammedopejin on 4/30/2018.
 */

public class NetworkUtils {


    public static URL getUrl(String s) {
        return buildUrl(s);
    }

    private static URL buildUrl(String query) {
        Uri movieQueryUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(KEY, MYKEY)
                .build();

        try {
            URL movieQueryUrl = new URL(movieQueryUri.toString());
            return movieQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
