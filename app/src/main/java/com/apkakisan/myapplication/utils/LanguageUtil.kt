package com.apkakisan.myapplication.utils

import java.util.*

object LanguageUtil {
    private lateinit var language: String

    fun getLanguage(): String {
        language = Locale.getDefault().language
        return language
    }

    fun isHindi(): Boolean {
        return getLanguage() == "hi"
    }

    fun isEnglish(): Boolean {
        return getLanguage() == "en"
    }

}