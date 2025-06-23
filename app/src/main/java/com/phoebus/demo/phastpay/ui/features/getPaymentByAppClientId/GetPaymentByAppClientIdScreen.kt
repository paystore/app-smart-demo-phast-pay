package com.phoebus.demo.phastpay.ui.features.getPaymentByAppClientId

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
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.phastpay.sdk.client.PhastPayClient

@Composable
fun GetPaymentByAppClientIdScreen(
    navController: NavController,
    phastPayClient: PhastPayClient,
    viewModel: GetPaymentByAppClientIdViewModel = viewModel()
) {
    val state by viewModel.state

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_payment_by_client_id), navController)
        },
        content = {
            FindPaymentByAppClientIdContent(
                phastPayClient = phastPayClient,
                formEvent = viewModel::onEvent,
                formState = state,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun FindPaymentByAppClientIdContent(
    modifier: Modifier = Modifier,
    phastPayClient: PhastPayClient,
    formState: GetPaymentByAppClientIdState,
    formEvent: (GetPaymentByAppClientIdEvent) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val paymentId = formState.appClientId
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = paymentId,
            onValueChange = {
                formEvent(GetPaymentByAppClientIdEvent.UpdateAppClientIdPayment(it))
            },
            label = { Text(stringResource(R.string.find_by_app_client_id)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        PhButton(
            title = stringResource(R.string.check_button),
            modifier = Modifier.fillMaxWidth(),
            enabled = formState.appClientId.isNotEmpty()
        ) {
            formEvent(GetPaymentByAppClientIdEvent.OnSubmit(phastPayClient))
        }

        formState.successMessage?.let {
            PhDialog(
                onConfirm = {
                    formEvent(GetPaymentByAppClientIdEvent.UpdateSuccessMessage(null))
                },
                onDismissRequest = {
                    formEvent(GetPaymentByAppClientIdEvent.UpdateSuccessMessage(null))
                },
                title = stringResource(R.string.method_response),
                message = formState.successMessage
            )
        }

        formState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            formEvent(GetPaymentByAppClientIdEvent.UpdateErrorMessage(null))
        }
    }
}