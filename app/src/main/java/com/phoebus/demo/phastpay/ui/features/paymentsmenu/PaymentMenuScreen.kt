package com.phoebus.demo.phastpay.ui.features.paymentsmenu

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
import com.phoebus.demo.phastpay.ui.navigation.NavDestination


@Composable
fun PaymentMenuScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_payment), navController)
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
        R.string.get_payment_by_id
    ) { navController.navigate(NavDestination.GetPaymentById.route) },
    MenuItem(
        2,
        R.string.get_payment_by_client_id
    ) { navController.navigate(NavDestination.GetPaymentByAppClientId.route) }
)
