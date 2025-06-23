package com.phoebus.demo.phastpay.ui.features.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.DatePeriod
import com.phoebus.demo.phastpay.data.enums.FilterType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.demo.phastpay.ui.components.filter.FilterDate
import com.phoebus.demo.phastpay.ui.components.filter.FilterPeriodSelection
import com.phoebus.demo.phastpay.ui.components.filter.FilterReportType
import com.phoebus.demo.phastpay.ui.components.filter.FilterService
import com.phoebus.demo.phastpay.ui.components.filter.FilterTime
import com.phoebus.demo.phastpay.ui.components.filter.FilterTransactionStatus
import com.phoebus.demo.phastpay.ui.components.filter.FilterValue
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.GetPaymentsRoute
import com.phoebus.demo.phastpay.ui.navigation.GetTransactionsRoute
import com.phoebus.demo.phastpay.ui.navigation.GetPaymentsToRefundRoute
import com.phoebus.demo.phastpay.ui.navigation.GetReportsRoute
import com.phoebus.demo.phastpay.ui.navigation.RouteParams
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import com.phoebus.demo.phastpay.utils.DateUtils
import com.phoebus.demo.phastpay.utils.getScreenWidth


@Composable
fun FilterScreen(
    navController: NavController,
    filterType: FilterType = FilterType.GET_PAYMENTS,
    viewModel: FilterViewModel = viewModel(),
) {

    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.onEvent(FilterEvent.SelectFilterType(filterType))
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is FilterNavigationEvents.NavigateToGetPayments -> {
                    GetPaymentsRoute(
                        RouteParams.GetPayments(
                            event.startDateTime,
                            event.endDateTime,
                            event.status,
                            event.value
                        )
                    ).navigate(navController)
                }

                is FilterNavigationEvents.NavigateToGetTransactions -> {
                    GetTransactionsRoute(
                        RouteParams.GetTransactions(
                            event.startDateTime,
                            event.endDateTime,
                        )
                    ).navigate(navController)
                }

                is FilterNavigationEvents.NavigateToRefundList -> {
                    GetPaymentsToRefundRoute(
                        RouteParams.GetPaymentsToRefund(
                            event.startDateTime,
                            event.endDateTime,

                        )
                    ).navigate(navController)
                }

                is FilterNavigationEvents.NavigateToReport -> {
                    GetReportsRoute(
                        RouteParams.GetReports(
                            event.startDateTime,
                            event.endDateTime,
                            event.reportType,
                            event.serviceType

                            )
                    ).navigate(navController)
                }
            }
        }
    }

    val onClickFilter = {
        when (filterType) {
            FilterType.GET_TRANSACTIONS -> {
                viewModel.navigateToGetTransactions(
                    DateUtils.parseDateAndTimeToUTC(state.startDate, state.startTime),
                    DateUtils.parseDateAndTimeToUTC(state.endDate, state.endTime)
                )
            }

            FilterType.GET_PAYMENTS_TO_REFUND -> {
                viewModel.navigateToPaymentsToRefund(
                    DateUtils.parseDateAndTimeToUTC(state.startDate, state.startTime),
                    DateUtils.parseDateAndTimeToUTC(state.endDate, state.endTime),
                )
            }

            FilterType.REPORT -> {
                viewModel.navigateToReport(
                    DateUtils.parseDateAndTimeToUTC(state.startDate, state.startTime),
                    DateUtils.parseDateAndTimeToUTC(state.endDate, state.endTime),
                    state.reportType,
                    reportSelected = state.selectedService
                )
            }

            FilterType.GET_PAYMENTS -> {
                viewModel.navigateToGetListPayments(
                    DateUtils.parseDateAndTimeToUTC(state.startDate, state.startTime),
                    DateUtils.parseDateAndTimeToUTC(state.endDate, state.endTime),
                    state.status,
                    state.value
                )
            }
        }

    }


    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_filter), navController)
        },
        content = {
            FilterContent(
                Modifier.padding(it),
                formState = state,
                onEvent = viewModel::onEvent,
                onClickFilter = onClickFilter,
                selectedService = state.selectedService,
                onChangeService = { viewModel.onEvent(FilterEvent.SelectService(it)) }
            )
        }
    )
}


@Composable
fun FilterContent(
    modifier: Modifier = Modifier,
    formState: FilterState,
    onEvent: (FilterEvent) -> Unit,
    selectedService: ServiceType,
    onChangeService: (ServiceType) -> Unit,
    onClickFilter: () -> Unit
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 35.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FilterPeriodSelection(
                formState.periodSelected
            ) { onEvent(FilterEvent.SelectDatePeriod(it)) }

            Spacer(modifier = Modifier.height(16.dp))

            FilterTime(
                startTime = formState.startTime,
                endTime = formState.endTime,
                periodSelected = formState.periodSelected,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(16.dp))

            FilterDate(
                visible = formState.periodSelected == DatePeriod.OTHER_PERIOD,
                startDate = formState.startDate,
                endDate = formState.endDate,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (formState.filterType == FilterType.REPORT) {
                FilterService(
                    service = selectedService,
                    onServiceTypeSelected = { provider -> onChangeService(provider) }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }


            Spacer(modifier = Modifier.height(20.dp))

            FilterReportType(
                filterType = formState.filterType,
                reportSelected = formState.reportType,
            ) { onEvent(FilterEvent.SelectReportType(it)) }

            FilterTransactionStatus(
                filterType = formState.filterType,
                listStatus = listOf(
                    TransactionStatus.REQUEST_PAYMENT,
                    TransactionStatus.CONFIRMED_PAYMENT,
                    TransactionStatus.CANCELED_PAYMENT,
                    TransactionStatus.WAITING_PAYMENT,
                    TransactionStatus.ERROR_PAYMENT,
                    TransactionStatus.EXPIRED_PAYMENT,
                    TransactionStatus.PARTIAL_REFUND,
                    TransactionStatus.COMPLETED_REFUND,
                ),
                statusSelected = formState.status,
            ) {
                onEvent(FilterEvent.UpdateTransactionsStatus(it))
            }

            FilterValue(
                filterType = formState.filterType,
                value = formState.value ?: "",
            ) { onEvent(FilterEvent.UpdateValue(it)) }

        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            onClick = {
                onClickFilter()
            },
            enabled = true,
            modifier = Modifier
                .width(getScreenWidth() * 0.4f)
                .padding(top = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = "Ícone de filtro",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = stringResource(R.string.filter))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentTypeComponentPreview() {
    AppSmartDemoPhastPayTheme {
        FilterContent(
            formState = FilterState(
                filterType = FilterType.REPORT,
                periodSelected = DatePeriod.TODAY,
                startTime = "00:00",
                endTime = "23:59",
                startDate = "01/01/2025",
                endDate = "02/12/2025"
            ),
            onEvent = {},
            onClickFilter = {},
            selectedService = ServiceType.ALL,
            onChangeService = {}
        )
    }
}


