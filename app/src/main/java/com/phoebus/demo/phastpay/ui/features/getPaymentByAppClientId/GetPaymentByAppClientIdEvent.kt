package com.phoebus.demo.phastpay.ui.features.getPaymentByAppClientId

import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetPaymentByAppClientIdEvent {
    data object OnSubmit : GetPaymentByAppClientIdEvent
    data class UpdateAppClientIdPayment(val appClientId : String ) : GetPaymentByAppClientIdEvent
    data class UpdateErrorMessage(val message : String? = "" ) : GetPaymentByAppClientIdEvent
    data class UpdateSuccessMessage(val message : String? = "" ) : GetPaymentByAppClientIdEvent
}