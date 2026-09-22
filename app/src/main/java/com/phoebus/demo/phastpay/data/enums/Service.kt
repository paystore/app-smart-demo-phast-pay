package com.phoebus.demo.phastpay.data.enums
import com.phoebus.demo.phastpay.R

import androidx.annotation.StringRes

enum class Service {
    CRYPTO,
    MBWAY,
    PIX,
    TWINT,
    BIZUM;
}

@StringRes
fun Service.getStringRes(): Int = when (this) {
    Service.BIZUM -> R.string.bizum_acquirer
    Service.TWINT -> R.string.twint_acquirer
    Service.MBWAY -> R.string.mbway_acquirer
    Service.CRYPTO -> R.string.crypto_acquirer
    Service.PIX -> R.string.pix_acquirer
}