package com.apkakisan.myapplication.network.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class Order(
    @SerializedName("orderId")
    var orderId: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("quantity")
    var quantity: Int = 0,

    @SerializedName("apkakisanRate")
    var apkakisanRate: Int = 0,

    @SerializedName("mandiRate")
    var mandiRate: Int = 0,

    @SerializedName("detail")
    var detail: String = "",

    @SerializedName("location")
    var location: String = "",

    @SerializedName("street")
    var street: String = "",

    @SerializedName("pincode")
    var pincode: String = "",

    @SerializedName("inspectionDateTime")
    var inspectionDateTime: String = "",

    @SerializedName("totalSellPrice")
    var totalSellPrice: Int = 0,

    @SerializedName("orderCompletedDateTime")
    var orderCompletedDateTime: String = "",

    @SerializedName("orderInspectedDateTime")
    var orderInspectedDateTime: String = "",

    @SerializedName("orderConfirmedDateTime")
    var orderConfirmedDateTime: String = "",

    @SerializedName("orderReceivedDateTime")
    var orderReceivedDateTime: String = "",

    @SerializedName("orderStatus")
    var orderStatus: String = "",

    @SerializedName("phoneNo")
    var phoneNo: String = "",

    @SerializedName("upiContact")
    var upiContact: String = "",

    @SerializedName("cancellationReason")
    var cancellationReason: String = ""
) : Parcelable