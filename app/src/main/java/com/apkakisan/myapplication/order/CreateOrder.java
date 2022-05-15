package com.apkakisan.myapplication.order;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apkakisan.myapplication.R;
import com.apkakisan.myapplication.SellOrderItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CreateOrder extends AppCompatActivity {
    String commodity_name;
    String apka_kisan_price;
    Button sellOrderBtn;
    CheckBox orderCheckBox;
    TextView sellOrderHeading, totalEarningText;
    TextInputLayout pickupDateTime, quantityKg, addressLocation, addressStreet, addressPincode, commodityDetail, orderUPIPhoneNo;
    TextInputEditText pickupDateTimeInput;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;

    String quantityKgInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        sellOrderBtn = findViewById(R.id.sell_order_btn);
        sellOrderHeading = findViewById(R.id.sell_order_title);
        pickupDateTime = findViewById(R.id.pickup_date_time);
        pickupDateTimeInput = findViewById(R.id.pickup_date_time_input);
        quantityKg = findViewById(R.id.quantity_kg);
        addressLocation = findViewById(R.id.address_location);
        addressStreet = findViewById(R.id.address_street);
        addressPincode = findViewById(R.id.address_pincode);
        commodityDetail = findViewById(R.id.commodity_detail);
        orderCheckBox = findViewById(R.id.order_check_box);
        orderUPIPhoneNo = findViewById(R.id.upi_phone_number);
        totalEarningText = findViewById(R.id.earning_price);


        quantityKgInput = quantityKg.getEditText().getText().toString();

//        int totalEarning = Integer.parseInt(quantityKgInput);

//        totalEarningText.setText(totalEarning);
//        String totalEarningString = String.valueOf(totalEarning) ;



        pickupDateTimeInput.setInputType(InputType.TYPE_NULL);

        commodity_name = getIntent().getStringExtra("commodity_name");
        apka_kisan_price = getIntent().getStringExtra("commodity_apka_kisan_price");

//        Sell Order details for Name of the Harvest Rs 100.00/kg" />
//        sellOrderHeading.setText("Sell Order details for "+ commodity_name+" "+ apka_kisan_price);

        Toast.makeText(this, "name is -" + commodity_name + "ak-price is - "+apka_kisan_price, Toast.LENGTH_SHORT).show();

        sellOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateOrder.this, "clicked sell now", Toast.LENGTH_SHORT).show();
//                Validate all the inputs
                if(!validateQuantity() | !validatePinCode() | !validateLocation() | !validateStreet() | !validateDateTime() |!validateUPIPhoneNo() | !validateTermsAndCondition()){

                    return;
                }
                // update total earning based on user input of quantity

                Toast.makeText(CreateOrder.this, "Ready to post", Toast.LENGTH_SHORT).show();

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("ORDERS");

                user = auth.getCurrentUser();
                String phoneNumber = user.getPhoneNumber();

                String generateUUID;
                UUID idOne = UUID.randomUUID();
                UUID idTwo = UUID.randomUUID();

                generateUUID = String.valueOf(idOne) + String.valueOf(idTwo);

                SimpleDateFormat now = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z");
                String currentDateandTime = now.format(new Date());

                SellOrderItem addNewOrder = new SellOrderItem(phoneNumber, generateUUID,"Order Ongoing", "Order Created", "Pending Order Review", currentDateandTime, currentDateandTime, "order_commodity_name", "order_commodity_quantity",  "order_commodity_apkakisan_rate", "order_commodity_mandi_rate", "order_commodity_total_sell_price", "order_commodity_pickup_date_time",  "order_commodity_detail", "order_upi_contact");
                reference.child(generateUUID).setValue(addNewOrder);
//                reference.add(addNewOrder).addOnsucc

                Toast.makeText(CreateOrder.this, "aaya- "+generateUUID, Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });

        pickupDateTimeInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Toast.makeText(CreateOrder.this, "clicked pickupdate now", Toast.LENGTH_SHORT).show();
                showDateTimePicker(pickupDateTimeInput);
            }
        });

    }

    private void showDateTimePicker(final TextInputEditText pickupDateTimeInput) {
//        Toast.makeText(CreateOrder.this, "going to showdatetimepicker now", Toast.LENGTH_SHORT).show();

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                        String pickUpDateAndTime = simpleDateFormat.format(calendar.getTime()).toString();

                        pickupDateTimeInput.setText(pickUpDateAndTime);
                    }
                };

                new TimePickerDialog(CreateOrder.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrder.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    private Boolean validateQuantity() {
        String val = quantityKg.getEditText().getText().toString();

        if (val.isEmpty()) {
            quantityKg.setError("Quantity field cannot be empty");
            return false;
        } else {
            quantityKg.setError(null);
            quantityKg.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateLocation() {
        String temp = addressLocation.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            addressLocation.setError(null);
            addressLocation.setErrorEnabled(false);
            return true;
        } else {
            addressLocation.setError("Address field cannot be empty");
            return false;
        }
    }

    private Boolean validateStreet() {
        String temp = addressStreet.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            addressStreet.setError(null);
            addressStreet.setErrorEnabled(false);
            return true;
        } else {
            addressStreet.setError("Street field cannot be empty");
            return false;
        }
    }

    private Boolean validateDateTime() {
        String temp = pickupDateTimeInput.getText().toString();
        if (!temp.isEmpty()) {
            pickupDateTime.setError(null);
            pickupDateTime.setErrorEnabled(false);
            return true;
        } else {
            pickupDateTime.setError("Date and Time field cannot be empty");
            return false;
        }
    }

    private Boolean validateUPIPhoneNo() {
        String temp = orderUPIPhoneNo.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            if (temp.length() == 10 ) {
                orderUPIPhoneNo.setError(null);
                orderUPIPhoneNo.setErrorEnabled(false);
                return true;
            } else {
                orderUPIPhoneNo.setError("Please provide a valid UPI Phone Number.");
                return false;
            }
        } else {
            orderUPIPhoneNo.setError("UPI phone number field cannot be empty");
            return false;
        }
    }

    private Boolean validatePinCode() {
        String temp = addressPincode.getEditText().getText().toString();
        if (!temp.isEmpty()) {
            if (temp.length() == 6 ) {
                addressPincode.setError(null);
                addressPincode.setErrorEnabled(false);
                return true;
            } else {
                addressPincode.setError("Please provide a valid Pin Code.");
                return false;
            }
        } else {
            addressPincode.setError("Pincode field cannot be empty");
            return false;
        }
    }

    private Boolean validateTermsAndCondition() {

        if (!orderCheckBox.isChecked()) {
            orderCheckBox.setError("Please accept the Terms and Conditions to proceed.");
            orderCheckBox.setTextColor(Color.parseColor("#FB4141"));
            return false;
        } else {
            orderCheckBox.setTextColor(Color.parseColor("#171725"));
            orderCheckBox.setError(null);
            return true;
        }
    }

}