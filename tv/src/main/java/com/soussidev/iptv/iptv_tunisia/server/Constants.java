package com.soussidev.iptv.iptv_tunisia.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Soussi on 10/02/2018.
 */

public class Constants {

    public static final String BASE_URL = "http://10.0.2.2:8080/";
  //  public static final String BASE_URL = "http://192.168.0.50:8080/";
//    public static final String GET_CHANNEL_OPERATION = "getChannel";
//    public static final String GET_CATEGORY_OPERATION = "getCategory";
//    public static final String GET_LANGAGE_OPERATION = "getLangage";
//    public static final String GET_VOD_OPERATION = "get_VOD";
//    public static final String GET_PROGRAMME_OPERATION = "getProgramme";
    public static final String NAME_PATH ="IPTVTunisia/";

    public static final String SOURCE_VOD_EN_LIGNE ="EnLigne";
    public static final String SOURCE_VOD_EN_LOCAL ="EnLocal";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final String TAG = "IPTVTunisia";

    private static Retrofit retrofit = null;

    public enum GET_OPERATION{
        getChannel ,getCategory ,getLangage ,get_VOD , getProgramme
    }

    public enum SourceUrl{
        Enligne,Enlocal
    }

    /**
     * @auteur Soussi Mohamed
     * @see Retrofit
     *
     */

    public static Retrofit getClient() {

        OkHttpClient client = new OkHttpClient.Builder()

                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }



}
