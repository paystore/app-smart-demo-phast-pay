package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PhastPayGetRefundByIdRequest(
    @SerializedName("refund_id")
    val refundId: String = "",

    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean = true,

    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean = true,
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}