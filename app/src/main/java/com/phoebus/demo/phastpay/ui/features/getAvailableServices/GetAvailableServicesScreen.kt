package com.phoebus.demo.phastpay.ui.features.getAvailableServices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.progress.LoadingIndicator
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants
import com.phoebus.phastpay.sdk.client.PhastPayClient


@Composable
fun GetAvailableServicesScreen(
    navController: NavController,
    phastPayClient: PhastPayClient,
    viewModel: GetAvailableServicesModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(GetAvailableServicesEvent.StartGetServices(phastPayClient))
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is GetAvailableServicesNavigationEvents.NavigateToHome -> {
                    navController.navigate(RoutesConstants.ROUTE_HOME, builder = {
                        popUpTo(RoutesConstants.ROUTE_HOME) {
                            inclusive = true
                        }
                    })
                }
            }

        }
    }


    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_available_services), navController)
        },
        content = {
            GetAvailableServicesContent(
                formState = state,
                formEvent = viewModel::onEvent,
                navEvent = viewModel::onNavigationEvent,
                modifier = Modifier.padding(it)
            )
        }
    )




}

@Composable
fun GetAvailableServicesContent(
    formState: GetAvailableServicesState,
    formEvent: (GetAvailableServicesEvent) -> Unit,
    navEvent: (GetAvailableServicesNavigationEvents) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (formState.loading) {
            LoadingIndicator(text = stringResource(R.string.getting_available_services))
        }

        formState.dialogMessage?.let { message ->
            PhDialog(
                onDismissRequest = {},
                onConfirm = {
                    formEvent(GetAvailableServicesEvent.UpdateDialogMessage(null))
                    navEvent(GetAvailableServicesNavigationEvents.NavigateToHome)
                },
                title = stringResource(R.string.get_available_services),
                message = message
            )
        }
    }
}


