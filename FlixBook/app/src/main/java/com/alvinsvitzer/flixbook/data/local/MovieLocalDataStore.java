package com.alvinsvitzer.flixbook.data.local;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.data.model.Trailer;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;

import java.util.List;

/**
 * Created by Alvin on 2/17/17.
 */

public class MovieLocalDataStore implements MovieDataStore {

    private static MovieLocalDataStore INSTANCE;

    private List<Trailer> mTrailerList;
    private List<Movie> mMovieList;
    private Movie mCurrentMovie;

    private MovieLocalDataStore(){

    }

    public static synchronized MovieLocalDataStore getInstance(){

        if(INSTANCE == null){

           INSTANCE = new MovieLocalDataStore();
        }

        return INSTANCE;

    }

    @Override
    public void getMovies(@NonNull GetMoviesCallback callback, @NonNull MoviesFilterType moviesFilterType) {


        if (mMovieList != null){

            callback.onMoviesLoaded(mMovieList);

        } else {

            callback.onMovieListDataNotAvailable();

        }

    }

    @Override
    public void getMovie(@NonNull GetMovieCallback callback) {

        if (mCurrentMovie != null){

            callback.onMovieLoaded(mCurrentMovie);

        } else {

            callback.onMovieDataNotAvailable();

        }

    }

    @Override
    public void getTrailers(@NonNull String movieId, @NonNull GetTrailersCallback callback) {

        if (mTrailerList != null){

            callback.onTrailersLoaded(mTrailerList);

        } else {

            callback.onTrailerDataNotAvailable();

        }

    }


    @Override
    public void saveMovie(Movie movie){

        mCurrentMovie = movie;
    }

    @Override
    public void saveMovies(List<Movie> movieList){

        mMovieList = movieList;
    }
}
