package com.phoebus.demo.phastpay.ui.features.getRefundById

import com.phoebus.demo.phastpay.utils.LastTransactionState

data class GetRefundByIdState (
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val previewCustomerReceipt: Boolean = true,
    val previewMerchantReceipt: Boolean = true,
    val refundId: String = LastTransactionState.getRefundId() ?: "",
    val errorMessage: String? = null,
    val refundResult: String?= null
)