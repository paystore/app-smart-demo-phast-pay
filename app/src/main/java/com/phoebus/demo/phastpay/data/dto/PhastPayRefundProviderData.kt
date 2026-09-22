package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayRefundProviderData(
    @SerialName("rate_refunded")
    val rateRefunded: String? = null,
    @SerialName("amount_refunded_brl")
    val amountRefundedBrl: String? = null
)
