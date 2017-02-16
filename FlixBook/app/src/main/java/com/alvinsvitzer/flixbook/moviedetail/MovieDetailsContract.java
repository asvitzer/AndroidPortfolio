package com.alvinsvitzer.flixbook.moviedetail;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.BasePresenter;
import com.alvinsvitzer.flixbook.BaseView;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Alvin on 2/15/17.
 */

public interface MovieDetailsContract {

    interface Presenter extends BasePresenter{

        void attachView(MovieDetailsContract.View view);
        void detachView();
        void getOfficialYouTubeTrailerUrl();


    }
    interface View extends BaseView{

        void attachPresenter();
        void setPosterImage(@NonNull String imageUrl, @NonNull ImageLoader imageLoader);
        void setBackdropImage(@NonNull String imageUrl, @NonNull ImageLoader imageLoader);
        void setBannerText(String text);
        void disableTrailerFab();
        void enableTrailerFab();
        void setTrailerUri(Uri uri);
        void notifyUserNoTrailer();


    }
}

