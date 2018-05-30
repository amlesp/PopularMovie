package com.example.selma.popularmoviestage1;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.selma.popularmoviestage1.Utils.MovieSortType;
import com.example.selma.popularmoviestage1.adapter.IMovieAdapter;
import com.example.selma.popularmoviestage1.adapter.MovieAdapter;
import com.example.selma.popularmoviestage1.data.model.MovieModel;
import com.example.selma.popularmoviestage1.presenter.IMovieActivityContract;
import com.example.selma.popularmoviestage1.presenter.MovieActivityPresenter;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements IMovieActivityContract.View, IMovieAdapter {

    /*
    * Resource: https://stackoverflow.com/questions/30177137/how-to-save-instance-state-of-selected-radiobutton-on-menu
    * */

    private final String TAG = MovieActivity.class.getSimpleName();
    private static final String LIST_STATE_KEY = "list.state";
    private final String SEARCH_CRITERIA = "search_criteria";
    private final static String MENU_SELECTED = "selected";
    private RecyclerView recyclerView;
    private ImageView ivNoConnection;
    private MovieAdapter movieAdapter;
    private GridLayoutManager gridLayoutManager;
    private IMovieActivityContract.Presenter mPresenter;

    private int spanCount = 2;
    private MovieSortType movieSortType = MovieSortType.HIGHEST_RATED;
    private int selected = -1;
    private MenuItem menuItem;
    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check orientation to display 2 or 3 columns for better Ux
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            spanCount = 2;
        else spanCount = 3;

        //check search criteria
        if (savedInstanceState != null) {
            movieSortType = MovieSortType.valueOf(savedInstanceState.getString(SEARCH_CRITERIA));
            selected = savedInstanceState.getInt(MENU_SELECTED);
        }

        initUI();
        mPresenter = new MovieActivityPresenter(this);
    }

    private void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ivNoConnection = (ImageView) findViewById(R.id.iv_no_connection);
        gridLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save search criteria
        outState.putString(SEARCH_CRITERIA, movieSortType.toString());
        outState.putInt(MENU_SELECTED, selected);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        if (selected == -1) {
            return true;
        }

        switch (selected) {
            case R.id.sort_highest_rated:
                menuItem = (MenuItem) menu.findItem(R.id.sort_highest_rated);
                menuItem.setChecked(true);
                break;

            case R.id.sort_most_popular:
                menuItem = (MenuItem) menu.findItem(R.id.sort_most_popular);
                menuItem.setChecked(true);
                break;

            case R.id.sort_favorites:
                menuItem = (MenuItem) menu.findItem(R.id.sort_favorites);
                menuItem.setChecked(true);
                break;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (movieSortType) {
            case FAVORITES:
                mPresenter.loadFavoritesList(getSupportLoaderManager());
                break;
            case MOST_POPULAR:
            case HIGHEST_RATED:
                mPresenter.loadMovieList(movieSortType);
                break;
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
            selected = item.getItemId();
        }

        switch (item.getItemId()) {
            case R.id.sort_highest_rated:
                movieSortType = MovieSortType.HIGHEST_RATED;
                mPresenter.loadMovieList(MovieSortType.HIGHEST_RATED);
                break;
            case R.id.sort_most_popular:
                movieSortType = MovieSortType.MOST_POPULAR;
                mPresenter.loadMovieList(MovieSortType.MOST_POPULAR);
                break;

            case R.id.sort_favorites:
                movieSortType = MovieSortType.FAVORITES;
                mPresenter.loadFavoritesList(getSupportLoaderManager());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(MovieModel movieModel) {
        //navigate to detail activity
        Intent toDetailActivity = new Intent(MovieActivity.this, MovieDetailActivity.class);
        toDetailActivity.putExtra(MovieDetailActivity.MOVIE_DATA, movieModel);
        startActivity(toDetailActivity);
    }

    @Override
    public void showError(String error) {
        if (error==null){
            showDialog();
            return;
        }
        if (error.equals(getString(R.string.no_favorites))){
            movieAdapter.update(new ArrayList<MovieModel>());
            Snackbar mSnackbar = Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }else {
            ivNoConnection.setVisibility(View.VISIBLE);
            Snackbar mSnackbar = Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ivNoConnection.setVisibility(View.GONE);
                            mPresenter.loadMovieList(movieSortType);
                        }
                    });
            mSnackbar.show();
        }
    }

    @Override
    public void updateRecyclerview(ArrayList<MovieModel> movieItemArrayList) {
        movieAdapter.update(movieItemArrayList);
        if(listState!=null){
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    private void showDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MovieActivity.this).create();
        alertDialog.setTitle(getString(R.string.missing_apikey));
        alertDialog.setMessage(getString(R.string.insert_apikey));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
