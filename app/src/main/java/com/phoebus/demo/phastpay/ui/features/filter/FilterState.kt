package com.phoebus.demo.phastpay.ui.features.filter

import com.phoebus.demo.phastpay.data.enums.DatePeriod
import com.phoebus.demo.phastpay.data.enums.FilterType
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.demo.phastpay.utils.DateUtils

data class FilterState(
    val filterType: FilterType = FilterType.GET_TRANSACTIONS,
    val selectedService: ServiceType = ServiceType.ALL,
    val reportType: ReportType = ReportType.SUMMARY,
    val periodSelected: DatePeriod = DatePeriod.TODAY,
    val startDate: String = DateUtils.getCurrentDateFormatted(),
    val endDate: String = DateUtils.getCurrentDateFormatted(),
    val value: String? = null,
    val status: List<TransactionStatus> = listOf(
        TransactionStatus.REQUEST_PAYMENT,
        TransactionStatus.CONFIRMED_PAYMENT,
        TransactionStatus.CANCELED_PAYMENT,
        TransactionStatus.WAITING_PAYMENT,
        TransactionStatus.ERROR_PAYMENT,
        TransactionStatus.EXPIRED_PAYMENT,
        TransactionStatus.PARTIAL_REFUND,
        TransactionStatus.COMPLETED_REFUND,
    ),
    val startTime: String = "00:00",
    val endTime: String = "23:59",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val selectingStartDate: Boolean = true,
)

