package com.phoebus.demo.phastpay.ui.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.menu.MainMenu
import com.phoebus.demo.phastpay.ui.components.menu.MenuItem
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.features.isPhastPayInstalled.IsPhastPayInstalledScreen
import com.phoebus.demo.phastpay.ui.navigation.NavDestination
import com.phoebus.phastpay.sdk.client.PhastPayClient
import kotlinx.coroutines.delay
import kotlin.system.exitProcess


@Composable
fun HomeScreen(navController: NavController, phastPayClient: PhastPayClient) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.app_name), navController)
            },
            content = {
                if (phastPayClient.isPhastPayAppInstalled()) {
                    val menuItems = getMainItems(navController);
                    MainMenu(Modifier.padding(it), menuItems)
                } else {
                    IsPhastPayInstalledScreen(
                        navController, phastPayClient
                    )
                    LaunchedEffect(Unit) {
                        delay(30000)
                        exitProcess(0)
                    }
                }
            }
        )
    }
}

fun getMainItems(navController: NavController): List<MenuItem> = listOf(
    MenuItem(
        id = 1,
        text = R.string.start_payment,
        nav = { navController.navigate(NavDestination.StartPayment.route) }
    ),
    MenuItem(
        id = 2,
        text = R.string.get_list_transactions,
        nav = { navController.navigate(NavDestination.GetTransactionsFilter.route) }
    ),
    MenuItem(
        id = 3,
        text = R.string.get_payment,
        nav = { navController.navigate(NavDestination.GetPaymentMenu.route) }
    ),
    MenuItem(
        id = 4,
        text = R.string.get_list_payments,
        nav = { navController.navigate(NavDestination.GetPaymentsFilter.route) }
    ),
    MenuItem(
        id = 5,
        text = R.string.start_refund,
        nav = { navController.navigate(NavDestination.StartRefund.route) }
    ),
    MenuItem(
        id = 6,
        text = R.string.get_refund_id,
        nav = { navController.navigate(NavDestination.GetRefundById.route) }
    ),
    MenuItem(
        id = 7,
        text = R.string.get_refund,
        nav = { navController.navigate(NavDestination.GetPaymentsToRefundFilter.route) }
    ),
    MenuItem(
        id = 8,
        text = R.string.get_reports,
        nav = { navController.navigate(NavDestination.GetReportsFilter.route) }
    ),
    MenuItem(
        id = 9,
        text = R.string.sync_data,
        nav = { navController.navigate(NavDestination.SyncData.route) }
    ),
    MenuItem(
        id = 10,
        text = R.string.check_installed_app,
        nav = { navController.navigate(NavDestination.CheckInstalled.route) }
    ),
    MenuItem(
        id = 11,
        text = R.string.get_available_services,
        nav = { navController.navigate(NavDestination.GetAvailableServices.route) }
    )
)
