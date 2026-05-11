package com.phoebus.demo.phastpay.ui.features.abortPayment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar

@Composable
fun AbortPaymentScreen(
    navController: NavController,
    viewModel: AbortPaymentViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.abort_payment), navController)
        },
        content = {
            AbortPaymentContent(
                formState = viewModel.state,
                formEvent = viewModel::onEvent,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun AbortPaymentContent(
    formState: AbortPaymentState,
    formEvent: (AbortPaymentEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = formState.clientId,
            onValueChange = {
                formEvent(AbortPaymentEvent.OnClientIdChange(it))
            },
            label = { Text(stringResource(R.string.client_id)) },
            modifier = Modifier.fillMaxWidth()
        )

        if (formState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                formEvent(AbortPaymentEvent.OnAbortPaymentClick)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !formState.isLoading
        ) {
            Text(stringResource(R.string.abort_payment))
        }

        formState.message?.let {
            Text(it)
        }
    }
}


