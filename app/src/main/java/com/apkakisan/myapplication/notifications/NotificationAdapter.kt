package com.apkakisan.myapplication.notifications

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.network.responses.Notification
import com.apkakisan.myapplication.utils.DateTimeUtil

class NotificationAdapter(
    private val context: Context,
    private var notificationList: List<Notification>,
    private val onNotificationClick: (
        notification: Notification,
        view: View,
        position: Int
    ) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(notification: Notification) {
            tvTitle.text = notification.title
            tvDescription.text = notification.description
            tvDate.text = DateTimeUtil.convertDateTimeFormat(
                notification.createdDate,
                DateTimeUtil.DF_NOTIFICATION_OLD,
                DateTimeUtil.DF_NOTIFICATION_NEW
            )

            itemView.setOnClickListener {
                onNotificationClick(notification, it, adapterPosition)
            }
        }
    }
}