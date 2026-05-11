package com.phoebus.demo.phastpay.ui.features.getAvailableServices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.services.GetAvailableServicesService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAvailableServicesModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getAvailableServicesService: GetAvailableServicesService
) : ViewModel() {

    private val _state = MutableStateFlow(GetAvailableServicesState())
    val state: StateFlow<GetAvailableServicesState> = _state.asStateFlow()

    private val _sideEffect = Channel<GetAvailableServicesEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()


    fun onEvent(event: GetAvailableServicesEvent) {
        when (event) {
            is GetAvailableServicesEvent.UpdateLoading -> {
                _state.update { it.copy(loading = event.loading) }
            }

            is GetAvailableServicesEvent.UpdateAvailableServices -> {
                _state.update { it.copy(services = event.services) }
            }

            is GetAvailableServicesEvent.StartGetServices -> {
                viewModelScope.launch {
                    getAvailableServicesData()
                }
            }

            is GetAvailableServicesEvent.UpdateErrorMessage -> {
                viewModelScope.launch {
                    event.message?.let {
                        viewModelScope.launch {
                            _sideEffect.send(GetAvailableServicesEffect.ShowToast(it))
                        }
                    }
                }
            }
        }
    }

    private suspend fun getAvailableServicesData() {
        getAvailableServicesService.invoke(
            phastPayClient,
        ).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    if (response != null) {
                        onEvent(GetAvailableServicesEvent.UpdateAvailableServices(response))
                    }
                    onEvent(GetAvailableServicesEvent.UpdateLoading(false))
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    onEvent(GetAvailableServicesEvent.UpdateLoading(false))
                    if (exception !== null) {
                        onEvent(GetAvailableServicesEvent.UpdateErrorMessage(exception.message))
                    }
                }
            }
        }
    }
}
