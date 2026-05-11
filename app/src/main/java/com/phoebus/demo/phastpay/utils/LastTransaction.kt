package com.phoebus.demo.phastpay.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class LastTransaction(
    val appClientId: String? = null,
    val paymentId: String? = null,
    val refundId: String? = null
)

object LastTransactionState {

    private var lastTransaction by mutableStateOf(LastTransaction())

    fun getAppClientId(): String? {
        return lastTransaction.appClientId
    }

    fun getPaymentId(): String? {
        return lastTransaction.paymentId
    }

    fun getRefundId(): String? {
        return lastTransaction.refundId
    }

    fun updateAppClientId(appClientId: String) {
        lastTransaction = lastTransaction.copy(appClientId = appClientId)
    }

    fun updatePaymentId(paymentId: String) {
        lastTransaction = lastTransaction.copy(paymentId = paymentId)
    }

    fun updateRefundId(refundId: String) {
        lastTransaction = lastTransaction.copy(refundId = refundId)
    }
}
