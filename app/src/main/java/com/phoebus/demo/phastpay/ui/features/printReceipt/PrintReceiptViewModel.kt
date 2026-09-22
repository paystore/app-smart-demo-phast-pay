package com.phoebus.demo.phastpay.ui.features.printReceipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayPrintReceiptRequest
import com.phoebus.demo.phastpay.data.repositories.DeviceRepository
import com.phoebus.demo.phastpay.services.PrintReceiptService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PrintReceiptViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val printReceiptService: PrintReceiptService,
    private val json: Json,
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PrintReceiptState());
    val state = _state.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<PrintReceiptNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<PrintReceiptNavigationEvents> = _navigationEvent.asSharedFlow()

    private val _sideEffect = Channel<PrintReceiptSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: PrintReceiptEvent) {
        when (event) {
            is PrintReceiptEvent.OnSubmit -> {
                sendRequest()
            }

            is PrintReceiptEvent.UpdateRefundId -> {
                _state.update { it.copy(refundId = event.refundId) }
            }

            is PrintReceiptEvent.Initialize -> {
                _state.update {
                    it.copy(
                        applicationId = deviceRepository.getPackageName(),
                        applicationName = deviceRepository.getAppName()
                    )
                }
            }

            is PrintReceiptEvent.UpdatePrintCustomerReceipt -> {
                _state.update { it.copy(printCustomerReceipt = event.print) }
            }

            is PrintReceiptEvent.UpdatePrintMerchantReceipt -> {
                _state.update { it.copy(printMerchantReceipt = event.print) }
            }

            is PrintReceiptEvent.UpdatePreviewCustomerReceipt -> {
                _state.update { it.copy(previewCustomerReceipt = event.preview) }
            }

            is PrintReceiptEvent.UpdatePreviewMerchantReceipt -> {
                _state.update { it.copy(previewMerchantReceipt = event.preview) }
            }

            is PrintReceiptEvent.UpdateClientId -> {
                _state.update { it.copy(appClientId = event.clientId) }
            }

            is PrintReceiptEvent.UpdatePaymentId -> {
                _state.update { it.copy(paymentId = event.paymentId) }
            }

            is PrintReceiptEvent.UpdateLastTransaction -> {
                _state.update { it.copy(lastTransaction = event.lastTransaction) }
            }
        }
    }

    fun onNavigationEvent(event: PrintReceiptNavigationEvents) {
        when (event) {
            is PrintReceiptNavigationEvents.NavigateToHome -> {
                viewModelScope.launch {
                    _navigationEvent.emit(PrintReceiptNavigationEvents.NavigateToHome)
                }
            }
        }
    }

    private fun sendRequest() {
        viewModelScope.launch {
            printReceiptService.invoke(
                phastPayClient,
                PhastPayPrintReceiptRequest(
                    applicationName = state.value.applicationName,
                    applicationId = state.value.applicationId,
                    appClientId = state.value.appClientId,
                    paymentId = state.value.paymentId,
                    refundId = state.value.refundId,
                    printCustomerReceipt = state.value.printCustomerReceipt,
                    printMerchantReceipt = state.value.printMerchantReceipt,
                    previewCustomerReceipt = state.value.previewCustomerReceipt,
                    previewMerchantReceipt = state.value.previewMerchantReceipt,
                    lastTransaction = state.value.lastTransaction
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        onNavigationEvent(PrintReceiptNavigationEvents.NavigateToHome)
                    }

                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        exception?.message?.let {
                            viewModelScope.launch {
                                _sideEffect.send(PrintReceiptSideEffect.ShowToast(it))
                            }
                        }
                    }
                }
            }
        }
    }


}