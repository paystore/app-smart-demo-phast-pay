package com.phoebus.demo.phastpay.ui.features.getAvailableServices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.services.GetAvailableServicesService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GetAvailableServicesModel() : ViewModel() {

    private val _state = MutableStateFlow(GetAvailableServicesState())
    val state: StateFlow<GetAvailableServicesState> = _state.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<GetAvailableServicesNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<GetAvailableServicesNavigationEvents> = _navigationEvent.asSharedFlow()


    fun onEvent(event: GetAvailableServicesEvent){
        when(event){
            is GetAvailableServicesEvent.UpdateLoading -> {
                _state.update { it.copy(loading = event.loading) }
            }
            is GetAvailableServicesEvent.UpdateDialogMessage -> {
                _state.update { it.copy(dialogMessage = event.message) }
            }
            is GetAvailableServicesEvent.StartGetServices -> {
                viewModelScope.launch {
                    getAvailableServicesData(event.phastPayClient)
                }
            }
        }
    }

    fun onNavigationEvent(event: GetAvailableServicesNavigationEvents){
        when(event){
            is GetAvailableServicesNavigationEvents.NavigateToHome -> {
                viewModelScope.launch {
                    _navigationEvent.emit(GetAvailableServicesNavigationEvents.NavigateToHome)
                }
            }
        }
    }

    private suspend fun getAvailableServicesData(phastPayClient: PhastPayClient) {
        val getAvailableServicesService = GetAvailableServicesService();
        getAvailableServicesService.invoke(
            phastPayClient,
        ).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    if(response != null){
                        onEvent(GetAvailableServicesEvent.UpdateDialogMessage(response.toJson()))
                        onEvent(GetAvailableServicesEvent.UpdateLoading(false))
                    }
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(GetAvailableServicesEvent.UpdateDialogMessage(exception.message))
                        onEvent(GetAvailableServicesEvent.UpdateLoading(false))
                    }
                }
            }
        }
    }
}
