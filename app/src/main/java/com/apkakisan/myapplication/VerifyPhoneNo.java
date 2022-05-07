package com.apkakisan.myapplication;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.apkakisan.myapplication.R;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity {

    //Variables for design
    String verificationCodeBySystem;
    Button verify_btn;
    TextView phoneNoEnteredByTheUser;
    ProgressBar progressBar;

    String name, username, email, phoneNo, password;
    TextInputLayout verificationCodeEntered;

    String phoneNumberFromPrevActivity;
    String phoneNumberId;
    String nameFromSignupActivity;
    String pincodeFromSignupActivity;
    String locationFromSignupActivity;
    String createdDateFromSignupActivity;
    String modifiedDateFromSignupActivity;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
//    private FirebaseDatabase rootNode;
//    private DatabaseReference reference;
    // [NEWLY Updated code  --  START declare_auth]
//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByTheUser = findViewById(R.id.phone_no_entered_by_user);
        verificationCodeEntered = findViewById(R.id.login_otp_code);

        nameFromSignupActivity = getIntent().getStringExtra("name");
//        phoneNumberFromPrevActivity = getIntent().getStringExtra("phoneNo");
        phoneNumberId = getIntent().getStringExtra("phoneNoId");
//        Integer pc = valueOf(phoneNumberId);
        pincodeFromSignupActivity = getIntent().getStringExtra("pinCode");
        locationFromSignupActivity = getIntent().getStringExtra("location");
        createdDateFromSignupActivity = getIntent().getStringExtra("createdDate");
        modifiedDateFromSignupActivity = getIntent().getStringExtra("modifiedDate");


//        Toast.makeText(this, "ph-nu" + phoneNumberId + "pcode" + phoneNumberId, Toast.LENGTH_SHORT).show();
        sendVerificationCodeToUser(phoneNumberId);


        progressBar = findViewById(R.id.progress_bar);

        //Firebase Instantiation
//        rootNode = FirebaseDatabase.getInstance();
//        reference = rootNode.getReference("users");

        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);

//        name = getIntent().getStringExtra("name");
//        username = getIntent().getStringExtra("username");
//        email = getIntent().getStringExtra("email");
//        phoneNo = getIntent().getStringExtra("phoneNo");
//        password = getIntent().getStringExtra("password");

//        sendVerificationCodeToUser(phoneNo);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = verificationCodeEntered.getEditText().getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    verificationCodeEntered.setError("Wrong OTP...");
                    verificationCodeEntered.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });

    }



    private void sendVerificationCodeToUser(String phoneNo) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+1" + phoneNo)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+1" + phoneNo,
//                60,
//                TimeUnit.SECONDS,
//                .setActivity(this),
//                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    Log.i("LOG ERROR--->>>>","inside oncodesent");
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.i("FAILED LOGIN--->>>>",e.getMessage());
//            Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String codeByUser) {
        Log.i("VERIFY CODE L 147->>>",codeByUser);

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);

    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        Log.i("VERIFY CODE L155--->>>>", "credential");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

//                            Toast.makeText(VerifyPhoneNo.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In
                            // or Create a User Account as created below

                            //Create helperclass reference and store data using firebase
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("USERS");

                            UserHelperClass addNewUser = new UserHelperClass(nameFromSignupActivity, locationFromSignupActivity, phoneNumberId, pincodeFromSignupActivity, createdDateFromSignupActivity, modifiedDateFromSignupActivity);
                            reference.child(phoneNumberId).setValue(addNewUser);

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(VerifyPhoneNo.this, "------>>>ERROR- Failing in adding user to the db VerifyPhone.java"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
