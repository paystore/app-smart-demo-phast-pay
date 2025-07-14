package com.phoebus.demo.phastpay.ui.features.filter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.enums.DatePeriod
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.demo.phastpay.utils.DateUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class FilterViewModel : ViewModel() {

    private val _state = mutableStateOf(FilterState())
    val state: State<FilterState> = _state

    private val _navigationEvent = MutableSharedFlow<FilterNavigationEvents>()
    val navigationEvent: SharedFlow<FilterNavigationEvents> = _navigationEvent.asSharedFlow()


    fun navigateToGetTransactions(startDateTime: String, endDateTime: String) {
        viewModelScope.launch {
            _navigationEvent.emit(
                FilterNavigationEvents.NavigateToGetTransactions(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime
                )
            )
        }
    }

    fun navigateToPaymentsToRefund(startDateTime: String, endDateTime: String) {
        viewModelScope.launch {
            _navigationEvent.emit(
                FilterNavigationEvents.NavigateToRefundList(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime
                )
            )
        }
    }

    fun navigateToReport(
        startDateTime: String,
        endDateTime: String,
        reportType: ReportType,
        reportSelected: ServiceType
    ) {
        viewModelScope.launch {
            _navigationEvent.emit(
                FilterNavigationEvents.NavigateToReport(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                    reportType = reportType,
                    serviceType = reportSelected
                )
            )
        }
    }

    fun navigateToGetListPayments(
        startDateTime: String,
        endDateTime: String,
        status: List<TransactionStatus>,
        value: String?
    ) {
        viewModelScope.launch {
            _navigationEvent.emit(
                FilterNavigationEvents.NavigateToGetPayments(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                    status = status,
                    value = value,
                )
            )
        }
    }


    fun onEvent(event: FilterEvent) {
        when (event) {
            is FilterEvent.SelectFilterType -> {
                _state.value = _state.value.copy(filterType = event.type)
            }

            is FilterEvent.UpdateTransactionsStatus -> {
                _state.value = _state.value.copy(status = event.status)
            }

            is FilterEvent.UpdateValue -> {
                _state.value = _state.value.copy(value = event.value)
            }

            is FilterEvent.SelectReportType -> {
                _state.value = _state.value.copy(reportType = event.reportType)
            }

            is FilterEvent.SelectDatePeriod -> {
                val newState = when (event.period) {
                    DatePeriod.THIS_MONTH -> {
                        val calendarStart = Calendar.getInstance().apply {
                            set(Calendar.DAY_OF_MONTH, 1)
                        }
                        val startDate = DateUtils.formatDate(calendarStart.time)

                        val calendarEnd = Calendar.getInstance().apply {
                            set(
                                Calendar.DAY_OF_MONTH,
                                calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH)
                            )
                        }

                        val endDate = DateUtils.formatDate(calendarEnd.time)

                        _state.value.copy(
                            periodSelected = event.period,
                            startDate = startDate,
                            endDate = endDate,
                            startTime = "00:00",
                            endTime = "23:59"
                        )
                    }

                    DatePeriod.TODAY -> {
                        val today = DateUtils.formatDate(Date())

                        _state.value.copy(
                            periodSelected = event.period,
                            startDate = today,
                            endDate = today,
                            startTime = "00:00",
                            endTime = "23:59"
                        )
                    }

                    DatePeriod.OTHER_PERIOD -> {
                        val now = Calendar.getInstance()
                        val currentDay = now[Calendar.DAY_OF_MONTH]

                        val endDate = DateUtils.formatDate(now.time)

                        val calendarStart = (now.clone() as Calendar).apply {
                            add(Calendar.MONTH, -1)
                            val maxDayOfMonth = getActualMaximum(Calendar.DAY_OF_MONTH)
                            set(Calendar.DAY_OF_MONTH, minOf(currentDay, maxDayOfMonth))
                        }
                        val startDate = DateUtils.formatDate(calendarStart.time)

                        _state.value.copy(
                            periodSelected = event.period,
                            startDate = startDate,
                            endDate = endDate,
                            startTime = "00:00",
                            endTime = "23:59"
                        )
                    }
                }

                _state.value = newState
            }

            is FilterEvent.UpdateStartDate -> {
                _state.value = _state.value.copy(startDate = event.date)
            }

            is FilterEvent.UpdateEndDate -> {
                _state.value = _state.value.copy(endDate = event.date)
            }

            is FilterEvent.UpdateStartTime -> {
                _state.value = _state.value.copy(startTime = event.time)
            }

            is FilterEvent.UpdateEndTime -> {
                _state.value = _state.value.copy(endTime = event.time)
            }

            FilterEvent.ShowDatePicker -> {
                _state.value = _state.value.copy(showDatePicker = true)
            }

            FilterEvent.ShowTimePicker -> {
                _state.value = _state.value.copy(showTimePicker = true)
            }

            FilterEvent.DismissPicker -> {
                _state.value = _state.value.copy(showDatePicker = false, showTimePicker = false)
            }

            is FilterEvent.SelectService -> {
                _state.value = _state.value.copy(
                    selectedService = event.service
                )
            }
        }
    }
}

