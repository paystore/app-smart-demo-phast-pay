package com.phoebus.demo.phastpay.ui.features.getRefundById

import com.phoebus.demo.phastpay.data.dto.PhastPayGetRefundByIdResponse
import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetRefundByIdEvent {
    data class OnSubmit(val phastPayClient: PhastPayClient) : GetRefundByIdEvent
    data class UpdatePrintMerchantReceipt(val print : Boolean ) : GetRefundByIdEvent
    data class UpdatePrintCustomerReceipt(val print : Boolean ) : GetRefundByIdEvent
    data class UpdateRefundById(val refundId : String ) : GetRefundByIdEvent
    data class UpdateRefundByResult(val refundResult : PhastPayGetRefundByIdResponse? ) : GetRefundByIdEvent
    data class UpdateErrorMessage(val message : String? = "" ) : GetRefundByIdEvent
}