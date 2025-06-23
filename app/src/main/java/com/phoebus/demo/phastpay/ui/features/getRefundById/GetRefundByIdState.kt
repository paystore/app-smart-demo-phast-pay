package com.phoebus.demo.phastpay.ui.features.getRefundById

import com.phoebus.demo.phastpay.data.dto.PhastPayGetRefundByIdResponse

data class GetRefundByIdState (
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val refundId: String = "",
    val errorMessage: String? = null,
    val refundResult: PhastPayGetRefundByIdResponse?= null
)