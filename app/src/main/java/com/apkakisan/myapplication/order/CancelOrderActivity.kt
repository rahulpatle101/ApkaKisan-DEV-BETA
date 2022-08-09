package com.apkakisan.myapplication.order

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.R
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

    private var cancellationReasonEn = ""
    private var cancellationReasonHi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        order = intent.getParcelableExtra(OBJ_ORDER) ?: Order()

        binding.toolbar.ibBack.setOnClickListener {
            finish()
        }

        binding.toolbar.tvTitle.text = getString(R.string.cancellation_reason)

        binding.btnSubmit.setOnClickListener {
            if (cancellationReasonEn.isEmpty() || cancellationReasonHi.isEmpty()) {
                showShortToast("Please select cancellation reason")
                return@setOnClickListener
            }

            if (cancellationReasonEn == "Etc" || cancellationReasonHi == "आदि") {
                val cancellationReasonText = binding.etReason.text.toString().trim()
                if (cancellationReasonText.isEmpty()) {
                    binding.etReason.error = "Must not be empty"
                } else {
                    cancellationReasonEn = cancellationReasonText
                    cancellationReasonHi = cancellationReasonText
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
                if (reason.cancellationReason == "Etc" || reason.cancellationReason == "आदि")
                    binding.etReason.visibility = View.VISIBLE
                else {
                    binding.etReason.visibility = View.GONE
                }
                cancellationReasonEn = reason.cancellationReasonEn
                cancellationReasonHi = reason.cancellationReasonHi

            }
        )
        binding.rvCancellationReasons.adapter = adapter
    }

    private fun getCancellationReasonList(): List<CancellationReason> {
        return JSONReaderHelper.getJSON(this, JSONReaderHelper.JSONFile) ?: ArrayList()
    }

    private fun cancelOrderProcess() {
        val reference = FirebaseDatabase.getInstance().getReference("Orders")
        reference.child(order.orderId)
            .child("orderStatusEn")
            .setValue("Cancelled")
            .addOnSuccessListener {
                showShortToast(getString(R.string.order_cancellation_succeedded))
                reference.child(order.orderId).child("cancellationReasonEn")
                    .setValue(cancellationReasonEn)
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addOnFailureListener {
                showShortToast(getString(R.string.order_cancellation_failed))
            }

        reference.child(order.orderId)
            .child("orderStatusHi")
            .setValue("रद्द")
            .addOnSuccessListener {
                showShortToast(getString(R.string.order_cancellation_succeedded))
                reference.child(order.orderId).child("cancellationReasonHi")
                    .setValue(cancellationReasonHi)
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addOnFailureListener {
                showShortToast(getString(R.string.order_cancellation_failed))
            }
    }
}