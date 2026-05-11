package com.phoebus.demo.phastpay.ui.features.getTransactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetTransactionsRequest
import com.phoebus.demo.phastpay.services.GetTransactionsService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GetTransactionsViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val getTransactionsService: GetTransactionsService,
    private val json: Json
    ) : ViewModel() {
    private val _state = mutableStateOf(GetTransactionsState())
    val state: State<GetTransactionsState> = _state

    private val _navigationEvent =
        MutableSharedFlow<GetTransactionsNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<GetTransactionsNavigationEvents> = _navigationEvent.asSharedFlow()


    private suspend fun sendRequest() {
        val phastPayGetTransactionsRequest = PhastPayGetTransactionsRequest(
            startDate = state.value.startDate,
            endDate = state.value.endDate,
            printMerchantReceipt = state.value.printMerchantReceipt,
            printCustomerReceipt = state.value.printCustomerReceipt,
            previewCustomerReceipt = state.value.previewCustomerReceipt,
            previewMerchantReceipt = state.value.previewMerchantReceipt
        )

        getTransactionsService.invoke(phastPayClient, phastPayGetTransactionsRequest).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    onEvent(GetListTransactionsEvent.UpdateGetResult(response))
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(GetListTransactionsEvent.UpdateMessageError(exception.message))
                    }
                }
            }
        }
    }
    fun onNavigationEvent(event: GetTransactionsNavigationEvents){
        when(event){
            is GetTransactionsNavigationEvents.NavigateToHome -> {
                viewModelScope.launch {
                    _navigationEvent.emit(GetTransactionsNavigationEvents.NavigateToHome)
                }
            }
        }
    }

    fun onEvent(event: GetListTransactionsEvent) {
        when (event) {
            is GetListTransactionsEvent.UpdateParams -> {
                _state.value = _state.value.copy(
                    startDate = event.startDate,
                    endDate = event.endDate
                )
            }

            is GetListTransactionsEvent.StartGet -> {
                viewModelScope.launch {
                    sendRequest()
                }
            }

            is GetListTransactionsEvent.UpdateGetResult -> {
                val result = event.getTransactionsResult?.let { json.encodeToString(it) }
                _state.value = _state.value.copy(getListResult = result)
            }

            is GetListTransactionsEvent.UpdatePrintCustomerReceipt -> {
                _state.value = _state.value.copy(printCustomerReceipt = event.print)
            }

            is GetListTransactionsEvent.UpdatePrintMerchantReceipt -> {
                _state.value = _state.value.copy(printMerchantReceipt = event.print)
            }

            is GetListTransactionsEvent.UpdatePreviewCustomerReceipt -> {
                _state.value = _state.value.copy(previewCustomerReceipt = event.preview)
            }

            is GetListTransactionsEvent.UpdatePreviewMerchantReceipt -> {
                _state.value = _state.value.copy(previewMerchantReceipt = event.preview)
            }

            is GetListTransactionsEvent.UpdateMessageError -> {
                _state.value = _state.value.copy(errorMessage = event.messageError)
            }
        }
    }
}