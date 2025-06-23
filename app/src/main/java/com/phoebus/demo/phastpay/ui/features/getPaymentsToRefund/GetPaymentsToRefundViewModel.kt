package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsToRefundRequest
import com.phoebus.demo.phastpay.services.GetPaymentsToRefundService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.launch

class GetPaymentsToRefundViewModel : ViewModel() {

    private val _state = mutableStateOf(GetPaymentsToRefundState())
    val state: State<GetPaymentsToRefundState> = _state

    fun onEvent(event: GetPaymentsToRefundEvent) {
        when (event) {
            is GetPaymentsToRefundEvent.UpdateStartDate -> {
                _state.value = _state.value.copy(startDate = event.startDate)
            }

            is GetPaymentsToRefundEvent.UpdateEndDate -> {
                _state.value = _state.value.copy(endDate = event.endDate)
            }

            is GetPaymentsToRefundEvent.UpdateRefundResult -> {
                _state.value = _state.value.copy(refundResult = event.refundResult)
            }

            is GetPaymentsToRefundEvent.UpdateErrorMessage -> {
                _state.value = _state.value.copy(errorMessage = event.message)
            }

            is GetPaymentsToRefundEvent.OnSubmit -> {
                onSubmit(event.phastPayClient)
            }

            is GetPaymentsToRefundEvent.StartGet -> {
                viewModelScope.launch {
                    onSubmit(event.phastPayClient);
                }
            }

            is GetPaymentsToRefundEvent.UpdateParams -> {
                _state.value = _state.value.copy(
                    startDate = event.startDate,
                    endDate = event.endDate
                )
            }

            is GetPaymentsToRefundEvent.UpdatePrintCustomerReceipt -> {
                _state.value = _state.value.copy(printCustomerReceipt = event.print)
            }

            is GetPaymentsToRefundEvent.UpdatePrintMerchantReceipt -> {
                _state.value = _state.value.copy(printMerchantReceipt = event.print)
            }
        }
    }

    private fun onSubmit(phastPayClient: PhastPayClient) {
        viewModelScope.launch {
            val request = PhastPayGetPaymentsToRefundRequest(
                startDate = state.value.startDate,
                endDate = state.value.endDate,
            )
            GetPaymentsToRefundService().invoke(phastPayClient, request).collect { result ->
                result.onSuccess { response ->
                    onEvent(GetPaymentsToRefundEvent.UpdateRefundResult(response))
                }
                result.onFailure { error ->
                    onEvent(GetPaymentsToRefundEvent.UpdateErrorMessage(error.message))
                }
            }
        }
    }

}