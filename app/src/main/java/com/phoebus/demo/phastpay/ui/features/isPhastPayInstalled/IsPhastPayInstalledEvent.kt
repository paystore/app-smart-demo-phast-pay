package com.phoebus.demo.phastpay.ui.features.isPhastPayInstalled

import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface IsPhastPayInstalledEvent {
    data class UpdateLoading(val loading: Boolean) : IsPhastPayInstalledEvent
    data class StartCheck(val phastPayClient: PhastPayClient) : IsPhastPayInstalledEvent
    data class UpdateIsAppInstalled(val isAppInstalled: Boolean) : IsPhastPayInstalledEvent
    data class UpdateErrorMessage(val message: String?) : IsPhastPayInstalledEvent
}