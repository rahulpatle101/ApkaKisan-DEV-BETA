package com.apkakisan.myapplication.registration

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import android.os.Bundle
import android.view.WindowManager
import com.apkakisan.myapplication.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import android.content.Intent
import com.google.firebase.database.DatabaseError
import android.app.ActivityOptions
import android.os.Build
import android.util.Pair
import android.view.View
import android.widget.*
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.helpers.USER
import com.apkakisan.myapplication.utils.BuildTypeUtil
import com.apkakisan.myapplication.helpers.hideKeyboard
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var callSignUp: Button
    private lateinit var loginBtn: Button
    private lateinit var image: ImageView
    private lateinit var welcomeText: TextView
    private lateinit var sloganText: TextView
    private lateinit var etPhoneNo: TextInputLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //This Line will hide the status bar from the screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        progressBar = findViewById(R.id.progressBar)
        callSignUp = findViewById(R.id.signup_screen)
        image = findViewById(R.id.logo_image)
        welcomeText = findViewById(R.id.welcome_text)
        sloganText = findViewById(R.id.slogan_name)
        etPhoneNo = findViewById(R.id.login_phoneNo)
        val etPhoneNoNew = findViewById<TextInputEditText>(R.id.etPhoneNo)
        if (BuildTypeUtil.isDebugWithRegistration())
            etPhoneNoNew.setText("3234364949")
        loginBtn = findViewById(R.id.login_btn)
    }

    private fun validatePhoneNo(): Boolean {
        val phoneNo = etPhoneNo.editText?.text.toString()
        return if (phoneNo.isNotEmpty()) {
            when {
                phoneNo.length != 10 -> {
                    etPhoneNo.error = "Please provide a valid phone number"
                    false
                }
                else -> {
                    etPhoneNo.error = null
                    etPhoneNo.isErrorEnabled = false
                    true
                }
            }
        } else {
            etPhoneNo.error = "Field cannot be empty"
            false
        }
    }

    fun loginUser(view: View?) {
        hideKeyboard()
        if (validatePhoneNo()) {
            isUser()
        }
    }

    private fun isUser() {
        progressBar.visibility = View.VISIBLE
        val userEnteredPhoneNumber = if (BuildTypeUtil.isDebugWithRegistration())
            "+92${etPhoneNo.editText?.text.toString().trim()}"
        else
            "+91${etPhoneNo.editText?.text.toString().trim()}"

        val reference = FirebaseDatabase.getInstance().getReference("User")
        val checkUser = reference.orderByChild("phoneNumber").equalTo(userEnteredPhoneNumber)
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                if (dataSnapshot.exists()) {
                    etPhoneNo.error = null
                    etPhoneNo.isErrorEnabled = false

                    var user: User? = null
                    for (userSnapshot in dataSnapshot.children)
                        user = userSnapshot.getValue(User::class.java)

                    val intent = Intent(this@LoginActivity, VerifyPhoneNoActivity::class.java)
                    intent.putExtra(USER, user)
                    startActivity(intent)
                } else {
                    etPhoneNo.error = "No such user exist. Please Sign up for a new account"
                    etPhoneNo.requestFocus()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun callSignUpScreen(view: View?) {
        hideKeyboard()
        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)

        //create pairs for animation
        val pairs = arrayOfNulls<Pair<View, String>>(6)
        pairs[0] = Pair<View, String>(image, "logo_image")
        pairs[1] = Pair<View, String>(welcomeText, "logo_text")
        pairs[2] = Pair<View, String>(sloganText, "logo_desc")
        pairs[3] = Pair<View, String>(etPhoneNo, "username_tran")
        pairs[4] = Pair<View, String>(loginBtn, "button_tran")
        pairs[5] = Pair<View, String>(callSignUp, "login_signup_tran")

        //Call next activity by attaching the animation with it.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity, *pairs)
            startActivity(intent, options.toBundle())
        }
    }
}