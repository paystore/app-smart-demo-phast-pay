package com.phoebus.demo.phastpay.ui.features.getPaymentById

import com.phoebus.demo.phastpay.utils.LastTransactionState

data class GetPaymentByIdState(
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val paymentId: String = LastTransactionState.getPaymentId() ?: "",
    val errorMessage: String? = null,
    val successMessage: String? = null
)