package com.alvinsvitzer.flixbook.moviedetail;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.data.local.FavoriteDataStoreLocal;
import com.alvinsvitzer.flixbook.data.local.MovieDataStoreInMemory;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.Trailer;
import com.alvinsvitzer.flixbook.data.remote.MovieDataStoreRemote;
import com.alvinsvitzer.flixbook.logger.Logger;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.YouTubeUtils;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Alvin on 2/15/17.
 */

public class MovieDetailPresenter implements MovieDetailsContract.Presenter
        , MovieDataStoreRemote.GetTrailersCallback
        , MovieDataStoreInMemory.GetMovieCallback
        , FavoriteDataStoreLocal.CheckMovieCallback {

    private static final String TAG = MovieDetailPresenter.class.getSimpleName();
    @NonNull
    private final AppRepository mAppRepository;
    @NonNull
    private final ImageLoader mImageLoader;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected Movie mMovie;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected MovieDetailsContract.View mView;

    private boolean mIsFavorited;
    private Logger mLogger;

    MovieDetailPresenter(@NonNull AppRepository appRepository
                        , @NonNull ImageLoader imageLoader
            , @NonNull MovieDetailsContract.View view
            , @NonNull Logger logger) {

        mAppRepository = checkNotNull(appRepository, "movieRemoteDataStore cannot be null");
        mImageLoader = checkNotNull(imageLoader, "imageLoader cannot be null");
        mView = checkNotNull(view, "view cannot be null");
        mLogger = checkNotNull(logger, "logger cannot be null");

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

        mAppRepository.getMovie(this);

        mView.disableTrailerFab();

    }

    @Override
    public void onTrailersLoaded(List<Trailer> trailerList) {

        mMovie.setTrailerList(trailerList);
        getOfficialYouTubeTrailerUrl();


    }

    @Override
    public void onTrailerDataNotAvailable() {

        mView.notifyUserNoTrailer();

    }

    @Override
    public void getOfficialYouTubeTrailerUrl(){

        if(mMovie.getTrailerList() == null){

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

        mView.notifyUserNoTrailer();

    }


    @Override
    public void onMovieLoaded(Movie movie) {

        mMovie = movie;

        mView.setActivityTitle(mMovie.getMovieTitle());

        String posterImageUrl = MovieDBUtils.buildMoviePosterURL(mMovie.getMoviePoster()).toString();
        mView.setPosterImage(posterImageUrl, mImageLoader);

        String backdropImageUrl = MovieDBUtils.buildMovieBackdropURL(mMovie.getMovieBackdrop()).toString();
        mView.setBackdropImage(backdropImageUrl, mImageLoader);

        mView.setBannerText(mMovie.getMovieTitle());

        mAppRepository.getTrailers(String.valueOf(mMovie.getMovieId()), this);

        isMovieStored();

    }

    @Override
    public void onMovieDataNotAvailable() {

        mView.notifyUserNoMovie();
    }

    @Override
    public void favoriteFabClicked() {

        //Toggle the boolean to be the opposite of its current value
        mIsFavorited = !mIsFavorited;

        //Set the image of the fab favorite icon when clicked
        mView.setFavoriteFabImage(mIsFavorited);

        if (mIsFavorited) {

            mAppRepository.addFavoriteMovie(mMovie);
            mView.displayFavorite();

        } else {

            mAppRepository.removeFavoriteMovie(String.valueOf(mMovie.getMovieId()));
            mView.displayFavoriteRemoval();
        }

    }

    @Override
    public void movieStored(boolean movieStored) {

        mIsFavorited = movieStored;

        mView.setFavoriteFabImage(mIsFavorited);

        mLogger.logd(TAG, "method: movieStored | " + mMovie.getMovieId() + " isFavorite: " + mIsFavorited);
    }

    @Override
    public void isMovieStored() {

        mAppRepository.checkFavorite(String.valueOf(mMovie.getMovieId())
                , this);
    }


}
