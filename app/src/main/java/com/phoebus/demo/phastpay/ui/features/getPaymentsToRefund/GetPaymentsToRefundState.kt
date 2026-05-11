package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund

data class GetPaymentsToRefundState(
    val startDate: String? = "",
    val endDate: String? = "",
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val previewCustomerReceipt: Boolean = true,
    val previewMerchantReceipt: Boolean = true,
    val refundResult: String? = null,
    val errorMessage: String? = null
)
