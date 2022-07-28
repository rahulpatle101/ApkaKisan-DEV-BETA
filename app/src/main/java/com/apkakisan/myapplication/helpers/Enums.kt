package com.apkakisan.myapplication.helpers

enum class CountryCode(val countryCode: String) {
    PAKISTAN("+92"),
    INDIA("+91")
}

enum class OrderStatus {
    RECEIVED,
    CONFIRMED,
    INSPECTED,
    COMPLETED
}