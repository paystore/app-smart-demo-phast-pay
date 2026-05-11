package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.Service
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetQrCodeRequest(
    @SerialName("provider_id")
    val providerId: String = "",
    @SerialName("service")
    val service: Service,
) {
}