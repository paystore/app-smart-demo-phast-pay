package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.phoebus.demo.phastpay.data.enums.TransactionStatus

data class PhastPayGetPaymentsRequest(
    @SerializedName("start_date")
    private val startDate: String,

    @SerializedName("end_date")
    private val endDate: String,

    @SerializedName("status")
    private val status: List<TransactionStatus>,

    @SerializedName("value")
    val value: String? = null,
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}