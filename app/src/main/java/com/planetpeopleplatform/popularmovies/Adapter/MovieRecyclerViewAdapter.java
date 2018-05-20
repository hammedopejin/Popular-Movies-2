package com.planetpeopleplatform.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planetpeopleplatform.popularmovies.Activity.DetailActivity;
import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_POSITION;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.EXTRA_STRING;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.MOVIE_ID;
import static com.planetpeopleplatform.popularmovies.utilities.Constants.SORT_BY;

/**
 * Created by Hammedopejin on 5/10/2018.
 */

public  class MovieRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private String jsonMovieResponse = "";
    private static String sortBy = "";

    private List<Movie> mMovies;
    public Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mImageView;
        public final TextView mMovieTitleTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.movie_image);
            mMovieTitleTV = itemView.findViewById(R.id.movie_title);
        }
    }

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies, String jResponse, String sortBy) {
        mContext = context;
        mMovies = movies;
        jsonMovieResponse = jResponse;
        this.sortBy = sortBy;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, final int position) {

        holder.mMovieTitleTV.setText(mMovies.get(position).getOriginalTitle());
        Picasso.with(mContext).load(mMovies.get(position).getThumbnailImage()).into(holder.mImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(EXTRA_POSITION, position);
                intent.putExtra(EXTRA_STRING, jsonMovieResponse);
                intent.putExtra(MOVIE_ID, mMovies.get(position).getId());
                intent.putExtra(SORT_BY, sortBy);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null != mMovies) {
            return mMovies.size();
        }
        return  0;
    }
}
