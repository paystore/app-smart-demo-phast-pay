package com.phoebus.demo.phastpay.ui.features.startPayment

import com.phoebus.demo.phastpay.data.enums.Service
import java.util.UUID

data class PaymentState(
    val appClientId: String = UUID.randomUUID().toString(),
    val applicationId: String = "100231",
    val applicationName: String = "PhastPay Demo",
    val service: String = Service.MBWAY.name,

    val sendValue: Boolean = false,
    val value: String? = null,
    val currency: String? = null,
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val sendPhoneNumber: Boolean = false,
    val phoneNumber: String? = null,
    val countryCode: String? = null,
    val switchAdditionalInfo: Boolean = false,
    val customerName: String? = null,
    val customerEmail: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResult: String? = null
)
