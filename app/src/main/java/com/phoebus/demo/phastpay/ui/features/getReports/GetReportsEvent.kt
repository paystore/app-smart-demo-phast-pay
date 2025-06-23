package com.phoebus.demo.phastpay.ui.features.getReports

import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetReportsEvent {
    data class UpdateParams(
        val startDate: String,
        val endDate: String,
        val serviceType: ServiceType,
        val reportType: ReportType
    ) : GetReportsEvent
    data class FetchReport(val phastPayClient: PhastPayClient) : GetReportsEvent
    data class FetchReportWithMessage(
        val phastPayClient: PhastPayClient,
        val successMessage: String,
        val errorMessage: String
    ) : GetReportsEvent
}
