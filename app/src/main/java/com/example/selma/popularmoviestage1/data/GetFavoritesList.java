package com.example.selma.popularmoviestage1.data;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;
import com.example.selma.popularmoviestage1.data.model.MovieModel;

import java.util.ArrayList;


public class GetFavoritesList implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FAVORITE_LOADER_ID = 100;
    private final String TAG = GetFavoritesList.class.getSimpleName();
    private Context context;
    private ArrayList<MovieModel> mData;
    private android.support.v4.app.LoaderManager loaderManager;
    private IFavoritesList mListener;

    public interface IFavoritesList{
        void onSuccess(ArrayList<MovieModel> modelArrayList);
        void onError();
    }

    public GetFavoritesList(Context context, android.support.v4.app.LoaderManager loaderManager, IFavoritesList mListener) {
        this.context = context;
        this.mListener = mListener;
        this.loaderManager = loaderManager;
        loaderManager.destroyLoader(FAVORITE_LOADER_ID);
        loaderManager.initLoader(FAVORITE_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<Cursor>(context) {

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
                try {
                    return context.getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
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
        ArrayList<MovieModel> movieFavorite = new ArrayList();

        if (data != null && data.moveToFirst()) {

            while (!data.isAfterLast()) {

                movieFavorite.add(generateObjectFromCursor(data));
                data.moveToNext();
            }
            mListener.onSuccess(movieFavorite);
        }

        if (movieFavorite.size() == 0)
            mListener.onError();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private MovieModel generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        MovieModel model = new MovieModel();

        model.setId(cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)));
        model.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE)));
        model.setOriginal_title(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE)));
        model.setBackdrop_path(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH)));
        model.setPoster_path(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH)));
        model.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW)));
        model.setRelease_date(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)));

        return model;

    }
}
