package com.phoebus.demo.phastpay.ui.features.startPayment

import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface PaymentEvent {
    data class Initialize(
        val appClientId: String,
        val applicationId: String,
        val applicationName: String
    ) : PaymentEvent

    data class UpdateService(val service: String) : PaymentEvent
    data class UpdateSendValue(val sendValue: Boolean) : PaymentEvent
    data class UpdateValue(val value: String?) : PaymentEvent
    data class UpdateCurrency(val currency: String?) : PaymentEvent
    data class UpdatePrintCustomerReceipt(val print: Boolean) : PaymentEvent
    data class UpdatePrintMerchantReceipt(val print: Boolean) : PaymentEvent
    data class UpdateSendPhoneNumber(val sendPhone: Boolean) : PaymentEvent
    data class UpdatePhoneNumber(val phoneNumber: String?) : PaymentEvent
    data class UpdateCountryCode(val countryCode: String?) : PaymentEvent
    data class SendAdditionalInfo(val send: Boolean) : PaymentEvent
    data class UpdateCustomerName(val name: String?) : PaymentEvent
    data class UpdateCustomerEmail(val email: String?) : PaymentEvent
    data class SubmitPayment(val phastPayClient: PhastPayClient) : PaymentEvent
    data class UpdateErrorMessage(val message: String? = "") : PaymentEvent
    data class UpdateSuccessMessage(val message: String? = "") : PaymentEvent
}

sealed interface PaymentNavigationEvents {
    data object NavigateToHome :
        PaymentNavigationEvents
}
