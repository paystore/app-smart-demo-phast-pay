package com.phoebus.demo.phastpay.ui.features.getPaymentByAppClientId

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByAppClientIdRequest
import com.phoebus.demo.phastpay.services.GetPaymentByAppClientIdService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GetPaymentByAppClientIdViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getPaymentByAppClientIdService: GetPaymentByAppClientIdService,
    private val json: Json
) : ViewModel() {
    private val _state = mutableStateOf(GetPaymentByAppClientIdState());
    val state: State<GetPaymentByAppClientIdState> = _state;

    fun onEvent(event: GetPaymentByAppClientIdEvent) {
        when (event) {
            is GetPaymentByAppClientIdEvent.OnSubmit -> {
                sendRequest()
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

    private fun sendRequest() {
        viewModelScope.launch {
            getPaymentByAppClientIdService.invoke(
                phastPayClient,
                PhastPayGetPaymentByAppClientIdRequest(
                    appClientId = state.value.appClientId,
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        onEvent(GetPaymentByAppClientIdEvent.UpdateSuccessMessage(json.encodeToString(response)))
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