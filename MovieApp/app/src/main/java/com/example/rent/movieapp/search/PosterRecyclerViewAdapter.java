package com.example.rent.movieapp.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.listing.OnMovieItemClickListener;

import java.util.Collections;
import java.util.List;

import static butterknife.ButterKnife.findById;
import static com.example.rent.movieapp.R.id.single_poster;

/**
 * Created by RENT on 2017-03-11.
 */

public class PosterRecyclerViewAdapter extends RecyclerView.Adapter<PosterRecyclerViewAdapter.ViewHolder> {

    private List<SimpleMovieItem> urls = Collections.emptyList();
    private OnMovieItemClickListener onMovieItemClickListener;


    public void setOnMovieItemClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }


    public void setSimpleMovieItems(List<SimpleMovieItem> urls) {
        this.urls = urls;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);
       return new ViewHolder(layout);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.posterImageView.getContext()).load(urls.get(position).getPoster()).into(holder.posterImageView);
        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMovieItemClickListener!=null){
                    onMovieItemClickListener.onMovieItemClick(urls.get(position).getImdbID());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = findById(itemView, single_poster);
        }
    }
}
