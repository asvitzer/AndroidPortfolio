package com.alvinsvitzer.flixbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;

public class MovieDashboardActivity extends SingleFragmentActivity implements MovieGridFragment.OnFragmentInteractionListener{

    private static final String TAG = MovieDashboardActivity.class.getSimpleName();
    public static final String GRID_FRAGMENT_TAG = "gridFragment";
    public static final String DETAIL_FRAGMENT_TAG = "detailFragment";

    private int mFragmentContainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentContainerId = R.id.fragment_container;

     }

    @Override
    protected Fragment createFragment() {
        return MovieGridFragment.newInstance(getMovieDBApiKey());
    }

    @Override
    public void onMovieClick(Movie movie) {

        Log.i(TAG, "onMovieClick | " + "Pulling up detail for movie: " + movie.toString());

        Fragment movieDetail = MovieDetailFragment.newInstance(movie);

        replaceFragmentContainer(movieDetail, DETAIL_FRAGMENT_TAG, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //TODO Use the id of a fragment to hide the menu options when it's the detail fragment showing
        // http://stackoverflow.com/questions/9294603/get-currently-displayed-fragment
        // Shift menu to fragments as a better alternative: http://stackoverflow.com/questions/15653737/oncreateoptionsmenu-inside-fragments

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                Fragment moveGrid = MovieGridFragment.newInstance(getMovieDBApiKey());

                replaceFragmentContainer(moveGrid, GRID_FRAGMENT_TAG, false);

                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                return true;

            case R.id.sort_menu_item:

                Toast.makeText(this, "Sorted!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragmentContainer(Fragment fragmentToCommit, String tag, boolean addToBackStack){


        FragmentManager fragmentManager =  getSupportFragmentManager();

        if (addToBackStack){
            fragmentManager.beginTransaction()
                    .replace(mFragmentContainerId, fragmentToCommit, tag)
                    .addToBackStack(tag)
                    .commit();
        }else{

            fragmentManager.beginTransaction()
                    .replace(mFragmentContainerId, fragmentToCommit, tag)
                    .commit();
        }



    }
}
