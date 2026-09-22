package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayAbortTransactionRequest(
    @SerialName("application_id")
    val applicationId: String,
    @SerialName("application_name")
    val applicationName: String,
)