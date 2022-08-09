package com.apkakisan.myapplication.network.responses

import com.apkakisan.myapplication.utils.LanguageUtil

data class NotificationType(
    var id: String = "",
    var typeEn: String = "",
    var typeHi: String = "",
    var notificationSetting: NotificationTypeSetting = NotificationTypeSetting()
) {
    val type: String get() = if (LanguageUtil.isEnglish()) typeEn else typeHi
}
