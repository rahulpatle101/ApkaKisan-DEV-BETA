package com.apkakisan.myapplication.notifications

import com.apkakisan.myapplication.network.FirebaseDataSource
import com.apkakisan.myapplication.network.responses.NotificationTypeSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class NotificationSettingRepository(private val firebaseController: FirebaseDataSource) {

    suspend fun getNotificationType(userId: String) = withContext(Dispatchers.IO) {
        val notificationTypeListDeferred = async(Dispatchers.IO) {
            firebaseController.getNotificationType()
        }

        val notificationSettingListDeferred = async {
            firebaseController.getNotificationSetting(userId)
        }

        val notificationTypeList = notificationTypeListDeferred.await()
        val notificationSettingList = notificationSettingListDeferred.await()
        if (notificationTypeList != null && notificationSettingList != null) {
            for (notificationType in notificationTypeList) {
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
