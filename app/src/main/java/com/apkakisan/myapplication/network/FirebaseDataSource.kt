package com.apkakisan.myapplication.network

import android.net.Uri
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.network.requests.CustomerQuery
import com.apkakisan.myapplication.network.responses.Notification
import com.apkakisan.myapplication.network.responses.NotificationTypeSetting
import com.apkakisan.myapplication.network.responses.NotificationType
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSource {

    suspend fun getNotifications(userId: String): List<Notification>? {
        return try {
            val reference = FirebaseDatabase.getInstance().getReference("Notification")
            val task = reference.orderByChild("userId").equalTo(userId).get().await()
            val notificationList = mutableListOf<Notification>()
            for (postSnapshot in task.children) {
                val notification = postSnapshot.getValue(Notification::class.java)
                notificationList.add(notification!!)
            }
            notificationList.filter {
                it.type == "In App Notification"
            }
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun getNotificationType(): List<NotificationType>? {
        return try {
            val reference = FirebaseDatabase.getInstance().getReference("NotificationType")
            val task = reference.get().await()
            val notificationTypeList = mutableListOf<NotificationType>()
            for (postSnapshot in task.children) {
                val notification = postSnapshot.getValue(NotificationType::class.java)
                notificationTypeList.add(notification!!)
            }
            notificationTypeList
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun getNotificationSetting(userId: String): List<NotificationTypeSetting>? {
        return try {
            val reference = FirebaseDatabase.getInstance().getReference("NotificationTypeSetting")
            val task = reference.orderByChild("userId").equalTo(userId).get().await()
            val notificationSettingList = mutableListOf<NotificationTypeSetting>()
            for (postSnapshot in task.children) {
                val notification = postSnapshot.getValue(NotificationTypeSetting::class.java)
                notificationSettingList.add(notification!!)
            }
            notificationSettingList
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun setNotificationTypeState(notificationSetting: NotificationTypeSetting) {
        try {
            val reference = FirebaseDatabase.getInstance().getReference("NotificationTypeSetting")
            reference.child(notificationSetting.id).setValue(notificationSetting).await()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    suspend fun addUser(user: User) {
        try {
            val reference = FirebaseDatabase.getInstance().getReference("User")
            reference.child(user.userId).setValue(user).await()
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    suspend fun updateUser(
        userId: String,
        name: String,
        phoneNo: String,
        address: String
    ): Boolean {
        return try {
            val reference = FirebaseDatabase.getInstance().getReference("User")
            reference.child(userId).child("fullName").setValue(name).await()
            reference.child(userId).child("phoneNumber").setValue(phoneNo).await()
            reference.child(userId).child("location").setValue(address).await()
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun uploadPhoto(userId: String, photoUri: Uri): String =
        suspendCoroutine { continuation ->
            try {
                val ref = FirebaseStorage.getInstance().reference.child("images/$userId")
                ref.putFile(photoUri).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result.toString())
                    } else {
                        continuation.resume("")
                    }
                }
            } catch (ex: Exception) {
                continuation.resume("")
            }
        }

    suspend fun updateUserPhoto(
        userId: String,
        photo: String
    ): Boolean {
        return try {
            val reference = FirebaseDatabase.getInstance().getReference("User")
            reference.child(userId).child("photo").setValue(photo).await()
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun sendMessage(
        customerQuery: CustomerQuery
    ): Boolean {
        return try {
            val reference = FirebaseDatabase.getInstance().getReference("CustomerQuery")
            reference.child(customerQuery.queryId).setValue(customerQuery).await()
            true
        } catch (ex: Exception) {
            false
        }
    }
}