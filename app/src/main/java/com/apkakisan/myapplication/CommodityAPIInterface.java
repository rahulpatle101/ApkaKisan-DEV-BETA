package com.apkakisan.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommodityAPIInterface {

    @GET("/rahulpatle101/demo/commodities/")
//    @GET("/posts")

    Call<List<CommodityItem>> getCommodity_title();
}
