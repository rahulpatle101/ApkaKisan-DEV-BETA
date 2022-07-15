package com.apkakisan.myapplication.network.responses

import com.google.gson.annotations.SerializedName

data class NotificationType(

    @SerializedName("id")
    var id: String = "",

    @SerializedName("type")
    var type: String = "",

    var notificationSetting: NotificationTypeSetting = NotificationTypeSetting()
)
