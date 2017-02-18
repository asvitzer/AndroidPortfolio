package com.alvinsvitzer.flixbook.utilities;

import android.util.Log;

import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.Trailer;

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

    public static List<Movie> getMovieDataFromJSONObject(JSONObject jsonObject)
            throws JSONException {

        List<Movie> movieList = new ArrayList<>();

        JSONArray movieArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < movieArray.length(); i++) {

            Movie addMovie = new Movie();
            JSONObject movieJson = movieArray.getJSONObject(i);

            addMovie.setMoviePoster(movieJson.getString("poster_path"));
            addMovie.setMovieBackdrop(movieJson.getString("backdrop_path"));
            addMovie.setMovieTitle(movieJson.getString("title"));
            addMovie.setPlotSynopsis(movieJson.getString("overview"));
            addMovie.setVoteAverage(movieJson.getDouble("vote_average"));
            addMovie.setReleaseDate(movieJson.getString("release_date"));
            addMovie.setMovieId(movieJson.getInt("id"));

            movieList.add(addMovie);

            Log.d(TAG, "getMovieDataFromJSONObject: " + addMovie.toString());

        }

        return movieList;

    }

    public static List<Trailer> getVideoDataFromJSONObject(JSONObject jsonObject)
            throws JSONException {

        List<Trailer> videoList = new ArrayList<>();

        JSONArray trailerArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < trailerArray.length(); i++) {

            Trailer addTrailer = new Trailer();
            JSONObject trailerJson = trailerArray.getJSONObject(i);

            addTrailer.setId(trailerJson.getString("id"));
            addTrailer.setKey(trailerJson.getString("key"));
            addTrailer.setName(trailerJson.getString("name"));
            addTrailer.setSite(trailerJson.getString("site"));
            addTrailer.setType(trailerJson.getString("type"));

            videoList.add(addTrailer);

            Log.d(TAG, "getVideoDataFromJSONObject: " + addTrailer.toString());

        }

        return videoList;

    }


}
