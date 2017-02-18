package com.alvinsvitzer.flixbook.data;

import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.local.MovieLocalDataStore;
import com.alvinsvitzer.flixbook.data.remote.MovieRemoteDataStore;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.movies.MoviesFilterType;

import java.util.List;

/**
 * Created by Alvin on 2/17/17.
 */

public class AppRepository implements MovieDataStore {

    private static AppRepository INSTANCE = null;

    private final MovieRemoteDataStore mMovieRemoteDataStore;
    private final MovieLocalDataStore mMovieLocalDataStore;

    private AppRepository(MovieRemoteDataStore movieRemoteDataStore, MovieLocalDataStore movieLocalDataStore){

        mMovieRemoteDataStore = movieRemoteDataStore;
        mMovieLocalDataStore = movieLocalDataStore;
    }

    public static synchronized AppRepository getInstance(MovieRemoteDataStore movieRemoteDataStore,
                                                         MovieLocalDataStore movieLocalDataStore) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(movieRemoteDataStore, movieLocalDataStore);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(@NonNull GetMoviesCallback callback, @NonNull MoviesFilterType moviesFilterType) {

        mMovieRemoteDataStore.getMovies(callback, moviesFilterType);

    }

    @Override
    public void getMovie(@NonNull GetMovieCallback callback) {

        mMovieLocalDataStore.getMovie(callback);
    }

    @Override
    public void getTrailers(@NonNull String movieId, @NonNull GetTrailersCallback callback) {

        mMovieRemoteDataStore.getTrailers(movieId, callback);

    }

    @Override
    public void saveMovie(@NonNull Movie movie) {

        mMovieLocalDataStore.saveMovie(movie);

    }

    @Override
    public void saveMovies(@NonNull List<Movie> movieList) {

        mMovieLocalDataStore.saveMovies(movieList);

    }
}
