package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class PhastPayGetPaymentByAppClientIdResponse (
    @SerialName("payment_id")
    val paymentId: String? = null,
    @SerialName("app_client_id")
    val appClientId: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("value")
    val value: String? = null,
    @SerialName("additional_value")
    val additionalValue: String? = null,
    @SerialName("iva")
    val iva: String? = null,
    @SerialName("date_time")
    val dateTime: String? = null,
    @SerialName("service")
    val service: String? = null,
    @SerialName("currency")
    val currency: String,
    @SerialName("application_id")
    val applicationId: String? = null,
    @SerialName("application_name")
    val applicationName: String? = null,
    @SerialName("original_amount_value")
    val originalAmountValue: String? = null,
    @SerialName("provider_data")
    val providerData: PhastPayProviderData? = null,
    @SerialName("refunds")
    val refunds: List<PhastPayGetRefundByIdResponse>? = null
)