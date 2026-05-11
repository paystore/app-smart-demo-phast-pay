package com.phoebus.demo.phastpay.ui.navigation

import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import kotlinx.serialization.Serializable

@Serializable
sealed interface RouteParams {
    @Serializable
    data class GetPayments(
        val startData: String,
        val endData: String,
        val status: List<TransactionStatus>,
        val value: String?,
    ) : RouteParams

    @Serializable
    data class GetTransactions(
        val startData: String,
        val endData: String,
    ) : RouteParams
    @Serializable
    data class GetPaymentsToRefund(
        val startData: String? = null,
        val endData: String? = null,
    ) : RouteParams

    @Serializable
    data class GetReports(
        val startData: String,
        val endData: String,
        val reportType: ReportType,
        val service: ServiceType
    ) : RouteParams
}