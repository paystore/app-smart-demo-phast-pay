package com.phoebus.demo.phastpay.ui.features.getRefundById

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.data.dto.PhastPayGetRefundByIdRequest
import com.phoebus.demo.phastpay.services.GetRefundByIdService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GetRefundByIdViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getRefundByIdService: GetRefundByIdService,
    private val json: Json
) : ViewModel() {
    private val _state = mutableStateOf(GetRefundByIdState());
    val state: State<GetRefundByIdState> = _state;

    fun onEvent(event: GetRefundByIdEvent) {
        when (event) {
            is GetRefundByIdEvent.OnSubmit -> {
                sendRequest()
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

            is GetRefundByIdEvent.UpdatePreviewCustomerReceipt -> {
                _state.value = _state.value.copy(previewCustomerReceipt = event.preview)
            }

            is GetRefundByIdEvent.UpdatePreviewMerchantReceipt -> {
                _state.value = _state.value.copy(previewMerchantReceipt = event.preview)
            }

            is GetRefundByIdEvent.UpdateRefundByResult -> {
                val result = event.refundResult?.let { json.encodeToString(it) }
                _state.value = _state.value.copy(refundResult = result)
            }

            is GetRefundByIdEvent.UpdateErrorMessage -> {
                _state.value = _state.value.copy(errorMessage = event.message)
            }
        }
    }

    private fun sendRequest() {
        viewModelScope.launch {
            getRefundByIdService.invoke(
                phastPayClient,
                PhastPayGetRefundByIdRequest(
                    refundId = state.value.refundId,
                    printCustomerReceipt = state.value.printCustomerReceipt,
                    printMerchantReceipt = state.value.printMerchantReceipt,
                    previewCustomerReceipt = state.value.previewCustomerReceipt,
                    previewMerchantReceipt = state.value.previewMerchantReceipt
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        onEvent(GetRefundByIdEvent.UpdateRefundByResult(response))
                    }

                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        if (exception !== null) {
                            onEvent(GetRefundByIdEvent.UpdateErrorMessage(exception.message))
                        }
                    }
                }
            }
        }
    }


}