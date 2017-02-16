package com.alvinsvitzer.flixbook.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.data.remote.MovieRemoteDataStore;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jakewharton.rxbinding.view.RxView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static com.alvinsvitzer.flixbook.movies.MovieActivity.INTENT_EXTRA_API_KEY;
import static com.alvinsvitzer.flixbook.movies.MovieActivity.INTENT_EXTRA_MOVIE;

public class DetailActivity extends AppCompatActivity implements MovieDetailsContract.View {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.movie_backdrop_image)
    NetworkImageView mBackdropImage;

    @BindView(R.id.movie_poster_image)
    NetworkImageView mPosterImage;

    @BindView(R.id.banner_text_view)
    TextView mBannerText;

    @BindView(R.id.playTrailerFab)
    FloatingActionButton mPlayMovieFab;

    private MovieDetailsContract.Presenter mPresenter;

    private Uri mTrailerUri;
    private String mApiKey;
    private Movie mMovie;

    // Used to handle unsubscription during teardown of Fragment
    CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setTitle(getString(R.string.movie_detail_fragment_title));

        Intent receivingIntent = getIntent();

        if (receivingIntent != null) {

            mApiKey = receivingIntent.getStringExtra(INTENT_EXTRA_API_KEY);
            mMovie = Parcels.unwrap(receivingIntent.getParcelableExtra(INTENT_EXTRA_MOVIE));
        }

        attachPresenter();

        setupSubscription();

        PagerAdapter pagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager()
                                                            , this
                                                            , mApiKey
                                                            , Parcels.wrap(mMovie));

        viewPager.setAdapter(pagerAdapter);
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
            MovieRemoteDataStore movieRemoteDataStore = MovieRemoteDataStore.getInstance(volleyNetworkSingleton, mApiKey);
            ImageLoader imageLoader = volleyNetworkSingleton.getImageLoader();

            mPresenter = new MovieDetailPresenter(mMovie, movieRemoteDataStore, imageLoader, this);

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
    public void notifyUserNoTrailer() {

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.MovieDetailLinearLayout);

        Snackbar snackbar = Snackbar
                .make(linearLayout, R.string.toast_no_trailer, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    protected void onDestroy() {

        mPresenter.detachView();
        subscriptions.unsubscribe();
        super.onDestroy();

    }

}
