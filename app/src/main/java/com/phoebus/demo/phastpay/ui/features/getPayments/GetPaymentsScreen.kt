package com.phoebus.demo.phastpay.ui.features.getPayments

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentByAppClientIdResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayGetPaymentsResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayGetRefundByIdResponse
import com.phoebus.demo.phastpay.data.dto.PhastPayProviderData
import com.phoebus.demo.phastpay.data.dto.PhastPayRefundProviderData
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.demo.phastpay.ui.components.receipt.PhastItem
import com.phoebus.demo.phastpay.ui.components.receipt.RefundsItem
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RouteParams
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme

@Composable
fun GetPaymentsScreen(
    navController: NavController,
    params: RouteParams.GetPayments,
    viewModel: GetPaymentsViewModel = hiltViewModel()
) {

    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            GetPaymentsEvent.UpdateParams(
                startDate = params.startData,
                endDate = params.endData,
                status = params.status,
                value = if (params.value == "0,00") {
                    null
                } else {
                    params.value?.replace(",", ".")
                }
            )
        )
        viewModel.onEvent(GetPaymentsEvent.StartGetPayments)
    }


    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_list_payments), navController)
        },
        content = {
            GetPaymentsContent(
                formState = state,
                formEvent = viewModel::onEvent,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun GetPaymentsContent(
    formState: GetPaymentsState,
    formEvent: (GetPaymentsEvent) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current;
    val text: String? = when {
        formState.getListResult == null -> stringResource(R.string.finding_list)
        formState.getListResult.payments.isEmpty() -> stringResource(R.string.empty_list)
        else -> null
    }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        text?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        }

        formState.getListResult.let {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (it != null) {
                    items(it.payments) { item ->
                        val refunds = item.refunds?.map{ ref ->
                            RefundsItem(refundId = ref.refundId, valor = ref.value, status = ref.status, dateTime = ref.dateTime, iva = ref.iva, providerData = ref.providerData)
                        }
                        PhastItem(
                            currency = item.currency,
                            status = item.status,
                            appClientId = item.appClientId,
                            service = item.service,
                            paymentId = item.paymentId,
                            value = item.value,
                            additionalValue = item.additionalValue,
                            dateTime = item.dateTime,
                            iva = item.iva,
                            refunds = refunds,
                            providerData = item.providerData
                        )
                    }
                }
            }
        }


        formState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            formEvent(GetPaymentsEvent.UpdateMessageError(null))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GetPaymentsContentPreview() {
    AppSmartDemoPhastPayTheme {
        GetPaymentsContent(
            formState = GetPaymentsState(
                getListResult = PhastPayGetPaymentsResponse(
                    payments = listOf(
                        PhastPayGetPaymentByAppClientIdResponse(
                            paymentId = "PAY-0001",
                            appClientId = "CLIENT-0001",
                            status = TransactionStatus.CONFIRMED_PAYMENT.name,
                            value = "25.00",
                            dateTime = "2026-08-12T10:30:00Z",
                            service = ServiceType.BIZUM.name,
                            currency = "EUR"
                        ),
                        PhastPayGetPaymentByAppClientIdResponse(
                            paymentId = "PAY-0002",
                            appClientId = "CLIENT-0002",
                            status = TransactionStatus.CONFIRMED_PAYMENT.name,
                            value = "150.00",
                            dateTime = "2026-08-12T09:00:00Z",
                            service = ServiceType.CRYPTO.name,
                            currency = "EUR",
                            providerData = PhastPayProviderData(
                                leftToPayAmountValue = "0.00",
                                cryptoAmount = "0.0021",
                                cryptoCurrencyCode = "BTC",
                                network = "Bitcoin",
                                processingFeeFiatAmount = "1.50",
                                transactionHash = "0xabc123def456"
                            )
                        ),
                        PhastPayGetPaymentByAppClientIdResponse(
                            paymentId = "PAY-0003",
                            appClientId = "CLIENT-0003",
                            status = TransactionStatus.WAITING_PAYMENT.name,
                            value = "10.50",
                            dateTime = "2026-08-11T18:15:00Z",
                            service = ServiceType.PIX.name,
                            currency = "USD",
                            providerData = PhastPayProviderData(
                                rate = "5.42",
                                amountBrl = "56.91"
                            ),
                            refunds = listOf(
                                PhastPayGetRefundByIdResponse(
                                    refundId = "REF-0001",
                                    appClientId = "CLIENT-0003",
                                    status = TransactionStatus.COMPLETED_REFUND.name,
                                    value = "5.25",
                                    dateTime = "2026-08-12T08:00:00Z",
                                    providerData = PhastPayRefundProviderData(
                                        rateRefunded = "5.42",
                                        amountRefundedBrl = "28.46"
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            formEvent = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GetPaymentsContentEmptyPreview() {
    AppSmartDemoPhastPayTheme {
        GetPaymentsContent(
            formState = GetPaymentsState(
                getListResult = PhastPayGetPaymentsResponse(payments = emptyList())
            ),
            formEvent = {},
            modifier = Modifier
        )
    }
}
