package com.alvinsvitzer.flixbook.data.remote;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;

import java.util.List;

/**
 * Created by Alvin on 3/15/17.
 */

public interface MovieDataStoreRemote extends MovieDataStore{

    interface GetMoviesCallback {

        void onMoviesLoaded(List<Movie> movieList);

        void onMovieListDataNotAvailable();
    }

    void getMovies(@NonNull GetMoviesCallback callback
            , @NonNull MoviesFilterType moviesFilterType);
}
