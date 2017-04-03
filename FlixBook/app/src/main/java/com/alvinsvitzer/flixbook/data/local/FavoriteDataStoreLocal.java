package com.alvinsvitzer.flixbook.data.local;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.model.Movie;

/**
 * Created by alvin.svitzer on 24/03/2017.
 */

public interface FavoriteDataStoreLocal {

    void checkFavorite(@NonNull String movieId, @NonNull CheckMovieCallback callback);

    void addFavoriteMovie(@NonNull Movie movie);

    void removeFavoriteMovie(@NonNull String movieId);

    void getFavorites(@NonNull GetFavoritesCallback callback);

    interface GetFavoritesCallback {

        void onFavoritesLoaded(Cursor favorites);

        void onFavoritesNotAvailable();
    }

    interface CheckMovieCallback {

        void movieStored(boolean movieStored);
    }

}
