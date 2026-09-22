package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetTransactionsResponse (
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
    @SerialName("transaction_id")
    val transactionId: String? = null,
    @SerialName("value")
    val value: String? = null,
    @SerialName("additional_value")
    val additionalValue: String? = null,
    @SerialName("original_amount_value")
    val originalAmountValue: String? = null,
    @SerialName("provider_data")
    val providerData: PhastPayProviderData? = null,
)