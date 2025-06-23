package com.phoebus.demo.phastpay

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.ui.AppDemo
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import com.phoebus.demo.phastpay.utils.ConstantsUtils

class MainActivity : ComponentActivity() {
    private lateinit var phastPayClient: PhastPayClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phastPayClient = PhastPayClient(this);
        enableEdgeToEdge()
        setContent {
            AppSmartDemoPhastPayTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    AppDemo(phastPayClient)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        phastPayClient?.unbind()
    }

    override fun onResume() {
        super.onResume()
        phastPayClient.bind(object : PhastPayClient.IBindCallback {
            override fun onServiceDisconnected() {
                Log.d(ConstantsUtils.TAG, "Servico desconectado")
                Toast.makeText(applicationContext, "Servico desconectado", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onServiceConnected() {
                Log.d(ConstantsUtils.TAG, "Servico conectado")
                Toast.makeText(applicationContext, "Servico conectado", Toast.LENGTH_SHORT).show()
            }
        })
    }

}