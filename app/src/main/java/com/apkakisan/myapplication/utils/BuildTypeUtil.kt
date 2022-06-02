package com.apkakisan.myapplication.utils

import com.apkakisan.myapplication.BuildConfig

object BuildTypeUtil {

    fun isDebug(): Boolean {
        return BuildConfig.BUILD_TYPE == "debug"
    }

    fun isDebugWithRegistration(): Boolean {
        return BuildConfig.BUILD_TYPE == "debug_with_registration"
    }

    fun isRahul(): Boolean {
        return BuildConfig.BUILD_TYPE == "rahul"
    }
}