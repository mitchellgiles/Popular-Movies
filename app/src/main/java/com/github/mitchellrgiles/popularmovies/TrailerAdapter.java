package com.github.mitchellrgiles.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private List<Trailers> trailersList;

    private final TrailerAdapterOnClickHandler clickHandler;

    private Context context;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailerUrl, String movieKey);
    }

    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView trailerThumbnailImageView;

        private final TextView trailerTitleTextView;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            trailerThumbnailImageView = view.findViewById(R.id.trailer_thumbnail);
            trailerTitleTextView = view.findViewById(R.id.trailer_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String trailerUrl = trailersList.get(adapterPosition).getVideoUrl();
            String movieKey = trailersList.get(adapterPosition).getMovieKey();
            clickHandler.onClick(trailerUrl, movieKey);
        }
    }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder, int position) {
        Trailers currentTrailer = trailersList.get(position);
        String thumbnailUrl = currentTrailer.getThumbnailUrl();
        Picasso.with(context).load(thumbnailUrl).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(holder.trailerThumbnailImageView);
        holder.trailerTitleTextView.setText(currentTrailer.getTrailerName());
    }

    @Override
    public int getItemCount() {
        if(trailersList == null) {
            return 0;
        }
        return trailersList.size();
    }

    public void setTrailersList(List<Trailers> trailers) {
        trailersList = trailers;
        notifyDataSetChanged();
    }
}
