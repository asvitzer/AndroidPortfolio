package com.alvinsvitzer.flixbook.moviedetail.fragments;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.moviedetail.fragments.MovieOverviewContract.Presenter;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/17/17.
 */

public class MovieOverviewPresenter implements Presenter, Serializable {

    private MovieOverviewContract.View mView;
    private final Movie mMovie;

    MovieOverviewPresenter(@NonNull MovieOverviewContract.View view, @NonNull Movie movie){

        mView = checkNotNull(view, "View cannot be null");
        mMovie = checkNotNull(movie, "movie cannot be null");

    }

    @Override
    public void start() {

        mView.setPlot(mMovie.getPlotSynopsis());
        mView.setReleaseDate(MovieDBUtils.getLocalDate(mMovie.getReleaseDate()));
        mView.setVoteAverage(mMovie.getVoteAverage());

    }

    @Override
    public void detachView() {

        mView = null;
    }

}
