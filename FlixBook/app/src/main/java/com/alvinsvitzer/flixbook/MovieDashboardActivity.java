package com.alvinsvitzer.flixbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;

public class MovieDashboardActivity extends SingleFragmentActivity implements MovieGridFragment.OnFragmentInteractionListener{

    private static final String TAG = MovieDashboardActivity.class.getSimpleName();
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

        getSupportFragmentManager().beginTransaction()
                .replace(mFragmentContainerId, movieDetail)
                .addToBackStack(null)
                .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

                getSupportFragmentManager().beginTransaction()
                        .replace(mFragmentContainerId, moveGrid)
                        //.disallowAddToBackStack()
                        .commit();

                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                return true;

            case R.id.sort_menu_item:

                Toast.makeText(this, "Sorted!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
