package com.phoebus.demo.phastpay.ui.features.getPaymentById

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByIdRequest
import com.phoebus.demo.phastpay.services.GetPaymentByIdService
import kotlinx.coroutines.launch

class GetPaymentByIdViewModel : ViewModel() {
    private val _state = mutableStateOf(GetPaymentByIdState());
    val state: State<GetPaymentByIdState> = _state;

    fun onEvent(event: GetPaymentByIdEvent) {
        when (event) {
            is GetPaymentByIdEvent.OnSubmit -> {
                sendRequest(event.phastPayClient)
            }
            is GetPaymentByIdEvent.UpdatePaymentById -> {
                _state.value = _state.value.copy(paymentId = event.paymentId)
            }
            is GetPaymentByIdEvent.UpdatePrintCustomerReceipt -> {
                _state.value = _state.value.copy(printCustomerReceipt = event.print)
            }
            is GetPaymentByIdEvent.UpdatePrintMerchantReceipt -> {
                _state.value = _state.value.copy(printMerchantReceipt = event.print)
            }
            is GetPaymentByIdEvent.UpdateErrorMessage -> {
                _state.value = _state.value.copy(errorMessage = event.message)
            }
            is GetPaymentByIdEvent.UpdateSuccessMessage -> {
                _state.value = _state.value.copy(successMessage = event.message)
            }
        }
    }

    private fun sendRequest(phastPayClient: PhastPayClient) {
        viewModelScope.launch {
            val getPaymentByIdService = GetPaymentByIdService()

            getPaymentByIdService.invoke(
                phastPayClient,
                PhastPayGetPaymentByIdRequest(
                    paymentId = state.value.paymentId,
                    printCustomerReceipt = state.value.printCustomerReceipt,
                    printMerchantReceipt = state.value.printMerchantReceipt
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        onEvent(GetPaymentByIdEvent.UpdateSuccessMessage(response?.toJson()))
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