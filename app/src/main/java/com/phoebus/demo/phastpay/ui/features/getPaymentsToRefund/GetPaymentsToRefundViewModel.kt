package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsToRefundRequest
import com.phoebus.demo.phastpay.services.GetPaymentsToRefundService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GetPaymentsToRefundViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getPaymentsToRefundService: GetPaymentsToRefundService,
    private val json: Json
) : ViewModel() {

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
                val result = event.refundResult?.let { json.encodeToString(it) }
                _state.value = _state.value.copy(refundResult = result)

            }

            is GetPaymentsToRefundEvent.UpdateErrorMessage -> {
                _state.value = _state.value.copy(errorMessage = event.message)
            }

            is GetPaymentsToRefundEvent.OnSubmit -> {
                onSubmit()
            }

            is GetPaymentsToRefundEvent.StartGet -> {
                viewModelScope.launch {
                    onSubmit()
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

            is GetPaymentsToRefundEvent.UpdatePreviewCustomerReceipt -> {
                _state.value = _state.value.copy(previewCustomerReceipt = event.preview)
            }

            is GetPaymentsToRefundEvent.UpdatePreviewMerchantReceipt -> {
                _state.value = _state.value.copy(previewMerchantReceipt = event.preview)
            }
        }
    }

    private fun onSubmit() {
        viewModelScope.launch {
            val request = PhastPayGetPaymentsToRefundRequest(
                startDate = state.value.startDate,
                endDate = state.value.endDate,
                printCustomerReceipt = state.value.printCustomerReceipt,
                printMerchantReceipt = state.value.printMerchantReceipt,
                previewCustomerReceipt = state.value.previewCustomerReceipt,
                previewMerchantReceipt = state.value.previewMerchantReceipt
            )
            getPaymentsToRefundService.invoke(phastPayClient, request).collect { result ->
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