package com.apkakisan.myapplication.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://my-json-server.typicode.com/";
//    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts/rahul/";
    private static Retrofit retrofit = null;

    public static APIInterface getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(APIInterface.class);
    }
}
