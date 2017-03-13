package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;

import com.alvinsvitzer.flixbook.BasePresenter;
import com.alvinsvitzer.flixbook.BaseView;
import com.alvinsvitzer.flixbook.data.model.Review;

import java.util.List;

/**
 * Created by Alvin on 3/13/17.
 */

public interface MovieReviewContract {

    interface Presenter extends BasePresenter{}

    interface View extends BaseView{

        void attachPresenter();
        void notifyNoReviews();
        void displayReviews(List<Review> reviews);

    }

}
