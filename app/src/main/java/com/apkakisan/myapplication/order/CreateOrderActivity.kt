package com.apkakisan.myapplication.order

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.apkakisan.myapplication.R
import android.content.Intent
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.*
import com.apkakisan.myapplication.Order
import java.text.SimpleDateFormat
import java.util.*

class CreateOrderActivity : AppCompatActivity() {

    private lateinit var sellOrderBtn: Button
    private lateinit var orderCheckBox: CheckBox
    private lateinit var sellOrderHeading: TextView
    private lateinit var pickupDateTime: TextInputLayout
    private lateinit var tiQuantity: TextInputLayout
    private lateinit var addressLocation: TextInputLayout
    private lateinit var addressStreet: TextInputLayout
    private lateinit var pinCode: TextInputLayout
    private lateinit var etCommoditydetail: TextInputLayout
    private lateinit var orderUPIPhoneNo: TextInputLayout
    private lateinit var pickupDateTimeInput: TextInputEditText
    private lateinit var tvHarvestPrice: TextView
    private lateinit var tvTotalEarning: TextView

    private var commodityNameArg: String = ""
    private var mandiPrice: String = ""
    private var apkaKisanPrice: String = ""

    private var totalEarning = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        commodityNameArg = intent?.getStringExtra("commodity_name") ?: ""
        mandiPrice = intent?.getStringExtra("mandiPrice") ?: ""
        apkaKisanPrice = intent?.getStringExtra("commodity_apka_kisan_price") ?: ""

        sellOrderHeading = findViewById(R.id.sell_order_title)

        tiQuantity = findViewById(R.id.tiQuantity)
        tiQuantity.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!char.isNullOrEmpty()) {
                    val quantity: Int = char.toString().trim().toInt()
                    apkaKisanPrice = "105"
                    totalEarning = apkaKisanPrice.toInt() * quantity
                    tvHarvestPrice.text = "Rs. $totalEarning"
                    tvTotalEarning.text = "Rs. $totalEarning"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        pickupDateTime = findViewById(R.id.pickup_date_time)
        pickupDateTimeInput = findViewById(R.id.pickup_date_time_input)
        pickupDateTimeInput.inputType = InputType.TYPE_NULL

        addressLocation = findViewById(R.id.address_location)
        addressStreet = findViewById(R.id.address_street)
        pinCode = findViewById(R.id.address_pincode)
        etCommoditydetail = findViewById(R.id.etCommoditydetail)
        tvHarvestPrice = findViewById(R.id.harvest_price)
        tvTotalEarning = findViewById(R.id.earning_price)
        orderUPIPhoneNo = findViewById(R.id.upi_phone_number)
        orderCheckBox = findViewById(R.id.order_check_box)

        sellOrderBtn = findViewById(R.id.sell_order_btn)
        sellOrderBtn.setOnClickListener {
            if (!validateQuantity()
                or !validatePinCode()
                or !validateLocation()
                or !validateStreet()
                or !validateDateTime()
                or !validateUPIPhoneNo()
                or !validateTermsAndCondition()
            ) {
                return@setOnClickListener
            }

            val user = FirebaseAuth.getInstance().currentUser

            val idOne = UUID.randomUUID()
            val idTwo = UUID.randomUUID()
            val generateUUID = idOne.toString() + idTwo.toString()

            val now = SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z", Locale.getDefault())
            val currentDateAndTime = now.format(Date())

            val orderReference = FirebaseDatabase.getInstance().getReference("ORDERS")
            Order().apply {
                phoneNo = user?.phoneNumber ?: ""
                orderId = generateUUID
                orderStatus = "Order Ongoing"
                currentStep = "Order Created"
                nextStep = "Pending Order Review"
                createdDateTime = currentDateAndTime
                modifiedDateTime = currentDateAndTime
                commodityName = commodityNameArg
                commodityQuantity = tiQuantity.editText?.text.toString().trim()
                commodityApkakisaRate = apkaKisanPrice
                commodityMandiRate = mandiPrice
                commodityTotalSellPrice = totalEarning
                commodityPickupDateTime = pickupDateTimeInput.text.toString()
                commodityDetail = etCommoditydetail.editText?.text.toString()
                upiContact = orderUPIPhoneNo.editText?.text.toString()
            }.let {
                orderReference.child(generateUUID).setValue(it)
                showSellOrderCreatedDialog()
            }
        }

        pickupDateTimeInput.setOnClickListener {
            showDateTimePicker(
                pickupDateTimeInput
            )
        }
    }

    private fun showSellOrderCreatedDialog() {
        val orderCreatedDialogFragment = OrderCreatedDialogFragment.newInstance()
        orderCreatedDialogFragment.onCreateAnotherOrderPressed(onCreateAnotherOrderPressed = {
            resetForm()
        })
        orderCreatedDialogFragment.onCancelPressed(onCancelPressed = {
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
            finish()
        })
        orderCreatedDialogFragment.show(supportFragmentManager, OrderCreatedDialogFragment.TAG)
    }

    private fun resetForm() {
        tiQuantity.editText?.setText("")
        pickupDateTime.editText?.setText("")
        addressLocation.editText?.setText("")
        addressStreet.editText?.setText("")
        pinCode.editText?.setText("")
        etCommoditydetail.editText?.setText("")
        tvHarvestPrice.setText("")
        tvTotalEarning.setText("")
        orderUPIPhoneNo.editText?.setText("")
        orderCheckBox.isChecked = false
    }

    private fun showDateTimePicker(pickupDateTimeInput: TextInputEditText?) {
        val calendar = Calendar.getInstance()
        val dateSetListener =
            OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val timeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
                    calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                    calendar[Calendar.MINUTE] = minute
                    val simpleDateFormat =
                        SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault())
                    val pickUpDateAndTime = simpleDateFormat.format(calendar.time).toString()
                    pickupDateTimeInput?.setText(pickUpDateAndTime)
                }
                TimePickerDialog(
                    this@CreateOrderActivity,
                    timeSetListener,
                    calendar[Calendar.HOUR_OF_DAY],
                    calendar[Calendar.MINUTE],
                    false
                )
                    .show()
            }
        val datePickerDialog = DatePickerDialog(
            this@CreateOrderActivity,
            dateSetListener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun validateQuantity(): Boolean {
        val quantityPerKg = tiQuantity.editText?.text.toString()
        return if (quantityPerKg.isEmpty()) {
            tiQuantity.error = "Quantity field cannot be empty"
            false
        } else {
            tiQuantity.error = null
            tiQuantity.isErrorEnabled = false
            true
        }
    }

    private fun validateLocation(): Boolean {
        val temp = addressLocation.editText?.text.toString()
        return if (temp.isNotEmpty()) {
            addressLocation.error = null
            addressLocation.isErrorEnabled = false
            true
        } else {
            addressLocation.error = "Address field cannot be empty"
            false
        }
    }

    private fun validateStreet(): Boolean {
        val temp = addressStreet.editText?.text.toString()
        return if (temp.isNotEmpty()) {
            addressStreet.error = null
            addressStreet.isErrorEnabled = false
            true
        } else {
            addressStreet.error = "Street field cannot be empty"
            false
        }
    }

    private fun validateDateTime(): Boolean {
        val temp = pickupDateTimeInput.text.toString()
        return if (temp.isNotEmpty()) {
            pickupDateTime.error = null
            pickupDateTime.isErrorEnabled = false
            true
        } else {
            pickupDateTime.error = "Date and Time field cannot be empty"
            false
        }
    }

    private fun validateUPIPhoneNo(): Boolean {
        val temp = orderUPIPhoneNo.editText?.text.toString()
        return if (temp.isNotEmpty()) {
            if (temp.length == 10) {
                orderUPIPhoneNo.error = null
                orderUPIPhoneNo.isErrorEnabled = false
                true
            } else {
                orderUPIPhoneNo.error = "Please provide a valid UPI Phone Number."
                false
            }
        } else {
            orderUPIPhoneNo.error = "UPI phone number field cannot be empty"
            false
        }
    }

    private fun validatePinCode(): Boolean {
        val temp = pinCode.editText?.text.toString()
        return if (temp.isNotEmpty()) {
            if (temp.length == 6) {
                pinCode.error = null
                pinCode.isErrorEnabled = false
                true
            } else {
                pinCode.error = "Please provide a valid Pin Code."
                false
            }
        } else {
            pinCode.error = "Pincode field cannot be empty"
            false
        }
    }

    private fun validateTermsAndCondition(): Boolean {
        return if (!orderCheckBox.isChecked) {
            orderCheckBox.error = "Please accept the Terms and Conditions to proceed."
            orderCheckBox.setTextColor(Color.parseColor("#FB4141"))
            false
        } else {
            orderCheckBox.setTextColor(Color.parseColor("#171725"))
            orderCheckBox.error = null
            true
        }
    }
}