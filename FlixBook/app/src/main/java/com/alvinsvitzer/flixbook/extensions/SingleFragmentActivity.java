package com.alvinsvitzer.flixbook.extensions;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.movies.MovieActivity;

/**
 * Created by Alvin on 1/1/16.
 * Abstract Activity Class used to load a fragment onto a layout with a single fragment
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private String mMovieDBApiKey;
    private static final String TAG = MovieActivity.class.getSimpleName();
    private Fragment mFragment;

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        /** Set mMovieDBApiKey below to your API key for The MovieDB**/
        setMovieDBApiKey(getString(R.string.the_movie_db_auth_key));

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);

        if (mFragment == null){

            mFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }

    }

    public String getMovieDBApiKey(){

        return mMovieDBApiKey;
    }

    public Fragment getCurrentFragment(){

        return mFragment;
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
