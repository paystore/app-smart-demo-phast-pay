package com.phoebus.demo.phastpay.services

import com.google.gson.Gson
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByIdRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByIdResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class GetPaymentByIdService {

    operator fun invoke(
        phastPayClient: PhastPayClient,
        phastPayGetPaymentByIdRequest: PhastPayGetPaymentByIdRequest
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
                    gson.fromJson(response, PhastPayGetPaymentByIdResponse::class.java)
                trySend(Result.success(responseObject))
                close()
            }
        }

        try {
            val request = phastPayGetPaymentByIdRequest.toJson()
            phastPayClient.getPaymentById(request, callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose {  }
    }

}