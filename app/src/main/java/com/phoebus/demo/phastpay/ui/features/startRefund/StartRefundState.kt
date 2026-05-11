package com.phoebus.demo.phastpay.ui.features.startRefund

import com.phoebus.demo.phastpay.utils.LastTransactionState

data class StartRefundState(
    val applicationId: String = "",
    val applicationName: String = "",
    val value: String? = null,
    val paymentId: String = LastTransactionState.getPaymentId() ?: "",
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val previewCustomerReceipt: Boolean = true,
    val previewMerchantReceipt: Boolean = true,
    val phoneNumber: String? = null,
    val errorMessage: String? = null,
    val sendPartialValue: Boolean = false,
    val refundResult: String? = null,
)