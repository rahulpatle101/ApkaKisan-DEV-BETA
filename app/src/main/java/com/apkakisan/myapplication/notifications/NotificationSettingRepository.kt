package com.apkakisan.myapplication.notifications

import com.apkakisan.myapplication.network.FirebaseDataSource
import com.apkakisan.myapplication.network.responses.NotificationTypeSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class NotificationSettingRepository(private val firebaseController: FirebaseDataSource) {

    suspend fun getNotificationType(userIdValue: String) = withContext(Dispatchers.IO) {
        val notificationTypeListDeferred = async {
            firebaseController.getNotificationType()
        }

        val notificationSettingListDeferred = async {
            firebaseController.getNotificationSetting(userIdValue)
        }

        val notificationTypeList = notificationTypeListDeferred.await()
        val notificationSettingList = notificationSettingListDeferred.await()
        if (notificationTypeList != null && notificationSettingList != null) {
            for (notificationType in notificationTypeList) {
                if (notificationSettingList.isEmpty()) {
                    NotificationTypeSetting().apply {
                        notificationTypeId = "1"
                        isChecked = true
                        userId = userIdValue
                    }.also {
                        setNotificationTypeState(it)
                        notificationType.notificationSetting = it
                    }
                }
                for (notificationSetting in notificationSettingList) {
                    if (notificationType.id == notificationSetting.notificationTypeId)
                        notificationType.notificationSetting = notificationSetting
                }
            }
        }
        notificationTypeList
    }

    suspend fun setNotificationTypeState(notificationSetting: NotificationTypeSetting) {
        firebaseController.setNotificationTypeState(notificationSetting)
    }
}
