package com.planetpeopleplatform.popularmovies.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.planetpeopleplatform.popularmovies.Model.Movie;
import com.planetpeopleplatform.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hammedopejin on 4/27/2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {


    public MovieAdapter(Activity context, List<Movie> movies)  {
        super(context, 0,  movies);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if (null == convertView){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        ImageView thumbImage = convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(movie.getThumdnailImage()).into(thumbImage);

        TextView movieTitleView = convertView.findViewById(R.id.movie_title);
        movieTitleView.setText(movie.getOriginalTitle());

        return convertView;
    }
}
