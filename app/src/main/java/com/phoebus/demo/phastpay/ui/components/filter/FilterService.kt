package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import com.phoebus.demo.phastpay.R

@Composable
fun FilterService(
    service: ServiceType,
    onServiceTypeSelected: (ServiceType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = buildList {
        add(
            FilterItem(
                label = stringResource(R.string.all_acquirers),
                value = ServiceType.ALL
            )
        )
        add(
            FilterItem(
                label = stringResource(R.string.mbway_acquirer),
                value = ServiceType.MBWAY
            )
        )


        add(
            FilterItem(
                label = stringResource(R.string.bizum_acquirer),
                value = ServiceType.BIZUM
            )
        )

    }


    val onSelectedItem: (FilterItem) -> Unit = { item ->
        onServiceTypeSelected(item.value as ServiceType)
    }

    val selectedItem = items.find { it.value == service }
    FilterBox(
        items,
        onSelectedItem = onSelectedItem,
        selectedItem = selectedItem,
        modifier = modifier.testTag("filter_provider_${service.name.lowercase()}")
    )
}


@Preview
@Composable
fun PaymentTypeComponentPreview() {
    var selectedTransactionProvider by remember { mutableStateOf(ServiceType.ALL) }
    AppSmartDemoPhastPayTheme {
        FilterService(
            service = selectedTransactionProvider,
            onServiceTypeSelected = { selectedTransactionProvider = it },
            modifier = Modifier
        )
    }
}
