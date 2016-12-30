package com.alvinsvitzer.flixbook.utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alvin on 12/29/16. Class used to communicate with Movie DB API
 */

public class MovieDBUtils {

    final static String TAG = MovieDBUtils.class.getSimpleName();

    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    final static String PARAM_API_KEY = "api_key";
    final static String PARAM_LANGUAGE = "language";
    final static String PARAM_INCLUDE_ADULT = "include_adult";
    final static String PARAM_INCLUDE_VIDEO = "include_video";
    final static String PARAM_PAGE = "page";
    final static String PARAM_SORT_BY = "sort_by";

    final static String SORT_BY_POPULARITY = "popularity.desc";
    final static String INCLUDE_TRUE = "1";
    final static String INCLUDE_FALSE = "0";
    final static String FIRST_PAGE = "1";
    final static String LANGUAGE_ENGLISH_US = "en-US";


    /**
     * Builds URL for app home page that gets list of most popular movies
     */
    public static URL buildDefaultUrl(String apiKey){

        Uri finalUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_ENGLISH_US)
                .appendQueryParameter(PARAM_INCLUDE_ADULT, INCLUDE_TRUE)
                .appendQueryParameter(PARAM_INCLUDE_VIDEO, INCLUDE_FALSE)
                .appendQueryParameter(PARAM_PAGE, FIRST_PAGE)
                .appendQueryParameter(PARAM_SORT_BY, SORT_BY_POPULARITY)
                .build();

        Log.i(TAG, "buildDefaultUrl: " + finalUri.toString());

        URL url = null;

        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }

}
