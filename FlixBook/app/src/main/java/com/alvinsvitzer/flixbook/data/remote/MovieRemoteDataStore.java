package com.alvinsvitzer.flixbook.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.Trailer;
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

public class MovieRemoteDataStore implements MovieDataStore {

    private VolleyNetworkSingleton mNetworkSingleton;
    private String mApiKey;
    private static MovieRemoteDataStore INSTANCE;
    private static final String TAG = MovieRemoteDataStore.class.getSimpleName();

    // Prevent direct instantiation.
    private MovieRemoteDataStore(@NonNull Context context) {
        checkNotNull(context);

        mNetworkSingleton = VolleyNetworkSingleton.getInstance(context);
        setMovieDBApiKey(context.getString(R.string.the_movie_db_auth_key));

    }

    public static synchronized MovieRemoteDataStore getInstance(@NonNull Context context) {

        checkNotNull(context);

        if (INSTANCE == null) {
            INSTANCE = new MovieRemoteDataStore(context);
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

                            callback.onMovieListDataNotAvailable();
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "onErrorResponse: ", error);

                        callback.onMovieListDataNotAvailable();

                    }
                });

        mNetworkSingleton.addToRequestQueue(jsObjectRequest);
    }

    @Override
    public void getMovie(@NonNull GetMovieCallback callback) {

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

                        callback.onTrailerDataNotAvailable();

                    }
                });

        mNetworkSingleton.addToRequestQueue(jsObjectRequest);

    }

    @Override
    public void saveMovie(@NonNull Movie movie) {
        throw new RuntimeException("Operation Not Implemented");
    }

    @Override
    public void saveMovies(@NonNull List<Movie> movieList) {
        throw new RuntimeException("Operation Not Implemented");

    }

    public void setMovieDBApiKey(String key){

        if (key != null && !key.equals("")){

            mApiKey = key;

        }else {

            Log.e(TAG, "setMovieDBApiKey: key cannot be null or empty", new IllegalArgumentException());
        }

    }

}