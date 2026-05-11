package com.phoebus.demo.phastpay.ui.features.getPaymentById

sealed interface GetPaymentByIdEvent {
    data object OnSubmit : GetPaymentByIdEvent
    data class UpdatePaymentById(val paymentId : String ) : GetPaymentByIdEvent
    data class UpdateErrorMessage(val message : String? = "" ) : GetPaymentByIdEvent
    data class UpdateSuccessMessage(val message : String? = "" ) : GetPaymentByIdEvent
}