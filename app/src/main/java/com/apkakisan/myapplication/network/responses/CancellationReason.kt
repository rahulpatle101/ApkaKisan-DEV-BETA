package com.apkakisan.myapplication.network.responses

import com.google.gson.annotations.SerializedName

class CancellationReason {
    @SerializedName("cancellation_reason")
    val cancellationReason: String = ""

    var isSelected = false
}