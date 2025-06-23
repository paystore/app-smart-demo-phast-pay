package com.phoebus.demo.phastpay.ui.features.sync

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
fun SyncDataScreen(
    navController: NavController,
    phastPayClient: PhastPayClient,
    viewModel: SyncDataViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(SyncDataEvent.StartSync(phastPayClient))
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is SyncDataNavigationEvents.NavigateToHome -> {
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
            TopBar(title = stringResource(R.string.sync_data), navController)
        },
        content = {
            SyncContent(
                formState = state,
                formEvent = viewModel::onEvent,
                navEvent = viewModel::onNavigationEvent,
                modifier = Modifier.padding(it)
            )
        }
    )




}

@Composable
fun SyncContent(
    formState: SyncDataState,
    formEvent: (SyncDataEvent) -> Unit,
    navEvent: (SyncDataNavigationEvents) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (formState.loading) {
            LoadingIndicator(text = stringResource(R.string.synchronizing_data))
        }

        formState.dialogMessage?.let { message ->
            PhDialog(
                onDismissRequest = {},
                onConfirm = {
                    formEvent(SyncDataEvent.UpdateDialogMessage(null))
                    navEvent(SyncDataNavigationEvents.NavigateToHome)
                },
                title = stringResource(R.string.Synchronization),
                message = message
            )
        }
    }
}


