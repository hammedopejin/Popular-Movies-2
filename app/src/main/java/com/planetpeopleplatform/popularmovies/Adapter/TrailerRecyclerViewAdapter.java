package com.planetpeopleplatform.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planetpeopleplatform.popularmovies.Model.Trailer;
import com.planetpeopleplatform.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.planetpeopleplatform.popularmovies.utilities.Constants.TRAILER_BASE_URL;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolder> {

    private List<Trailer> mTrailer;
    public Context mContext;

    public TrailerRecyclerViewAdapter(Context context, List<Trailer> trailers) {
        mContext = context;
        mTrailer = trailers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mTrailerNameTV.setText(mTrailer.get(position).getName());
        holder.mTrailerTypeV.setText(mTrailer.get(position).getType());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TRAILER_BASE_URL + mTrailer.get(position).getKey()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        Picasso.with(mContext).load(mTrailer.get(position).getThumbnail()).into(holder.mImageView);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTrailerNameTV;
        public final TextView mTrailerTypeV;
        public final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTrailerNameTV = itemView.findViewById(R.id.trailer_name_tv);
            mTrailerTypeV = itemView.findViewById(R.id.trailer_type_tv);
            mImageView = itemView.findViewById(R.id.trailer_thumbnail_iv);
        }
    }

    @Override
    public int getItemCount() {
        if (null != mTrailer) {
           return mTrailer.size();
        }
        return 0;
    }
}
