package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PhastPayGetPaymentByIdResponse(
    @SerializedName("payment_id")
    val transactionId: String? = null,
    @SerializedName("app_client_id")
    val appClientId: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("value")
    val value: String? = null,
    @SerializedName("iva")
    val iva: String? = null,
    @SerializedName("date_time")
    val dateTime: String? = null
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}