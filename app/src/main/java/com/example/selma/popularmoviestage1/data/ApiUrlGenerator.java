package com.example.selma.popularmoviestage1.data;


import com.example.selma.popularmoviestage1.BuildConfig;

public class ApiUrlGenerator {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String MOST_POPULAR = "/popular";
    public static final String HIGHEST_RATED = "/top_rated";
    public static final String REVIEWS = "/reviews";
    public static final String VIDEOS = "/videos";

    public static String getApiUrl(String endpoint) {

        if (endpoint == "") try {
            throw new Exception("Endpoint can not be empty string!");
        } catch (Exception e) {
            e.printStackTrace();
            //TODO notiffy user that something went wrong
        }

        return BASE_URL + endpoint + "?api_key=" + BuildConfig.TheMovieApiKey;
    }

    public static String getUrl(String endpoint, String movieID) {

        if (endpoint == "") try {
            throw new Exception("Endpoint can not be empty string!");
        } catch (Exception e) {
            e.printStackTrace();
            //TODO notiffy user that something went wrong
        }



        return BASE_URL +"/"+ movieID + endpoint + "?api_key=" + BuildConfig.TheMovieApiKey;
    }
}
