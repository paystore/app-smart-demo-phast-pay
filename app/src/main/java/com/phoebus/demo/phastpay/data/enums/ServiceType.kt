package com.phoebus.demo.phastpay.data.enums

import androidx.annotation.StringRes
import com.phoebus.demo.phastpay.R

enum class ServiceType {
    ALL,
    CRYPTO,
    MBWAY,
    PIX,
    TWINT,
    BIZUM;

    companion object {
        fun fromString(value: String, default: ServiceType = ALL): ServiceType {
            return try {
                ServiceType.valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}

@StringRes
fun ServiceType.getStringRes(): Int = when (this) {
    ServiceType.ALL -> R.string.all_acquirers
    ServiceType.BIZUM -> R.string.bizum_acquirer
    ServiceType.CRYPTO -> R.string.crypto_acquirer
    ServiceType.TWINT -> R.string.twint_acquirer
    ServiceType.MBWAY -> R.string.mbway_acquirer
    ServiceType.PIX -> R.string.pix_acquirer
}