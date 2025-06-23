package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PhastPayGetPaymentByAppClientIdRequest(
@SerializedName("app_client_id")
val appClientId: String = ""
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}