package com.alvinsvitzer.flixbook.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvinsvitzer.flixbook.Injection;
import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.alvinsvitzer.flixbook.R.string.no_movie_data_text;

public class MovieGridFragment extends Fragment implements MoviesContract.View {

    public static final String SORT_MENU_CHECKED_PREF = "sortMenuChecked";
    public static final String TAG = MovieGridFragment.class.getSimpleName();
    @BindView(R.id.no_data_text_view)
    TextView mNoDataTextView;
    @BindView(R.id.movie_recycler_view)
    RecyclerView mRecyclerView;
    private MoviesFilterType mSortMenuIdChecked;
    private List<Movie> mMovieList;
    private MovieAdapter mMovieAdapter;
    private SharedPreferences mSharedPreferences;
    private MoviesContract.Presenter mPresenter;
    private OnFragmentInteractionListener mListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        //Used to keep Presenter alive during rotation changes
        setRetainInstance(true);

        // Restore preferences for which sorting option was used
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        mSortMenuIdChecked = MoviesFilterType.valueOf(mSharedPreferences.getInt(SORT_MENU_CHECKED_PREF, MoviesFilterType.POPULAR_MOVIES.getValue()));

        //Pass in an empty ArrayList for now. Data is added after Volley Network call.
        mMovieList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(mMovieList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        ButterKnife.bind(this, v);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        attachPresenter();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);

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

        View menuItemView = getActivity().findViewById(R.id.sort_menu_item);
        PopupMenu popupMenu = new PopupMenu(getActivity(), menuItemView);
        popupMenu.inflate(R.menu.sub_menu_filter);

        //Set the menu item that was chosen last time. If first time, use default.
        int currentId;

        switch(mSortMenuIdChecked){

            case HIGHLY_RATED_MOVIES:
                currentId = R.id.action_sort_highest_rated;
                break;
            case POPULAR_MOVIES:
                currentId = R.id.action_sort_most_popular;
                break;
            case FAVORITE_MOVIES:
                currentId = R.id.action_sort_favorite;
                break;
            default:
                currentId = R.id.action_sort_most_popular;
        }

        MenuItem mi = popupMenu.getMenu().findItem(currentId);

        //Set the proper menu item to be checked based off shared preferences.
        mi.setChecked(true);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_sort_highest_rated:

                        saveCheckedItemState(item, MoviesFilterType.HIGHLY_RATED_MOVIES);

                        return true;

                    case R.id.action_sort_most_popular:

                        saveCheckedItemState(item, MoviesFilterType.POPULAR_MOVIES);

                        return true;

                    case R.id.action_sort_favorite:

                        saveCheckedItemState(item, MoviesFilterType.FAVORITE_MOVIES);

                        return true;

                    default:

                        return false;
                }

            }

            private void saveCheckedItemState(MenuItem item, MoviesFilterType moviesFilterType) {

                //No need to re-sort grid (involving network calls) if the current sort option is picked again.
                if (item.isChecked()){
                    return;
                }

                item.setChecked(true);

                mSortMenuIdChecked = moviesFilterType;

                // Save which menu item is checked in saved preferences.
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt(SORT_MENU_CHECKED_PREF, mSortMenuIdChecked.getValue());

                // Commit the edits!
                editor.apply();

                //Reloads movies since sorting criteria has changed
                mPresenter.loadMovies();

            }

        });

        popupMenu.show();
    }

    @Override
    public MoviesFilterType getSortingId(){

       return mSortMenuIdChecked;
    }

    @Override
    public void attachPresenter() {


        if(mPresenter == null ){

            mPresenter = new MoviesPresenter(Injection.provideMovieDataStoreRepository(getActivity())
                    , this
                    , VolleyNetworkSingleton.getInstance(getActivity()).getImageLoader());

            mPresenter.start();

        }

    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showNoDataTextView(){

        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoDataTextView.setVisibility(View.VISIBLE);


    }

    @Override
    public void setNoDataTextView() {

        mNoDataTextView.setText(getString(no_movie_data_text));
    }

    @Override
    public void setNoFavoriteTextView() {

        mNoDataTextView.setText(getString(R.string.text_no_favorite_data));
    }

    @Override
    public void showMovies(List<Movie> movieList) {

        mMovieList.clear();

        for(Movie m: movieList){
            mMovieList.add(m);
        }

        if (mMovieAdapter == null){

            mMovieAdapter = new MovieAdapter(mMovieList);
            mRecyclerView.setAdapter(mMovieAdapter);

        } else {

            mMovieAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void hideNoDataTextView(){

        mRecyclerView.setVisibility(View.VISIBLE);
        mNoDataTextView.setVisibility(View.INVISIBLE);

    }

    public interface OnFragmentInteractionListener {

        void onMovieClick();
    }

    private class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private NetworkImageView mMoviePoster;
        private TextView mMovieTitle;
        private TextView mMovieReleaseDate;


        public MovieHolder(View itemView) {
            super(itemView);

            mMoviePoster = (NetworkImageView) itemView.findViewById(R.id.movie_poster_image);
            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_poster_title);
            mMovieReleaseDate = (TextView) itemView.findViewById(R.id.movie_poster_release_date);

            itemView.setOnClickListener(this);
        }

        private void bindMovie(Movie movie, ImageLoader imageLoader, String imageUrl){

            mMovieTitle.setText(movie.getMovieTitle());
            mMovieReleaseDate.setText(MovieDBUtils.getLocalDate(movie.getReleaseDate()));

            //Set Default Image & Error Images if can't be fetched from network
            imageLoader.get(imageUrl, ImageLoader.getImageListener(mMoviePoster,
                    R.drawable.small_movie_placeholder, R.drawable.small_movie_placeholder));

            mMoviePoster.setImageUrl(imageUrl,imageLoader);

        }

        @Override
        public void onClick(View v) {

            Movie movie = mMovieList.get(getAdapterPosition());

            Log.i(TAG, "onMovieClick | " + "Pulling up detail for movie: " + movie.toString());

            mPresenter.saveMovie(movie);
            mListener.onMovieClick();


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

            String imageUrl = mPresenter.buildMovieUrl(movie);

            holder.bindMovie(movie, mPresenter.getImageLoader(), imageUrl);

        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }


}
