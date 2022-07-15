package com.apkakisan.myapplication.notifications

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.network.responses.NotificationTypeSetting
import com.apkakisan.myapplication.network.responses.NotificationType

class NotificationSettingAdapter(
    private val context: Context,
    private var notificationSettingList: List<NotificationType>,
    private val onCheckedClick: (
        notificationType: NotificationType,
        notificationSetting: NotificationTypeSetting,
        position: Int
    ) -> Unit
) : RecyclerView.Adapter<NotificationSettingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification_setting, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notificationSettingList[position])
    }

    override fun getItemCount(): Int {
        return notificationSettingList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        private val swSetting: SwitchCompat = itemView.findViewById(R.id.swSetting)

        fun bind(notificationType: NotificationType) {
            tvType.text = notificationType.type
            swSetting.isChecked = notificationType.notificationSetting.isChecked
            swSetting.setOnCheckedChangeListener { _, isChecked ->
                notificationType.notificationSetting.notificationTypeId = notificationType.id
                notificationType.notificationSetting.isChecked = isChecked
                onCheckedClick(
                    notificationType,
                    notificationType.notificationSetting,
                    adapterPosition
                )
            }
        }
    }
}