package com.phoebus.demo.phastpay.ui.features.getRefundById

import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.phastpay.sdk.client.PhastPayClient

@Composable
fun GetRefundByIdScreen(
    navController: NavController,
    phastPayClient: PhastPayClient,
    viewModel: GetRefundByIdViewModel = viewModel()
) {
    val state by viewModel.state

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_refund_id), navController)
        },
        content = {
            FindRefundByIdContent(
                phastPayClient = phastPayClient,
                formEvent = viewModel::onEvent,
                formState = state,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun FindRefundByIdContent(
    modifier: Modifier = Modifier,
    phastPayClient: PhastPayClient,
    formState: GetRefundByIdState,
    formEvent: (GetRefundByIdEvent) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val refundId = formState.refundId
    val context = LocalContext.current
    val printCustomerReceipt = formState.printCustomerReceipt
    val printMerchantReceipt = formState.printMerchantReceipt

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = refundId,
            onValueChange = {
                formEvent(GetRefundByIdEvent.UpdateRefundById(it))
            },
            label = { Text(stringResource(R.string.find_by_refund_id)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CheckboxPrint(
            printCustomerReceiptChecked = printCustomerReceipt,
            printMerchantReceiptChecked = printMerchantReceipt,
            onPrintCustomerReceiptChange = {
                formEvent(GetRefundByIdEvent.UpdatePrintCustomerReceipt(!printCustomerReceipt))
            },
            onPrintMerchantReceiptChange = {
                formEvent(GetRefundByIdEvent.UpdatePrintMerchantReceipt(!printMerchantReceipt))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PhButton(
            title = stringResource(R.string.check_button),
            modifier = Modifier.fillMaxWidth(),
            enabled = formState.refundId.isNotEmpty()
        ) {
            formEvent(GetRefundByIdEvent.OnSubmit(phastPayClient))
        }

        formState.refundResult?.let {
            PhDialog(
                onConfirm = {
                    formEvent(GetRefundByIdEvent.UpdateRefundByResult(null))
                },
                onDismissRequest = {
                    formEvent(GetRefundByIdEvent.UpdateRefundByResult(null))
                },
                title = stringResource(R.string.method_response),
                message = formState.refundResult.toJson()
            )
        }

        formState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            formEvent(GetRefundByIdEvent.UpdateErrorMessage(null))
        }
    }
}