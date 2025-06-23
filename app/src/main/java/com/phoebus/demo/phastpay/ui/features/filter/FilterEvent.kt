package com.phoebus.demo.phastpay.ui.features.filter

import com.phoebus.demo.phastpay.data.enums.DatePeriod
import com.phoebus.demo.phastpay.data.enums.FilterType
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus

sealed interface FilterEvent {
    data class SelectFilterType(val type: FilterType) : FilterEvent
    data class SelectReportType(val reportType: ReportType) : FilterEvent
    data class UpdateTransactionsStatus(val status: List<TransactionStatus>) : FilterEvent
    data class SelectDatePeriod(val period: DatePeriod) : FilterEvent
    data class UpdateStartDate(val date: String) : FilterEvent
    data class UpdateEndDate(val date: String) : FilterEvent
    data class UpdateValue(val value: String?) : FilterEvent
    data class UpdateStartTime(val time: String) : FilterEvent
    data class UpdateEndTime(val time: String) : FilterEvent
    data object ShowDatePicker : FilterEvent
    data object ShowTimePicker : FilterEvent
    data object DismissPicker : FilterEvent
    data class SelectService(val service: ServiceType) : FilterEvent

}

sealed interface FilterNavigationEvents {
    data class NavigateToGetTransactions(val startDateTime: String, val endDateTime: String) :
        FilterNavigationEvents

    data class NavigateToGetPayments(
        val startDateTime: String,
        val endDateTime: String,
        val status: List<TransactionStatus>,
        val value: String? = ""
    ) :
        FilterNavigationEvents

    data class NavigateToRefundList(
        val startDateTime: String,
        val endDateTime: String,
    ) :
        FilterNavigationEvents

    data class NavigateToReport(
        val startDateTime: String,
        val endDateTime: String,
        val reportType: ReportType,
        val serviceType: ServiceType,
    ) :
        FilterNavigationEvents
}
