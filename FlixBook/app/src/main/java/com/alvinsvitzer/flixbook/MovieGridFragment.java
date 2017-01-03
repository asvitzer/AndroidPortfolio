package com.alvinsvitzer.flixbook;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

public class MovieGridFragment extends Fragment {

    private static final String TAG = MovieGridActivity.class.getSimpleName();
    private static final String MOVIE_DB_API_KEY = "movieDbApiKey";

    private String mMovieDBApiKey;
    private List<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private VolleyNetworkSingleton mVolleyNetworkSingleton;
    private TextView mNoDataTextView;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {

        void onMovieClick(Movie movie);
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

        if (getArguments() != null && mMovieDBApiKey == null) {
            mMovieDBApiKey = getArguments().getString(MOVIE_DB_API_KEY);
        }

        mVolleyNetworkSingleton = VolleyNetworkSingleton.getInstance(getActivity());

        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        mNoDataTextView = (TextView) v.findViewById(R.id.no_data_text_view);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.movie_recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        //Pass in an empty ArrayList for now. Data is added after Volley Network call.
        mMovieList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(mMovieList);
        mRecyclerView.setAdapter(mMovieAdapter);

        grabHomeMovies();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void grabHomeMovies() {

        URL requestDefaultMovieURL = MovieDBUtils.buildDefaultUrl(mMovieDBApiKey);

        JsonObjectRequest jsObjectRequest  = new JsonObjectRequest
                (Request.Method.GET, requestDefaultMovieURL.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            List<Movie> addMovie = MovieDBJSONUtils.getMovieDataFromJSONObject(getActivity(), response);

                            for (Movie m: addMovie){

                                mMovieList.add(m);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        updateUI();

                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "onErrorResponse: ", error);

                        showNoDataTextView();

                        Toast.makeText(getActivity(), getString(R.string.no_movie_data_text), Toast.LENGTH_SHORT)
                                .show();

                    }
                });

        mVolleyNetworkSingleton.addToRequestQueue(jsObjectRequest);

    }

    private void updateUI() {

        if (mMovieAdapter == null){

            mMovieAdapter = new MovieAdapter(mMovieList);
            mRecyclerView.setAdapter(mMovieAdapter);

        } else{

            mMovieAdapter.notifyDataSetChanged();
        }

    }

    private void showNoDataTextView(){

        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoDataTextView.setVisibility(View.VISIBLE);
    }

    private void hideNoDataTextView(){

        mRecyclerView.setVisibility(View.VISIBLE);
        mNoDataTextView.setVisibility(View.INVISIBLE);

    }

    private class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private NetworkImageView mMoviePoster;
        private ImageLoader mImageLoader;
        private TextView mMovieTitle;
        private TextView mMovieReleaseDate;

        public MovieHolder(View itemView) {
            super(itemView);

            mMoviePoster = (NetworkImageView) itemView.findViewById(R.id.movie_poster_image);
            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_poster_title);
            mMovieReleaseDate = (TextView) itemView.findViewById(R.id.movie_poster_release_date);

            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie){

            String imageUrl = MovieDBUtils.buildMoviePosterURL(movie.getMoviePoster()).toString();

            Log.i(TAG, "bindMovie | ImageUrl: " + imageUrl);

            mImageLoader = mVolleyNetworkSingleton.getImageLoader();
            mMoviePoster.setImageUrl(imageUrl,mImageLoader);

            mMovieTitle.setText(movie.getMovieTitle());
            mMovieReleaseDate.setText(movie.getReleaseDate());

        }

        @Override
        public void onClick(View v) {

            mListener.onMovieClick(mMovieList.get(getAdapterPosition()));

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
