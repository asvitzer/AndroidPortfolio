package com.alvinsvitzer.flixbook.data.local;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alvinsvitzer.flixbook.data.model.Movie;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by alvin.svitzer on 25/03/2017.
 */

public class FavoriteDataStoreLocalImpl implements FavoriteDataStoreLocal {

    private static final String TAG = FavoriteDataStoreLocalImpl.class.getSimpleName();
    private static final int INSERT_TOKEN = 1;
    private static final int DELETE_TOKEN = 2;
    private static final int QUERY_MOVIE_CHECK_TOKEN = 3;
    private static final int QUERY_ALL_MOVIES_TOKEN = 4;

    private static FavoriteDataStoreLocalImpl INSTANCE = null;
    private AsyncQueryHandler mAsyncQueryHandler;


    private FavoriteDataStoreLocalImpl(@NonNull ContentResolver contentResolver) {

        mAsyncQueryHandler = new MyAsyncQueryHandler(contentResolver);

        Log.d(TAG, "FavoriteDataStoreLocalImpl: instatiating Favo9resDataStoreLocalImp");

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

        Log.d(TAG, "checkFavorite: startQuery checkFavorite " + movieId);

    }

    @Override
    public void addFavoriteMovie(@NonNull Movie movie) {

        String movieId = String.valueOf(movie.getMovieId());

        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI
                .buildUpon()
                .appendEncodedPath(movieId)
                .build();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE, movie.getMovieTitle());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER_LINK, movie.getMoviePoster());

        mAsyncQueryHandler.startInsert(INSERT_TOKEN, null, uri, contentValues);

        Log.d(TAG, "startInsert addFavoriteMovie " + movieId);

    }

    @Override
    public void removeFavoriteMovie(@NonNull String movieId) {

        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI
                .buildUpon()
                .appendEncodedPath(movieId)
                .build();

        mAsyncQueryHandler.startDelete(DELETE_TOKEN, null, uri, null, null);

        Log.d(TAG, "removeFavoriteMovie: startDelete " + movieId);

    }

    @Override
    public void getFavorites(@NonNull GetFavoritesCallback callback) {

        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;

        Log.d(TAG, "getFavorites URI: uri " + uri);

        mAsyncQueryHandler.startQuery(QUERY_ALL_MOVIES_TOKEN, callback, uri, null, null, null, null);

        Log.d(TAG, "getFavorites: startQuery ");

    }

    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        ContentResolver cr;

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
            this.cr = cr;
        }

        @Override
        public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
            super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);

            Log.d(TAG, "startQuery: MyAsyncQueryHandler");
            Log.d(TAG, "startQuery: content resolver null?" + (cr == null) + cr.toString());
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            super.onInsertComplete(token, cookie, uri);

            Log.d(TAG, "onInsertComplete: MyAsyncQueryHandler");
            Log.d(TAG, "startQuery: content resolver null?" + (cr == null) + cr.toString());
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
            super.onUpdateComplete(token, cookie, result);

            Log.d(TAG, "onUpdateComplete: MyAsyncQueryHandler");
            Log.d(TAG, "startQuery: content resolver null?" + (cr == null) + cr.toString());
        }

        @Override
        protected void onDeleteComplete(int token, Object cookie, int result) {
            super.onDeleteComplete(token, cookie, result);

            Log.d(TAG, "onDeleteComplete: MyAsyncQueryHandler");
            Log.d(TAG, "startQuery: content resolver null?" + (cr == null) + cr.toString());
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);

            Log.d(TAG, "onQueryComplete: Token " + token);
            Log.d(TAG, "startQuery: content resolver null?" + (cr == null) + cr.toString());

            switch (token) {
                case QUERY_MOVIE_CHECK_TOKEN:

                    CheckMovieCallback checkMovieCallback = (CheckMovieCallback) cookie;

                    //Sends a true or false back to movie stored based on whether the cursor
                    //is empty or not

                    if (cursor == null || cursor.moveToFirst() == false) {

                        checkMovieCallback.movieStored(false);

                    } else {
                        checkMovieCallback.movieStored(true);
                        cursor.close();
                    }

                    break;

                case QUERY_ALL_MOVIES_TOKEN:

                    GetFavoritesCallback getFavoritesCallback = (GetFavoritesCallback) cookie;

                    if (cursor == null || cursor.moveToFirst() == false) {
                        getFavoritesCallback.onFavoritesNotAvailable();

                        break;
                    }

                    getFavoritesCallback.onFavoritesLoaded(cursor);

                    break;

                default:
                    Log.d(TAG, "onQueryComplete: No logic for token: " + token);
            }


        }

    }

}
