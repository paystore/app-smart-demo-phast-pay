package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.phoebus.demo.phastpay.data.enums.Service

class PhastPayStartPaymentRequest(
    @SerializedName("app_client_id")
    val appClientId: String? = null,

    @SerializedName("application_id")
    val applicationId: String? = null,

    @SerializedName("application_name")
    val applicationName: String? = null,

    @SerializedName("value")
    val value: String? = null,

    @SerializedName("service")
    val service: Service? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("print_customer_receipt")
    val printCustomerReceipt: Boolean? = null,

    @SerializedName("print_merchant_receipt")
    val printMerchantReceipt: Boolean? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("county_code")
    val countyCode: String? = null,

    @SerializedName("customer_name")
    val customerName: String? = null,

    @SerializedName("customer_email")
    val customerEmail: String? = null
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
