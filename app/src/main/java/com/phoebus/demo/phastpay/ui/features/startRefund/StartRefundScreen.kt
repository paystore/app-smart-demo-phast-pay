package com.phoebus.demo.phastpay.ui.features.startRefund

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.payment.InputValue
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants


@Composable
fun StartRefundScreen(
    navController: NavController,
    viewModel: StartRefundViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.onEvent(
            StartRefundEvent.Initialize
        )
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is StartRefundNavigationEvents.NavigateToHome -> {
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
            TopBar(title = stringResource(R.string.start_refund), navController)
        },
        content = {
            StartRefundContent(
                formState = state,
                formEvent = viewModel::onEvent,
                navEvent = viewModel::onNavigationEvent,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun StartRefundContent(
    formState: StartRefundState,
    formEvent: (StartRefundEvent) -> Unit,
    navEvent: (StartRefundNavigationEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
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
            OutlinedTextField(
                value = formState.paymentId,
                onValueChange = {
                    formEvent(StartRefundEvent.UpdatePaymentId(it))
                },
                label = { Text(stringResource(R.string.find_by_payment_id)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.send_partial_value))
                Switch(
                    checked = formState.sendPartialValue,
                    onCheckedChange = {
                        formEvent(StartRefundEvent.UpdateSendPartialValue(it))
                        if (!it) formEvent(StartRefundEvent.UpdateValue(null))
                    }
                )
            }

            if (formState.sendPartialValue) {
                InputValue(
                    checked = true,
                    value = formState.value ?: "",
                    showCurrency = false,
                    onChangeValue = { formEvent(StartRefundEvent.UpdateValue(it)) }
                )
            }

            CheckboxPrint(
                printCustomerReceiptChecked = formState.printCustomerReceipt,
                printMerchantReceiptChecked = formState.printMerchantReceipt,
                previewCustomerReceiptChecked = formState.previewCustomerReceipt,
                previewMerchantReceiptChecked = formState.previewMerchantReceipt,
                onPrintCustomerReceiptChange = {
                    formEvent(StartRefundEvent.UpdatePrintCustomerReceipt(it))
                },
                onPrintMerchantReceiptChange = {
                    formEvent(StartRefundEvent.UpdatePrintMerchantReceipt(it))
                },
                onPreviewCustomerReceiptChange = {
                    formEvent(StartRefundEvent.UpdatePreviewCustomerReceipt(it))
                },
                onPreviewMerchantReceiptChange = {
                    formEvent(StartRefundEvent.UpdatePreviewMerchantReceipt(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PhButton(
                title = stringResource(R.string.start_refund),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.CenterHorizontally),
                enabled = formState.paymentId.isNotEmpty()
            ) {
                formEvent(StartRefundEvent.SubmitRefund)
            }

            formState.refundResult?.let {
                PhDialog(
                    onDismissRequest = {
                        formEvent(StartRefundEvent.UpdateSuccessMessage(null))
                    },
                    onConfirm = {
                        navEvent(StartRefundNavigationEvents.NavigateToHome)
                        formEvent(StartRefundEvent.UpdateSuccessMessage(null))
                    },
                    message = formState.refundResult
                )
            }

            formState.errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                formEvent(StartRefundEvent.UpdateErrorMessage(null))
            }
        }
    }
}