package com.example.selma.popularmoviestage1.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieContract {

    //content provider constants
    public static final String AUTHORITY = "com.example.selma.popularmoviestage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_FAVORITE = "favorite";

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        // table and column names
        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
