package com.example.selma.popularmoviestage1.data;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.selma.popularmoviestage1.application.AppController;
import com.example.selma.popularmoviestage1.data.model.TrailerModel;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetTrailers {

    private GetTrailers.IApiTrailersResponse mListener;

    public GetTrailers(Context context, String url) {
        this.mListener = (GetTrailers.IApiTrailersResponse) context;
        getTrailerList(url);
    }

    public interface IApiTrailersResponse{
        void onSuccess(ArrayList<TrailerModel> trailerModelArrayList);
        void onError(String error);
    }

    private void getTrailerList(String url) {
        StringRequest getTrailerList = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("trailer response", response);
                        //parse data from response and return array
                       try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            Moshi moshi = new Moshi.Builder().build();
                            JsonAdapter<TrailerModel> jsonAdapter = moshi.adapter(TrailerModel.class);

                            ArrayList<TrailerModel> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String trailerObject = jsonArray.getJSONObject(i).toString();
                                try {
                                    TrailerModel trailerItem = jsonAdapter.fromJson(trailerObject);
                                    arrayList.add(trailerItem);
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
        requestQueue.add(getTrailerList);
    }


}
