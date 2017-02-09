package com.alvinsvitzer.flixbook;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private Movie mMovie;

    @BindView(R.id.movie_backdrop_image)
    NetworkImageView mBackdropImage;

    @BindView(R.id.movie_poster_image)
    NetworkImageView mPosterImage;

    @BindView(R.id.banner_text_view)
    TextView mBannerText;

    @BindView(R.id.movie_plot_synopsis_textview)
    TextView mPlotSynopsis;

    @BindView(R.id.movie_release_date_textview)
    TextView mReleaseDate;

    @BindView(R.id.movie_vote_average_textview)
    TextView mVoteAverage;

    private ImageLoader mImageLoader;


    public static final String MOVIE_DETAIL = "movieDetail";

    public static MovieDetailFragment newInstance(Parcelable movie){

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_DETAIL, movie);
        movieDetailFragment.setArguments(args);

        return movieDetailFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mMovie = Parcels.unwrap(getArguments().getParcelable(MOVIE_DETAIL));
        } else {
            Toast.makeText(getActivity(), R.string.movie_data_error_text, Toast.LENGTH_SHORT).show();
        }

        VolleyNetworkSingleton volleyNetworkSingleton = VolleyNetworkSingleton.getInstance(getActivity());
        mImageLoader = volleyNetworkSingleton.getImageLoader();

        getActivity().setTitle(getString(R.string.movie_detail_fragment_title));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.detail, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
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

        mBannerText.setText(mMovie.getMovieTitle());
        mPlotSynopsis.setText(mMovie.getPlotSynopsis());
        mReleaseDate.setText(mMovie.getReleaseDate());

        int movieRatingId = R.string.movie_info_vote_average_rating;

        String movieRating = String.format(getString(movieRatingId), mMovie.getVoteAverage());
        mVoteAverage.setText(movieRating);

        String posterImageUrl = MovieDBUtils.buildMoviePosterURL(mMovie.getMoviePoster()).toString();
        mPosterImage.setImageUrl(posterImageUrl,mImageLoader);

        String backdropImageUrl = MovieDBUtils.buildMovieBackdropURL(mMovie.getMovieBackdrop()).toString();
        mBackdropImage.setImageUrl(backdropImageUrl,mImageLoader);

    }




}
