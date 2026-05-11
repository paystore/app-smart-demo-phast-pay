package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayAbortPaymentRequest(
    @SerialName("app_client_id")
    val appClientId: String,
)
