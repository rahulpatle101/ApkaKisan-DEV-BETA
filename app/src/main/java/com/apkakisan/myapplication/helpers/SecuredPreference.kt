package com.apkakisan.myapplication.helpers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SecuredPreference : KoinComponent {

    val TAG: String = SecuredPreference::class.java.simpleName

    private const val PrefName = "com.apkakisan.myapplication.secured_key_prefs_apkakisan"

    private val context by inject<Context>()

    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPref = EncryptedSharedPreferences.create(
        context,
        PrefName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun clearPreference() {
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }

    fun saveString(key: String, value: String?) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(pRef: String, defaultValue: String?): String? {
        return sharedPref.getString(pRef, defaultValue)
    }

    fun saveInt(key: String, value: Int) {
        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(pRef: String, defaultValue: Int): Int? {
        return sharedPref.getInt(pRef, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun isBoolean(pRef: String, defaultValue: Boolean): Boolean? {
        return sharedPref.getBoolean(pRef, defaultValue)
    }
}
