package com.phoebus.demo.phastpay.ui.features.getqrcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.ui.components.ServiceSelector
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.popup.AppToast
import com.phoebus.demo.phastpay.ui.components.progress.LoadingIndicator
import com.phoebus.demo.phastpay.ui.components.qrcode.QRCodeWithLogo
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar

@Composable
fun GetQrCodeScreen(
    navController: NavController,
    viewModel: GetQrCodeViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is GetQrCodeSideEffect.ShowToast -> {
                    AppToast.show(context, effect.message, AppToast.Duration.SHORT)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_qrcode), navController)
        },
        content = {
            QrCodeContent(
                formEvent = viewModel::onEvent,
                formState = state,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun QrCodeContent(
    modifier: Modifier = Modifier,
    formState: GetQrCodeState,
    formEvent: (GetQrCodeEvent) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val providerId = formState.providerId

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = providerId,
            onValueChange = {
                formEvent(GetQrCodeEvent.UpdateProviderId(it))
            },
            label = { Text(stringResource(R.string.provider_id)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ServiceSelector(
                service = formState.service,
                allowedServices = listOf(Service.TWINT),
                onPhastTypeSelected = { formEvent(GetQrCodeEvent.UpdateService(it)) },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PhButton(
            title = stringResource(R.string.get_qrcode),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally),
            enabled = formState.providerId.isNotEmpty() || !formState.isLoading
        ) {
            formEvent(GetQrCodeEvent.OnSubmit)
        }

        if (formState.isLoading) {
            LoadingIndicator(Modifier.fillMaxWidth(), text = "")

        } else {
            formState.qrCodeResult?.let {
                QRCodeWithLogo(
                    logoBase64 = formState.qrCodeResult.base64,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

    }
}