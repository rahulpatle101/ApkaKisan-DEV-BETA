package com.apkakisan.myapplication.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.databinding.ActivityOrderDetailBinding
import com.apkakisan.myapplication.helpers.OBJ_ORDER
import com.apkakisan.myapplication.helpers.REQUEST_CODE
import com.apkakisan.myapplication.network.responses.Order
import com.apkakisan.myapplication.utils.DateTimeUtil
import com.apkakisan.myapplication.utils.IntentUtil

class OrderDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        order = intent.getParcelableExtra(OBJ_ORDER) ?: Order()

        binding.toolbar.ibBack.setOnClickListener {
            finish()
        }

        binding.toolbar.tvTitle.text = order.name

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, CancelOrderActivity::class.java)
            intent.putExtra(OBJ_ORDER, order)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.btnContactSupport.setOnClickListener {
            IntentUtil.makeCall(this, "+14154717633")
        }

        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE)
                finish()
        }
    }

    private fun updateUI() {
        if (order.orderCompletedDateTime.isNotEmpty()) {
            binding.ivOrderCompleted.background =
                ContextCompat.getDrawable(this, R.drawable.shape_bg_circle_order_status_selected)
            binding.ivOrderCompleted.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.tvOrderCompletedTime.text = DateTimeUtil.convertDateTimeFormat(
                order.orderCompletedDateTime,
                DateTimeUtil.DF_ORDER_OLD,
                DateTimeUtil.DF_ORDER_DETAIL_STATUS_NEW
            )
        }

        if (order.orderInspectedDateTime.isNotEmpty()) {
            binding.ivOrderInspected.background =
                ContextCompat.getDrawable(this, R.drawable.shape_bg_circle_order_status_selected)
            binding.ivOrderInspected.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.tvOrderInspectedTime.text = DateTimeUtil.convertDateTimeFormat(
                order.orderInspectedDateTime,
                DateTimeUtil.DF_ORDER_OLD,
                DateTimeUtil.DF_ORDER_DETAIL_STATUS_NEW
            )
        }

        if (order.orderConfirmedDateTime.isNotEmpty()) {
            binding.ivOrderConfirmed.background =
                ContextCompat.getDrawable(this, R.drawable.shape_bg_circle_order_status_selected)
            binding.ivOrderConfirmed.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.tvOrderConfirmedTime.text = DateTimeUtil.convertDateTimeFormat(
                order.orderConfirmedDateTime,
                DateTimeUtil.DF_ORDER_OLD,
                DateTimeUtil.DF_ORDER_DETAIL_STATUS_NEW
            )
        }

        if (order.orderReceivedDateTime.isNotEmpty()) {
            binding.ivSellOrderReceived.background =
                ContextCompat.getDrawable(this, R.drawable.shape_bg_circle_order_status_selected)
            binding.ivSellOrderReceived.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.tvSellOrderReceivedTime.text = DateTimeUtil.convertDateTimeFormat(
                order.orderReceivedDateTime,
                DateTimeUtil.DF_ORDER_OLD,
                DateTimeUtil.DF_ORDER_DETAIL_STATUS_NEW
            )
        }

        binding.tvOrderId.text = order.orderId
        binding.tvHarvestName.text = order.name
        binding.tvHarvestQuantity.text = order.quantity.toString()
        binding.tvDateCreated.text = DateTimeUtil.convertDateTimeFormat(
            order.orderReceivedDateTime,
            DateTimeUtil.DF_ORDER_OLD,
            DateTimeUtil.DF_ORDER_NEW
        )
        binding.tvInsAptDate.text = DateTimeUtil.convertDateTimeFormat(
            order.inspectionDateTime,
            DateTimeUtil.DF_INS_APT_OLD,
            DateTimeUtil.DF_ORDER_NEW
        )
        binding.tvHarvestLocation.text = "${order.street} ${order.location}"
        binding.tvNotes.text = order.detail

        //val quantity = order.quantity
        //val apkaKisanPrice = order.apkakisanRate
        //val totalEarning = apkaKisanPrice * quantity
        binding.tvAmount.text = "Rs. ${order.totalSellPrice}"
        binding.tvTotalEarning.text = "Rs. ${order.totalSellPrice}"

        if (order.orderStatus == "Completed") {
            binding.btnCancel.visibility = View.GONE
            binding.btnContactSupport.visibility = View.GONE
        }
    }
}