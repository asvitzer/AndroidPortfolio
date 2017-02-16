package com.alvinsvitzer.flixbook.data.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alvinsvitzer.flixbook.data.MovieDataSource;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.model.Trailer;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;
import com.alvinsvitzer.flixbook.utilities.MovieDBJSONUtils;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/15/17.
 */

public class MovieRemoteDataStore implements MovieDataSource {

    private VolleyNetworkSingleton mNetworkSingleton;
    private String mApiKey;
    private static MovieRemoteDataStore INSTANCE;
    private static final String TAG = MovieRemoteDataStore.class.getSimpleName();

    // Prevent direct instantiation.
    private MovieRemoteDataStore(@NonNull VolleyNetworkSingleton volleyNetworkSingleton
    ,@NonNull String apiKey) {

        mNetworkSingleton = checkNotNull(volleyNetworkSingleton, "volleyNetworkSingleton cannot be null");
        mApiKey = checkNotNull(apiKey, "apiKey cannot be null");


    }

    public static synchronized MovieRemoteDataStore getInstance(@NonNull VolleyNetworkSingleton volleyNetworkSingleton
                                                                ,@NonNull String apiKey) {
        checkNotNull(volleyNetworkSingleton);
        checkNotNull(apiKey);

        if (INSTANCE == null) {
            INSTANCE = new MovieRemoteDataStore(volleyNetworkSingleton, apiKey);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(@NonNull final GetMoviesCallback callback
            , @NonNull MoviesFilterType moviesFilterType) {

        URL url;

        if (moviesFilterType == MoviesFilterType.HIGHLY_RATED_MOVIES) {

            url = MovieDBUtils.buildHighestRatingURL(mApiKey);

        } else {

            url = MovieDBUtils.buildMostPopularURL(mApiKey);
        }

        JsonObjectRequest jsObjectRequest  = new JsonObjectRequest
                (Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            List<Movie> addMovie = MovieDBJSONUtils.getMovieDataFromJSONObject(response);

                            callback.onMoviesLoaded(addMovie);

                        } catch (JSONException e) {

                            Log.e(MovieRemoteDataStore.TAG, "onResponse: ", e);
                            e.printStackTrace();

                            callback.onDataNotAvailable();
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "onErrorResponse: ", error);

                        callback.onDataNotAvailable();

                    }
                });

        mNetworkSingleton.addToRequestQueue(jsObjectRequest);
    }

    @Override
    public void getMovie(@NonNull GetMovieCallback callback
            , @NonNull MoviesFilterType moviesFilterType) {

    }

    @Override
    public void getTrailers(@NonNull String movieId, @NonNull final GetTrailersCallback callback) {

        URL url = MovieDBUtils.buildMovieVideoURL(mApiKey,movieId);

        JsonObjectRequest jsObjectRequest  = new JsonObjectRequest
                (Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            List<Trailer> addTrailer = MovieDBJSONUtils.getVideoDataFromJSONObject(response);

                            callback.onTrailersLoaded(addTrailer);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "onErrorResponse: ", error);

                        callback.onDataNotAvailable();

                    }
                });

        mNetworkSingleton.addToRequestQueue(jsObjectRequest);

    }

}