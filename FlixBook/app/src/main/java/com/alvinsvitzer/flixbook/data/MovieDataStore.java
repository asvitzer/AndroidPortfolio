package com.alvinsvitzer.flixbook.data;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.model.Review;
import com.alvinsvitzer.flixbook.data.model.Trailer;

import java.util.List;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MovieDataStore {

    interface GetTrailersCallback {

        void onTrailersLoaded(List<Trailer> trailerList);

        void onTrailerDataNotAvailable();
    }

    interface GetReviewsCallback{

        void onReviewsLoaded(List<Review> reviewList);

        void onReviewDataNotAvailable();

    }

    void getTrailers(@NonNull String movieId, @NonNull GetTrailersCallback callback);

    void getReviews(@NonNull String movieId, @NonNull GetReviewsCallback callback);

}