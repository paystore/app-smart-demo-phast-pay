package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.getStringRes
import com.phoebus.demo.phastpay.ui.components.ServiceSelector
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme

@Composable
fun FilterService(
    service: ServiceType,
    onServiceTypeSelected: (ServiceType) -> Unit,
    modifier: Modifier = Modifier,
) {
    ServiceSelector(
        items = ServiceType.entries,
        selectedItem = service,
        labelFor = { stringResource(it.getStringRes()) },
        onItemSelected = onServiceTypeSelected,
        modifier = modifier,
        testTagPrefix = "filter_provider"
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