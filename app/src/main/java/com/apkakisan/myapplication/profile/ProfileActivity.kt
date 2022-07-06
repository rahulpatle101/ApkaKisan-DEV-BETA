package com.apkakisan.myapplication.profile

import com.apkakisan.myapplication.BaseActivity
import android.os.Bundle
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.helpers.addFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : BaseActivity() {

    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        addFragment(
            ProfileFragment.newInstance(),
            false,
            ProfileFragment.TAG
        )

//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.home:
////                        Toast.makeText(MainContainer.this, "HOME SELECTED", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    overridePendingTransition(0, 0);
//                    return true;
//                case R.id.orders:
////                        Toast.makeText(MainContainer.this, "ORDER SELECTED", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
//                    overridePendingTransition(0, 0);
//                    return true;
//                case R.id.notifications:
////                        Toast.makeText(MainContainer.this, "NOTIFICATION SELECTED", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
//                    overridePendingTransition(0, 0);
//                    return true;
//                case R.id.profile:
////                        Toast.makeText(MainContainer.this, "PROFILE SELECTED", Toast.LENGTH_SHORT).show();
//                    return true;
//            }
//            return false;
//        });
    }
}