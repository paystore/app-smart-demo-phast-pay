package com.phoebus.demo.phastpay.ui.features.getPaymentByAppClientId

import com.phoebus.demo.phastpay.utils.LastTransactionState

data class GetPaymentByAppClientIdState (
    val appClientId: String = LastTransactionState.getAppClientId() ?: "",
    val errorMessage: String? = null,
    val successMessage: String?= null
)