package com.alvinsvitzer.flixbook.movies;

import com.alvinsvitzer.flixbook.BasePresenter;
import com.alvinsvitzer.flixbook.BaseView;
import com.alvinsvitzer.flixbook.model.Movie;

import java.util.List;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MoviesContract {

    interface Presenter extends BasePresenter{

        void loadMovies();
        void attachView(View view);
        void detachView();

    }

    interface View extends BaseView{

        MoviesFilterType getSortingId();
        void hideNoDataTextView();
        void showNoDataTextView();
        void showMovies(List<Movie> movieList);
    }
}
