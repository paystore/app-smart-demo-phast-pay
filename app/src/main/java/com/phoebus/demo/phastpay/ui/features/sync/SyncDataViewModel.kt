package com.phoebus.demo.phastpay.ui.features.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.services.SyncDataService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SyncDataViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val syncDataService: SyncDataService,
    private val json: Json
) : ViewModel() {

    private val _state = MutableStateFlow(SyncDataState())
    val state: StateFlow<SyncDataState> = _state.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<SyncDataNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<SyncDataNavigationEvents> = _navigationEvent.asSharedFlow()


    fun onEvent(event: SyncDataEvent){
        when(event){
            is SyncDataEvent.UpdateLoading -> {
                _state.update { it.copy(loading = event.loading) }
            }
            is SyncDataEvent.UpdateDialogMessage -> {
                _state.update { it.copy(dialogMessage = event.message) }
            }
            is SyncDataEvent.StartSync -> {
                viewModelScope.launch {
                    syncData()
                }
            }
        }
    }

    fun onNavigationEvent(event: SyncDataNavigationEvents){
        when(event){
            is SyncDataNavigationEvents.NavigateToHome -> {
                viewModelScope.launch {
                    _navigationEvent.emit(SyncDataNavigationEvents.NavigateToHome)
                }
            }
        }
    }

    private suspend fun syncData() {
        syncDataService.invoke(
            phastPayClient,
        ).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    if(response != null){
                        onEvent(SyncDataEvent.UpdateDialogMessage(json.encodeToString(response)))
                        onEvent(SyncDataEvent.UpdateLoading(false))
                    }
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(SyncDataEvent.UpdateDialogMessage(exception.message))
                        onEvent(SyncDataEvent.UpdateLoading(false))
                    }
                }
            }
        }
    }
}
