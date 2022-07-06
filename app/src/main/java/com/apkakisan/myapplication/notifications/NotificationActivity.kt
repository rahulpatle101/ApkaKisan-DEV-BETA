package com.apkakisan.myapplication.notifications

import android.os.Bundle
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.helpers.addFragment

class NotificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        addFragment(
            NotificationFragment.newInstance(),
            false,
            NotificationFragment.TAG
        )
    }
}