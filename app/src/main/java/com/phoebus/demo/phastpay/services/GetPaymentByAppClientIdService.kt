package com.phoebus.demo.phastpay.services

import android.util.Log
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByAppClientIdRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByAppClientIdResponse
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetPaymentByAppClientIdService @Inject constructor(
    private val json: Json
) {
    operator fun invoke(
        phastPayClient: PhastPayClient,
        phastPayGetPaymentByAppClientIdRequest: PhastPayGetPaymentByAppClientIdRequest
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
                    json.decodeFromString<PhastPayGetPaymentByAppClientIdResponse>(response)
                }

                result.onSuccess { res ->
                    trySend(Result.success(res))
                }.onFailure { erro ->
                    // captura erros de parsing, JSON malformado, etc.
                    trySend(Result.failure(Exception("Falha ao processar dados: ${erro.message}")))
                }
                close()
            }
        }

        try {
            val request = json.encodeToString(phastPayGetPaymentByAppClientIdRequest)
            phastPayClient.getPaymentByAppClientId(request, callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose {  }
    }

}