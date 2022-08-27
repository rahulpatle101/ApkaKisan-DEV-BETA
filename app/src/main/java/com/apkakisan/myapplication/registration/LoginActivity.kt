package com.apkakisan.myapplication.registration

import androidx.appcompat.app.AppCompatActivity
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
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Pair
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.databinding.ActivityLoginBinding
import com.apkakisan.myapplication.helpers.*
import com.apkakisan.myapplication.utils.BuildTypeUtil
import com.apkakisan.myapplication.utils.PhoneNoUtil
import org.koin.android.ext.android.get

class LoginActivity : AppCompatActivity() {

    private lateinit var callSignUp: Button
    private lateinit var loginBtn: Button
    private lateinit var image: ImageView
    private lateinit var welcomeText: TextView
    private lateinit var sloganText: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window?.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutPhone.etPhoneNo.hint = getString(R.string.phone_no)

        progressBar = findViewById(R.id.progressBar)
        callSignUp = findViewById(R.id.signup_screen)
        image = findViewById(R.id.logo_image)
        welcomeText = findViewById(R.id.welcome_text)
        sloganText = findViewById(R.id.slogan_name)

        if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration())
            binding.layoutPhone.tvCountryCode.text = CountryCode.PAKISTAN.countryCode

        if (BuildTypeUtil.isDebugWithRegistration())
            binding.layoutPhone.etPhoneNo.setText(PhoneNoUtil.format10DigitToUS(PHONE_PK))

        binding.layoutPhone.etPhoneNo.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(
                PHONE_COUNTRY_CODE_FORMAT
            )
        )
        binding.layoutPhone.etPhoneNo.doAfterTextChanged {
            binding.tvPhoneError.visibility = View.GONE
        }

        loginBtn = findViewById(R.id.login_btn)
    }

    private fun validatePhoneNo(): Boolean {
        val usFormattedPhoneNo = binding.layoutPhone.etPhoneNo.text.toString()
        val phoneNo = PhoneNoUtil.formatUSTo10Digit(usFormattedPhoneNo)
        if (phoneNo.isEmpty()) {
            binding.tvPhoneError.visibility = View.VISIBLE
            binding.tvPhoneError.text = getString(R.string.empty_phone)
            return false
        }
        if (phoneNo.length < 10) {
            binding.tvPhoneError.visibility = View.VISIBLE
            binding.tvPhoneError.text = getString(R.string.invalid_phone)
            return false
        }
        return true
    }

    fun loginUser(view: View?) {
        hideKeyboard()
        if (validatePhoneNo()) {
            isUser()
        }
    }

    private fun isUser() {
        progressBar.visibility = View.VISIBLE
        val userEnteredPhoneNumber =
            PhoneNoUtil.formatForServer(binding.layoutPhone.etPhoneNo.text.toString())

        val reference = FirebaseDatabase.getInstance().getReference("User")
        val checkUser = reference.orderByChild("phoneNumber").equalTo(userEnteredPhoneNumber)
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                if (dataSnapshot.exists()) {
                    var user: User? = null
                    for (userSnapshot in dataSnapshot.children)
                        user = userSnapshot.getValue(User::class.java)

                    val intent = Intent(this@LoginActivity, VerifyPhoneNoActivity::class.java)
                    intent.putExtra(USER, user)
                    startActivity(intent)
                } else {
                    binding.layoutPhone.etPhoneNo.error = getString(R.string.user_not_found)
                    binding.layoutPhone.etPhoneNo.requestFocus()
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
        pairs[3] = Pair<View, String>(binding.layoutPhone.etPhoneNo, "username_tran")
        pairs[4] = Pair<View, String>(loginBtn, "button_tran")
        pairs[5] = Pair<View, String>(callSignUp, "login_signup_tran")

        //Call next activity by attaching the animation with it.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity, *pairs)
            startActivity(intent, options.toBundle())
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}