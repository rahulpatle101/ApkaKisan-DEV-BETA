package com.apkakisan.myapplication.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apkakisan.myapplication.order.HomeActivity;
import com.apkakisan.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button callSignUp, login_btn;
    ImageView image;
    TextView welcomeText, sloganText;
    TextInputLayout login_phoneNo;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This Line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //All elements Hooks
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        welcomeText = findViewById(R.id.welcome_text);
        sloganText = findViewById(R.id.slogan_name);
        login_phoneNo = findViewById(R.id.login_phoneNo);
        login_btn = findViewById(R.id.login_btn);
//        loginReDirect();
    }

    public void loginReDirect() {
        user = mAuth.getCurrentUser();
        Intent intent = new Intent(Login.this, HomeActivity.class);

        if (user != null) {
            startActivity(intent);
            finish();
        }
    }


    private Boolean validatePhoneNo() {
        String temp = login_phoneNo.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            if (!(temp.length() == 10)) {
                login_phoneNo.setError("Please provide a valid phone number");
                return false;
            } else {
                login_phoneNo.setError(null);
                login_phoneNo.setErrorEnabled(false);
                return true;
            }
        } else {
            login_phoneNo.setError("Field cannot be empty");
            return false;
        }
    }

    //    private Boolean validateUsername() {
//        String val = username.getEditText().getText().toString();
//        if (val.isEmpty()) {
//            username.setError("Field cannot be empty");
//            return false;
//        } else {
//            username.setError(null);
//            username.setErrorEnabled(false);
//            return true;
//        }
//    }
//
//    private Boolean validatePassword() {
//        String val = password.getEditText().getText().toString();
//        if (val.isEmpty()) {
//            password.setError("Field cannot be empty");
//            return false;
//        } else {
//            password.setError(null);
//            password.setErrorEnabled(false);
//            return true;
//        }
//    }
//
    public void loginUser(View view) {

//        Validate Login Info
        if (!validatePhoneNo()) {
            return;
        } else {
            isUser();
        }

//        Intent intent = new Intent(getApplicationContext(),VerifyPhoneNo.class);
//        startActivity(intent);

    }

    private void isUser() {
        progressBar.setVisibility(View.VISIBLE);
        //Get users Field values
        final String userEnteredPhoneNumber = login_phoneNo.getEditText().getText().toString().trim();
//        final String userEnteredPassword = password.getEditText().getText().toString().trim();
        //Set Firebase Root reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUser = reference.orderByChild("phoneNumber").equalTo(userEnteredPhoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Toast.makeText(Login.this, "user exist", Toast.LENGTH_SHORT).show();

                    login_phoneNo.setError(null);
                    login_phoneNo.setErrorEnabled(false);
                    progressBar.setVisibility(View.GONE);


//                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
//                    if (passwordFromDB.equals(userEnteredPassword)) {
//                        username.setError(null);
//                        username.setErrorEnabled(false);


                    String nameFromDB = dataSnapshot.child(userEnteredPhoneNumber).child("fullName").getValue(String.class);
                    String phoneNoFromDB = dataSnapshot.child(userEnteredPhoneNumber).child("phoneNumber").getValue(String.class);
                    String pinCode = dataSnapshot.child(userEnteredPhoneNumber).child("pinCode").getValue(String.class);
                    String createdDate = dataSnapshot.child(userEnteredPhoneNumber).child("createdDate").getValue(String.class);
                    String modifiedDate = dataSnapshot.child(userEnteredPhoneNumber).child("modifiedDate").getValue(String.class);
                    String location = dataSnapshot.child(userEnteredPhoneNumber).child("location").getValue(String.class);


                    Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("phoneNoId", phoneNoFromDB);
                    intent.putExtra("pincode", pinCode);
                    intent.putExtra("location", location);
                    intent.putExtra("createdDate", createdDate);
                    intent.putExtra("modifiedDate", modifiedDate);

                    startActivity(intent);


//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        password.setError("Wrong Password");
//                        password.requestFocus();
//                    }


                } else {
                    Toast.makeText(Login.this, "user doesnt exist", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                    login_phoneNo.setError("No such User exist. Please Sign up for a new account");
                    login_phoneNo.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Call SignUp Screen
    public void callSignUpScreen(View view) {
        //To call next activity
//        Intent intent = new Intent(Login.this, SignUp.class);
        Intent intent = new Intent(Login.this, SignUpActivity.class);

        //create pairs for animation
        Pair[] pairs = new Pair[6];
        pairs[0] = new Pair<View, String>(image, "logo_image"); //1st one is the element and 2nd is the transition name of animation.
        pairs[1] = new Pair<View, String>(welcomeText, "logo_text");
        pairs[2] = new Pair<View, String>(sloganText, "logo_desc");
        pairs[3] = new Pair<View, String>(login_phoneNo, "username_tran");
        pairs[4] = new Pair<View, String>(login_btn, "button_tran");
        pairs[5] = new Pair<View, String>(callSignUp, "login_signup_tran");

        //Call next activity by attaching the animation with it.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());
        }
    }
}
