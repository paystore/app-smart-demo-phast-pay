package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.FilterType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus
import com.phoebus.demo.phastpay.ui.components.button.PhButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterTransactionStatus(
    filterType: FilterType,
    listStatus: List<TransactionStatus> = TransactionStatus.entries,
    statusSelected: List<TransactionStatus>,
    onStatusSelected: (List<TransactionStatus>) -> Unit
) {
    AnimatedVisibility(
        visible = filterType == FilterType.GET_PAYMENTS,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        // Estado interno mutável da lista de selecionados
        val selectedStatuses = remember(statusSelected) {
            mutableStateListOf(*statusSelected.toTypedArray())
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.filter_status_title).uppercase(),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listStatus.forEach { status ->
                    val isSelected = status in selectedStatuses
                    PhButton(
                        title = statusToDisplayName(status),
                        onClick = {
                            if (isSelected) {
                                selectedStatuses.remove(status)
                            } else {
                                selectedStatuses.add(status)
                            }
                            onStatusSelected(selectedStatuses.toList())
                        },
                        enabled = true,
                        colors = if (isSelected) {
                            ButtonDefaults.elevatedButtonColors()
                        } else {
                            ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun statusToDisplayName(status: TransactionStatus): String {
    return when (status) {
        TransactionStatus.REQUEST_PAYMENT -> stringResource(R.string.filter_status_request_payment)
        TransactionStatus.WAITING_PAYMENT -> stringResource(R.string.filter_status_waiting_payment)
        TransactionStatus.CONFIRMED_PAYMENT -> stringResource(R.string.filter_status_confirmed_payment)
        TransactionStatus.CANCELED_PAYMENT -> stringResource(R.string.filter_status_canceled_payment)
        TransactionStatus.EXPIRED_PAYMENT -> stringResource(R.string.filter_status_expired_payment)
        TransactionStatus.ERROR_PAYMENT -> stringResource(R.string.filter_status_error_payment)
        TransactionStatus.REQUEST_REFUND -> stringResource(R.string.filter_status_request_refund)
        TransactionStatus.PARTIAL_REFUND -> stringResource(R.string.filter_status_partial_refund)
        TransactionStatus.COMPLETED_REFUND -> stringResource(R.string.filter_status_completed_refund)
        TransactionStatus.REFUNDED -> stringResource(R.string.filter_status_refunded)
        TransactionStatus.UNKNOWN -> ""
    }
}

