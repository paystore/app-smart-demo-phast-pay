package com.phoebus.demo.phastpay

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.phoebus.demo.phastpay.services.system.ForegroundBroadcastService
import com.phoebus.demo.phastpay.ui.components.popup.AppToast
import com.phoebus.demo.phastpay.ui.features.main.MainScreen
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import com.phoebus.demo.phastpay.utils.ConstantsUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var phastPayClient: PhastPayClient
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startPaymentService()
        } else {
            AppToast.show(this, getString(R.string.permissions_denied))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppSmartDemoPhastPayTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    //Necessário criar um canal de notificação para androids com versão => 8 para permitir a execução de broadcasts implicitos.
    private fun startPaymentService() {
        val intent = Intent(this, ForegroundBroadcastService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (phastPayClient.isBound()) {
            phastPayClient.unbind()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!phastPayClient.isBound()) {
            phastPayClient.bind(object : PhastPayClient.IBindCallback {
                override fun onServiceDisconnected() {
                    Log.d(ConstantsUtils.TAG, "Servico desconectado")
                    AppToast.show(
                        applicationContext,
                        applicationContext.getString(R.string.bind_disconnected),
                        AppToast.Duration.SHORT
                    )
                }

                override fun onServiceConnected() {
                    Log.d(ConstantsUtils.TAG, "Servico conectado")
                    AppToast.show(
                        applicationContext,
                        applicationContext.getString(R.string.bind_connected),
                        AppToast.Duration.SHORT
                    )
                }
            })
        }
    }

}
