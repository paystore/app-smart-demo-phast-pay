package com.phoebus.demo.phastpay.ui.features.getqrcode

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetQrCodeRequest
import com.phoebus.demo.phastpay.services.GetQrCodeService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetQrCodeViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getQrCodeService: GetQrCodeService
) :
    ViewModel() {
    private val _state = mutableStateOf(GetQrCodeState());
    val state: State<GetQrCodeState> = _state;


    private val _sideEffect = Channel<GetQrCodeSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: GetQrCodeEvent) {
        when (event) {
            is GetQrCodeEvent.OnSubmit -> {
                onEvent(GetQrCodeEvent.UpdateIsLoading(true))
                sendRequest()
            }

            is GetQrCodeEvent.UpdateProviderId -> {
                _state.value = _state.value.copy(providerId = event.providerId)
            }

            is GetQrCodeEvent.UpdateIsLoading -> {
                _state.value = _state.value.copy(isLoading = event.isLoading)
            }

            is GetQrCodeEvent.UpdateQrCode -> {
                _state.value = _state.value.copy(qrCodeResult = event.qrCodeResult)
            }

            is GetQrCodeEvent.UpdateErrorMessage -> {
                viewModelScope.launch {
                    event.message?.let {
                        viewModelScope.launch {
                            _sideEffect.send(GetQrCodeSideEffect.ShowToast(it))
                        }
                    }
                }
            }

            is GetQrCodeEvent.UpdateService -> {
                _state.value = _state.value.copy(service = event.service)
            }
        }
    }

    private fun sendRequest() {
        viewModelScope.launch {
            getQrCodeService.invoke(
                phastPayClient,
                PhastPayGetQrCodeRequest(
                    providerId = state.value.providerId,
                    service = state.value.service,
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        onEvent(GetQrCodeEvent.UpdateIsLoading(false))
                        onEvent(GetQrCodeEvent.UpdateQrCode(response))
                    }

                    result.isFailure -> {
                        onEvent(GetQrCodeEvent.UpdateQrCode(null));
                        onEvent(GetQrCodeEvent.UpdateIsLoading(false))
                        val exception = result.exceptionOrNull()
                        if (exception !== null) {
                            onEvent(GetQrCodeEvent.UpdateErrorMessage(exception.message))
                        }
                    }
                }
            }
        }
    }


}