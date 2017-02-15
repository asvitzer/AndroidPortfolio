package com.alvinsvitzer.flixbook.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.alvinsvitzer.flixbook.data.remote.MovieRemoteDataStore;
import com.alvinsvitzer.flixbook.extensions.SingleFragmentActivity;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.moviedetail.DetailActivity;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;

import org.parceler.Parcels;

public class MovieActivity extends SingleFragmentActivity
        implements MovieGridFragment.OnFragmentInteractionListener {

    private static final String TAG = MovieActivity.class.getSimpleName();
    public static final String SORT_MENU_CHECKED_PREF = "sortMenuChecked";
    public static final String INTENT_EXTRA_API_KEY = "api_key";
    public static final String INTENT_EXTRA_MOVIE = "movie";


    @Override
    protected Fragment createFragment() {
        return new MovieGridFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attachPresenter();

    }

    private void attachPresenter() {

        VolleyNetworkSingleton volleyNetworkSingleton = VolleyNetworkSingleton.getInstance(this);

        new MoviesPresenter(
                MovieRemoteDataStore.getInstance(volleyNetworkSingleton)
                , (MoviesContract.View) getCurrentFragment()
                , getMovieDBApiKey());
    }

    @Override
    public void onMovieClick(Movie movie) {

       Log.i(TAG, "onMovieClick | " + "Pulling up detail for movie: " + movie.toString());

        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(INTENT_EXTRA_API_KEY ,getMovieDBApiKey());
        detailIntent.putExtra(INTENT_EXTRA_MOVIE ,Parcels.wrap(movie));
        startActivity(detailIntent);

    }

}
