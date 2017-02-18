package com.alvinsvitzer.flixbook.movies;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/15/17.
 */

public class MoviesPresenter implements MoviesContract.Presenter, MovieDataStore.GetMoviesCallback {

    private final MovieDataStore mMovieRepo;
    private MoviesContract.View mMoviesView;
    private final ImageLoader mImageLoader;

    MoviesPresenter(@NonNull MovieDataStore movieRepo
                    , @NonNull MoviesContract.View moviesView
                    , @NonNull ImageLoader imageLoader
                    ){

        mMovieRepo = checkNotNull(movieRepo, "movieRepo cannot be null");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null");
        mImageLoader = checkNotNull(imageLoader, "imageLoader cannot be null");
    }

    @Override
    public void start() {

        mMoviesView.hideNoDataTextView();
        loadMovies();

    }

    @Override
    public void loadMovies() {

        MoviesFilterType sortingId = mMoviesView.getSortingId();

        mMovieRepo.getMovies(this, sortingId);

    }

    @Override
    public void detachView() {

        mMoviesView = null;
    }

    @Override
    public void saveMovie(Movie movie) {
        mMovieRepo.saveMovie(movie);
    }

    @Override
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    @Override
    public String buildMovieUrl(Movie movie) {
        return MovieDBUtils.buildMoviePosterURL(movie.getMoviePoster()).toString();
    }

    @Override
    public void onMoviesLoaded(List<Movie> movieList) {

        mMoviesView.showMovies(movieList);

    }

    @Override
    public void onMovieListDataNotAvailable() {

        mMoviesView.showNoDataTextView();

    }

}



