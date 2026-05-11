package com.phoebus.demo.phastpay.services

import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayPrintReceiptRequest
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PrintReceiptService @Inject constructor(
    private val json: Json
) {
    operator fun invoke(
        phastPayClient: PhastPayClient,
        phastPayPrintReceiptRequest: PhastPayPrintReceiptRequest
    ) = callbackFlow {

        val callback = object : PhastPayClient.ICallbackService {
            override fun onError(response: String?) {
                val responseError = response?.let { json.decodeFromString<PhastErrorResponse>(it) }
                trySend(Result.failure(Exception(responseError?.errorMessage)))
                close()
            }

            override fun onSuccess(response: String?) {
                trySend(Result.success(response))
                close()
            }
        }

        try {
            val request = json.encodeToString(phastPayPrintReceiptRequest)
            phastPayClient.printReceipt(request, callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose { }
    }

}