package com.phoebus.demo.phastpay.ui.features.printReceipt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.CheckboxWithLabel
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.popup.AppToast
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants

@Composable
fun PrintReceiptScreen(
    navController: NavController,
    viewModel: PrintReceiptViewModel = hiltViewModel()
) {
    val formState by viewModel.state.collectAsStateWithLifecycle()
    val formEvent = viewModel::onEvent

    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is PrintReceiptSideEffect.ShowToast -> {
                    AppToast.show(context, effect.message, AppToast.Duration.SHORT)
                }
            }
        }
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is PrintReceiptNavigationEvents.NavigateToHome -> {
                    navController.navigate(RoutesConstants.ROUTE_HOME, builder = {
                        popUpTo(RoutesConstants.ROUTE_HOME) {
                            inclusive = true
                        }
                    })
                }
            }

        }
    }

    LaunchedEffect(Unit) {
        formEvent(PrintReceiptEvent.Initialize)
    }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.print_receipt), navController)
        },
        content = {
            PrintReceiptContent(
                formState = formState,
                formEvent = formEvent,
                modifier = Modifier.padding(it)
            )
        }
    )
}

@Composable
fun PrintReceiptContent(
    formState: PrintReceiptState,
    formEvent: (PrintReceiptEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val clientIdFocusRequester = remember { FocusRequester() }
    val paymentIdFocusRequester = remember { FocusRequester() }
    val refundIdFocusRequester = remember { FocusRequester() }

    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(25.dp)
        ) {

            CheckboxWithLabel(
                checked = formState.lastTransaction,
                onCheckedChange = {
                    formEvent(PrintReceiptEvent.UpdateLastTransaction(it))
                },
                text = stringResource(R.string.print_last_transaction)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = formState.appClientId,
                onValueChange = {
                    formEvent(PrintReceiptEvent.UpdateClientId(it))
                },
                label = { Text(stringResource(R.string.find_by_app_client_id)) },
                enabled = !formState.lastTransaction,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onDone = {
                        paymentIdFocusRequester.requestFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(clientIdFocusRequester)
            )

            OutlinedTextField(
                value = formState.paymentId,
                onValueChange = {
                    formEvent(PrintReceiptEvent.UpdatePaymentId(it))
                },
                label = { Text(stringResource(R.string.find_by_payment_id)) },
                enabled = !formState.lastTransaction,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onDone = {
                        refundIdFocusRequester.requestFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(paymentIdFocusRequester)
            )

            OutlinedTextField(
                value = formState.refundId,
                onValueChange = {
                    formEvent(PrintReceiptEvent.UpdateRefundId(it))
                },
                label = { Text(stringResource(R.string.find_by_refund_id)) },
                enabled = !formState.lastTransaction,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(refundIdFocusRequester)
            )


            CheckboxPrint(
                printCustomerReceiptChecked = formState.printCustomerReceipt,
                printMerchantReceiptChecked = formState.printMerchantReceipt,
                previewCustomerReceiptChecked = formState.previewCustomerReceipt,
                previewMerchantReceiptChecked = formState.previewMerchantReceipt,
                onPrintCustomerReceiptChange = {
                    formEvent(PrintReceiptEvent.UpdatePrintCustomerReceipt(it))
                },
                onPrintMerchantReceiptChange = {
                    formEvent(PrintReceiptEvent.UpdatePrintMerchantReceipt(it))
                },
                onPreviewCustomerReceiptChange = {
                    formEvent(PrintReceiptEvent.UpdatePreviewCustomerReceipt(it))
                },
                onPreviewMerchantReceiptChange = {
                    formEvent(PrintReceiptEvent.UpdatePreviewMerchantReceipt(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PhButton(
                title = stringResource(R.string.print_receipt),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.CenterHorizontally),
                enabled = formState.isButtonEnabled()
            ) {
                formEvent(PrintReceiptEvent.OnSubmit)
            }
        }
    }
}
