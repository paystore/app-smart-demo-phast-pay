package com.phoebus.demo.phastpay.utils

enum class CurrencyType {
    BRL, EUR, USD, GBP, ARS, CNY, RUB, SZL, SEK, PLN, RON, BGN, CZK, BAM, ALL, MKD, CHF;

    companion object {
        fun fromString(value: String, default: CurrencyType = EUR): CurrencyType {
            return try {
                CurrencyType.valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}