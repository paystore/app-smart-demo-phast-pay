package com.phoebus.demo.phastpay.ui.features.startPayment

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.ServiceSelector
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.payment.InputValue
import com.phoebus.demo.phastpay.ui.components.payment.PhoneNumberInput
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants
import com.phoebus.demo.phastpay.utils.CurrencyType
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.ui.components.payment.getCountryCallingCode
import com.phoebus.demo.phastpay.utils.PhoneNumberConstants.countries
import com.phoebus.phastpay.sdk.client.PhastPayClient
import java.util.UUID


@Composable
fun PaymentScreen(
    navController: NavController,
    phastPayClient: PhastPayClient,
    viewModel: PaymentViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val dialogMessage by viewModel.dialogMessage.collectAsState()

    if (dialogMessage != null) {
        PhDialog(
            onConfirm = {
                viewModel.dismissDialog()
                viewModel.onNavigationEvent(PaymentNavigationEvents.NavigateToHome)
            },
            onDismissRequest = {
                viewModel.dismissDialog()
                viewModel.onNavigationEvent(PaymentNavigationEvents.NavigateToHome)
            },
            title = stringResource(R.string.result),
            message = dialogMessage ?: ""
        )
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is PaymentNavigationEvents.NavigateToHome -> {
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
            PaymentEvent.Initialize(
                appClientId = UUID.randomUUID().toString(),
                applicationId = "123456789",
                applicationName = context.getString(R.string.app_name)
            )
        )
    }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.payment), navController)
        },
        content = {
            PaymentContent(
                state = state,
                phastPayClient = phastPayClient,
                onEvent = viewModel::onEvent,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun PaymentContent(
    state: PaymentState,
    phastPayClient: PhastPayClient,
    onEvent: (PaymentEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val selectedCountry = remember { mutableStateOf(countries.first()) }

    LaunchedEffect(Unit) {
        onEvent(PaymentEvent.UpdateCountryCode(getCountryCallingCode(selectedCountry.value.code)))
    }

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
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.client_id),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = state.appClientId,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(text = stringResource(R.string.select_service))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ServiceSelector(
                    service = Service.valueOf(state.service),
                    onPhastTypeSelected = { onEvent(PaymentEvent.UpdateService(it.name)) },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.send_value))
                Switch(
                    checked = state.sendValue,
                    onCheckedChange = {
                        onEvent(PaymentEvent.UpdateSendValue(it))
                        if (!it) onEvent(PaymentEvent.UpdateValue(null))
                    }
                )
            }

            if (state.sendValue) {
                InputValue(
                    checked = state.sendValue,
                    value = state.value ?: "",
                    currencyType = CurrencyType.valueOf(state.currency ?: "EUR"),
                    onCurrencyChange = { onEvent(PaymentEvent.UpdateCurrency(it.name)) },
                    onChangeValue = { onEvent(PaymentEvent.UpdateValue(it)) },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.send_phone_number))
                Switch(
                    checked = state.sendPhoneNumber,
                    onCheckedChange = {
                        onEvent(PaymentEvent.UpdateSendPhoneNumber(it))
                        if (!it) onEvent(PaymentEvent.UpdateValue(null))
                    }
                )
            }

            if (state.sendPhoneNumber) {
                PhoneNumberInput(
                    country = selectedCountry.value,
                    updateCountry = { selectedCountry.value = it },
                    onDdiChange = { onEvent(PaymentEvent.UpdateCountryCode(it)) },
                    phone = state.phoneNumber ?: "",
                    onPhoneChange = { onEvent(PaymentEvent.UpdatePhoneNumber(it)) }
                )
            }

            AddictionalInfo(switchItem = state.switchAdditionalInfo, onSwitchChanged = onEvent)

            if (state.switchAdditionalInfo) {
                OutlinedTextField(
                    value = state.customerName ?: "",
                    onValueChange = {
                        onEvent(PaymentEvent.UpdateCustomerName(it))
                        nameError = it.isBlank()
                    },
                    label = { Text(stringResource(R.string.name)) },
                    isError = nameError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = {
                        if (nameError) Text(
                            stringResource(R.string.empty_name),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.customerEmail ?: "",
                    onValueChange = {
                        onEvent(PaymentEvent.UpdateCustomerEmail(it))
                        emailError = !Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    label = { Text(stringResource(R.string.email)) },
                    isError = emailError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    supportingText = {
                        if (emailError) Text(
                            stringResource(R.string.invalid_email),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                )
            }

            CheckboxPrint(
                printCustomerReceiptChecked = state.printCustomerReceipt,
                printMerchantReceiptChecked = state.printMerchantReceipt,
                onPrintCustomerReceiptChange = {
                    onEvent(PaymentEvent.UpdatePrintCustomerReceipt(it))
                },
                onPrintMerchantReceiptChange = {
                    onEvent(PaymentEvent.UpdatePrintMerchantReceipt(it))
                }
            )

            PhButton(
                title = stringResource(R.string.start_payment),
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            ) {
                onEvent(PaymentEvent.SubmitPayment(phastPayClient))
            }
        }
    }
}

@Composable
fun AddictionalInfo(
    switchItem: Boolean,
    onSwitchChanged: (PaymentEvent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.add_info),
            textAlign = TextAlign.Start,
        )

        Switch(
            checked = switchItem,
            onCheckedChange = { isChecked ->
                onSwitchChanged(PaymentEvent.SendAdditionalInfo(send = isChecked))
            },
            modifier = Modifier
                .testTag("tgl_switch_phone_screen")
                .semantics { contentDescription = "tgl_switch_phone_screen" },
        )
    }
}

fun validateInput(value: String): Boolean {
    return value.all { it.isDigit() }
}
