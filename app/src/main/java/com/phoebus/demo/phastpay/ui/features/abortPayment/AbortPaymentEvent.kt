package com.phoebus.demo.phastpay.ui.features.abortPayment

sealed interface AbortPaymentEvent {
    data class OnClientIdChange(val clientId: String) : AbortPaymentEvent
    object OnAbortPaymentClick : AbortPaymentEvent
}