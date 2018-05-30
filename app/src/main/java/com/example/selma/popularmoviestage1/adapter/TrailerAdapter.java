package com.example.selma.popularmoviestage1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.selma.popularmoviestage1.R;
import com.example.selma.popularmoviestage1.Utils.Constants;
import com.example.selma.popularmoviestage1.data.model.TrailerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TrailerModel> mData = new ArrayList<>();
    private ITrailerAdapter mListener;

    public TrailerAdapter(Context context) {
        this.mContext = context;
        this.mListener = (ITrailerAdapter) context;
    }

    public void update(ArrayList<TrailerModel> data) {
        if (data != null) {
            mData = data;
            notifyDataSetChanged();
        }
    }

    public interface ITrailerAdapter{
       void onClick(String key);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TrailerModel trailerModel = mData.get(position);
        holder.tvTrailerTitle.setText(trailerModel.getName());
        Picasso.with(mContext)
                .load(Constants.YOUTUBE_THUMBNAIL_BASE_URL + trailerModel.getKey() + Constants.YOUTUBE_THUMBNAIL)
                .placeholder(R.drawable.ic_theaters_black_24px)
                .error(R.drawable.ic_theaters_black_24px)
                .into(holder.ivTrailer);

        holder.ivTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mListener.onClick(trailerModel.getKey());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrailerTitle;
        ImageView ivTrailer;


        public ViewHolder(View itemView) {
            super(itemView);

            tvTrailerTitle = (TextView) itemView.findViewById(R.id.tv_trailer_title);
            ivTrailer = (ImageView) itemView.findViewById(R.id.iv_trailer);

        }
    }
}
