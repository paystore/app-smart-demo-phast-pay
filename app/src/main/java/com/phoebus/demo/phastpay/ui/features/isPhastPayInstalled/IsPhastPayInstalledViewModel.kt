package com.phoebus.demo.phastpay.ui.features.isPhastPayInstalled

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.services.IsPhastPayInstalledService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IsPhastPayInstalledViewModel() : ViewModel() {

    private val _state = MutableStateFlow(IsPhastPayInstalledState())
    val state: StateFlow<IsPhastPayInstalledState> = _state.asStateFlow()


    private suspend fun checkAppInstalled(phastPayClient: PhastPayClient) {
        val isPhastPayInstalledService = IsPhastPayInstalledService();
        isPhastPayInstalledService.invoke(
            phastPayClient,
        ).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    if (response != null) {
                        onEvent(IsPhastPayInstalledEvent.UpdateIsAppInstalled(response))
                        onEvent(IsPhastPayInstalledEvent.UpdateLoading(false))
                    }
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(IsPhastPayInstalledEvent.UpdateErrorMessage(exception.message))
                        onEvent(IsPhastPayInstalledEvent.UpdateLoading(false))
                    }
                }
            }
        }
    }


    fun onEvent(event: IsPhastPayInstalledEvent) {
        when (event) {
            is IsPhastPayInstalledEvent.UpdateLoading -> {
                _state.update { it.copy(loading = event.loading) }
            }

            is IsPhastPayInstalledEvent.UpdateIsAppInstalled -> {
                _state.update { it.copy(isAppInstalled = event.isAppInstalled) }
            }

            is IsPhastPayInstalledEvent.UpdateErrorMessage -> {
                _state.update { it.copy(errorMessage = event.message) }
            }

            is IsPhastPayInstalledEvent.StartCheck -> {
                viewModelScope.launch {
                    checkAppInstalled(event.phastPayClient)
                }
            }
        }
    }
}