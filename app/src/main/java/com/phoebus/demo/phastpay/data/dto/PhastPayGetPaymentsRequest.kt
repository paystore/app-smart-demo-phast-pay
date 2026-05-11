package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetPaymentsRequest(
    @SerialName("start_date")
    private val startDate: String,

    @SerialName("end_date")
    private val endDate: String,

    @SerialName("status")
    private val status: List<TransactionStatus>,

    @SerialName("value")
    val value: String? = null,
) {
}