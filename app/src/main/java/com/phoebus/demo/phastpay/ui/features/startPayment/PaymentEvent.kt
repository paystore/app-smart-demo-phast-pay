package com.phoebus.demo.phastpay.ui.features.startPayment

sealed interface PaymentEvent {
    data object Initialize : PaymentEvent

    data class UpdateService(val service: String) : PaymentEvent
    data class UpdateSendValue(val sendValue: Boolean) : PaymentEvent
    data class UpdateValue(val value: String?) : PaymentEvent
    data class UpdateCurrency(val currency: String) : PaymentEvent
    data class UpdateSendCurrency(val sendCurrency: Boolean) : PaymentEvent
    data class UpdatePrintCustomerReceipt(val print: Boolean) : PaymentEvent
    data class UpdatePrintMerchantReceipt(val print: Boolean) : PaymentEvent
    data class UpdatePreviewCustomerReceipt(val preview: Boolean) : PaymentEvent
    data class UpdatePreviewMerchantReceipt(val preview: Boolean) : PaymentEvent
    data class UpdateSendPhoneNumber(val sendPhone: Boolean) : PaymentEvent
    data class UpdatePhoneNumber(val phoneNumber: String?) : PaymentEvent
    data class UpdateCountryCode(val countryCode: String?) : PaymentEvent
    data class UpdateSendTipValue(val sendTip: Boolean) : PaymentEvent
    data class UpdateTipValue(val tipValue: String?) : PaymentEvent
    data class SendAdditionalInfo(val send: Boolean) : PaymentEvent
    data class UpdateCustomerName(val name: String?) : PaymentEvent
    data class UpdateCustomerEmail(val email: String?) : PaymentEvent
    data class UpdateSendProviderId(val sendProviderId: Boolean) : PaymentEvent
    data class UpdateProviderId(val providerId: String?) : PaymentEvent
    data object SubmitPayment : PaymentEvent
    data object SubmitPaymentWithAbort : PaymentEvent
    data class UpdateErrorMessage(val message: String? = "") : PaymentEvent
    data class UpdateSuccessMessage(val message: String? = "") : PaymentEvent
}

sealed interface PaymentNavigationEvents {
    data object NavigateToHome :
        PaymentNavigationEvents
}
