package com.phoebus.demo.phastpay.ui.features.startPayment

import com.phoebus.demo.phastpay.data.enums.Service
import java.util.UUID

data class PaymentState(
    val appClientId: String = UUID.randomUUID().toString(),
    val applicationId: String = "",
    val applicationName: String = "",
    val service: String = Service.MBWAY.name,

    val sendValue: Boolean = false,
    val value: String? = null,
    val currency: String = "EUR",
    val sendCurrency: Boolean = true,
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val previewCustomerReceipt: Boolean = true,
    val previewMerchantReceipt: Boolean = true,
    val sendPhoneNumber: Boolean = false,
    val phoneNumber: String? = null,
    val countryCode: String? = null,
    val sendTipValue: Boolean = false,
    val additionalValue: String? = null,
    val switchAdditionalInfo: Boolean = false,
    val customerName: String? = null,
    val customerEmail: String? = null,
    val switchSendProviderId: Boolean = false,
    val providerId: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResult: String? = null
)
