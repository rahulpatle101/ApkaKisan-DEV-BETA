package com.apkakisan.myapplication.helpers

import com.apkakisan.myapplication.User
import com.google.gson.Gson

object LocalStore {
    private var user: User? = null

    fun setUser(user: User?) {
        var userStr: String? = null
        if (user != null) {
            userStr = Gson().toJson(user)
        }
        SecuredPreference.saveString(USER, userStr)
        this.user = user
    }

    fun getUser(): User? {
        if (user == null) {
            val userStr = SecuredPreference.getString(USER, null)
            if (userStr != null) {
                user = if (userStr.startsWith("{"))
                    Gson().fromJson(userStr, User::class.java)
                else
                    null
            }
        }
        return user
    }
}