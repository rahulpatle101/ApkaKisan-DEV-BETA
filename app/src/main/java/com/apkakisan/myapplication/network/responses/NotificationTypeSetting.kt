package com.apkakisan.myapplication.network.responses

import com.google.gson.annotations.SerializedName
import java.util.*

data class NotificationTypeSetting(

    @SerializedName("id")
    var id: String = UUID.randomUUID().toString(),

    @SerializedName("notificationTypeId")
    var notificationTypeId: String = "",

    @SerializedName("isChecked")
    var isChecked: Boolean = false,

    @SerializedName("userId")
    var userId: String = ""
)
