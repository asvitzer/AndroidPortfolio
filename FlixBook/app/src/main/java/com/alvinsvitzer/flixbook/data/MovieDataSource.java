package com.alvinsvitzer.flixbook.data;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.model.Trailer;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;

import java.util.List;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MovieDataSource {

    interface GetMoviesCallback {

        void onMoviesLoaded(List<Movie> movieList);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {

        void onMovieLoaded(Movie movie);

        void onDataNotAvailable();
    }

    interface GetTrailersCallback {

        void onTrailersLoaded(List<Trailer> trailerList);

        void onDataNotAvailable();
    }

    void getMovies(@NonNull GetMoviesCallback callback
            , @NonNull MoviesFilterType moviesFilterType);

    void getMovie(@NonNull GetMovieCallback callback
            , @NonNull MoviesFilterType moviesFilterType);

    void getTrailers(@NonNull String movieId, @NonNull GetTrailersCallback callback);
}