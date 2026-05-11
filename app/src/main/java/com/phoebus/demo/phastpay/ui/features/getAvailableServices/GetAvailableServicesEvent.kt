package com.phoebus.demo.phastpay.ui.features.getAvailableServices

import com.phoebus.demo.phastpay.data.dto.PhastPayGetAvailableServicesResponse

sealed interface GetAvailableServicesEvent {
    data class UpdateLoading(val loading: Boolean): GetAvailableServicesEvent
    data object StartGetServices: GetAvailableServicesEvent
    data class UpdateAvailableServices(val services: PhastPayGetAvailableServicesResponse):  GetAvailableServicesEvent
    data class UpdateErrorMessage(val message: String? = "") : GetAvailableServicesEvent
}

sealed class  GetAvailableServicesEffect {
    data class ShowToast(val message: String) : GetAvailableServicesEffect()
}