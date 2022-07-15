package com.apkakisan.myapplication.registration

import com.apkakisan.myapplication.BaseRepository
import com.apkakisan.myapplication.network.FirebaseDataSource

class VerifyPhoneNoRepository(
    private val firebaseController: FirebaseDataSource
) : BaseRepository(firebaseController)
