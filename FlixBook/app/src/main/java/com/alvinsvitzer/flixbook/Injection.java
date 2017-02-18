package com.alvinsvitzer.flixbook;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.data.local.MovieLocalDataStore;
import com.alvinsvitzer.flixbook.data.remote.MovieRemoteDataStore;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static AppRepository provideMovieDataStoreRepository(@NonNull Context context) {

        checkNotNull(context);

        return AppRepository.getInstance(MovieRemoteDataStore.getInstance(context)
                , MovieLocalDataStore.getInstance());
    }

    public static MovieRemoteDataStore provideRemoteDataSource(@NonNull Context context) {

        checkNotNull(context);

        return MovieRemoteDataStore.getInstance(context);
    }

    public static MovieLocalDataStore provideLocalDataSource() {

        return MovieLocalDataStore.getInstance();
    }

}