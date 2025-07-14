package com.phoebus.demo.phastpay.ui.features.startRefund

import com.phoebus.demo.phastpay.data.dto.PhastPayStartRefundResponse
import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed class StartRefundEvent {
    data class Initialize(
        val applicationId: String,
        val applicationName: String
    ) : StartRefundEvent()

    data class UpdateSendPartialValue(val sendPartialValue: Boolean) : StartRefundEvent()
    data class UpdatePaymentId(val paymentId: String) : StartRefundEvent()
    data class UpdateValue(val value: String?) : StartRefundEvent()
    data class UpdatePrintCustomerReceipt(val print: Boolean) : StartRefundEvent()
    data class UpdatePrintMerchantReceipt(val print: Boolean) : StartRefundEvent()
    data class SubmitRefund(val phastPayClient: PhastPayClient) : StartRefundEvent()
    data class UpdateErrorMessage(val message : String? = "" ) : StartRefundEvent()
    data class UpdateSuccessMessage(val refundResult : PhastPayStartRefundResponse?) : StartRefundEvent()
}

sealed interface StartRefundNavigationEvents {
    data object NavigateToHome :
        StartRefundNavigationEvents
}
