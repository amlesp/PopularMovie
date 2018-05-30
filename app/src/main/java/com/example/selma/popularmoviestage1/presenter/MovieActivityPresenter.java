package com.example.selma.popularmoviestage1.presenter;


import android.content.Context;

import com.example.selma.popularmoviestage1.R;
import com.example.selma.popularmoviestage1.Utils.AppStatus;
import com.example.selma.popularmoviestage1.Utils.MovieSortType;
import com.example.selma.popularmoviestage1.data.ApiUrlGenerator;
import com.example.selma.popularmoviestage1.data.GetFavoritesList;
import com.example.selma.popularmoviestage1.data.IApiResponse;
import com.example.selma.popularmoviestage1.data.GetMovieList;
import com.example.selma.popularmoviestage1.data.model.MovieModel;

import java.util.ArrayList;

public class MovieActivityPresenter implements IMovieActivityContract.Presenter {

    private IMovieActivityContract.View mView;

    public MovieActivityPresenter(IMovieActivityContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadMovieList(MovieSortType movieSortType) {

        if (AppStatus.getInstance((Context) mView).isOnline()) {
            String url = "";
            switch (movieSortType) {
                case MOST_POPULAR:
                    url = ApiUrlGenerator.getApiUrl(ApiUrlGenerator.MOST_POPULAR);
                    new GetMovieList(url, new IApiResponse() {
                        @Override
                        public void onSuccess(ArrayList<MovieModel> arrayList) {
                            mView.updateRecyclerview(arrayList);
                        }

                        @Override
                        public void onError(String error) {
                            mView.showError(error);
                        }
                    });
                    break;
                case HIGHEST_RATED:
                    url = ApiUrlGenerator.getApiUrl(ApiUrlGenerator.HIGHEST_RATED);
                    new GetMovieList(url, new IApiResponse() {
                        @Override
                        public void onSuccess(ArrayList<MovieModel> arrayList) {
                            mView.updateRecyclerview(arrayList);
                        }

                        @Override
                        public void onError(String error) {
                            mView.showError(error);
                        }
                    });
                    break;

            }
        } else {
            mView.showError(((Context) mView).getString(R.string.no_internet));
        }
    }

    @Override
    public void loadFavoritesList(android.support.v4.app.LoaderManager loaderManager) {

        new GetFavoritesList((Context) mView, loaderManager, new GetFavoritesList.IFavoritesList() {
            @Override
            public void onSuccess(ArrayList<MovieModel> modelArrayList) {
                mView.updateRecyclerview(modelArrayList);
            }

            @Override
            public void onError() {
                mView.showError(((Context) mView).getResources().getString(R.string.no_favorites));
            }
        });
    }
}
