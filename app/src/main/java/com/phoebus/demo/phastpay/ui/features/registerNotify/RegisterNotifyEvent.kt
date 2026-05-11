package com.phoebus.demo.phastpay.ui.features.registerNotify

import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface RegisterNotifyEvent {
    data object RegisterNotify: RegisterNotifyEvent
    data object UnRegisterNotify: RegisterNotifyEvent
    data class UpdateSuccessMessage(val message: String?): RegisterNotifyEvent
    data class UpdateErrorMessage(val message: String?): RegisterNotifyEvent
    data class UpdateCreatePayment(val message: String?): RegisterNotifyEvent
    data class UpdateConfirmedPaymentMessage(val message: String?): RegisterNotifyEvent
    data class UpdateShowReceiptMessage(val message: String?): RegisterNotifyEvent
    data class UpdatePingMessage(val message: String?): RegisterNotifyEvent

}

sealed interface RegisterNotifyNavigationEvents {
    data object NavigateToHome :
        RegisterNotifyNavigationEvents
}