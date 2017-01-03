package com.alvinsvitzer.flixbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.Menu;

public class MovieDetailActivity extends SingleFragmentActivity {

    public static final String MOVIE_DETAIL = "movieDetail";
    private Parcelable mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

    }

    @Override
    protected Fragment createFragment() {

        Intent intent = getIntent();

        if (intent.getParcelableExtra(MOVIE_DETAIL) != null){
            mMovie = intent.getParcelableExtra(MOVIE_DETAIL);
        }

       return MovieDetailFragment.newInstance(mMovie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }



}
