package com.phoebus.demo.phastpay.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.phoebus.demo.phastpay.data.enums.FilterType
import com.phoebus.demo.phastpay.utils.JsonUtils
import com.phoebus.demo.phastpay.ui.features.isPhastPayInstalled.IsPhastPayInstalledScreen
import com.phoebus.demo.phastpay.ui.features.getPaymentByAppClientId.GetPaymentByAppClientIdScreen
import com.phoebus.demo.phastpay.ui.features.getPaymentById.GetPaymentByIdScreen
import com.phoebus.demo.phastpay.ui.features.getRefundById.GetRefundByIdScreen
import com.phoebus.demo.phastpay.ui.features.getPayments.GetPaymentsScreen
import com.phoebus.demo.phastpay.ui.features.getPaymentsToRefund.GetPaymentsToRefundScreen
import com.phoebus.demo.phastpay.ui.features.getTransactions.GetTransactionsScreen
import com.phoebus.demo.phastpay.ui.features.getReports.GetReportsScreen
import com.phoebus.demo.phastpay.ui.features.paymentsmenu.PaymentMenuScreen
import com.phoebus.demo.phastpay.ui.features.filter.FilterScreen
import com.phoebus.demo.phastpay.ui.features.getAvailableServices.GetAvailableServicesScreen
import com.phoebus.demo.phastpay.ui.features.home.HomeScreen
import com.phoebus.demo.phastpay.ui.features.startPayment.PaymentScreen
import com.phoebus.demo.phastpay.ui.features.startRefund.StartRefundScreen
import com.phoebus.demo.phastpay.ui.features.sync.SyncDataScreen
import com.phoebus.phastpay.sdk.client.PhastPayClient

@Composable
fun DemoNavHost(navController: NavHostController, phastPayClient: PhastPayClient) {
    NavHost(
        startDestination = NavDestination.Home.route,
        navController = navController,
        enterTransition = { this.slideInTo(AnimatedContentTransitionScope.SlideDirection.Right) },
        exitTransition = { this.slideOutTo(AnimatedContentTransitionScope.SlideDirection.Left) }
    ) {
        composable(
            route = NavDestination.Home.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = NavDestination.StartPayment.route
        ) {
            PaymentScreen(navController)
        }
        composable(
            route = NavDestination.StartRefund.route
        ) {
            StartRefundScreen(navController)
        }
        composable(
            route = NavDestination.GetPaymentMenu.route
        ) {
            PaymentMenuScreen(navController)
        }
        composable(
            route = NavDestination.GetPaymentById.route
        ) {
            GetPaymentByIdScreen(navController)
        }
        composable(
            route = NavDestination.GetRefundById.route
        ) {
            GetRefundByIdScreen(navController)
        }
        composable(
            route = NavDestination.GetPaymentByAppClientId.route
        ) {
            GetPaymentByAppClientIdScreen(navController)
        }
        composable(
            route = NavDestination.GetPaymentsFilter.route,

            ) {
            FilterScreen(navController, filterType = FilterType.GET_PAYMENTS)
        }

        composable(
            route = NavDestination.GetPaymentsToRefundFilter.route,

            ) {
            FilterScreen(navController, filterType = FilterType.GET_PAYMENTS_TO_REFUND)
        }

        composable(
            route = NavDestination.GetPayments.route,
            arguments = listOf(
                navArgument(RoutesConstants.ARGS_PARAMS) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args =
                backStackEntry.arguments?.getString(RoutesConstants.ARGS_PARAMS)
            requireNotNull(args)
            val params =
                JsonUtils.json.decodeFromString<RouteParams.GetPayments>(args)

            GetPaymentsScreen(navController, params)

        }
        composable(
            route = NavDestination.GetPaymentsToRefund.route,
            arguments = listOf(
                navArgument(RoutesConstants.ARGS_PARAMS) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args =
                backStackEntry.arguments?.getString(RoutesConstants.ARGS_PARAMS)
            requireNotNull(args)
            val params =
                JsonUtils.json.decodeFromString<RouteParams.GetPaymentsToRefund>(args)

            GetPaymentsToRefundScreen(navController, params)
        }
        composable(
            route = NavDestination.GetReports.route,
            arguments = listOf(
                navArgument(RoutesConstants.ARGS_PARAMS) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args =
                backStackEntry.arguments?.getString(RoutesConstants.ARGS_PARAMS)
            requireNotNull(args)
            val params =
                JsonUtils.json.decodeFromString<RouteParams.GetReports>(args)

            GetReportsScreen(navController, params, phastPayClient)
        }
        composable(route = NavDestination.SyncData.route) {
            SyncDataScreen(navController)
        }
        composable(route = NavDestination.CheckInstalled.route) {
            IsPhastPayInstalledScreen(navController)
        }
        composable(
            route = NavDestination.GetTransactionsFilter.route,

            ) {
            FilterScreen(navController, filterType = FilterType.GET_TRANSACTIONS)
        }
        composable(
            route = NavDestination.GetTransactions.route,
            arguments = listOf(
                navArgument(RoutesConstants.ARGS_PARAMS) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args =
                backStackEntry.arguments?.getString(RoutesConstants.ARGS_PARAMS)
            requireNotNull(args)
            val params =
                JsonUtils.json.decodeFromString<RouteParams.GetTransactions>(args)

            GetTransactionsScreen(navController, params)
        }
        composable(
            route = NavDestination.GetReportsFilter.route
        ) {
            FilterScreen(navController, filterType = FilterType.REPORT)
        }

        composable(
            route = NavDestination.GetAvailableServices.route
        ) {
            GetAvailableServicesScreen(navController)
        }

    }

}