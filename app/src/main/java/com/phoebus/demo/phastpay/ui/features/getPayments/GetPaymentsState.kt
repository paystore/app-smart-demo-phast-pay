package com.phoebus.demo.phastpay.ui.features.getPayments

import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsResponse
import com.phoebus.demo.phastpay.data.enums.TransactionStatus

data class GetPaymentsState(
    val startDate: String = "",
    val endDate: String = "",
    val status: List<TransactionStatus> = listOf(
        TransactionStatus.REQUEST_PAYMENT,
        TransactionStatus.CONFIRMED_PAYMENT,
        TransactionStatus.CANCELED_PAYMENT,
        TransactionStatus.WAITING_PAYMENT,
        TransactionStatus.ERROR_PAYMENT,
        TransactionStatus.EXPIRED_PAYMENT,
    ),
    val value: String? = null,
    val errorMessage: String? = null,
    val getListResult: PhastPayGetPaymentsResponse? = null
)
