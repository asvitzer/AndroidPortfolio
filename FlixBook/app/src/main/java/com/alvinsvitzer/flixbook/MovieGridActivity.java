package com.alvinsvitzer.flixbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MovieGridActivity extends SingleFragmentActivity implements MovieGridFragment.OnFragmentInteractionListener{

    private static final String TAG = MovieGridActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     }

    @Override
    protected Fragment createFragment() {
        return MovieGridFragment.newInstance(getMovieDBApiKey());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
