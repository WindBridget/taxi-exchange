package com.taxiexchange.android.config.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hieu.nguyennam on 3/16/2017.
 */

public class RestClient {
    private static Retrofit mRetrofit;
    private static final String API_URI_TAXI_EXCHANGE = "http://xelienket.com";

    public static Retrofit getClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(API_URI_TAXI_EXCHANGE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
