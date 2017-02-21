package com.alvinsvitzer.flixbook.data.model;

/**
 * Created by Alvin on 2/21/17.
 */

public class NullMovie extends Movie {


    private static NullMovie INSTANCE = null;


    private NullMovie(){

        mMovieId = -1;
        mMovieTitle = "Movie Not Available";
        mMoviePoster = "Movie Not Available";
        mMovieBackdrop = "Movie Not Available";
        mPlotSynopsis = "Movie Not Available";
        mVoteAverage = 0.0;
        mReleaseDate = "Movie Not Available";

    }


    public static synchronized NullMovie getInstance(){

        if(INSTANCE == null){

            INSTANCE = new NullMovie();
        }

        return INSTANCE;

    }


}
