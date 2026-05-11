package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund

import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsToRefundResponse
import com.phoebus.phastpay.sdk.client.PhastPayClient


sealed interface GetPaymentsToRefundEvent {
    data class UpdateStartDate(val startDate: String) : GetPaymentsToRefundEvent
    data class UpdateEndDate(val endDate: String) : GetPaymentsToRefundEvent
    data object OnSubmit : GetPaymentsToRefundEvent
    data class UpdateRefundResult(val refundResult: PhastPayGetPaymentsToRefundResponse?) :
        GetPaymentsToRefundEvent

    data class UpdateErrorMessage(val message: String?) : GetPaymentsToRefundEvent
    data object StartGet : GetPaymentsToRefundEvent
    data class UpdatePrintMerchantReceipt(val print: Boolean) : GetPaymentsToRefundEvent
    data class UpdatePrintCustomerReceipt(val print: Boolean) : GetPaymentsToRefundEvent
    data class UpdatePreviewMerchantReceipt(val preview: Boolean) : GetPaymentsToRefundEvent
    data class UpdatePreviewCustomerReceipt(val preview: Boolean) : GetPaymentsToRefundEvent
    data class UpdateParams(
        val startDate: String?,
        val endDate: String?,
    ) : GetPaymentsToRefundEvent
}