package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.Injection;
import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.data.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alvin on 3/13/17.
 */

public class MovieReviewFragment extends Fragment implements MovieReviewContract.View {

    private MovieReviewContract.Presenter mPresenter;
    private List<Review> mReviewList;
    private ReviewAdapter mReviewAdapter;

    private static final String TAG = MovieReviewFragment.class.getSimpleName();

    @BindView(R.id.fragment_grid_container)
    FrameLayout mFrameLayout;

    @BindView(R.id.review_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReviewList = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(mReviewList);
        attachPresenter();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movie_review, container, false);

        ButterKnife.bind(this, v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mReviewAdapter);

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

        Snackbar.make(mFrameLayout, R.string.snackbar_text_no_review, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void displayReviews(List<Review> reviews) {

        mReviewList.clear();

        for(Review r: reviews){

            mReviewList.add(r);
        }

        mReviewAdapter.notifyDataSetChanged();


    }

    private class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mReviewText;
        private TextView mAuthor;

        public ReviewHolder(View itemView) {
            super(itemView);

            mReviewText = (TextView) itemView.findViewById(R.id.textViewReview);
            mAuthor = (TextView) itemView.findViewById(R.id.textViewReviewer);

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
            View v = inflater.inflate(R.layout.grid_item_review, parent, false);

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
