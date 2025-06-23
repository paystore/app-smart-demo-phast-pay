package com.phoebus.demo.phastpay.ui.features.getTransactions

import com.phoebus.demo.phastpay.data.dto.PhastPayGetTransactionsResponse

data class GetTransactionsState(
    val startDate: String = "",
    val endDate: String = "",
    val errorMessage: String? = null,
    val getListResult: PhastPayGetTransactionsResponse? = null,
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
)
