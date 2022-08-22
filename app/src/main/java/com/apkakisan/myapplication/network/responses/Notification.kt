package com.apkakisan.myapplication.network.responses

import com.apkakisan.myapplication.utils.LanguageUtil

data class Notification(
    var id: String = "",
    var typeEn: String = "",
    var typeHi: String = "",
    var titleEn: String = "",
    var titleHi: String = "",
    var descriptionEn: String = "",
    var descriptionHi: String = "",
    var createdDate: String = "",
    var userId: String = "",
    var orderId: String = ""
) {
    val type: String get() = if (LanguageUtil.isEnglish()) typeEn else typeHi
    val title: String get() = if (LanguageUtil.isEnglish()) titleEn else titleHi
    val description: String get() = if (LanguageUtil.isEnglish()) descriptionEn else descriptionHi
}
