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
import androidx.compose.material3.HorizontalDivider
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

data class RefundsItem(
    val refundId: String? = "",
    val valor: String? = "",
    val status: String? = "",
    val dateTime: String? = "",
    val iva: String? = ""
)

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
    refunds: List<RefundsItem>? = null
) {
    Surface(
        modifier = Modifier
            .padding(5.dp),
        color = YellowLight
    ) {
        val dateTime = DateUtils.formatDateStrUTCToStrLocal(dateTime ?: "")
        val transactionStatus = statusToDisplayName(
            TransactionStatus.fromString(status ?: "")
        )
        val paymentValue = formatAmount(
            value,
            currency
        )
        val serviceName = ServiceType.fromString(
            service ?: ""
        )
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            TextItem(
                text = "${stringResource(R.string.filter_status_title)}: $transactionStatus"
            )
            TextItem(
                text = "${stringResource(R.string.filter_value_title)}: $paymentValue"
            )
            TextItem(text = "${stringResource(R.string.iva_label)}: ${formatValue(iva)}")
            TextItem(
                text = "${stringResource(R.string.filter_service_title)}: $serviceName"
            )
            paymentId?.let {
                TextWithCopyIcon("paymentId", it)
            }
            appClientId?.let {
                TextWithCopyIcon("appClientId", it)
            }
            TextItem(text = "${stringResource(R.string.date_label)}:  $dateTime")
            if (!refunds.isNullOrEmpty()) {
                TextItem(text = "${stringResource(R.string.refunds_label)}: ")
                PrintRefunds(refunds, currency)
            }
        }
    }
}

@Composable
private fun TextItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun PrintRefunds(refunds: List<RefundsItem>?, currency: String?) {
    if (!refunds.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            refunds.forEach { refund ->
                    val dateTime = DateUtils.formatDateStrUTCToStrLocal(refund.dateTime ?: "")
                    val refundValue = formatAmount(
                        refund.valor,
                        currency
                    )
                    val refundStatus = statusToDisplayName(
                        TransactionStatus.fromString(refund.status ?: "")
                    )
                    HorizontalDivider()
                    refund.refundId?.let {
                        TextWithCopyIcon("refundId", it)
                    }
                    TextItem(
                        text = "${stringResource(R.string.filter_value_title)}: $refundValue",
                    )
                    TextItem(
                        text = "${stringResource(R.string.filter_status_title)}: $refundStatus",
                    )
                    TextItem(
                        text = "${stringResource(R.string.iva_label)}: ${formatValue(refund.iva)}",
                    )
                    TextItem(
                        text = "${stringResource(R.string.date_label)}:  $dateTime",
                    )
            }
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
    val copiedText = stringResource(R.string.copied_text);

    Row(
        modifier = modifier.clickable {
            clipboardManager.setText(AnnotatedString(text))
            Toast.makeText(context, String.format(copiedText, type ), Toast.LENGTH_SHORT).show()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextItem(text = "$type: $text")
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

