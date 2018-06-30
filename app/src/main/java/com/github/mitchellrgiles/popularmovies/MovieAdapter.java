package com.github.mitchellrgiles.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> movieList;

    private final MovieAdapterOnClickHandler clickHandler;

    private Context context;

    public interface MovieAdapterOnClickHandler {
        void onClick(String movieId);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView moviePosterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            moviePosterImageView = view.findViewById(R.id.movie_list_item_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String movieId = movieList.get(adapterPosition).getMovieId();
            clickHandler.onClick(movieId);
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie currentMovie = movieList.get(position);
        movieAdapterViewHolder.moviePosterImageView.setContentDescription(currentMovie.getMovieTitle());
        String imagePath = currentMovie.getPosterPath();
        Log.d("TEST!!", "onBindViewHolder: " + imagePath);
        String moviePosterUrl = MoviesUrlBuilder.moviePosterUrlBuilder(context, imagePath, "w185");
        Log.d("TEST", "onBindViewHolder: " + moviePosterUrl);

        Picasso.with(context).load(moviePosterUrl).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(movieAdapterViewHolder.moviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if (movieList == null) return 0;
        return movieList.size();
    }

    public void setMovie(List<Movie> movies) {
        movieList = movies;
        notifyDataSetChanged();
    }
}
