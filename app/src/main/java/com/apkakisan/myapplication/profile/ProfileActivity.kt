package com.apkakisan.myapplication.profile

import android.content.Intent
import com.apkakisan.myapplication.BaseActivity
import android.os.Bundle
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.helpers.addFragment

class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bottomNavigation = findViewById(R.id.layoutBottom)
        bottomNavigation.selectedItemId = R.id.profile
        setupBottomNavigation()

        addFragment(
            ProfileFragment.newInstance(),
            false,
            ProfileFragment.TAG
        )
    }

    fun restartHomeActivity() {

        finish()

        val newIntent = intent
        newIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(newIntent)

//        overridePendingTransition(0, 0);
    }
}