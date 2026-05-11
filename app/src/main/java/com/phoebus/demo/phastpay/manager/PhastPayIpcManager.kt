package com.phoebus.demo.phastpay.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.phoebus.demo.phastpay.services.RegisterNotifyService
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhastPayIpcManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phastPayClient: PhastPayClient,
    private val registerNotifyService: RegisterNotifyService,
) {
    var isCurrentlyRegistered: Boolean = false
        private set

    // SharedFlow para emitir os eventos de confirmação
    private val _paymentConfirmationEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val paymentConfirmationEvent = _paymentConfirmationEvent.asSharedFlow()

    fun registerNotificationListener(onResult: (Boolean) -> Unit = {}) {
        registerNotifyService.execute(
            phastPayClient,
            object : PhastPayClient.IRegisterNotifyCallback {
                override fun onSuccess(response: String?) {
                    Log.d(ConstantsUtils.TAG, "RegisterNotify callback onSuccess: $response")
                    isCurrentlyRegistered = true
                    onResult(true)
                }

                override fun onError(response: String?) {
                    Log.d(ConstantsUtils.TAG, "RegisterNotify callback onError: $response")
                    isCurrentlyRegistered = false
                    onResult(false)
                }

                override fun onNotifyPaymentCreate(response: String) {
                    Log.d(
                        ConstantsUtils.TAG,
                        "RegisterNotify callback onNotifyPaymentCreate: $response"
                    )
                }

                override fun onNotifyPaymentConfirmed(response: String) {
                    Log.d(
                        ConstantsUtils.TAG,
                        "RegisterNotify callback onNotifyPaymentConfirmed: $response"
                    )
                    _paymentConfirmationEvent.tryEmit(response)
                }

                override fun onNotifyPaymentUpdate(response: String) {
                    Log.d(
                        ConstantsUtils.TAG,
                        "RegisterNotify callback onNotifyPaymentUpdate: $response"
                    )
                }

                override fun onNotifyPing(response: String) {
                    Log.d(ConstantsUtils.TAG, "RegisterNotify callback onNotifyPing: $response")
                    showNotification(response)
                }
            }
        )
    }

    fun setUnregistered() {
        isCurrentlyRegistered = false
    }

    private fun showNotification(message: String) {
        val builder = NotificationCompat.Builder(context, "1111")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Alerta IPC")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1111, builder.build())
            }
        }
    }
}