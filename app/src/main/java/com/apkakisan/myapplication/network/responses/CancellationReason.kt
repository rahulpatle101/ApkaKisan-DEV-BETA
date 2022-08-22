package com.apkakisan.myapplication.network.responses

import com.apkakisan.myapplication.utils.LanguageUtil
import com.google.gson.annotations.SerializedName

data class CancellationReason(
    @SerializedName("cancellation_reason_en")
    val cancellationReasonEn: String = "",

    @SerializedName("cancellation_reason_hi")
    val cancellationReasonHi: String = "",

    var isSelected: Boolean = false
){
    val cancellationReason: String get() = if (LanguageUtil.isEnglish()) cancellationReasonEn else cancellationReasonHi
}