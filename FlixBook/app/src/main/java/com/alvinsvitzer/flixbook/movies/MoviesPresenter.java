package com.alvinsvitzer.flixbook.movies;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.data.local.FavoriteDataStoreLocal;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.remote.MovieDataStoreRemote;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.alvinsvitzer.flixbook.data.local.FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID;
import static com.alvinsvitzer.flixbook.data.local.FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER_LINK;
import static com.alvinsvitzer.flixbook.data.local.FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.alvinsvitzer.flixbook.data.local.FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/15/17.
 */

public class MoviesPresenter implements MoviesContract.Presenter
        , MovieDataStoreRemote.GetMoviesCallback
        , FavoriteDataStoreLocal.GetFavoritesCallback {

    private final AppRepository mMovieRepo;
    private final ImageLoader mImageLoader;
    @VisibleForTesting
    protected MoviesContract.View mMoviesView;

    MoviesPresenter(@NonNull AppRepository movieRepo
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

    @Override
    public void onLoad(Cursor favorites) {

        List<Movie> favoriteList = new ArrayList<>();

        while (!favorites.isAfterLast()) {

            Movie movie = new Movie();
            movie.setMovieId(Integer.valueOf(favorites.getString(favorites.getColumnIndex(COLUMN_MOVIE_ID))));
            movie.setReleaseDate(favorites.getString(favorites.getColumnIndex(COLUMN_MOVIE_RELEASE_DATE)));
            movie.setMovieTitle(favorites.getString(favorites.getColumnIndex(COLUMN_MOVIE_TITLE)));
            movie.setMoviePoster(favorites.getString(favorites.getColumnIndex(COLUMN_MOVIE_POSTER_LINK)));

            favoriteList.add(movie);

            favorites.moveToNext();

        }

        mMoviesView.showMovies(favoriteList);

        favorites.close();

    }

    @Override
    public void onDataNotAvailable() {

        mMoviesView.showNoDataTextView();
    }
}



