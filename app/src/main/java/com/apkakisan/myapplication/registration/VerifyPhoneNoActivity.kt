package com.apkakisan.myapplication.registration

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ProgressBar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.apkakisan.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.FirebaseException
import android.content.Intent
import android.view.View
import android.widget.Button
import com.apkakisan.myapplication.order.HomeActivity
import android.widget.Toast
import com.apkakisan.myapplication.UserHelperClass
import com.apkakisan.myapplication.helpers.BuildTypeUtil
import com.apkakisan.myapplication.helpers.DialogUtil
import com.google.android.material.textfield.TextInputEditText
import java.util.concurrent.TimeUnit

class VerifyPhoneNoActivity : AppCompatActivity() {

    private lateinit var verificationCodeEntered: TextInputEditText
    private lateinit var progressBar: ProgressBar

    private var phoneNo: String = ""
    private var name: String = ""
    private var pinCode: String = ""
    private var location: String = ""
    private var createdDate: String = ""
    private var modifiedDate: String = ""
    private var verificationCodeBySystem: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone_no)

        name = intent.getStringExtra("name") ?: ""
        phoneNo = intent.getStringExtra("phoneNo") ?: ""
        pinCode = intent.getStringExtra("pinCode") ?: ""
        location = intent.getStringExtra("location") ?: ""
        createdDate = intent.getStringExtra("createdDate") ?: ""
        modifiedDate = intent.getStringExtra("modifiedDate") ?: ""

        val phoneNoEnteredByTheUser: TextView = findViewById(R.id.phone_no_entered_by_user)
        phoneNo = if (BuildTypeUtil.isDebug())
            "+92$phoneNo"
        else
            "+1$phoneNo"

        phoneNoEnteredByTheUser.text = phoneNo

        progressBar = findViewById(R.id.progress_bar)
        verificationCodeEntered = findViewById(R.id.etOtp)

        val tvResendOtp = findViewById<TextView>(R.id.tvResendOtp)
        tvResendOtp.setOnClickListener { sendVerificationCodeToUser() }

        val verifyBtn = findViewById<Button>(R.id.verify_btn)
        verifyBtn.setOnClickListener(View.OnClickListener {
            val code = verificationCodeEntered.text.toString().trim()
            if (code.isEmpty() || code.length < 6) {
                verificationCodeEntered.error = "Wrong OTP..."
                verificationCodeEntered.requestFocus()
                return@OnClickListener
            }
            verifyCode(code)
        })

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener { finish() }

        sendVerificationCodeToUser()
    }

    private fun sendVerificationCodeToUser() {
        val mAuth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNo) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationCodeBySystem = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    verificationCodeEntered.setText(code)
                    progressBar.visibility = View.VISIBLE
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                DialogUtil.alert(this@VerifyPhoneNoActivity, e.message ?: "")
            }
        }

    private fun verifyCode(codeByUser: String) {
        if (verificationCodeBySystem.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            val credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser)
            signInTheUserByCredentials(credential)
        } else
            DialogUtil.alert(this, getString(R.string.something_went_wrong))
    }

    private fun signInTheUserByCredentials(credential: PhoneAuthCredential) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this@VerifyPhoneNoActivity) { task ->
                if (task.isSuccessful) {
                    //Perform Your required action here to either let the user sign In
                    // or Create a User Account as created below
                    val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val reference: DatabaseReference = rootNode.getReference("USERS")
                    val addNewUser = UserHelperClass(
                        name,
                        location,
                        phoneNo,
                        pinCode,
                        createdDate,
                        modifiedDate
                    )
                    reference.child(phoneNo).setValue(addNewUser)
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@VerifyPhoneNoActivity,
                        "------>>>ERROR- Failing in adding user to the db VerifyPhone.java" + task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}