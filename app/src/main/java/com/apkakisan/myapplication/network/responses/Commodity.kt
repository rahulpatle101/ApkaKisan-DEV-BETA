package com.apkakisan.myapplication.network.responses

import com.google.gson.annotations.SerializedName

class Commodity(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("mandi_price")
    val mandiPrice: Int = 0,

    @SerializedName("apkakisan_price")
    val apkakisanPrice: Int = 0
)