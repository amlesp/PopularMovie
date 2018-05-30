package com.example.selma.popularmoviestage1.data;


import com.example.selma.popularmoviestage1.data.model.MovieModel;

import java.util.ArrayList;

public interface IApiResponse {
    void onSuccess(ArrayList<MovieModel> arrayList);

    void onError(String error);
}
