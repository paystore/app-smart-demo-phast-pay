package com.phoebus.demo.phastpay.ui.features.isPhastPayInstalled

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.components.progress.LoadingIndicator
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar
import com.phoebus.demo.phastpay.ui.theme.Green40
import com.phoebus.demo.phastpay.ui.theme.RedError
import com.phoebus.phastpay.sdk.client.PhastPayClient

@Composable
fun IsPhastPayInstalledScreen(
    navController: NavController,
    phastPayClient: PhastPayClient,
    viewModel: IsPhastPayInstalledViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(IsPhastPayInstalledEvent.StartCheck(phastPayClient))
    }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.app_name), navController)
        },
        content = {
            CheckAppPhastPayContent(
                modifier = Modifier.padding(it),
                formState = state
            )
        }
    )
}

@Composable
fun CheckAppPhastPayContent(
    modifier: Modifier = Modifier,
    formState: IsPhastPayInstalledState
) {
    if (formState.loading) {
        LoadingIndicator(text = stringResource(R.string.finding_list))
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = if (formState.isAppInstalled) Green40 else RedError),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (formState.isAppInstalled) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(150.dp),
                    tint = Color.White
                )
                Text(
                    text = stringResource(if (formState.isAppInstalled) R.string.is_installed else R.string.not_is_installed),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }

}

