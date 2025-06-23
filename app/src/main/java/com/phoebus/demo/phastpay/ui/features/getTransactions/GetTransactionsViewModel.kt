package com.phoebus.demo.phastpay.ui.features.getTransactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetTransactionsRequest
import com.phoebus.demo.phastpay.services.GetTransactionsService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class GetTransactionsViewModel : ViewModel() {
    private val _state = mutableStateOf(GetTransactionsState())
    val state: State<GetTransactionsState> = _state

    private val _navigationEvent =
        MutableSharedFlow<GetTransactionsNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<GetTransactionsNavigationEvents> = _navigationEvent.asSharedFlow()


    private suspend fun sendRequest(phastPayClient: PhastPayClient) {
        val phastPayGetTransactionsRequest = PhastPayGetTransactionsRequest(
            startDate = state.value.startDate,
            endDate = state.value.endDate,
            printMerchantReceipt = state.value.printMerchantReceipt,
            printCustomerReceipt = state.value.printMerchantReceipt,
        )
        val service = GetTransactionsService();
        service.invoke(phastPayClient, phastPayGetTransactionsRequest).collect { result ->
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
                    sendRequest(event.phastPayClient);
                }
            }

            is GetListTransactionsEvent.UpdateGetResult -> {
                _state.value = _state.value.copy(getListResult = event.getTransactionsResult)
            }

            is GetListTransactionsEvent.UpdatePrintCustomerReceipt -> {
                _state.value = _state.value.copy(printCustomerReceipt = event.print)
            }

            is GetListTransactionsEvent.UpdatePrintMerchantReceipt -> {
                _state.value = _state.value.copy(printMerchantReceipt = event.print)
            }

            is GetListTransactionsEvent.UpdateMessageError -> {
                _state.value = _state.value.copy(errorMessage = event.messageError)
            }
        }
    }
}