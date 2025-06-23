package com.phoebus.demo.phastpay.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.phoebus.demo.phastpay.R

@Composable
fun CheckboxPrint(
    printCustomerReceiptChecked: Boolean,
    printMerchantReceiptChecked: Boolean,
    onPrintCustomerReceiptChange: (Boolean) -> Unit,
    onPrintMerchantReceiptChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CheckboxWithLabel(
            checked = printCustomerReceiptChecked,
            onCheckedChange = onPrintCustomerReceiptChange,
            text = stringResource(R.string.print_customer_receipt)
        )
        CheckboxWithLabel(
            checked = printMerchantReceiptChecked,
            onCheckedChange = onPrintMerchantReceiptChange,
            text = stringResource(R.string.print_merchant_receipt)
        )
    }
}

@Composable
fun CheckboxWithLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = text)
    }
}