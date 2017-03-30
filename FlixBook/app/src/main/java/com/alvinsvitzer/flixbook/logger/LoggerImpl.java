package com.alvinsvitzer.flixbook.logger;

import android.util.Log;

/**
 * Created by alvin.svitzer on 30/03/2017.
 */

public class LoggerImpl implements Logger {

    public static LoggerImpl INSTANCE = null;

    private LoggerImpl() {
    }

    public static LoggerImpl getINSTANCE() {

        if (INSTANCE == null) {

            INSTANCE = new LoggerImpl();
        }

        return INSTANCE;
    }

    @Override
    public void logd(String tag, String message) {
        Log.d(tag, "logd: " + message);
    }

}
