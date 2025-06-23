package com.phoebus.demo.phastpay.services

import com.google.gson.Gson
import com.phoebus.demo.phastpay.data.dto.PhastErrorResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayGetRefundByIdRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayGetRefundByIdResponse
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class GetRefundByIdService {
    operator fun invoke(
        phastPayClient: PhastPayClient,
        phastPayGetRefundByIdRequest: PhastPayGetRefundByIdRequest
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
                    gson.fromJson(response, PhastPayGetRefundByIdResponse::class.java)
                trySend(Result.success(responseObject))
                close()
            }
        }

        try {
            val request = phastPayGetRefundByIdRequest.toJson()
            phastPayClient.getRefundById(request, callback)
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
        awaitClose { }
    }

}