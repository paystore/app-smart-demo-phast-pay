package com.phoebus.demo.phastpay.ui.features.report.reportMenu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetReportsRequest
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.services.GetReportsService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsMenuViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getReportsService: GetReportsService
) : ViewModel() {

    private val _state = mutableStateOf(ReportsMenuState())
    val state: State<ReportsMenuState> = _state

    fun onEvent(event: ReportsMenuEvent) {
        when (event) {
            ReportsMenuEvent.FilterReports -> {
                filterReports()
            }
            ReportsMenuEvent.ClearMessages -> {
                _state.value = _state.value.copy(successMessage = null, errorMessage = null)
            }
        }
    }

    private fun filterReports() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val request = PhastPayGetReportsRequest(
                startDate = null,
                endDate = null,
                reportType = ReportType.SUMMARY,
                service = ServiceType.ALL
            )
            getReportsService(phastPayClient, request).collect { result ->
                _state.value = _state.value.copy(isLoading = false)
                result.onSuccess {
                    _state.value = _state.value.copy(successMessage = it)
                }.onFailure {
                    _state.value = _state.value.copy(errorMessage = it.message ?: "Unknown Error")
                }
            }
        }
    }
}
