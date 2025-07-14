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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.receipt.PhastItem
import com.phoebus.demo.phastpay.ui.components.receipt.RefundsItem
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RouteParams
import com.phoebus.phastpay.sdk.client.PhastPayClient

@Composable
fun GetPaymentsScreen(
    navController: NavController,
    params: RouteParams.GetPayments,
    phastPayClient: PhastPayClient,
    viewModel: GetPaymentsViewModel = viewModel()
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
        viewModel.onEvent(GetPaymentsEvent.StartGetPayments(phastPayClient))
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
                        val refunds = item.refunds?.map{
                            RefundsItem(refundId = it.refundId, valor = it.value, status = it.status, dateTime = it.dateTime, iva = it.iva)
                        }
                        PhastItem(
                            currency = item.currency,
                            status = item.status,
                            appClientId = item.appClientId,
                            service = item.service,
                            paymentId = item.paymentId,
                            value = item.value,
                            dateTime = item.dateTime,
                            iva = item.iva,
                            refunds = refunds
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


