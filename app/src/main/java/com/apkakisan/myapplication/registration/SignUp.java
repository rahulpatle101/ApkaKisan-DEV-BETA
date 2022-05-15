package com.apkakisan.myapplication.registration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apkakisan.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    //Variables
    TextInputLayout regName, regPhoneNo, regPhoneNoConfirmation, regPincode, regLocation;
    Button regBtn, regToLoginBtn;
    CheckBox checkBox;
    ProgressBar progressBar;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This Line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        //Firebase Instantiation
//        rootNode = FirebaseDatabase.getInstance();
//        reference = rootNode.getReference("users");

        //Hooks to all xml elements in activity_sign_up.xml
        regName = findViewById(R.id.reg_name);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPhoneNoConfirmation = findViewById(R.id.reg_phoneNoConfirmation);
        regPincode = findViewById(R.id.reg_pincode);
        regLocation = findViewById(R.id.reg_location);
        checkBox = findViewById(R.id.checkBox);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);

        regBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("USERS");

                SimpleDateFormat now = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z");
                String currentDateandTime = now.format(new Date());

//                Performing Validation by calling validation functions
                if (!validateName() | !validatePinCode() | !validateLocation() | !validatePhoneNo() | !validateTermsAndCondition()) {

                    return;
                }


                //Get users Field values
                final String userEnteredPhoneNumber = regPhoneNo.getEditText().getText().toString().trim();
                //Set Firebase Root reference
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USERS");
                Query checkUser = reference.orderByChild("phoneNumber").equalTo(userEnteredPhoneNumber);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Toast.makeText(SignUp.this, "User exist. Please Sign in.", Toast.LENGTH_SHORT).show();

//                            progressBar.setVisibility(View.GONE);
                            regPhoneNo.setError("User exist. Please Sign in.");
                            regPhoneNo.requestFocus();

                        } else {

                            // Get all the values
                            String fullName = regName.getEditText().getText().toString();
                            String phone_number_id = regPhoneNo.getEditText().getText().toString();
//                int phoneNumber = Integer.parseInt(phone_number_id);
                            String pinCode = regPincode.getEditText().getText().toString();
                            String location = regLocation.getEditText().getText().toString();
                            String createdDate = currentDateandTime;
                            String modifiedDate = currentDateandTime;

//                Toast.makeText(SignUp.this, "ph"+phone_number_id, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                            intent.putExtra("name", fullName);
//                intent.putExtra("phoneNo", phoneNumber);
                            intent.putExtra("phoneNoId", phone_number_id);
                            intent.putExtra("pincode", pinCode);
                            intent.putExtra("location", location);
                            intent.putExtra("createdDate", createdDate);
                            intent.putExtra("modifiedDate", modifiedDate);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignUp.this, "------->>> Database Error: Line 120 in SignupJava, on data changed else condition", Toast.LENGTH_SHORT).show();
                    }
                });


//                UserHelperClass helperClass = new UserHelperClass(fullName, location, phoneNumber, pinCode, createdDate, modifiedDate);
//                reference.child(phone_number_id).setValue(helperClass);

                //Toast.makeText(getApplicationContext(),"Hello " + fullName + "phonenumber " + phoneNumber + "pincode " + pinCode + "location " + location + "createdDate " + createdDate + "modifiedDate "+modifiedDate, Toast.LENGTH_LONG).show();
            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePinCode() {
        String temp = regPincode.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            if (temp.length() == 6) {
                regPincode.setError(null);
                regPincode.setErrorEnabled(false);
                return true;
            } else {
                regPincode.setError("Please provide a valid Pin Code.");
                return false;
            }
        } else {
            regPincode.setError("Field cannot be empty");
            return false;
        }
    }

//    private Boolean validateUsername() {
//        String val = regUsername.getEditText().getText().toString();
//        String noWhiteSpace = "\\A\\w{4,20}\\z";
//
//        if (val.isEmpty()) {
//            regUsername.setError("Field cannot be empty");
//            return false;
//        } else if (val.length() >= 15) {
//            regUsername.setError("Username too long");
//            return false;
//        } else if (!val.matches(noWhiteSpace)) {
//            regUsername.setError("White Spaces are not allowed");
//            return false;
//        } else {
//            regUsername.setError(null);
//            regUsername.setErrorEnabled(false);
//            return true;
//        }
//    }

    //    private Boolean validateEmail() {
//        String val = regEmail.getEditText().getText().toString();
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//
//        if (val.isEmpty()) {
//            regEmail.setError("Field cannot be empty");
//            return false;
//        } else if (!val.matches(emailPattern)) {
//            regEmail.setError("Invalid email address");
//            return false;
//        } else {
//            regEmail.setError(null);
//            regEmail.setErrorEnabled(false);
//            return true;
//        }
//    }
    private Boolean matchPhoneNumber() {
        String temp = regPhoneNoConfirmation.getEditText().getText().toString() + "";
        String temp_confirm = regPhoneNo.getEditText().getText().toString() + "";

//        Convert Numbers to string and use compareTo() method in the string class. compareTo() method returns 0 if both strings are same, else returns 1 or -1.
        if (temp.compareTo(temp_confirm) == 0) {
            regPhoneNoConfirmation.setError(null);
            regPhoneNoConfirmation.setErrorEnabled(false);
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        } else {
            regPhoneNoConfirmation.setError("Phone numbers are not the same. Please enter same numbers");
            regPhoneNo.setError("Phone Numbers not the same");
            return false;
        }

    }

    private Boolean confirmPhoneNo() {
        String temp = regPhoneNoConfirmation.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            if (matchPhoneNumber()) {
                regPhoneNoConfirmation.setError(null);
                regPhoneNoConfirmation.setErrorEnabled(false);
                regPhoneNo.setError(null);
                regPhoneNo.setErrorEnabled(false);
                return true;
            } else {
                regPhoneNoConfirmation.setError("Phone Number needs to match");
                regPhoneNo.setError("Phone Number needs to match");
                return false;
            }
        } else {
            regPhoneNoConfirmation.setError("Field cannot be empty");
            return false;
        }
    }

    private Boolean validateLocation() {
        String temp = regLocation.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            regLocation.setError(null);
            regLocation.setErrorEnabled(false);
            return true;
        } else {
            regLocation.setError("Field cannot be empty");
            return false;
        }
    }

    private Boolean validateTermsAndCondition() {

        if (!checkBox.isChecked()) {
            checkBox.setError("Field cannot be empty");
            checkBox.setTextColor(Color.parseColor("#FB4141"));
            return false;
        } else {
            checkBox.setTextColor(Color.parseColor("#171725"));
            checkBox.setError(null);
            return true;
        }
    }


    private Boolean validatePhoneNo() {
        String temp = regPhoneNo.getEditText().getText().toString();
        String tempC = regPhoneNoConfirmation.getEditText().getText().toString();

        Toast.makeText(this, "str len " + temp.length() + tempC.length(), Toast.LENGTH_SHORT).show();
        if (!temp.isEmpty()) {
            if (temp.length() == 10 && tempC.length() == 10) {
//                Toast.makeText(this, "going in "+temp.length()+tempC.length(), Toast.LENGTH_SHORT).show();

                if (matchPhoneNumber()) {
                    regPhoneNoConfirmation.setError(null);
                    regPhoneNoConfirmation.setErrorEnabled(false);
                    regPhoneNo.setError(null);
                    regPhoneNo.setErrorEnabled(false);
                    return true;
                } else {
                    regPhoneNoConfirmation.setError("Phone number don't match");
                    regPhoneNo.setError("Phone number don't match");
                    return false;
                }
            } else {
//                Toast.makeText(this, "going out "+temp.length()+tempC.length(), Toast.LENGTH_SHORT).show();
                regPhoneNoConfirmation.setError("Please enter valid phone number");
                regPhoneNo.setError("Please enter valid phone number");

                return false;
            }

        } else {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        }

//        if (Integer.parseInt(phonenumberStr) == Integer.parseInt(confirmPhonenumberStr)) {
//            regPhoneNo.setError(null);
//            regPhoneNo.setErrorEnabled(false);
//            regPhoneNoConfirmation.setError(null);
//            regPhoneNoConfirmation.setErrorEnabled(false);
//            return true;
//        }
//
//        int val = Integer.parseInt(regPhoneNo.getEditText().getText().toString());
//        Integer confirmPhone = Integer.parseInt(regPhoneNoConfirmation.getEditText().getText().toString());

//        if (val == null) {
//            regPhoneNo.setError("Field cannot be empty");
//            return false;
//        } else {
//            if (val.equals(confirmPhone)) {
//                regPhoneNo.setError(null);
//                regPhoneNo.setErrorEnabled(false);
//                return true;
//            }
//            regPhoneNoConfirmation.setError("Phone number do not match. Please re-enter same number in both fields");
//            return false;
//        }

    }

//    private Boolean validatePassword() {
//        String val = regPassword.getEditText().getText().toString();
//        String passwordVal = "^" +
//                //"(?=.*[0-9])" +         //at least 1 digit
//                //"(?=.*[a-z])" +         //at least 1 lower case letter
//                //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=\\S+$)" +           //no white spaces
//                ".{4,}" +               //at least 4 characters
//                "$";
//
//        if (val.isEmpty()) {
//            regPassword.setError("Field cannot be empty");
//            return false;
//        } else if (!val.matches(passwordVal)) {
//            regPassword.setError("Password is too weak");
//            return false;
//        } else {
//            regPassword.setError(null);
//            regPassword.setErrorEnabled(false);
//            return true;
//        }
//    }

    //This function will execute when user click on Register Button
//    public void registerUser(View view) {
//
//        Toast.makeText(getApplicationContext(),"Hello Javatpoint", Toast.LENGTH_SHORT).show();

    //Performing Validation by calling validation functions
//        if(!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()){
//            return;
//        }

    //Get all the values in String
//        String name = regName.getEditText().getText().toString();
//        String phoneNo = regPhoneNo.getEditText().getText().toString();
//        String pinCode = regPhoneNo.getEditText().getText().toString();
//        String location = regPhoneNo.getEditText().getText().toString();
//
//
//        Intent intent = new Intent(getApplicationContext(),VerifyPhoneNo.class);
//        intent.putExtra("name",name);
//        intent.putExtra("phoneNo",phoneNo);
//        intent.putExtra("pincode",pinCode);
//        intent.putExtra("location",location);
//        startActivity(intent);


//    }


}
