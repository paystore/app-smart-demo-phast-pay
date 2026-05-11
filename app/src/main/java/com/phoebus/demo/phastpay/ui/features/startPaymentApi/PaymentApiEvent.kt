package com.phoebus.demo.phastpay.ui.features.startPaymentApi

sealed interface PaymentApiEvent {
    data object Initialize : PaymentApiEvent
    data class UpdateAppClientId(val appClientId: String) : PaymentApiEvent
    data class UpdateService(val service: String) : PaymentApiEvent
    data class UpdateSendValue(val sendValue: Boolean) : PaymentApiEvent
    data class UpdateSendTipValue(val sendTipValue: Boolean) : PaymentApiEvent
    data class UpdateValue(val value: String?) : PaymentApiEvent
    data class UpdateTipValue(val tipValue: String?) : PaymentApiEvent
    data class UpdateCurrency(val currency: String) : PaymentApiEvent
    data class UpdatePrintCustomerReceipt(val print: Boolean) : PaymentApiEvent
    data class UpdatePrintMerchantReceipt(val print: Boolean) : PaymentApiEvent
    data class UpdatePreviewCustomerReceipt(val preview: Boolean) : PaymentApiEvent
    data class UpdatePreviewMerchantReceipt(val preview: Boolean) : PaymentApiEvent
    data class UpdateSendProviderId(val send: Boolean) : PaymentApiEvent
    data class UpdateProviderId(val providerId: String?) : PaymentApiEvent
    data object SubmitPaymentApi : PaymentApiEvent
    data class UpdateErrorMessage(val message: String? = "") : PaymentApiEvent
    data class UpdateSuccessMessage(val message: String? = "") : PaymentApiEvent
}

sealed interface PaymentApiNavigationEvents {
    data object NavigateToHome :
        PaymentApiNavigationEvents
}
