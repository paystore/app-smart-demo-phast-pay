package com.phoebus.demo.phastpay.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType

data class PhastPayGetReportsRequest(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("report_type")
    val reportType: ReportType,

    @SerializedName("service")
    val service: ServiceType,
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
