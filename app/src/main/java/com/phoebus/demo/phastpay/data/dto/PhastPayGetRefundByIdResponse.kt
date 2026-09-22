package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PhastPayGetRefundByIdResponse(
    @SerialName("refund_id")
    val refundId: String? = null,
    @SerialName("app_client_id")
    val appClientId: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("value")
    val value: String? = null,
    @SerialName("iva")
    val iva: String? = null,
    @SerialName("date_time")
    val dateTime: String? = null,
    @SerialName("provider_data")
    val providerData: PhastPayRefundProviderData? = null,
)
