package com.apkakisan.myapplication.profile

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
}