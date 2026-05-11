package com.phoebus.demo.phastpay.ui.features.registerNotify

data class RegisterNotifyState (
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val createPaymentMessage: String? = null,
    val confirmedPaymentMessage: String? = null,
    val showReceiptMessage: String? = null,
    val pingMessage: String? = null,
    val pingTrigger: Long = 0L,
    val isRegistered: Boolean = false
)