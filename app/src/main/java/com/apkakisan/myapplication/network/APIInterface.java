package com.apkakisan.myapplication.network;

import com.apkakisan.myapplication.network.responses.Commodity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/rahulpatle101/demo/commodities/")
    Call<List<Commodity>> getCommodity_title();
}
