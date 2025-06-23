package com.phoebus.demo.phastpay.services

import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByAppClientIdRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByAppClientIdResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class GetPaymentByAppClientIdService {

    operator fun invoke(
        phastPayClient: PhastPayClient,
        phastPayGetPaymentByAppClientIdRequest: PhastPayGetPaymentByAppClientIdRequest
    ) = callbackFlow {
        val gson = Gson()
        val callback = object : PhastPayClient.ICallbackService {
            override fun onError(response: String?) {
                val responseError = gson.fromJson(response, PhastErrorResponse::class.java)
                trySend(Result.failure(Exception(responseError.errorMessage)))
                close()
            }

            override fun onSuccess(response: String?) {
                val responseObject =
                    gson.fromJson(response, PhastPayGetPaymentByAppClientIdResponse::class.java)
                trySend(Result.success(responseObject))
                close()
            }
        }

        try {
            val request = phastPayGetPaymentByAppClientIdRequest.toJson()
            phastPayClient.getPaymentByAppClientId(request, callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose {  }
    }

}