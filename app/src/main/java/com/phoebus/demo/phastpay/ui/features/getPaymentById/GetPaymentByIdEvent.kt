package com.phoebus.demo.phastpay.ui.features.getPaymentById

import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetPaymentByIdEvent {
    data class OnSubmit(val phastPayClient: PhastPayClient) : GetPaymentByIdEvent
    data class UpdatePrintMerchantReceipt(val print : Boolean ) : GetPaymentByIdEvent
    data class UpdatePrintCustomerReceipt(val print : Boolean ) : GetPaymentByIdEvent
    data class UpdatePaymentById(val paymentId : String ) : GetPaymentByIdEvent
    data class UpdateErrorMessage(val message : String? = "" ) : GetPaymentByIdEvent
    data class UpdateSuccessMessage(val message : String? = "" ) : GetPaymentByIdEvent
}