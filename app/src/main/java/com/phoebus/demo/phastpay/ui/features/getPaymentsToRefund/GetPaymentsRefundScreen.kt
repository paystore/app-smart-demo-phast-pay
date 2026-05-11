package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RouteParams
import com.phoebus.demo.phastpay.utils.DateUtils

@Composable
fun GetPaymentsToRefundScreen(
    navController: NavController,
    params: RouteParams.GetPaymentsToRefund,
    viewModel: GetPaymentsToRefundViewModel = hiltViewModel()
) {
    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            GetPaymentsToRefundEvent.UpdateParams(
                startDate = params.startData,
                endDate = params.endData,
            )
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.get_list_to_refund_get_refunds),
                navController = navController
            )
        },
        content = {
            GetPaymentsToRefundContent(
                formEvent = viewModel::onEvent,
                formState = state,
                modifier = Modifier.padding(it)
            )
        }
    )

}

@Composable
fun GetPaymentsToRefundContent(
    formState: GetPaymentsToRefundState,
    formEvent: (GetPaymentsToRefundEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (formState.startDate != null || formState.endDate != null) {
            Text(
                text = String.format(
                    stringResource(R.string.filter_period_between_to),
                    DateUtils.formatDateStrUTCToStrLocal(formState.startDate.orEmpty()),
                    DateUtils.formatDateStrUTCToStrLocal(formState.endDate.orEmpty())
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        CheckboxPrint(
            printCustomerReceiptChecked = formState.printCustomerReceipt,
            printMerchantReceiptChecked = formState.printMerchantReceipt,
            previewCustomerReceiptChecked = formState.previewCustomerReceipt,
            previewMerchantReceiptChecked = formState.previewMerchantReceipt,
            onPrintCustomerReceiptChange = {
                formEvent(GetPaymentsToRefundEvent.UpdatePrintCustomerReceipt(it))
            },
            onPrintMerchantReceiptChange = {
                formEvent(GetPaymentsToRefundEvent.UpdatePrintMerchantReceipt(it))
            },
            onPreviewCustomerReceiptChange = {
                formEvent(GetPaymentsToRefundEvent.UpdatePreviewCustomerReceipt(it))
            },
            onPreviewMerchantReceiptChange = {
                formEvent(GetPaymentsToRefundEvent.UpdatePreviewMerchantReceipt(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PhButton(
            title = stringResource(R.string.get_list_to_refund_get_list_refunds),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally)
        ) {
            formEvent(
                GetPaymentsToRefundEvent.OnSubmit
            )
        }

        formState.refundResult?.let {
            PhDialog(
                title = stringResource(R.string.method_response),
                onDismissRequest = {
                    formEvent(GetPaymentsToRefundEvent.UpdateRefundResult(null))
                },
                onConfirm = {
                    formEvent(GetPaymentsToRefundEvent.UpdateRefundResult(null))
                },
                message = formState.refundResult
            )
        }

        formState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            formEvent(GetPaymentsToRefundEvent.UpdateErrorMessage(null))
        }
    }
}