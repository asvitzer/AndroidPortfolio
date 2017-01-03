package com.alvinsvitzer.flixbook;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;

import org.parceler.Parcels;

import static com.alvinsvitzer.flixbook.MovieDetailActivity.MOVIE_DETAIL;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private Movie mMovie;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

}
