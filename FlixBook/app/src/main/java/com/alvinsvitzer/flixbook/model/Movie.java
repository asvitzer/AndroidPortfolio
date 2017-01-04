package com.alvinsvitzer.flixbook.model;

import org.parceler.Parcel;

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
    public String mMovieTitle;
    public String mMoviePoster;
    public String mMovieBackdrop;
    public String mPlotSynopsis;
    public Double mVoteAverage;
    public String mReleaseDate;
    public int mMovieId;

    public Movie(){

    }

    public Movie (int movieId, String title, String moviePoster, String movieBackdrop, String plotSynopsis, Double voteAverage, String releaseDate){
        
        mMovieId = movieId;
        mMovieTitle = title;
        mMoviePoster = moviePoster;
        mMovieBackdrop = movieBackdrop;
        mPlotSynopsis = plotSynopsis;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
        
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


}
