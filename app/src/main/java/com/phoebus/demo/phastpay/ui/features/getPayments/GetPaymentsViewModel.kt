package com.phoebus.demo.phastpay.ui.features.getPayments

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsRequest
import com.phoebus.demo.phastpay.services.GetPaymentsService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.launch

class GetPaymentsViewModel : ViewModel() {
    private val _state = mutableStateOf(GetPaymentsState())
    val state: State<GetPaymentsState> = _state


    private suspend fun sendRequest(phastPayClient: PhastPayClient) {
        val phastPayGetPaymentsRequest = PhastPayGetPaymentsRequest(
            startDate = state.value.startDate,
            endDate = state.value.endDate,
            status = state.value.status,
            value = state.value.value
        )
        val service = GetPaymentsService();
        service.invoke(phastPayClient, phastPayGetPaymentsRequest).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    onEvent(GetPaymentsEvent.UpdatePaymentResult(response))
                    if(response != null && response.payments.isEmpty()){
                        onEvent(GetPaymentsEvent.UpdateMessageError("Empty"))
                    }
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(GetPaymentsEvent.UpdateMessageError(exception.message))
                    }
                }
            }
        }
    }

    fun onEvent(event: GetPaymentsEvent) {
        when (event) {
            is GetPaymentsEvent.UpdateParams -> {
                _state.value = _state.value.copy(
                    value = event.value,
                    status = event.status,
                    startDate = event.startDate,
                    endDate = event.endDate
                )
            }

            is GetPaymentsEvent.StartGetPayments -> {
                viewModelScope.launch {
                    sendRequest(event.phastPayClient);
                }
            }

            is GetPaymentsEvent.UpdatePaymentResult -> {
                _state.value = _state.value.copy(getListResult = event.paymentListResult)
            }

            is GetPaymentsEvent.UpdateMessageError -> {
                _state.value = _state.value.copy(errorMessage = event.messageError)
            }
        }
    }
}