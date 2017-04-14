package com.alvinsvitzer.flixbook.moviedetail;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alvinsvitzer.flixbook.Injection;
import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.extensions.AppBarStateChangeListener;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements MovieDetailsContract.View {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.sliding_tabs)
    TabLayout mTabLayout;

    @BindView(R.id.movie_backdrop_image)
    NetworkImageView mBackdropImage;

    @BindView(R.id.movie_poster_image)
    NetworkImageView mPosterImage;

    @BindView(R.id.banner_text_view)
    TextView mBannerText;

    @BindView(R.id.playTrailerFab)
    FloatingActionButton mPlayMovieFab;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.MovieDetailCoordLayout)
    CoordinatorLayout mCoordinatorLayout;

    private MovieDetailsContract.Presenter mPresenter;
    private Uri mTrailerUri;
    private String mMovieTitle;
    private boolean mIsFavoriteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachPresenter();

        setupListener();

        setupAdapter();

        setupAppBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void attachPresenter(){

        mPresenter = (MovieDetailsContract.Presenter) getLastCustomNonConfigurationInstance();

        if (mPresenter == null) {

            VolleyNetworkSingleton volleyNetworkSingleton = VolleyNetworkSingleton.getInstance(this);
            ImageLoader imageLoader = volleyNetworkSingleton.getImageLoader();

            AppRepository appRepository = Injection.provideMovieDataStoreRepository(this);

            mPresenter = new MovieDetailPresenter(appRepository, imageLoader, this, Injection.provideLogger());

        }else{

            mPresenter.attachView(this);

        }

    }

    private void setupListener() {

        mPlayMovieFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(mTrailerUri);

                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }

            }
        });
    }

    private void setupAdapter() {
        DetailsPagerAdapter pagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager()
                , this);

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupAppBar() {

        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {

                    mCollapsingToolbarLayout.setTitle(" ");

                    if (mCurrentState != State.EXPANDED) {
                        onStateChanged(State.EXPANDED);
                    }
                    mCurrentState = State.EXPANDED;
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {

                    mCollapsingToolbarLayout.setTitle(mMovieTitle);

                    if (mCurrentState != State.COLLAPSED) {
                        onStateChanged(State.COLLAPSED);
                    }
                    mCurrentState = State.COLLAPSED;

                } else {

                    mCollapsingToolbarLayout.setTitle(" ");

                    if (mCurrentState != State.IDLE) {
                        onStateChanged(State.IDLE);
                    }
                    mCurrentState = State.IDLE;
                }

            }

            @Override
            public void onStateChanged(State state) {
                switch (state) {
                    case COLLAPSED:
                        mPlayMovieFab.hide();
                        break;
                    case EXPANDED:
                        mPlayMovieFab.show();
                        break;
                    case IDLE:
                        mPlayMovieFab.show();
                        break;
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favorite_movie_button:
                mPresenter.favoriteFabClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPosterImage(@NonNull String imageUrl, @NonNull ImageLoader imageLoader) {

        mPosterImage.setImageUrl(imageUrl, imageLoader);

    }

    @Override
    public void setBackdropImage(@NonNull String imageUrl, @NonNull ImageLoader imageLoader) {

        mBackdropImage.setImageUrl(imageUrl, imageLoader);
    }

    @Override
    public void setBannerText(String text) {

        mBannerText.setText(text);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {

        return mPresenter;
    }

    @Override
    public void disableTrailerFab(){

        mPlayMovieFab.setVisibility(View.INVISIBLE);

    }

    @Override
    public void enableTrailerFab(){

        mPlayMovieFab.setVisibility(View.VISIBLE);
        mPlayMovieFab.setEnabled(true);

    }

    @Override
    public void setTrailerUri(Uri uri) {
        mTrailerUri = uri;
    }


    @Override
    public void setActivityTitle(String title) {
        mMovieTitle = title;
    }

    @Override
    public void notifyUserNoTrailer() {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.MovieDetailCoordLayout);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.text_no_trailer, Snackbar.LENGTH_LONG);

        snackbar.show();

        mPlayMovieFab.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {

        mPresenter.detachView();
        super.onDestroy();

    }

    @Override
    public void notifyUserNoMovie() {

        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, R.string.text_no_movie_data, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void setFavoriteFabImage(boolean isFavorite) {

        mIsFavoriteMovie = isFavorite;
        invalidateOptionsMenu();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem favoriteMenu = menu.findItem(R.id.favorite_movie_button);

        Log.d("DAAAAAA", "onPrepareOptionsMenu: menu " + (menu == null) + " " + menu);

        if (mIsFavoriteMovie) {

            favoriteMenu.setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));

        } else {

            favoriteMenu.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));

        }

        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void displayFavorite() {

        Snackbar.make(mCoordinatorLayout, R.string.snackbar_movie_favorite, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void displayFavoriteRemoval() {

        Snackbar.make(mCoordinatorLayout, R.string.snackbar_movie_unfavorited, Snackbar.LENGTH_LONG)
                .show();

    }

}
