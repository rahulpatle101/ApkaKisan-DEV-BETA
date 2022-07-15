package com.apkakisan.myapplication.order

import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.apkakisan.myapplication.R
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.*
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.common.AppDialogFragment
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.helpers.popFragment
import com.apkakisan.myapplication.network.responses.Notification
import com.apkakisan.myapplication.network.responses.Order
import java.text.SimpleDateFormat
import java.util.*

class CreateOrderActivity : BaseActivity() {

    private lateinit var sellOrderBtn: Button
    private lateinit var orderCheckBox: CheckBox
    private lateinit var tvCommodityName: TextView
    private lateinit var pickupDateTime: TextInputLayout
    private lateinit var tiQuantity: TextInputLayout
    private lateinit var addressLocation: TextInputLayout
    private lateinit var addressStreet: TextInputLayout
    private lateinit var etPinCode: TextInputLayout
    private lateinit var etCommoditydetail: TextInputLayout
    private lateinit var orderUPIPhoneNo: TextInputLayout
    private lateinit var pickupDateTimeInput: TextInputEditText
    private lateinit var tvHarvestPrice: TextView
    private lateinit var tvTotalEarning: TextView

    private var commodityName: String = ""
    private var mandiPriceArg: Int = 0
    private var apkaKisanPriceArg: Int = 0

    private var totalEarning = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        commodityName = intent?.getStringExtra("title") ?: ""
        mandiPriceArg = intent?.getIntExtra("mandi_price", 0) ?: 0
        apkaKisanPriceArg = intent?.getIntExtra("apkakisan_price", 0) ?: 0

        findViewById<ImageButton>(R.id.ibBack).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.tvTitle).text = getString(R.string.home)

        tvCommodityName = findViewById(R.id.tvCommodityName)
        tvCommodityName.text =
            String.format(getString(R.string.selling_commodity), commodityName, mandiPriceArg)

        tiQuantity = findViewById(R.id.tiQuantity)
        tiQuantity.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!char.isNullOrEmpty()) {
                    val quantity: Int = char.toString().trim().toInt()
                    totalEarning = apkaKisanPriceArg * quantity
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
        etPinCode = findViewById(R.id.address_pincode)
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
            val generateUUID = UUID.randomUUID().toString()
            val now = SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z", Locale.getDefault())
            val currentDateAndTime = now.format(Date())

            Order().apply {
                orderId = generateUUID
                name = commodityName
                quantity = tiQuantity.editText?.text.toString().trim().toInt()
                apkakisanRate = apkaKisanPriceArg
                mandiRate = mandiPriceArg
                orderStatus = "Received"
                totalSellPrice = totalEarning
                inspectionDateTime = pickupDateTimeInput.text.toString().trim()
                detail = etCommoditydetail.editText?.text.toString().trim()
                phoneNo = user?.phoneNumber ?: ""
                upiContact = orderUPIPhoneNo.editText?.text.toString().trim()
                location = addressLocation.editText?.text.toString().trim()
                street = addressStreet.editText?.text.toString().trim()
                orderReceivedDateTime = currentDateAndTime
                pincode = etPinCode.editText?.text.toString().trim()
                userId = LocalStore.user?.userId!!
            }.also { order ->
                val orderReference = FirebaseDatabase.getInstance().getReference("Orders")
                orderReference.child(generateUUID).setValue(order)

                val notificationId = UUID.randomUUID().toString()
                Notification().apply {
                    id = notificationId
                    type = "In App Notification"
                    title = "Order Received!"
                    description = "You order has received. Thanks."
                    createdDate = currentDateAndTime
                    userId = LocalStore.user?.userId!!
                    orderId = order.orderId
                }.also { notification ->
                    val reference = FirebaseDatabase.getInstance().getReference("Notification")
                    reference.child(notificationId).setValue(notification)
                }

                showAppDialog()
            }
        }

        pickupDateTimeInput.setOnClickListener {
            showDateTimePicker(
                pickupDateTimeInput
            )
        }
    }

    private fun showAppDialog() {
        val dialog = AppDialogFragment.newInstance(
            getString(R.string.sell_order_created),
            getString(R.string.sell_order_created_congrats)
        )
        dialog.onDonePressed { finish() }
        dialog.show(supportFragmentManager, AppDialogFragment.TAG)
    }

    private fun resetForm() {
        tiQuantity.editText?.setText("")
        pickupDateTime.editText?.setText("")
        addressLocation.editText?.setText("")
        addressStreet.editText?.setText("")
        etPinCode.editText?.setText("")
        etCommoditydetail.editText?.setText("")
        tvHarvestPrice.text = ""
        tvTotalEarning.text = ""
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
        val temp = etPinCode.editText?.text.toString()
        return if (temp.isNotEmpty()) {
            if (temp.length == 6) {
                etPinCode.error = null
                etPinCode.isErrorEnabled = false
                true
            } else {
                etPinCode.error = "Please provide a valid Pin Code."
                false
            }
        } else {
            etPinCode.error = "Pincode field cannot be empty"
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

    companion object {
        const val TAG = "CreateOrderActivity"

        const val TITLE = "title"
        const val MANDI_PRICE = "mandi_price"
        const val APKAKISAN_PRICE = "apkakisan_price"
    }
}