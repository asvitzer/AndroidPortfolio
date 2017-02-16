package com.alvinsvitzer.flixbook.movies;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.MovieDataSource;
import com.alvinsvitzer.flixbook.data.remote.MovieRemoteDataStore;
import com.alvinsvitzer.flixbook.model.Movie;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/15/17.
 */

public class MoviesPresenter implements MoviesContract.Presenter, MovieDataSource.GetMoviesCallback {

    private final MovieRemoteDataStore mMovieRemoteDataStore;
    private MoviesContract.View mMoviesView;

    MoviesPresenter(@NonNull MovieRemoteDataStore movieRemoteDataStore
                    , @NonNull MoviesContract.View moviesView
                    ){

        mMovieRemoteDataStore = checkNotNull(movieRemoteDataStore, "movieRemoteDataStore cannot be null");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null");
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {

        mMoviesView.hideNoDataTextView();
        loadMovies();

    }

    @Override
    public void loadMovies() {

        MoviesFilterType sortingId = mMoviesView.getSortingId();

        mMovieRemoteDataStore.getMovies(this, sortingId);

    }

    @Override
    public void attachView(MoviesContract.View view) {

        mMoviesView = view;
        mMoviesView.setPresenter(this);
    }

    @Override
    public void detachView() {

        mMoviesView = null;
    }

    @Override
    public void onMoviesLoaded(List<Movie> movieList) {

        mMoviesView.showMovies(movieList);

    }

    @Override
    public void onDataNotAvailable() {

        mMoviesView.showNoDataTextView();

    }
}



