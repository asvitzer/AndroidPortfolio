package com.alvinsvitzer.flixbook.moviedetail;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.model.Trailer;
import com.alvinsvitzer.flixbook.utilities.MovieDBJSONUtils;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.alvinsvitzer.flixbook.utilities.YouTubeUtils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.jakewharton.rxbinding.view.RxView;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

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

    @BindView(R.id.playTrailerFab)
    FloatingActionButton mPlayMovieFab;

    private ImageLoader mImageLoader;
    private Movie mMovie;
    private String mApiKey;

    // Used to handle unsubscription during teardown of Fragment
    CompositeSubscription subscriptions = new CompositeSubscription();

    public static final String PARAM_MOVIE_DETAIL = "movieDetail";
    public static final String PARAM_API_KEY = "apiKey";

    public static final String TAG = MovieDetailFragment.class.getSimpleName();

    VolleyNetworkSingleton mVolleyNetworkSingleton;

    public static MovieDetailFragment newInstance(Parcelable movie, String apiKey){

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_MOVIE_DETAIL, movie);
        args.putString(PARAM_API_KEY, apiKey);
        movieDetailFragment.setArguments(args);

        return movieDetailFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mMovie = Parcels.unwrap(getArguments().getParcelable(PARAM_MOVIE_DETAIL));
            mApiKey = getArguments().getString(PARAM_API_KEY);
        } else {
            Toast.makeText(getActivity(), R.string.movie_data_error_text, Toast.LENGTH_SHORT).show();
        }

        mVolleyNetworkSingleton = VolleyNetworkSingleton.getInstance(getActivity());
        mImageLoader = mVolleyNetworkSingleton.getImageLoader();

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

        grabTrailerInformation(MovieDBUtils.buildMovieVideoURL(mApiKey, String.valueOf(mMovie.getMovieId())));

        attachMovieInformation();

        // Inflate the layout for this fragment
        return v;
    }

    private void attachMovieInformation() {

        mBannerText.setText(mMovie.getMovieTitle());
        mPlotSynopsis.setText(mMovie.getPlotSynopsis());
        mReleaseDate.setText(MovieDBUtils.getLocalDate(mMovie.getReleaseDate()));

        int movieRatingId = R.string.movie_info_vote_average_rating;

        String movieRating = String.format(getString(movieRatingId), mMovie.getVoteAverage());
        mVoteAverage.setText(movieRating);

        String posterImageUrl = MovieDBUtils.buildMoviePosterURL(mMovie.getMoviePoster()).toString();
        mPosterImage.setImageUrl(posterImageUrl,mImageLoader);

        String backdropImageUrl = MovieDBUtils.buildMovieBackdropURL(mMovie.getMovieBackdrop()).toString();
        mBackdropImage.setImageUrl(backdropImageUrl,mImageLoader);

        subscriptions.add(RxView.clicks(mPlayMovieFab).map(new Func1<Void, Intent>() {

            @Override
            public Intent call(Void aVoid) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getOfficialYoutubeTrailerUrl()));

                return intent;

            }

        }).subscribe(new Action1<Intent>() {
            @Override
            public void call(Intent intent) {
                if (intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(intent);
                }

            }
        }));

    }

    public String getOfficialYoutubeTrailerUrl(){

        if(mMovie.getTrailerList() == null){
            Log.w(TAG, "getOfficialYoutubeTrailerUrl: Trailer List is empty");
            return null;
        }

        for(Trailer t: mMovie.getTrailerList()){

            if (t.getType().equals(MovieDBUtils.ATTRIBUTE_VIDEO_TYPE_TRAILER) && t.getSite().equals(MovieDBUtils.ATTRIBUTE_VIDE_SITE_YOUTUBE)){
                return YouTubeUtils.buildYouTubeUrl(t.getKey()).toString();
            }
        }

        Log.w(TAG, "getOfficialYoutubeTrailerUrl: No trailer for YouTube");
        return null;

    }


    public void grabTrailerInformation(URL movieDbUrl) {

        JsonObjectRequest jsObjectRequest  = new JsonObjectRequest
                (Request.Method.GET, movieDbUrl.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            List<Trailer> addTrailer = MovieDBJSONUtils.getVideoDataFromJSONObject(response);

                            mMovie.setTrailerList(addTrailer);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        enableTrailerFab();

                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        disableTrailerFab();

                        Log.e(TAG, "onErrorResponse: ", error);


                    }
                });

        mVolleyNetworkSingleton.addToRequestQueue(jsObjectRequest);

    }

    private void disableTrailerFab(){


        mPlayMovieFab.setVisibility(View.INVISIBLE);

    }

    private void enableTrailerFab(){

        mPlayMovieFab.setVisibility(View.VISIBLE);
        mPlayMovieFab.setEnabled(true);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        subscriptions.unsubscribe();

    }

}
