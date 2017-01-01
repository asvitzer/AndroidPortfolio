package com.alvinsvitzer.flixbook.utilities;

import android.content.Context;
import android.util.Log;

import com.alvinsvitzer.flixbook.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvin on 12/29/16.
 */

public class MovieDBJSONUtils {

    private static final String TAG = MovieDBJSONUtils.class.getSimpleName();

    // TODO Remove Context object if not being used.

    public static List<Movie> getMovieDataFromJSONObject(Context context, JSONObject jsonObject)
            throws JSONException {

        List<Movie> movieList = new ArrayList<>();

        JSONArray movieArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < movieArray.length(); i++) {

            Movie addMovie = new Movie();
            JSONObject movieJson = movieArray.getJSONObject(i);

            addMovie.setMoviePoster(movieJson.getString("poster_path"));
            addMovie.setMovieTitle(movieJson.getString("title"));
            addMovie.setPlotSynopsis(movieJson.getString("overview"));
            addMovie.setUserRating(movieJson.getDouble("vote_average"));
            addMovie.setReleaseDate(movieJson.getString("release_date"));
            addMovie.setMovieId(movieJson.getInt("id"));

            movieList.add(addMovie);

            Log.d(TAG, "getMovieDataFromJSONObject: " + addMovie.toString());

        }

        return movieList;

    }


}
