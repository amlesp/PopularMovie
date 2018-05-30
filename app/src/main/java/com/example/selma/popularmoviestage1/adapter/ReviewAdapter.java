package com.example.selma.popularmoviestage1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.selma.popularmoviestage1.R;
import com.example.selma.popularmoviestage1.data.model.ReviewModel;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ReviewModel> mData = new ArrayList<>();


    public ReviewAdapter(Context context) {
        this.mContext = context;
    }

    public void update(ArrayList<ReviewModel> data){
        if (data != null){
            mData = data;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewModel reviewModel = mData.get(position);
        holder.tvAuthor.setText(reviewModel.getAuthor());
        holder.expandableTextView.setText(reviewModel.getContent());
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthor;
        ExpandableTextView expandableTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            expandableTextView = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);
        }
    }
}
