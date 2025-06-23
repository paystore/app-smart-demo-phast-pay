package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.DatePeriod
import com.phoebus.demo.phastpay.ui.features.filter.FilterEvent


@Composable
fun FilterTime(
    startTime: String,
    endTime: String,
    periodSelected: DatePeriod,
    onEvent: (FilterEvent) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    var isEditingStartTime by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = periodSelected == DatePeriod.TODAY || periodSelected == DatePeriod.OTHER_PERIOD,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(), // Slide de cima para baixo
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut() // Slide de baixo para cima
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ClockButton(
                onClickAction = {
                    isEditingStartTime = true
                    showPicker = true
                },
                text = startTime
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.filter_period_to),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            ClockButton(
                onClickAction = {
                    isEditingStartTime = false
                    showPicker = true
                },
                text = endTime
            )
        }

        if (showPicker) {
            CustomTimePicker(
                initialTime = if (isEditingStartTime) startTime else endTime,
                showState = showPicker,
                onCancel = { showPicker = false },
                onConfirm = { _, formattedTime ->
                    if (isEditingStartTime) {
                        onEvent(FilterEvent.UpdateStartTime(formattedTime))
                    } else {
                        if (isEndTimeValid(startTime, formattedTime)) {
                            onEvent(FilterEvent.UpdateEndTime(formattedTime))
                        }
                    }
                    showPicker = false
                }
            )
        }
    }
}

fun isEndTimeValid(startTime: String, endTime: String): Boolean {
    val startParts = startTime.split(":").map { it.toInt() }
    val endParts = endTime.split(":").map { it.toInt() }

    val startMinutes = startParts[0] * 60 + startParts[1]
    val endMinutes = endParts[0] * 60 + endParts[1]

    return endMinutes > startMinutes
}

