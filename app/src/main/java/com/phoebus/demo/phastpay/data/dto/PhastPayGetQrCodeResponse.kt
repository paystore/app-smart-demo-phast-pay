package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.ImageFormat
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetQrCodeResponse(
    val base64: String,
    val imageFormat: ImageFormat
)