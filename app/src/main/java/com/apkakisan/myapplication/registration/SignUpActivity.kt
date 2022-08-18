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
import android.content.Intent
import android.graphics.Color
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.databinding.ActivitySignUpBinding
import com.apkakisan.myapplication.domainlayer.TermsAndPrivacyManager
import com.apkakisan.myapplication.helpers.*
import com.apkakisan.myapplication.utils.BuildTypeUtil
import com.apkakisan.myapplication.utils.LanguageUtil
import com.apkakisan.myapplication.utils.PhoneNoUtil
import com.google.firebase.database.DatabaseError
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var regName: TextInputLayout
    private lateinit var regPinCode: TextInputLayout
    private lateinit var regLocation: TextInputLayout
    private lateinit var regBtn: Button
    private lateinit var checkBox: CheckBox

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //This Line will hide the status bar from the screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        regName = findViewById(R.id.reg_name)
        regPinCode = findViewById(R.id.reg_pincode)
        regLocation = findViewById(R.id.reg_location)
        checkBox = findViewById(R.id.checkBox)

        if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration()) {
            binding.layoutPhone.tvCountryCode.text = CountryCode.PAKISTAN.countryCode
            binding.layoutConfirmPhone.tvCountryCode.text = CountryCode.PAKISTAN.countryCode
        }

        if (BuildTypeUtil.isDebugWithRegistration()) {
            regName.editText?.setText("Muhammad Omer Saleem")
            binding.layoutPhone.etPhoneNo.setText(PhoneNoUtil.format10DigitToUS(PHONE_PK))
            binding.layoutConfirmPhone.etPhoneNo.setText(PhoneNoUtil.format10DigitToUS(PHONE_PK))
            regPinCode.editText?.setText("123456")
            regLocation.editText?.setText("Lahore, Pakistan")
            checkBox.isChecked = true
        }

        binding.layoutPhone.etPhoneNo.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(
                PHONE_COUNTRY_CODE_FORMAT
            )
        )
        binding.layoutPhone.etPhoneNo.doAfterTextChanged {
            binding.tvPhoneError.visibility = View.GONE
        }

        binding.layoutConfirmPhone.etPhoneNo.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(
                PHONE_COUNTRY_CODE_FORMAT
            )
        )
        binding.layoutConfirmPhone.etPhoneNo.doAfterTextChanged {
            binding.tvConfirmPhoneError.visibility = View.GONE
        }

        TermsAndPrivacyManager().formatTermsAndPolicyString(this, binding.tvTermsPrivacy)

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
                PhoneNoUtil.formatForServer(binding.layoutPhone.etPhoneNo.text.toString())

            val reference = FirebaseDatabase.getInstance().getReference("User")
            val checkUser = reference.orderByChild("phoneNumber").equalTo(userEnteredPhoneNumber)
            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        binding.layoutPhone.etPhoneNo.error = getString(R.string.user_exists)
                        binding.layoutPhone.etPhoneNo.requestFocus()
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
                    println("Database Error: Line 120 in SignupJava, on data changed else condition")
                }
            })
        })

        findViewById<Button>(R.id.reg_login_btn).apply {
            if (LanguageUtil.isHindi())
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            setOnClickListener {
                hideKeyboard()
                finish()
            }
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
                regPinCode.error = getString(R.string.invalid_pincode)
                false
            }
        } else {
            regPinCode.error = getString(R.string.empty_field)
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
            regLocation.error = getString(R.string.empty_field)
            false
        }
    }

    private fun validateTermsAndCondition(): Boolean {
        return if (!checkBox.isChecked) {
            checkBox.error = getString(R.string.empty_field)
            checkBox.setTextColor(Color.parseColor("#FB4141"))
            false
        } else {
            checkBox.setTextColor(Color.parseColor("#171725"))
            checkBox.error = null
            true
        }
    }

    private fun validatePhoneNo(): Boolean {
        val usFormattedPhoneNo = binding.layoutPhone.etPhoneNo.text.toString()
        val phoneNo = PhoneNoUtil.formatUSTo10Digit(usFormattedPhoneNo)

        val usFormattedConfirmedPhoneNo = binding.layoutConfirmPhone.etPhoneNo.text.toString()
        val confirmedPhoneNo = PhoneNoUtil.formatUSTo10Digit(usFormattedConfirmedPhoneNo)

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
        if (confirmedPhoneNo.isEmpty()) {
            binding.tvConfirmPhoneError.visibility = View.VISIBLE
            binding.tvConfirmPhoneError.text = getString(R.string.empty_confirmed_phone)
            return false
        }
        if (confirmedPhoneNo.length < 10) {
            binding.tvConfirmPhoneError.visibility = View.VISIBLE
            binding.tvConfirmPhoneError.text = getString(R.string.invalid_confirmed_phone)
            return false
        }
        if (phoneNo != confirmedPhoneNo) {
            binding.tvPhoneError.visibility = View.GONE
            binding.tvConfirmPhoneError.visibility = View.VISIBLE
            binding.tvConfirmPhoneError.text = getString(R.string.phone_confirmed_not_matched)
            return false
        }

        return true
    }
}