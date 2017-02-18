package com.alvinsvitzer.flixbook.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.extensions.SingleFragmentActivity;
import com.alvinsvitzer.flixbook.moviedetail.DetailActivity;

public class MovieActivity extends SingleFragmentActivity
        implements MovieGridFragment.OnFragmentInteractionListener {

    @Override
    protected Fragment createFragment() {
        return new MovieGridFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setTitle(getString(R.string.movie_grid_fragment_title));

    }

    @Override
    public void onMovieClick() {

        Intent detailIntent = new Intent(this, DetailActivity.class);
        startActivity(detailIntent);

    }

}
