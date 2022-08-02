package com.apkakisan.myapplication.order

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.network.responses.Order
import com.apkakisan.myapplication.utils.DateTimeUtil

class OrderAdapter(
    private val context: Context,
    private val orderType: String,
    private var orderList: List<Order>,
    private val onDetailsBtnClick: (
        order: Order,
        view: View,
        position: Int
    ) -> Unit,
    private val onSellAgainBtnClick: (
        order: Order,
        view: View,
        position: Int
    ) -> Unit
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCommodityName: TextView = itemView.findViewById(R.id.tvCommodityName)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val btnDetails: Button = itemView.findViewById(R.id.btnDetails)
        private val btnSellAgain: Button = itemView.findViewById(R.id.btnSellAgain)

        fun bind(order: Order) {
            tvCommodityName.text = order.name

            tvStatus.text = order.orderStatus
            if (order.orderStatusEn == "Cancelled") {
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorError))
                btnSellAgain.background =
                    ContextCompat.getDrawable(context, R.drawable.shape_bg_btn_red)
            } else {
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
                btnSellAgain.background =
                    ContextCompat.getDrawable(context, R.drawable.shape_bg_btn_green)
            }

            tvDate.text = DateTimeUtil.convertDateTimeFormat(
                order.orderReceivedDateTime,
                DateTimeUtil.DF_ORDER_OLD,
                DateTimeUtil.DF_ORDER_NEW
            )

            if (orderType == OrderHistoryFragment.TAG) {
                btnDetails.visibility = View.GONE
                btnSellAgain.visibility = View.VISIBLE
            }

            btnDetails.setOnClickListener {
                onDetailsBtnClick(order, it, adapterPosition)
            }

            btnSellAgain.setOnClickListener {
                onSellAgainBtnClick(order, it, adapterPosition)
            }
        }
    }
}