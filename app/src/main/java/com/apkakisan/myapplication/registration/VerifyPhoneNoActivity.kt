package com.apkakisan.myapplication.registration

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ProgressBar
import com.google.firebase.database.FirebaseDatabase
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
import androidx.lifecycle.lifecycleScope
import com.apkakisan.myapplication.order.HomeActivity
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.helpers.USER
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.network.FirebaseDataSource
import com.apkakisan.myapplication.utils.BuildTypeUtil
import com.apkakisan.myapplication.utils.DialogUtil
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.util.concurrent.TimeUnit

class VerifyPhoneNoActivity : AppCompatActivity() {

    private lateinit var verificationCodeEntered: TextInputEditText

    private val verifyPhoneNoViewModel: VerifyPhoneNoViewModel by viewModel()
    private lateinit var user: User
    private lateinit var progressBar: ProgressBar

    private var verificationCodeBySystem: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone_no)

        user = intent?.getParcelableExtra(USER)!!

        val tvPhoneNoEnteredByTheUser: TextView = findViewById(R.id.phone_no_entered_by_user)
        tvPhoneNoEnteredByTheUser.text = user.phoneNumber

        progressBar = findViewById(R.id.progress_bar)
        verificationCodeEntered = findViewById(R.id.etOtp)
        if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration())
            verificationCodeEntered.setText("123456")

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
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(user.phoneNumber!!) // Phone number to verify
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
            val credential = PhoneAuthProvider.getCredential(
                verificationCodeBySystem,
                codeByUser
            )
            signInTheUserByCredentials(credential)
        } else
            DialogUtil.alert(this, getString(R.string.something_went_wrong))
    }

    private fun signInTheUserByCredentials(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this@VerifyPhoneNoActivity) { task ->
                if (task.isSuccessful) {
                    LocalStore.user = user
                    val intent = Intent(this@VerifyPhoneNoActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    showShortToast("------>>>ERROR- Failing in adding user to the db VerifyPhone.java" + task.exception?.message)
                }
            }
    }
}