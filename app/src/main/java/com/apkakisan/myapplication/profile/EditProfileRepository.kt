package com.apkakisan.myapplication.profile

import android.net.Uri
import com.apkakisan.myapplication.BaseRepository
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.network.FirebaseDataSource

class EditProfileRepository(
    private val firebaseDataSource: FirebaseDataSource
) : BaseRepository(firebaseDataSource) {

    suspend fun uploadPhoto(
        userId: String,
        photoUri: Uri
    ): Boolean {
        val photoUrl = firebaseDataSource.uploadPhoto(userId, photoUri)
        var isPhotoUpdated = false
        if (photoUrl.isNotEmpty()) {
            isPhotoUpdated = firebaseDataSource.updateUserPhoto(userId, photoUrl)
            if (isPhotoUpdated) LocalStore.user?.photo = photoUrl
        }
        return isPhotoUpdated
    }

    suspend fun updateUser(
        userId: String,
        name: String,
        phoneNo: String,
        address: String
    ): Boolean {
        val isUpdated = firebaseDataSource.updateUser(userId, name, phoneNo, address)
        if (isUpdated)
            LocalStore.user?.let {
                it.fullName = name
                it.phoneNumber = phoneNo
                it.location = address
            }
        return isUpdated
    }
}
