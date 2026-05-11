package com.phoebus.demo.phastpay.ui.features.printReceipt

import com.phoebus.demo.phastpay.utils.LastTransactionState

data class PrintReceiptState (
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val previewCustomerReceipt: Boolean = true,
    val previewMerchantReceipt: Boolean = true,
    val refundId: String = LastTransactionState.getRefundId() ?: "",
    val paymentId: String = LastTransactionState.getPaymentId() ?: "",
    val appClientId: String = LastTransactionState.getAppClientId() ?: "",
    val applicationId: String = "",
    val applicationName: String = "",
){
    fun isButtonEnabled(): Boolean{
        return appClientId.isNotBlank() || paymentId.isNotBlank() || refundId.isNotEmpty()
    }
}