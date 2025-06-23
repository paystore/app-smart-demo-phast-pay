package com.phoebus.demo.phastpay.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class LastTransaction(
    var appClientId: String? = null,
    var paymentId: String? = null
)

object LastTransactionState {

    private var lastTransaction by mutableStateOf(LastTransaction())

    fun getAppClientId(): String? {
        return lastTransaction.appClientId
    }

    fun getPaymentId(): String? {
        return lastTransaction.paymentId
    }

    fun updateAppClientId(appClientId: String) {
        lastTransaction = lastTransaction.copy(appClientId = appClientId)
    }

    fun updatePaymentId(paymentId: String) {
        lastTransaction = lastTransaction.copy(paymentId = paymentId)
    }
}
