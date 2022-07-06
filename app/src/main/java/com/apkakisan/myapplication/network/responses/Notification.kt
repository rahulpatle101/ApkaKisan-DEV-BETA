package com.apkakisan.myapplication.network.responses

import com.google.gson.annotations.SerializedName

data class Notification(

    @SerializedName("id")
    var id: String = "",

    @SerializedName("type")
    var type: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("createdDate")
    var createdDate: String = "",

    @SerializedName("userId")
    var userId: String = "",

    @SerializedName("orderId")
    var orderId: String = ""
)
