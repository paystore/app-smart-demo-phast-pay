package com.phoebus.demo.phastpay.ui.features.getAvailableServices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.dto.ResourceServiceProvider
import com.phoebus.demo.phastpay.data.dto.ServiceDetail
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.data.enums.getStringRes
import com.phoebus.demo.phastpay.data.models.TipInformation
import com.phoebus.demo.phastpay.ui.components.dialogs.PhDialog
import com.phoebus.demo.phastpay.ui.components.info.ResumeInfo
import com.phoebus.demo.phastpay.ui.components.info.ResumeInfoList
import com.phoebus.demo.phastpay.ui.components.popup.AppToast
import com.phoebus.demo.phastpay.ui.components.progress.LoadingIndicator
import com.phoebus.demo.phastpay.ui.components.topbar.TopBar


@Composable
fun GetAvailableServicesScreen(
    navController: NavController,
    viewModel: GetAvailableServicesModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(GetAvailableServicesEvent.StartGetServices)
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is GetAvailableServicesEffect.ShowToast -> {
                    AppToast.show(context, effect.message, AppToast.Duration.SHORT)
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.get_available_services), navController)
        },
        content = {
            GetAvailableServicesContent(
                formState = state,
                modifier = Modifier.padding(it),
                onClickButton = { navController.popBackStack() }
            )
        }
    )


}

@Composable
fun GetAvailableServicesContent(
    formState: GetAvailableServicesState,
    modifier: Modifier,
    onClickButton: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            if (formState.loading) {
                LoadingIndicator(text = stringResource(R.string.getting_available_services))
            }

            val currentServices = formState.services
            if (currentServices != null) {
                val hasProviders = currentServices.serviceProviderResources?.isNotEmpty() == true

                if (currentServices.services.isNotEmpty()) {
                    ServicesAvailable(
                        currentServices.services,
                        showDialog = !hasProviders,
                        onClickButton
                    )
                }
                if (hasProviders) {
                    ServiceProviderTable(currentServices.serviceProviderResources)
                }
            }
        }

    }
}

@Composable
fun ServicesAvailable(
    services: List<Service>,
    showDialog: Boolean,
    onCloseDialog: () -> Unit = {}
) {
    val context = LocalContext.current
    val servicesNames = services.joinToString(separator = ", ") { service ->
        context.getString(service.getStringRes())
    }
    if (showDialog) {
        PhDialog(
            onDismissRequest = {},
            onConfirm = onCloseDialog,
            title = stringResource(R.string.get_available_services),
            message = servicesNames,
        )

    } else {
        val resumeServicesInfo = listOf(
            ResumeInfo(
                stringResource(R.string.service_info_provider_available),
                servicesNames
            )
        )

        ResumeInfoList(
            data = resumeServicesInfo,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }

}


@Composable
fun ServiceProviderTable(resources: List<ResourceServiceProvider>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        resources.forEach { resource ->
            item(key = "header_${resource.providerId}") {
                val resumeProvider = resumeProviderData(resource)
                ResumeInfoList(
                    title = stringResource(R.string.provider_info_provider_data),
                    data = resumeProvider,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            resource.tipInformation?.let { tip ->
                item(key = "tip_${resource.providerId}") {
                    val tipData = resumeTipData(tip)
                    ResumeInfoList(
                        data = tipData,
                        title = stringResource(R.string.provider_info_tip_title),
                        modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 16.dp)
                    )
                }
            }

            resource.services?.let { services ->
                items(
                    items = services,
                    key = { "${resource.providerId}_${it.service ?: it.hashCode()}" }
                ) { serviceItem ->
                    val serviceInfo = resumeServicesData(serviceItem)
                    ResumeInfoList(
                        data = serviceInfo,
                        title = stringResource(R.string.provider_info_enabled_services),
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 23.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun resumeTipData(data: TipInformation?): List<ResumeInfo> {
    val resumeInfo: MutableList<ResumeInfo> = ArrayList()

    if (data != null) {
        // Exemplo: Se houver uma lista de porcentagens (ex: 10, 15, 20)
        if (!data.percentages.isNullOrEmpty()) {
            resumeInfo.add(
                ResumeInfo(
                    stringResource(R.string.provider_info_tip_percentages),
                    data.percentages!!.joinToString(", ") { "$it%" }
                )
            )
        }

        // Exemplo: Valores Fixos (ex: 5, 10) -> "5, 10" (ou formate como moeda se preferir)
        if (!data.amounts.isNullOrEmpty()) {
            resumeInfo.add(
                ResumeInfo(
                    stringResource(R.string.provider_info_tip_amounts),
                    data.amounts!!.joinToString(", ")
                )
            )
        }
    }

    return resumeInfo
}


@Composable
fun resumeProviderData(data: ResourceServiceProvider?): List<ResumeInfo> {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val resumeInfo: MutableList<ResumeInfo> = ArrayList()
    if (data != null) {
        if (data.providerId != null) {
            resumeInfo.add(
                ResumeInfo(
                    title = stringResource(R.string.provider_info_id),
                    description = data.providerId,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(data.providerId))
                        val message = context.getString(
                            R.string.copied_text,
                            context.getString(R.string.provider_id)
                        )
                        AppToast.show(context, message, AppToast.Duration.SHORT)
                    }
                )
            )
        }
        if (data.providerName != null) {
            resumeInfo.add(
                ResumeInfo(
                    stringResource(R.string.provider_info_name),
                    data.providerName,
                    false
                )
            )
        }
    }
    return resumeInfo
}


@Composable
fun resumeServicesData(data: ServiceDetail?): List<ResumeInfo> {
    val resumeInfo: MutableList<ResumeInfo> = ArrayList()
    if (data != null) {
        if (data.service != null) {
            resumeInfo.add(
                ResumeInfo(
                    stringResource(R.string.provider_info_service_name),
                    stringResource(data.service.getStringRes())
                )
            )
        }

        if (data.displayMode != null) {
            resumeInfo.add(
                ResumeInfo(
                    stringResource(R.string.provider_info_display_mode),
                    data.displayMode.toString()
                )
            )
        }
        if (data.cobExpiration != null) {
            resumeInfo.add(
                ResumeInfo(
                    stringResource(R.string.provider_info_cob_expiration),
                    data.cobExpiration.toString().plus(" ms"),
                    false
                )
            )
        }
    }
    return resumeInfo
}
