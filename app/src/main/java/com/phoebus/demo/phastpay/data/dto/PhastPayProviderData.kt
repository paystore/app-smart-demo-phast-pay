package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayProviderData(
    @SerialName("left_to_pay_amount_value")
    val leftToPayAmountValue: String? = null,
    @SerialName("crypto_amount")
    val cryptoAmount: String? = null,
    @SerialName("crypto_currency_code")
    val cryptoCurrencyCode: String? = null,
    @SerialName("network")
    val network: String? = null,
    @SerialName("processing_fee_fiat_amount")
    val processingFeeFiatAmount: String? = null,
    @SerialName("transaction_hash")
    val transactionHash: String? = null,
    val rate: String? = null,
    @SerialName("amount_brl")
    val amountBrl: String? = null
)
