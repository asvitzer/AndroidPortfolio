package com.alvinsvitzer.flixbook.data.local;


import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.alvinsvitzer.flixbook";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Path for accessing data
    public static final String PATH_FAVORITE = "favorites";

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";

        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";

        public static final String COLUMN_MOVIE_POSTER_LINK = "movie_poster_link";


        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows

        Note: Because this implements BaseColumns, the _id column is generated automatically

        favorites
         - - - - - - - - - - - - - - - - - - - - -
        | _id  |    movieID     |    movieTitle   |
         - - - - - - - - - - - - - - - - - - - - -
        |  1   |  38484757748   |    Spanglish    |
         - - - - - - - - - - - - - - - - - - - - -
        |  2   |  87484757473   |    Moonlight    |
         - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - -
        |  9   |  56484757473   |    Rise         |
         - - - - - - - - - - - - - - - - - - - - -

         */

    }
}
