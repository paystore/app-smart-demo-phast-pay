package com.phoebus.demo.phastpay.data.enums

enum class TransactionStatus {
    REQUEST_PAYMENT,
    WAITING_PAYMENT,
    CONFIRMED_PAYMENT,
    CANCELED_PAYMENT,
    EXPIRED_PAYMENT,
    ERROR_PAYMENT,
    REQUEST_REFUND,
    PARTIAL_REFUND,
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