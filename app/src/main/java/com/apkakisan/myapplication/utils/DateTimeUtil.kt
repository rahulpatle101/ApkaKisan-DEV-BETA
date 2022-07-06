package com.apkakisan.myapplication.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {
    const val DF_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val DF_NEW = "dd/MM/yyyy"

    const val DF_ORDER_OLD = "dd.MM.yyyy 'at' HH:mm:ss"
    const val DF_INS_APT_OLD = "dd/MM/yyyy"
    const val DF_ORDER_NEW = "dd MMM yyyy"
    const val DF_ORDER_DETAIL_STATUS_NEW = "hh:mm a"

    const val DF_NOTIFICATION_OLD = "dd.MM.yyyy 'at' HH:mm:ss"
    const val DF_NOTIFICATION_NEW = "dd MMM yyyy hh:mm a"

    fun convertUtcToDate(utcTimeStamp: String, oldFormat: String, newFormat: String): String? {
        val oldFormatter = SimpleDateFormat(oldFormat, Locale.getDefault())
        oldFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var newDate = ""
        try {
            val value: Date? = oldFormatter.parse(utcTimeStamp)
            val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
            newFormatter.timeZone = TimeZone.getDefault()
            newDate = newFormatter.format(value ?: Date())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return newDate
    }

    fun convertDateTimeFormat(date: String, oldFormat: String, newFormat: String): String {
        val oldFormatter = SimpleDateFormat(oldFormat, Locale.getDefault())
        return try {
            val value: Date? = oldFormatter.parse(date)
            val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
            newFormatter.timeZone = TimeZone.getDefault()
            newFormatter.format(value ?: Date())
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }
}