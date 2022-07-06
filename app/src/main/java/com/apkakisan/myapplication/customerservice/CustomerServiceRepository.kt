package com.apkakisan.myapplication.customerservice

import com.apkakisan.myapplication.BaseRepository
import com.apkakisan.myapplication.network.FirebaseDataSource
import com.apkakisan.myapplication.network.requests.CustomerQuery

class CustomerServiceRepository(
    private val firebaseController: FirebaseDataSource
) : BaseRepository(firebaseController) {

    suspend fun sendMessage(
        customerQuery: CustomerQuery
    ): Boolean {
        return firebaseController.sendMessage(customerQuery)
    }
}
