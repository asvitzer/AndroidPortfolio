package com.alvinsvitzer.flixbook.data.local;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by alvin.svitzer on 25/03/2017.
 */

public class FavoriteDataStoreLocalImpl implements FavoriteDataStoreLocal {

    private static final String TAG = FavoriteDataStoreLocal.class.getSimpleName();
    private static final int INSERT_TOKEN = 1;
    private static final int DELETE_TOKEN = 2;
    private static final int QUERY_MOVIE_CHECK_TOKEN = 3;
    private static final int QUERY_ALL_MOVIES_TOKEN = 4;
    private static FavoriteDataStoreLocalImpl INSTANCE = null;
    private AsyncQueryHandler mAsyncQueryHandler;


    private FavoriteDataStoreLocalImpl(@NonNull ContentResolver contentResolver) {

        mAsyncQueryHandler = new AsyncQueryHandler(contentResolver) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);

                switch (token) {
                    case QUERY_MOVIE_CHECK_TOKEN:

                        CheckMovieCallback callback = (CheckMovieCallback) cookie;

                        //Sends a true or false back to movie stored based on whether the cursor
                        //is empty or not
                        callback.movieStored(cursor.moveToFirst());

                        break;
                    default:
                        Log.d(TAG, "onQueryComplete: No logic for token: " + token);
                }
            }
        };

    }

    public static FavoriteDataStoreLocalImpl getInstance(@NonNull ContentResolver contentResolver) {

        checkNotNull(contentResolver, "contentProvider cannot be null");

        if (INSTANCE == null) {
            INSTANCE = new FavoriteDataStoreLocalImpl(contentResolver);
        }

        return INSTANCE;
    }

    @Override
    public void checkFavorite(@NonNull String movieId, @NonNull CheckMovieCallback callback) {

        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI
                .buildUpon()
                .appendEncodedPath(movieId)
                .build();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);

        mAsyncQueryHandler.startQuery(QUERY_MOVIE_CHECK_TOKEN, callback, uri, null, null, null, null);

    }

    @Override
    public void addFavoriteMovie(@NonNull String movieId) {

        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI
                .buildUpon()
                .appendEncodedPath(movieId)
                .build();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);

        mAsyncQueryHandler.startInsert(INSERT_TOKEN, null, uri, contentValues);

    }

    @Override
    public void removeFavoriteMovie(@NonNull String movieId) {

        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI
                .buildUpon()
                .appendEncodedPath(movieId)
                .build();

        mAsyncQueryHandler.startDelete(DELETE_TOKEN, null, uri, null, null);

    }
}
