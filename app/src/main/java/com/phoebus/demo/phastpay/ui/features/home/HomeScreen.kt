package com.phoebus.demo.phastpay.ui.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.menu.MainMenu
import com.phoebus.demo.phastpay.ui.components.menu.MenuItem
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.NavDestination
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.app_name), navController)
            },
            content = {
                val menuItems = getMainItems(navController)
                MainMenu(Modifier.padding(it), menuItems)
            }
        )
    }
}

fun getMainItems(navController: NavController): List<MenuItem> = listOf(
    MenuItem(
        id = 1,
        text = R.string.start_payment_menu,
        nav = { navController.navigate(NavDestination.StartPaymentMenu.route) }
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
        text = R.string.get_refund_menu,
        nav = { navController.navigate(NavDestination.GetPaymentsToRefundMenu.route) }
    ),
    MenuItem(
        id = 8,
        text = R.string.get_reports_menu,
        nav = { navController.navigate(NavDestination.GetReportsMenu.route) }
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
    ),
    MenuItem(
        id = 12,
        text = R.string.get_qrcode,
        nav = { navController.navigate(NavDestination.GetQrCode.route) }
    ),
    MenuItem(
        id = 13,
        text = R.string.register_notify,
        nav = { navController.navigate(NavDestination.RegisterNotify.route) }
    ),
    MenuItem(
        id = 14,
        text = R.string.abort_payment,
        nav = { navController.navigate(NavDestination.AbortPayment.route) }
    ),
    MenuItem(
        id = 15,
        text = R.string.print_receipt,
        nav = { navController.navigate(NavDestination.PrintReceipt.route) }
    )
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppSmartDemoPhastPayTheme {
        HomeScreen(navController = rememberNavController())
    }
}