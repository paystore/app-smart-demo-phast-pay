package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PhastPayStartPaymentResponse(
    @SerialName("app_client_id")
    val appClientId: String? = null,
    @SerialName("date_time")
    val dateTime: String? = null,
    @SerialName("iva")
    val iva: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("payment_id")
    val paymentId: String? = null,
    @SerialName("value")
    val value: String? = null,
    @SerialName("additional_value")
    val additionalValue: String? = null
)
