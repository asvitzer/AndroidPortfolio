package com.alvinsvitzer.flixbook.data;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;

import java.util.List;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MovieDataSource {

    interface GetMoviesCallback {

        void onTasksLoaded(List<Movie> movieList);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {

        void onTaskLoaded(Movie movie);

        void onDataNotAvailable();
    }

    void getMovies(@NonNull GetMoviesCallback callback
            , @NonNull MoviesFilterType moviesFilterType
            , @NonNull String apiKey);

    void getMovie(@NonNull GetMovieCallback callback
            , @NonNull MoviesFilterType moviesFilterType
            , @NonNull String apiKey);
}