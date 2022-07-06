package com.apkakisan.myapplication.network.requests

data class CustomerQuery(
    var queryId: String = "",
    var name: String = "",
    var phoneNo: String = "",
    var message: String = "",
)