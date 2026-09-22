package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import kotlinx.serialization.Serializable

@Serializable
class PhastPayStartPaymentApiResponse(
    val correlationId: String,
    val transactionId: String,
    val status: TransactionStatus,
    val dateTimeOrder: String,
    val qrcode: PhastPayGetQrCodeResponse?,
    val providerData: PhastPayProviderData?
)

