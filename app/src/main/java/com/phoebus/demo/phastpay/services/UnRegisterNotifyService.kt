package com.phoebus.demo.phastpay.services

import android.util.Log
import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayStartPaymentResponse
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UnRegisterNotifyService @Inject constructor(
    private val json: Json,
    private val phastPayClient: PhastPayClient,
) {
    operator fun invoke(
    ) = callbackFlow {
        val callback = object : PhastPayClient.ICallbackService {
            override fun onError(response: String?) {
                val responseError = response?.let { json.decodeFromString<PhastErrorResponse>(it) }
                trySend(Result.failure(Exception(responseError?.errorMessage)))
                close()
            }

            override fun onSuccess(response: String?) {
                if (response.isNullOrBlank()) {
                    Log.d(ConstantsUtils.TAG, "Resposta vazia do app-phaspay")
                    close()
                    return
                }

                val result = runCatching {
                    json.decodeFromString<PhastPayStartPaymentResponse>(response)
                }

                result.onSuccess { res ->
                    trySend(Result.success(res))
                }.onFailure { erro ->
                    // Aqui você captura erros de parsing, JSON malformado, etc.
                    trySend(Result.failure(Exception("Falha ao processar dados: ${erro.message}")))
                }

                close()
            }
        }

        try {
            phastPayClient.unRegisterNotify(callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose {  }
    }

}