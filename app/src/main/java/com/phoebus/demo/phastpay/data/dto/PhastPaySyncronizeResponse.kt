package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPaySyncronizeResponse (
    @SerialName("result")
    val result: Boolean
)