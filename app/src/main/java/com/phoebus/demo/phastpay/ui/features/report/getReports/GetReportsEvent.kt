package com.phoebus.demo.phastpay.ui.features.report.getReports

import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType

sealed interface GetReportsEvent {
    data class UpdateParams(
        val startDate: String,
        val endDate: String,
        val serviceType: ServiceType,
        val reportType: ReportType
    ) : GetReportsEvent
    data object FetchReport : GetReportsEvent
    data class FetchReportWithMessage(
        val successMessage: String,
        val errorMessage: String
    ) : GetReportsEvent
}
