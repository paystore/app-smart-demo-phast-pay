package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PhastPaySyncronizeResponse (
    @SerializedName("result")
    val result: Boolean
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}