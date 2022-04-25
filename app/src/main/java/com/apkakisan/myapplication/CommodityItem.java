package com.apkakisan.myapplication;

public class CommodityItem {

    private String title;
    private String mandi_price;
    private String apkakisan_price;

    public CommodityItem(String commodity_title, String commodity_mandi_price, String commodity_apkakisan_price) {
        this.title = commodity_title;
        this.mandi_price = commodity_mandi_price;
        this.apkakisan_price = commodity_apkakisan_price;
    }

    public String getCommodity_title() {
        return title;
    }

    public String getCommodity_mandi_price() {
        return mandi_price;
    }

    public String getCommodity_apkakisan_price() {
        return apkakisan_price;
    }
}
