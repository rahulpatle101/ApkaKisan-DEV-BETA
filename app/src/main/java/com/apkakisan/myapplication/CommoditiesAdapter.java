package com.apkakisan.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class CommoditiesAdapter extends RecyclerView.Adapter<CommoditiesAdapter.ViewHolder> {

    private List<CommodityItem> commodityItemList;
    private OnCommodityListener mOnCommodityListener;


    public CommoditiesAdapter(List<CommodityItem> commodityItemList, OnCommodityListener onCommodityListener) {
        this.commodityItemList = commodityItemList;
        this.mOnCommodityListener = onCommodityListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commodity_list_item, parent, false);

        return new ViewHolder(view, mOnCommodityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.commodityTitle.setText(commodityItemList.get(position).getCommodity_title());
        holder.commodityMandiPrice.setText(commodityItemList.get(position).getCommodity_mandi_price());
        holder.commodityApkaKisanPrice.setText(commodityItemList.get(position).getCommodity_apkakisan_price());
    }

    @Override
    public int getItemCount() {
        return commodityItemList.size();
    }

    public void filterList(List<CommodityItem> filteredList) {
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

            System.out.println("title--->>>>"+commodityTitle);
        }

        @Override
        public void onClick(View view) {
            onCommodityListener.onCommodityClick(getAdapterPosition());
        }
    }

    public interface OnCommodityListener{
        void onCommodityClick(int position);
    }
}
