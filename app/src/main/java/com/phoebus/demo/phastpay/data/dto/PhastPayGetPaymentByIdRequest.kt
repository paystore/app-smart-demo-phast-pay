package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetPaymentByIdRequest(
    @SerialName("payment_id")
    val paymentId: String,
)
