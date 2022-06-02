package com.apkakisan.myapplication.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apkakisan.myapplication.network.responses.Commodity;
import com.apkakisan.myapplication.R;

import java.util.List;

public class CommoditiesAdapterOld extends RecyclerView.Adapter<CommoditiesAdapterOld.ViewHolder> {

    private List<Commodity> commodityItemList;
    private final OnCommodityListener mOnCommodityListener;

    public CommoditiesAdapterOld(List<Commodity> commodityItemList, OnCommodityListener onCommodityListener) {
        this.commodityItemList = commodityItemList;
        this.mOnCommodityListener = onCommodityListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_commodity, parent, false);
        return new ViewHolder(view, mOnCommodityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.commodityTitle.setText(commodityItemList.get(position).getTitle());
        holder.commodityMandiPrice.setText(commodityItemList.get(position).getMandiPrice());
        holder.commodityApkaKisanPrice.setText(commodityItemList.get(position).getApkakisanPrice());
    }

    @Override
    public int getItemCount() {
        return commodityItemList.size();
    }

    public void filterList(List<Commodity> filteredList) {
        commodityItemList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView commodityTitle;
        TextView commodityMandiPrice;
        TextView commodityApkaKisanPrice;
        OnCommodityListener onCommodityListener;

        public ViewHolder(@NonNull View itemView, OnCommodityListener onCommodityListener) {
            super(itemView);
            commodityTitle = itemView.findViewById(R.id.commodity_name);
            commodityMandiPrice = itemView.findViewById(R.id.mandi_price_value);
            commodityApkaKisanPrice = itemView.findViewById(R.id.ak_price_value);
            this.onCommodityListener = onCommodityListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCommodityListener.onCommodityClick(getAdapterPosition());
        }
    }

    public interface OnCommodityListener {
        void onCommodityClick(int position);
    }
}
