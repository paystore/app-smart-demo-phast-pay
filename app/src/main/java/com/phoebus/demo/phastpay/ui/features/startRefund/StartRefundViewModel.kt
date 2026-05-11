package com.phoebus.demo.phastpay.ui.features.startRefund

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayStartRefundRequest
import com.phoebus.demo.phastpay.data.repositories.DeviceRepository
import com.phoebus.demo.phastpay.services.StartRefundService
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StartRefundViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val startRefundService: StartRefundService,
    private val json: Json,
    private val deviceRepository: DeviceRepository
)  : ViewModel() {
    private val _state = MutableStateFlow(StartRefundState())
    val state: StateFlow<StartRefundState> = _state.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<StartRefundNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<StartRefundNavigationEvents> = _navigationEvent.asSharedFlow()

    private fun formatToCurrency(value: String): String {
        return try {
            val nValue = value.toDouble()
            val cents = nValue / 100
            String.format(Locale.ENGLISH, "%.2f", cents)
        } catch (e: NumberFormatException) {
            Log.e(ConstantsUtils.TAG, "Error formatting value to currency: ${e.message}")
            "0.00"
        }
    }

    private fun valueToSend(value: String?): String? {
        if (value.isNullOrEmpty()) return value
        return formatToCurrency(value)
    }

    fun onNavigationEvent(event: StartRefundNavigationEvents) {
        when (event) {
            is StartRefundNavigationEvents.NavigateToHome -> {
                viewModelScope.launch {
                    _navigationEvent.emit(StartRefundNavigationEvents.NavigateToHome)
                }
            }
        }
    }

    fun onEvent(event: StartRefundEvent) {
        when (event) {
            is StartRefundEvent.Initialize -> {
                _state.update {
                    it.copy(
                        applicationId = deviceRepository.getPackageName(),
                        applicationName = deviceRepository.getAppName()
                    )
                }
            }

            is StartRefundEvent.UpdateSendPartialValue -> {
                _state.update { it.copy(sendPartialValue = event.sendPartialValue) }
                if (event.sendPartialValue) {
                    onEvent(StartRefundEvent.UpdateValue(null))
                }
            }

            is StartRefundEvent.UpdateValue -> {
                _state.update { it.copy(value = event.value) }
            }

            is StartRefundEvent.UpdatePrintCustomerReceipt -> {
                _state.update { it.copy(printCustomerReceipt = event.print) }
            }

            is StartRefundEvent.UpdatePrintMerchantReceipt -> {
                _state.update { it.copy(printMerchantReceipt = event.print) }
            }

            is StartRefundEvent.UpdatePreviewCustomerReceipt -> {
                _state.update { it.copy(previewCustomerReceipt = event.preview) }
            }

            is StartRefundEvent.UpdatePreviewMerchantReceipt -> {
                _state.update { it.copy(previewMerchantReceipt = event.preview) }
            }

            is StartRefundEvent.SubmitRefund -> {
                viewModelScope.launch {
                    startRefundService.invoke(
                        phastPayClient,
                        PhastPayStartRefundRequest(
                            applicationId = state.value.applicationId,
                            applicationName = state.value.applicationName,
                            paymentId = state.value.paymentId,
                            value = valueToSend(state.value.value),
                            printCustomerReceipt = state.value.printCustomerReceipt,
                            printMerchantReceipt = state.value.printMerchantReceipt,
                            previewCustomerReceipt = state.value.previewCustomerReceipt,
                            previewMerchantReceipt = state.value.previewMerchantReceipt
                        )
                    ).collect { result ->
                        when {
                            result.isSuccess -> {
                                val response = result.getOrNull()
                                onEvent(StartRefundEvent.UpdateSuccessMessage(response))
                            }

                            result.isFailure -> {
                                val exception = result.exceptionOrNull()
                                if (exception !== null) {
                                    onEvent(StartRefundEvent.UpdateErrorMessage(exception.message))
                                }
                            }
                        }
                    }
                }
            }

            is StartRefundEvent.UpdateErrorMessage -> {
                _state.update { it.copy(errorMessage = event.message) }
            }

            is StartRefundEvent.UpdateSuccessMessage -> {
                val result = event.refundResult?.let { json.encodeToString(it) }
                _state.update { it.copy(refundResult = result) }
            }

            is StartRefundEvent.UpdatePaymentId -> {
                _state.update { it.copy(paymentId = event.paymentId) }
            }
        }
    }
}
