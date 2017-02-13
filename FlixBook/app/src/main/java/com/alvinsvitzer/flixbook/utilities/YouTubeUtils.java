package com.alvinsvitzer.flixbook.utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import static com.alvinsvitzer.flixbook.utilities.MovieDBUtils.LANGUAGE_ENGLISH_US;
import static com.alvinsvitzer.flixbook.utilities.MovieDBUtils.PARAM_LANGUAGE;

/**
 * Created by Alvin on 2/13/17.
 */

public class YouTubeUtils {


    public static final String TAG = YouTubeUtils.class.getSimpleName();

    final static String YOUTUBE_BASE_URL = "https://www.youtube.com";

    final static String PATH_WATCH = "watch";
    final static String PATH_VIDEO_ID = "v";

    /**
     * Builds URL to get video link for youtube
     */
    public static URL buildYouTubeUrl(String videoId){

        Uri finalUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(PATH_WATCH)
                .appendQueryParameter(PATH_VIDEO_ID, videoId)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_ENGLISH_US)
                .build();

        Log.i(TAG, "buildYouTubeUrl: " + finalUri.toString());

        URL url = null;

        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


}
