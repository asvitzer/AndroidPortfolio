package com.alvinsvitzer.flixbook;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;

import org.parceler.Parcels;

public class MovieGridActivity extends SingleFragmentActivity
        implements MovieGridFragment.OnFragmentInteractionListener{

    private static final String TAG = MovieGridActivity.class.getSimpleName();

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

                Toast.makeText(this, "Sorted!", Toast.LENGTH_LONG).show();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }
}
