package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.local.MovieDataStoreInMemory;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.NullMovie;
import com.alvinsvitzer.flixbook.data.model.Review;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 3/13/17.
 */

public class MovieReviewPresenter implements MovieReviewContract.Presenter
        , MovieDataStoreInMemory.GetMovieCallback
        , MovieDataStore.GetReviewsCallback{

    private final AppRepository mAppRepository;
    private MovieReviewContract.View mView;
    private Movie mMovie;

    MovieReviewPresenter(@NonNull MovieReviewContract.View view, @NonNull AppRepository appRepository){

        mView = checkNotNull(view, "view cannot be null");
        mAppRepository = checkNotNull(appRepository, "appRepository cannot be null");
        mMovie = NullMovie.getInstance();

    }

    @Override
    public void start() {

        mAppRepository.getMovie(this);
        mAppRepository.getReviews(String.valueOf(mMovie.getMovieId()), this);

    }

    @Override
    public void onMovieLoaded(Movie movie) {
        mMovie = movie;
    }

    @Override
    public void onMovieDataNotAvailable() {

        mView.showNoDataTextView();

    }

    @Override
    public void onReviewsLoaded(List<Review> reviewList) {

        mView.displayReviews(reviewList);
        mView.hideNoDataTextView();
    }

    @Override
    public void onReviewDataNotAvailable() {

        mView.showNoDataTextView();

    }

    @Override
    public void detachView() {
        mView = null;
    }
}
