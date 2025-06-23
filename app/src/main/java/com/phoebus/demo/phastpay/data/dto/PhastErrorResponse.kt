package com.phoebus.demo.phastpay.data.dto

import com.google.gson.annotations.SerializedName

data class PhastErrorResponse(
    @SerializedName("error_message")
    val errorMessage: String
)