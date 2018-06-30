package com.github.mitchellrgiles.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Reviews> reviewsList;
    
    private final ReviewAdapterOnClickHandler clickHandler;

    private Context context;

    public interface ReviewAdapterOnClickHandler {
        void onClick();
    }

    public ReviewAdapter(Context context, ReviewAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView reviewAuthorTextView;

        private final TextView reviewContentTextView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            reviewAuthorTextView = view.findViewById(R.id.review_author_name);
            reviewContentTextView = view.findViewById(R.id.review_content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick();
        }
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        Reviews currentReview = reviewsList.get(position);
        holder.reviewAuthorTextView.setText(currentReview.getReviewAuthor());
        holder.reviewContentTextView.setText(currentReview.getReviewContent());
    }

    @Override
    public int getItemCount() {
        if(reviewsList == null) {
            return 0;
        }
        return reviewsList.size();
    }

    public void setReviewsList(List<Reviews> reviews) {
        reviewsList = reviews;
        notifyDataSetChanged();
    }
}
