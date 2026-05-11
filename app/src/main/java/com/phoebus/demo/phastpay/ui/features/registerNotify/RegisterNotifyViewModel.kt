package com.phoebus.demo.phastpay.ui.features.registerNotify

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoebus.demo.phastpay.manager.PhastPayIpcManager
import com.phoebus.demo.phastpay.services.UnRegisterNotifyService
import com.phoebus.demo.phastpay.services.system.ForegroundBroadcastService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterNotifyViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val unRegisterNotifyService: UnRegisterNotifyService,
    private val ipcManager: PhastPayIpcManager
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterNotifyState())
    val state: StateFlow<RegisterNotifyState> = _state.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<RegisterNotifyNavigationEvents>(replay = 1, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<RegisterNotifyNavigationEvents> =
        _navigationEvent.asSharedFlow()

    private val _permissionRequestEvent = MutableSharedFlow<Unit>()
    val permissionRequestEvent: SharedFlow<Unit> = _permissionRequestEvent.asSharedFlow()

    init {
        // Inicializa o estado com base no valor atual do Manager (que é Singleton)
        _state.update { it.copy(isRegistered = ipcManager.isCurrentlyRegistered) }
    }

    fun onEvent(event: RegisterNotifyEvent) {
        when (event) {
            is RegisterNotifyEvent.RegisterNotify -> {
                doRegisterNotify()
            }
            is RegisterNotifyEvent.UnRegisterNotify -> {
                viewModelScope.launch {
                    doUnRegisterNotify()
                }
            }

            is RegisterNotifyEvent.UpdateSuccessMessage -> {
                _state.update { it.copy(successMessage = event.message) }
            }

            is RegisterNotifyEvent.UpdateErrorMessage -> {
                _state.update { it.copy(errorMessage = event.message) }
            }

            is RegisterNotifyEvent.UpdateCreatePayment -> {
                _state.update { it.copy(createPaymentMessage = event.message) }
            }

            is RegisterNotifyEvent.UpdateConfirmedPaymentMessage -> {
                _state.update { it.copy(confirmedPaymentMessage = event.message) }
            }

            is RegisterNotifyEvent.UpdateShowReceiptMessage -> {
                _state.update { it.copy(showReceiptMessage = event.message) }
            }

            is RegisterNotifyEvent.UpdatePingMessage -> {
                _state.update { it.copy(pingMessage = "app-demo received: ${event.message}" ,
                    pingTrigger = System.currentTimeMillis()) }
            }
        }
    }

    private fun doRegisterNotify() {
        ipcManager.registerNotificationListener { success ->
            if (success) {
                _state.update { it.copy(isRegistered = true) }
                startPaymentService()
            }
        }
    }
    private suspend fun doUnRegisterNotify() {
        unRegisterNotifyService.invoke().collect { result ->
            when {
                result.isSuccess -> {
                    ipcManager.setUnregistered()
                    _state.update { it.copy(isRegistered = false) }
                    Toast.makeText(context, "Registro removido com sucesso", Toast.LENGTH_SHORT).show()
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    if (exception !== null) {
                        Toast.makeText(context, "Error ao tentar remover o registro de notificação: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }
    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            startPaymentService()
        }
    }

    private fun startPaymentService() {
        val intent = Intent(context, ForegroundBroadcastService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
