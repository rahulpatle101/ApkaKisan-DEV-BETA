package com.apkakisan.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.apkakisan.myapplication.databinding.ActivityMainContainerBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainContainer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.
//                }
//                return false;
//            }
//        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Activity selectedActivity = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedActivity = new HomeActivity();
//                        Toast.makeText(MainContainer.this, "HOME SELECTED", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.orders:
                        selectedActivity = new OrdersActivity();
//                        Toast.makeText(MainContainer.this, "ORDER SELECTED", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notifications:
                        selectedActivity = new NotificationsActivity();
//                        Toast.makeText(MainContainer.this, "NOTIFICATION SELECTED", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        selectedActivity = new ProfileActivity();
//                        Toast.makeText(MainContainer.this, "PROFILE SELECTED", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

}