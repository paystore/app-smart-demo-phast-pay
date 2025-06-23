package com.phoebus.demo.phastpay.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.phoebus.phastpay.sdk.client.PhastPayClient
import com.phoebus.demo.phastpay.ui.navigation.DemoNavHost

@Composable
fun AppDemo(phastPayClient: PhastPayClient) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DemoNavHost(navController = navController, phastPayClient)
        }
    }
}