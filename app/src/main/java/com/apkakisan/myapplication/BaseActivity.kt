package com.apkakisan.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.contracts.UIInterface
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.helpers.somethingWentWrong
import com.apkakisan.myapplication.notifications.NotificationActivity
import com.apkakisan.myapplication.order.HomeActivity
import com.apkakisan.myapplication.order.OrdersActivity
import com.apkakisan.myapplication.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

abstract class BaseActivity : AppCompatActivity(), UIInterface {

    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window?.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    protected fun hideStatusBar() {
        //This Line will hide the status bar from the screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    protected fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
                R.id.orders -> {
                    startActivity(Intent(this, OrdersActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
                R.id.notifications -> {
                    startActivity(Intent(this, NotificationActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
                R.id.profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    override fun showLoadingView() {}

    override fun showContentView() {}

    override fun showEmptyView() {
        showShortToast(getString(R.string.no_result_found))
    }

    override fun showErrorView() {
        somethingWentWrong()
    }
}