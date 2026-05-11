package com.phoebus.demo.phastpay.ui.features.registerNotify

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.navigation.RoutesConstants
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme


@Composable
fun RegisterNotifyScreen(
    navController: NavController,
    viewModel: RegisterNotifyViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(isGranted)
        }
    )


    LaunchedEffect(viewModel.permissionRequestEvent) {
        viewModel.permissionRequestEvent.collect {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is RegisterNotifyNavigationEvents.NavigateToHome -> {
                    navController.navigate(RoutesConstants.ROUTE_HOME, builder = {
                        popUpTo(RoutesConstants.ROUTE_HOME) {
                            inclusive = true
                        }
                    })
                }
            }

        }
    }


    RegisterNotifyScreenContent(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )


}

@Composable
fun RegisterNotifyScreenContent(
    navController: NavController,
    state: RegisterNotifyState,
    onEvent: (RegisterNotifyEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.title_register_notify), navController)
        },
        content = {
            RegisterNotifyContent(
                formState = state,
                onFormEvent = onEvent,
                modifier = Modifier.padding(it)
            )
        }
    )
}

@Composable
fun RegisterNotifyContent(
    formState: RegisterNotifyState,
    onFormEvent: (RegisterNotifyEvent) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (formState.isRegistered) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = stringResource(R.string.text_register_notify),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                if (!formState.isRegistered) {
                    Button(
                        onClick = { onFormEvent(RegisterNotifyEvent.RegisterNotify) }
                    ) {
                        Text(text = "Register")
                    }
                }

                if (formState.isRegistered) {
                    Button(
                        onClick = { onFormEvent(RegisterNotifyEvent.UnRegisterNotify) }
                    ) {
                        Text(text = "UnRegister")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterNotifyScreenPreview() {
    AppSmartDemoPhastPayTheme {
        RegisterNotifyScreenContent(
            navController = rememberNavController(),
            state = RegisterNotifyState(
                successMessage = "Success message",
                errorMessage = "Error message",
                createPaymentMessage = "Create payment message",
                confirmedPaymentMessage = "Confirmed payment message",
                showReceiptMessage = "Show receipt message",
                pingMessage = "Ping message",
                isRegistered = true
            ),
            onEvent = {}
        )
    }
}
