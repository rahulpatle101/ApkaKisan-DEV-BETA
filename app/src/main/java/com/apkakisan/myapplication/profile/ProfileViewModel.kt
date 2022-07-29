package com.apkakisan.myapplication.profile

import android.telephony.PhoneNumberUtils
import com.apkakisan.myapplication.BaseViewModel
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.utils.BuildTypeUtil

class ProfileViewModel(
    private val repository: ProfileRepository
) : BaseViewModel(repository) {

    val user: User = LocalStore.getUser()!!

    fun getFormattedPhone(): String {
        var phone = ""
        user.phoneNumber?.let {
            phone = it.substring(3, it.length)
            phone = PhoneNumberUtils.formatNumber(phone, "US")
            if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration())
                phone = "+92 $phone"
            if (BuildTypeUtil.isRahul())
                phone = "+91 $phone"
        }
        return phone
    }
}