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
    ): String {
        val photoUrl = firebaseDataSource.uploadPhoto(userId, photoUri)
        if (photoUrl.isNotEmpty())
            LocalStore.user?.photo = photoUrl
        return photoUrl
    }

    suspend fun updateUser(
        userId: String,
        photo: String,
        name: String,
        phoneNo: String,
        address: String
    ): Boolean {
        val isUpdated = firebaseDataSource.updateUser(userId, photo, name, phoneNo, address)
        if (isUpdated)
            LocalStore.user?.let {
                it.photo = photo
                it.fullName = name
                it.phoneNumber = phoneNo
                it.location = address
            }
        return isUpdated
    }
}
