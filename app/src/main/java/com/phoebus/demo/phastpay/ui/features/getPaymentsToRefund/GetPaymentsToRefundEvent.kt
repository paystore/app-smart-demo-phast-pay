package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund

import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsToRefundResponse
import com.phoebus.phastpay.sdk.client.PhastPayClient


sealed interface GetPaymentsToRefundEvent {
    data class UpdateStartDate(val startDate: String) : GetPaymentsToRefundEvent
    data class UpdateEndDate(val endDate: String) : GetPaymentsToRefundEvent
    data class OnSubmit(val phastPayClient: PhastPayClient) : GetPaymentsToRefundEvent
    data class UpdateRefundResult(val refundResult: PhastPayGetPaymentsToRefundResponse?) :
        GetPaymentsToRefundEvent

    data class UpdateErrorMessage(val message: String?) : GetPaymentsToRefundEvent
    data class StartGet(val phastPayClient: PhastPayClient) : GetPaymentsToRefundEvent
    data class UpdatePrintMerchantReceipt(val print: Boolean) : GetPaymentsToRefundEvent
    data class UpdatePrintCustomerReceipt(val print: Boolean) : GetPaymentsToRefundEvent
    data class UpdateParams(
        val startDate: String,
        val endDate: String,
    ) : GetPaymentsToRefundEvent
}