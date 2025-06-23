package com.phoebus.demo.phastpay.ui.features.getReports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RouteParams
import com.phoebus.demo.phastpay.utils.DateUtils
import com.phoebus.phastpay.sdk.client.PhastPayClient

@Composable
fun GetReportsScreen(
    navController: NavController,
    params: RouteParams.GetReports,
    phastPayClient: PhastPayClient,
    viewModel: GetReportsViewModel = viewModel()
) {
    val context = LocalContext.current

    var dialogMessage by remember { mutableStateOf<String?>(null) }
    var dialogTitle by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GetReportsEffect.Success -> {
                    dialogTitle = context.getString(R.string.success)
                    dialogMessage =
                        context.getString(R.string.get_report_screen_success_message)
                }
                is GetReportsEffect.Error -> {
                    dialogTitle = context.getString(R.string.error)
                    dialogMessage = effect.message
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            GetReportsEvent.UpdateParams(
                startDate = params.startData,
                endDate = params.endData,
                serviceType = params.service,
                reportType = params.reportType
            )
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.get_reports),
                navController = navController
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                GetReportsContent(
                    state = viewModel.state.value,
                    onFetchReport = {
                        viewModel.onEvent(GetReportsEvent.FetchReport(phastPayClient))
                    }
                )

                if (dialogMessage != null && dialogTitle != null) {
                    PhDialog(
                        title = dialogTitle ?: "",
                        message = dialogMessage ?: "",
                        onConfirm = {
                            dialogMessage = null
                            dialogTitle = null
                        },
                        onDismissRequest = {
                            dialogMessage = null
                            dialogTitle = null
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun GetReportsContent(
    state: GetReportsState,
    onFetchReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSuccessDialog by remember { mutableStateOf(true) }
    var showErrorDialog by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format(
                stringResource(R.string.filter_period_between_to),
                DateUtils.formatDateStrUTCToStrLocal(state.startDate),
                DateUtils.formatDateStrUTCToStrLocal(state.endDate)
            )
        )

        Text(
            text = stringResource(
                R.string.get_report_report_type,
                state.reportType.localizedName()
            )
        )

        Text(text = stringResource(R.string.get_report_service_type, state.serviceType))

        Button(onClick = onFetchReport) {
            Text(stringResource(R.string.get_reports))
        }
    }

    if (state.successMessage != null && showSuccessDialog) {
        PhDialog(
            title = stringResource(R.string.success),
            message = state.successMessage,
            onConfirm = { showSuccessDialog = false },
            onDismissRequest = { showSuccessDialog = false }
        )
    }

    if (state.errorMessage != null && showErrorDialog) {
        PhDialog(
            title = stringResource(R.string.error),
            message = state.errorMessage,
            onConfirm = { showErrorDialog = false },
            onDismissRequest = { showErrorDialog = false }
        )
    }
}

@Composable
fun ReportType.localizedName(): String {
    return when (this) {
        ReportType.SUMMARY -> stringResource(R.string.report_type_summary)
        ReportType.DETAILED -> stringResource(R.string.report_type_detailed)
    }
}

