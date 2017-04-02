package com.alvinsvitzer.flixbook.data.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Alvin on 12/29/16. Model Movie object.
 */

@Parcel
public class Movie {

    /**
     * These fields are public because the Parceler libary requires them to be to work.
     * Apparently, annotations cannot detect private fields. Parceler also requires an empty constructor.
     * The getter/setter methods are still kept just out of habit even though you can direct manipulate the
     * member variables without encapsulaton.
     */
    public int mMovieId;
    public String mMovieTitle;
    public String mMoviePoster;
    public String mMovieBackdrop;
    public String mPlotSynopsis;
    public Double mVoteAverage;
    public String mReleaseDate;
    public List<Trailer> mTrailerList;
    public boolean mIsPartial = true;

    public Movie(){

    }

    public Movie(int movieId, String title, String moviePoster
            , String movieBackdrop, String plotSynopsis, Double voteAverage
            , String releaseDate, boolean isPartial) {
        
        mMovieId = movieId;
        mMovieTitle = title;
        mMoviePoster = moviePoster;
        mMovieBackdrop = movieBackdrop;
        mPlotSynopsis = plotSynopsis;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
        mIsPartial = isPartial;
        
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        this.mMovieId = movieId;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.mMovieTitle = movieTitle;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.mMoviePoster = moviePoster;
    }

    public String getMovieBackdrop() {
        return mMovieBackdrop;
    }

    public void setMovieBackdrop(String movieBackdrop) {
        mMovieBackdrop = movieBackdrop;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.mPlotSynopsis = plotSynopsis;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public String toString(){

        return mMovieId + " | " + mMovieTitle + " | " + mReleaseDate;

    }

    public List<Trailer> getTrailerList() {
        return mTrailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        mTrailerList = trailerList;
    }

    public boolean isPartial() {
        return mIsPartial;
    }

    public void setPartial(boolean partial) {
        mIsPartial = partial;
    }
}
