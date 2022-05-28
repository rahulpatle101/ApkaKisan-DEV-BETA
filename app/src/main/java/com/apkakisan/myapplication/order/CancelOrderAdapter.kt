package com.apkakisan.myapplication.order

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.network.responses.CancellationReason

class CancelOrderAdapter(
    private val context: Context,
    private var cancellationReasonList: List<CancellationReason>,
    private val onItemClick: (
        reason: CancellationReason,
        view: View,
        position: Int
    ) -> Unit,
) : RecyclerView.Adapter<CancelOrderAdapter.ViewHolder>() {

    private var selectedItemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cancel_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cancellationReasonList[position])
    }

    override fun getItemCount(): Int {
        return cancellationReasonList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvReason: TextView = itemView.findViewById(R.id.tvReason)

        fun bind(reason: CancellationReason) {
            tvReason.text = reason.cancellationReason

            if (reason.isSelected)
                tvReason.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.shape_bg_cancellation_reason_selected
                )
            else
                tvReason.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.shape_bg_cancellation_reason_unselected
                )

            itemView.setOnClickListener {
                cancellationReasonList[selectedItemPosition].isSelected = false
                cancellationReasonList[adapterPosition].isSelected = true
                selectedItemPosition = adapterPosition
                onItemClick(cancellationReasonList[adapterPosition], it, adapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}