package com.phoebus.demo.phastpay.ui.features.startPaymentApi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.ServiceSelector
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.payment.InputCurrency
import com.phoebus.demo.phastpay.ui.components.payment.InputValue
import com.phoebus.demo.phastpay.ui.components.progress.LoadingDialog
import com.phoebus.demo.phastpay.ui.components.selector.GenericSelector
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import com.phoebus.demo.phastpay.utils.CurrencyType


@Composable
fun PaymentApiScreen(
    navController: NavController,
    viewModel: PaymentApiViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dialogMessage by viewModel.dialogMessage.collectAsState()

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is PaymentApiNavigationEvents.NavigateToHome -> {
                    navController.navigate(RoutesConstants.ROUTE_HOME, builder = {
                        popUpTo(RoutesConstants.ROUTE_HOME) {
                            inclusive = true
                        }
                    })
                }
            }

        }
    }

    LaunchedEffect(viewModel) {
        viewModel.onEvent(
            PaymentApiEvent.Initialize
        )
    }

    PaymentApiScreenContent(
        state = state,
        dialogMessage = dialogMessage,
        onEvent = viewModel::onEvent,
        onDismissDialog = {
            viewModel.dismissDialog()
        },
        topBar = {
            TopBar(title = stringResource(R.string.payment_api), navController)
        }
    )
}

@Composable
private fun PaymentApiScreenContent(
    state: PaymentApiState,
    dialogMessage: String?,
    onEvent: (PaymentApiEvent) -> Unit,
    onDismissDialog: () -> Unit,
    topBar: @Composable () -> Unit
) {
    if (dialogMessage != null) {
        PhDialog(
            onConfirm = onDismissDialog,
            onDismissRequest = onDismissDialog,
            title = stringResource(R.string.result),
            message = dialogMessage,
            qrCodeBase64 = state.qrCodeResult?.base64
        )
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = topBar,
        content = {
            Box(modifier = Modifier.padding(it)) {
                PaymentContent(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}


@Composable
fun PaymentContent(
    state: PaymentApiState,
    onEvent: (PaymentApiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.client_id),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = state.appClientId,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 14.sp
                )
            }

            Text(
                text = stringResource(R.string.select_service),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ServiceSelector(
                    service = Service.valueOf(state.service),
                    onPhastTypeSelected = { onEvent(PaymentApiEvent.UpdateService(it.name)) },
                    allowedServices = listOf(Service.TWINT, Service.CRYPTO, Service.PIX)
                )
            }

            HorizontalDivider(thickness = 0.5.dp)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.providerId ?: "",
                    onValueChange = {
                        onEvent(PaymentApiEvent.UpdateProviderId(it))
                    },
                    label = { Text(stringResource(R.string.provider_id), fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(0.3f)) {
                        InputCurrency(
                            currencyType = CurrencyType.valueOf(state.currency),
                            onCurrencyChange = { onEvent(PaymentApiEvent.UpdateCurrency(it.name)) }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(0.7f)) {
                        InputValue(
                            label = stringResource(R.string.value),
                            value = state.value ?: "",
                            currencyType = CurrencyType.valueOf(state.currency),
                            onChangeValue = {
                                onEvent(PaymentApiEvent.UpdateValue(it))
                            },
                        )
                    }
                }
            }

            Column {
                GenericSelector(
                    label = stringResource(R.string.send_additional_value),
                    checked = state.sendTipValue,
                    onCheckedChange = {
                        onEvent(PaymentApiEvent.UpdateSendTipValue(it))
                        if (!it) onEvent(PaymentApiEvent.UpdateTipValue(null))
                    },
                    testTag = "tgl_switch_additional_value_screen"
                )

                if (state.sendTipValue) {
                    InputValue(
                        label = "Valor Adicional",
                        value = state.tipValue ?: "",
                        currencyType = CurrencyType.valueOf(state.currency),
                        onChangeValue = {
                            onEvent(PaymentApiEvent.UpdateTipValue(it))
                        },
                    )
                }
            }

            HorizontalDivider(thickness = 0.5.dp)

            Column {
                CheckboxPrint(
                    printCustomerReceiptChecked = state.printCustomerReceipt,
                    printMerchantReceiptChecked = state.printMerchantReceipt,
                    previewCustomerReceiptChecked = state.previewCustomerReceipt,
                    previewMerchantReceiptChecked = state.previewMerchantReceipt,
                    onPrintCustomerReceiptChange = {
                        onEvent(
                            PaymentApiEvent.UpdatePrintCustomerReceipt(
                                it
                            )
                        )
                    },
                    onPrintMerchantReceiptChange = {
                        onEvent(
                            PaymentApiEvent.UpdatePrintMerchantReceipt(
                                it
                            )
                        )
                    },
                    onPreviewCustomerReceiptChange = {
                        onEvent(
                            PaymentApiEvent.UpdatePreviewCustomerReceipt(
                                it
                            )
                        )
                    },
                    onPreviewMerchantReceiptChange = {
                        onEvent(
                            PaymentApiEvent.UpdatePreviewMerchantReceipt(
                                it
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            PhButton(
                title = stringResource(R.string.start_payment),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = !state.isLoading
            ) {
                onEvent(PaymentApiEvent.SubmitPaymentApi)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentApiScreenPreview() {
    AppSmartDemoPhastPayTheme {
        PaymentApiScreenContent(
            state = PaymentApiState(
                appClientId = "ad4c6cce-34ba-4177-931e-222d0ac47d9c",
                providerId = "123456",
                value = "1500"
            ),
            dialogMessage = null,
            onEvent = {},
            onDismissDialog = {},
            topBar = {
                TopBar(
                    title = stringResource(R.string.payment_api),
                    navController = rememberNavController()
                )
            }
        )
    }
}
