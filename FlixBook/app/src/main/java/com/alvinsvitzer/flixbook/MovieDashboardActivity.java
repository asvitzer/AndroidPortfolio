package com.alvinsvitzer.flixbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
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

        getMenuInflater().inflate(R.menu.dashboard, menu);
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

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    public void replaceFragmentContainer(Fragment fragmentToCommit, String tag, boolean addToBackStack){

        FragmentManager fragmentManager =  getSupportFragmentManager();

        if (addToBackStack){
            fragmentManager.beginTransaction()
                    .replace(mFragmentContainerId, fragmentToCommit, tag)
                    .addToBackStack(tag)
                    .commit();
        }else{

            /**
             * popBackstack done so that if the Up/Home navigation was used when going from MovieDetailFragment to
             * MovieGridFragment, the back button will not try and reload the MovieGridFragment. Pressing the back button
             * will either go to whatever launched the application or the home screen. This was implemented because
             * the back button & up/home button mirror themselves in how they affect the navigation flow,
             * at least when going from MovieGridFragment to MovieDetailFragment with both sharing 1 hosting activity.
             * popping off the last transaction keeps their navigation flow in sync.
             *
             * This will need to be changed if there if ever another flow that gets to the MovieDetailFragment other
             * than from MovieGridFragment.
             *
             */
            fragmentManager.popBackStack();

            fragmentManager.beginTransaction()
                    .replace(mFragmentContainerId, fragmentToCommit, tag)
                    .commit();
        }



    }
}
