package com.apkakisan.myapplication.helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

object JSONReaderHelper {

    const val JSONFile: String = "json_cancellation_reasons.json"

    inline fun <reified T> getJSON(context: Context, jsonFile: String): T? {
        readJSONFromAsset(context, jsonFile)?.let {
            return Gson().fromJson(it, object : TypeToken<T>() {}.type)
        }

        return null
    }

    fun readJSONFromAsset(context: Context, jsonFile: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(jsonFile)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}