package com.phoebus.demo.phastpay.ui.features.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.phoebus.demo.phastpay.ui.components.progress.LoadingIndicator
import com.phoebus.demo.phastpay.ui.navigation.MainNavHost


@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {

    val startRoute by viewModel.startRoute.collectAsState()
    val mainNavController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.onEvent(MainEvent.CheckStartRoute)
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (startRoute != null) {
                MainNavHost(
                    navController = mainNavController,
                    startRoute = startRoute!!,
                    json = viewModel.json
                )
            } else {
                LoadingIndicator()
            }
        }
    }
}


