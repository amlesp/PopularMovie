package com.example.selma.popularmoviestage1;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.selma.popularmoviestage1.adapter.ReviewAdapter;
import com.example.selma.popularmoviestage1.data.ApiUrlGenerator;
import com.example.selma.popularmoviestage1.data.GetReviews;
import com.example.selma.popularmoviestage1.data.model.ReviewModel;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity implements GetReviews.IApiReviewResponse {

    public static final String MOVIE_ID = "movie_id";
    private static final String LIST_STATE_KEY = "list.state";

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ReviewAdapter mAdapter;
    private String movieID;

    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        initUI();
        Intent intent = getIntent();
        if (intent != null) {
            movieID = intent.getStringExtra(MOVIE_ID);
        }

        if (movieID != null) {
            new GetReviews(ReviewActivity.this, ApiUrlGenerator.getUrl(ApiUrlGenerator.REVIEWS, movieID));
        }
    }

    private void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.reviews));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24px);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ReviewAdapter(ReviewActivity.this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSuccess(ArrayList<ReviewModel> reviewModelArrayList) {
        if (!reviewModelArrayList.isEmpty()){
            mAdapter.update(reviewModelArrayList);
            if(listState!=null){
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }else {
            Toast.makeText(this, R.string.no_reviews, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(ReviewActivity.this, error, Toast.LENGTH_SHORT).show();
    }
}
