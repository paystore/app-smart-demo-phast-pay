package com.phoebus.demo.phastpay.services

import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class IsPhastPayInstalledService {
    operator fun invoke(
        phastPayClient: PhastPayClient
    ) = callbackFlow {
        try {
            val isAppInstalled = phastPayClient.isPhastPayAppInstalled()
            trySend(Result.success(isAppInstalled))
            close()
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose { }
    }
}