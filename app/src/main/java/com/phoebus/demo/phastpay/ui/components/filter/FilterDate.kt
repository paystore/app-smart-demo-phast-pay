package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.CalendarSelectionMode
import com.phoebus.demo.phastpay.ui.features.filter.FilterEvent
import com.phoebus.demo.phastpay.utils.getScreenWidth
import com.phoebus.demo.phastpay.utils.DateUtils

@Composable
fun FilterDate(
    visible: Boolean,
    startDate: String,
    endDate: String,
    onEvent: (FilterEvent) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(), // Slide de cima para baixo
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut() // Slide de baixo para cima
    ) {
        var showCalendar by remember { mutableStateOf(false) }
        val calendarSelectionMode = remember { mutableStateOf(CalendarSelectionMode.START) }

        OutlinedButton(
            onClick = { showCalendar = !showCalendar },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(getScreenWidth() * 0.65f)
                .height(30.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = String.format(
                    stringResource(R.string.filter_period_between_to),
                    startDate,
                    endDate
                ),
                maxLines = 1,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                color =  MaterialTheme.colorScheme.onBackground
            )
        }

        CalendarPicker(
            calendarState = showCalendar,
            closeFeedback = { showCalendar = it },
            dateStart = DateUtils.parseDate(startDate) ?: DateUtils.getCurrentDate(),
            dateEnd = DateUtils.parseDate(endDate) ?: DateUtils.getCurrentDate(),
            dateStartResult = { selectedDate ->
                onEvent(FilterEvent.UpdateStartDate(DateUtils.formatDate(selectedDate)))
            },
            dateEndResult = { selectedDate ->
                onEvent(FilterEvent.UpdateEndDate(DateUtils.formatDate(selectedDate)))
            },
            calendarSelectionMode = calendarSelectionMode
        )

    }
}