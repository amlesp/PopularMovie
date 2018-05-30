package com.example.selma.popularmoviestage1.presenter;


import com.example.selma.popularmoviestage1.Utils.MovieSortType;
import com.example.selma.popularmoviestage1.data.model.MovieModel;

import java.util.ArrayList;

public interface IMovieActivityContract {

    interface View {

        void showError(String error);

        void updateRecyclerview(ArrayList<MovieModel> movieItemArrayList);
    }

    interface Presenter {

        void loadMovieList(MovieSortType movieSortType);

        void loadFavoritesList(android.support.v4.app.LoaderManager loaderManager);
    }
}
