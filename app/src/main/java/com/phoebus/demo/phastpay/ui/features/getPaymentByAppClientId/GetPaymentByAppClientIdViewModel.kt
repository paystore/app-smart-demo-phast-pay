package com.phoebus.demo.phastpay.ui.features.getPaymentByAppClientId

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByAppClientIdRequest
import com.phoebus.demo.phastpay.services.GetPaymentByAppClientIdService
import kotlinx.coroutines.launch

class GetPaymentByAppClientIdViewModel : ViewModel() {
    private val _state = mutableStateOf(GetPaymentByAppClientIdState());
    val state: State<GetPaymentByAppClientIdState> = _state;

    fun onEvent(event: GetPaymentByAppClientIdEvent) {
        when (event) {
            is GetPaymentByAppClientIdEvent.OnSubmit -> {
                sendRequest(event.phastPayClient)
            }
            is GetPaymentByAppClientIdEvent.UpdateAppClientIdPayment -> {
                _state.value = _state.value.copy(appClientId = event.appClientId)
            }
            is GetPaymentByAppClientIdEvent.UpdateErrorMessage -> {
                _state.value = _state.value.copy(errorMessage = event.message)
            }
            is GetPaymentByAppClientIdEvent.UpdateSuccessMessage -> {
                _state.value = _state.value.copy(successMessage = event.message)
            }
        }
    }

    private fun sendRequest(phastPayClient: PhastPayClient) {
        viewModelScope.launch {
            val getPaymentByAppClientIdService = GetPaymentByAppClientIdService()
            getPaymentByAppClientIdService.invoke(
                phastPayClient,
                PhastPayGetPaymentByAppClientIdRequest(
                    appClientId = state.value.appClientId,
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        onEvent(GetPaymentByAppClientIdEvent.UpdateSuccessMessage(response?.toJson()))
                    }
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if(exception !== null){
                            onEvent(GetPaymentByAppClientIdEvent.UpdateErrorMessage(exception.message))
                        }
                    }
                }
            }
        }
    }


}