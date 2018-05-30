package com.example.selma.popularmoviestage1.data;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.selma.popularmoviestage1.application.AppController;
import com.example.selma.popularmoviestage1.data.IApiResponse;
import com.example.selma.popularmoviestage1.data.model.MovieModel;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetMovieList {

    private IApiResponse mListener;

    public GetMovieList(String url, IApiResponse iApiResponse) {
        this.mListener = iApiResponse;
        getMovieListTask(url);
    }

    private void getMovieListTask(String url) {
        StringRequest getMovieList = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        //parse data from response and return array
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            Moshi moshi = new Moshi.Builder().build();
                            JsonAdapter<MovieModel> jsonAdapter = moshi.adapter(MovieModel.class);

                            ArrayList<MovieModel> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String movieObject = jsonArray.getJSONObject(i).toString();
                                try {
                                    MovieModel movieItem = jsonAdapter.fromJson(movieObject);
                                    arrayList.add(movieItem);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            mListener.onSuccess(arrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mListener.onError(error.getMessage());
                    }
                }
        );

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(getMovieList);
    }
}
