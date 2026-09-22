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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.ServiceSelector
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.payment.InputCurrency
import com.phoebus.demo.phastpay.ui.components.payment.InputValue
import com.phoebus.demo.phastpay.ui.components.payment.PhoneNumberInput
import com.phoebus.demo.phastpay.ui.components.payment.getCountryCallingCode
import com.phoebus.demo.phastpay.ui.components.selector.GenericSelector
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants
import com.phoebus.demo.phastpay.utils.CurrencyType
import com.phoebus.demo.phastpay.utils.PhoneNumberConstants.countries


@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
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
            PaymentEvent.Initialize
        )
    }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.payment), navController)
        },
        content = {
            PaymentContent(
                state = state,
                onEvent = viewModel::onEvent,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun PaymentContent(
    state: PaymentState,
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

            ServicePaymentSelector(formState = state, formEvent = onEvent)

            GenericSelector(
                label = stringResource(R.string.send_currency),
                checked = state.sendValue || state.sendTipValue || state.sendCurrency,
                enabled = !state.sendValue && !state.sendTipValue,
                onCheckedChange = { onEvent(PaymentEvent.UpdateSendCurrency(it)) },
                testTag = "tgl_switch_send_currency"
            )

            if (state.sendValue || state.sendTipValue || state.sendCurrency) {
                InputCurrency(
                    currencyType = CurrencyType.valueOf(state.currency),
                    onCurrencyChange = { onEvent(PaymentEvent.UpdateCurrency(it.name)) }
                )
            }

            GenericSelector(
                label = stringResource(R.string.send_value),
                checked = state.sendValue,
                onCheckedChange = {
                    onEvent(PaymentEvent.UpdateSendValue(it))
                    if (!it) onEvent(PaymentEvent.UpdateValue(null))
                },
                testTag = "tgl_switch_value_screen"
            )

            if (state.sendValue) {
                InputValue(
                    checked = true,
                    value = state.value ?: "",
                    currencyType = CurrencyType.valueOf(state.currency),
                    onChangeValue = { onEvent(PaymentEvent.UpdateValue(it)) },
                )
            }

            if (state.service in listOf(Service.MBWAY.name, Service.BIZUM.name)) {
                GenericSelector(
                    label = stringResource(R.string.send_phone_number),
                    checked = state.sendPhoneNumber,
                    onCheckedChange = { onEvent(PaymentEvent.UpdateSendPhoneNumber(it)) },
                    testTag = "tgl_switch_send_phone_number"
                )

                if (state.sendPhoneNumber) {
                    PhoneNumberInput(
                        country = selectedCountry.value,
                        updateCountry = { selectedCountry.value = it },
                        onDdiChange = { onEvent(PaymentEvent.UpdateCountryCode(it)) },
                        phone = state.phoneNumber ?: "",
                        onPhoneChange = { onEvent(PaymentEvent.UpdatePhoneNumber(it)) }
                    )
                }
            }
            GenericSelector(
                label = stringResource(R.string.send_additional_value),
                checked = state.sendTipValue,
                onCheckedChange = { onEvent(PaymentEvent.UpdateSendTipValue(it)) },
                testTag = "tgl_switch_phone_screen"
            )

            if (state.sendTipValue) {
                InputValue(
                    checked = true,
                    value = state.additionalValue ?: "",
                    currencyType = CurrencyType.valueOf(state.currency),
                    onChangeValue = { onEvent(PaymentEvent.UpdateTipValue(it)) },
                )
            }


            GenericSelector(
                label = stringResource(R.string.send_provider_id),
                checked = state.switchSendProviderId,
                onCheckedChange = { onEvent(PaymentEvent.UpdateSendProviderId(sendProviderId = it)) },
                testTag = "tgl_switch_send_providerId_info"
            )
            if (state.switchSendProviderId) {
                OutlinedTextField(
                    value = state.providerId ?: "",
                    onValueChange = { onEvent(PaymentEvent.UpdateProviderId(it)) },
                    label = { Text(stringResource(R.string.provider_id)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            if (state.service in listOf(Service.MBWAY.name, Service.BIZUM.name)) {
                GenericSelector(
                    label = stringResource(R.string.add_info),
                    checked = state.switchAdditionalInfo,
                    onCheckedChange = { onEvent(PaymentEvent.SendAdditionalInfo(send = it)) },
                    testTag = "tgl_switch_add_info"
                )
            }

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
                previewCustomerReceiptChecked = state.previewCustomerReceipt,
                previewMerchantReceiptChecked = state.previewMerchantReceipt,
                onPrintCustomerReceiptChange = {
                    onEvent(PaymentEvent.UpdatePrintCustomerReceipt(it))
                },
                onPrintMerchantReceiptChange = {
                    onEvent(PaymentEvent.UpdatePrintMerchantReceipt(it))
                },
                onPreviewCustomerReceiptChange = {
                    onEvent(PaymentEvent.UpdatePreviewCustomerReceipt(it))
                },
                onPreviewMerchantReceiptChange = {
                    onEvent(PaymentEvent.UpdatePreviewMerchantReceipt(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PhButton(
                title = stringResource(R.string.start_payment),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.CenterHorizontally),
                enabled = true
            ) {
                onEvent(PaymentEvent.SubmitPayment)
            }

            if (state.service != Service.MBWAY.name) {
                Spacer(modifier = Modifier.height(8.dp))

                PhButton(
                    title = stringResource(R.string.start_payment_abort),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.CenterHorizontally),
                    enabled = true
                ) {
                    onEvent(PaymentEvent.SubmitPaymentWithAbort)
                }

                Card(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.start_payment_abort_info),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServicePaymentSelector(
    modifier: Modifier = Modifier,
    formState: PaymentState,
    formEvent: (PaymentEvent) -> Unit
) {
    ServiceSelector(
        service = Service.valueOf(formState.service),
        onPhastTypeSelected = { formEvent(PaymentEvent.UpdateService(it.name)) },
        modifier = modifier.fillMaxWidth()
    )
}

fun validateInput(value: String): Boolean {
    return value.all { it.isDigit() }
}
