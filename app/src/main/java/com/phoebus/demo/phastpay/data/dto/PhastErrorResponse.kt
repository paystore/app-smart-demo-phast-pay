package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastErrorResponse(
    @SerialName("error_message")
    val errorMessage: String
)