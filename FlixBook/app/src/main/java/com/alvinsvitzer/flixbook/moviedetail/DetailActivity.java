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
import android.view.View;
import android.widget.TextView;

import com.alvinsvitzer.flixbook.Injection;
import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.extensions.AppBarStateChangeListener;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

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
    // Used to handle unsubscription during teardown of Fragment
    CompositeSubscription subscriptions = new CompositeSubscription();
    private MovieDetailsContract.Presenter mPresenter;
    private Uri mTrailerUri;
    private String mMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachPresenter();

        setupSubscription();

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

            mPresenter = new MovieDetailPresenter(appRepository, imageLoader, this);

        }else{

            mPresenter.attachView(this);

        }

    }

    private void setupSubscription() {

        subscriptions.add(RxView.clicks(mPlayMovieFab).map(new Func1<Void, Intent>() {

            @Override
            public Intent call(Void aVoid) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(mTrailerUri);

                return intent;

            }

        }).subscribe(new Action1<Intent>() {
            @Override
            public void call(Intent intent) {
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }

            }
        }));
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
        //setTitle(title);
        mMovieTitle = title;
    }

    @Override
    public void notifyUserNoTrailer() {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.MovieDetailCoordLayout);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.text_no_trailer, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    protected void onDestroy() {

        mPresenter.detachView();
        subscriptions.unsubscribe();
        super.onDestroy();

    }

    @Override
    public void notifyUserNoMovie() {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.MovieDetailCoordLayout);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.text_no_movie_data, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

}
