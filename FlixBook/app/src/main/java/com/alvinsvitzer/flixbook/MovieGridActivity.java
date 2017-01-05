package com.alvinsvitzer.flixbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alvinsvitzer.flixbook.extensions.SingleFragmentActivity;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;

import org.parceler.Parcels;

public class MovieGridActivity extends SingleFragmentActivity
        implements MovieGridFragment.OnFragmentInteractionListener {

    private static final String TAG = MovieGridActivity.class.getSimpleName();
    public static final String SORT_MENU_CHECKED_PREF = "sortMenuChecked";
    private int sortMenuIdChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore preferences for which sorting option was used
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        sortMenuIdChecked = settings.getInt(SORT_MENU_CHECKED_PREF, R.id.action_sort_most_popular);

    }

    @Override
    protected Fragment createFragment() {
        return MovieGridFragment.newInstance(getMovieDBApiKey());
    }

    @Override
    public void onMovieClick(Movie movie) {

        Log.i(TAG, "onMovieClick | " + "Pulling up detail for movie: " + movie.toString());

        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_DETAIL, Parcels.wrap(movie));

        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard, menu);

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.sort_menu_item:

                displayPopUpMenu();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    private void displayPopUpMenu() {

        View menuItemView = findViewById(R.id.sort_menu_item);
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.sub_menu_filter);

        MenuItem mi = popupMenu.getMenu().findItem(sortMenuIdChecked);

        //Set the proper menu item to be checked based off shared preferences.
        if (mi != null) {
            mi.setChecked(true);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_sort_highest_rated:

                        saveCheckedItemState(item);

                        return true;

                    case R.id.action_sort_most_popular:

                        saveCheckedItemState(item);

                        return true;

                    default:

                        return false;
                }

            }

            public void saveCheckedItemState(MenuItem item) {

                //No need to re-sort grid (involving network calls) if the current sort option is picked again.
                if (item.isChecked()){
                    return;
                }

                item.setChecked(true);

                sortMenuIdChecked = item.getItemId();

                // Save which menu item is checked in saved preferences.
                SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(SORT_MENU_CHECKED_PREF, sortMenuIdChecked);

                // Commit the edits!
                editor.apply();

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                MovieGridFragment gridFragment = (MovieGridFragment) fragmentManager.findFragmentById(R.id.fragment_container);

                if(item.getItemId() == R.id.action_sort_highest_rated){

                    gridFragment.grabHomeMovies(MovieDBUtils.buildHighestRatingURL(getMovieDBApiKey()));

                } else {

                    gridFragment.grabHomeMovies(MovieDBUtils.buildMostPopularURL(getMovieDBApiKey()));
                }

            }


        });

        popupMenu.show();
    }

}
