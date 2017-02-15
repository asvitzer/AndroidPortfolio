package com.alvinsvitzer.flixbook.data.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alvinsvitzer.flixbook.data.MovieDataSource;
import com.alvinsvitzer.flixbook.model.Movie;
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
    private static MovieRemoteDataStore INSTANCE;
    private static final String TAG = MovieRemoteDataStore.class.getSimpleName();

    // Prevent direct instantiation.
    private MovieRemoteDataStore(@NonNull VolleyNetworkSingleton volleyNetworkSingleton) {

        checkNotNull(volleyNetworkSingleton);
        mNetworkSingleton = volleyNetworkSingleton;

    }

    public static synchronized MovieRemoteDataStore getInstance(@NonNull VolleyNetworkSingleton volleyNetworkSingleton) {
        checkNotNull(volleyNetworkSingleton);

        if (INSTANCE == null) {
            INSTANCE = new MovieRemoteDataStore(volleyNetworkSingleton);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(@NonNull final GetMoviesCallback callback
            , @NonNull MoviesFilterType moviesFilterType
            , @NonNull String apiKey) {

        URL url;

        if (moviesFilterType == MoviesFilterType.HIGHLY_RATED_MOVIES) {

            url = MovieDBUtils.buildHighestRatingURL(apiKey);

        } else {

            url = MovieDBUtils.buildMostPopularURL(apiKey);
        }

        JsonObjectRequest jsObjectRequest  = new JsonObjectRequest
                (Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            List<Movie> addMovie = MovieDBJSONUtils.getMovieDataFromJSONObject(response);

                            callback.onTasksLoaded(addMovie);

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
            , @NonNull MoviesFilterType moviesFilterType
            , @NonNull String apiKey) {

    }

}