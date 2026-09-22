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
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.data.enums.getStringRes

@Composable
fun <T> ServiceSelector(
    items: List<T>,
    selectedItem: T,
    labelFor: @Composable (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    testTagPrefix: String = "filter_service"
) {
    val filterItems = items.map { item ->
        FilterItem(label = labelFor(item), value = item as Any)
    }
    val onSelectedFilterItem: (FilterItem) -> Unit = { item ->
        @Suppress("UNCHECKED_CAST")
        onItemSelected(item.value as T)
    }
    val selectedFilterItem = filterItems.find { it.value == selectedItem }

    FilterBox(
        filterItems,
        onSelectedItem = onSelectedFilterItem,
        selectedItem = selectedFilterItem,
        modifier = modifier.testTag("${testTagPrefix}_${selectedItem.toString().lowercase()}")
    )
}

@Composable
fun ServiceSelector(
    service: Service,
    onPhastTypeSelected: (Service) -> Unit,
    modifier: Modifier = Modifier,
    allowedServices: List<Service>? = null
) {
    ServiceSelector(
        items = allowedServices ?: Service.entries,
        selectedItem = service,
        labelFor = { stringResource(it.getStringRes()) },
        onItemSelected = onPhastTypeSelected,
        modifier = modifier
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
