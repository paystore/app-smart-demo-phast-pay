package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class PhastPayStartRefundRequest(
    @SerializedName("app_client_id")
    val appClientId: String? = null,

    @SerializedName("application_id")
    val applicationId: String? = null,

    @SerializedName("application_name")
    val applicationName: String? = null,

    @SerializedName("payment_id")
    val paymentId: String? = null,

    @SerializedName("value")
    val value: String? = null,

    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean? = null,

    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean? = null
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
