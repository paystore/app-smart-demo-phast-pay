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
import com.phoebus.demo.phastpay.data.dto.PhastPayProviderData
import com.phoebus.demo.phastpay.data.dto.PhastPayRefundProviderData
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.demo.phastpay.data.enums.getResId
import com.phoebus.demo.phastpay.ui.components.payment.getCurrencyFormat
import com.phoebus.demo.phastpay.ui.theme.YellowLight
import com.phoebus.demo.phastpay.utils.CurrencyType
import com.phoebus.demo.phastpay.utils.DateUtils

data class RefundsItem(
    val refundId: String? = "",
    val valor: String? = "",
    val status: String? = "",
    val dateTime: String? = "",
    val iva: String? = "",
    val providerData: PhastPayRefundProviderData? = null
)

@Composable
fun PhastItem(
    paymentId: String? = "",
    status: String? = "",
    value: String? = "",
    additionalValue: String? = null,
    iva: String? = null,
    dateTime: String? = "",
    service: String? = "",
    appClientId: String? = null,
    currency: String? = null,
    refunds: List<RefundsItem>? = null,
    providerData: PhastPayProviderData? = null
) {
    val serviceName = ServiceType.fromString(service ?: "")

    Surface(
        modifier = Modifier.padding(5.dp),
        color = YellowLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            PaymentSummary(
                status = status,
                value = value,
                additionalValue = additionalValue,
                iva = iva,
                currency = currency,
                serviceName = serviceName,
                paymentId = paymentId,
                appClientId = appClientId,
                dateTime = dateTime
            )

            if (providerData != null) {
                when (serviceName) {
                    ServiceType.CRYPTO -> CryptoProviderInfo(providerData, currency)
                    ServiceType.PIX -> PixAmountInfo(providerData.amountBrl, providerData.rate)
                    else -> Unit
                }
            }

            if (!refunds.isNullOrEmpty()) {
                TextItem(text = "${stringResource(R.string.refunds_label)}: ")
                PrintRefunds(refunds, serviceName, currency)
            }
        }
    }
}

@Composable
private fun PaymentSummary(
    status: String?,
    value: String?,
    additionalValue: String?,
    iva: String?,
    currency: String?,
    serviceName: ServiceType,
    paymentId: String?,
    appClientId: String?,
    dateTime: String?
) {
    val transactionStatus = stringResource(TransactionStatus.fromString(status ?: "").getResId())
    val dateTimeValue = DateUtils.formatDateStrUTCToStrLocal(dateTime ?: "")

    TextItem(text = "${stringResource(R.string.filter_status_title)}: $transactionStatus")
    TextItem(text = "${stringResource(R.string.filter_value_title)}: ${formatAmount(value, currency)}")
    additionalValue?.let {
        TextItem(text = "${stringResource(R.string.filter_additional_value_title)}: ${formatAmount(it, currency)}")
    }
    iva?.let {
        TextItem(text = "${stringResource(R.string.iva_label)}: ${formatAmount(it, currency)}")
    }
    TextItem(text = "${stringResource(R.string.filter_service_title)}: $serviceName")
    paymentId?.let { TextWithCopyIcon(stringResource(R.string.label_payment_id), it) }
    appClientId?.let { TextWithCopyIcon(stringResource(R.string.label_app_client_id), it) }
    TextItem(text = "${stringResource(R.string.date_label)}:  $dateTimeValue")
}

@Composable
private fun CryptoProviderInfo(providerData: PhastPayProviderData, currency: String?) {
    providerData.leftToPayAmountValue?.let {
        TextItem(text = stringResource(R.string.crypto_left_to_pay, formatAmount(it, currency)))
    }
    providerData.cryptoAmount?.let {
        TextItem(text = stringResource(R.string.crypto_amount, it, providerData.cryptoCurrencyCode ?: ""))
    }
    providerData.network?.let {
        TextItem(text = stringResource(R.string.crypto_network, it))
    }
    providerData.processingFeeFiatAmount?.let {
        TextItem(text = stringResource(R.string.crypto_processing_fee, formatAmount(it, currency)))
    }
    providerData.transactionHash?.let {
        TextWithCopyIcon(stringResource(R.string.label_transaction_hash), it)
    }
}

@Composable
private fun PixAmountInfo(amountBrl: String?, rate: String?) {
    amountBrl?.takeIf { it.isNotBlank() }?.let {
        TextItem(text = stringResource(R.string.pix_amount_brl, formatAmount(it, "BRL")))
    }
    rate?.takeIf { it.isNotBlank() }?.let {
        TextItem(text = stringResource(R.string.pix_rate, formatAmount(it, "BRL")))
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
private fun PrintRefunds(refunds: List<RefundsItem>?, serviceType: ServiceType, currency: String?) {
    if (refunds.isNullOrEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        refunds.forEach { refund ->
            RefundDetails(refund, serviceType, currency)
        }
    }
}

@Composable
private fun RefundDetails(refund: RefundsItem, serviceType: ServiceType, currency: String?) {
    val dateTime = DateUtils.formatDateStrUTCToStrLocal(refund.dateTime ?: "")
    val refundStatus = stringResource(TransactionStatus.fromString(refund.status ?: "").getResId())

    HorizontalDivider()
    refund.refundId?.let { TextWithCopyIcon(stringResource(R.string.label_refund_id), it) }
    TextItem(text = "${stringResource(R.string.filter_value_title)}: ${formatAmount(refund.valor, currency)}")
    TextItem(text = "${stringResource(R.string.filter_status_title)}: $refundStatus")
    TextItem(text = "${stringResource(R.string.iva_label)}: ${formatValue(refund.iva)}")
    TextItem(text = "${stringResource(R.string.date_label)}:  $dateTime")

    if (serviceType == ServiceType.PIX) {
        refund.providerData?.let {
            PixAmountInfo(it.amountRefundedBrl, it.rateRefunded)
        }
    }
}

fun formatAmount(value: String?, currency: String?): String {
    val currencyItem = getCurrencyFormat(CurrencyType.fromString(currency ?: ""))
    val fValue = formatValue(value)
    return if (currencyItem.isPrefix) "${currencyItem.symbol} $fValue" else "$fValue ${currencyItem.symbol}"
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
    val copiedText = stringResource(R.string.copied_text)
    val copyIconDescription = stringResource(R.string.copy_icon_description)

    Row(
        modifier = modifier.clickable {
            clipboardManager.setText(AnnotatedString(text))
            Toast.makeText(context, String.format(copiedText, type), Toast.LENGTH_SHORT).show()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextItem(text = "$type: $text")
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = copyIconDescription,
            tint = Color.Gray,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(12.dp)
        )
    }
}