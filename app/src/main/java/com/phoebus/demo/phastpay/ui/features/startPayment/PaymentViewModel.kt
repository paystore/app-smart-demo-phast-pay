package com.phoebus.demo.phastpay.ui.features.startPayment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayStartPaymentRequest
import com.phoebus.demo.phastpay.services.StartPaymentService
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale


class PaymentViewModel : ViewModel() {

    private val _state = MutableStateFlow(PaymentState())
    val state: StateFlow<PaymentState> = _state.asStateFlow()

    private val _dialogMessage = MutableStateFlow<String?>(null)
    val dialogMessage: StateFlow<String?> = _dialogMessage.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<PaymentNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<PaymentNavigationEvents> = _navigationEvent.asSharedFlow()

    fun onNavigationEvent(event: PaymentNavigationEvents) {
        when (event) {
            is PaymentNavigationEvents.NavigateToHome -> {
                viewModelScope.launch {
                    _navigationEvent.emit(PaymentNavigationEvents.NavigateToHome)
                }
            }
        }
    }

    private suspend fun requestPayment(phastPayClient: PhastPayClient) {
        val startPaymentService = StartPaymentService()
        startPaymentService.invoke(
            phastPayClient,
            PhastPayStartPaymentRequest(
                appClientId = state.value.appClientId,
                applicationId = state.value.applicationId,
                applicationName = state.value.applicationName,
                service = Service.valueOf(state.value.service),
                value = if(state.value.sendValue) valueToSend(state.value.value) else null,
                currency = if(state.value.sendValue) state.value.currency else null,
                printCustomerReceipt = state.value.printCustomerReceipt,
                printMerchantReceipt = state.value.printMerchantReceipt,
                phoneNumber = if (state.value.sendPhoneNumber) state.value.phoneNumber else null,
                countyCode = if (state.value.sendPhoneNumber) state.value.countryCode else null,
                customerName = if (state.value.switchAdditionalInfo) state.value.customerName else null,
                customerEmail = if (state.value.switchAdditionalInfo) state.value.customerEmail else null
            )
        ).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    onEvent(PaymentEvent.UpdateSuccessMessage(response?.toJson()))
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(PaymentEvent.UpdateErrorMessage(exception.message))
                    }
                }
            }
        }
    }

    fun onEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.Initialize -> {
                _state.update {
                    it.copy(
                        appClientId = event.appClientId,
                        applicationId = event.applicationId,
                        applicationName = event.applicationName
                    )
                }
            }

            is PaymentEvent.UpdateService -> {
                _state.update { it.copy(service = event.service) }
            }

            is PaymentEvent.UpdateSendValue -> {
                _state.update { it.copy(sendValue = event.sendValue) }
            }

            is PaymentEvent.UpdateValue -> {
                _state.update { it.copy(value = event.value) }
            }

            is PaymentEvent.UpdateCurrency -> {
                _state.update { it.copy(currency = event.currency) }
            }

            is PaymentEvent.UpdatePrintCustomerReceipt -> {
                _state.update { it.copy(printCustomerReceipt = event.print) }
            }

            is PaymentEvent.UpdatePrintMerchantReceipt -> {
                _state.update { it.copy(printMerchantReceipt = event.print) }
            }

            is PaymentEvent.UpdateSendPhoneNumber -> {
                _state.update { it.copy(sendPhoneNumber = event.sendPhone) }
            }

            is PaymentEvent.UpdatePhoneNumber -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber) }
            }

            is PaymentEvent.UpdateCountryCode -> {
                _state.update { it.copy(countryCode = event.countryCode, phoneNumber = null) }
            }

            is PaymentEvent.SendAdditionalInfo -> {
                _state.update { it.copy(switchAdditionalInfo = event.send) }
            }

            is PaymentEvent.UpdateCustomerName -> {
                _state.update { it.copy(customerName = event.name) }
            }

            is PaymentEvent.UpdateCustomerEmail -> {
                _state.update { it.copy(customerEmail = event.email) }
            }

            is PaymentEvent.SubmitPayment -> {
                viewModelScope.launch {
                    requestPayment(event.phastPayClient)
                }
            }

            is PaymentEvent.UpdateErrorMessage -> {
                Log.d(ConstantsUtils.TAG, event.message ?: "Msg null")
                _dialogMessage.value = event.message ?: "Unknown error"
            }

            is PaymentEvent.UpdateSuccessMessage -> {
                Log.d(ConstantsUtils.TAG, event.message ?: "Msg error null")

                _dialogMessage.value = event.message ?: "Unknown success"
            }
        }
    }

    fun dismissDialog() {
        _dialogMessage.value = null
    }

    private fun formatToCurrency(value: String): String {
        return try {
            val nValue = value.toDouble()
            val cents = nValue / 100
            String.format(Locale.ENGLISH, "%.2f", cents)
        } catch (e: NumberFormatException) {
            "0.00"
        }
    }

    private fun valueToSend(value: String?): String? {
        if (value.isNullOrEmpty()) return value;
        return formatToCurrency(value);
    }
}
