package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class PhastPayGetPaymentByAppClientIdResponse (
    @SerializedName("payment_id")
    val paymentId: String? = null,
    @SerializedName("app_client_id")
    val appClientId: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("value")
    val value: String? = null,
    @SerializedName("iva")
    val iva: String? = null,
    @SerializedName("date_time")
    val dateTime: String? = null,
    @SerializedName("service")
    val service: String? = null,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("application_id")
    val applicationId: String? = null,
    @SerializedName("application_name")
    val applicationName: String? = null,
    @SerializedName("refunds")
    val refunds: List<PhastPayGetRefundByIdResponse>? = null
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}