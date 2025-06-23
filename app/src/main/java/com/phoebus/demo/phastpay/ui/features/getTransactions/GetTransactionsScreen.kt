package com.phoebus.demo.phastpay.ui.features.getTransactions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.CheckboxPrint
import com.phoebus.demo.phastpay.ui.components.button.PhButton
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RouteParams
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants
import com.phoebus.demo.phastpay.utils.DateUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient

@Composable
fun GetTransactionsScreen(
    navController: NavController,
    params: RouteParams.GetTransactions,
    phastPayClient: PhastPayClient,
    viewModel: GetTransactionsViewModel = viewModel()
) {

    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            GetListTransactionsEvent.UpdateParams(
                startDate = params.startData,
                endDate = params.endData
            )
        )
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is GetTransactionsNavigationEvents.NavigateToHome -> {
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
            TopBar(title = stringResource(R.string.get_list_transactions), navController)
        },
        content = {
            GetTransactionsContent(
                formState = state,
                formEvent = viewModel::onEvent,
                navEvent = viewModel::onNavigationEvent,
                phastPayClient = phastPayClient,
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun GetTransactionsContent(
    formState: GetTransactionsState,
    formEvent: (GetListTransactionsEvent) -> Unit,
    navEvent: (GetTransactionsNavigationEvents) -> Unit,
    phastPayClient: PhastPayClient,
    modifier: Modifier
) {
    val context = LocalContext.current;

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = String.format(
                stringResource(R.string.filter_period_between_to),
                DateUtils.formatDateStrUTCToStrLocal(formState.startDate),
                DateUtils.formatDateStrUTCToStrLocal(formState.endDate)
            )

        );

        Spacer(modifier = Modifier.height(8.dp))

        CheckboxPrint(
            printCustomerReceiptChecked = formState.printCustomerReceipt,
            printMerchantReceiptChecked = formState.printMerchantReceipt,
            onPrintCustomerReceiptChange = {
                formEvent(GetListTransactionsEvent.UpdatePrintCustomerReceipt(it))
            },
            onPrintMerchantReceiptChange = {
                formEvent(GetListTransactionsEvent.UpdatePrintMerchantReceipt(it))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PhButton(title = stringResource(R.string.check_button)) {
            formEvent(GetListTransactionsEvent.StartGet(phastPayClient))
        }

        formState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            formEvent(GetListTransactionsEvent.UpdateMessageError(null))
        }

        formState.getListResult?.let {
            PhDialog(
                onConfirm = {
                    navEvent(GetTransactionsNavigationEvents.NavigateToHome)
                },
                onDismissRequest = {
                    navEvent(GetTransactionsNavigationEvents.NavigateToHome)
                },
                title = stringResource(R.string.method_response),
                message = formState.getListResult.toJson()
            )
        }

    }

}


