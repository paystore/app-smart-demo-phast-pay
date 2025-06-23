package com.phoebus.demo.phastpay.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import com.phoebus.demo.phastpay.data.enums.Service

@Composable
fun ServiceSelector(
    service: Service,
    onPhastTypeSelected: (Service) -> Unit,
    modifier: Modifier = Modifier
) {

    val items = buildList {
        add(
            FilterItem(
                label = stringResource(R.string.mbway_acquirer),
                value = Service.MBWAY
            )
        )
        add(
            FilterItem(
                label = stringResource(R.string.bizum_acquirer),
                value = Service.BIZUM
            )
        )
    }

    val onSelectedItem: (FilterItem) -> Unit = { item ->
        onPhastTypeSelected(item.value as Service)
    }

    val selectedItem = items.find { it.value == service }
    FilterBox(
        items,
        onSelectedItem = onSelectedItem,
        selectedItem = selectedItem,
        modifier = modifier.testTag("filter_service_${service.name.lowercase()}")
    )
}


@Preview
@Composable
fun PaymentTypeComponentPreview() {
    var selectedService by remember { mutableStateOf(Service.MBWAY) }
    AppSmartDemoPhastPayTheme {
        ServiceSelector(
            service = selectedService,
            onPhastTypeSelected = { selectedService = it },
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun PaymentTypeComponentPreviewDark() {
    var selectedService by remember { mutableStateOf(Service.BIZUM) }
    AppSmartDemoPhastPayTheme(darkTheme = true) {
        ServiceSelector(
            service = selectedService,
            onPhastTypeSelected = { selectedService = it },
            modifier = Modifier
        )
    }
}

