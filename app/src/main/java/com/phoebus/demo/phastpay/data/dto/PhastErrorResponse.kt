package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class PhastErrorResponse(
    @SerialName("error_message")
    val errorMessage: String,
    val code: String? = null,
    val message: String? = null,
    val details: JsonElement? = null
)