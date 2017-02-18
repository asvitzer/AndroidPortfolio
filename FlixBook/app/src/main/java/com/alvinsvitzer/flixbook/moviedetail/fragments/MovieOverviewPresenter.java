package com.alvinsvitzer.flixbook.moviedetail.fragments;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.moviedetail.fragments.MovieOverviewContract.Presenter;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/17/17.
 */

public class MovieOverviewPresenter implements Presenter, MovieDataStore.GetMovieCallback {

    private MovieOverviewContract.View mView;
    private Movie mMovie;

    @NonNull
    private final AppRepository mAppRepository;

    MovieOverviewPresenter(@NonNull MovieOverviewContract.View view, @NonNull AppRepository appRepository){

        mView = checkNotNull(view, "View cannot be null");
        mAppRepository = checkNotNull(appRepository, "appRepository cannot be null");

    }

    @Override
    public void start() {

        mAppRepository.getMovie(this);
    }

    @Override
    public void detachView() {

        mView = null;
    }

    @Override
    public void onMovieLoaded(Movie movie) {

        mMovie = movie;
        mView.setPlot(mMovie.getPlotSynopsis());
        mView.setReleaseDate(MovieDBUtils.getLocalDate(mMovie.getReleaseDate()));
        mView.setVoteAverage(mMovie.getVoteAverage());

    }

    @Override
    public void onMovieDataNotAvailable() {

        mView.notifyNoMovieData();

    }
}
