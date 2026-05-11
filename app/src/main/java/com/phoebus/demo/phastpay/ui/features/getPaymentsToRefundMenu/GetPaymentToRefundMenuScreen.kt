package com.phoebus.demo.phastpay.ui.features.getPaymentsToRefundMenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.menu.MainMenu
import com.phoebus.demo.phastpay.ui.components.menu.MenuItem
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.GetPaymentsToRefundRoute
import com.phoebus.demo.phastpay.ui.navigation.NavDestination
import com.phoebus.demo.phastpay.ui.navigation.RouteParams


@Composable
fun GetPaymentToRefundMenuScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_refund_menu), navController)
        },
        content = {
            PaymentMenuContent(
                modifier = Modifier.padding(it),
                navController = navController
            )
        }
    )
}

@Composable
fun PaymentMenuContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(modifier = modifier.fillMaxSize()) {
        val menuItems = getMenuItems(navController);
        MainMenu(menuItems = menuItems)
    }

}


fun getMenuItems(navController: NavController): List<MenuItem> = listOf(
    MenuItem(
        1,
        R.string.get_refund_with_filter
    ) { navController.navigate(NavDestination.GetPaymentsToRefundFilter.route) },
    MenuItem(
        2,
        R.string.get_refund_without_filter
    ) { GetPaymentsToRefundRoute(params = RouteParams.GetPaymentsToRefund()).navigate(navController) }
)
