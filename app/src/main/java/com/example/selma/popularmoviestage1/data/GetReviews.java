package com.example.selma.popularmoviestage1.data;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.selma.popularmoviestage1.application.AppController;
import com.example.selma.popularmoviestage1.data.model.ReviewModel;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetReviews {

    private IApiReviewResponse mListener;

    public GetReviews(Context context, String url) {
        this.mListener = (IApiReviewResponse) context;
        getReviewList(url);
    }

    public interface IApiReviewResponse{
        void onSuccess(ArrayList<ReviewModel> reviewModelArrayList);
        void onError(String error);
    }

    private void getReviewList(String url) {
        StringRequest getReviewList = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("review response", response);
                        //parse data from response and return array
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            Moshi moshi = new Moshi.Builder().build();
                            JsonAdapter<ReviewModel> jsonAdapter = moshi.adapter(ReviewModel.class);

                            ArrayList<ReviewModel> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String reviewObject = jsonArray.getJSONObject(i).toString();
                                try {
                                    ReviewModel reviewItem = jsonAdapter.fromJson(reviewObject);
                                    arrayList.add(reviewItem);
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
        requestQueue.add(getReviewList);
    }
}
