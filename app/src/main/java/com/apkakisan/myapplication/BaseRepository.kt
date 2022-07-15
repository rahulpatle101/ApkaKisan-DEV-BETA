package com.apkakisan.myapplication

import com.apkakisan.myapplication.network.FirebaseDataSource

abstract class BaseRepository(
    private val firebaseController: FirebaseDataSource
) {}