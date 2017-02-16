package com.alvinsvitzer.flixbook.moviedetail;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieOverviewFragment extends Fragment {

    @BindView(R.id.movie_plot_synopsis_textview)
    TextView mPlotSynopsis;

    @BindView(R.id.movie_release_date_textview)
    TextView mReleaseDate;

    @BindView(R.id.movie_vote_average_textview)
    TextView mVoteAverage;

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
        } else {
            Toast.makeText(getActivity(), R.string.movie_data_error_text, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, v);

        attachMovieInformation();

        // Inflate the layout for this fragment
        return v;
    }

    private void attachMovieInformation() {

        mPlotSynopsis.setText(mMovie.getPlotSynopsis());
        mReleaseDate.setText(MovieDBUtils.getLocalDate(mMovie.getReleaseDate()));

        int movieRatingId = R.string.movie_info_vote_average_rating;

        String movieRating = String.format(getString(movieRatingId), mMovie.getVoteAverage());
        mVoteAverage.setText(movieRating);


    }

}
