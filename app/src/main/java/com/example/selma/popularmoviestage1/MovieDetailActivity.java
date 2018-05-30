package com.example.selma.popularmoviestage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selma.popularmoviestage1.Utils.Constants;
import com.example.selma.popularmoviestage1.data.FavoriteMovieContract;
import com.example.selma.popularmoviestage1.data.model.MovieModel;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String MOVIE_DATA = "movie_data";
    private static final int DETAIL_LOADER_ID = 1;

    private Toolbar mToolbar;
    private ImageView ivPoster, ivBackdrop;
    private TextView tvTitle, tvReleaseDate, tvVoteAverage, tvOverview, tvOriginalTitle;
    private LinearLayout llTrailers, llReviews;
    private FloatingActionButton fabFavorite;
    private MovieModel movieModel;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initUI();

        movieModel = (MovieModel) getIntent().getExtras().get(MOVIE_DATA);

        Picasso.with(this).load(Constants.MOVIE_POSTER_BASE_URL + movieModel.getPoster_path())
                .error(R.drawable.ic_theaters_black_24px).into(ivPoster);
        Picasso.with(this).load(Constants.MOVIE_POSTER_BASE_URL + movieModel.getBackdrop_path())
                .error(R.drawable.ic_theaters_black_24px).into(ivBackdrop);
        tvTitle.setText(movieModel.getTitle());
        tvReleaseDate.setText(movieModel.getRelease_date());
        tvVoteAverage.setText(String.valueOf(movieModel.getVote_average()));
        tvOverview.setText(movieModel.getOverview());
        tvOriginalTitle.setText(movieModel.getOriginal_title());

        getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
    }

    private void initUI() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.movie_detail));
        ivPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);
        tvReleaseDate = (TextView) findViewById(R.id.tv_releaese_date);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        fabFavorite = (FloatingActionButton)findViewById(R.id.fab_favorite);
        tvVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        tvOverview = (TextView) findViewById(R.id.tv_overview);
        tvOriginalTitle = (TextView) findViewById(R.id.tv_original_title);
        llTrailers = (LinearLayout) findViewById(R.id.ll_trailers);
        llReviews = (LinearLayout) findViewById(R.id.ll_reviews);
        llTrailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTrailersActivity = new Intent(MovieDetailActivity.this, TrailersActivity.class);
                toTrailersActivity.putExtra(ReviewActivity.MOVIE_ID, String.valueOf(movieModel.getId()));
                startActivity(toTrailersActivity);
            }
        });
        llReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
                intent.putExtra(ReviewActivity.MOVIE_ID, String.valueOf(movieModel.getId()));
                startActivity(intent);
            }
        });

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFavorite){

                    Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(String.valueOf(movieModel.getId())).build();

                    getContentResolver().delete(uri,null,null);
                    Toast.makeText(MovieDetailActivity.this, R.string.remove_favorite, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: ------>remove: "+getContentResolver().delete(uri,null,null));
                    fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24px);

                    isFavorite = false;

                }else{

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,movieModel.getId());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE,movieModel.getTitle());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE,movieModel.getOriginal_title());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,movieModel.getPoster_path());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH,movieModel.getBackdrop_path());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE,movieModel.getVote_average());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW,movieModel.getOverview());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,movieModel.getRelease_date());

                    Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,contentValues);

                    if (uri!=null){
                        Toast.makeText(MovieDetailActivity.this, R.string.add_favorite, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: ------>add: "+uri);
                        fabFavorite.setImageResource(R.drawable.ic_favorite_black_24px);
                    }
                    isFavorite = true;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(DETAIL_LOADER_ID, null, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try{
                    String[] whereIs = {Integer.toString(movieModel.getId())};
                    return getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                            null,
                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?",
                            whereIs,
                            null);
                } catch (Exception e){
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            isFavorite = true;
            fabFavorite.setImageResource(R.drawable.ic_favorite_black_24px);
        }else{
            isFavorite = false;
            fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24px);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
