package com.phoebus.demo.phastpay.ui.features.abortPayment

data class AbortPaymentState(
    val clientId: String = "",
    val isLoading: Boolean = false,
    val message: String? = null
)
