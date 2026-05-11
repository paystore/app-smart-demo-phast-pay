package com.phoebus.demo.phastpay.services

import com.phoebus.phastpay.sdk.client.PhastPayClient
import javax.inject.Inject

class RegisterNotifyService @Inject constructor() {
    fun execute(
        phastPayClient: PhastPayClient,
        callback: PhastPayClient.IRegisterNotifyCallback
    ) {
        try {
            phastPayClient.registerNotify(callback)
        } catch (e: Exception) {
            callback.onError(e.message)
        }

    }
}