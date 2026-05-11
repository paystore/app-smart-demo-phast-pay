package com.phoebus.demo.phastpay.ui.features.getPaymentById

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByIdRequest
import com.phoebus.demo.phastpay.services.GetPaymentByIdService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GetPaymentByIdViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getPaymentByIdService: GetPaymentByIdService,
    private val json: Json
) : ViewModel() {
    private val _state = mutableStateOf(GetPaymentByIdState());
    val state: State<GetPaymentByIdState> = _state;

    fun onEvent(event: GetPaymentByIdEvent) {
        when (event) {
            is GetPaymentByIdEvent.OnSubmit -> {
                sendRequest()
            }
            is GetPaymentByIdEvent.UpdatePaymentById -> {
                _state.value = _state.value.copy(paymentId = event.paymentId)
            }
            is GetPaymentByIdEvent.UpdateErrorMessage -> {
                _state.value = _state.value.copy(errorMessage = event.message)
            }
            is GetPaymentByIdEvent.UpdateSuccessMessage -> {
                _state.value = _state.value.copy(successMessage = event.message)
            }
        }
    }

    private fun sendRequest() {
        viewModelScope.launch {
            getPaymentByIdService.invoke(
                phastPayClient,
                PhastPayGetPaymentByIdRequest(
                    paymentId = state.value.paymentId
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        onEvent(GetPaymentByIdEvent.UpdateSuccessMessage(json.encodeToString(response)))
                    }
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if(exception !== null){
                            onEvent(GetPaymentByIdEvent.UpdateErrorMessage(exception.message))
                        }
                    }
                }
            }
        }
    }


}