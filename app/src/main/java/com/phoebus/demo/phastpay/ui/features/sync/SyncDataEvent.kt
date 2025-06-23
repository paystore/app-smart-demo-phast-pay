package com.phoebus.demo.phastpay.ui.features.sync

import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface SyncDataEvent {
    data class UpdateLoading(val loading: Boolean): SyncDataEvent
    data class StartSync(val phastPayClient: PhastPayClient): SyncDataEvent
    data class UpdateDialogMessage(val message: String?): SyncDataEvent
}

sealed interface SyncDataNavigationEvents {
    data object NavigateToHome :
        SyncDataNavigationEvents
}