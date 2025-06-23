package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.phoebus.demo.phastpay.data.enums.Service

data class PhastPayGetAvailableServicesResponse (
    val services: List<Service>
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}