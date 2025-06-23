package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund

import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsToRefundResponse

data class GetPaymentsToRefundState(
    val startDate: String = "",
    val endDate: String = "",
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val refundResult: PhastPayGetPaymentsToRefundResponse? = null,
    val errorMessage: String? = null
)
