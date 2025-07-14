package com.phoebus.demo.phastpay.ui.features.startRefund

import com.phoebus.demo.phastpay.data.dto.PhastPayStartRefundResponse
import java.util.UUID

data class StartRefundState (
    val applicationId: String = "100231",
    val applicationName: String = "PhastPay Demo",
    val value: String? = null,
    val paymentId: String = "",
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val phoneNumber: String? = null,
    val errorMessage: String? = null,
    val sendPartialValue: Boolean = false,
    val refundResult: PhastPayStartRefundResponse? = null,
)