package com.alvinsvitzer.flixbook;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import static com.alvinsvitzer.flixbook.MovieDetailActivity.MOVIE_DETAIL;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private Movie mMovie;
    private NetworkImageView mBackdropImage;
    private NetworkImageView mPosterImage;
    private TextView mBannerText;
    private TextView mPlotSynopsis;
    private ImageLoader mImageLoader;
    private VolleyNetworkSingleton mVolleyNetworkSingleton;

    public static MovieDetailFragment newInstance(Parcelable movie){

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MovieDetailActivity.MOVIE_DETAIL, movie);
        movieDetailFragment.setArguments(args);

        return movieDetailFragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMovie = Parcels.unwrap(getArguments().getParcelable(MOVIE_DETAIL));
        } else {
            Toast.makeText(getActivity(), R.string.movie_data_error_text, Toast.LENGTH_SHORT).show();
        }

        mVolleyNetworkSingleton = VolleyNetworkSingleton.getInstance(getActivity());
        mImageLoader = mVolleyNetworkSingleton.getImageLoader();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mBackdropImage = (NetworkImageView) view.findViewById(R.id.movie_backdrop_image);
        mPosterImage = (NetworkImageView) view.findViewById(R.id.movie_poster_image);
        mBannerText = (TextView) view.findViewById(R.id.banner_text_view);
        mPlotSynopsis = (TextView) view.findViewById(R.id.movie_plot_synopsis_textview);

        attachMovieInformation();

        // Inflate the layout for this fragment
        return view;
    }

    private void attachMovieInformation() {

        mBannerText.setText(mMovie.getMovieTitle());
        mPlotSynopsis.setText(mMovie.getPlotSynopsis());

        String posterImageUrl = MovieDBUtils.buildMoviePosterURL(mMovie.getMoviePoster()).toString();
        mPosterImage.setImageUrl(posterImageUrl,mImageLoader);

        String backdropImageUrl = MovieDBUtils.buildMovieBackdropURL(mMovie.getMovieBackdrop()).toString();
        mBackdropImage.setImageUrl(backdropImageUrl,mImageLoader);

    }


}
