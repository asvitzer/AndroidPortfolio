package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;

import com.alvinsvitzer.flixbook.BasePresenter;
import com.alvinsvitzer.flixbook.BaseView;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MovieOverviewContract {

    interface Presenter extends BasePresenter{

        void detachView();


    }
    interface View extends BaseView{

        void attachPresenter();
        void setPlot(String plot);
        void setReleaseDate(String releaseDate);
        void setVoteAverage(Double voteAverage);
        void notifyNoMovieData();


    }
}

