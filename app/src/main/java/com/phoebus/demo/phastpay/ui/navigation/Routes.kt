package com.phoebus.demo.phastpay.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

sealed class NavDestination(
    val route: String
) {
    data object Home : NavDestination(
        route = RoutesConstants.ROUTE_HOME
    )
    data object StartPayment : NavDestination(
        route = RoutesConstants.ROUTE_START_PAYMENT
    )
    data object StartRefund : NavDestination(
        route = RoutesConstants.ROUTE_START_REFUND
    )
    data object GetPaymentMenu : NavDestination(
        route = RoutesConstants.ROUTE_GET_PAYMENT_MENU
    )
    data object GetPaymentById : NavDestination(
        route = RoutesConstants.ROUTE_GET_PAYMENT_ID
    )
    data object GetRefundById : NavDestination(
        route = RoutesConstants.ROUTE_GET_REFUND_ID
    )
    data object GetPaymentByAppClientId : NavDestination(
        route = RoutesConstants.ROUTE_GET_APP_CLIENT_ID
    )
    data object GetPayments : NavDestination(
        route = "${RoutesConstants.ROUTE_GET_PAYMENTS}/${RoutesConstants.PARAMS}"
    )
    data object GetPaymentsFilter : NavDestination(
        route = RoutesConstants.ROUTE_GET_PAYMENTS_FILTER
    )
    data object GetPaymentsToRefund : NavDestination(
        route = "${RoutesConstants.ROUTE_GET_PAYMENT_TO_REFUND}/${RoutesConstants.PARAMS}"
    )
    data object GetReports : NavDestination(
        route = "${RoutesConstants.ROUTE_GET_REPORTS}/${RoutesConstants.PARAMS}"
    )
    data object SyncData : NavDestination(
        route = RoutesConstants.ROUTE_SYNC_DATA
    )
    data object CheckInstalled : NavDestination(
        route = RoutesConstants.ROUTE_CHECK_INSTALLED_APP
    )
    data object GetTransactionsFilter : NavDestination(
        route = RoutesConstants.ROUTE_GET_TRANSACTIONS_FILTER
    )
    data object GetTransactions : NavDestination(
        route = "${RoutesConstants.ROUTE_GET_TRANSACTIONS}/${RoutesConstants.PARAMS}"
    )
    data object GetPaymentsToRefundFilter : NavDestination(
        route = RoutesConstants.ROUTE_GET_REFUNDS_FILTER
    )
    data object GetReportsFilter : NavDestination(
        route = RoutesConstants.ROUTE_GET_REPORTS_FILTER
    )

    data object GetAvailableServices : NavDestination(
        route = RoutesConstants.ROUTE_GET_AVAILABLE_SERVICES
    )
}

class GetPaymentsRoute(
    private var params: RouteParams.GetPayments,
) : IRoutes {
    override fun navigate(
        navController: NavController,
        builder: NavOptionsBuilder.() -> Unit,
    ) {
        navController.navigate(
            NavDestination.GetPayments.route
                .replace(RoutesConstants.PARAMS, params.toJson()),
            builder
        )
    }
}

class GetPaymentsToRefundRoute(
    private var params: RouteParams.GetPaymentsToRefund,
) : IRoutes {
    override fun navigate(
        navController: NavController,
        builder: NavOptionsBuilder.() -> Unit,
    ) {
        navController.navigate(
            NavDestination.GetPaymentsToRefund.route
                .replace(RoutesConstants.PARAMS, params.toJson()),
            builder
        )
    }
}

class GetTransactionsRoute(
    private var params: RouteParams.GetTransactions,
) : IRoutes {
    override fun navigate(
        navController: NavController,
        builder: NavOptionsBuilder.() -> Unit,
    ) {
        navController.navigate(
            NavDestination.GetTransactions.route
                .replace(RoutesConstants.PARAMS, params.toJson()),
            builder
        )
    }
}

class GetReportsRoute(
    private var params: RouteParams.GetReports,
) : IRoutes {
    override fun navigate(
        navController: NavController,
        builder: NavOptionsBuilder.() -> Unit,
    ) {
        navController.navigate(
            NavDestination.GetReports.route
                .replace(RoutesConstants.PARAMS, params.toJson()),
            builder
        )
    }
}