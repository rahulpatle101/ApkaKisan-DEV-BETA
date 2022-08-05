package com.apkakisan.myapplication.utils

import java.lang.Exception

object StringUtil {

    /**
     * returns the first index of a sub string within a string
     */
    fun findStartIndex(str: String, subStr: String): Int {
        return try {
            str.indexOf(subStr)
        } catch (ex: Exception) {
            0
        }
    }

    /**
     * startIndex: Previously received from findStartIndex function
     * returns the last index of a sub string
     */
    fun findEndIndex(startIndex: Int, subStr: String): Int {
        return startIndex + subStr.length
    }
}