package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.Injection;
import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.data.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alvin on 3/13/17.
 */

public class MovieReviewFragment extends Fragment implements MovieReviewContract.View {

    private MovieReviewContract.Presenter mPresenter;
    private List<Review> mReviewList;

    private static final String TAG = MovieReviewFragment.class.getSimpleName();

    @BindView(R.id.MovieDetailCoordLayout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attachPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movie_review, container, false);

        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void attachPresenter() {

        if (mPresenter == null){

            mPresenter = new MovieReviewPresenter(this, Injection.provideMovieDataStoreRepository(getActivity()));
            mPresenter.start();

        }

    }

    @Override
    public void notifyNoReviews() {

        Snackbar.make(mCoordinatorLayout, R.string.snackbar_text_no_review, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void displayReviews(List<Review> reviews) {

    }

    private class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mReviewText;
        private TextView mAuthor;

        public ReviewHolder(View itemView) {
            super(itemView);

            mReviewText = (TextView) itemView.findViewById(R.id.movie_poster_title);
            mAuthor = (TextView) itemView.findViewById(R.id.movie_poster_title);

            itemView.setOnClickListener(this);
        }

        public void bindReview(Review review){

            mReviewText.setText(review.getContent());
            mAuthor.setText(review.getAuthor());

        }

        @Override
        public void onClick(View v) {

            Review review = mReviewList.get(getAdapterPosition());

            Log.i(TAG, "onReviewClick | " + "Pulling up review: " + review.getId());

            Toast.makeText(getActivity(), "Review Clicked", Toast.LENGTH_LONG).show();

        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder>{

        private List<Review> mReviews;

        ReviewAdapter(List<Review> reviews){

            mReviews = reviews;

        }

        @Override
        public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.grid_item_movie, parent, false);

            return new ReviewHolder(v);
        }

        @Override
        public void onBindViewHolder(ReviewHolder holder, int position) {
            Review review = mReviews.get(position);
            holder.bindReview(review);
        }


        @Override
        public int getItemCount() {
            return mReviews.size();
        }
    }

}
