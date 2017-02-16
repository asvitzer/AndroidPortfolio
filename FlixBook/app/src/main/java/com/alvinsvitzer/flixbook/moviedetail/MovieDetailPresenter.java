package com.alvinsvitzer.flixbook.moviedetail;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alvinsvitzer.flixbook.data.remote.MovieRemoteDataStore;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.model.Trailer;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.YouTubeUtils;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/15/17.
 */

public class MovieDetailPresenter implements MovieDetailsContract.Presenter, MovieRemoteDataStore.GetTrailersCallback {

    @NonNull
    private final Movie mMovie;

    @NonNull
    private final MovieRemoteDataStore mMovieRemoteDataStore;

    @NonNull
    private final ImageLoader mImageLoader;

    private MovieDetailsContract.View mView;

    public static final String TAG = MovieDetailPresenter.class.getSimpleName();

    MovieDetailPresenter(@NonNull Movie movie
                        ,@NonNull MovieRemoteDataStore movieRemoteDataStore
                        ,@NonNull ImageLoader imageLoader
                        ,@NonNull MovieDetailsContract.View view){

        mMovie = checkNotNull(movie, "Movie cannot be null");
        mMovieRemoteDataStore = checkNotNull(movieRemoteDataStore, "movieRemoteDataStore cannot be null");
        mImageLoader = checkNotNull(imageLoader, "imageLoader cannot be null");
        mView = checkNotNull(view, "view cannot be null");

    }

    @Override
    public void attachView(MovieDetailsContract.View view) {

        mView = view;
    }

    @Override
    public void detachView() {

        mView = null;
    }

    @Override
    public void start() {

        mView.disableTrailerFab();

        String posterImageUrl = MovieDBUtils.buildMoviePosterURL(mMovie.getMoviePoster()).toString();
        mView.setPosterImage(posterImageUrl,mImageLoader);

        String backdropImageUrl = MovieDBUtils.buildMovieBackdropURL(mMovie.getMovieBackdrop()).toString();
        mView.setBackdropImage(backdropImageUrl,mImageLoader);

        mView.setBannerText(mMovie.getMovieTitle());

        mMovieRemoteDataStore.getTrailers(String.valueOf(mMovie.getMovieId()), this);

    }

    @Override
    public void onTrailersLoaded(List<Trailer> trailerList) {

        mMovie.setTrailerList(trailerList);
        getOfficialYouTubeTrailerUrl();

    }

    @Override
    public void getOfficialYouTubeTrailerUrl(){

        if(mMovie.getTrailerList() == null){

            Log.w(TAG, "getOfficialYouTubeTrailerUrl: Trailer List is empty");
            mView.notifyUserNoTrailer();
            return;
        }

        for(Trailer t: mMovie.getTrailerList()){

            if (t.getType().equals(MovieDBUtils.ATTRIBUTE_VIDEO_TYPE_TRAILER) && t.getSite().equals(MovieDBUtils.ATTRIBUTE_VIDEO_SITE_YOUTUBE)){
                mView.setTrailerUri(Uri.parse(YouTubeUtils.buildYouTubeUrl(t.getKey()).toString()));
                mView.enableTrailerFab();
                return;
            }
        }

        Log.w(TAG, "getOfficialYouTubeTrailerUrl: No trailer for YouTube.");
        mView.notifyUserNoTrailer();

    }



    @Override
    public void onDataNotAvailable() {

        mView.disableTrailerFab();

    }
}
