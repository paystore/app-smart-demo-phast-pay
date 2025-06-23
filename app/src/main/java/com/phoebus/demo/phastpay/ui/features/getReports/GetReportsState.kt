package com.phoebus.demo.phastpay.ui.features.getReports

import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType

data class GetReportsState(
    val startDate: String = "",
    val endDate: String = "",
    val reportType: ReportType = ReportType.SUMMARY,
    val serviceType: ServiceType = ServiceType.ALL,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
