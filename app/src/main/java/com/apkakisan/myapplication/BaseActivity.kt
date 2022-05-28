package com.apkakisan.myapplication

import androidx.appcompat.app.AppCompatActivity
import com.apkakisan.myapplication.helpers.showShortToast

open class BaseActivity : AppCompatActivity() {

    protected fun comingSoon() {
        showShortToast("Coming soon")
    }
}