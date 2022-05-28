package com.apkakisan.myapplication.order

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.databinding.ActivityCancelOrderBinding
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import com.apkakisan.myapplication.helpers.JSONReaderHelper
import com.apkakisan.myapplication.helpers.OBJ_ORDER
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.network.responses.CancellationReason
import com.apkakisan.myapplication.network.responses.Order
import com.google.firebase.database.FirebaseDatabase

class CancelOrderActivity : BaseActivity() {

    private lateinit var binding: ActivityCancelOrderBinding
    private lateinit var order: Order

    private var cancellationReason = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        order = intent.getParcelableExtra(OBJ_ORDER) ?: Order()

        binding.btnSubmit.setOnClickListener {
            if (cancellationReason.isEmpty()) {
                showShortToast("Please select cancellation reason")
                return@setOnClickListener
            }
            
            if (cancellationReason == "Etc") {
                val cancellationReasonText = binding.etReason.text.toString().trim()
                if (cancellationReasonText.isEmpty()) {
                    binding.etReason.error = "Must not be empty"
                } else {
                    cancellationReason = cancellationReasonText
                    cancelOrderProcess()
                }
            } else cancelOrderProcess()
        }

        adapterProcess()
    }

    private fun adapterProcess() {
        binding.rvCancellationReasons.addItemDecoration(DefaultItemDecorator(48, 48))
        val adapter = CancelOrderAdapter(
            this,
            getCancellationReasonList(),
            onItemClick = { reason: CancellationReason, _: View, _: Int ->
                if (reason.cancellationReason == "Etc")
                    binding.etReason.visibility = View.VISIBLE
                else {
                    binding.etReason.visibility = View.GONE
                }
                cancellationReason = reason.cancellationReason
            }
        )
        binding.rvCancellationReasons.adapter = adapter
    }

    private fun getCancellationReasonList(): List<CancellationReason> {
        return JSONReaderHelper.getJSON(this, JSONReaderHelper.JSONFile) ?: ArrayList()
    }

    private fun cancelOrderProcess() {
        val reference = FirebaseDatabase.getInstance().getReference("ORDERS")
        reference.child(order.orderId)
            .child("orderStatus")
            .setValue("Cancelled")
            .addOnSuccessListener {
                showShortToast("Order cancellation succeeded!")
                reference.child(order.orderId).child("cancellationReason")
                    .setValue(cancellationReason)
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addOnFailureListener {
                showShortToast("Order cancellation failed!")
            }
    }
}