package com.phoebus.demo.phastpay.ui.features.getRefundById

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastPayGetRefundByIdRequest
import com.phoebus.demo.phastpay.services.GetRefundByIdService
import kotlinx.coroutines.launch

class GetRefundByIdViewModel : ViewModel() {
    private val _state = mutableStateOf(GetRefundByIdState());
    val state: State<GetRefundByIdState> = _state;

    fun onEvent(event: GetRefundByIdEvent) {
        when (event) {
            is GetRefundByIdEvent.OnSubmit -> {
                sendRequest(event.phastPayClient)
            }
            is GetRefundByIdEvent.UpdateRefundById -> {
                _state.value = _state.value.copy(refundId = event.refundId)
            }
            is GetRefundByIdEvent.UpdatePrintCustomerReceipt -> {
                _state.value = _state.value.copy(printCustomerReceipt = event.print)
            }
            is GetRefundByIdEvent.UpdatePrintMerchantReceipt -> {
                _state.value = _state.value.copy(printMerchantReceipt = event.print)
            }
            is GetRefundByIdEvent.UpdateRefundByResult -> {
                _state.value = _state.value.copy(refundResult = event.refundResult)
            }
            is GetRefundByIdEvent.UpdateErrorMessage -> {
                _state.value = _state.value.copy(errorMessage = event.message)
            }
        }
    }

    private fun sendRequest(phastPayClient: PhastPayClient) {
        viewModelScope.launch {
            val getRefundByIdService = GetRefundByIdService()

            getRefundByIdService.invoke(
                phastPayClient,
                PhastPayGetRefundByIdRequest(
                    refundId = state.value.refundId,
                    printCustomerReceipt = state.value.printCustomerReceipt,
                    printMerchantReceipt = state.value.printMerchantReceipt
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        onEvent(GetRefundByIdEvent.UpdateRefundByResult(response))
                    }
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if(exception !== null){
                            onEvent(GetRefundByIdEvent.UpdateErrorMessage(exception.message))
                        }
                    }
                }
            }
        }
    }


}