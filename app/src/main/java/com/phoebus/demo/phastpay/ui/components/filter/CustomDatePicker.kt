package com.phoebus.demo.phastpay.ui.components.filter

import android.annotation.SuppressLint
import android.os.Build
import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.CalendarSelectionMode
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


@SuppressLint("UnrememberedMutableState")
@Composable
fun CalendarPicker(
    calendarState: Boolean,
    closeFeedback: (Boolean) -> Unit,
    dateStart: Date,
    dateEnd: Date,
    dateStartResult: (Date) -> Unit,
    dateEndResult: (Date) -> Unit,
    calendarSelectionMode: MutableState<CalendarSelectionMode>,
) {
    val invalidDate: MutableState<Boolean?> = mutableStateOf(false)
    val context = LocalContext.current
    val selectedColor = MaterialTheme.colorScheme.primary.toArgb();

    if (calendarState) {
        Dialog(onDismissRequest = {
            closeFeedback(false)
        }) {
            Column(
                modifier = Modifier
                    .size(450.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                invalidDate.value?.let {
                    AnimatedVisibility(visible = it) {
                        Text(
                            text = stringResource(R.string.filter_invalid_date),
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                CalendarHeader(
                    startDate = dateStart,
                    finishDate = dateEnd,
                    isStartDateActive = calendarSelectionMode.value == CalendarSelectionMode.START,
                    onClickStartDate = {
                        calendarSelectionMode.value = CalendarSelectionMode.START
                    },
                    isFinishDateActive = calendarSelectionMode.value == CalendarSelectionMode.END,
                    onClickFinishDate = {
                        calendarSelectionMode.value = CalendarSelectionMode.END
                    })


                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(0.87f)
                        .align(alignment = Alignment.CenterHorizontally),
                    factory = {
                        CalendarView(context).apply {
                            setSelectedWeekBackgroundColor(selectedColor)
                        }
                    },
                    update = {
                        it.setOnDateChangeListener { _, year, mouth, day ->
                            when (calendarSelectionMode.value) {
                                CalendarSelectionMode.START -> {
                                    dateStartResult(parseDate(day, mouth, year))
                                }

                                CalendarSelectionMode.END -> {
                                    dateEndResult(parseDate(day, mouth, year))
                                }
                            }
                        }
                    }
                )

                CalendarFooter(
                    onCancel = { closeFeedback(false) },
                    onConfirm = {
                        if (dateEnd.before(dateStart)) {
                            invalidDate.value = true
                        } else {
                            closeFeedback(false)
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun CalendarHeader(
    startDate: Date,
    finishDate: Date,
    isStartDateActive: Boolean,
    isFinishDateActive: Boolean,
    onClickStartDate: () -> Unit,
    onClickFinishDate: () -> Unit,
) {
    Column {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp, bottom = 20.dp),
                ) {
                    Text(
                        stringResource(R.string.filter_select_date).uppercase(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .height(35.dp)
                    .width(125.dp),
                shape = RoundedCornerShape(50.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    2.dp, if (isStartDateActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                )
            ) {
                CalenderTextField(onClick = {
                    onClickStartDate()
                }, text = formatDate(startDate))
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Surface( //calendarPickerSurface
                modifier = Modifier
                    .height(35.dp)
                    .width(125.dp),
                shape = RoundedCornerShape(50.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    2.dp, if (isFinishDateActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                )
            ) {
                CalenderTextField(onClick = {
                    onClickFinishDate()
                }, text = formatDate(finishDate))
            }
        }
    }

}


@Composable
private fun CalendarFooter(onCancel: () -> Unit, onConfirm: () -> Unit) {
    Column() {
        HorizontalDivider(
            modifier = Modifier.height(1.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CalendarActionButton(
                title = stringResource(R.string.cancel),
                onClick = {
                    onCancel();
                }
            )
            VerticalDivider(
                modifier = Modifier
                    .padding(top = 13.dp)
                    .height(13.dp)
                    .width(1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
            CalendarActionButton(
                title = stringResource(R.string.confirm),
                onClick = {
                    onConfirm()
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
private fun CalendarActionButton(title: String, onClick: () -> Unit) {
    Button( //filterButton
        modifier = Modifier
            .width(140.dp)
            .height(35.dp),
        onClick = { onClick(); },
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Text(
            text = title.lowercase()
                .replaceFirstChar { it.uppercase() },
            fontWeight = FontWeight.Medium, fontSize = 15.sp, modifier = Modifier
        )
    }
}

@Composable
private fun CalenderTextField(text: String, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            enabled = true,
            onClick = {
                onClick()
            }
        ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Preview()
@Composable
private fun CalendarPickerPreview() {
    val calendarSelectionMode = remember { mutableStateOf(CalendarSelectionMode.START) }
    AppSmartDemoPhastPayTheme{
        CalendarPicker(
            calendarState = true,
            closeFeedback = {},
            dateStart = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
            dateEnd = Calendar.getInstance().time,
            dateStartResult = { date -> {} },
            dateEndResult = { date -> {} },
            calendarSelectionMode = calendarSelectionMode
        )
    }
}

fun formatDate(date: Date): String {
    val locale = Locale.getDefault()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", locale)
        val localDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(date.time), ZoneId.systemDefault())
        localDateTime.format(formatter)
    } else {
        val formatter = SimpleDateFormat("dd/MM/yyyy", locale)
        formatter.format(date)
    }
}

fun parseDateStart(day: Int, month: Int, year: Int): Date {
    val locale = Locale.getDefault()
    val calendar = Calendar.getInstance(locale)
    calendar.set(year, month, day, 0, 0, 0)

    return calendar.time
}

fun parseDate(day: Int, month: Int, year: Int): Date {
    val locale = Locale.getDefault()
    val calendar = Calendar.getInstance(locale)
    calendar.set(year, month, day)

    return calendar.time
}

