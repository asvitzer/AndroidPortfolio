package com.alvinsvitzer.flixbook.data;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.Review;
import com.alvinsvitzer.flixbook.data.model.Trailer;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;

import java.util.List;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MovieDataStore {

    interface GetMoviesCallback {

        void onMoviesLoaded(List<Movie> movieList);

        void onMovieListDataNotAvailable();
    }

    interface GetMovieCallback {

        void onMovieLoaded(Movie movie);

        void onMovieDataNotAvailable();
    }

    interface GetTrailersCallback {

        void onTrailersLoaded(List<Trailer> trailerList);

        void onTrailerDataNotAvailable();
    }

    interface GetReviewsCallback{

        void onReviewsLoaded(List<Review> reviewList);

        void onReviewDataNotAvailable();

    }

    void getMovies(@NonNull GetMoviesCallback callback
            , @NonNull MoviesFilterType moviesFilterType);

    void getMovie(@NonNull GetMovieCallback callback);

    void getTrailers(@NonNull String movieId, @NonNull GetTrailersCallback callback);

    void getReviews(@NonNull String movieId, @NonNull GetReviewsCallback callback);

    void saveMovie(@NonNull Movie movie);

    void saveMovies(@NonNull List<Movie> movieList);
}