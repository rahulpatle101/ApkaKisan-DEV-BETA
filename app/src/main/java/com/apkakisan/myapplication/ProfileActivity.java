package com.apkakisan.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apkakisan.myapplication.order.HomeActivity;
import com.apkakisan.myapplication.order.OrdersActivity;
import com.apkakisan.myapplication.registration.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference database;
    String uid;
    String phoneNo;
    TextView phoneNumber, fullName;
    Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = (TextView) findViewById(R.id.full_name);
        phoneNumber = (TextView) findViewById(R.id.phone_number);
        signOutBtn = findViewById(R.id.sign_out_btn);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        database = FirebaseDatabase.getInstance().getReference("USERS");


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            phoneNo = user.getPhoneNumber();

            uid = user.getUid();
//            Toast.makeText(ProfileActivity.this, "user data -" + name + "phone " + phoneNo + "uid " + uid, Toast.LENGTH_SHORT).show();

//            String fullname = database.getChildren;
//            FirebaseDatabase.getChildren();

            getUserData();
        } else {
            Toast.makeText(ProfileActivity.this, "user isnt logged in", Toast.LENGTH_SHORT).show();
        }


        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth user;
                user = FirebaseAuth.getInstance();
                user.signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
//                        Toast.makeText(MainContainer.this, "HOME SELECTED", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.orders:
//                        Toast.makeText(MainContainer.this, "ORDER SELECTED", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notifications:
//                        Toast.makeText(MainContainer.this, "NOTIFICATION SELECTED", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
//                        Toast.makeText(MainContainer.this, "PROFILE SELECTED", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    private void getUserData() {
        Toast.makeText(ProfileActivity.this, "getting to get userdaya" + phoneNo.substring(2), Toast.LENGTH_SHORT).show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USERS");
        String strippedPhoneNumber = phoneNo.substring(2);

        Query checkUser = reference.orderByChild("phoneNumber").equalTo(strippedPhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

//                    login_phoneNo.setError(null);
//                    login_phoneNo.setErrorEnabled(false);
//                    progressBar.setVisibility(View.GONE);


                    String nameFromDB = dataSnapshot.child(strippedPhoneNumber).child("fullName").getValue(String.class);
                    String phoneNoFromDB = dataSnapshot.child(strippedPhoneNumber).child("phoneNumber").getValue(String.class);
                    String pinCode = dataSnapshot.child(strippedPhoneNumber).child("pinCode").getValue(String.class);
                    String createdDate = dataSnapshot.child(strippedPhoneNumber).child("createdDate").getValue(String.class);
                    String modifiedDate = dataSnapshot.child(strippedPhoneNumber).child("modifiedDate").getValue(String.class);
                    String location = dataSnapshot.child(strippedPhoneNumber).child("location").getValue(String.class);

                    Toast.makeText(ProfileActivity.this, "name- " + nameFromDB + "phone- " + phoneNoFromDB, Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);

                    fullName.setText(nameFromDB);
                    phoneNumber.setText(phoneNoFromDB);
//                    intent.putExtra("name", nameFromDB);
//                    intent.putExtra("phoneNoId", phoneNoFromDB);
//                    intent.putExtra("pincode", pinCode);
//                    intent.putExtra("location", location);
//                    intent.putExtra("createdDate", createdDate);
//                    intent.putExtra("modifiedDate", modifiedDate);

//                    startActivity(intent);

                } else {
                    Toast.makeText(ProfileActivity.this, "no data match", Toast.LENGTH_SHORT).show();

//                    progressBar.setVisibility(View.GONE);
//                    login_phoneNo.setError("No such User exist. Please Sign up for a new account");
//                    login_phoneNo.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}