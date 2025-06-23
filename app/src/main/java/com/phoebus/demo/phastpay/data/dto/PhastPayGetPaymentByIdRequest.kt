package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PhastPayGetPaymentByIdRequest(
    @SerializedName("payment_id")
    val paymentId: String = "",

    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean = true,

    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean = true,
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}