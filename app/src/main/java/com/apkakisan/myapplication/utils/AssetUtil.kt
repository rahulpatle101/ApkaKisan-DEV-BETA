package com.apkakisan.myapplication.utils

import android.content.Context
import java.io.IOException

object AssetUtil {

    const val FILENAME_TERMS_OF_USE = "terms-of-use-apkakisan.txt"
    const val FILENAME_PRIVACY_POLICY = "privacy-policy-apkakisan.txt"

    @Throws(IOException::class)
    fun readFile(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    }
}