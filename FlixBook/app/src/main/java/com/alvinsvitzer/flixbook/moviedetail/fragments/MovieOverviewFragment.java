package com.alvinsvitzer.flixbook.moviedetail.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.model.Movie;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    private MovieOverviewContract.Presenter mPresenter;

    private Movie mMovie;

    public static final String PARAM_MOVIE_DETAIL = "movieDetail";

    public static MovieOverviewFragment newInstance(Parcelable movie){

        MovieOverviewFragment movieOverviewFragment = new MovieOverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_MOVIE_DETAIL, movie);
        movieOverviewFragment.setArguments(args);

        return movieOverviewFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mMovie = Parcels.unwrap(getArguments().getParcelable(PARAM_MOVIE_DETAIL));
        }

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

        mPresenter = new MovieOverviewPresenter(this, mMovie);
        mPresenter.start();

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
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
