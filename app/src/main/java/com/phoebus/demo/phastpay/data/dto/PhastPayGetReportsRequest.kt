package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetReportsRequest(
    @SerialName("start_date")
    val startDate: String? = null,

    @SerialName("end_date")
    val endDate: String? = null,

    @SerialName("report_type")
    val reportType: ReportType,

    @SerialName("service")
    val service: ServiceType,
)
