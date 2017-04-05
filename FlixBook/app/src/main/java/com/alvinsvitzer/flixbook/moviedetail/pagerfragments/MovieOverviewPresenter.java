package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.data.local.FavoriteDataStoreLocal;
import com.alvinsvitzer.flixbook.data.local.MovieDataStoreInMemory;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.NullMovie;
import com.alvinsvitzer.flixbook.logger.Logger;
import com.alvinsvitzer.flixbook.moviedetail.pagerfragments.MovieOverviewContract.Presenter;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/17/17.
 */

public class MovieOverviewPresenter implements Presenter
        , MovieDataStoreInMemory.GetMovieCallback
        , FavoriteDataStoreLocal.CheckMovieCallback {

    private static final String TAG = MovieOverviewPresenter.class.getSimpleName();
    @NonNull
    private final AppRepository mAppRepository;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    MovieOverviewContract.View mView;
    private Movie mMovie;
    private boolean mIsFavorited;
    private Logger mLogger;

    MovieOverviewPresenter(@NonNull MovieOverviewContract.View view
            , @NonNull AppRepository appRepository
            , @NonNull Logger logger) {

        mView = checkNotNull(view, "View cannot be null");
        mAppRepository = checkNotNull(appRepository, "appRepository cannot be null");
        mLogger = checkNotNull(logger, "logger cannot be null");
        mMovie = NullMovie.getInstance();

    }

    @Override
    public void start() {

        mAppRepository.getMovie(this);
        mView.setFavoriteFabEnabled(false);
    }

    @Override
    public void detachView() {

        mView = null;
    }

    @Override
    public void isMovieStored() {

        mAppRepository.checkFavorite(String.valueOf(mMovie.getMovieId())
                , this);
    }

    @Override
    public void favoriteFabClicked() {

        //Set the image of the fab favorite icon when clicked
        mView.setFavoriteFabEnabled(mIsFavorited);

        //Toggle the boolean to be the opposite of its current value
        mIsFavorited = !mIsFavorited;

        if (mIsFavorited) {

            mAppRepository.addFavoriteMovie(mMovie);
            mView.displayFavorite();

        } else {

            mAppRepository.removeFavoriteMovie(String.valueOf(mMovie.getMovieId()));
            mView.displayFavoriteRemoval();
        }

    }

    @Override
    public void onMovieLoaded(Movie movie) {

        mMovie = movie;
        mView.setPlot(mMovie.getPlotSynopsis());
        mView.setReleaseDate(MovieDBUtils.getLocalDate(mMovie.getReleaseDate()));
        mView.setVoteAverage(mMovie.getVoteAverage());

        isMovieStored();
    }

    @Override
    public void onMovieDataNotAvailable() {

        mView.setPlot(mMovie.getPlotSynopsis());
        mView.setReleaseDate(mMovie.getReleaseDate());
        mView.setVoteAverage(mMovie.getVoteAverage());

        mView.notifyNoMovieData();

    }

    public void movieStored(boolean movieStored) {

        mView.setFavoriteFabEnabled(true);

        mIsFavorited = movieStored;

        mView.setFavoriteFabImage(mIsFavorited);

        mLogger.logd(TAG, "method: movieStored | " + mMovie.getMovieId() + " isFavorite: " + mIsFavorited);
    }
}
