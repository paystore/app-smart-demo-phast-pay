package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetPaymentsResponse(
    val payments: List<PhastPayGetPaymentByAppClientIdResponse>,
)