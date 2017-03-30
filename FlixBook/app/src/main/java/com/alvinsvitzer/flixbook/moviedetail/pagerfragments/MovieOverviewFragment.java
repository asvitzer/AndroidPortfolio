package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvinsvitzer.flixbook.Injection;
import com.alvinsvitzer.flixbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.design.widget.Snackbar.make;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieOverviewFragment extends Fragment implements MovieOverviewContract.View {

    private static final String TAG = MovieOverviewFragment.class.getSimpleName();

    @BindView(R.id.movie_plot_synopsis_textview)
    TextView mPlotSynopsis;
    @BindView(R.id.movie_release_date_textview)
    TextView mReleaseDate;
    @BindView(R.id.movie_vote_average_textview)
    TextView mVoteAverage;
    @BindView(R.id.favoriteMovieFab)
    FloatingActionButton mFavoriteMovie;

    ConstraintLayout mConstraintLayout;

    private MovieOverviewContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, v);

        attachPresenter();

        mConstraintLayout = (ConstraintLayout) getActivity().findViewById(R.id.OverviewConstraintLayout);

        mFavoriteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.favoriteFabClicked();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void attachPresenter() {

        if (mPresenter == null ){

            mPresenter = new MovieOverviewPresenter(this
                    , Injection.provideMovieDataStoreRepository(getActivity())
                    , Injection.provideLogger());

            mPresenter.start();
        }

    }

    @Override
    public void setPlot(String plot) {

        mPlotSynopsis.setText(plot);

    }

    @Override
    public void setReleaseDate(String releaseDate) {

        mReleaseDate.setText(releaseDate);
    }

    @Override
    public void setVoteAverage(Double voteAverage) {

        int movieRatingFormatId = R.string.movie_info_vote_average_rating;
        String movieRating = String.format(getString(movieRatingFormatId), voteAverage);

        mVoteAverage.setText(movieRating);

    }

    @Override
    public void notifyNoMovieData() {

        Snackbar snackbar =
                make(mConstraintLayout, R.string.text_no_movie_data, Snackbar.LENGTH_LONG);

        snackbar.show();

    }

    @Override
    public void setFavoriteFabImage(boolean isFavorite) {

        if (isFavorite) {

            mFavoriteMovie.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));

        } else {

            mFavoriteMovie.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));

        }

    }

    @Override
    public void setFavoriteFabEnabled(boolean isEnabled) {
        mFavoriteMovie.setEnabled(isEnabled);
    }

    @Override
    public void displayFavorite() {

        Snackbar.make(mConstraintLayout, R.string.snackbar_movie_favorite, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayFavoriteRemoval() {

        Snackbar.make(mConstraintLayout, R.string.snackbar_movie_unfavorited, Snackbar.LENGTH_LONG).show();

    }


}
