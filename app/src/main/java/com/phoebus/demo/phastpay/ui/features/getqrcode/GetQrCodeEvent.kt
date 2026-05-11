package com.phoebus.demo.phastpay.ui.features.getqrcode

import com.phoebus.demo.phastpay.data.dto.PhastPayGetQrCodeResponse
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetQrCodeEvent {
    data object OnSubmit : GetQrCodeEvent
    data class UpdateIsLoading(val isLoading: Boolean) : GetQrCodeEvent
    data class UpdateProviderId(val providerId: String) : GetQrCodeEvent
    data class UpdateQrCode(val qrCodeResult: PhastPayGetQrCodeResponse?) : GetQrCodeEvent
    data class UpdateService(val service: Service) : GetQrCodeEvent
    data class UpdateErrorMessage(val message: String? = "") : GetQrCodeEvent
}

sealed class GetQrCodeSideEffect {
    data class ShowToast(val message: String) : GetQrCodeSideEffect()
}