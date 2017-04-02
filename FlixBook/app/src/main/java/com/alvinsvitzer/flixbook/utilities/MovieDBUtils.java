package com.alvinsvitzer.flixbook.utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alvin on 12/29/16. Class used to communicate with Movie DB API
 * API Link -> https://developers.themoviedb.org/3
 */

public class MovieDBUtils {

    public final static String ATTRIBUTE_VIDEO_SITE_YOUTUBE = "YouTube";
    public final static String ATTRIBUTE_VIDEO_TYPE_TRAILER = "Trailer";
    final static String TAG = MovieDBUtils.class.getSimpleName();
    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    final static String MOVIE_POSTER_SIZE_W185 = "w185";
    final static String MOVIE_POSTER_SIZE_W500 = "w500";
    final static String PATH_POPULAR = "popular";
    final static String PATH_VIDEO = "videos";
    final static String PATH_TOP_RATED = "top_rated";
    final static String PATH_REVIEW = "reviews";
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
     * Builds URL to get all the videos for a movie
     */
    public static URL buildMovieVideoURL(String apiKey, String movieId){

        Uri finalUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(PATH_VIDEO)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_ENGLISH_US)
                .build();

        Log.i(TAG, "buildMovieVideoURL: " + finalUri.toString());

        URL url = null;

        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds URL for app home page that gets list of most popular movies
     */
    public static URL buildMostPopularURL(String apiKey){

        Uri finalUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(PATH_POPULAR)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_ENGLISH_US)
                .build();

        Log.i(TAG, "buildMostPopularURL: " + finalUri.toString());

        URL url = null;

        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }


    public static URL buildHighestRatingURL(String apiKey){

        Uri finalUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(PATH_TOP_RATED)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_ENGLISH_US)
                .build();

        Log.i(TAG, "buildHighestRatingURL: " + finalUri.toString());

        URL url = null;

        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }

    public static URL buildMoviePosterURL(String posterFilePath){

        String movieUrlString = MOVIE_IMAGE_BASE_URL + "/" + MOVIE_POSTER_SIZE_W185 + posterFilePath;

        Log.i(TAG, "buildMoviePosterURL: " + movieUrlString);

        URL url = null;

        try {
            url = new URL(movieUrlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildMovieBackdropURL(String backdropFilePath){

        String movieUrlString = MOVIE_IMAGE_BASE_URL + "/" + MOVIE_POSTER_SIZE_W500 + backdropFilePath;

        Log.i(TAG, "buildMovieBackdropURL: " + movieUrlString);

        URL url = null;

        try {
            url = new URL(movieUrlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieURL(String apiKey, String movieId) {

        Uri finalUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_ENGLISH_US)
                .build();

        Log.i(TAG, "buildMovieURL: " + finalUri.toString());

        URL url = null;

        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }

    public static URL buildMovieReviewURL(String apiKey, String movieId){

        Uri finalUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(PATH_REVIEW)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_ENGLISH_US)
                .build();

        Log.i(TAG, "buildMovieReviewURL: " + finalUri.toString());

        URL url = null;

        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }

    public static String getLocalDate(String dateToConvert){


        String formattedDate = dateToConvert;

        SimpleDateFormat movieDbformat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat localDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        Date date;

        try {
            date = movieDbformat.parse(dateToConvert);
            formattedDate = localDateFormat.format(date);

        } catch (ParseException e) {

            Log.e(TAG, "getLocalDate: Could not parse this date: " + dateToConvert, e);

        }

        return formattedDate;


    }

}
