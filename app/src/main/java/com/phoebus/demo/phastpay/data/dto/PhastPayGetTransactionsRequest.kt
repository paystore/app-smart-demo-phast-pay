package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PhastPayGetTransactionsRequest (
    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean = true,

    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean = true,

    @SerializedName("start_date")
    val startDate: String = "",

    @SerializedName("end_date")
    val endDate: String = "",
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}