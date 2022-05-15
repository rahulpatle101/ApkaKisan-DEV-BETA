package com.apkakisan.myapplication.order

import com.apkakisan.myapplication.network.responses.Commodity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.apkakisan.myapplication.R
import android.widget.TextView

class CommoditiesAdapter(
    private var commodityItemList: List<Commodity>,
    private val onClick: (
        commodity: Commodity,
        view: View,
        position: Int
    ) -> Unit
) : RecyclerView.Adapter<CommoditiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_commodity, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commodityItemList.get(position))
    }

    override fun getItemCount(): Int {
        return commodityItemList.size
    }

    fun filterList(filteredList: List<Commodity>) {
        commodityItemList = filteredList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var commodityTitle: TextView = itemView.findViewById(R.id.commodity_name)
        var commodityMandiPrice: TextView = itemView.findViewById(R.id.mandi_price_value)
        var commodityApkaKisanPrice: TextView = itemView.findViewById(R.id.ak_price_value)

        fun bind(commodity: Commodity) {
            commodityTitle.text = commodity.title
            commodityMandiPrice.text = commodity.mandi_price
            commodityApkaKisanPrice.text = commodity.apkakisan_price

            itemView.setOnClickListener {
                onClick(commodity, it, adapterPosition)
            }
        }
    }
}