package com.alvinsvitzer.flixbook.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alvin.svitzer on 22/03/2017.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String TAG = FavoriteDbHelper.class.getSimpleName();

    // The name of the database
    private static final String DATABASE_NAME = "movieDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER AUTOINCREMENT, " +
                FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                " UNIQUE (" + FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        Log.d(TAG, "onCreate: createQuery: " + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
