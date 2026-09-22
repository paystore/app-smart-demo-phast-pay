package com.phoebus.demo.phastpay.ui.features.startPaymentApi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayGetQrCodeRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayPrintReceiptRequest
import com.phoebus.demo.phastpay.data.dto.PhastPayStartPaymentApiRequest
import com.phoebus.demo.phastpay.data.enums.AdditionalType
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.data.repositories.DeviceRepository
import com.phoebus.demo.phastpay.services.GetQrCodeService
import com.phoebus.demo.phastpay.services.PrintReceiptService
import com.phoebus.demo.phastpay.services.RegisterNotifyService
import com.phoebus.demo.phastpay.services.StartPaymentApiService
import com.phoebus.demo.phastpay.services.UnRegisterNotifyService
import com.phoebus.demo.phastpay.ui.features.printReceipt.PrintReceiptSideEffect
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.demo.phastpay.utils.CurrencyType
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PaymentApiViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val registerNotifyService: RegisterNotifyService,
    private val unRegisterNotifyService: UnRegisterNotifyService,
    private val startPaymentApiService: StartPaymentApiService,
    private val getQrCodeService: GetQrCodeService,
    private val printReceiptService: PrintReceiptService,
    private val deviceRepository: DeviceRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentApiState())
    val state: StateFlow<PaymentApiState> = _state.asStateFlow()

    private val _dialogMessage = MutableStateFlow<String?>(null)
    val dialogMessage: StateFlow<String?> = _dialogMessage.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<PaymentApiNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<PaymentApiNavigationEvents> = _navigationEvent.asSharedFlow()

    private val _sideEffect = Channel<PrintReceiptSideEffect>()

    init {
        doRegisterNotify()
    }

    private suspend fun requestPayment() {
        _state.update { it.copy(isLoading = true) }
        startPaymentApiService.invoke(
            phastPayClient,
            PhastPayStartPaymentApiRequest(
                appClientId = state.value.appClientId,
                applicationId = state.value.applicationId,
                applicationName = state.value.applicationName,
                service = Service.valueOf(state.value.service),
                value = if (state.value.sendValue) valueToSend(state.value.value) else null,
                additionalValue = if (state.value.sendTipValue) valueToSend(state.value.tipValue) else null,
                additionalType = if (state.value.sendTipValue) AdditionalType.TIP else null,
                currency = if (state.value.sendValue) state.value.currency else null,
                providerId = if (state.value.sendProviderId) state.value.providerId else null
            )
        ).collect { result ->
            _state.update { it.copy(isLoading = false) }
            when {

                result.isSuccess -> {
                    val response = result.getOrNull()
                    onEvent(PaymentApiEvent.UpdateSuccessMessage(response))
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(PaymentApiEvent.UpdateErrorMessage(exception.message))
                    }
                }
            }
        }
    }

    private suspend fun fetchQrCode() {
        var shouldRequestPayment = false
        getQrCodeService.invoke(
            phastPayClient,
            PhastPayGetQrCodeRequest(
                providerId = state.value.providerId ?: "",
                service = Service.TWINT
            )
        ).collect { result ->
            when {
                result.isSuccess -> {
                    val qrCodeResult = result.getOrNull()
                    _state.update { it.copy(qrCodeResult = qrCodeResult) }
                    shouldRequestPayment = true
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        onEvent(PaymentApiEvent.UpdateErrorMessage(exception.message))
                    }
                }
            }
        }
        if (shouldRequestPayment) {
            requestPayment()
        }
    }

    fun onEvent(event: PaymentApiEvent) {
        when (event) {
            is PaymentApiEvent.Initialize -> {
                _state.update {
                    it.copy(
                        appClientId = UUID.randomUUID().toString(),
                        applicationId = deviceRepository.getPackageName(),
                        applicationName = deviceRepository.getAppName()
                    )
                }
            }

            is PaymentApiEvent.UpdateService -> {
                _state.update {
                    it.copy(
                        service = event.service,
                        currency = if (event.service == Service.CRYPTO.name) CurrencyType.USD.name else it.currency
                    )
                }
            }

            is PaymentApiEvent.UpdateSendValue -> {
                _state.update { it.copy(sendValue = event.sendValue) }
            }

            is PaymentApiEvent.UpdateSendTipValue -> {
                _state.update { it.copy(sendTipValue = event.sendTipValue) }
            }

            is PaymentApiEvent.UpdateValue -> {
                _state.update { it.copy(value = event.value) }
            }

            is PaymentApiEvent.UpdateTipValue -> {
                _state.update { it.copy(tipValue = event.tipValue) }
            }

            is PaymentApiEvent.UpdateCurrency -> {
                _state.update { it.copy(currency = event.currency) }
            }

            is PaymentApiEvent.UpdateSendProviderId -> {
                _state.update { it.copy(sendProviderId = event.send) }
            }

            is PaymentApiEvent.UpdateProviderId -> {
                _state.update { it.copy(providerId = event.providerId) }
            }

            is PaymentApiEvent.UpdateAppClientId -> {
                _state.update { it.copy(appClientId = event.appClientId) }
            }

            is PaymentApiEvent.SubmitPaymentApi -> {
                viewModelScope.launch {
                    if (state.value.service == Service.TWINT.name) {
                        fetchQrCode()
                    }else {
                        requestPayment()
                    }
                }
            }

            is PaymentApiEvent.UpdateErrorMessage -> {
                Log.d(ConstantsUtils.TAG, event.message ?: "Msg null")
                _dialogMessage.value = event.message ?: "Unknown error"
                _state.update { it.copy(shouldPrintOnDismiss = false) }
            }

            is PaymentApiEvent.UpdateSuccessMessage -> {
                val response = event.response
                Log.d(ConstantsUtils.TAG, response?.toString() ?: "Msg error null")
                _dialogMessage.value = response?.let {
                    """
                        correlationId: ${it.correlationId}
                        transactionId: ${it.transactionId}
                        status: ${it.status}
                        dateTimeOrder: ${it.dateTimeOrder}
                    """.trimIndent()
                } ?: "Unknown success"
                _state.update {
                    it.copy(
                        shouldPrintOnDismiss = false,
                        qrCodeResult = response?.qrcode ?: it.qrCodeResult
                    )
                }
            }

            is PaymentApiEvent.UpdatePrintCustomerReceipt -> {
                _state.update { it.copy(printCustomerReceipt = event.print) }
            }

            is PaymentApiEvent.UpdatePrintMerchantReceipt -> {
                _state.update { it.copy(printMerchantReceipt = event.print) }
            }

            is PaymentApiEvent.UpdatePreviewCustomerReceipt -> {
                _state.update { it.copy(previewCustomerReceipt = event.preview) }
            }

            is PaymentApiEvent.UpdatePreviewMerchantReceipt -> {
                _state.update { it.copy(previewMerchantReceipt = event.preview) }
            }
        }
    }

    fun dismissDialog() {
        _dialogMessage.value = null
        _state.update { it.copy(qrCodeResult = null) }
        if (_state.value.shouldPrintOnDismiss && _state.value.isPrintEnabled()) {
            printReceipt()
        }
    }

    private fun printReceipt() {
        viewModelScope.launch {
            printReceiptService.invoke(
                phastPayClient,
                PhastPayPrintReceiptRequest(
                    applicationName = state.value.applicationName,
                    applicationId = state.value.applicationId,
                    appClientId = state.value.appClientId,
                    previewCustomerReceipt = state.value.previewCustomerReceipt,
                    previewMerchantReceipt = state.value.previewMerchantReceipt,
                    printCustomerReceipt = state.value.printCustomerReceipt,
                    printMerchantReceipt = state.value.printMerchantReceipt
                )
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        _navigationEvent.emit(PaymentApiNavigationEvents.NavigateToHome)
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

    private fun doRegisterNotify() {
        registerNotifyService.execute(
            phastPayClient,
            object : PhastPayClient.IRegisterNotifyCallback {
                override fun onSuccess(response: String?) {
                    Log.d(ConstantsUtils.TAG, "RegisterNotify callback onSuccess: $response")
                }

                override fun onError(response: String?) {
                    Log.d(ConstantsUtils.TAG, "RegisterNotify callback onError: $response")
                }

                override fun onNotifyPaymentConfirmed(response: String) {
                    super.onNotifyPaymentConfirmed(response)
                    Log.d(ConstantsUtils.TAG, "ViewModel recebeu confirmação: $response")
                    _dialogMessage.value = "Pagamento Confirmado com Sucesso!"
                    _state.update { it.copy(shouldPrintOnDismiss = true, qrCodeResult = null) }
                }
            }
        )
    }

    private suspend fun doUnRegisterNotify() {
        unRegisterNotifyService.invoke().collect { result ->
            when {
                result.isSuccess -> {
                    Log.d(ConstantsUtils.TAG, "Registro de notificações removido com sucesso")
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        Log.d(
                            ConstantsUtils.TAG,
                            "Error ao tentar remover o registro de notificação: ${exception.message}"
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            doUnRegisterNotify()
            super.onCleared()
        }
    }
}
