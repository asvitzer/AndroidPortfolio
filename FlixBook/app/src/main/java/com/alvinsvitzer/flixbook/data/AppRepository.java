package com.alvinsvitzer.flixbook.data;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.local.FavoriteDataStoreLocal;
import com.alvinsvitzer.flixbook.data.local.FavoriteDataStoreLocalImpl;
import com.alvinsvitzer.flixbook.data.local.MovieDataStoreInMemory;
import com.alvinsvitzer.flixbook.data.local.MovieDataStoreInMemoryImpl;
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

public class AppRepository implements MovieDataStoreInMemory, MovieDataStoreRemote, FavoriteDataStoreLocal {

    private static AppRepository INSTANCE = null;

    private final MovieDataStoreRemoteImpl mMovieDataStoreRemoteImpl;
    private final MovieDataStoreInMemoryImpl mMovieDataStoreInMemoryImpl;
    private final FavoriteDataStoreLocalImpl mFavoriteDataStoreLocalImpl;

    private AppRepository(@NonNull MovieDataStoreRemoteImpl movieDataStoreRemoteImpl
            , @NonNull MovieDataStoreInMemoryImpl movieDataStoreInMemoryImpl
            , @NonNull FavoriteDataStoreLocalImpl favoriteDataStoreLocalImpl) {

        mMovieDataStoreRemoteImpl = checkNotNull(movieDataStoreRemoteImpl,"movieDataStoreRemoteImpl cannot be null");
        mMovieDataStoreInMemoryImpl = checkNotNull(movieDataStoreInMemoryImpl, "movieDataStoreInMemoryImpl cannot be null");
        mFavoriteDataStoreLocalImpl = checkNotNull(favoriteDataStoreLocalImpl, "favoriteDataStoreLocalImpl cannot be null");

    }

    public static synchronized AppRepository getInstance(@NonNull MovieDataStoreRemoteImpl movieDataStoreRemoteImpl,
                                                         @NonNull MovieDataStoreInMemoryImpl movieDataStoreInMemoryImpl
            , @NonNull FavoriteDataStoreLocalImpl favoriteDataStoreLocalImpl) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(movieDataStoreRemoteImpl, movieDataStoreInMemoryImpl, favoriteDataStoreLocalImpl);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(@NonNull GetMoviesCallback callback, @NonNull MoviesFilterType moviesFilterType) {

        mMovieDataStoreRemoteImpl.getMovies(callback, moviesFilterType);

    }

    @Override
    public void getMovie(@NonNull GetMovieCallback callback) {

        mMovieDataStoreInMemoryImpl.getMovie(callback);
    }

    @Override
    public void getTrailers(@NonNull final String movieId, @NonNull final GetTrailersCallback callback) {

        mMovieDataStoreInMemoryImpl.getTrailers(movieId, new GetTrailersCallback() {
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

        mMovieDataStoreInMemoryImpl.getReviews(movieId, new GetReviewsCallback() {
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

        mMovieDataStoreInMemoryImpl.saveMovie(movie);

    }

    @Override
    public void saveReviews(List<Review> reviews) {

        mMovieDataStoreInMemoryImpl.saveReviews(reviews);
    }

    @Override
    public void saveTrailers(List<Trailer> trailers) {

        mMovieDataStoreInMemoryImpl.saveTrailers(trailers);

    }

    @Override
    public void checkFavorite(@NonNull String movieId, @NonNull CheckMovieCallback callback) {

        mFavoriteDataStoreLocalImpl.checkFavorite(movieId, callback);

    }

    @Override
    public void addFavoriteMovie(@NonNull String movieId) {
        mFavoriteDataStoreLocalImpl.addFavoriteMovie(movieId);
    }

    @Override
    public void removeFavoriteMovie(@NonNull String movieId) {
        mFavoriteDataStoreLocalImpl.removeFavoriteMovie(movieId);
    }

}
