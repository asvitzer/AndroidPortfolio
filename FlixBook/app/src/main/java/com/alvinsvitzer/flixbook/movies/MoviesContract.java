package com.alvinsvitzer.flixbook.movies;

import com.alvinsvitzer.flixbook.BasePresenter;
import com.alvinsvitzer.flixbook.BaseView;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MoviesContract {

    interface Presenter extends BasePresenter{

        void loadMovies();
        void detachView();
        void saveMovie(Movie movie);
        ImageLoader getImageLoader();
        String buildMovieUrl(Movie movie);

    }

    interface View extends BaseView{

        MoviesFilterType getSortingId();
        void attachPresenter();
        void hideNoDataTextView();
        void showNoDataTextView();
        void showMovies(List<Movie> movieList);
    }
}
