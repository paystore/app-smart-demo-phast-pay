package com.phoebus.demo.phastpay.ui.features.getReports

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetReportsRequest
import com.phoebus.demo.phastpay.services.GetReportsService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GetReportsViewModel() : ViewModel() {

    private val _state = mutableStateOf(GetReportsState())
    val state: State<GetReportsState> = _state

    private val _effect = Channel<GetReportsEffect>()
    val effect = _effect.receiveAsFlow()


    fun onEvent(event: GetReportsEvent) {
        when (event) {
            is GetReportsEvent.FetchReport -> {
                fetchReport(event.phastPayClient)
            }

            is GetReportsEvent.FetchReportWithMessage -> {
                fetchReport(event.phastPayClient)
            }

            is GetReportsEvent.UpdateParams -> {
                _state.value = _state.value.copy(
                    startDate = event.startDate,
                    endDate = event.endDate,
                    serviceType = event.serviceType,
                    reportType = event.reportType
                )
            }
        }
    }


    private fun fetchReport(phastPayClient: PhastPayClient) {
        viewModelScope.launch {
            val request = PhastPayGetReportsRequest(
                startDate = state.value.startDate,
                endDate = state.value.endDate,
                reportType = state.value.reportType,
                service = state.value.serviceType,
            )
            GetReportsService()(phastPayClient, request).collect { result ->
                result.onSuccess {
                    _effect.send(GetReportsEffect.Success)
                }
                result.onFailure { error ->
                    _effect.send(
                        GetReportsEffect.Error(error.message ?: "Unknown error when querying report")
                    )
                }
            }
        }
    }


}
