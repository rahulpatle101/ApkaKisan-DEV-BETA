package com.apkakisan.myapplication.notifications

import com.apkakisan.myapplication.network.FirebaseDataSource
import com.apkakisan.myapplication.network.responses.Notification

class NotificationRepository(private val firebaseController: FirebaseDataSource) {

    suspend fun getNotifications(userId: String): List<Notification>? {
        val notificationSettingList = firebaseController.getNotificationSetting(userId)
        var isInAppNotificationChecked = false
        notificationSettingList?.let {
            for (notification in it)
                if (notification.notificationTypeId == "1" && notification.isChecked) {
                    isInAppNotificationChecked = true

                }
        }
        return if (isInAppNotificationChecked)
            firebaseController.getNotifications(userId)
        else
            listOf()
    }
}
