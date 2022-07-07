package com.apkakisan.myapplication.registration

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import android.widget.CheckBox
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.view.WindowManager
import com.apkakisan.myapplication.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import android.widget.Toast
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Button
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.helpers.USER
import com.apkakisan.myapplication.helpers.hideKeyboard
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.utils.BuildTypeUtil
import com.google.firebase.database.DatabaseError
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var regName: TextInputLayout
    private lateinit var regPhoneNo: TextInputLayout
    private lateinit var regPhoneNoConfirmation: TextInputLayout
    private lateinit var regPinCode: TextInputLayout
    private lateinit var regLocation: TextInputLayout
    private lateinit var regBtn: Button
    private lateinit var regToLoginBtn: Button
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //This Line will hide the status bar from the screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        regName = findViewById(R.id.reg_name)
        regPhoneNo = findViewById(R.id.reg_phoneNo)
        regPhoneNoConfirmation = findViewById(R.id.reg_phoneNoConfirmation)
        regPinCode = findViewById(R.id.reg_pincode)
        regLocation = findViewById(R.id.reg_location)
        checkBox = findViewById(R.id.checkBox)

        if (BuildTypeUtil.isDebugWithRegistration()) {
            regName.editText?.setText("Muhammad Omer Saleem")
            regPhoneNo.editText?.setText("3234364949")
            regPhoneNoConfirmation.editText?.setText("3234364949")
            regPinCode.editText?.setText("123456")
            regLocation.editText?.setText("Lahore, Pakistan")
            checkBox.isChecked = true
        }

        regBtn = findViewById(R.id.reg_btn)
        regBtn.setOnClickListener(View.OnClickListener {
            hideKeyboard()
            val now = SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z", Locale.getDefault())
            val currentDateAndTime = now.format(Date())

            if (!validateName()
                or !validatePinCode()
                or !validateLocation()
                or !validatePhoneNo()
                or !validateTermsAndCondition()
            ) {
                return@OnClickListener
            }

            val userEnteredPhoneNumber =
                if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration())
                    "+92${regPhoneNo.editText?.text.toString().trim()}"
                else
                    "+91${regPhoneNo.editText?.text.toString().trim()}"

            //Set Firebase Root reference
            val reference = FirebaseDatabase.getInstance().getReference("User")
            val checkUser = reference.child("phoneNumber").equalTo(userEnteredPhoneNumber)
            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        regPhoneNo.error = "User already exists. Please Sign in."
                        regPhoneNo.requestFocus()
                    } else {
                        val fullNameValue = regName.editText?.text.toString()
                        val pinCodeValue = regPinCode.editText?.text.toString()
                        val locationValue = regLocation.editText?.text.toString()

                        val user = User().apply {
                            userId = UUID.randomUUID().toString()
                            fullName = fullNameValue
                            phoneNumber = userEnteredPhoneNumber
                            pinCode = pinCodeValue
                            location = locationValue
                            createdDate = currentDateAndTime
                            modifiedDate = currentDateAndTime
                            photo = ""
                        }

                        val intent = Intent(this@SignUpActivity, VerifyPhoneNoActivity::class.java)
                        intent.putExtra(USER, user)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showShortToast("------->>> Database Error: Line 120 in SignupJava, on data changed else condition")
                }
            })
        })

        regToLoginBtn = findViewById(R.id.reg_login_btn)
        regToLoginBtn.setOnClickListener {
            hideKeyboard()
            finish()
        }
    }

    private fun validateName(): Boolean {
        val name = regName.editText?.text.toString()
        return if (name.isEmpty()) {
            regName.error = "Field cannot be empty"
            false
        } else {
            regName.error = null
            regName.isErrorEnabled = false
            true
        }
    }

    private fun validatePinCode(): Boolean {
        val temp = regPinCode.editText?.text.toString()
        return if (temp.isNotEmpty()) {
            if (temp.length == 6) {
                regPinCode.error = null
                regPinCode.isErrorEnabled = false
                true
            } else {
                regPinCode.error = "Please provide a valid Pin Code."
                false
            }
        } else {
            regPinCode.error = "Field cannot be empty"
            false
        }
    }

    private fun matchPhoneNumber(): Boolean {
        val temp = regPhoneNoConfirmation.editText?.text.toString()
        val tempConfirm = regPhoneNo.editText?.text.toString()

//        Convert Numbers to string and use compareTo() method in the string class. compareTo() method returns 0 if both strings are same, else returns 1 or -1.
        return if (temp.compareTo(tempConfirm) == 0) {
            regPhoneNoConfirmation.error = null
            regPhoneNoConfirmation.isErrorEnabled = false
            regPhoneNo.error = null
            regPhoneNo.isErrorEnabled = false
            true
        } else {
            regPhoneNoConfirmation.error =
                "Phone numbers are not the same. Please enter same numbers"
            regPhoneNo.error = "Phone Numbers not the same"
            false
        }
    }

    private fun validateLocation(): Boolean {
        val temp = regLocation.editText?.text.toString()
        return if (temp.isNotEmpty()) {
            regLocation.error = null
            regLocation.isErrorEnabled = false
            true
        } else {
            regLocation.error = "Field cannot be empty"
            false
        }
    }

    private fun validateTermsAndCondition(): Boolean {
        return if (!checkBox.isChecked) {
            checkBox.error = "Field cannot be empty"
            checkBox.setTextColor(Color.parseColor("#FB4141"))
            false
        } else {
            checkBox.setTextColor(Color.parseColor("#171725"))
            checkBox.error = null
            true
        }
    }

    private fun validatePhoneNo(): Boolean {
        val phoneNo = regPhoneNo.editText?.text.toString()
        val confirmPhoneNo = regPhoneNoConfirmation.editText?.text.toString()
        return if (phoneNo.isNotEmpty()) {
            if (phoneNo.length == 10 && confirmPhoneNo.length == 10) {
                if (matchPhoneNumber()) {
                    regPhoneNoConfirmation.error = null
                    regPhoneNoConfirmation.isErrorEnabled = false
                    regPhoneNo.error = null
                    regPhoneNo.isErrorEnabled = false
                    true
                } else {
                    regPhoneNoConfirmation.error = "Phone number don't match"
                    regPhoneNo.error = "Phone number don't match"
                    false
                }
            } else {
                regPhoneNoConfirmation.error = "Please enter valid phone number"
                regPhoneNo.error = "Please enter valid phone number"
                false
            }
        } else {
            regPhoneNo.error = "Field cannot be empty"
            false
        }
    }
}