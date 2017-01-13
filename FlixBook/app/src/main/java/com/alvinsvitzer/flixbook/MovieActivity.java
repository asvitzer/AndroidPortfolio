package com.alvinsvitzer.flixbook;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.alvinsvitzer.flixbook.extensions.SingleFragmentActivity;
import com.alvinsvitzer.flixbook.model.Movie;

import org.parceler.Parcels;

public class MovieActivity extends SingleFragmentActivity
        implements MovieGridFragment.OnFragmentInteractionListener {

    private static final String TAG = MovieActivity.class.getSimpleName();
    public static final String SORT_MENU_CHECKED_PREF = "sortMenuChecked";


    @Override
    protected Fragment createFragment() {
        return MovieGridFragment.newInstance(getMovieDBApiKey());
    }

    @Override
    public void onMovieClick(Movie movie) {

        Log.i(TAG, "onMovieClick | " + "Pulling up detail for movie: " + movie.toString());

        Fragment newDetail = MovieDetailFragment.newInstance(Parcels.wrap(movie));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newDetail)
                .addToBackStack(null)
                .commit();

    }

}
