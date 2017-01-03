package com.alvinsvitzer.flixbook;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;

import org.parceler.Parcels;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private static final String MOVIE_DETAIL = "movieDetail";
    private Movie mMovie;

    public static MovieDetailFragment newInstance(Movie movie){

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_DETAIL, Parcels.wrap(movie));
        movieDetailFragment.setArguments(args);

        return movieDetailFragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMovie = Parcels.unwrap(getArguments().getParcelable(MOVIE_DETAIL));
        } else {
            Toast.makeText(getActivity(), R.string.movie_data_error_text, Toast.LENGTH_SHORT).show();
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    /**
     * Removes the sort menu item from the fragment since the menu logic is in the hosting activity
     * that also hosts the MovieGridFragment. Another approach, if the menu becomes more complicated,
     * is to remove the menu logic from the hosting activity and keep it in each respective fragment.
     * @param menu
     */

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.sort_menu_item);
        item.setVisible(false);
    }

}