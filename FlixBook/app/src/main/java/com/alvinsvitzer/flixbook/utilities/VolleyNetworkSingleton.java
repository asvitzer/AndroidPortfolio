package com.alvinsvitzer.flixbook.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alvin on 12/29/16.
 *
 * Used to create one instance of Volley's RequestQueue that will last the lifetime of the app. This
 * is especially useful for the image cache so that it will be used for the entire application as well
 * as not having to recreate this every time a network request needs to be made.
 */

public class VolleyNetworkSingleton {

    private static VolleyNetworkSingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleyNetworkSingleton(Context context) {

        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {

                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {

                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleyNetworkSingleton getInstance(Context context) {
        if (mInstance == null) {

            mInstance = new VolleyNetworkSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(mCtx);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
