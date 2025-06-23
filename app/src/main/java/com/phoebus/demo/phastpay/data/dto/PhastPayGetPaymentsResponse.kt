package com.phoebus.demo.phastpay.data.dto

data class PhastPayGetPaymentsResponse(
    val payments: List<PhastPayGetPaymentByAppClientIdResponse>,
)