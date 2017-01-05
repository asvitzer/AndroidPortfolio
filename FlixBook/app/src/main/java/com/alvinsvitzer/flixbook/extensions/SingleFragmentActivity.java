package com.alvinsvitzer.flixbook.extensions;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alvinsvitzer.flixbook.MovieGridActivity;
import com.alvinsvitzer.flixbook.R;

/**
 * Created by Alvin on 1/1/16.
 * Abstract Activity Class used to load a fragment onto a layout with a single fragment
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private String mMovieDBApiKey;
    private static final String TAG = MovieGridActivity.class.getSimpleName();

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        /** Set mMovieDBApiKey below to your API key for The MovieDB**/
        setMovieDBApiKey("INSERT YOUR KEY HERE");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){

            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    public String getMovieDBApiKey(){

        return mMovieDBApiKey;
    }

    public void setMovieDBApiKey(String key){

        if (key != null && !key.equals("")){

            mMovieDBApiKey = key;

        }else {

            Log.e(TAG, "setMovieDBApiKey: key cannot be null or empty", new IllegalArgumentException());
        }

    }

    protected abstract Fragment createFragment();

}
