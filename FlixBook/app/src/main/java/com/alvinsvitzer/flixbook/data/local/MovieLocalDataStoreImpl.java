package com.alvinsvitzer.flixbook.data.local;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.NullMovie;
import com.alvinsvitzer.flixbook.data.model.Review;
import com.alvinsvitzer.flixbook.data.model.Trailer;

import java.util.List;

/**
 * Created by Alvin on 2/17/17.
 */

public class MovieLocalDataStoreImpl implements MovieDataStoreLocal {

    private static MovieLocalDataStoreImpl INSTANCE;

    private List<Trailer> mTrailerList;
    private List<Review> mReviewList;
    private Movie mCurrentMovie;

    private MovieLocalDataStoreImpl(){

        mCurrentMovie = NullMovie.getInstance();
    }

    public static synchronized MovieLocalDataStoreImpl getInstance(){

        if(INSTANCE == null){

           INSTANCE = new MovieLocalDataStoreImpl();
        }

        return INSTANCE;

    }

    @Override
    public void getMovie(@NonNull MovieDataStoreLocal.GetMovieCallback callback) {

        if (mCurrentMovie != null){

            callback.onMovieLoaded(mCurrentMovie);

        } else {

            callback.onMovieDataNotAvailable();

        }

    }

    @Override
    public void getTrailers(@NonNull String movieId, @NonNull MovieDataStore.GetTrailersCallback callback) {

        if (mTrailerList != null && String.valueOf(mCurrentMovie.getMovieId()).equals(movieId)){

            callback.onTrailersLoaded(mTrailerList);

        } else {

            callback.onTrailerDataNotAvailable();

        }

    }

    @Override
    public void getReviews(@NonNull String movieId, @NonNull MovieDataStore.GetReviewsCallback callback) {

        if (mReviewList != null && String.valueOf(mCurrentMovie.getMovieId()).equals(movieId)){

            callback.onReviewsLoaded(mReviewList);
        } else {

            callback.onReviewDataNotAvailable();
        }


    }

    @Override
    public void saveMovie(Movie movie){

        /**
         * If the movie being saved is different from the one cached then save it
         * and clear out the reviews and trailers since they will be for the previously
         * cached movie. Clearing them out results in the AppRepository deciding it
         * needs to make a network call to get new data if finds the local data store empty.
         */
        if (movie.getMovieId() != mCurrentMovie.mMovieId){

            mCurrentMovie = movie;
            mReviewList = null;
            mTrailerList = null;
        }
    }

    @Override
    public void saveReviews(List<Review> reviews) {

        mReviewList = reviews;
    }

    @Override
    public void saveTrailers(List<Trailer> trailers) {

        mTrailerList = trailers;

    }

}
