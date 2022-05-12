package com.apkakisan.myapplication.helpers

import com.apkakisan.myapplication.BuildConfig

object BuildTypeUtil {

    fun isDebug(): Boolean {
        return BuildConfig.BUILD_TYPE == "debug"
    }

    fun isRahul(): Boolean {
        return BuildConfig.BUILD_TYPE == "rahul"
    }
}