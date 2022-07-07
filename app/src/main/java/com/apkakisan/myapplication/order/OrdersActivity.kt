package com.apkakisan.myapplication.order

import android.os.Bundle
import android.view.View
import com.apkakisan.myapplication.R
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.databinding.ActivityOrdersBinding
import com.apkakisan.myapplication.helpers.replaceFragment
import com.google.android.material.tabs.TabLayout

class OrdersActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private val activeOrderFragment = ActiveOrderFragment.newInstance()
    private val orderHistoryFragment = OrderHistoryFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.ibBack.visibility = View.GONE
        binding.toolbar.tvTitle.text = getString(R.string.orders)

        bottomNavigation = findViewById(R.id.layoutBottom)
        bottomNavigation.selectedItemId = R.id.orders
        setupBottomNavigation()

        binding.tlTabs.getTabAt(0)?.customView?.findViewById<TextView>(R.id.tvTabName)
            ?.setTextColor(ContextCompat.getColor(this@OrdersActivity, R.color.green))

        tabLayoutProcess()

        replaceFragment(
            activeOrderFragment,
            false,
            ActiveOrderFragment.TAG
        )
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