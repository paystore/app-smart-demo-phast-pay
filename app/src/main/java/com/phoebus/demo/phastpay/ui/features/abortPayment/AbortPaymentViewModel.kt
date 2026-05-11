package com.phoebus.demo.phastpay.ui.features.abortPayment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.data.dto.PhastPayAbortPaymentRequest
import com.phoebus.demo.phastpay.services.AbortPaymentService
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AbortPaymentViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    private val abortPaymentService: AbortPaymentService,
) : ViewModel() {

    var state by mutableStateOf(AbortPaymentState())
        private set

    fun onEvent(event: AbortPaymentEvent) {
        when (event) {

            is AbortPaymentEvent.OnClientIdChange -> {
                state = state.copy(clientId = event.clientId)
            }

            AbortPaymentEvent.OnAbortPaymentClick -> {
                abortPayment()
            }
        }
    }

    private fun abortPayment() {
        val clientId = state.clientId
        if (clientId.isBlank()) {
            state = state.copy(message = "ClientId inválido")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, message = null)

            val request = PhastPayAbortPaymentRequest(
                appClientId = clientId
            )

            abortPaymentService(
                phastPayClient,
                request
            ).collect { result ->

                result.onSuccess {
                    state = state.copy(
                        isLoading = false,
                        message = "Pagamento abortado com sucesso"
                    )
                }

                result.onFailure {
                    state = state.copy(
                        isLoading = false,
                        message = it.message
                    )
                }
            }
        }
    }
}