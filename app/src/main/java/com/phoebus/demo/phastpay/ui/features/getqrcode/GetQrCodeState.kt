package com.phoebus.demo.phastpay.ui.features.getqrcode

import com.phoebus.demo.phastpay.data.dto.PhastPayGetQrCodeResponse
import com.phoebus.demo.phastpay.data.enums.Service

data class GetQrCodeState (
    val printCustomerReceipt: Boolean = true,
    val printMerchantReceipt: Boolean = true,
    val providerId: String = "",
    val service: Service = Service.TWINT,
    val isLoading: Boolean = false,
    val qrCodeResult: PhastPayGetQrCodeResponse?= null
)