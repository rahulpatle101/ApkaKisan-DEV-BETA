package com.apkakisan.myapplication.profile

import com.apkakisan.myapplication.BaseRepository
import com.apkakisan.myapplication.database.RoomDataSource
import com.apkakisan.myapplication.network.FirebaseDataSource

class ProfileRepository(
    private val firebaseController: FirebaseDataSource
) : BaseRepository(firebaseController)
