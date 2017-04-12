package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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

    @BindView(R.id.movie_plot_synopsis_textview)
    TextView mPlotSynopsis;
    @BindView(R.id.movie_release_date_textview)
    TextView mReleaseDate;
    @BindView(R.id.movie_vote_average_textview)
    TextView mVoteAverage;
    @BindView(R.id.OverviewCoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

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

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void attachPresenter() {

        if (mPresenter == null ){

            mPresenter = new MovieOverviewPresenter(this
                    , Injection.provideMovieDataStoreRepository(getActivity()));

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
                make(mCoordinatorLayout, R.string.text_no_movie_data, Snackbar.LENGTH_LONG);

        snackbar.show();

    }


    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
