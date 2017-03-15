package com.alvinsvitzer.flixbook.data;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.local.MovieDataStoreLocal;
import com.alvinsvitzer.flixbook.data.local.MovieLocalDataStoreImpl;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.Review;
import com.alvinsvitzer.flixbook.data.model.Trailer;
import com.alvinsvitzer.flixbook.data.remote.MovieDataStoreRemote;
import com.alvinsvitzer.flixbook.data.remote.MovieDataStoreRemoteImpl;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/17/17.
 */

public class AppRepository implements MovieDataStoreLocal, MovieDataStoreRemote {

    private static AppRepository INSTANCE = null;

    private final MovieDataStoreRemoteImpl mMovieDataStoreRemoteImpl;
    private final MovieLocalDataStoreImpl mMovieLocalDataStoreImpl;

    private AppRepository(@NonNull MovieDataStoreRemoteImpl movieDataStoreRemoteImpl
            ,@NonNull MovieLocalDataStoreImpl movieLocalDataStoreImpl){

        mMovieDataStoreRemoteImpl = checkNotNull(movieDataStoreRemoteImpl,"movieDataStoreRemoteImpl cannot be null");
        mMovieLocalDataStoreImpl = checkNotNull(movieLocalDataStoreImpl, "movieLocalDataStoreImpl cannot be null");

    }

    public static synchronized AppRepository getInstance(@NonNull MovieDataStoreRemoteImpl movieDataStoreRemoteImpl,
                                                         @NonNull MovieLocalDataStoreImpl movieLocalDataStoreImpl) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(movieDataStoreRemoteImpl, movieLocalDataStoreImpl);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(@NonNull GetMoviesCallback callback, @NonNull MoviesFilterType moviesFilterType) {

        mMovieDataStoreRemoteImpl.getMovies(callback, moviesFilterType);

    }

    @Override
    public void getMovie(@NonNull GetMovieCallback callback) {

        mMovieLocalDataStoreImpl.getMovie(callback);
    }

    @Override
    public void getTrailers(@NonNull final String movieId, @NonNull final GetTrailersCallback callback) {

        mMovieLocalDataStoreImpl.getTrailers(movieId, new GetTrailersCallback() {
            @Override
            public void onTrailersLoaded(List<Trailer> trailerList) {

                callback.onTrailersLoaded(trailerList);
            }

            @Override
            public void onTrailerDataNotAvailable() {

                mMovieDataStoreRemoteImpl.getTrailers(movieId, new GetTrailersCallback() {
                    @Override
                    public void onTrailersLoaded(List<Trailer> trailerList) {

                        saveTrailers(trailerList);
                        callback.onTrailersLoaded(trailerList);
                    }

                    @Override
                    public void onTrailerDataNotAvailable() {

                        callback.onTrailerDataNotAvailable();
                    }
                });

            }
        });

    }

    @Override
    public void getReviews(@NonNull final String movieId, @NonNull final GetReviewsCallback callback) {

        mMovieLocalDataStoreImpl.getReviews(movieId, new GetReviewsCallback() {
            @Override
            public void onReviewsLoaded(List<Review> reviewList) {
                callback.onReviewsLoaded(reviewList);
            }

            @Override
            public void onReviewDataNotAvailable() {

                mMovieDataStoreRemoteImpl.getReviews(movieId, new GetReviewsCallback() {
                    @Override
                    public void onReviewsLoaded(List<Review> reviewList) {
                        saveReviews(reviewList);
                        callback.onReviewsLoaded(reviewList);
                    }

                    @Override
                    public void onReviewDataNotAvailable() {
                        callback.onReviewDataNotAvailable();

                    }
                });

            }
        });

    }

    @Override
    public void saveMovie(@NonNull Movie movie) {

        mMovieLocalDataStoreImpl.saveMovie(movie);

    }

    @Override
    public void saveReviews(List<Review> reviews) {

        mMovieLocalDataStoreImpl.saveReviews(reviews);
    }

    @Override
    public void saveTrailers(List<Trailer> trailers) {

        mMovieLocalDataStoreImpl.saveTrailers(trailers);

    }

}
