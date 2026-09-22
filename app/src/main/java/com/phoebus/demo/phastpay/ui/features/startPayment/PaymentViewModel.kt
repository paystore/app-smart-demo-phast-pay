package com.phoebus.demo.phastpay.ui.features.startPayment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayAbortTransactionRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayStartPaymentRequest
import com.phoebus.demo.phastpay.data.enums.AdditionalType
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.data.repositories.DeviceRepository
import com.phoebus.demo.phastpay.services.AbortTransactionService
import com.phoebus.demo.phastpay.services.StartPaymentService
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.demo.phastpay.utils.CurrencyType
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val startPaymentService: StartPaymentService,
    private val abortTransactionService: AbortTransactionService,
    private val json: Json,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

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

    private suspend fun requestPayment() {
        startPaymentService.invoke(
            phastPayClient,
            PhastPayStartPaymentRequest(
                appClientId = state.value.appClientId,
                applicationId = state.value.applicationId,
                applicationName = state.value.applicationName,
                service = Service.valueOf(state.value.service),
                value = if (state.value.sendValue) valueToSend(state.value.value) else null,
                currency = if (state.value.sendCurrency) state.value.currency else null,
                printCustomerReceipt = state.value.printCustomerReceipt,
                printMerchantReceipt = state.value.printMerchantReceipt,
                previewCustomerReceipt = state.value.previewCustomerReceipt,
                previewMerchantReceipt = state.value.previewMerchantReceipt,
                phoneNumber = if (state.value.service != Service.TWINT.name && state.value.sendPhoneNumber) state.value.phoneNumber else null,
                countyCode = if (state.value.service != Service.TWINT.name && state.value.sendPhoneNumber) state.value.countryCode else null,
                additionalValue = valueToSend(state.value.additionalValue),
                additionalType = if(state.value.sendTipValue) AdditionalType.TIP else null,
                customerName = if (state.value.switchAdditionalInfo) state.value.customerName else null,
                customerEmail = if (state.value.switchAdditionalInfo) state.value.customerEmail else null,
                providerId = if(state.value.switchSendProviderId) state.value.providerId else null
            )
        ).collect { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()
                    onEvent(PaymentEvent.UpdateSuccessMessage(json.encodeToString(response)))
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

    private suspend fun abortTransaction() {
        abortTransactionService.invoke(
            phastPayClient,
            PhastPayAbortTransactionRequest(
                applicationId = state.value.applicationId,
                applicationName = state.value.applicationName,
            )
        ).collect { result ->
            result.onSuccess {
                Log.d(ConstantsUtils.TAG, "Abort transaction enviado com sucesso: $it")
            }
            result.onFailure {
                Log.e(ConstantsUtils.TAG, "Falha ao enviar abort transaction: ${it.message}")
            }
        }
    }

    fun onEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.Initialize -> {
                _state.update {
                    it.copy(
                        appClientId = UUID.randomUUID().toString(),
                        applicationId = deviceRepository.getPackageName(),
                        applicationName = deviceRepository.getAppName()
                    )
                }
            }

            is PaymentEvent.UpdateService -> {
                _state.update {
                    it.copy(
                        service = event.service,
                        currency = if (event.service == Service.CRYPTO.name) CurrencyType.USD.name else it.currency
                    )
                }
            }

            is PaymentEvent.UpdateSendValue -> {
                _state.update {
                    it.copy(
                        sendValue = event.sendValue,
                        sendCurrency = if (event.sendValue) true else it.sendCurrency
                    )
                }
            }

            is PaymentEvent.UpdateValue -> {
                _state.update { it.copy(value = event.value) }
            }

            is PaymentEvent.UpdateCurrency -> {
                _state.update { it.copy(currency = event.currency) }
            }

            is PaymentEvent.UpdateSendCurrency -> {
                _state.update { it.copy(sendCurrency = event.sendCurrency) }
            }

            is PaymentEvent.UpdatePrintCustomerReceipt -> {
                _state.update { it.copy(printCustomerReceipt = event.print) }
            }

            is PaymentEvent.UpdatePrintMerchantReceipt -> {
                _state.update { it.copy(printMerchantReceipt = event.print) }
            }

            is PaymentEvent.UpdatePreviewCustomerReceipt -> {
                _state.update { it.copy(previewCustomerReceipt = event.preview) }
            }

            is PaymentEvent.UpdatePreviewMerchantReceipt -> {
                _state.update { it.copy(previewMerchantReceipt = event.preview) }
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

            is PaymentEvent.UpdateSendTipValue -> {
                _state.update {
                    it.copy(
                        sendTipValue = event.sendTip,
                        sendCurrency = if (event.sendTip) true else it.sendCurrency
                    )
                }
            }

            is PaymentEvent.UpdateTipValue -> {
                _state.update { it.copy(additionalValue = event.tipValue) }
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

            is PaymentEvent.UpdateProviderId -> {
                _state.update { it.copy(providerId = event.providerId) }
            }

            is PaymentEvent.UpdateSendProviderId -> {
                _state.update { it.copy(switchSendProviderId = event.sendProviderId) }
            }

            is PaymentEvent.SubmitPayment -> {
                viewModelScope.launch {
                    requestPayment()
                }
            }

            is PaymentEvent.SubmitPaymentWithAbort -> {
                viewModelScope.launch {
                    requestPayment()
                }
                viewModelScope.launch {
                    delay(30_000)
                    abortTransaction()
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
            Log.e(ConstantsUtils.TAG, "Error formatting value to currency: ${e.message}")
            "0.00"
        }
    }

    private fun valueToSend(value: String?): String? {
        if (value.isNullOrEmpty()) return value
        return formatToCurrency(value)
    }
}
