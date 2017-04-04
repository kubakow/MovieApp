package com.example.rent.movieapp.listing;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;
import com.like.LikeButton;

import java.util.Collections;
import java.util.List;

/**
 * Created by RENT on 2017-03-08.
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnLikeButtonClickListener onLikeButtonClickListener;
    private OnMovieItemClickListener onMovieItemClickListener;
    private static final int GAMES_VIEW_HOLDER = 1;
    private static final int MY_VIEW_HOLDER = 2;


    public void setOnLikeButtonClickListener(OnLikeButtonClickListener onLikeButtonClickListener) {
        this.onLikeButtonClickListener = onLikeButtonClickListener;
    }



    public void setOnMovieItemClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    private List<MovieListingItem> items = Collections.emptyList();

    public void setItems(List<MovieListingItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == GAMES_VIEW_HOLDER){
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
        return new GamesViewHolder(layout);
        }else {
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyViewHolder(layout);
        }
        }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == MY_VIEW_HOLDER) {
            MovieListingItem movieListingItem = items.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Glide.with(myViewHolder.poster.getContext()).load(movieListingItem.getPoster()).into(myViewHolder.poster);
            myViewHolder.titleAndYear.setText(movieListingItem.getTitle() + " (" + movieListingItem.getYear() + ")");
            myViewHolder.type.setText("type: " + movieListingItem.getType());
            myViewHolder.itemView.setOnClickListener(v -> {
                if (onMovieItemClickListener != null) {
                    onMovieItemClickListener.onMovieItemClick(movieListingItem.getImdbID());
                }
            });
            myViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onLikeButtonClickListener != null){
                        onLikeButtonClickListener.onLikeButtonClick(movieListingItem);
                    }
                }
            });
        }
        else{
            MovieListingItem movieListingItem = items.get(position);
            GamesViewHolder gamesViewHolder = (GamesViewHolder) holder;
            Glide.with(gamesViewHolder.poster.getContext()).load(movieListingItem.getPoster()).into(gamesViewHolder.poster);
            gamesViewHolder.title.setText(movieListingItem.getTitle());
            gamesViewHolder.itemView.setOnClickListener(v -> {
                if (onMovieItemClickListener != null) {
                    onMovieItemClickListener.onMovieItemClick(movieListingItem.getImdbID());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if("Game".equalsIgnoreCase(items.get(position).getType())){
            return GAMES_VIEW_HOLDER;
        }else{
            return MY_VIEW_HOLDER;
        }
    }

    public void addItems(List<MovieListingItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }


    class GamesViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        ImageView poster;
        TextView title;

        public GamesViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            poster = (ImageView) itemView.findViewById(R.id.game_poster);
            title = (TextView) itemView.findViewById(R.id.title_game);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LikeButton likeButton;
        View itemView;
        ImageView poster;
        TextView titleAndYear;
        TextView type;

    public MyViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        likeButton = (LikeButton) itemView.findViewById(R.id.star_like_button);
        poster = (ImageView) itemView.findViewById(R.id.list_item_poster);
        titleAndYear = (TextView) itemView.findViewById(R.id.list_item_title_and_year);
        type = (TextView) itemView.findViewById(R.id.list_item_type);

    }
}
}
