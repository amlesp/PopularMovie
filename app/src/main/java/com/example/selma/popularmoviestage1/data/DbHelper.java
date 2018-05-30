package com.example.selma.popularmoviestage1.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE "+ FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " ("
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, "
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, "
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT, "
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH + " TEXT, "
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL, "
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT, "
                + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);

    }

}
