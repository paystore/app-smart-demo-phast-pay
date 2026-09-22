package com.phoebus.demo.phastpay.ui.features.startPaymentApi

import com.phoebus.demo.phastpay.data.dto.PhastPayGetQrCodeResponse
import com.phoebus.demo.phastpay.data.enums.Service
import java.util.UUID

data class PaymentApiState(
    val appClientId: String = UUID.randomUUID().toString(),
    val applicationId: String = "100231",
    val applicationName: String = "PhastPay Demo",
    val service: String = Service.TWINT.name,

    val sendValue: Boolean = true,
    val sendTipValue: Boolean = false,
    val value: String? = null,
    val tipValue: String? = null,
    val currency: String = "EUR",
    val sendProviderId: Boolean = true,
    val providerId: String? = null,
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val previewCustomerReceipt: Boolean = true,
    val previewMerchantReceipt: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResult: String? = null,
    val shouldPrintOnDismiss: Boolean = false,
    val qrCodeResult: PhastPayGetQrCodeResponse? = null
){
    fun isPrintEnabled(): Boolean = printMerchantReceipt || printCustomerReceipt || previewCustomerReceipt || previewMerchantReceipt
}
