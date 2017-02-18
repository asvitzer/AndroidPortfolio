package com.alvinsvitzer.flixbook.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.alvinsvitzer.flixbook.Injection;
import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.extensions.SingleFragmentActivity;
import com.alvinsvitzer.flixbook.moviedetail.DetailActivity;

public class MovieActivity extends SingleFragmentActivity
        implements MovieGridFragment.OnFragmentInteractionListener {

    private static final String TAG = MovieActivity.class.getSimpleName();
    public static final String SORT_MENU_CHECKED_PREF = "sortMenuChecked";
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

            mPresenter = new MoviesPresenter(Injection.provideMovieDataStoreRepository(this)
                                            , (MoviesContract.View) getCurrentFragment());
        } else {

            mPresenter.attachView((MoviesContract.View) getCurrentFragment());
        }

    }

    @Override
    public void onMovieClick() {

        Intent detailIntent = new Intent(this, DetailActivity.class);
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
