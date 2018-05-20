package com.planetpeopleplatform.popularmovies.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.TextView;

import com.planetpeopleplatform.popularmovies.Model.Review;
import com.planetpeopleplatform.popularmovies.R;

import java.util.List;


/**
 * Created by Hammedopejin on 5/11/2018.
 */

public  class ReviewRecyclerViewAdapter
        extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {


    private List<Review> mReviews;
    public Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mAuthurTV;
        public final TextView mReviewTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthurTV = itemView.findViewById(R.id.review_authur_tv);
            mReviewTV = itemView.findViewById(R.id.review_content_tv);
        }
    }

    public ReviewRecyclerViewAdapter(Context context, List<Review> reviews) {
        mContext = context;
        mReviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mAuthurTV.setText(mReviews.get(position).getAuthor());
        holder.mReviewTV.setText(mReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (null != mReviews) {
            return mReviews.size();
        }
        return 0;
    }
}
