package com.example.selma.popularmoviestage1;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.selma.popularmoviestage1.Utils.Constants;
import com.example.selma.popularmoviestage1.adapter.TrailerAdapter;
import com.example.selma.popularmoviestage1.data.ApiUrlGenerator;
import com.example.selma.popularmoviestage1.data.GetTrailers;
import com.example.selma.popularmoviestage1.data.model.TrailerModel;

import java.util.ArrayList;

public class TrailersActivity extends AppCompatActivity implements GetTrailers.IApiTrailersResponse,TrailerAdapter.ITrailerAdapter{

    public static final String MOVIE_ID = "movie_id";
    private static final String LIST_STATE_KEY = "list.state";
    
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TrailerAdapter mAdapter;
    private String movieID;

    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        
        initUI();
        Intent intent = getIntent();
        if (intent != null) {
            movieID = intent.getStringExtra(MOVIE_ID);
        }

        if (movieID != null) {
            new GetTrailers(TrailersActivity.this, ApiUrlGenerator.getUrl(ApiUrlGenerator.VIDEOS, movieID));
        }
    }

    private void initUI() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.watch_trailers);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24px);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TrailerAdapter(TrailersActivity.this);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
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
    public void onSuccess(ArrayList<TrailerModel> trailerModelArrayList) {
        if (!trailerModelArrayList.isEmpty()){
            mAdapter.update(trailerModelArrayList);

            if(listState!=null){
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }

        }else {
            Toast.makeText(this, R.string.no_trailers, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(TrailersActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_VIDEO_BASE_URL+key));
        Intent chooser = Intent.createChooser(intent , getString(R.string.open_with));
        if (intent .resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
