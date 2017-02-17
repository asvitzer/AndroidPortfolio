package com.alvinsvitzer.flixbook.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.alvinsvitzer.flixbook.R;
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
    private MoviesContract.Presenter mPresenter;


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
        attachPresenter();

    }

    private void attachPresenter() {

        mPresenter = (MoviesContract.Presenter) getLastCustomNonConfigurationInstance();

        if (mPresenter == null) {

            VolleyNetworkSingleton volleyNetworkSingleton = VolleyNetworkSingleton.getInstance(this);

            mPresenter = new MoviesPresenter(MovieRemoteDataStore.getInstance(volleyNetworkSingleton, getMovieDBApiKey())
                                            , (MoviesContract.View) getCurrentFragment());
        } else {

            mPresenter.attachView((MoviesContract.View) getCurrentFragment());
        }

    }

    @Override
    public void onMovieClick(Movie movie) {

       Log.i(TAG, "onMovieClick | " + "Pulling up detail for movie: " + movie.toString());

        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(INTENT_EXTRA_API_KEY ,getMovieDBApiKey());
        detailIntent.putExtra(INTENT_EXTRA_MOVIE ,Parcels.wrap(movie));
        startActivity(detailIntent);

    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mPresenter;
    }

}
