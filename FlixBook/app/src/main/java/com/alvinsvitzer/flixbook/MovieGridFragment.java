package com.alvinsvitzer.flixbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.utilities.MovieDBJSONUtils;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class MovieGridFragment extends Fragment {

    private static final String TAG = MovieGridActivity.class.getSimpleName();
    private static final String MOVIE_DB_API_KEY = "movieDbApiKey";

    private String mMovieDBApiKey;
    private List<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private VolleyNetworkSingleton mVolleyNetworkSingleton;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static MovieGridFragment newInstance(String movieDBApiKey) {
        MovieGridFragment fragment = new MovieGridFragment();
        Bundle args = new Bundle();
        args.putString(MOVIE_DB_API_KEY, movieDBApiKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMovieDBApiKey = getArguments().getString(MOVIE_DB_API_KEY);
        }

        mVolleyNetworkSingleton = VolleyNetworkSingleton.getInstance(getActivity());
        grabHomeMovies();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false));

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        //updateUI();
    }

    private void grabHomeMovies() {

        URL requestDefaultMovieURL = MovieDBUtils.buildDefaultUrl(mMovieDBApiKey);

        JsonObjectRequest jsObjectRequest  = new JsonObjectRequest
                (Request.Method.GET, requestDefaultMovieURL.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            mMovieList = MovieDBJSONUtils.getMovieDataFromJSONObject(getActivity(), response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "onResponse: " + "Just finished JSON calls. " + mMovieList.get(0).toString());
                        updateUI();

                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        Log.e(TAG, "onErrorResponse: ", error);

                        Toast.makeText(getActivity(), "Unable to retrieve movie data.", Toast.LENGTH_SHORT)
                                .show();

                    }
                });

        mVolleyNetworkSingleton.addToRequestQueue(jsObjectRequest);

    }

    private void updateUI() {

        if (mMovieAdapter == null){

            mMovieAdapter = new MovieAdapter(mMovieList);
            mRecyclerView.setAdapter(mMovieAdapter);

        }

    }

    private class MovieHolder extends RecyclerView.ViewHolder{

        private NetworkImageView mMoviePoster;
        private ImageLoader mImageLoader;

        public MovieHolder(View itemView) {
            super(itemView);

            mMoviePoster = (NetworkImageView) itemView.findViewById(R.id.movie_poster_image);
        }

        public void bindMovie(Movie movie){

            String imageUrl = MovieDBUtils.buildMoviePosterURL(movie.getMoviePoster()).toString();

            Log.i(TAG, "bindMovie | ImageUrl: " + imageUrl);

            mImageLoader = mVolleyNetworkSingleton.getImageLoader();
            mMoviePoster.setImageUrl(imageUrl,mImageLoader);

        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder>{

        private List<Movie> mMovies;

        MovieAdapter(List<Movie> movies){

            mMovies = movies;

        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.grid_item_movie, parent, false);

            return new MovieHolder(v);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {

            Movie movie = mMovies.get(position);
            holder.bindMovie(movie);

        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }
}
