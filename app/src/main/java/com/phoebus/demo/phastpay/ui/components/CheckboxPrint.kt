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
import androidx.compose.ui.unit.sp
import com.phoebus.demo.phastpay.R

@Composable
fun CheckboxPrint(
    printCustomerReceiptChecked: Boolean,
    printMerchantReceiptChecked: Boolean,
    previewCustomerReceiptChecked: Boolean,
    previewMerchantReceiptChecked: Boolean,
    onPrintCustomerReceiptChange: (Boolean) -> Unit,
    onPrintMerchantReceiptChange: (Boolean) -> Unit,
    onPreviewCustomerReceiptChange: (Boolean) -> Unit,
    onPreviewMerchantReceiptChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CheckboxWithLabel(
                checked = printCustomerReceiptChecked,
                onCheckedChange = onPrintCustomerReceiptChange,
                text = stringResource(R.string.print_customer_receipt),
                modifier = Modifier.weight(1f)
            )
            CheckboxWithLabel(
                checked = printMerchantReceiptChecked,
                onCheckedChange = onPrintMerchantReceiptChange,
                text = stringResource(R.string.print_merchant_receipt),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CheckboxWithLabel(
                checked = previewCustomerReceiptChecked,
                onCheckedChange = onPreviewCustomerReceiptChange,
                text = stringResource(R.string.preview_customer_receipt),
                modifier = Modifier.weight(1f)
            )
            CheckboxWithLabel(
                checked = previewMerchantReceiptChecked,
                onCheckedChange = onPreviewMerchantReceiptChange,
                text = stringResource(R.string.preview_merchant_receipt),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CheckboxWithLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
}