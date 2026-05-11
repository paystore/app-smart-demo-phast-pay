package com.phoebus.demo.phastpay.data.enums

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.phoebus.demo.phastpay.R

enum class TransactionStatus {
    REQUEST_PAYMENT,
    WAITING_PAYMENT,
    CONFIRMED_PAYMENT,
    CANCELED_PAYMENT,
    ABORT_PAYMENT,
    EXPIRED_PAYMENT,
    ERROR_PAYMENT,
    REQUEST_REFUND,
    PARTIAL_REFUND,
    ERROR_REFUND,
    COMPLETED_REFUND,
    UNKNOWN,
    REFUNDED;

    companion object {
        fun fromString(value: String = "", default: TransactionStatus = UNKNOWN): TransactionStatus {
            return try {
                TransactionStatus.valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}

@StringRes
fun TransactionStatus.getResId(): Int = when(this){
        TransactionStatus.REQUEST_PAYMENT ->R.string.filter_status_request_payment
        TransactionStatus.WAITING_PAYMENT -> R.string.filter_status_waiting_payment
        TransactionStatus.CONFIRMED_PAYMENT ->R.string.filter_status_confirmed_payment
        TransactionStatus.CANCELED_PAYMENT -> R.string.filter_status_canceled_payment
        TransactionStatus.EXPIRED_PAYMENT -> R.string.filter_status_expired_payment
        TransactionStatus.ERROR_PAYMENT -> R.string.filter_status_error_payment
        TransactionStatus.ERROR_REFUND -> R.string.filter_status_error_payment
        TransactionStatus.REQUEST_REFUND -> R.string.filter_status_request_refund
        TransactionStatus.PARTIAL_REFUND -> R.string.filter_status_partial_refund
        TransactionStatus.COMPLETED_REFUND -> R.string.filter_status_completed_refund
        TransactionStatus.REFUNDED -> R.string.filter_status_refunded
        TransactionStatus.ABORT_PAYMENT -> R.string.filter_status_aborted
        TransactionStatus.UNKNOWN -> R.string.empty
}