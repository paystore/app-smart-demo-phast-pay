package com.phoebus.demo.phastpay.ui.features.report.reportMenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.menu.MainMenu
import com.phoebus.demo.phastpay.ui.components.menu.MenuItem
import com.phoebus.demo.phastpay.ui.components.progress.LoadingIndicator
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.NavDestination

@Composable
fun ReportsMenuScreen(
    navController: NavController,
    viewModel: ReportsMenuViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_reports), navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                ReportsMenuContent(
                    navController = navController,
                    onFilterClick = { viewModel.onEvent(ReportsMenuEvent.FilterReports) }
                )

                if (state.isLoading) {
                    LoadingIndicator()
                }

                state.successMessage?.let {
                    PhDialog(
                        title = stringResource(R.string.success),
                        message = stringResource(R.string.get_report_screen_success_message),
                        onConfirm = { viewModel.onEvent(ReportsMenuEvent.ClearMessages) },
                        onDismissRequest = { viewModel.onEvent(ReportsMenuEvent.ClearMessages) }
                    )
                }

                state.errorMessage?.let {
                    PhDialog(
                        title = stringResource(R.string.error),
                        message = it,
                        onConfirm = { viewModel.onEvent(ReportsMenuEvent.ClearMessages) },
                        onDismissRequest = { viewModel.onEvent(ReportsMenuEvent.ClearMessages) }
                    )
                }
            }
        }
    )
}

@Composable
fun ReportsMenuContent(
    navController: NavController,
    onFilterClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val menuItems = listOf(
            MenuItem(
                id = 1,
                text = R.string.report_with_filter
            ) { navController.navigate(NavDestination.GetReportsFilter.route) },
            MenuItem(
                id = 2,
                text = R.string.report_without_filter
            ) { onFilterClick() }
        )
        MainMenu(menuItems = menuItems)
    }
}
