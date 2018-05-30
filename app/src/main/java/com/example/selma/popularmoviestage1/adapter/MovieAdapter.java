package com.example.selma.popularmoviestage1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.selma.popularmoviestage1.R;
import com.example.selma.popularmoviestage1.Utils.Constants;
import com.example.selma.popularmoviestage1.data.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MovieModel> mData;
    private IMovieAdapter mListener;

    public MovieAdapter(Context context) {
        this.mContext = context;
        this.mListener = (IMovieAdapter) context;
    }

    public void update(ArrayList<MovieModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieModel movieModel = mData.get(position);
        Picasso.with(mContext)
                .load(Constants.MOVIE_POSTER_BASE_URL + movieModel.getPoster_path())
                .placeholder(R.drawable.ic_theaters_black_24px)
                .into(holder.ivMoviThumbnail);

        holder.ivMoviThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(movieModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMoviThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
        }
    }
}
