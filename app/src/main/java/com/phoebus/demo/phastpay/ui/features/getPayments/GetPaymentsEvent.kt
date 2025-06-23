package com.phoebus.demo.phastpay.ui.features.getPayments

import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsResponse
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetPaymentsEvent {
    data class UpdateParams(
        val startDate: String,
        val endDate: String,
        val status: List<TransactionStatus>,
        val value: String?
    ) : GetPaymentsEvent
    data class StartGetPayments(val phastPayClient: PhastPayClient) : GetPaymentsEvent
    data class UpdateMessageError(val messageError: String?): GetPaymentsEvent
    data class UpdatePaymentResult(val paymentListResult: PhastPayGetPaymentsResponse?): GetPaymentsEvent

}