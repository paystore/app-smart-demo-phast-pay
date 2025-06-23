package com.phoebus.demo.phastpay.ui.features.getAvailableServices

import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetAvailableServicesEvent {
    data class UpdateLoading(val loading: Boolean): GetAvailableServicesEvent
    data class StartGetServices(val phastPayClient: PhastPayClient): GetAvailableServicesEvent
    data class UpdateDialogMessage(val message: String?): GetAvailableServicesEvent
}

sealed interface GetAvailableServicesNavigationEvents {
    data object NavigateToHome :
        GetAvailableServicesNavigationEvents
}