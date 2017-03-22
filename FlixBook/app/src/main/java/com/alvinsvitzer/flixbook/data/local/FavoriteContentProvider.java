package com.alvinsvitzer.flixbook.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.alvinsvitzer.flixbook.data.local.FavoriteContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by alvin.svitzer on 22/03/2017.
 */

public class FavoriteContentProvider extends ContentProvider {

    private FavoriteDbHelper mFavoriteDbHelper;

    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_MOVIE_ID = 101;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteDbHelper = new FavoriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavoriteDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {

            // Query for the tasks directory
            case FAVORITES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_WITH_MOVIE_ID:

                String id = uri.getPathSegments().get(1);

                retCursor = db.query(TABLE_NAME,
                        projection,
                        FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " =  ? ",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;

            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @NonNull ContentValues values) {

        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVORITES:

                // Insert new values into the database

                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {

                    int movieId = values.getAsInteger(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID);
                    returnUri = ContentUris.withAppendedId(FavoriteContract.FavoriteEntry.CONTENT_URI, movieId);

                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int favoritesDeleted;

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVORITE_WITH_MOVIE_ID:

                // Get the movie id from the URI
                String id = uri.getPathSegments().get(1);

                // Use selections/selectionArgs to filter for this ID
                favoritesDeleted = db.delete(TABLE_NAME,
                        FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ? "
                        , new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of movies deleted
        if (favoritesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of favorite movies deleted
        return favoritesDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the movie directory and a single movie by movieID.
         */
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITE, FAVORITES);
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITE + "/#", FAVORITE_WITH_MOVIE_ID);

        return uriMatcher;
    }
}
