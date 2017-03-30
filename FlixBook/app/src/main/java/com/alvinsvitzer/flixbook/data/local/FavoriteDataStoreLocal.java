package com.alvinsvitzer.flixbook.data.local;

import android.support.annotation.NonNull;

/**
 * Created by alvin.svitzer on 24/03/2017.
 */

public interface FavoriteDataStoreLocal {

    void checkFavorite(@NonNull String movieId, @NonNull CheckMovieCallback callback);

    void addFavoriteMovie(@NonNull String movieId);

    void removeFavoriteMovie(@NonNull String movieId);

    interface CheckMovieCallback {

        void movieStored(boolean movieStored);
    }

}
