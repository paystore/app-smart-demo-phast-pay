package com.phoebus.demo.phastpay.ui.components.receipt

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.demo.phastpay.ui.components.filter.statusToDisplayName
import com.phoebus.demo.phastpay.ui.components.payment.getCurrencyFormat
import com.phoebus.demo.phastpay.ui.theme.YellowLight
import com.phoebus.demo.phastpay.utils.CurrencyType
import com.phoebus.demo.phastpay.utils.DateUtils

@Composable
fun PhastItem(
    paymentId: String? = "",
    status: String? = "",
    value: String? = "",
    iva: String? = "",
    dateTime: String? = "",
    service: String? = "",
    appClientId: String? = null,
    currency: String? = null,
) {
    Surface(
        modifier = Modifier
            .padding(5.dp),
        color = YellowLight
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(
                text = "${stringResource(R.string.filter_status_title)}: ${
                    statusToDisplayName(
                        TransactionStatus.fromString(status ?: "")
                    )
                }",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "${stringResource(R.string.filter_value_title)}: ${
                    formatAmount(
                        value,
                        currency
                    )
                }",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "IVA: ${formatValue(iva)}",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "${stringResource(R.string.filter_service_title)}: ${
                    ServiceType.fromString(
                        service ?: ""
                    )
                }",
                style = MaterialTheme.typography.bodySmall,
            )
            if (paymentId != null) TextWithCopyIcon("paymentId", paymentId)
            if (appClientId != null) TextWithCopyIcon("appClientId", appClientId)
            val dateTime = DateUtils.formatDateStrUTCToStrLocal(dateTime ?: "")
            Text(
                text = "DATA E HORA:  $dateTime",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

fun formatAmount(value: String?, currency: String?): String {
    val currencyItem = getCurrencyFormat(
        CurrencyType.fromString(
            currency ?: ""
        )
    )
    val fValue = formatValue(value);
    return if (currencyItem.isPrefix) "${currencyItem.symbol} $fValue";
    else "$fValue ${currencyItem.symbol}"
}

fun formatValue(value: String?): String? {
    return value?.replace(".", ",")
}

@Composable
fun TextWithCopyIcon(
    type: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = modifier.clickable {
            clipboardManager.setText(AnnotatedString(text))
            Toast.makeText(context, "$type copiado", Toast.LENGTH_SHORT).show()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$type: $text",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
        )
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy",
            tint = Color.Gray,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(12.dp)
        )
    }
}

