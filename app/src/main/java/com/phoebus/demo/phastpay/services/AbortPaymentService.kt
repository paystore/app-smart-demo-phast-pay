package com.phoebus.demo.phastpay.services

import android.util.Log
import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayAbortPaymentRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayAbortPaymentResponse
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AbortPaymentService @Inject constructor(
    private val json: Json
) {
    operator fun invoke(
        phastPayClient: PhastPayClient,
        phastPayAbortPaymentRequest: PhastPayAbortPaymentRequest
    ) = callbackFlow {
        val callback = object : PhastPayClient.ICallbackService {
            override fun onError(response: String?) {
                val responseError = response?.let {
                    runCatching { json.decodeFromString<PhastErrorResponse>(it) }.getOrNull()
                }
                trySend(Result.failure(Exception(responseError?.errorMessage ?: response ?: "Erro desconhecido")))
                close()
            }

            override fun onSuccess(response: String?) {
                if (response.isNullOrBlank()) {
                    Log.d(ConstantsUtils.TAG, "Resposta vazia do app-phastpay")
                    close()
                    return
                }

                val result = runCatching {
                    json.decodeFromString<PhastPayAbortPaymentResponse>(response)
                }

                result.onSuccess { res ->
                    trySend(Result.success(res))
                }.onFailure { erro ->
                    trySend(Result.failure(Exception("Falha ao processar dados: ${erro.message}")))
                }

                close()
            }
        }

        try {
            val request = json.encodeToString(phastPayAbortPaymentRequest)
            phastPayClient.abortPayment(request, callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose { }
    }
}
