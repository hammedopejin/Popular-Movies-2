package com.planetpeopleplatform.popularmovies.utilities;

import android.net.Uri;

import com.planetpeopleplatform.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.KEY;

/**
 * Created by Hammedopejin on 4/30/2018.
 */

public class NetworkUtils {


    public static URL getUrl(String baseUrl, String path1, String path2) {
        if (!path2.isEmpty()){
            return buildUrls(baseUrl, path2, path1);
        }
        return buildUrl(baseUrl, path1);
    }

    private static URL buildUrls(String baseUrl, String path2, String path1) {
        Uri movieQueryUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(path2)
                .appendPath(path1)
                .appendQueryParameter(KEY, BuildConfig.MOVIEDB_API_KEY)

                .build();

        try {
            URL movieQueryUrl = new URL(movieQueryUri.toString());
            return movieQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static URL buildUrl(String baseUrl, String path1) {
        Uri movieQueryUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(path1)
                .appendQueryParameter(KEY, BuildConfig.MOVIEDB_API_KEY)
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
