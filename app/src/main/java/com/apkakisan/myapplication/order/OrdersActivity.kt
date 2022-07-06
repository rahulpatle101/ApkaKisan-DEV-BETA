package com.apkakisan.myapplication.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apkakisan.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import android.content.Intent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.NotificationsActivity
import com.apkakisan.myapplication.profile.ProfileActivity
import com.apkakisan.myapplication.databinding.ActivityOrdersBinding
import com.apkakisan.myapplication.helpers.replaceFragment
import com.google.android.material.tabs.TabLayout

class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private val activeOrderFragment = ActiveOrderFragment.newInstance()
    private val orderHistoryFragment = OrderHistoryFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tlTabs.getTabAt(0)?.customView?.findViewById<TextView>(R.id.tvTabName)
            ?.setTextColor(ContextCompat.getColor(this@OrdersActivity, R.color.green))

        replaceFragment(
            activeOrderFragment,
            false,
            ActiveOrderFragment.TAG
        )

        tabLayoutProcess()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.layoutBottom)
        bottomNavigationView.selectedItemId = R.id.orders
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
                R.id.orders -> return@OnItemSelectedListener true
                R.id.notifications -> {
                    startActivity(Intent(applicationContext, NotificationsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
                R.id.profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    private fun tabLayoutProcess() {
        binding.tlTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    replaceFragment(
                        activeOrderFragment,
                        false,
                        ActiveOrderFragment.TAG
                    )
                } else if (tab?.position == 1) {
                    replaceFragment(
                        orderHistoryFragment,
                        false,
                        OrderHistoryFragment.TAG
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tvTabName)
                    ?.setTextColor(ContextCompat.getColor(this@OrdersActivity, R.color.colorGrey))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}