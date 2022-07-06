package com.apkakisan.myapplication.notifications

import com.apkakisan.myapplication.network.FirebaseDataSource
import com.apkakisan.myapplication.network.responses.Notification

class NotificationRepository(private val firebaseController: FirebaseDataSource) {

    suspend fun getNotifications(userId: String): List<Notification>? {
        return firebaseController.getNotifications(userId)
    }
}
