package com.phoebus.demo.phastpay.services

import android.util.Log
import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayGetQrCodeRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayGetQrCodeResponse
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetQrCodeService @Inject constructor(
    private val json: Json
) {

    operator fun invoke(
        phastPayClient: PhastPayClient,
        getQrCodeRequest: PhastPayGetQrCodeRequest,
    ) = callbackFlow {
        val callback = object : PhastPayClient.ICallbackService {
            override fun onError(response: String?) {
                // Proteja o parsing com runCatching
                val result = runCatching {
                    val responseError = response?.let { json.decodeFromString<PhastErrorResponse>(it) }
                    Exception(responseError?.errorMessage)
                }.getOrElse { Exception("Erro desconhecido: $response") }

                trySend(Result.failure(result))
                close()
            }

            override fun onSuccess(response: String?) {
                if (response.isNullOrBlank()) {
                    Log.d(ConstantsUtils.TAG, "Resposta vazia do app-phaspay")
                    close()
                    return
                }

                val result = runCatching {
                    json.decodeFromString< PhastPayGetQrCodeResponse>(response)
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
            val request = json.encodeToString(getQrCodeRequest)
            phastPayClient.getQrCode(request, callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }

        // O awaitClose deve ficar aqui.
        // Se o SDK permitir cancelar a requisição, coloque o código de cancelamento aqui.
        awaitClose {
            /* Ex: phastPayClient.cancel(callback) */
        }
    }
}